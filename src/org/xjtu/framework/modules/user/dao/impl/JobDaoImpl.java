package org.xjtu.framework.modules.user.dao.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xjtu.framework.core.base.constant.FrameStatus;
import org.xjtu.framework.core.base.constant.JobStatus;
import org.xjtu.framework.core.base.constant.TaskStatus;
import org.xjtu.framework.core.base.constant.UnitStatus;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.HibernateUtil;
import org.xjtu.framework.modules.user.dao.JobDao;
import org.xjtu.framework.modules.user.service.ClusterManageService;

@Service("jobDao")
@Transactional
public class JobDaoImpl extends HibernateUtil implements JobDao{

	private @Resource ClusterManageService clusterManageService;
	
	@Override
	public List<Job> queryJobsByProjectId(String projectId) {
		return this.getSessionFactory().getCurrentSession().createQuery("from Job as a where a.project.id = ? ").setString(0, projectId).list();
	}
	
	@Override
	public void persist(Job job){
		this.getSessionFactory().getCurrentSession().save(job);
	}

	@Override
	public Job queryJobsById(String jobId){
		List jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job where id = ? ").setString(0, jobId).list();
		if(jobs != null && jobs.size() == 1){
			return (Job)jobs.get(0);
		}		
		return null;
	}
	
	@Override
	public void updateJob(Job job){
		this.getSessionFactory().getCurrentSession().update(job);
	}

	@Override
	public int queryCount(int jobStatus) {
		int num;

		if(jobStatus==-1)
			num=((Long)this.getSessionFactory().getCurrentSession().createQuery("select count(j.id) from Job j").uniqueResult()).intValue();
		else
			num=((Long)this.getSessionFactory().getCurrentSession().createQuery("select count(j.id) from Job j where j.jobStatus = ?").setInteger(0, jobStatus).uniqueResult()).intValue();
		return num;
	}

	@Override
	public int queryCountByJobName(String searchText,int jobStatus) {
		int num;

		if(jobStatus==-1)
			num=((Long)this.getSessionFactory().getCurrentSession().createQuery("select count(j.id) from Job j where j.cameraName like ? ").setString(0, "%" + searchText + "%").uniqueResult()).intValue();
		else
			num=((Long)this.getSessionFactory().getCurrentSession().createQuery("select count(j.id) from Job j where j.cameraName like ? and j.jobStatus = ?").setString(0, "%" + searchText + "%").setInteger(1, jobStatus).uniqueResult()).intValue();

		return num;
	}
	@Override
	public int queryCountcopyJobName(String searchText) {
		int num;
		num=((Long)this.getSessionFactory().getCurrentSession().createQuery("select count(j.id) from Job j where j.cameraName like ? and j.cameraName not like ? ").setString(0,  searchText +"-鍓湰%").setString(1, searchText +"-鍓湰%"+"-鍓湰%") .uniqueResult()).intValue();

		return num;
	}
	
	public int queryCountByAccurateProjectId(String searchText,int jobStatus)
	{
		int num;
		
		if(jobStatus==-1)
			num=((Long)this.getSessionFactory().getCurrentSession().createQuery("select count(j.id) from Job j where j.project.id = ? ").setString(0, searchText).uniqueResult()).intValue();
		else
			num=((Long)this.getSessionFactory().getCurrentSession().createQuery("select count(j.id) from Job j where j.project.id = ? and j.jobStatus = ?").setString(0, searchText).setInteger(1, jobStatus).uniqueResult()).intValue();
		return num;
	}
	

	@Override
	public List<Job> pagnate(int pageNum, int pageSize,int jobStatus) {
		List jobs;

		if(jobStatus==-1)
			jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job j order by j.createTime desc").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else if(jobStatus==JobStatus.inQueue)
			jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job j where j.jobStatus = ? order by j.jobPriority desc,j.queueNum asc").setInteger(0, jobStatus).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job j where j.jobStatus = ? order by j.createTime desc").setInteger(0, jobStatus).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
			
