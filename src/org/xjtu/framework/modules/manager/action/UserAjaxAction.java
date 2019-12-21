package org.xjtu.framework.modules.manager.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.vo.NodesInfo;

@ParentPackage("json-default")
@Namespace("/web/manager")
public class UserAjaxAction extends ManagerBaseAction{
	
	private @Resource UserService userService;
	private @Resource ClusterManageService clusterManageService;
	
	private String param;
	private String status;	
	private String info;
	
	private int pageSize = 30;	
	private int pageTotal = 0;
	private int pageNum=1;
	
	@Action(value = "validateUserName", results = {@Result(name = SUCCESS, type = "json")})
	public String validateUserName(){
		User user = userService.findUserByName(param);
		if(user != null){
			this.status = "n";
			this.info = "您输入的用户名已存在，请重新换一个";
		}else{
			this.status = "y";
		}
		return SUCCESS;
	}
	
	@Action(value = "validateEmail", results = {@Result(name = SUCCESS, type = "json")})
	public String validateEmail(){
		User user = userService.findUserByEmail(param);
		if(user != null){
			this.status = "n";
			this.info = "您输入的邮箱已存在，请重新换一个！";
		}else{
			this.status = "y";
		}
		return SUCCESS;
	}
	
	@Action(value = "listNodeGroupLength", results = {@Result(name = SUCCESS, type = "json")})
	public String listNodeGroupLength(){
		
		int allNodesNum=clusterManageService.getAllNodesNum();
		
		this.pageTotal = allNodesNum/this.pageSize + 1;
				
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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	
}

