package org.xjtu.framework.modules.user.vo;

import java.util.Date;

import org.xjtu.framework.core.base.model.RenderEngine;

public class JobListInfo {
	private String id;
	private String cameraName;
	private String filePath;
	private Integer jobStatus;
	private Integer jobPriority;
	private Integer cameraProgress;
	private RenderEngine renderEngine;
	private Double renderCost;
	private Integer paymentStatus;
	private String timeInfo;//显示开始和结束时间
	private Integer isPreRender;
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
	public Integer getCameraProgress() {
		return cameraProgress;
	}
	public void setCameraProgress(Integer cameraProgress) {
		this.cameraProgress = cameraProgress;
	}
	public RenderEngine getRenderEngine() {
		return renderEngine;
	}
	public void setRenderEngine(RenderEngine renderEngine) {
		this.renderEngine = renderEngine;
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
	public String getTimeInfo() {
		return timeInfo;
	}
	public void setTimeInfo(String timeInfo) {
		this.timeInfo = timeInfo;
	}
	public Integer getIsPreRender() {
		return isPreRender;
	}
	public void setIsPreRender(Integer isPreRender) {
		this.isPreRender = isPreRender;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "careme is"+this.cameraName+" pretag is"+this.isPreRender;
	}

}
