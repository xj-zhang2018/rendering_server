package org.xjtu.framework.modules.protocol;

import java.util.ArrayList;

/*
  	Int  TaskID;  //记录task的ID，唯一的
	Char * camera_path;  //镜头路径
	Int  frame_number;  //记录这个task包含多少帧
	Char **  frame_names;  //记录帧的名字
	Long  needed_memory_size;  //预估需要的内存大小
	Int  prerender_tag;  //预渲染标识 0:正常渲染 1:预渲染
	Int  x_resolution;
	Int  y_resolution;     //预渲染参数
	Int  sample_rate;
*/
public class TaskDiscription {

	private String taskID;
	private String cameraPath;
	private int frameNumber;
	private ArrayList<String> frameNames;
	private int memorySize;
	private int preRenderTag;
	private int x_Resolution;
	private int y_Resolution;
	private int sampleRate;
	
	
	
	public String connectString() {
		//根据TaskDiscription拼接字符串
		String str= "#taskdiscription";
		str=str+"#"+this.getTaskID();
		str=str+"#"+this.getCameraPath();
		str=str+"#"+this.getFrameNumber();
		
		for(int i=0;i<this.getFrameNames().size();++i)
		{
			str=str+"#"+this.getFrameNames().get(i);
		}
		
		str=str+"#"+this.getMemorySize();
		str=str+"#"+this.getPreRenderTag();
		if (this.getPreRenderTag()==1) {//预渲染，后面有三个参数
			str=str+"#"+this.getX_Resolution();
			str=str+"#"+this.getY_Resolution();
			str=str+"#"+this.getSampleRate();
		} else {//正常渲染，后面三个#
			str=str+"#0#0#0";
		}
		str=str+"|end";
		return str;
	}
	
	
	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public String getCameraPath() {
		return cameraPath;
	}

	public void setCameraPath(String cameraPath) {
		this.cameraPath = cameraPath;
	}

	public int getFrameNumber() {
		return frameNumber;
	}

	public void setFrameNumber(int frameNumber) {
		this.frameNumber = frameNumber;
	}

	public int getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}

	public int getPreRenderTag() {
		return preRenderTag;
	}

	public void setPreRenderTag(int preRenderTag) {
		this.preRenderTag = preRenderTag;
	}

	public int getX_Resolution() {
		return x_Resolution;
	}

	public void setX_Resolution(int x_Resolution) {
		this.x_Resolution = x_Resolution;
	}

	public int getY_Resolution() {
		return y_Resolution;
	}

	public void setY_Resolution(int y_Resolution) {
		this.y_Resolution = y_Resolution;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	public void setFrameNames(ArrayList<String> frameNames) {
		this.frameNames = frameNames;
	}

	public ArrayList<String> getFrameNames() {
		return frameNames;
	}
	
}

