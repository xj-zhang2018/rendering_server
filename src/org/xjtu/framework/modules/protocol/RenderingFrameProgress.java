package org.xjtu.framework.modules.protocol;
/*
    Char * finished_frame_name;  //已完成帧名
	Int  finished_frame_value;  //已完成帧的结束标记  1:已完成 0:未完成
	Double  progress;  //该帧渲染进度
	Int  error_tag;  //帧出错标记 0: 成功  非0: 有错误
	Char * error_info;  //渲染帧报错信息
	Int redo_tag;  //是否准备单元内重新渲染 0: 不重新渲染  1: 重新渲染
*/
public class RenderingFrameProgress {

	private String FrameName;
	private int FinishedTag;
	private double Progress;
	private int Error_Tag;
	private String ErrorInfo;
	private int RedoTag;

	public String getFrameName() {
		return FrameName;
	}
	public void setFrameName(String frameName) {
		FrameName = frameName;
	}
	public int getFinishedTag() {
		return FinishedTag;
	}
	public void setFinishedTag(int finishedTag) {
		FinishedTag = finishedTag;
	}
	public double getProgress() {
		return Progress;
	}
	public void setProgress(double progress) {
		Progress = progress;
	}
	public int getError_Tag() {
		return Error_Tag;
	}
	public void setError_Tag(int error_Tag) {
		Error_Tag = error_Tag;
	}
	public String getErrorInfo() {
		return ErrorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		ErrorInfo = errorInfo;
	}
	public int getRedoTag() {
		return RedoTag;
	}
	public void setRedoTag(int redoTag) {
		RedoTag = redoTag;
	}

}
