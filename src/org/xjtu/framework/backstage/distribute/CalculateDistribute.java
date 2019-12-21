package org.xjtu.framework.backstage.distribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xjtu.framework.backstage.initialize.MayaJobInitialize;
import org.xjtu.framework.core.base.constant.CalculateStatus;
import org.xjtu.framework.core.base.constant.FrameStatus;
import org.xjtu.framework.core.base.constant.JobStatus;
import org.xjtu.framework.core.base.constant.SystemConfig;
import org.xjtu.framework.core.base.constant.TaskStatus;
import org.xjtu.framework.core.base.constant.UnitStatus;
import org.xjtu.framework.core.base.model.Calculate;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.core.util.PbsExecute;
import org.xjtu.framework.core.util.StringUtil;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.modules.user.dao.CalculateDao;
import org.xjtu.framework.modules.user.dao.JobDao;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.ConfigurationService;
import org.xjtu.framework.modules.user.service.FrameService;
import org.xjtu.framework.modules.user.service.JobService;
import org.xjtu.framework.modules.user.service.TaskService;

import com.xj.framework.ssh.ShellLocal;

public class CalculateDistribute {
	
	private static Log log = LogFactory.getLog(CalculateDistribute.class);
	private @Resource CalculateDao calculateDao;
	private @Resource JobService jobService;
	private @Resource TaskService taskService;
	private @Resource FrameService frameService;
	private @Resource ClusterManageService clusterManageService;
	private @Resource ConfigurationService configurationService;
	private @Resource SystemConfig systemConfig;
	private int nodesNumPerUnit;
	private String pbsFilePath=this.getClass().getResource("/").getPath()+"shell/render_building_rUnit.pbs";
	public boolean distributeCalculate(Calculate calculate) throws Exception {
		log.info("distribute calculate is starting"); 
			String filePath = calculate.getXmlFilePath();
			int NodesNumPerUnit=configurationService.findAllConfiguration().getNodesNumPerUnit();
			String fuWuListName=configurationService.findAllConfiguration().getFuWuListName();
			String sceneMem=configurationService.findAllConfiguration().getSceneMemory();
			String hostStack=configurationService.findAllConfiguration().getHostStack();
			String shareMen=configurationService.findAllConfiguration().getShareSize();
			String stdout="";
				if(systemConfig.getJobManageService().equals("openPBS")){	
					stdout = clusterManageService.submit(pbsFilePath);
				}else{
					 SystemConfig systemConfig=new SystemConfig();
		    		String renderingWorkDir=systemConfig.getRenderingWorkDir();
		    		//bsub -b -I -q q_sw_share -n 64 -cgsp 64 -host_stack 256 -share_size 7000 /home/export/online1/systest/swsdu/xijiao/Users/xjtu/calculator1/sw5.hybrid
					//bsub -b -I -q q_sw_share -n 216 -cgsp 64 -host_stack 2048 -share_size 4096 /home/export/online1/systest/swsdu/xijiao/Users/xjtu/calculator11111/sw5.hybrid
					//String renderInstruct="/home/export/online1/systest/swsdu/xijiao/Users/xjtu/calculator11111/sw5.hybrid";
		    		//String Cmd="cd /home/export/online1/systest/swsdu/xijiao/Users/xjtu/calculator11111";
		    		
		    		String renderInstruct=filePath+"/sw5.hybrid";// /home/export/online1/systest/swsdu/xijiao/Users/xjtu/calculator11111/sw5.hybrid
		    		String Cmd="cd "+ filePath;//   cd /home/export/online1/systest/swsdu/xijiao/Users/xjtu/calculator11111
					String shenweiPbsCommand="";
						//shenweiPbsCommand="-q q_sw_share -I -b -share_size 7168 -n 31 "+ renderInstruct+" "+filePath+" / |tee result-frame-n21";

					//old version Instruct
//					shenweiPbsCommand="-q "+fuWuListName+" -o /home/export/online1/systest/swsdu/xijiao/Outprint/"+calculate.getId()+".out"+ " -b -share_size 7168 -n 64 -host_stack 256 "+ renderInstruct;

					//Wang Xiong 2019.11.21 test :new version Instruct
					shenweiPbsCommand="-b -I -q "+fuWuListName+" -n 216 -cgsp 64 -host_stack 2048 -share_size 4096 "+renderInstruct;


					log.info("已经设置解析任务状态");
					PbsExecute  pbs=new PbsExecute(Cmd+" && bsub "+shenweiPbsCommand);
					log.info("已经运行完PbsExecute");
					System.out.println("bsu comand is"+" bsub "+shenweiPbsCommand);

					//PbsExecute  pbs=new PbsExecute("bsub "+shenweiPbsCommand);
					     stdout=pbs.executeCmd().trim();
					log.info("当前的运行指令是： stdout=pbs.executeCmd().trim();");
					     log.info(" stdout的内容是 "+stdout);
					     if(stdout!=null&&stdout.length()>0){
							 log.info("即将运行指令是：stdout=stdout.substring(stdout.indexOf('<')+1,stdout.indexOf('>')");
//							 stdout=stdout.substring(stdout.indexOf('<')+1,stdout.indexOf('>'));
							 log.info("stdout的长度="+stdout.length());
							 log.info("已经运行完上一条指令！");
						 }
				}
				log.info("Initializing Rendering Unit..."+stdout);

				calculate.setXmlStatus(CalculateStatus.distributed);

				calculateDao.updateCalculate(calculate);

			    log.info("distibute is finish=====================>");
			    return true;
		}

}
//
//
// 19:12:01
//19:13:29
//19:13:30,
