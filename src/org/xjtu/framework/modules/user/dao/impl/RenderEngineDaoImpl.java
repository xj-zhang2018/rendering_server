package org.xjtu.framework.modules.user.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xjtu.framework.core.base.model.EmailLink;
import org.xjtu.framework.core.base.model.RenderEngine;
import org.xjtu.framework.modules.user.dao.EmailLinkDao;
import org.xjtu.framework.modules.user.dao.RenderEngineDao;

@Repository("renderEngineDao")
public class RenderEngineDaoImpl implements RenderEngineDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public RenderEngine queryRenderEnginebyId(String id) {
		List renderEngines = sessionFactory.getCurrentSession().createQuery("from RenderEngine where id = ?").setString(0, id).list();
		if(renderEngines != null && renderEngines.size() == 1){
			return (RenderEngine)renderEngines.get(0);
		}
		return null;
	}
	@Override
	public RenderEngine queryRenderEnginebyName(String name) {
		List renderEngines = sessionFactory.getCurrentSession().createQuery("from RenderEngine where name = ?").setString(0, name).list();
		if(renderEngines != null && renderEngines.size() == 1){
			return (RenderEngine)renderEngines.get(0);
		}
		return null;
	}
	@Override
	public List<RenderEngine> queryAllRenderEngine(){
		List renderEngines = sessionFactory.getCurrentSession().createQuery("from RenderEngine").list();
		return renderEngines;
		
	}
	@Override 
	public void persist(RenderEngine renderEngine){
		sessionFactory.getCurrentSession().persist(renderEngine);
	}

}
