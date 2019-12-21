package org.xjtu.framework.backstage.schedule;

import javax.servlet.ServletContext;
import org.apache.log4j.Logger;

public class ProgressInquire implements Runnable {
	private static final Logger log = Logger.getLogger(ProgressInquire.class);
	private ServletContext context = null;
	
	public ProgressInquire(ServletContext context){
		this.context = context;
	}
	
	public void run(){
		log.info("ProgressInquire run()");                

		/*ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);	
		JobService jobService=(JobService)ctx.getBean("jobService");
		FrameService frameService=(FrameService)ctx.getBean("frameService");       
		
		List<Job> jobs=jobService.findDistributedJob();
		//查找不是rUnit渲染引擎的正在运行的jobs
		int i=0;
		boolean isHaveRUNIT=false;
		while(i<jobs.size()){
			isHaveRUNIT=false;
			Job job =jobs.get(i);
			if (job.getRenderEngine().getName()=="RWing"){
				jobs.remove(jobs.get(i));	
				isHaveRUNIT=true;
			}
			if(isHaveRUNIT==false){
				i++;
			}
		}
					
        if(jobs!=null){
        	log.info("find progress!");	  				
				
            try {
            	frameService.UpdateProgressMessage(jobs);
			} catch (Exception e) {
				e.printStackTrace();
			}
				           		
		}else{
			log.info("No new other renderEngine distributed job!");
		}*/
	              
	}

}
