package org.xjtu.framework.modules.user.vo;

import java.util.Date;
/*
 * 用于工程列表的现实
 * 添加时间：2016-01-05
 * wanglei
 */
import java.util.List;

import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.User;

public class ProjectInfomas {

	private String id;
	private String name;
	private Date createTime;
	private Integer camerasNum;
	private Integer projectStatus;
	private Integer projectProgress;
	private User user;
	private List<Job> jobs;
	private String StartTime;
	private String EndTime;
	private Integer AllFramesNum;
	
	
		public Integer getCamerasNum() {
			return camerasNum;
		}
		public void setCamerasNum(Integer camerasNum) {
			this.camerasNum = camerasNum;
		}
		
		public int getAllFramesNum() {
			return AllFramesNum;
		}
		public void setAllFramesNum(int i) {
			AllFramesNum = i;
		}
		
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
		public List<Job> getJobs() {
			return jobs;
		}
		public void setJobs(List<Job> jobs) {
			this.jobs = jobs;
		}
		public void setAllFramesNum(Integer allFramesNum) {
			AllFramesNum = allFramesNum;
		}
		public String getStartTime() {
			return StartTime;
		}
		public void setStartTime(String startTime) {
			StartTime = startTime;
		}
		public String getEndTime() {
			return EndTime;
		}
		public void setEndTime(String endTime) {
			EndTime = endTime;
		}
	


}
