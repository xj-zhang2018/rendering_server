package org.xjtu.framework.core.base.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name="tasks")
public class Task {
	private String id;
	
	private Integer frameNumber;
	private Integer taskProgress;
	private Integer taskStatus;
	private Integer taskPriority;
	private Integer taskType;
	private String filePath;
	private Date startTime;
	private Date endTime;
	private Date updateTime;


	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	private Unit unit;
	private Job job;
	
	private List<Frame> frames;

	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "unitId")	
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "jobId")	
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	
	
	public Integer getFrameNumber() {
		return frameNumber;
	}
	public void setFrameNumber(Integer frameNumber) {
		this.frameNumber = frameNumber;
	}
	public Integer getTaskProgress() {
		return taskProgress;
	}
	public void setTaskProgress(Integer taskProgress) {
		this.taskProgress = taskProgress;
	}
	public Integer getTaskPriority() {
		return taskPriority;
	}
	public void setTaskPriority(Integer taskPriority) {
		this.taskPriority = taskPriority;
	}
	public Integer getTaskType() {
		return taskType;
	}
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
	public Integer getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
	@Cascade(value = {CascadeType.SAVE_UPDATE,CascadeType.DELETE_ORPHAN,CascadeType.ALL}) 
	public List<Frame> getFrames() {
		return frames;
	}
	public void setFrames(List<Frame> frames) {
		this.frames = frames;
	}
	
}
