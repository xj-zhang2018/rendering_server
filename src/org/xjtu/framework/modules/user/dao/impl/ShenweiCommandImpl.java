package org.xjtu.framework.modules.user.dao.impl;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.xjtu.framework.core.util.PbsExecute;
import org.xjtu.framework.modules.user.dao.ClusterManageCommand;
import org.xjtu.framework.modules.user.dao.ConfigurationDao;
import org.xjtu.framework.modules.user.service.impl.JobServiceImpl;

import com.xj.framework.ssh.Shell;


@Repository("shenweiCommand")
public class ShenweiCommandImpl implements ClusterManageCommand{
	
	private @Resource ConfigurationDao configurationDao;
	private  String fuWuListName=null;
	private static final Log log = LogFactory.getLog(JobServiceImpl.class);	

	
	@Override
	public int getAllNodesNum() {
		
		fuWuListName=configurationDao.queryAllConfiguration().getFuWuListName();
		String cmd="qload "+fuWuListName+" |grep vn|wc -l";
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		result=result.trim();
		if(result!=null&&!result.equals(""))
	        return Integer.parseInt(result);
			else return 0;
		
	}
	
	@Override
	public int getFreeNodesNum() {
		fuWuListName=configurationDao.queryAllConfiguration().getFuWuListName();
		String cmd="qload "+fuWuListName+" |grep idle|wc -l";
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		result=result.trim();
		if(result!=null&&!result.equals(""))
	        return Integer.parseInt(result);
			else return 0;
	}
	
	@Override
	public int getJobExclusiveNodesNum(){
		fuWuListName=configurationDao.queryAllConfiguration().getFuWuListName();
		//String cmd="qload q_sw_hpcag |grep busy|wc -l";
		String cmd="qload "+fuWuListName+" |grep busy|wc -l";
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		result=result.trim();
        System.out.println("busy node is"+result+"fuWuListName is"+fuWuListName);
		if(result!=null&&!result.equals(""))
        return Integer.parseInt(result);
		else return 0;
	}
	
	@Override
	public int getDownNodesNum(){
		fuWuListName=configurationDao.queryAllConfiguration().getFuWuListName();
		//String cmd="qload q_sw_hpcag |grep down|wc -l";
		
		String cmd="qload "+fuWuListName+" |grep down|wc -l";
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		result=result.trim();
		if(result!=null&&!result.equals(""))
	        return Integer.parseInt(result);
			else return 0;
	}

	@Override
	public String getJobInfo(String id) {
		String cmd="bjobinfo "+id;
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		return result.trim();
	}

	@Override
	public void delJob(String id) {
		String cmd = "bkill " + id;
		PbsExecute pbs = new PbsExecute(cmd);
		pbs.executeCmd();
	}

	@Override
	public String getJobInfoByXmlFormat(String id) {
		String cmd="bjobnodes "+id;   //查询任务标识号为id的任务的状态
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		return result.trim();
		
		//可能会返回Job is not start yet
	}

	@Override
	public String submit(String shenweiPbsCommand) {
		String cmd="bsub "+shenweiPbsCommand;
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		return result.trim();
	}

	@Override
	public String getXMLNodeInfoByNodename(String nodename) {
				
		String cmd="cnload -c "+nodename+" |grep "+nodename+" |sed -r \"s/\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]//g\""+" |awk '{print $1\",\"$2\",\"$3\",\"$4\",\"$5\",\"$6\",\"$7\",\"$8\",\"$9\",\"$10\",\"$11}'";
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		
		return result.trim();
	}

	@Override
	public String getJobNameByIp(String ip) {//无锡修改15变为16
		fuWuListName=configurationDao.queryAllConfiguration().getFuWuListName();
		
		String spip = new String();
		spip="\" "+ip+" \"";
			
		spip.replaceAll("\\.", "\\\\.");
		
		
		
		//String cmd="qload -l q_sw_hpcag"+" |grep -w "+ip+" |awk '{print $15}'";
		String cmd="qload -l "+fuWuListName+" |grep -w "+spip+" |awk '{print $16}'";
	
		
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		return result.trim();
	}
	
	@Override
	public String getNodesInfoByStartAndEnd(int start,int end){
		fuWuListName=configurationDao.queryAllConfiguration().getFuWuListName();
		String cmd="qload "+fuWuListName+" |grep vn"+" |sed -r \"s/\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]//g\""+" |awk '{print $1\",\"$2\",\"$3\",\"$4\",\"$5\",\"$6\",\"$7\",\"$8\",\"$9\",\"$10\",\"$11}' | sed -n '"+start+","+end+"p'";
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		return result.trim();
	}

}
