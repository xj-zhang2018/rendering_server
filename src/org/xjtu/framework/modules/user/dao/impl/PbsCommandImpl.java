package org.xjtu.framework.modules.user.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.xjtu.framework.core.util.PbsExecute;
import org.xjtu.framework.modules.user.dao.ClusterManageCommand;

@Repository("pbsCommand")
public class PbsCommandImpl implements ClusterManageCommand{
	private static final Log log = LogFactory.getLog(PbsCommandImpl.class);
	

	@Override
	public int getAllNodesNum() {
		String cmd="qnodes -l all|wc -l";
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		result=result.trim();
		return Integer.parseInt(result);
	}
		
	@Override
	public int getFreeNodesNum() {
		String cmd="qnodes -l free|wc -l";
		
		
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		result=result.trim();
		return Integer.parseInt(result);
	}
	
	@Override
	public int getJobExclusiveNodesNum(){
		String cmd="qnodes -l busy|wc -l";
		
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		result=result.trim();

		return Integer.parseInt(result);
	}
	
	@Override
	public int getDownNodesNum(){
		String cmd="qnodes -l down|wc -l";
		
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		result=result.trim();

		return Integer.parseInt(result);
	}

	@Override
	public String getJobInfo(String id) {
		String cmd="qstat -f "+id;
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		return result.trim();
	}

	@Override
	public void delJob(String id) {
		String cmd = "qdel " + id;
		PbsExecute pbs = new PbsExecute(cmd);
		pbs.executeCmd();
	}

	@Override
	public String getJobInfoByXmlFormat(String id) {
		String cmd="qstat -x "+id;   //查询任务标识号为id的任务的状态
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		
		return result.trim();
	}

	@Override
	public String submit(String scriptPath) {
		String cmd="qsub "+scriptPath;
		
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		return result.trim();
	}

	@Override
	public String getXMLNodeInfoByNodename(String nodename) {

		String cmd="pbsnodes -x "+nodename;   //查询任务标识号为id的任务的状态
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		
		return result.trim();
	}

	@Override
	public String getJobNameByIp(String ip) {

		//在pbs下不需要
		return null;
	}
	
	
	//此处先取出所有节点的字符串长度然后返回到后面处理起始和结束的字符串
	@Override
	public String getNodesInfoByStartAndEnd(int start,int end){

		String cmd="qnodes -x ";   //查询任务标识号为id的任务的状态
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		
		return result.trim();
		
	}

}
