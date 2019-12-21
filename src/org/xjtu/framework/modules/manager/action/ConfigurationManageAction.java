package org.xjtu.framework.modules.manager.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.modules.user.service.ConfigurationService;
import org.xjtu.framework.modules.user.service.RenderEngineService;
import org.xjtu.framework.modules.user.vo.ConfigurationInfo;
import org.xjtu.framework.core.base.model.Configuration;
import org.xjtu.framework.core.base.model.RenderEngine;
import org.xjtu.framework.core.util.UUIDGenerator;

@ParentPackage("struts-default")
@Namespace("/web/manager")
public class ConfigurationManageAction extends ManagerBaseAction {
	
	private Configuration  configuration = new Configuration();
	
	private RenderEngine renderEngine = new RenderEngine();
	
	private ConfigurationInfo configurationInfo;
	
	private String newRenderEngine;
	
	private @Resource ConfigurationService configurationService;
	
	private @Resource RenderEngineService  renderEngineService ;
	
	
	@Action(value = "configurationList", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "/web/manager/configurationEdit.jsp")})		
	public String configurationList(){
		
		this.menuName="configurationManage";
		setConfiguration(configurationService.findAllConfiguration());
		return SUCCESS;
		
	}
	
	@Action(value = "configurationUpdate", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "/web/manager/configurationEdit.jsp")})		
	public String configurationUpdate(){	
		this.menuName="configurationManage";
		configuration.setId("1");
		configurationService.updateConfigurationInfo( configuration);	
		if(newRenderEngine!=null && !newRenderEngine.equals("")){
			String uuId=UUIDGenerator.getUUID();
			renderEngine.setName(newRenderEngine);
			renderEngine.setId(uuId);
			renderEngineService.addRenderEngine(renderEngine);
		}
		this.addActionMessage("配置信息修改成功");
		return SUCCESS;	
	}


	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	public ConfigurationInfo getConfigurationInfo() {
		return configurationInfo;
	}
	public void setConfigurationInfo(ConfigurationInfo configurationInfo) {
		this.configurationInfo = configurationInfo;
	}
	public String getNewRenderEngine() {
		return newRenderEngine;
	}
	public void setNewRenderEngine(String newRenderEngine) {
		this.newRenderEngine = newRenderEngine;
	}


}
