package org.xjtu.framework.modules.user.dao;

public interface ClusterManageCommand {

	public int getAllNodesNum();
	public int getFreeNodesNum();
	public int getJobExclusiveNodesNum();
	public int getDownNodesNum();
	public String getJobInfo(String id);
	public String getJobInfoByXmlFormat(String id);
	public void delJob(String id);
	public String submit(String scriptPath);
	public String getXMLNodeInfoByNodename(String nodename);
	public String getJobNameByIp(String ip);
	public String getNodesInfoByStartAndEnd(int start,int end);
	
}
