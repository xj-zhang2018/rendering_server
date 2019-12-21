package org.xjtu.framework.modules.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.model.RenderEngine;
import org.xjtu.framework.modules.user.dao.RenderEngineDao;
import org.xjtu.framework.modules.user.service.RenderEngineService;

@Service("renderEngineService")
public class RenderEngineServiceImpl implements RenderEngineService{

	private @Resource RenderEngineDao renderEngineDao;

	@Override
	public RenderEngine findRenderEnginebyId(String id) {
		return renderEngineDao.queryRenderEnginebyId(id);
	}
	@Override
	public RenderEngine findRenderEnginebyName(String name) {
		return renderEngineDao.queryRenderEnginebyName(name);
	}
	@Override
	public List<RenderEngine> findAllRenderEngines(){
		return renderEngineDao. queryAllRenderEngine();
		
	}
	@Override
	public void addRenderEngine(RenderEngine renderEngine) {
		renderEngineDao.persist(renderEngine);
	}


}
