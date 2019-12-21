package org.xjtu.framework.modules.protocol;

import java.util.ArrayList;

public class ProgressMessage {
	
	private String message;

	private String taskId;
	
	private int reportedFrameNumber;
	
	private ArrayList<RenderingFrameProgress> reportedFrames;
	
	private int allFinished;
	
	private int idleNumber;
	
	
	public ProgressMessage(String message){
		this.message=message;
		
		//根据通信协议获取数据
		//格式如下：#taskstatus#id#number#rendering_frameprogress#name#value#progess#tag#info#redotag|progress…#finish|end
		String[] str=message.split("#");
		this.taskId=str[2];
		this.reportedFrameNumber=Integer.parseInt(str[3]);
		this.idleNumber=Integer.parseInt(str[4]);
		
		
		//处理消息中的帧进度信息
		int beginIndex=message.indexOf("#rendering_frameprogress");
		String frameMessage=message.substring(beginIndex);
		
		String[] frameInfos=frameMessage.split("\\|progress"); //"\\|"是为了转义
		
		int i;
		this.reportedFrames=new ArrayList<RenderingFrameProgress>();
		for(i=0; i<frameInfos.length-1;i++){ 
			RenderingFrameProgress reportedFrame=new RenderingFrameProgress();
			String tmp=frameInfos[i];
			String[] infos=tmp.split("#");
			
			reportedFrame.setFrameName(infos[2]);
			reportedFrame.setFinishedTag(Integer.parseInt(infos[3]));
			reportedFrame.setProgress(Double.parseDouble(infos[4]));
			reportedFrame.setError_Tag(Integer.parseInt(infos[5]));
			reportedFrame.setErrorInfo(infos[6]);
			reportedFrame.setRedoTag(Integer.parseInt(infos[7]));
			this.reportedFrames.add(i, reportedFrame);
		}
		
		int mark=frameInfos[i].indexOf("|end");
		this.allFinished=Integer.parseInt(frameInfos[i].substring(1,mark));
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getTaskId() {
		return taskId;
	}


	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public int getReportedFrameNumber() {
		return reportedFrameNumber;
	}


	public void setReportedFrameNumber(int reportedFrameNumber) {
		this.reportedFrameNumber = reportedFrameNumber;
	}


	public ArrayList<RenderingFrameProgress> getReportedFrames() {
		return reportedFrames;
	}


	public void setReportedFrames(ArrayList<RenderingFrameProgress> reportedFrames) {
		this.reportedFrames = reportedFrames;
	}


	public int getAllFinished() {
		return allFinished;
	}


	public void setAllFinished(int allFinished) {
		this.allFinished = allFinished;
	}


	public int getIdleNumber() {
		return idleNumber;
	}


	public void setIdleNumber(int idleNumber) {
		this.idleNumber = idleNumber;
	}

}
