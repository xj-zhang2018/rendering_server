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
@Table(name="jobs")
public class Job {
	
	private String id;
	private String cameraName;
	private String filePath;
	private Date createTime;
	private Integer jobStatus;
	private Integer queueNum;
	private Integer jobPriority;
	private Date startTime;
	private Date endTime;
	private Integer frameNumbers;
	private String frameRange;
	private Integer estimatedTime;
	private Integer estimatedSpace;
	private Integer cameraProgress;
	private Integer nodesNumber;	
	private Integer unitsNumber;
	private Integer preRenderingTag;
	private Integer xResolution;
	private Integer yResolution;
	private Integer sampleRate;
	
	
	
	private RenderEngine renderEngine;
	private Project project;

	private List<Task> tasks;
	
	private Double renderCost;
	private Integer paymentStatus;
	
	
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCameraName() {
		return cameraName;
	}
	public void setCameraName(String cameraName) {
		this.cameraName = cameraName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
	}
	public Integer getJobPriority() {
		return jobPriority;
	}
	public void setJobPriority(Integer jobPriority) {
		this.jobPriority = jobPriority;
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
	public Integer getFrameNumbers() {
		return frameNumbers;
	}
	public void setFrameNumbers(Integer frameNumbers) {
		this.frameNumbers = frameNumbers;
	}
	public String getFrameRange() {
		return frameRange;
	}
	public void setFrameRange(String frameRange) {
		this.frameRange = frameRange;
	}
	public Integer getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(Integer estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public Integer getEstimatedSpace() {
		return estimatedSpace;
	}
	public void setEstimatedSpace(Integer estimatedSpace) {
		this.estimatedSpace = estimatedSpace;
	}
	public Integer getCameraProgress() {
		return cameraProgress;
	}
	public void setCameraProgress(Integer cameraProgress) {
		this.cameraProgress = cameraProgress;
	}
	public Integer getNodesNumber() {
		return nodesNumber;
	}
	public void setNodesNumber(Integer nodesNumber) {
		this.nodesNumber = nodesNumber;
	}
	public Integer getUnitsNumber() {
		return unitsNumber;
	}
	public void setUnitsNumber(Integer unitsNumber) {
		this.unitsNumber = unitsNumber;
	}
	public Integer getPreRenderingTag() {
		return preRenderingTag;
	}
	public void setPreRenderingTag(Integer preRenderingTag) {
		this.preRenderingTag = preRenderingTag;
	}
	public Integer getxResolution() {
		return xResolution;
	}
	public void setxResolution(Integer xResolution) {
		this.xResolution = xResolution;
	}
	public Integer getyResolution() {
		return yResolution;
	}
	public void setyResolution(Integer yResolution) {
		this.yResolution = yResolution;
	}
	public Integer getSampleRate() {
		return sampleRate;
	}
	public void setSampleRate(Integer sampleRate) {
		this.sampleRate = sampleRate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projectId")
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Integer getQueueNum() {
		return queueNum;
	}
	public void setQueueNum(Integer queueNum) {
		this.queueNum = queueNum;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "renderEngineId")
	public RenderEngine getRenderEngine() {
		return renderEngine;
	}
	public void setRenderEngine(RenderEngine renderEngine) {
		this.renderEngine = renderEngine;
	}
	
	@Override 
	public boolean equals(Object obj) { 

		if (null != obj && obj instanceof Job) 
		{ 
			Job j = (Job)obj; 

			if (this.id.equals(j.id) && this.jobStatus.equals(j.jobStatus) && this.unitsNumber.equals(j.unitsNumber)) 
			{ 
				return true; 
			} 
		} 
		return false; 
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
	@Cascade(value = {CascadeType.SAVE_UPDATE,CascadeType.DELETE_ORPHAN,CascadeType.ALL}) 
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public Double getRenderCost() {
		return renderCost;
	}
	public void setRenderCost(Double renderCost) {
		this.renderCost = renderCost;
	}
	public Integer getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	

}
