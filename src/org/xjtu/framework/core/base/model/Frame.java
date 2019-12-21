package org.xjtu.framework.core.base.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="frames")
public class Frame {
	private String id;
	private String frameName;
	private Integer frameStatus;
	private Integer frameProgress;
	private String errorInfo;
	
	private Date startTime;
	private Date endTime;
	
	private Task task;

	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFrameName() {
		return frameName;
	}
	public void setFrameName(String frameName) {
		this.frameName = frameName;
	}
	public Integer getFrameStatus() {
		return frameStatus;
	}
	public void setFrameStatus(Integer frameStatus) {
		this.frameStatus = frameStatus;
	}
	public Integer getFrameProgress() {
		return frameProgress;
	}
	public void setFrameProgress(Integer frameProgress) {
		this.frameProgress = frameProgress;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "taskId")		
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
