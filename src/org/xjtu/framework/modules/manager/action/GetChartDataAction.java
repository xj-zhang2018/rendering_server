package org.xjtu.framework.modules.manager.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.JobService;
import java.util.Calendar;
import javax.annotation.Resource;

@ParentPackage("json-default")
@Namespace("/web/manager")

public class GetChartDataAction extends ManagerBaseAction {
	 
	 private List<String> strEndTime;
	 private List<Integer> jobStartNums; 
	 private List<Integer> jobCreateNums;
	 private List<Integer> jobEndNums;
	 private int freeNodes;
	 private int busyNodes;
	 private int downNodes;
	 
	 private @Resource JobService jobService;
	 private @Resource ClusterManageService clusterManageService;
/*
 * author:wanglei
 * date:2013-10-11
 * 给homeHighcharts.js(显示主界面的工作折线图)传送Json数据	 
 */
  	@Action(value = "queryChartData", results = {@Result(name = SUCCESS, type = "json")})
	public String queryChartData(){ 
  		
  		
		setJobStartNums(new ArrayList<Integer>());
		setStrEndTime(new ArrayList<String>() ) ;
		setJobCreateNums(new ArrayList<Integer>());	
		setJobEndNums(new ArrayList<Integer>());	
	
	 	jobService.findNewJobCountsByDate(new Date());
	 	jobService.findStartJobCountsByDate(new Date());
	 	jobService.findEndJobCountsByDate(new Date());
		int createCounts,endCounts,stateCounts;
		String time;
		Date date=new Date();		
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -7);
		
		for( int i=0; i<7 ;i++){
			cal.add(Calendar.DATE, +1);
			date=cal.getTime();			
			createCounts=jobService.findNewJobCountsByDate(date);
			stateCounts=jobService.findStartJobCountsByDate(date);
			endCounts=jobService.findEndJobCountsByDate(date);
			jobCreateNums.add(createCounts);
			jobStartNums.add(stateCounts);
			jobEndNums.add(endCounts);									
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");  
	        time=formatter.format(date); 
			strEndTime.add(time);				
		}
		
	    return SUCCESS;			
	}
/*
 * author:wanglei
 * date:2013-10-11
 * 给nodesPiecharts.js(显示主界面的节点状态圆饼图)传送Json数据	 
 */
  	
  	@Action(value = "queryNodesPieChartData", results = {@Result(name = SUCCESS, type = "json")})
	public String queryNodesPieChartData (){
  		 		
 		freeNodes=clusterManageService.getFreeNodesNum();
  		busyNodes=clusterManageService.getJobExclusiveNodesNum();
  		downNodes=clusterManageService.getDownNodesNum();
  		  		
	    return SUCCESS;			
	}
  	
	public List<String> getStrEndTime() {
		return strEndTime;
	}

	public void setStrEndTime(List<String> strEndTime) {
		this.strEndTime = strEndTime;
	}

	public List<Integer> getJobCreateNums() {
		return jobCreateNums;
	}

	public void setJobCreateNums(List<Integer> jobCreateNums) {
		this.jobCreateNums = jobCreateNums;
	}

	public List<Integer> getJobStartNums() {
		return jobStartNums;
	}

	public void setJobStartNums(List<Integer> jobStartNums) {
		this.jobStartNums = jobStartNums;
	}

	public List<Integer> getJobEndNums() {
		return jobEndNums;
	}

	public void setJobEndNums(List<Integer> jobEndNums) {
		this.jobEndNums = jobEndNums;
	}

	public int getFreeNodes() {
		return freeNodes;
	}

	public void setFreeNodes(int freeNodes) {
		this.freeNodes = freeNodes;
	}

	public int getBusyNodes() {
		return busyNodes;
	}

	public void setBusyNodes(int busyNodes) {
		this.busyNodes = busyNodes;
	}

	public int getDownNodes() {
		return downNodes;
	}

	public void setDownNodes(int downNodes) {
		this.downNodes = downNodes;
	}
		
}

	
	


