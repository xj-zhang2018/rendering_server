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
import org.xjtu.framework.backstage.distribute.JobDistribute;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.modules.user.service.JobService;

import com.xj.framework.ssh.ShellLocal;

public class DecompreeReciedFileSchedule implements Runnable  {
		
	private static final Logger log = Logger.getLogger(DecompreeReciedFileSchedule.class);
	private ServletContext context = null;
	private static boolean isRunning = false;
	public DecompreeReciedFileSchedule(ServletContext context){
		this.context = context;
	}
	
	public void run(){
		log.info("run()");                
		
		if (!isRunning) {   
		
            isRunning = true;
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);			
			JobService jobService=(JobService)ctx.getBean("jobService");
			        
			Job job=jobService.FindNotStartJob();
			
	        while(job!=null){
	            try {
					String filePath=job.getFilePath();
	            	
           	log.info("decompression ZipCmd is"+"cd "+filePath+" && unzip mydata.zip");
					String decompressionZipCmd="cd "+filePath+" && unzip mydata.zip";
	     		ShellLocal shell=new ShellLocal(decompressionZipCmd);
	     		shell.executeCmd();
	            	
				} catch (Exception e) {
					e.printStackTrace();
				}
	            job=jobService.FindNotStartJob();
			}
	        isRunning = false;
		}else{    
	           log.info("last scheduling not finished!");
		}
	}

}

