package org.xjtu.framework.modules.manager.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.backstage.schedule.JobProgressUpdate;
import org.xjtu.framework.core.base.constant.RegistReturnInfo;
import org.xjtu.framework.core.base.constant.UserType;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.LinuxInvoker;
import org.xjtu.framework.core.util.MD5;
import org.xjtu.framework.modules.user.service.ProjectService;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.vo.RegisterUserInfo;
import org.xjtu.framework.modules.user.webservice.RenderUserService;

import com.opensymphony.xwork2.ActionContext;

@ParentPackage("struts-default")
@Namespace("/web/manager")
public class UserManageAction extends ManagerBaseAction{
	
	private @Resource UserService userService;
	private @Resource RenderUserService renderUserService;
	private @Resource ProjectService projectService;
	
	public static  User  existedUser;
	private static final Logger log = Logger.getLogger(UserManageAction.class);
    private int pageTotal = 0;
    
    private int pageSize = 10;
    
    private int pageNum = 1;
    
    private String searchText = "";
    
    private String searchType = "name";
    
    private String userId;

    private User user;
    
    private RegisterUserInfo registerUserInfo;

	private List<String> userIds;

	private List<User> users;
	
	private Integer isChgPasswd;
	
	private Project currentProject;
	
	private Map<String,String> mapScenePath;
	
	private String attachmentName;
	private File attachment;
	
