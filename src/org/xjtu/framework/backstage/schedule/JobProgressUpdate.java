package org.xjtu.framework.backstage.schedule;

import java.util.List;

import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.modules.user.service.FrameService;
import org.xjtu.framework.modules.user.service.JobService;

public class JobProgressUpdate implements Runnable  {
		
	private static final Logger log = Logger.getLogger(JobProgressUpdate.class);
	private ServletContext context = null;

	private static boolean isRunning = false;
	private static boolean flag=true;
	
	public JobProgressUpdate(ServletContext context){
		this.context = context;
	}
	
	public void run(){
		if (!isRunning) {    
            isRunning = true;
            try{      
	            	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	            	FrameService frameService=(FrameService)ctx.getBean("frameService");
	            	JobService  jobService=(JobService)ctx.getBean("jobService");
	            //	List<Job> jobs=(jobService).findDistributedJob();
	            	
	            	/*if(jobs.size()>320 && flag==true){
	            		for(int j=0; j<jobs.size()/2; j++){		
		        	         frameService.updateTaskAndJobProgress(jobs.get(j) );	
		        		}
	            		flag=false;
	            	}
	            	else if(jobs.size()>320 && flag==false){
	            		for(int j=jobs.size()/2; j<jobs.size(); j++){		
		        	         frameService.updateTaskAndJobProgress(jobs.get(j) );	
		        		}
	            		flag=true;
	            	}
	            	else if(jobs.size()<=320){
	            		for(int j=0; j<jobs.size(); j++){		
		        	         frameService.updateTaskAndJobProgress(jobs.get(j) );	
		        		}
	            	}*/
	        		frameService.updateTaskAndJobProgress( );	
            }catch(Exception ex){
            	log.info("update progress exception");
            	ex.printStackTrace();
            }finally{
            	isRunning = false;
            }            
            
		}else{    
	           log.info("last update progress not finished");
		}
	              
	}
	

}
