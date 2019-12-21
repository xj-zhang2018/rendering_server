package org.xjtu.framework.modules.user.dao.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xjtu.framework.core.base.constant.UnitStatus;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.modules.user.dao.UnitDao;


@Repository("unitDao")
public class UnitDaoImpl implements UnitDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Unit> queryUnitsByStatus(int unitStatus) {
		return sessionFactory.getCurrentSession().createQuery("from Unit as a where a.unitStatus = ? ").setInteger(0, unitStatus).list();
	}
	
	@Override
	public List<Unit> queryNotDeadUnits() {
		return sessionFactory.getCurrentSession().createQuery("from Unit as a where a.unitStatus = ?").setInteger(0, UnitStatus.busy).list();
	}
	
	@Override
	public void persist(Unit unit) {
		sessionFactory.getCurrentSession().save(unit);
	}
	
	@Override
	public List<Unit> queryAllUnits(){
		return sessionFactory.getCurrentSession().createQuery("from Unit").list();
	}
	
	@Override
	public Unit queryUnitsByPbsId(String pbsId){
		List units=sessionFactory.getCurrentSession().createQuery("from Unit where pbsId = ? ").setString(0, pbsId).list();
		if(units != null && units.size() == 1){
			return (Unit)units.get(0);
		}		
		return null;
	}
	
	@Override
	public void updateUnit(Unit unit){
		sessionFactory.getCurrentSession().update(unit);
	}	
	
	@Override
	public int queryCount(int unitStatus) {	
		
		int num;

		if(unitStatus==-2)
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(u.id) from Unit u").uniqueResult()).intValue();
		else
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(u.id) from Unit u where u.unitStatus = ?").setInteger(0, unitStatus).uniqueResult()).intValue();
		
		return num;
	}

	@Override
	public int queryCountByUnitName(String searchText,int unitStatus) {
		
		int num;
		
		if(unitStatus==-2)
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(u.id) from Unit u where u.unitMasterName like ? ").setString(0, "%" + searchText + "%").uniqueResult()).intValue();
		else
			num=((Long)sessionFactory.getCurrentSession().createQuery("select count(u.id) from Unit u where u.unitMasterName like ? and u.unitStatus = ?").setString(0, "%" + searchText + "%").setInteger(1, unitStatus).uniqueResult()).intValue();

		return num;
	}

	@Override
	public List<Unit> pagnate(int pageNum, int pageSize,int unitStatus) {
		List units;

		if(unitStatus==-2)
			units=sessionFactory.getCurrentSession().createQuery("from Unit u order by u.id desc").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			units=sessionFactory.getCurrentSession().createQuery("from Unit u where u.unitStatus = ? order by u.id desc").setInteger(0, unitStatus).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
			
		return units;
	}

	@Override
	public List<Unit> pagnateByUnitName(String searchText, int pageNum,
			int pageSize,int unitStatus) {
		
		List units;
		
		if(unitStatus==-2)
			units=sessionFactory.getCurrentSession().createQuery("from Unit u where u.unitMasterName like ? order by u.id desc").setString(0, "%" + searchText + "%").setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			units=sessionFactory.getCurrentSession().createQuery("from Unit u where u.unitMasterName like ? and u.unitStatus = ? order by u.id desc").setString(0, "%" + searchText + "%").setInteger(1, unitStatus).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();

		return units;
	}
	
	@Override
	public List<Unit> pagnateByAccurateUnitName(String searchText, int pageNum,
			int pageSize,int unitStatus) {
		
		List units;
		
		if(unitStatus==-2)
			units=sessionFactory.getCurrentSession().createQuery("from Unit u where u.unitMasterName = ? order by u.id desc").setString(0, searchText).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();
		else
			units=sessionFactory.getCurrentSession().createQuery("from Unit u where u.unitMasterName = ? and u.unitStatus = ? order by u.id desc").setString(0, searchText).setInteger(1, unitStatus).setFirstResult((pageNum-1)*pageSize).setMaxResults(pageSize).list();

		return units;
	}

	@Override
	public Unit queryById(String unitId) {
		List units = sessionFactory.getCurrentSession().createQuery("from Unit where id = ? ").setString(0, unitId).list();
		if(units != null && units.size() == 1){
			return (Unit)units.get(0);
		}
		
		return null;
	}

	@Override
	public List<Unit> queryUnitsByJobId(String jobId) {
		return sessionFactory.getCurrentSession().createQuery("select distinct(t.unit) from Task as t where t.job.id = ? ").setString(0, jobId).list();
	}

	@Override
	public void removeUnit(Unit unit) {		
		sessionFactory.getCurrentSession().delete(unit);
	}
	


}