	@Action(value = "userList", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "/web/manager/userList.jsp")})
	public String list(){
		
		this.menuName="userManage";

		int totalSize = userService.findTotalCountByQuery(searchText, searchType, UserType.MANAGER);
		
		this.pageTotal = (totalSize-1)/this.pageSize + 1;
		
		users = userService.listUsersByQuery(searchText, searchType, pageNum, pageSize, UserType.MANAGER);
				
		return SUCCESS;
	}

	
	@Action(value = "usersDelete", results = { @Result(name = "login", location = "/web/login.jsp"),
			@Result(name = SUCCESS, location = "/userList.action", type="redirectAction")})
	public String usersDelete(){
	
		this.menuName="userManage";
		
		if(userIds!=null){
			//删除远程路径
			try {
				for(int i=0; i<userIds.size(); i++){
				User user=userService.findUserById(userIds.get(i));
    			//删除他的路径
    			LinuxInvoker ink = new LinuxInvoker();
    			ink.setCmd("rm -rf "+user.getHomeDir());
				ink.executeCommand();
			} 
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			userService.deleteUsersByIds(userIds);
			return SUCCESS;
		}
		else
			return ERROR;
	}
		
	
	@Action(value = "inUserEdit", results = { @Result(name = "login", location = "/web/login.jsp"),
			@Result(name = SUCCESS, location = "/web/manager/userEdit.jsp")})
	public String inUserEdit(){

		this.menuName="userManage";
		
		if(userId!=null){
			
			user=userService.findUserById(userId);			
		    return SUCCESS;
		}
		else
			return ERROR;
	}
	
	
	@Action(value = "userUpdate", results = { @Result(name = "login", location = "/web/login.jsp"),
			@Result(name = SUCCESS,  location = "/userList.action", type="redirectAction")})
	public String userUpdate(){

		this.menuName="userManage";
							
		user=userService.findUserById(userId);
		
		if(user!=null){
			
			
			try {
				String Old_homePath=user.getHomeDir();
				Old_homePath=Old_homePath.substring(0,Old_homePath.lastIndexOf("/"));
				String new_homePath=Old_homePath.substring(0,Old_homePath.lastIndexOf("/"))+"/"+registerUserInfo.getName();
				LinuxInvoker ink = new LinuxInvoker();
				ink.setCmd("mv "+Old_homePath+" "+new_homePath);
				ink.executeCommand();
				user.setHomeDir(new_homePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			user.setName(registerUserInfo.getName());
    	    user.setEmail(registerUserInfo.getEmail());
    	    user.setMobile(registerUserInfo.getMobile());
    	    if(isChgPasswd!=null&&isChgPasswd==1)
    	    	user.setPassword(MD5.encode(registerUserInfo.getPassword()));
    	    
    	    userService.updateUserInfo(user);
    	    return SUCCESS;
		}else 
			return ERROR;

	}
		
	
	@Action(value = "doNewUser", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "/userList.action", type="redirectAction")})
	public String doNewUser(){
		this.menuName="userManage";
		if(registerUserInfo!=null){
			int ret=renderUserService.doRegister(registerUserInfo);
			if(ret==RegistReturnInfo.success){
				return SUCCESS;
			}else
				return ERROR;
		}else
			return ERROR;
	}
	
	@Action(value = "inFileUpload", results = { @Result(name = "login", location = "/web/login.jsp"),
    		/*@Result(name = SUCCESS, location = "/web/manager/fileUpload.jsp"),*/
    		@Result(name = SUCCESS, location = "/uploadA.jsp"),
    		@Result(name = ERROR, location = "/web/404.jsp")})
	public String inFileUpload(){
		
		if(currentProject!=null&&currentProject.getId()!=null){
			Project p=projectService.findProjectById(currentProject.getId());
			
			System.out.println("get project is "+p);
			
			if(p!=null){
				log.info("project is not null");
				existedUser=p.getUser();
				return SUCCESS;
			}else{
				System.out.println("erro is occure");
				return ERROR;		
			}
		}else if(userId!=null){
			existedUser=userService.findUserById(userId);
			ActionContext actionContext = ActionContext.getContext();
		       Map<String, Object> mapSession = actionContext.getSession();
		       mapSession.put("existedUser", existedUser);	//后续修改上传路径
		       System.out.println("existedUser:"+existedUser);


			return SUCCESS;
		}else{
			
			return ERROR;
		}
	}
	
	@Action(value = "inScriptUpload", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "/web/manager/scriptUpload.jsp")})
	public String inScriptUpload(){
		this.menuName="scriptUpload";
		return SUCCESS;
	}
	
	
	@Action(value = "ieScriptUpload", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "/web/manager/scriptUpload.jsp")})
	public String iesScriptUpload() throws IOException {
		
		File file;
			
		file = new File(this.getClass().getResource("/").getPath()+"shell/"+new String(attachmentName.getBytes("iso8859-1"),"utf-8")); 
		System.out.print("wangleilei"+this.getClass().getResource("/").getPath()+"shell/");
		file.createNewFile();
		
		FileOutputStream fos=new FileOutputStream(file);
		InputStream is=new FileInputStream(attachment);;
		
		byte[] bytes = new byte[1024];
		int c;
		while ((c = is.read(bytes)) != -1) {
			fos.write(bytes, 0, c);
		}
		is.close();
		fos.close();
		
		return SUCCESS;
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
	
    public String getUserId() {
		return userId;
	}
    
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public RegisterUserInfo getResgisterUserInfo() {
		return registerUserInfo;
	}
	
	public void setResgisterUserInfo(RegisterUserInfo resgisterUserInfo) {
		this.registerUserInfo = resgisterUserInfo;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public List<String> getUserIds() {
		return userIds;
	}
	
	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}


	public RegisterUserInfo getRegisterUserInfo() {
		return registerUserInfo;
	}


	public void setRegisterUserInfo(RegisterUserInfo registerUserInfo) {
		this.registerUserInfo = registerUserInfo;
	}


	public Integer getIsChgPasswd() {
		return isChgPasswd;
	}


	public void setIsChgPasswd(Integer isChgPasswd) {
		this.isChgPasswd = isChgPasswd;
	}


	public Project getCurrentProject() {
		return currentProject;
	}


	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}


	public Map<String, String> getMapScenePath() {
		return mapScenePath;
	}


	public void setMapScenePath(Map<String, String> mapScenePath) {
		this.mapScenePath = mapScenePath;
	}


	public User getExistedUser() {
		return existedUser;
	}


	public void setExistedUser(User existedUser) {
		this.existedUser = existedUser;
	}


	public String getAttachmentName() {
		return attachmentName;
	}


	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}


	public File getAttachment() {
		return attachment;
	}


	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}
		
}
