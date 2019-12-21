package org.xjtu.framework.modules.user.dao;

import java.util.Date;
import java.util.List;
import org.xjtu.framework.core.base.model.Job;

public interface JobDao {
	public List<Job> queryJobsByProjectId(String projectId);
	public void persist(Job job);
	public Job queryJobsById(String jobId);
	public Job queryJobByJobNameAndProjectId(String jobName, String projectId);
	public void updateJob(Job job);
	public void removeJob(Job job);
	public int queryMaxQueueNumByPriority(int jobPriority);
	public int queryMinQueueNumByPriority(int jobPriority);
	public List<Job> queryQueueJobsByJobPriority(int jobPriority);
	
	public int queryCount(int jobStatus);
	public int queryCountByJobName(String searchText,int jobStatus);
	public int queryCountcopyJobName(String searchText);
	public int queryCountByAccurateProjectId(String searchText,int jobStatus);
	public List<Job> pagnate(int pageNum, int pageSize,int jobStatus);
	public List<Job> pagnateByJobName(String searchText, int pageNum, int pageSize,int jobStatus);
	public List<Job> pagnateByAccurateProjectId(String searchText, int pageNum,int pageSize,int jobStatus);
	public int queryNewJobCountsByDate(Date date);
	public int queryStartJobCountsByDate(Date date);
	public int queryEndJobCountsByDate(Date date);
	
	public Job queryHeadQueuingJob();
	public Job queryNotStartJob();
	public List<Job> queryJobs();
	
	public List<Job> queryDistributedJob();
	

	

}
