package org.xjtu.framework.core.util;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;


public class HibernateUtil {

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
}
