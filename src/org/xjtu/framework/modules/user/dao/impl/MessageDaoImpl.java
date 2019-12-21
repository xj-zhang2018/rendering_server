package org.xjtu.framework.modules.user.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xjtu.framework.core.base.constant.MessageState;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Message;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.HibernateUtil;
import org.xjtu.framework.modules.user.dao.MessageDao;
import org.xjtu.framework.modules.user.dao.ProjectDao;

@Repository("messageDao")
public class MessageDaoImpl implements MessageDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void addMessage(Message message) {
		sessionFactory.getCurrentSession().persist(message);
	}

	@Override
	public List<Message> queryMessageByCount(int count) {
		return sessionFactory.getCurrentSession().createQuery("from Message m order by m.createTime desc").setMaxResults(count).list();
	}

	@Override
	public void setAllMessageChecked() {
		sessionFactory.getCurrentSession().createQuery("update Message m set m.state = ?").setInteger(0, MessageState.CHECKED).executeUpdate();
	}
	@Override
	public List<Message> queryUncheckedMessage() {
		return sessionFactory.getCurrentSession().createQuery("from Message m where m.state = ? order by m.createTime asc").setInteger(0, MessageState.UNCHECKED).list();
	}

	@Override
	public void updateMessage(Message message) {
		sessionFactory.getCurrentSession().update(message);
	}
	
}
