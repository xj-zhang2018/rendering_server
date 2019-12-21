package org.xjtu.framework.modules.user.service;

import java.util.List;

import org.xjtu.framework.core.base.model.RenderEngine;

public interface RenderEngineService {
	public RenderEngine findRenderEnginebyId(String id);
	public RenderEngine findRenderEnginebyName(String name);
	public List<RenderEngine> findAllRenderEngines();
	public void addRenderEngine(RenderEngine renderEngine);
}
