package org.xjtu.framework.modules.manager.action;

import com.xj.framework.ssh.ShellLocal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.modules.user.service.JobService;
import org.xjtu.framework.modules.user.service.ProjectService;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.vo.CameraProgressInfo;
import org.xjtu.framework.modules.user.vo.ProjectInfo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.xjtu.framework.backstage.schedule.JobSchedule.jobs_render;
import static org.xjtu.framework.backstage.schedule.JobSchedule.temp_job_cameraName;

@ParentPackage("json-default")
@Namespace("/web/manager")
public class JobAjaxAction extends ManagerBaseAction{
	private @Resource JobService jobService;
	private @Resource ProjectService projectService;
	private @Resource UserService userService;
	private List<CameraProgressInfo> cameraProgressInfos;
    private int pageSize = 10;
	private int pageNum = 1;
    private String searchText = "";
    private String searchType = "name";
    private int jobStatus=-1;
    private String param;
    private Project currentProject;
	private String status;
	private String info;
	private List<String> jobIds;
	
	//doGetProjectsAndScenePath鍙傛暟
	private String project_client_id;
	private List<ProjectInfo> projects;
	private Map<String,String> mapScenePath;
	
	//validateFrameRange
	private String filePath;
	private int frameMax;

	private static final Log log = LogFactory.getLog(JobAjaxAction.class);
	
	@Action(value = "jobProgress", results = {@Result(name = SUCCESS, type = "json")})
	public String jobProgress(){
		List<Job> jobs = jobService.listJobsByQuery(searchText, searchType, pageNum, pageSize, jobStatus);
		
		if(jobs!=null){
			cameraProgressInfos=new ArrayList<CameraProgressInfo>();
			for(int i=0;i<jobs.size();i++){
				CameraProgressInfo cpi=new CameraProgressInfo();
				cpi.setCameraId(jobs.get(i).getId());
				cpi.setCameraProgress(jobs.get(i).getCameraProgress());
				cpi.setCameraStatus(jobs.get(i).getJobStatus());
				cpi.setPaymentStatus(jobs.get(i).getPaymentStatus());
				cpi.setRenderCost(jobs.get(i).getRenderCost());
				if(jobs.get(i).getEndTime()!=null&&jobs.get(i).getStartTime()!=null){
					cpi.setTimeInfo("startTime:"+jobs.get(i).getStartTime().toString().replaceAll(" ", "_")+"\n"+"endTime:"+jobs.get(i).getEndTime().toString().replaceAll(" ","_"));
				}
				else if(jobs.get(i).getStartTime()!=null){
					cpi.setTimeInfo("startTime:"+jobs.get(i).getStartTime().toString().replaceAll(" ", "_")+"\n"+"endTime:");
				}else{
					cpi.setTimeInfo("startTime:"+"\n"+"endTime");
				}
				
				
				cameraProgressInfos.add(cpi);
			}
			
		}
		return SUCCESS;
	}
	@Action(value = "validateCameraName", results = {@Result(name = SUCCESS, type = "json")})
	public String validateCameraName(){
		
		Job j=jobService.findJobByJobNameAndProjectId(param,currentProject.getId());
		
		if(j != null){
			this.status = "n";
			this.info = "工程已存在此镜头，请更换镜头名";
		}else{
			this.status = "y";
		}
		return SUCCESS;
	}
	
