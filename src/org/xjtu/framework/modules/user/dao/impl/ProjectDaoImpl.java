package org.xjtu.framework.modules.user.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.HibernateUtil;
import org.xjtu.framework.modules.user.dao.ProjectDao;

@Repository("projectDao")
public class ProjectDaoImpl implements ProjectDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Project> queryProjectsByUserName(String username) {
		return sessionFactory.getCurrentSession().createQuery("from Project as a where a.user.name = ? ").setString(0, username).list();
	}

	@Override
	public List<Project> queryProjectsByUserId(String userId) {
		return sessionFactory.getCurrentSession().createQuery("from Project as a where a.user.id = ? ").setString(0, userId).list();
	}
	
	@Override
	public void persist(Project project) {
		sessionFactory.getCurrentSession().save(project);
	}
	
	@Override
	public void removeProject(Project project){
		sessionFactory.getCurrentSession().delete(project);
	}
	
	@Override
	public Project queryProjectById(String projectId){
		List projects = sessionFactory.getCurrentSession().createQuery("from Project where id = ? ").setString(0, projectId).list();
		if(projects != null && projects.size() == 1){
			return (Project)projects.get(0);
		}
		
		return null;
	}
	
	
	@Override
	public int queryCount() {
		int num;

		num=((Long)sessionFactory.getCurrentSession().createQuery("select count(p.id) from Project p").uniqueResult()).intValue();

		return num;
	}

	@Override
	public int queryCountByProjectName(String searchText) {
		int num;

		num=((Long)sessionFactory.getCurrentSession().createQuery("select count(p.id) from Project p where p.name like ? ").setString(0, "%" + searchText + "%").uniqueResult()).intValue();

		return num;
	}

	@Override
	public int queryCountByAccurateUserId(String searchText) {
		int num;

		num=((Long)sessionFactory.getCurrentSession().createQuery("select count(p.id) from Project p where p.user.id = ? ").setString(0, searchText).uniqueResult()).intValue();

		return num;
	}
	
	@Override
	public List<Project> pagnate(int pageNum, int pageSize) {
		List projects;

		projects=sessionFactory.getCurrentSession().createQuery("from Project p order by p.createTime desc").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();

		return projects;
	}

	@Override
	public List<Project> pagnateByProjectName(String searchText, int pageNum,
			int pageSize) {
		List projects;

		projects=sessionFactory.getCurrentSession().createQuery("from Project p where p.name like ? order by p.createTime desc").setString(0, "%" + searchText + "%").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();

		return projects;
	}

	@Override
	public List<Project> pagnateByAccurateUserId(String searchText, int pageNum,
			int pageSize) {
		List projects;

		projects=sessionFactory.getCurrentSession().createQuery("from Project p where p.user.id = ? order by p.createTime desc").setString(0, searchText).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();

		return projects;
	}
	

	@Override
	public void updateProject(Project project) {
		sessionFactory.getCurrentSession().update(project);
	}

	@Override
	public Project queryProjectByProjectNameAndUserId(String projectName,
			String userId) {
		List projects = sessionFactory.getCurrentSession().createQuery("from Project as p where p.name = ? and p.user.id=?").setString(0, projectName).setString(1, userId).list();
		if(projects != null && projects.size() == 1){
			return (Project)projects.get(0);
		}
		
		return null;
	}
}
