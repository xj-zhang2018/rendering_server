package org.xjtu.framework.backstage.initialize;


import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xjtu.framework.core.base.constant.UnitStatus;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.core.util.LinuxInvoker;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.UnitService;


public class RndrJobInitialize{
		
	private static final Log log = LogFactory.getLog(RndrJobInitialize.class);
		
	private Unit unit;
	
	private ArrayList<Frame> frames;
		
	private String renderDir;
	
	private static int taskID=0;
	
	private String pbsFilePath=this.getClass().getResource("/").getPath()+"shell/render_building_rUnit.pbs";
	
	
	
	
	public RndrJobInitialize(ArrayList<Frame> frames,String renderDir){
		this.frames=frames;
		this.renderDir=renderDir;
		taskID++;
	}
	
	
	public void dobegin() throws Exception {

		String scriptName="renderSubTask"+taskID+"_"+UUIDGenerator.getUUID()+".pbs";
		
		String content="";
		content="#!/bin/sh\n" +
		        "#PBS -N renderSubTask_"+taskID+"\n" +
		        "#PBS -o /home/pbsuser1/pbs_workspce/my.output\n" +
		        "#PBS -e /home/pbsuser1/pbs_workspce/my.error\n" +
		        "#PBS -l nodes=1:ppn=3\n" +
		        "#PBS -q batch\n" +
		        "cd /home/pbsuser1\n"+"/home/pbsuser1/render.sh ";
		for(int i=0;i<frames.size();i++){
			if(i!=frames.size()-1)
			content+=renderDir+"/"+frames.get(i).getFrameName()+",";
			else content+=renderDir+"/"+frames.get(i).getFrameName();
		}
		log.info("pbsScript  content is=================>"+content);
		LinuxInvoker ink = new LinuxInvoker();
		ink.setCmd("echo -e '"+content+"'>"+scriptName);
		ink.executeCommand();
	
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext-server.xml");					
		ClusterManageService clusterManageService=(ClusterManageService)ctx.getBean("clusterManageService");
		String stdout=clusterManageService.submit(scriptName);
		//临时替换掉
		//String stdout=clusterManageService.submit(pbsFilePath);
		while (true){
			if(!clusterManageService.isReady(stdout)){				
				log.info("RenderingUnit is not ready.");
				Thread.sleep(100);
			}
			else{
				log.info("RenderingUnit is ready.");
				break;
			}
		}
		UnitService unitService=(UnitService)ctx.getBean("unitService");
		//every task is one unit ,every task receive som xml
		unit=new Unit();
		log.info("set unit start ===========================>");
		unit.setId(UUIDGenerator.getUUID());
		unit.setPbsId(stdout);
		unit.setUnitStatus(UnitStatus.busy);
		unit.setUnitNodesNum(1);
		unit.setUnitMasterName(null);
		unit.setUnitNodesInfo(clusterManageService.getNodeString(stdout));
		unitService.addUnit(unit);

	}


	public Unit getUnit() {
		return unit;
	}


	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
}
