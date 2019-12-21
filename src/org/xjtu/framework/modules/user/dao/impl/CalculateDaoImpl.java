package org.xjtu.framework.modules.user.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xjtu.framework.core.base.constant.CalculateStatus;
import org.xjtu.framework.core.base.constant.JobStatus;
import org.xjtu.framework.core.base.model.Calculate;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.modules.user.dao.CalculateDao;

@Repository("calculateDao")
@Transactional
public class CalculateDaoImpl implements CalculateDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public int queryCount() {
		int num;
		num=((Long)sessionFactory.getCurrentSession().createQuery("select count(c.id) from Calculate c").uniqueResult()).intValue();
		return num;
	}

	@Override
	public int queryCountByCalculateName(String searchText) {
		int num;
		num=((Long)sessionFactory.getCurrentSession().createQuery("select count(c.id) from Calculate c where c.name like ? ").setString(0, "%" + searchText + "%").uniqueResult()).intValue();
		return num;
	}

	@Override
	public int queryCountByAccurateUserId(String searchText) {
		/*int num;
		num=((Long)sessionFactory.getCurrentSession().createQuery("select count(c.id) from Caiculate c where c.user.id = ? ").setString(0, searchText).uniqueResult()).intValue();
		return num;*/
		return 0;
	}

	@Override
	public List<Calculate> pagnate(int pageNum, int pageSize) {
		List calculates;
		calculates=sessionFactory.getCurrentSession().createQuery("from Calculate c order by c.xmlCreateTime desc").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		return calculates;
	}

	@Override
	public List<Calculate> pagnateByCalculateName(String searchText,int pageNum, int pageSize) {
		List calculates;
		calculates=sessionFactory.getCurrentSession().createQuery("from Calculate c where c.xmlName like ? order by c.xmlCreateTime desc").setString(0, "%" + searchText + "%").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		return calculates;
	}

	@Override
	public List<Calculate> pagnateByAccurateUserId(String searchText,int pageNum, int pageSize) {
		return null;
	}

	@Override
	public Calculate queryCalculateById(String calculateId) {
		List calculates=sessionFactory.getCurrentSession().createQuery("from Calculate where id= ? ").setString(0,calculateId).list();
		if(calculates!=null&&calculates.size()==1){
			return (Calculate) calculates.get(0);
		}
		return null;
	}

	@Override
	public void removeCalculate(Calculate calculate) {
		sessionFactory.getCurrentSession().delete(calculate);
	}

	@Override
	public void persist(Calculate c) {
		sessionFactory.getCurrentSession().save(c);
	}

	@Override
	public Calculate queryCalculateByCalculateNameAndUserId(String param,
			String id) {
		List calculates = sessionFactory.getCurrentSession().createQuery("from Calculate as c where c.xmlName = ? and c.user.id=?").setString(0, param).setString(1, id).list();
		if(calculates != null && calculates.size() == 1){
			return (Calculate)calculates.get(0);
		}
		return null;
	}

	@Override
	public List<Calculate> queryCalculates() {
		List calculates=sessionFactory.getCurrentSession().createQuery("from Calculate").list();
		return calculates;
	}

	@Override
	public int queryCountcopyCalculateName(String xmlName) {
		int num;
		num=((Long)sessionFactory.getCurrentSession().createQuery("select count(c.id) from Calculate c where c.xmlName like ? and c.xmlName not like ? ").setString(0,  xmlName +"-副本%").setString(1, xmlName +"-副本%"+"-副本%") .uniqueResult()).intValue();
		
		return num;
	}

	
	
	@Override
	public Calculate queryHeadQueuingJob() {
		List<Calculate> calculates=sessionFactory.getCurrentSession().createQuery("from Calculate j where j.xmlStatus = ? order by j.xmlPriority desc").setInteger(0, JobStatus.inQueue).list();

		if(calculates!=null&&calculates.size()>0){
			return calculates.get(0);
		}else{
			return null;
		}
	
	}

	@Override
	public void updateCalculate(Calculate calculate) {
		sessionFactory.getCurrentSession().update(calculate);
	}

	@Override
	public List<Calculate> queryDistributedCalculate() {
		List calculates=sessionFactory.getCurrentSession().createQuery("from Calculate j where j.xmlStatus = ?").setInteger(0, CalculateStatus.distributed).list();	
		return calculates;
		
	}
}
