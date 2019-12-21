package org.xjtu.framework.modules.user.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.HibernateUtil;
import org.xjtu.framework.core.util.LinuxInvoker;
import org.xjtu.framework.modules.user.dao.UserDao;

@Repository("userDao")
public class UserDaoImpl implements UserDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void persist(User userinfo) {
		sessionFactory.getCurrentSession().persist(userinfo);
	}

	@Override
	public User queryByName(String name) {
		
		List users = sessionFactory.getCurrentSession().createQuery("from User where name = ? ").setString(0, name).list();
		if(users != null && users.size() == 1){
			return (User)users.get(0);
		}
		
		return null;
	}
	
	
	@Override
	public void removeUsersById(String userId) {

		Session session=sessionFactory.getCurrentSession();

			session.createQuery("delete from User j where j.id=?").setString(0, userId).executeUpdate();
	
	}
	
	
	@Override
	public void removeUsersByIds(List<String> userIds) {
		Session session=sessionFactory.getCurrentSession();
		for(int i=0; i<userIds.size(); i++){
			session.createQuery("delete from User j where j.id=?").setString(0, userIds.get(i)).executeUpdate();
		}
	}
	
	@Override
	public User queryById(String userId) {
		
		List users = sessionFactory.getCurrentSession().createQuery("from User where id = ? ").setString(0, userId).list();
		if(users != null && users.size() == 1){
			return (User)users.get(0);
		}
		
		return null;
	}

	@Override
	public User queryByEmailAndPasswd(String email, String md5Password) {
		List users = sessionFactory.getCurrentSession().createQuery("from User u where u.email = ? and u.password = ?").setString(0, email).setString(1, md5Password).list();
		
		if(users != null && users.size() == 1){
			return (User)users.get(0);
		}
		
		return null;
	}

	@Override
	public User queryByLoginName(String username, String md5Password) {

		List users = sessionFactory.getCurrentSession().createQuery("from User u where u.name = ? and u.password = ?").setString(0, username).setString(1, md5Password).list();
		
		if(users != null && users.size() == 1){
			return (User)users.get(0);
		}
		
		return null;
	}

	@Override
	public void updateUser(User user)
	{
		sessionFactory.getCurrentSession().update(user);
	}

	@Override
	public User queryByEmail(String email) {
		List users = sessionFactory.getCurrentSession().createQuery("from User u where u.email = ?").setString(0, email).list();
		
		if(users != null && users.size() == 1){
			return (User)users.get(0);
		}
		
		return null;
	}
	
	@Override
	public int queryCountByUserType(int userType) {

		int num;
		if(userType<0)
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(u.id) from User u").uniqueResult()).intValue();
		else
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(u.id) from User u where u.type = ? ").setInteger(0, userType).uniqueResult()).intValue();
		return num;
	}
	
	@Override
	public int queryCountByUserName(String searchText, int userType) {

		int num;
		if(userType<0)
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(u.id) from User u where u.name like ? ").setString(0, "%" + searchText + "%").uniqueResult()).intValue();
		else
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(u.id) from User u where u.name like ? and u.type = ? ").setString(0, "%" + searchText + "%").setInteger(1, userType).uniqueResult()).intValue();
		return num;
	}
	
	@Override
	public List<User> pagnateByUserType(int pageNum, int pageSize, int userType) {

		List users;
		if(userType<0)
			users=sessionFactory.getCurrentSession().createQuery("from User u order by u.createTime desc").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			users=sessionFactory.getCurrentSession().createQuery("from User u where u.type = ? order by u.createTime desc").setInteger(0, userType).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		return users;
	}
	
	@Override
	public List<User> pagnateByUserName(String searchText, int pageNum, int pageSize, int userType) {

		List users;
		if(userType<0)
			users=sessionFactory.getCurrentSession().createQuery("from User u where u.name like ? order by u.createTime desc").setString(0, "%" + searchText + "%").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			users=sessionFactory.getCurrentSession().createQuery("from User u where u.name like ? and u.type = ? order by u.createTime desc").setString(0, "%" + searchText + "%").setInteger(1, userType).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		return users;
	}
	
	@Override
	public List<User> queryAllUsers() {
		List users = sessionFactory.getCurrentSession().createQuery("from User").list();
		if(users != null && users.size()>0){
			return users;
		}
		
		return null;
	}
}
