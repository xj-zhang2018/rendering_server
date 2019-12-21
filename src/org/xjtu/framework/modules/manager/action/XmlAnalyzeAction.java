package org.xjtu.framework.modules.manager.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.constant.CalculateStatus;
import org.xjtu.framework.core.base.constant.JobPriority;
import org.xjtu.framework.core.base.constant.JobStatus;
import org.xjtu.framework.core.base.model.Calculate;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.RenderEngine;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.PbsExecute;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.modules.user.service.CalculateService;
import org.xjtu.framework.modules.user.service.JobService;
import org.xjtu.framework.modules.user.service.ProjectService;
import org.xjtu.framework.modules.user.service.RenderEngineService;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.vo.CalculateInfo;
import org.xjtu.framework.modules.user.vo.CalculateInfomas;
import org.xjtu.framework.modules.user.vo.CameraInfo;


/**
 * 2018/10/23
 * 鍏充簬XML鏂囦欢瑙ｆ瀽
 * @author 瀹嬮煢
 *
 */
@ParentPackage("struts-default")
@Namespace("/web/manager")
public class XmlAnalyzeAction extends ManagerBaseAction {
	
	private @Resource UserService userService;
	private @Resource CalculateService calculateService;
	private @Resource JobService jobService;
	private User user;
	private @Resource ProjectService projectService;	
	private List<User> users;
	private List<Job> jobs;

	private List<String> xmlIds;//瀵瑰簲xmlAnalyzeList.jsp椤甸潰涓〃涓瘡琛屾暟鎹墠涓�涓墦閽╃殑妗�
	private String xmlName;
	private String xmlCreateTime;//鍒涘缓鏃堕棿
	private int xmlStatus;//鐘舵��
	private int xmlProgress;//杩涘害
	private int xmlPriority;//浼樺厛绾�
	private int newXmlPriority = 2;//鍦╟alculate.js鏂囦欢涓渶瑕佺敤鍒�
	private String xmlId;//瀵瑰簲xmlAnalyzeList.jsp椤甸潰inCalculateEdit.action?xmlId=${id}
	private String userId;

	private String searchText = "";
    private String searchType = "name";
    private int pageTotal = 0;
    private int pageSize = 10;
    private int pageNum = 1;
    
    private CalculateInfo calculateInfo;
    private List<CalculateInfomas> calculateInfomas;//椤甸潰杩唬寰幆杈撳嚭琛ㄤ俊鎭氨鏄潬瀹冿紝灏嗚繖涓彉閲忔斁鍏mlAnalyzeList.jsp鐨�<s:iterator>涓�
    private List<Calculate> calculates;
    private User currentUser;
	private Job currentJob;
    private Calculate calculate;
    private String calculate_client_id;
    private String calculate_job_id;
    
    private String job_project_id;
	private String jobId;

    private Project currentProject;

    private Map<String,String> mapScenePath;


	//鏌ョ湅xml鏂囦欢淇℃伅鍒楄〃
	@Action(value="xmlAnalyzeList",results={
		@Result(name="login",location="/web/login.jsp"),
		@Result(name="success",location="/web/manager/xmlAnalyzeList.jsp")
	} )
	public String xmlAnalyzeList(){
		this.menuName="xmlAnalyze";
		int totalSize = calculateService.findTotalCountByQuery(searchText, searchType);
		this.pageTotal = (totalSize-1)/this.pageSize + 1;
		
		calculateInfomas=new ArrayList<CalculateInfomas>();
		calculates=calculateService.listCalculatesByQuery(searchText, searchType, pageNum, pageSize);
		
		for(int i=0;i<calculates.size();i++){
			CalculateInfomas calculateInfo=new CalculateInfomas();
			calculateInfo.setXmlName(calculates.get(i).getXmlName());
			calculateInfo.setId(calculates.get(i).getId());
			calculateInfo.setXmlCreateTime(calculates.get(i).getXmlCreateTime());
			calculateInfo.setXmlPriority(calculates.get(i).getXmlPriority());
			calculateInfo.setXmlProgress(calculates.get(i).getXmlProgress());
			calculateInfo.setXmlStatus(calculates.get(i).getXmlStatus());
			calculateInfo.setUser(calculates.get(i).getUser());
			calculateInfomas.add(calculateInfo);
		}
		System.out.println(calculateInfomas.size());
		return "success";
	}
	
	
	