	@Action(value = "validateFrameRange", results = {@Result(name = SUCCESS, type = "json")})
	public String validateFrameRange(){
		
		if(filePath!=null&&!filePath.equals("")){
		
			/*淇敼鎴愬懡浠ゆ柟寮�*/
			//filePath=filePath.substring(0,filePath.lastIndexOf("/"));
			//String cmd="cd "+filePath+" && ls *.rib";
			
			
			
			// 10.25 Thys 更换引擎，更改检测帧范围方式
			String cmd = "cd "+filePath+" && cat frameMax.txt";
			ShellLocal shell = new ShellLocal(cmd);
			String result = shell.executeCmd();
			result = result.trim();
			int frameMax = Integer.parseInt(result);
			
			Map<Integer,String> mapScenePath = new HashMap<Integer,String>();
			
			//eg:1,5-8 = 1,5,6,7,8
			//判断数字有没有大于frameMax
			String[] ss = param.split(",");
			if(ss != null && ss.length > 0) {
				
				for(int i = 0;i < ss.length;i++){

					String[] temp=ss[i].split("-");
			
					if(temp != null && temp.length > 0){
					
						if(temp.length == 1){
						
							if(Integer.parseInt(temp[0]) > frameMax){
								this.status = "n";
								this.info = "您要渲染的部分帧超过可渲染最大范围";
								return SUCCESS;
								
							}
							
							mapScenePath.put(Integer.parseInt(temp[0]), "frame"+temp[0]+".xml");
												
						}else if(temp.length == 2){
							try{
								Integer end = Integer.parseInt(temp[1]);
								Integer begin = Integer.parseInt(temp[0]);
							
								if(end < begin)
								{		
									this.status = "n";
						    		this.info = "您填写的帧范围格式有错误";
						    		return SUCCESS;
								}
								for(int z = begin;z < end + 1;z++){
									if(z > frameMax){
										this.status = "n";
							    		this.info = "您要渲染的部分帧超过可渲染最大范围";
							    		return SUCCESS;
									}
									
									mapScenePath.put(z, "frame"+z+".xml");
								}
							}catch(Exception e){
								this.status = "n";
					    		this.info = "您填写的帧范围格式有错误";
					    		return SUCCESS;
							}

						}else{
							this.status = "n";
				    		this.info = "您填写的帧范围格式有错误";
				    		return SUCCESS;
						}
					}else{
						this.status = "n";
			    		this.info = "您填写的帧范围格式有错误";
			    		return SUCCESS;
					}
				
				}
				
			} else {
				this.status = "n";
	    		this.info = "您填写的帧范围格式有错误";
	    		return SUCCESS;
			}
		}
		this.status = "y";
		return SUCCESS;

	}
	@Action(value = "doGetProjectsByUser", results = {@Result(name = SUCCESS, type = "json")})
	public String doGetProjectsByUser(){
		if(project_client_id!=null){
			List<Project> projs=projectService.findProjectsByUserId(project_client_id);
			if(projs!=null&&projs.size()>0){
				projects=new ArrayList<ProjectInfo>();
				for(int i=0;i<projs.size();i++){
					ProjectInfo pi=new ProjectInfo();
					pi.setId(projs.get(i).getId());
					pi.setName(projs.get(i).getName());
					projects.add(pi);
				}
			}
		}
		return SUCCESS;
	}
	@Action(value = "doGetScenePathByUser", results = {@Result(name = SUCCESS, type = "json")})
	public String doGetScenePathByUser(){
		if(project_client_id!=null){
			User existedUser=userService.findUserById(project_client_id);
			if(existedUser!=null){
				try {

					String homeDir=existedUser.getHomeDir();
					ShellLocal shell=new ShellLocal("ls "+homeDir);
					String result=shell.executeCmd();

					result=result.trim();
					
					
					mapScenePath=new HashMap<String, String>();
					String[]filesName=result.split("\n");
					for(int i=0;i<filesName.length;i++){
				    	 String s=filesName[i];
						 mapScenePath.put(s, homeDir+s);//鏂囦欢鍚嶏紝鏂囦欢鐨勫叏璺緞
					}
					System.out.println("mapScenePath is"+mapScenePath);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
				
		return SUCCESS;
	}
	@Action(value = "doJobBegin", results = {@Result(name = SUCCESS, type = "json")})
	public String doJobBegin(){

		if(jobIds!=null){
			jobService.doStartCameraRender(jobIds);
		}
		return SUCCESS;
	}
	@Action(value = "doJobStop", results = {@Result(name = SUCCESS, type = "json")})
	public String doJobStop(){
		if(jobIds!=null){
			for(int i=0;i<jobIds.size();i++){
				Job job=jobService.findJobsById(jobIds.get(i));

				for(int j=0;i<jobs_render.size();j++) {
					if (jobs_render.get(j).getCameraName().equals(job.getCameraName())) {
						log.info("当前的jobs_render[" + j + "]" + "被删除");
						jobs_render.remove(j);
					}
				}

				if (job.getCameraName().equals(temp_job_cameraName)){
					log.info("正在执行停止job，执行前：temp_job_cameraName="+temp_job_cameraName);
					temp_job_cameraName="0";
					log.info("执行后：temp_job_cameraName="+temp_job_cameraName);
				}
				
				jobService.doStopCameraRender(jobIds.get(i));
			}
		}
		return SUCCESS;
	}
	
	@Action(value = "doJobSuspend", results = {@Result(name = SUCCESS, type = "json")})
	public String doJobSuspend(){

		if(jobIds!=null){
			for(int i=0;i<jobIds.size();i++){
				Job job=jobService.findJobsById(jobIds.get(i));


				for(int j=0;i<jobs_render.size();j++) {
					if (jobs_render.get(j).getCameraName().equals(job.getCameraName())) {
						log.info("当前的jobs_render[" + j + "]" + "被删除");
						jobs_render.remove(j);
					}
				}

				if (job.getCameraName().equals(temp_job_cameraName)){
					log.info("正在执行暂停job，执行前：temp_job_cameraName="+temp_job_cameraName);
					temp_job_cameraName="0";
					log.info("执行后：temp_job_cameraName="+temp_job_cameraName);

				}
				jobService.suspendJobByJobId(jobIds.get(i));
			}
			
		}
		
		return SUCCESS;
	}
	
	@Action(value = "doJobContinue", results = {@Result(name = SUCCESS, type = "json")})
	public String doJobContinue(){

		if(jobIds!=null){

			jobService.doStartCameraRender(jobIds);
			
		}
		
		return SUCCESS;
	}
	
	@Action(value = "doJobCopy", results = {@Result(name = SUCCESS, type = "json")})
	public String doJobCopy(){

		if(jobIds!=null){
			
			jobService.doCopyjobs(jobIds);
		}
		return SUCCESS;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public int getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(int jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<String> getJobIds() {
		return jobIds;
	}

	public void setJobIds(List<String> jobIds) {
		this.jobIds = jobIds;
	}

	public List<CameraProgressInfo> getCameraProgressInfos() {
		return cameraProgressInfos;
	}

	public void setCameraProgressInfos(List<CameraProgressInfo> cameraProgressInfos) {
		this.cameraProgressInfos = cameraProgressInfos;
	}

	public String getProject_client_id() {
		return project_client_id;
	}

	public void setProject_client_id(String project_client_id) {
		this.project_client_id = project_client_id;
	}

	public Map<String, String> getMapScenePath() {
		return mapScenePath;
	}

	public void setMapScenePath(Map<String, String> mapScenePath) {
		this.mapScenePath = mapScenePath;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<ProjectInfo> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectInfo> projects) {
		this.projects = projects;
	}
	
}

