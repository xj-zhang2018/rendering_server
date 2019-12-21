package org.xjtu.framework.modules.user.service;

import java.util.List;

import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.modules.protocol.ProgressMessage;

public interface FrameService {
	public void addFrame(Frame frame);
	public List<Frame> findFramesByJobId(String jobId);
	public List<Frame> findUnfinishedFramesByUnitId(String unitId);
	public List<Frame> findNotStartFramesByJobId(String jobId);
	public void updateFrameInfo(Frame frame);
	public Frame findFrameByNameAndTaskId(String name,String taskId);
	public List<Frame> findFrameByName(String name);
	public void updateFrameInfoByProgressMessage(ProgressMessage progressMessage);
	//public void UpdateProgressMessage(List<Job> jobs);
	public List<Frame> findFramesByTaskId(String taskId);
	public int findTotalCountByQuery(String searchText,String searchType,int frameStatus);
	public List<Frame> listFramesByQuery(String searchText,String searchType, int pageNum, int pageSize,int frameStatus);
	public void updateTaskAndJobProgress();
	public List<Frame> findByFrameStatusAndTaskId(int frameStatus , String taskId);
	public int findCountByFrameStatusAndTaskId(int frameStatus , String taskId);
}