	//璺宠浆鍒版坊鍔爔ml浣滀笟椤甸潰
	@Action(value="xmlAnalyzeNew",results={
			@Result(name="login",location="/web/login.jsp"),
			@Result(name="success",location="/web/manager/xmlAnalyzeNew.jsp")
		} )
		public String xmlAnalyzeNew(){
			this.menuName="xmlAnalyze";
			users = userService.findAllUsers();
			jobs=jobService.findJobs();
			currentUser=userService.findUserById(userId);
			return "success";
		
	}
	
	
	


	@Action(value="doNewCalculate",results={
			@Result(name="login",location="/web/login.jsp"),
			@Result(name="success",location = "xmlAnalyzeList", type = "chain"),
			@Result(name="error",location = "###########jobList##########", type = "chain")
		} )
	public String doNewCalculate(){

		this.menuName="xmlAnalyze";
		
		System.out.println("get project id is"+job_project_id);
		if(calculate_client_id!=null&&job_project_id!=null){
		User existedUser=userService.findUserById(calculate_client_id);
		Project p=projectService.findProjectById(job_project_id);
		if(existedUser!=null && p!=null){
			Calculate c=new Calculate();
			String uuId=UUIDGenerator.getUUID();
			calculateInfo.setId(uuId);
			c.setId(uuId);
			c.setXmlName(calculateInfo.getXmlName());
			c.setXmlCreateTime(new Date());
			c.setXmlPriority(JobPriority.medium);
			c.setXmlFilePath(calculateInfo.getXmlFilePath());
			c.setXmlProgress(0);
			c.setXmlStatus(CalculateStatus.notStart);//鍏堣繖涓缃祴璇曪紝瀹為檯鏄痭otstart
			c.setProject(p);
			c.setUser(existedUser);
			calculateService.addCalculate(c);
		}
		}
		
		return "success";
	}
		
	
	
