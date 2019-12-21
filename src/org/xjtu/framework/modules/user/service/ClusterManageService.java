package org.xjtu.framework.modules.user.service;

import java.util.List;

import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.modules.user.vo.NodesInfo;
import org.xjtu.framework.modules.user.vo.RegisterUserInfo;

public interface ClusterManageService {

	public String getJobnameByIp(String ip);
	public int getAllNodesNum();
	public int getFreeNodesNum();
	public int getJobExclusiveNodesNum();
	public int getDownNodesNum();
	public boolean isReady(String id);
	public boolean isFault(String id);
	public void dismissUnit(String id);
	public String submit(String scriptPath);
	public String getNodeString(String id) throws Exception;
	public NodesInfo getNodeInfo(String name);
	public List<NodesInfo> getNodesInfoByStartAndEnd(int start,int end);
	 
}
