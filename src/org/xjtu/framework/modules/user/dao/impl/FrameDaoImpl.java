package org.xjtu.framework.modules.user.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xjtu.framework.core.base.constant.FrameStatus;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.modules.user.dao.FrameDao;

@Repository("frameDao")
public class FrameDaoImpl implements FrameDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void persist(Frame frame) {
		sessionFactory.getCurrentSession().save(frame);
	}
	@Override
	public int queryCountByFrameStatusAndTaskId(int frameStatus , String taskId) {
		int num=((Long)sessionFactory.getCurrentSession().createQuery("select count(f.id) from Frame f where f.frameStatus = ? and f.task.id = ?").setInteger(0, frameStatus).setString(1, taskId).uniqueResult()).intValue();
		return num;
	
	}
	
	@Override
	public List<Frame> queryByFrameStatusAndTaskId(int frameStatus , String taskId) {
		return sessionFactory.getCurrentSession().createQuery("from Frame f where f.frameStatus = ? and f.task.id = ?").setInteger(0, frameStatus).setString(1, taskId).list();
		
	
	}

	@Override
	public List<Frame> queryFramesByJobId(String jobId){
		return sessionFactory.getCurrentSession().createQuery("from Frame as a where a.task.job.id = ? ").setString(0, jobId).list();
	}
	
	@Override
	public List<Frame> queryUnfinishedFramesByUnitId(String unitId){
		return sessionFactory.getCurrentSession().createQuery("from Frame as a where a.frameStatus = ? and a.task.unit.id = ? ").setInteger(0, FrameStatus.unfinished).setString(1, unitId).list();
	}
	
	@Override
	public List<Frame> queryNotStartFramesByJobId(String jobId){
		return sessionFactory.getCurrentSession().createQuery("from Frame as a where a.frameStatus = ? and a.task.job.id = ? ").setInteger(0, FrameStatus.notStart).setString(1,jobId).list();
	}

	@Override
	public void updateFrame(Frame frame) {
		sessionFactory.getCurrentSession().update(frame);
	}

	@Override
	public Frame queryByNameAndTaskId(String name, String taskId) {
		List frames = sessionFactory.getCurrentSession().createQuery("from Frame f where f.frameName = ? and f.task.id = ?").setString(0, name).setString(1, taskId).list();
		if(frames != null && frames.size() == 1){
			return (Frame) frames.get(0);
		}
		return null;
	}

	@Override
	public List<Frame> queryFramesByName(String name) {
		return sessionFactory.getCurrentSession().createQuery("from Frame f where f.frameName = ? ").setString(0, name).list();
	}
	
	
	

	@Override
	public List<Frame> queryFramesByTaskId(String taskId) { 
		return sessionFactory.getCurrentSession().createQuery("from Frame as a where a.task.id = ? ").setString(0, taskId).list();		
	}
	@Override
	public List<Integer> queryFramesProgressByTaskId(String taskId) { 
		return (sessionFactory.getCurrentSession().createQuery("select frameProgress from Frame as a where a.task.id = ? ").setString(0, taskId)).list();		
	}
	
	
	
	
	@Override
	public int queryCount(int frameStatus) {
		int num;

		if(frameStatus==-1)
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(f.id) from Frame f").uniqueResult()).intValue();
		else
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(f.id) from Frame f where f.frameStatus = ?").setInteger(0, frameStatus).uniqueResult()).intValue();
		return num;
	}
	
	@Override
	public int queryCountByFrameName(String searchText,int frameStatus) {
		int num;

		if(frameStatus==-1)
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(f.id) from Frame f where f.frameName like ? ").setString(0, "%" + searchText + "%").uniqueResult()).intValue();
		else
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(f.id) from Frame f where f.frameName like ? and f.frameStatus = ?").setString(0, "%" + searchText + "%").setInteger(1, frameStatus).uniqueResult()).intValue();

		return num;
	}
	
	public int queryCountByAccurateJobId(String searchText,int frameStatus)
	{
		int num;
		
		if(frameStatus==-1)
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(f.id) from Frame f where f.task.job.id = ? ").setString(0, searchText).uniqueResult()).intValue();
		else
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(f.id) from Frame f where f.task.job.id = ? and f.frameStatus = ?").setString(0, searchText).setInteger(1, frameStatus).uniqueResult()).intValue();
		return num;
	}
	
	@Override
	public List<Frame> pagnate(int pageNum, int pageSize,int frameStatus) {
		List frames;

		if(frameStatus==-1)
			frames=sessionFactory.getCurrentSession().createQuery("from Frame f order by f.frameName desc").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			frames=sessionFactory.getCurrentSession().createQuery("from Frame f where f.frameStatus = ? order by f.frameName desc").setInteger(0, frameStatus).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
			
		return frames;
	}
	
	@Override
	public List<Frame> pagnateByFrameName(String searchText, int pageNum,
			int pageSize,int frameStatus) {
		List frames;

		if(frameStatus==-1)
			frames=sessionFactory.getCurrentSession().createQuery("from Frame f where f.frameName like ? order by f.frameName desc").setString(0, "%" + searchText + "%").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			frames=sessionFactory.getCurrentSession().createQuery("from Frame f where f.frameName like ? and f.frameStatus = ? order by f.frameName desc").setString(0, "%" + searchText + "%").setInteger(1, frameStatus).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		return frames;
	}
	
	@Override
	public List<Frame> pagnateByAccurateJobId(String searchText, int pageNum,
			int pageSize,int frameStatus) {
		List frames;

		if(frameStatus==-1)
			frames=sessionFactory.getCurrentSession().createQuery("from Frame f where f.task.job.id = ? order by f.frameName desc").setString(0, searchText).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			frames=sessionFactory.getCurrentSession().createQuery("from Frame f where f.task.job.id = ? and f.frameStatus = ? order by f.frameName desc").setString(0, searchText).setInteger(1, frameStatus).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();

		return frames;
	}
	@Override
	public int queryFinishFramesNum(){
		int allNum=((Long)sessionFactory.getCurrentSession().createQuery("select count(*) from  Frame f where f.frameProgress =100").uniqueResult()).intValue();
		return allNum;
		
	}
	@Override
	public String queryEarlystartTime(){
		String  startTime;
		if((sessionFactory.getCurrentSession().createQuery("select MIN(startTime) from Frame f where f.frameProgress =100 ").uniqueResult()) != null){
			startTime=(sessionFactory.getCurrentSession().createQuery("select MIN(startTime) from Frame f where f.frameProgress = 100").uniqueResult()).toString();
			return startTime;
		}
		
		
			return null;
		
	
	}
	
	@Override
	public String queryLastendTime(){
		
		String endTime;
		if((sessionFactory.getCurrentSession().createQuery("select MAX(endTime) from Frame f where f.frameProgress =100 ").uniqueResult()) != null){
			endTime=(sessionFactory.getCurrentSession().createQuery("select MAX(endTime) from Frame f where f.frameProgress =100 ").uniqueResult()).toString() ;
			return endTime;
		}
		
			return null;
		

	}
	
	
}
