package org.xjtu.framework.modules.user.vo;

import java.util.Date;

import org.xjtu.framework.core.base.model.User;

public class ProjectInfo {

	private String id;
	private String name;
	private Date createTime;
	private Integer camerasNum;
	private Integer projectStatus;
	private Integer projectProgress;

	
	private User user;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCamerasNum() {
		return camerasNum;
	}

	public void setCamerasNum(Integer camerasNum) {
		this.camerasNum = camerasNum;
	}

	public Integer getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(Integer projectStatus) {
		this.projectStatus = projectStatus;
	}

	public Integer getProjectProgress() {
		return projectProgress;
	}

	public void setProjectProgress(Integer projectProgress) {
		this.projectProgress = projectProgress;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