		//鏍规嵁xmlId淇敼
		@Action(value="inCalculateEdit",results={
			@Result(name="login",location="/web/login.jsp"),
			@Result(name="success",location="/web/manager/calculateEdit.jsp"),
			@Result(name="error",location="xmlAnalyzeList",type="chain")
		} )
		public String inCalculateEdit(){
			this.menuName="xmlAnalyze";
			//new code
			if(xmlId!=null){
			 calculate=calculateService.findCalculateById(xmlId);

			 calculateInfo=new CalculateInfo();
				if(calculate!=null){
					
					calculateInfo.xmlFilePath=calculate.getXmlFilePath();
					calculateInfo.xmlName=calculate.getXmlName();
					calculateInfo.id=calculate.getId();		
					try {					
						
						File file=new File(calculate.getProject().getUser().getHomeDir());
						
						if(file.isDirectory()){
							
							File[] files=file.listFiles();

							if (files != null && files.length > 0) {
								mapScenePath=new HashMap<String, String>();
								
								for(int i=0;i<files.length;i++){
							    	 String s=files[i].getAbsolutePath();
									 String[] ss=s.split("/");
									 mapScenePath.put(ss[ss.length-1], s);
								}
							}						
							
						}
							
					} catch (Exception e) {
						e.printStackTrace();
					}	
			}
			}else
				return ERROR;
			
			return SUCCESS;	
			
		/*
		 * if(xmlId!=null){ calculate=calculateService.findCalculateById(xmlId); return
		 * "success"; } return "error";
		 */
		}
		
		
		@Action(value="doCalculateEdit",results={
				@Result(name="login",location="/web/login.jsp"),
				@Result(name="success",location = "xmlAnalyzeList", type = "chain"),
				@Result(name="error",location = "xmlAnalyzeList", type = "chain")
			} )
			public String doCalculateEdit(){
				this.menuName="xmlAnalyze";
				
				if(currentUser!=null){
					searchType="accurateCalculateId";
					searchText=currentUser.getId();
				}
				return null;
			}
		
		
		//鍒犻櫎鍏ㄩ儴浣滀笟
		@Action(value="doCalculateDeleteAll",results={
			@Result(name="login",location="/web/login.jsp"),
			@Result(name="success",location="xmlAnalyzeList",type = "chain"),
			@Result(name="error",location = "xmlAnalyzeList", type = "chain")
		})
		public String doCalculateDeleteAll(){
			this.menuName="xmlAnalyze";
			List<Calculate> calculates=calculateService.findCalculates();
			if(calculates!=null){
				for(int i=0;i<calculates.size();i++){
					Calculate calculate=calculates.get(i);
					if(calculate!=null){
						calculateService.deleteCalculate(calculate);
					}
				}
				return "success";
			}else{
				return "error";
			}
			
		}
		
		
		//鏍规嵁xmlIds鍒犻櫎,鍙互閫変腑澶氫釜妗嗕竴璧峰垹闄�
		@Action(value="doCalculateDelete",results={
			@Result(name="login",location="/web/login.jsp"),
			@Result(name="success",location = "xmlAnalyzeList", type = "chain"),
			@Result(name="error",location = "xmlAnalyzeList", type = "chain")
		} )
		public String doCalculateDelete(){
			this.menuName="xmlAnalyze";
			
			if(currentUser!=null){
				searchType="accurateCalculateId";
				searchText=currentUser.getId();
			}
			
			if(xmlIds!=null){
				for(int i=0;i<xmlIds.size();i++){
					Calculate calculate=calculateService.findCalculateById(xmlIds.get(i));
					if(calculate!=null){
						calculateService.deleteCalculate(calculate);
					}
				}
				return "success";
			}
			return "error";
		}
		
		
		//鏍规嵁xmlId缃《
		@Action(value="xmlToTop",results={
			@Result(name="login",location="/web/login.jsp"),
			@Result(name="success",location="/web/manager/##########.jsp")})
	     	public String xmlToTop(){
			this.menuName="xmlAnalyze";
			return null;
		}
		
	
		
		//淇敼浣滀笟浼樺厛绾�
		@Action(value = "doChangeXmlPriority", results = {
			@Result(name = "login", location = "/web/login.jsp"),
			@Result(name = SUCCESS, location = "xmlAnalyzeList", type = "chain"),
			@Result(name = ERROR, location = "xmlAnalyzeList", type = "chain") })
		public String doChangeXmlPriority() {

			/*this.menuName="xmlAnalyze";

			Calculate calculate = calculateService.findCalculatesById(xmlId);
			if (calculate != null) {
				calculate.setXmlPriority(newXmlPriority);
				calculateService.updateCalculateInfo(calculate);
				return SUCCESS;
			} else {
				return ERROR;
			}
*/
			
			return null;
		}


	
		
		//update new edit calculateJob 
		@Action(value = "calculateUpdate", results = { @Result(name = "login", location = "/web/login.jsp"),
				@Result(name = SUCCESS,  location = "xmlAnalyzeList", type="chain")})
		public String calculateUpdate(){
			this.menuName = "jobManage";
			if(calculateInfo!=null){

				 calculate=calculateService.findCalculateById(xmlId);
			
				 calculate.setXmlName(calculateInfo.getXmlName());
				 calculate.setXmlFilePath(calculate.getXmlFilePath());
				 calculateService.updateCalculateInfo(calculate);
	        	if(currentProject!=null){
	        		searchType="accurateProjectId";
	        		searchText=currentProject.getId();
	        	}
	    	    return SUCCESS;	
	    	 }else 
	    		return ERROR;
		}
		
		
		
	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public List<User> getUsers() {
		return users;
	}



