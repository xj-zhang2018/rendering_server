package org.xjtu.framework.modules.normalUser.action;

import org.xjtu.framework.core.base.action.BaseAction;

public class UserBaseAction extends BaseAction{
  
protected String modulePath = "normalUser";
	
	protected String errorCode;
	
	protected String errorType;

	protected String themePath = "default";
	
	protected String actionPath = "index.jsp";
	
	
	
	public String getThemePath() {
		return themePath;
	}

	public void setThemePath(String themePath) {
		this.themePath = themePath;
	}
	
	public String getActionPath() {
		return actionPath;
	}

	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}
	
	public String getModulePath() {
		return modulePath;
	}

	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getPath(){
		return "theme/" + this.themePath + "/" + this.actionPath;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	

}