		return jobs;
	}

	//杩欎簺鍦版柟涔熷緱鍔犵瓑寰呴槦鍒楁帓搴�
	@Override
	public List<Job> pagnateByJobName(String searchText, int pageNum,
			int pageSize,int jobStatus) {
		List jobs;

		if(jobStatus==-1)
			jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job j where j.cameraName like ? order by j.createTime desc").setString(0, "%" + searchText + "%").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job j where j.cameraName like ? and j.jobStatus = ? order by j.createTime desc").setString(0, "%" + searchText + "%").setInteger(1, jobStatus).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		return jobs;
	}
	
	@Override
	public List<Job> pagnateByAccurateProjectId(String searchText, int pageNum,
			int pageSize,int jobStatus) {
		List jobs;

		if(jobStatus==-1)
			jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job j where j.project.id = ? order by j.createTime desc").setString(0, searchText).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job j where j.project.id = ? and j.jobStatus = ? order by j.createTime desc").setString(0, searchText).setInteger(1, jobStatus).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();

		return jobs;
	}

	@Override
	public void removeJob(Job job) {

		Session session=this.getSessionFactory().getCurrentSession();
		
		session.delete(job);
		
	}
	
	@Override
	public int queryMaxQueueNumByPriority(int jobPriority) {
		List jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job as j where j.jobPriority = ? and j.jobStatus = ? ").setInteger(0, jobPriority).setInteger(1, JobStatus.inQueue).list();
		if(jobs!=null&jobs.size()>0)
			return (Integer)this.getSessionFactory().getCurrentSession().createQuery("select max(j.queueNum) from Job j where j.jobStatus=? and j.jobPriority=?").setInteger(0, JobStatus.inQueue).setInteger(1, jobPriority).uniqueResult();
		else
			return -1;
	}
	
	@Override
	public int queryMinQueueNumByPriority(int jobPriority) {
		List jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job as j where j.jobPriority = ? and j.jobStatus = ? ").setInteger(0, jobPriority).setInteger(1, JobStatus.inQueue).list();
		if(jobs!=null&jobs.size()>0)
			return (Integer)this.getSessionFactory().getCurrentSession().createQuery("select min(j.queueNum) from Job j where j.jobStatus=? and j.jobPriority=?").setInteger(0, JobStatus.inQueue).setInteger(1, jobPriority).uniqueResult();
		else
			return 1;
	}
	
	@Override
	public int queryNewJobCountsByDate(Date date){
		Query query=this.getSessionFactory().getCurrentSession().createQuery("from Job where createTime between ? and ?");
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		query.setDate(0, calendar.getTime());
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		query.setDate(1, calendar.getTime());
		
		List<Job> jobs=query.list();
		if(jobs!=null) 
			return jobs.size();
		else 
			return 0;
	}


	@Override
	public List<Job> queryQueueJobsByJobPriority(int jobPriority) {
		return this.getSessionFactory().getCurrentSession().createQuery("from Job as a where a.jobPriority = ? and a.jobStatus = ? order by a.queueNum asc").setInteger(0, jobPriority).setInteger(1, JobStatus.inQueue).list();
	}

	@Override
	public int queryStartJobCountsByDate(Date date){
		Query query=this.getSessionFactory().getCurrentSession().createQuery("from Job where startTime between ? and ?");
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		query.setDate(0, calendar.getTime());
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		query.setDate(1, calendar.getTime());
		
		List<Job> jobs=query.list();
		if(jobs!=null) 
			return jobs.size();
		else 
			return 0;	
	}
	public int queryEndJobCountsByDate(Date date){
		Query query=this.getSessionFactory().getCurrentSession().createQuery("from Job where endTime between ? and ?");
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		query.setDate(0, calendar.getTime());
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		query.setDate(1, calendar.getTime());
		
		List<Job> jobs=query.list();
		if(jobs!=null) 
			return jobs.size();
		else 
			return 0;	
	}

	@Override
	public Job queryJobByJobNameAndProjectId(String jobName, String projectId) {
		List jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job as j where j.cameraName = ? and j.project.id = ?").setString(0, jobName).setString(1, projectId).list();
		if(jobs != null && jobs.size() == 1){
			return (Job)jobs.get(0);
		}		
		return null;
	}

	@Override
	public Job queryHeadQueuingJob() {

		List<Job> jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job j where j.jobStatus = ? order by j.jobPriority desc,j.queueNum asc").setInteger(0, JobStatus.inQueue).list();

		if(jobs!=null&&jobs.size()>0){
			return jobs.get(0);
		}else{
			return null;
		}
	
	}

	@Override
	public List<Job> queryJobs() {
		List jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job").list();	
		return jobs;
	}	
	
	@Override
	public List<Job> queryDistributedJob(){
		List jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job j where j.jobStatus = ?").setInteger(0, JobStatus.distributed).list();	
		return jobs;
	}

	@Override
	public Job queryNotStartJob() {
		List<Job> jobs=this.getSessionFactory().getCurrentSession().createQuery("from Job j where j.jobStatus = ? order by j.jobPriority desc,j.queueNum asc").setInteger(0, JobStatus.notStart).list();

		if(jobs!=null&&jobs.size()>0){
			return jobs.get(0);
		}else{
			return null;
		}
	}

}
