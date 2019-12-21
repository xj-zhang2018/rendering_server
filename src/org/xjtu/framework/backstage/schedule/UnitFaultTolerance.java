package org.xjtu.framework.backstage.schedule;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.core.base.constant.UnitStatus;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.core.util.PbsExecute;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.JobService;
import org.xjtu.framework.modules.user.service.TaskService;
import org.xjtu.framework.modules.user.service.UnitService;

public class UnitFaultTolerance implements Runnable {
		
	private static final Log log = LogFactory.getLog(UnitFaultTolerance.class);
	private ServletContext context = null;
	public UnitFaultTolerance(ServletContext context){
		this.context = context;
	}
	public void run(){
         log.info("start Unit timer task..."); //开始单元容错任务    
         try {
			//单元容错
        	log.info("begin to test fault");
        	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
    		UnitService unitService=(UnitService)ctx.getBean("unitService");
    		JobService jobService=(JobService)ctx.getBean("jobService");
    		TaskService taskService=(TaskService)ctx.getBean("taskService");
    		ClusterManageService clusterManageService=(ClusterManageService)ctx.getBean("clusterManageService");
    		//取忙碌的渲染单元
    		//List<Unit> units=unitService.findNotDeadUnits();
    		List<Task> tasks = taskService.findTasksHasProgress();
    		//24号新编写的
    		for(int i=0; i< tasks.size();i++){
    				//List<Task> tasks = taskService.findTasksHasProgress();
    				Task task=tasks.get(i);   			
    					//先暂停在该单元上执行的镜头,等待重新调度
    					if(tasks.get(0).getUpdateTime()!=null){
	    					long temp=(new Date().getTime()- tasks.get(0).getUpdateTime().getTime());//相差毫秒数
	    					long temp2 = temp % (1000 * 3600);
	    					int  mins= (int)(temp2 / 1000 / 60); //相差分钟数
	    					
	    					if ( mins>30)
	    					{
	    						
	    						task.setTaskProgress(100);
	    						taskService.updateTaskInfo(task);
	    						Unit existedUnit=task.getUnit();
	    						if(existedUnit!=null&&existedUnit.getUnitStatus()!=UnitStatus.dead){
	    							existedUnit.setUnitStatus(UnitStatus.dead);
	    							try {
	    								clusterManageService.dismissUnit(existedUnit.getPbsId());
	    								log.info("wanglei killed unit");
	    							} catch (Exception e) {
	    								e.printStackTrace();
	    							}
			        				unitService.updateUnitInfo(existedUnit);   				
			        				Thread.sleep(10000);
	    						
	    						}
	    					}
    					}
    		}
    					
    	/*	for(int i=0; i< units.size();i++){
    			
    			Unit unit=units.get(i);
	  			
    			//判断该渲染单元是否出错
    			boolean isFault = clusterManageService.isFault(unit.getPbsId());
    			
    			//如果出错
    			if(isFault){
    				
    				//先等10秒钟,再取一次
					Thread.sleep(10000);
					Unit existedUnit=unitService.findUnitById(unit.getId());
					
					if(existedUnit!=null&&existedUnit.getUnitStatus()!=UnitStatus.dead){
						log.info("The unit " + unit.getId()+" on ShenweiPbs has problem!");
						
						List<Task> tasks=taskService.findTasksByUnitId(unit.getId());
	    				
	    				if(tasks!=null){
	    					//先暂停在该单元上执行的镜头,等待重新调度
	    					String jobId=tasks.get(0).getJob().getId();
	    					jobService.suspendJobByJobId(jobId);
	    					
	    					//等待作业管理系统将作业killing完毕
	    					Thread.sleep(30000);
	    					
	    					//再继续该镜头作业
	    					List<String> jobIds=new ArrayList<String>();
	    					jobIds.add(jobId);
	    					jobService.doStartCameraRender(jobIds);
	    				}else{
	        				unit.setUnitStatus(UnitStatus.dead);
	        				unitService.updateUnitInfo(unit);
	    				}
	    				
	    				break;
					}
    				
    			}else{
    			    			
					String cmd="grep signo "+System.getProperty("user.dir")+"/"+unit.getId()+".out";
						
					PbsExecute pbs=new PbsExecute(cmd);
					String result=pbs.executeCmd();
				        
					if(result!=null){
						log.info("unit "+unit.getId()+" jobout signo=11");
						
						Thread.sleep(1000);
	    				Unit existedUnit=unitService.findUnitById(unit.getId());
	    				
	    				if(existedUnit!=null&&existedUnit.getUnitStatus()!=UnitStatus.dead){
		    				log.info("The unit " + unit.getId()+" has problem");
		    				
		    				List<Task> tasks=taskService.findTasksByUnitId(unit.getId());
		    				
		    				if(tasks!=null){
		    					//先暂停在该单元上执行的镜头,等待重新调度
		    					String jobId=tasks.get(0).getJob().getId();
		    					jobService.suspendJobByJobId(jobId);
		    					
		    					//等待作业管理系统将作业killing完毕
		    					Thread.sleep(40000);
		    					
		    					//再继续该镜头作业
		    					List<String> jobIds=new ArrayList<String>();
		    					jobIds.add(jobId);
		    					jobService.doStartCameraRender(jobIds);
		    				}else{
		        				unit.setUnitStatus(UnitStatus.dead);
		        				unitService.updateUnitInfo(unit);
		    				}
		    				
		    				break;
		    			}
					}
    			}

    		}
        	           	
			log.info("test fault is over");
	
		*/} catch (Exception e) {
			log.error("ERROR_GET_CONNECT_STRING_IOEXCEPTION");
			e.printStackTrace();
		}
         
        log.info("Unit timer task completed..."); //任务完成    

	}

}
