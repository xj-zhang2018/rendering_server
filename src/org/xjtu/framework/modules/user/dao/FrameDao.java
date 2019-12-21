package org.xjtu.framework.modules.user.dao;

import java.util.Date;
import java.util.List;

import org.xjtu.framework.core.base.model.Frame;

public interface FrameDao {
	public void persist(Frame frame);
	public List<Frame> queryFramesByJobId(String jobId);
	public List<Frame> queryUnfinishedFramesByUnitId(String unitId);
	public List<Frame> queryNotStartFramesByJobId(String jobId);
	public List<Frame> queryFramesByTaskId(String taskId);
	public void updateFrame(Frame frame);
	public Frame queryByNameAndTaskId(String name,String taskId);
	public List<Frame> queryFramesByName(String name);
	public int queryCount(int frameStatus);
	public int queryCountByFrameName(String searchText,int frameStatus);
	public int queryCountByAccurateJobId(String searchText,int frameStatus);
	public List<Frame> pagnate(int pageNum, int pageSize,int frameStatus);
	public List<Frame> pagnateByFrameName(String searchText, int pageNum,
			int pageSize,int frameStatus);
	public List<Frame> pagnateByAccurateJobId(String searchText, int pageNum,
			int pageSize,int frameStatus);
	public int queryCountByFrameStatusAndTaskId(int frameStatus , String taskId);
	public List<Frame> queryByFrameStatusAndTaskId(int frameStatus , String taskId);
	//2016-01新添
	public int queryFinishFramesNum();
	public String queryEarlystartTime();
	public String queryLastendTime();
	public List<Integer> queryFramesProgressByTaskId(String taskId);
	
}

