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
public class MayaJobInitialize {
	private static final Log log = LogFactory.getLog(RndrJobInitialize.class);
	
	private Unit unit;
	
	private ArrayList<Frame> frames;
		
	private String renderDir;
	private String mayafile;
	public MayaJobInitialize(ArrayList<Frame> frames,String renderDir,String mayafile){
		this.frames=frames;
		this.renderDir=renderDir;
		this.mayafile=mayafile;
		
	}
	public void dobegin() throws Exception {

		String scriptName=UUIDGenerator.getUUID()+".pbs";
		
		String content="";
		content="#PBS -q batch\n" +
				"#PBS -l nodes=1:ppn=12\n" +
				"/usr/autodesk/maya2012-x64/bin/Render";
		
		
	/*	content+="-s"+frames.get(0).getFrameName()+"-e "+frames.get(frames.size()-1).getFrameName()
				+" -rd "+renderDir+"/Pictures/"+"-proj"+renderDir+"/"+mayafile;*/
		
			content+=" -s"+frames.get(0).getFrameName()+" -e "+frames.get(frames.size()-1).getFrameName()
					+" -rd "+renderDir+"/Pictures/"+" -proj"+renderDir+"/"+mayafile;
	
		
		
		LinuxInvoker ink = new LinuxInvoker();
		ink.setCmd("echo -e '"+content+"'>"+scriptName);
		ink.executeCommand();
		
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext-server.xml");					
		ClusterManageService clusterManageService=(ClusterManageService)ctx.getBean("clusterManageService");
		
		String stdout=clusterManageService.submit(scriptName);
		
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
		unit=new Unit();
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
