package org.xjtu.framework.modules.user.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xjtu.framework.core.base.model.EmailLink;
import org.xjtu.framework.modules.user.dao.EmailLinkDao;

@Repository("emailLinkDao")
public class EmailLinkDaoImpl implements EmailLinkDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void persist(EmailLink emailLink) {
		sessionFactory.getCurrentSession().persist(emailLink);	
	}

	@Override
	public EmailLink queryEmailLinkbyIdAndName(String id, String name) {
		List emaillinks = sessionFactory.getCurrentSession().createQuery("from EmailLink where id = ? and name = ? ").setString(0, id).setString(1, name).list();
		if(emaillinks != null && emaillinks.size() == 1){
			return (EmailLink)emaillinks.get(0);
		}
		return null;
	}

}