	public void setUsers(List<User> users) {
		this.users = users;
	}



	public List<String> getXmlIds() {
		return xmlIds;
	}



	public void setXmlIds(List<String> xmlIds) {
		this.xmlIds = xmlIds;
	}



	public String getXmlName() {
		return xmlName;
	}



	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}



	public String getXmlCreateTime() {
		return xmlCreateTime;
	}



	public void setXmlCreateTime(String xmlCreateTime) {
		this.xmlCreateTime = xmlCreateTime;
	}



	public int getXmlStatus() {
		return xmlStatus;
	}



	public void setXmlStatus(int xmlStatus) {
		this.xmlStatus = xmlStatus;
	}



	public int getXmlProgress() {
		return xmlProgress;
	}



	public void setXmlProgress(int xmlProgress) {
		this.xmlProgress = xmlProgress;
	}



	public int getXmlPriority() {
		return xmlPriority;
	}



	public void setXmlPriority(int xmlPriority) {
		this.xmlPriority = xmlPriority;
	}



	public String getXmlId() {
		return xmlId;
	}



	public void setXmlId(String xmlId) {
		this.xmlId = xmlId;
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



	public CalculateInfo getCalculateInfo() {
		return calculateInfo;
	}



	public void setCalculateInfo(CalculateInfo calculateInfo) {
		this.calculateInfo = calculateInfo;
	}



	public List<CalculateInfomas> getCalculateInfomas() {
		return calculateInfomas;
	}



	public void setCalculateInfomas(List<CalculateInfomas> calculateInfomas) {
		this.calculateInfomas = calculateInfomas;
	}



	public List<Calculate> getCalculates() {
		return calculates;
	}



	public void setCalculates(List<Calculate> calculates) {
		this.calculates = calculates;
	}

	
	 public int getNewXmlPriority() {
			return newXmlPriority;
		}



		public void setNewXmlPriority(int newXmlPriority) {
			this.newXmlPriority = newXmlPriority;
		}



		public User getCurrentUser() {
			return currentUser;
		}



		public void setCurrentUser(User currentUser) {
			this.currentUser = currentUser;
		}
		
		
		public String getUserId() {
			return userId;
		}



		public void setUserId(String userId) {
			this.userId = userId;
		}
		

		public Job getCurrentJob() {
			return currentJob;
		}



		public void setCurrentJob(Job currentJob) {
			this.currentJob = currentJob;
		}

		public Calculate getCalculate() {
			return calculate;
		}



		public void setCalculate(Calculate calculate) {
			this.calculate = calculate;
		}

		
		public String getCalculate_client_id() {
			return calculate_client_id;
		}



		public void setCalculate_client_id(String calculate_client_id) {
			this.calculate_client_id = calculate_client_id;
		}

		
		public List<Job> getJobs() {
			return jobs;
		}



		public void setJobs(List<Job> jobs) {
			this.jobs = jobs;
		}

		public String getJobId() {
			return jobId;
		}



		public void setJobId(String jobId) {
			this.jobId = jobId;
		}

		
		 public String getCalculate_job_id() {
				return calculate_job_id;
			}



			public void setCalculate_job_id(String calculate_job_id) {
				this.calculate_job_id = calculate_job_id;
			}



			public String getJob_project_id() {
				return job_project_id;
			}



			public void setJob_project_id(String job_project_id) {
				this.job_project_id = job_project_id;
			}



			public Map<String, String> getMapScenePath() {
				return mapScenePath;
			}



			public void setMapScenePath(Map<String, String> mapScenePath) {
				this.mapScenePath = mapScenePath;
			}



			public Project getCurrentProject() {
				return currentProject;
			}



			public void setCurrentProject(Project currentProject) {
				this.currentProject = currentProject;
			}
			
				

}
