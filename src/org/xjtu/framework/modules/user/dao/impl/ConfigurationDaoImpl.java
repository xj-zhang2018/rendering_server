package org.xjtu.framework.modules.user.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xjtu.framework.core.base.model.Configuration;
import org.xjtu.framework.modules.user.dao.ConfigurationDao;

@Repository("configurationDao")
public class ConfigurationDaoImpl implements ConfigurationDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public double queryUnitPrice(){
		double price=0.00;
		if(sessionFactory.getCurrentSession().createQuery("select unitPrice from Configuration").uniqueResult()!=null){
			price=((Double)sessionFactory.getCurrentSession().createQuery("select unitPrice from Configuration").uniqueResult()).doubleValue();
		}
		else
			 price=0.00;
		return price;
		
	}
	@Override
	public Configuration  queryAllConfiguration(){
		List configurations = sessionFactory.getCurrentSession().createQuery("from Configuration").list();
		if(configurations != null && configurations.size() == 1){
			return (Configuration)configurations.get(0);
		}
		return null;
		
	}
	@Override
	public void updateConfiguration(Configuration configuration){
		sessionFactory.getCurrentSession().update(configuration);
	}
	@Override
	public void persist(Configuration configuration){
		sessionFactory.getCurrentSession().persist(configuration);
	}
}
