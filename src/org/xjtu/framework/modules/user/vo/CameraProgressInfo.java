package org.xjtu.framework.modules.user.vo;

import java.util.Date;

public class CameraProgressInfo {
	
	private String cameraId;
	private Integer cameraStatus;
	private Integer cameraProgress;
	private  Double renderCost;
	private Integer paymentStatus;
	private String timeInfo;
		
	public String getCameraId() {
		return cameraId;
	}
	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
	}
	public Integer getCameraStatus() {
		return cameraStatus;
	}
	public void setCameraStatus(Integer cameraStatus) {
		this.cameraStatus = cameraStatus;
	}
	public Integer getCameraProgress() {
		return cameraProgress;
	}
	public void setCameraProgress(Integer cameraProgress) {
		this.cameraProgress = cameraProgress;
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

}
