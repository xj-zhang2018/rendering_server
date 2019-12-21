package org.xjtu.framework.backstage.schedule;

import java.util.List;

import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.modules.user.service.CalculateService;
import org.xjtu.framework.modules.user.service.FrameService;
import org.xjtu.framework.modules.user.service.JobService;

public class CalculateUpdate implements Runnable  {
		
	private static final Logger log = Logger.getLogger(CalculateUpdate.class);
	private ServletContext context = null;

	private static boolean isRunning = false;
	private static boolean flag=true;
	
	public CalculateUpdate(ServletContext context){
		this.context = context;
	}
	public void run(){
		if (!isRunning) {    
            isRunning = true;
            try{      
	            	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	            	CalculateService calculateService=(CalculateService)ctx.getBean("calculateService");
	            	calculateService.updateCalculateProgress();
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
