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

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.backstage.distribute.JobDistribute;
import org.xjtu.framework.core.base.constant.JobStatus;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.modules.user.dao.JobDao;
import org.xjtu.framework.modules.user.service.JobService;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

public class JobSchedule implements Runnable  {

//	public static String path_render=null;
//	public static String frame_range=null;
//	public static String render_id=null;
	public static int flagg=0;

	public static List<Job> jobs_render=new ArrayList<Job>();
	private @Resource JobDao jobDao;
	public  static Job jobtemp;
	public  static String temp_job_cameraName="0";
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
			Job job=jobService.findHeadQueuingJob();//得到排队中的作业
			
	        while(job!=null){
	            try {
//	            	if (job.getCameraName().equals(temp_job_cameraName)){
					if(false){

						log.info("有重复提交的任务！需要跳过！重复任务的名字："+temp_job_cameraName);
//						job=null;
						log.info("job的cameraName="+job.getCameraName());
						log.info("job的运行状态="+job.getJobStatus());
						job.setJobStatus(JobStatus.distributed);
						jobDao.updateJob(job);
						log.info("经过状态修改后，job的运行状态="+job.getJobStatus());
					}else{


//						path_render=job.getFilePath();
//						frame_range=job.getFrameRange();
//						render_id=job.getId();


						boolean flag=jobDistribute.distributeJob(job);

						if(flag==false){
							log.info("flag="+flag+",分配任务失败！");
						}else{

							log.info("当前渲染作业预渲染标志位为："+job.getPreRenderingTag());

							if(job.getPreRenderingTag()==1) {
								log.info("当前job的名字是：" + job.getCameraName());
								jobtemp = job;
								flagg = flagg + 1;
								log.info("有新的渲染任务被提交，flag的值=" + flagg);

								log.info("flag=" + flag + ",分配任务成功！，将要执行预渲染任务！！！");
								jobs_render.add(job);
								log.info("执行代码\tjobs_render.add(job);，当前的jobs_render的大小是：" + jobs_render.size());
								temp_job_cameraName = job.getCameraName();

							}
						}



					}


				} catch (Exception e) {
					e.printStackTrace();
				}
	            job=jobService.findHeadQueuingJob();
			}
	        isRunning = false;
		}else{    
	           log.info("last scheduling not finished!");
		}
	}

}

