package org.xjtu.framework.modules.user.service;

import java.util.Date;
import java.util.List;

import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.modules.protocol.ProgressMessage;
import org.xjtu.framework.modules.protocol.TaskDiscription;

public interface JobService {
	public List<Job> findJobsByProjectId(String projectId);
	public void addJob(Job job);
	public void deleteJob(Job job);
	public Job findJobsById(String jobId);
	public Job findJobByJobNameAndProjectId(String jobName,String projectId);
	public void updateJobInfo(Job job);
	public int findMaxQueueNumByPriority(int jobPriority);
	public int findMinQueueNumByPriority(int jobPriority);
	public List<Job> findQueueJobsByJobPriority(int jobPriority);
	
	public int findTotalCountByQuery(String searchText,String searchType, int jobStatus);
	public List<Job> listJobsByQuery(String searchText,String searchType, int pageNum, int pageSize, int jobStatus);
	public int findNewJobCountsByDate(Date date);
	public int findStartJobCountsByDate(Date date);
	public int findEndJobCountsByDate(Date date);
	
	public Job findHeadQueuingJob();
	public List<Job> findDistributedJob();
	
	public void suspendJobByJobId(String jobId);
	public void doStartCameraRender(List<String> jobIds);
	public void doStopCameraRender(String jobId);
	public Boolean distributeRunitJob(Job job, int unitsNumber, int nodesNumPerUnit, String renderEngineName,int nodes);
	public void changeQueuingJobToTop(String jobId);
	public Boolean continueRunitJobtoTask(Job job, int unitsNumber, int nodesNumPerUnit, List<Frame> fs, String renderEngineName);
	
	public List<Job> findJobs();
	public double JobRenderPrice(String jodId);
	public void JobPayment(String jobId);
	public void doCopyjobs(List<String> jobIds);
	public void unitBalaing(String existedJobId , String taskBalacingId);
	public void taskadding(String existedJobId , String taskBalacingId);

	public Job FindNotStartJob();
	
	
	
	
	
}
