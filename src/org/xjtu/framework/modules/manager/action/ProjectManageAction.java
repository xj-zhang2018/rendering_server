package org.xjtu.framework.modules.manager.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.constant.ProjectStatus;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.modules.user.service.ProjectService;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.vo.JobListInfo;
import org.xjtu.framework.modules.user.vo.ProjectInfo;
import org.xjtu.framework.modules.user.vo.ProjectInfomas;

@ParentPackage("struts-default")
@Namespace("/web/manager")
public class ProjectManageAction extends ManagerBaseAction{
	
	private @Resource ProjectService projectService;
	private @Resource UserService userService;

	private String project_client_id;
	
    private int pageTotal = 0;
    
    private int pageSize = 10;
    
    private int pageNum = 1;
    
    private String searchText = "";
    
    private String searchType = "name";
    
    private String projectId;
	
	private String userId;
	
	private User currentUser;
	
	private List<User> users;
    
	private ProjectInfo projectInfo;
    
    private Project project;   
    
	private List<Project> projects;
	
	private List<ProjectInfomas> projectInfomas;
	
	
    private List<String> projectIds;
	
	@Action(value = "projectList", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "/web/manager/projectList.jsp")})
	public String list(){
		
		this.menuName="projectManage";

		int totalSize = projectService.findTotalCountByQuery(searchText, searchType);
		this.pageTotal = (totalSize-1)/this.pageSize + 1;
		
		projectInfomas=new ArrayList<ProjectInfomas>();	
		projects = projectService.listProjectsByQuery(searchText, searchType, pageNum, pageSize);
		
		Boolean flag=true;
		for(int i=0;i<projects.size();i++){
			ProjectInfomas projectInfoma =new ProjectInfomas();
			projectInfoma.setCamerasNum(projects.get(i).getCamerasNum());
			projectInfoma.setId(projects.get(i).getId());
			projectInfoma.setName(projects.get(i).getName());
			projectInfoma.setJobs(projects.get(i).getJobs());
			projectInfoma.setUser(projects.get(i).getUser());
			
			
			
			
			if(projectInfoma.getCamerasNum()>0 && flag==true){
				flag=false;
				projectInfoma.setAllFramesNum(projectService.findFinishFramesNum());
				projectInfoma.setEndTime(projectService.findLastendTime());
				projectInfoma.setStartTime(projectService.findEarlystartTime());	
			}
			projectInfomas.add(projectInfoma);
			
		}
		
				
		return SUCCESS;
	}
		
		
	public List<ProjectInfomas> getProjectInfomas() {
		return projectInfomas;
	}


	public void setProjectInfomas(List<ProjectInfomas> projectInfomas) {
		this.projectInfomas = projectInfomas;
	}


	@Action(value = "projectsDelete", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "projectList", type="chain"),
    		@Result(name = ERROR, location ="projectList", type="chain")})
	public String projectsDelete(){
		
		this.menuName="projectManage";
		
		if(currentUser!=null){
			searchType="accurateUserId";
			searchText=currentUser.getId();
		}
		
		if(projectIds!=null){
			
			for(int i=0;i<projectIds.size();i++){

				Project project=projectService.findProjectById(projectIds.get(i));
					
				if(project!=null){
							
					projectService.deleteProject(project);

				}
					
			}		
			
			return SUCCESS;
		}
		else
			return ERROR;
	}
	
	@Action(value = "inProjectEdit", results = { @Result(name = "login", location = "/web/login.jsp"),
			@Result(name = SUCCESS, location = "/web/manager/projectEdit.jsp"),
			@Result(name = ERROR, location ="projectList", type="chain")})
	public String inProjectEdit(){
		
		if(projectId!=null){			
			project=projectService.findProjectById(projectId);			
			return SUCCESS;			
		}
		else
			return ERROR;
	}
	
	@Action(value = "doProjectUpdate", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS,  location = "projectList", type="chain"),
    		@Result(name = ERROR, location ="projectList", type="chain")})
	public String doProjectUpdate(){

		//浼犻�掔粰projectList.action浣跨敤
		if(currentUser!=null){
			searchType="accurateUserId";
			searchText=currentUser.getId();
		}
		
		if(projectInfo!=null)
		{
			Project existedProject=projectService.findProjectById(projectInfo.getId());
			
			if(existedProject!=null){
				existedProject.setName(projectInfo.getName());
				projectService.updateProjectInfo(existedProject);
				
				this.errorType="success";
				return SUCCESS;
			}else{
				
	    		this.errorType="error";
	    		this.errorCode="该工程不存在!";
	    		return ERROR;
			}
				
    	}else{			
    		this.errorType="error";
    		this.errorCode="工程名不能为空！";
    		return ERROR;			
		}
	}
	
	@Action(value = "inNewProject", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location ="/web/manager/newProject.jsp")})
	public String inNewProject(){
	
		this.menuName="projectManage";
		
		if(userId!=null&&!userId.equals("")){
			
			users=userService.findAllUsers();
			currentUser=userService.findUserById(userId);
			return SUCCESS;
		}else{
			users=userService.findAllUsers();
			return SUCCESS;
		}
		
	}
	@Action(value = "doNewProject", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "projectList", type="chain"),
			@Result(name = ERROR, location ="inNewProject", type="chain")})
	public String doNewProject(){

		this.menuName="projectManage";
		
		if(project_client_id!=null){
			
			User existedUser=userService.findUserById(project_client_id);
			
			if(existedUser!=null){
				
				if(projectInfo!=null){
					Project p=new Project();
					String uuId=UUIDGenerator.getUUID();
					projectInfo.setId(uuId); //鐢ㄤ簬璋冪敤璇ユ帴鍙oAddProject鐨勮皟鐢ㄨ�呰幏鍙栨柊鐢熸垚鐨勫伐绋嬬殑id鍙�
					p.setId(uuId);
					p.setName(projectInfo.getName());
					p.setCreateTime(new Date());

					p.setCamerasNum(0); //鏂板缓鐨勫伐绋嬪洜涓烘病鏈夐暅澶达紝鎵�浠ラ暅澶存暟榛樿涓�0
					p.setProjectStatus(ProjectStatus.notStart);
					p.setProjectProgress(0);
					p.setUser(existedUser);
					projectService.addProject(p);
					
					//浼犻�掔粰projectList.action浣跨敤
					if(currentUser!=null){
						searchType="accurateUserId";
						searchText=currentUser.getId();
					}
					
					return SUCCESS;
				}else{
    	    		this.errorType="error";
    	    		this.errorCode="请填写工程信息！";
    	    		return ERROR;
				}
			}else{
	    		this.errorType="error";
	    		this.errorCode="用户不存在！";
	    		return ERROR;
			}		
		}else{			
	    	this.errorType="error";
	    	this.errorCode="请选择用户！";
	    	return ERROR;			
		}
			
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
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
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}

	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}

	public Project getProject() {
		return project;
	}


	public void setProject(Project project) {
		this.project = project;
	}

	public List<Project> getProjects() {
		return projects;
	}	

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<String> getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(List<String> projectIds) {
		this.projectIds = projectIds;
	}

	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public User getCurrentUser() {
		return currentUser;
	}


	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}


	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}


	public String getProject_client_id() {
		return project_client_id;
	}


	public void setProject_client_id(String project_client_id) {
		this.project_client_id = project_client_id;
	}
	
}
