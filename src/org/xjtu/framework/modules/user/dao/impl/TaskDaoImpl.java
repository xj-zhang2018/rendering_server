package org.xjtu.framework.modules.user.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.core.util.HibernateUtil;
import org.xjtu.framework.modules.user.dao.TaskDao;

@Repository("taskDao")
public class TaskDaoImpl implements TaskDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void persist(Task task) {
		sessionFactory.getCurrentSession().save(task);
	}

	@Override
	public void updateTask(Task task) {
		sessionFactory.getCurrentSession().update(task);
	}
          
	@Override
	public Task queryTasksById(String taskId) {
		List tasks=sessionFactory.getCurrentSession().createQuery("from Task where id = ? ").setString(0, taskId).list();
		if(tasks != null && tasks.size() == 1){
			return (Task)tasks.get(0);
		}		
		return null;
	}

	@Override
	public List<Task> queryTasksByJobId(String jobId) {
		return sessionFactory.getCurrentSession().createQuery("from Task as a where a.job.id = ? ").setString(0, jobId).list();
	}

	@Override
	public List<Task> queryTasksByUnitId(String unitId) {
		return sessionFactory.getCurrentSession().createQuery("from Task as a where a.unit.id = ? ").setString(0, unitId).list();
	}
	@Override
	public List<Task> queryTasksHasProgress(){
		return sessionFactory.getCurrentSession().createQuery("from Task  where taskprogress>=0 and taskprogress<100 ").list();
	}
	
}
