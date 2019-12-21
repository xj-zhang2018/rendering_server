package org.xjtu.framework.modules.manager.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.modules.user.service.ProjectService;

@ParentPackage("json-default")
@Namespace("/web/manager")
public class ProjectAjaxAction extends ManagerBaseAction{
	
	private @Resource ProjectService projectService;
	
    private String param;
    
    private Project currentUser;
    
	private String status;
	
	private String info;
    	
	@Action(value = "validateProjectName", results = {@Result(name = SUCCESS, type = "json")})
	public String validateProjectName(){
		
		Project p=projectService.findProjectByProjectNameAndUserId(param,currentUser.getId());
		
		if(p != null){
			this.status = "n";
			this.info = "该用户已创建该工程，请更换工程名";
		}else{
			this.status = "y";
		}
		return SUCCESS;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Project getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Project currentUser) {
		this.currentUser = currentUser;
	}
	
}

