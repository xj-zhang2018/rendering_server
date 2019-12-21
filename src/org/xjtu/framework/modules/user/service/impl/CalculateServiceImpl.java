package org.xjtu.framework.modules.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.core.base.constant.CalculateStatus;
import org.xjtu.framework.core.base.constant.FrameStatus;
import org.xjtu.framework.core.base.constant.JobPriority;
import org.xjtu.framework.core.base.constant.JobStatus;
import org.xjtu.framework.core.base.constant.MessageState;
import org.xjtu.framework.core.base.constant.PaymentStatus;
import org.xjtu.framework.core.base.constant.TaskStatus;
import org.xjtu.framework.core.base.constant.UnitStatus;
import org.xjtu.framework.core.base.model.Calculate;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Message;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.RenderEngine;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.modules.user.dao.CalculateDao;
import org.xjtu.framework.modules.user.dao.MessageDao;
import org.xjtu.framework.modules.user.service.CalculateService;
import org.xjtu.framework.modules.user.service.JobService;
import org.xjtu.framework.modules.user.service.ProjectService;
import org.xjtu.framework.modules.user.service.RenderEngineService;

import com.xj.framework.ssh.ShellLocal;

@Service("calculateService")
public class CalculateServiceImpl implements CalculateService {

	private @Resource CalculateDao calculateDao;
	private static Log log = LogFactory.getLog(CalculateServiceImpl.class);
	
	private @Resource MessageDao messageDao;
	private ServletContext context = null;
	
	
	@Override
	public List<Calculate> listCalculatesByQuery(String searchText,String searchType, int pageNum, int pageSize) {
		List<Calculate> calculates = new ArrayList<Calculate>();
		if(StringUtils.isBlank(searchText)){
			calculates = calculateDao.pagnate(pageNum, pageSize);
		}else if(StringUtils.isBlank(searchType)){
			calculates = calculateDao.pagnate(pageNum, pageSize);
		}else{
			if(searchType.equals("name")){
				calculates = calculateDao.pagnateByCalculateName(searchText, pageNum, pageSize);
			}else if(searchType.equals("accurateUserId")){
				calculates = calculateDao.pagnateByAccurateUserId(searchText, pageNum, pageSize);
			}else{
				calculates = calculateDao.pagnate(pageNum, pageSize);
			}
		}
		return calculates;
	}

	@Override
	public int findTotalCountByQuery(String searchText, String searchType) {
		int num = 0;
		if(StringUtils.isBlank(searchText)){
			num = calculateDao.queryCount();
		}else if(StringUtils.isBlank(searchType)){
			num = calculateDao.queryCount();
		}else{
			if(searchType.equals("name")){
				num = calculateDao.queryCountByCalculateName(searchText);
			}else if(searchType.equals("accurateUserId")){
				num = calculateDao.queryCountByAccurateUserId(searchText);
			}else{
				num = calculateDao.queryCount();
			}
		}
		return num;
	}

	@Override
	public Calculate findCalculateById(String calculateId) {
		return calculateDao.queryCalculateById(calculateId);
	}

	@Override
	public void deleteCalculate(Calculate calculate) {
		calculateDao.removeCalculate(calculate);
	}

	@Override
	public void addCalculate(Calculate c) {
		calculateDao.persist(c);
	}

	@Override
	public Calculate findCalculateByCalculateNameAndUserId(String param,String id) {
		return calculateDao.queryCalculateByCalculateNameAndUserId(param,id);
	}

	@Override
	public List<Calculate> findCalculates() {
		return calculateDao.queryCalculates();
	}

	@Override
	public void doCopyCalculates(List<String> xmlIds) {
		for(int i=0;i<xmlIds.size();i++){
			Calculate calculate=calculateDao.queryCalculateById(xmlIds.get(i));
			if(calculate!=null){
				Calculate c=new Calculate();
				int num;
				num=calculateDao.queryCountcopyCalculateName(calculate.getXmlName());
				c.setId(UUIDGenerator.getUUID());
				c.setXmlName(calculate.getXmlName()+"-副本"+num);
				c.setXmlCreateTime(new Date());
				c.setXmlStatus(0);
				c.setXmlProgress(0);
				c.setXmlPriority(0);
				c.setXmlFilePath(calculate.getXmlFilePath());
				User user=calculate.getUser();
				c.setUser(user);
				Project project=calculate.getProject();
				c.setProject(project);
				calculateDao.persist(c);
			}
		}
	}


	
	@Override
	public Calculate findHeadQueuingCalculate() {
		return calculateDao.queryHeadQueuingJob();
		
	}

	@Override
	public void updateCalculateProgress() {
		HandalMessage2();
	}

