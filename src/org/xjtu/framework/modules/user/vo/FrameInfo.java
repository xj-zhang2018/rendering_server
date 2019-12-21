package org.xjtu.framework.modules.user.vo;

import java.util.Date;

public class FrameInfo {

	private String id;
	private String frameName;
	private Integer frameStatus;
	private Integer frameProgress;
	private String errorInfo;

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
}
