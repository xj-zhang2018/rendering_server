/*package org.xjtu.framework.backstage.schedule;

import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.backstage.distribute.JobDistribute;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.modules.user.service.JobService;

public class JobSchedule implements Runnable  {
		
	private static final Logger log = Logger.getLogger(JobSchedule.class);
	private ServletContext context = null;
	
	private static boolean isRunning = false;
	
	public JobSchedule(ServletContext context){
		this.context = context;
	}
	
	public void run(){
		log.info("run()");                

		if (!isRunning) {    
            isRunning = true;
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);			
			JobService jobService=(JobService)ctx.getBean("jobService");
			JobDistribute jobDistribute=(JobDistribute)ctx.getBean("jobDistribute");        
			
			Job job=jobService.findHeadQueuingJob();
						
	        if(job!=null){
	        	log.info("start distribution!");	  				
					
	            try {
					jobDistribute.distributeJob(job);
				} catch (Exception e) {
					e.printStackTrace();
				}
					           		
			}else{
				log.info("No new job!");
			}
	        isRunning = false;
		}else{    
	           log.info("last scheduling not finished!");
		}        
	}

}*/

package org.xjtu.framework.backstage.schedule;

import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.backstage.distribute.CalculateDistribute;
import org.xjtu.framework.backstage.distribute.JobDistribute;
import org.xjtu.framework.core.base.model.Calculate;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.modules.user.service.CalculateService;
import org.xjtu.framework.modules.user.service.JobService;

public class CalculateSchedule implements Runnable  {
		
	private static final Logger log = Logger.getLogger(CalculateSchedule.class);
	private ServletContext context = null;
	private static boolean isRunning = false;
	public CalculateSchedule(ServletContext context){
		this.context = context;
	}
	
	public void run(){
		log.info("run()");                
		
		if (!isRunning) {   
		
            isRunning = true;
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);			
			CalculateService calculateService=(CalculateService)ctx.getBean("calculateService");
			CalculateDistribute calculateDistribute=(CalculateDistribute)ctx.getBean("calculateDistribute");        

			Calculate calculate=calculateService.findHeadQueuingCalculate();//得到排队中的作业

	        while(calculate!=null){
	            try {
	            	calculateDistribute.distributeCalculate(calculate);
				} catch (Exception e) {
					e.printStackTrace();
				}
	            calculate=calculateService.findHeadQueuingCalculate();
			}
	        isRunning = false;
		}else{    
	           log.info("last scheduling not finished!");
		}
	}

}

