


package org.xjtu.framework.backstage.schedule;

import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.core.base.constant.FrameStatus;
import org.xjtu.framework.core.base.constant.TaskStatus;
import org.xjtu.framework.core.base.constant.UnitStatus;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.modules.user.service.FrameService;
import org.xjtu.framework.modules.user.service.JobService;
import org.xjtu.framework.modules.user.service.TaskService;
import org.xjtu.framework.modules.user.service.UnitService;

import java.util.List;

public class NodeLoadBalancing implements Runnable  {
		
	private static final Logger log = Logger.getLogger(NodeLoadBalancing.class);
	private ServletContext context = null;

	private static boolean isRunning = false;
	
	public NodeLoadBalancing(ServletContext context){
		this.context = context;
	}
	
	public void run(){
		log.info("NodeLoadBalancing progress");

		if (!isRunning) {    
            isRunning = true;
            
        /*    try{            	
            	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);			
    			UnitService unitService=(UnitService)ctx.getBean("unitService");  
    			JobService jobService=(JobService)ctx.getBean("jobService");  
    			TaskService taskService=(TaskService)ctx.getBean("taskService");  
    			FrameService frameService=(FrameService)ctx.getBean("frameService"); 
    			
    			List<Unit> units =unitService.findUnitsByStatus(UnitStatus.available);	
    			if(units!=null && units.size()!=0){	
    				for(int m = 0; m<units.size() ; m++){
    					Unit unitBalacing = unitService.findUnitById(units.get(m).getId());//追加任务的渲染单元
    					int unitIdleNum = unitBalacing.getIdleNumber();
    					if(unitIdleNum > 0){
    						Task taskBalacing=taskService.findTasksByUnitId(unitBalacing.getId()).get(0);
    						List<Frame> frames=frameService.findByFrameStatusAndTaskId(FrameStatus.notStart, taskBalacing.getId());    						
    						Job existedJob=taskBalacing.getJob();
    						int notStartFramesNum =0;
    						if (frames!=null && frames.size()>0){
    							notStartFramesNum=frames.size();
    						}
    						if(notStartFramesNum==0){
    							jobService.unitBalaing( existedJob.getId() ,  taskBalacing.getId())	;	
    							 frames=frameService.findByFrameStatusAndTaskId(FrameStatus.notStart, taskBalacing.getId());
    							 if (frames!=null && frames.size()>0){
    								 notStartFramesNum = frames.size();		
    							 }   											 										    													
    						}
    						if(notStartFramesNum>0){
    							jobService.taskadding( existedJob.getId() ,  taskBalacing.getId());
    						}
    					} 				    					   					
    				}
				}
    			frameService.updateTaskAndJobProgress();
	
    			log.info("NodeLoadBalancing progress try");
            	
            }catch(Exception ex){
            	log.info("NodeLoadBalancing progress exception");
            	ex.printStackTrace();
            }finally{
            	isRunning = false;
            } */           
            
		}else{    
	           log.info("last NodeLoadBalancing progress not finished");
		}
	              
	}

}






