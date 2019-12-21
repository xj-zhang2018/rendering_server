package org.xjtu.framework.modules.user.dao;

import java.util.List;

import org.xjtu.framework.core.base.model.RenderEngine;

public interface RenderEngineDao {
	public RenderEngine queryRenderEnginebyId(String id);
	public RenderEngine queryRenderEnginebyName(String name);
	public List<RenderEngine> queryAllRenderEngine();
	public void persist(RenderEngine renderEngine);
	
}