		public void HandalMessage2(){
			log.info("update calculate process start");
			String filePath="";
			String logPath="";
			String ProcessInfo="";
			List<Calculate> calculates=calculateDao.queryDistributedCalculate();
			log.info("calculates.size()="+calculates.size());
			log.info("我进不来!");
			for(int j=0; j<calculates.size(); j++){
				log.info("HAHAcalculates.size()="+calculates.size());
				log.info("我进来啦!");





				Calculate calculate=calculates.get(j);
				filePath=calculate.getXmlFilePath();

				String homeDir=filePath;
				ShellLocal shell=new ShellLocal("ls "+homeDir);
				String result=shell.executeCmd();
				result=result.trim();


				String[]filesName=result.split("\n");
				for(int i=0;i<filesName.length;i++){
					log.info("存在文件--》"+filesName[i]+"  \n"+"  ");
				}

				logPath=filePath+"/log.txt";

				log.info("update Calculate log path is"+logPath);
				
				String GetProgressCmd="tail -1 "+logPath;
				ShellLocal executor=new ShellLocal(GetProgressCmd);
				String line=executor.executeCmd();
				ProcessInfo=GetRation(line);

				log.info("update Calculate ratio1 当前进度是： is "+ProcessInfo);


				if(ProcessInfo!=null&&ProcessInfo.length()>0){
					int ratio=(int)Double.parseDouble(ProcessInfo);
					System.out.println("update Calculate ratio is "+ratio);
					calculate.setXmlProgress(ratio);

					if(ratio==100){
						System.out.println("系统已完成作业");
						Message message=new Message();
						message.setId(UUIDGenerator.getUUID());
						message.setCreateTime(new Date());
						message.setContents("用户"+calculate.getProject().getUser().getName()+"完成解算任务"+calculate.getXmlName()+"的解算");
						message.setTitle("解算完成情况");
						message.setState(MessageState.UNCHECKED);
						message.setUser(calculate.getProject().getUser());
						messageDao.addMessage(message);
						calculate.setXmlStatus(CalculateStatus.finished);
					    calculateDao.updateCalculate(calculate);

						System.out.println("开始添加新渲染作业");
					    //新提交一个渲染作业，设计一个函数，新增一个结算触发的渲染作业
					    newAddcalculate_Renderjob(calculate);
					    
					}
				}else 
					return;
			}
			}
		
		
		public void newAddcalculate_Renderjob(Calculate calculate) {
			System.out.println("new finished Calculate is new add jobs");
			WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		//	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
			
			
			RenderEngineService renderEngineService=(RenderEngineService)ctx.getBean("renderEngineService");
					
				
			ProjectService projectService=(ProjectService) ctx.getBean("projectService");
					
			JobService jobService=(JobService) ctx.getBean("jobService");
			
			Job j=new Job();
			j.setId(UUIDGenerator.getUUID());
			j.setCameraName(calculate.getXmlName()+"_RenderJob");
			j.setCreateTime(new Date());
			j.setJobStatus(JobStatus.inQueue);
			j.setQueueNum(-1);
			j.setCameraProgress(0);
			j.setJobPriority(JobPriority.medium);	
			
			j.setFrameRange("1-7");//解算任务默认渲染全部		
			j.setFrameNumbers(7);		//需要根据情况变化
			
			//j.setFilePath(calculate.getXmlFilePath());		//   徐晓峰给的规固定路径  								
			j.setFilePath("/home/export/online1/systest/swsdu/xijiao/Users/xjtu/RenderJob_calculator");
			
			j.setRenderCost(0.0);
			j.setPreRenderingTag(0);
			Project p =calculate.getProject();
			p.setCamerasNum(p.getCamerasNum()+1);
			projectService.updateProjectInfo(p);
			String renderEngineId="e45c905c33eb4bfd9e42403d2566abad";
			RenderEngine re=renderEngineService.findRenderEnginebyId(renderEngineId);
				
			
				j.setRenderEngine(re);
				j.setUnitsNumber(2);//change this becoming coreNmber
				j.setProject(p);
			    jobService.addJob(j);
				System.out.println("new finished Calculate is add Fininsh");
		}
			
	
				public  String GetRation(String logLastLine) {
					String rato="";
					if(logLastLine==null||logLastLine.length()==0)return null;
					String ss[]=logLastLine.split(" ");
					String tmp[]=null;
					if(ss.length==4)
					{
						rato= ss[2];
						return rato.substring(0, rato.indexOf("%"));
					}
					return null;
				}

				
				@Override
				public void doStartCalculate(List<String> xmlIds) {
					for(int i=0;i<xmlIds.size();i++){
						Calculate calculate=calculateDao.queryCalculateById(xmlIds.get(i));
						if(calculate!=null){
							if(calculate.getXmlStatus()==CalculateStatus.inQueue||calculate.getXmlStatus()==CalculateStatus.distributed){
								continue;
							}
							else{
								calculate.setXmlStatus(CalculateStatus.inQueue);
								calculateDao.updateCalculate(calculate);
							}
						}

					}
					
				}

				@Override
				public void doStopCalculate(String xmlId) {
					Calculate calculate=calculateDao.queryCalculateById(xmlId);
					calculate.setXmlProgress(0);
					calculate.setXmlCreateTime(new Date());
					calculate.setXmlStatus(CalculateStatus.notStart);
					calculateDao.updateCalculate(calculate);
				}

				@Override
				public void updateCalculateInfo(Calculate calculate) {
					calculateDao.updateCalculate(calculate);
				}
}
