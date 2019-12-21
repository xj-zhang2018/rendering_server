package org.xjtu.framework.modules.manager.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.backstage.initialize.RndrJobInitialize;
import org.xjtu.framework.core.base.constant.JobPriority;
import org.xjtu.framework.core.base.constant.JobStatus;
import org.xjtu.framework.core.base.constant.ProjectStatus;
import org.xjtu.framework.core.base.constant.SystemConfig;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.RenderEngine;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.LinuxInvoker;
import org.xjtu.framework.core.util.PbsExecute;
import org.xjtu.framework.core.util.StringUtil;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.core.util.Xml2Model;
import org.xjtu.framework.modules.user.service.JobService;
import org.xjtu.framework.modules.user.service.ProjectService;
import org.xjtu.framework.modules.user.service.RenderEngineService;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.service.impl.JobServiceImpl;
import org.xjtu.framework.modules.user.vo.CameraInfo;
import org.xjtu.framework.modules.user.vo.JobListInfo;
import org.xjtu.framework.modules.user.vo.XmlJobInfo;

import com.xj.framework.ssh.ShellLocal;

@ParentPackage("struts-default")
@Namespace("/web/manager")
public class JobManageAction extends ManagerBaseAction{
	private @Resource JobService jobService;
	private @Resource ProjectService projectService;	
	private @Resource UserService userService;
	private @Resource RenderEngineService renderEngineService;
	private User user;
	//inNewJobs鍙傛暟
	private List<User> users;
	private List<RenderEngine> renderEngines;
	//doNewJobs鍙傛暟
	private String job_project_id;
	
	/*鍒嗛〉鎵�闇�鍙傛暟*/
    private int pageTotal = 0;
    
    private int pageSize = 10;
   
	private int pageNum = 1;
    
    private String searchText = "";
    
    private String searchType = "name";
    
    /*瀛愯彍鍗曞悕绉�*/
    private String subMenuName="allJobs";
    /*榛樿涓�-1锛屾樉绀烘墍鏈変綔涓�*/
    private int jobStatus=-1;
    
    private String jobId;
    
    private int newJobPriority=2;
       
    private Project currentProject;
    
	private Job job;
    
	private List<Job> jobs;
    
    private List<String> jobIds;
    private List<JobListInfo>jobListInfos;
	private static final Log log = LogFactory.getLog(JobManageAction.class);	
    private CameraInfo cameraInfo;
    private String collectScriptPath=this.getClass().getResource("/").getPath()+"shell/collection.sh";
    //鏂囦欢涓嬭浇
    private static final int BUFFEREDSIZE = 1024;
    ByteArrayInputStream renderResult; 
    String downloadFileName = "";
    
    private Map<String,String> mapScenePath;
    
    private String xmlName;
          
	@Action(value = "jobList", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "/web/manager/jobList.jsp")})		
	public String jobList(){
		
		user = (User)session.get("user");

		this.menuName = "jobManage";
		
		if(subMenuName.equals("allJobs")){
			jobStatus=-1;
		}else if(subMenuName.equals("runingJobs")){
			jobStatus=2;
		}else if(subMenuName.equals("inQueueJobs")){
			jobStatus=1;
		}else if(subMenuName.equals("notStartJobs")){
			jobStatus=0;
		}else if(subMenuName.equals("finishedJobs")){
			jobStatus=3;
		}

		if(user != null){
			int totalSize = jobService.findTotalCountByQuery(searchText, searchType, jobStatus);
			this.pageTotal = (totalSize-1)/this.pageSize + 1;
			
			jobListInfos=new ArrayList<JobListInfo>();
			jobs = jobService.listJobsByQuery(searchText, searchType, pageNum, pageSize, jobStatus);
			for(int i=0 ; i<jobs.size() ; i++){
				JobListInfo jobListInfo =new JobListInfo();
				jobListInfo.setCameraName(jobs.get(i).getCameraName());
				jobListInfo.setCameraProgress(jobs.get(i).getCameraProgress());
				jobListInfo.setFilePath(jobs.get(i).getFilePath());
				jobListInfo.setId(jobs.get(i).getId());
				jobListInfo.setJobPriority(jobs.get(i).getJobPriority());
				jobListInfo.setJobStatus(jobs.get(i).getJobStatus());
				jobListInfo.setPaymentStatus(jobs.get(i).getPaymentStatus());
				jobListInfo.setRenderCost(jobs.get(i).getRenderCost());
				jobListInfo.setRenderEngine(jobs.get(i).getRenderEngine());
				jobListInfo.setIsPreRender(jobs.get(i).getPreRenderingTag());
				//鏄剧ず娓叉煋寮�濮嬪拰缁撴潫鏃堕棿
				if(jobs.get(i).getEndTime()!=null&&jobs.get(i).getStartTime()!=null){
					jobListInfo.setTimeInfo("startTime:"+jobs.get(i).getStartTime().toString().replaceAll(" ", "_")+"&#13"+"endTime:"+jobs.get(i).getEndTime().toString().replaceAll(" ","_"));
				}
				else if(jobs.get(i).getStartTime()!=null){
					jobListInfo.setTimeInfo("startTime:"+jobs.get(i).getStartTime().toString().replaceAll(" ", "_")+"&#13"+"endTime:");
				}else{
					jobListInfo.setTimeInfo("startTime:&#13endTime");
				}				
				jobListInfos.add(jobListInfo);			
			}
			
			//for(JobListInfo jobListInfo:jobListInfos)Log.info("浣滀笟淇℃伅锛�"+jobListInfo);
			
			
			return SUCCESS;
		}else{						
			return "login";
		}
	}
	
	
	@Action(value = "jobTest", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location = "/web/manager/jobList.jsp")})		
	public String jobTest(){

		List<Job> allJobs=jobService.findJobs();
		
		List<String> testJobIds=new ArrayList<String>();
		testJobIds.add(allJobs.get(0).getId());
		jobService.doStartCameraRender(testJobIds);
		
		return SUCCESS;

	}
	
	@Action(value = "doJobsDelete", results = { @Result(name = "login", location = "/web/login.jsp"),
			@Result(name = SUCCESS, location = "jobList", type="chain"),
			@Result(name = ERROR, location = "jobList", type="chain")})
	public String doJobsDelete(){
		
		this.menuName = "jobManage";
			
		if(jobIds!=null){
				
			for(int i=0;i<jobIds.size();i++){

				Job job=jobService.findJobsById(jobIds.get(i));
					
				if(job!=null){
					
					jobService.doStopCameraRender(jobIds.get(i));
					jobService.deleteJob(job);
					Project project=job.getProject();
					project.setCamerasNum(project.getCamerasNum()-1);
					projectService.updateProjectInfo(project);
				}
					
			}
				
			return SUCCESS;

		}else{
			return ERROR;
		}
		
	}
	@Action(value = "doJobsDeleteAll", results = { @Result(name = "login", location = "/web/login.jsp"),
			@Result(name = SUCCESS, location = "jobList", type="chain"),
			@Result(name = ERROR, location = "jobList", type="chain")})
	public String doJobsDeleteAll(){
		
		this.menuName = "jobManage";
		List<Job> jobs =jobService.findJobs();
			
		if(jobs!=null){
				
			for(int i=0;i<jobs.size();i++){

				Job job=jobs.get(i);
					
				if(job!=null){
					
					jobService.doStopCameraRender(job.getId());
					jobService.deleteJob(job);
					Project project=job.getProject();
					project.setCamerasNum(project.getCamerasNum()-1);
					projectService.updateProjectInfo(project);
				}
					
			}
				
			return SUCCESS;

		}else{
			return ERROR;
		}
		
	}
			
	@Action(value = "inJobEdit", results = { @Result(name = "login", location = "/web/login.jsp"),
			@Result(name = SUCCESS, location = "/web/manager/jobEdit.jsp")})
	public String inJobEdit(){
		
		this.menuName = "jobManage";
	
		if(jobId!=null){
			job=jobService.findJobsById(jobId);
			
			cameraInfo=new CameraInfo();
			if(job!=null){
				cameraInfo.id=job.getId();
				cameraInfo.cameraName=job.getCameraName();
				cameraInfo.filePath=job.getFilePath();
				cameraInfo.frameRange=job.getFrameRange();
				cameraInfo.unitsNumber=job.getUnitsNumber();
				cameraInfo.nodesNumber=job.getNodesNumber();
				cameraInfo.preRenderingTag=job.getPreRenderingTag();
				cameraInfo.xResolution=job.getxResolution()+"";
				cameraInfo.yResolution=job.getyResolution()+"";
				cameraInfo.sampleRate=job.getSampleRate()+"";
				cameraInfo.renderEngineId=job.getRenderEngine().getId();
				if(cameraInfo.renderEngineId.equals("e45c905c33eb4bfd9e42403d2566abad")){
					cameraInfo.isrUnit=true;
				}
				else{
					cameraInfo.isrUnit=false;
				}
				
				
				try {					
					
					/*鏍规嵁rib鏂囦欢鏅鸿兘鏌ユ壘鍦烘櫙璺緞锛屾�ц兘鏈夌摱棰�*/
					/*
						FileDirectorySearch star=new FileDirectorySearch();
			           
				    	File[] files = star.getFiles(existedUser.getHomeDir(), "*.rib");  
					*/
					
					File file=new File(job.getProject().getUser().getHomeDir());
					
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
			return SUCCESS;	
		}
		else
			return ERROR;
	}
		
	@Action(value = "jobUpdate", results = { @Result(name = "login", location = "/web/login.jsp"),
			@Result(name = SUCCESS,  location = "jobList", type="chain")})
	public String jobUpdate(){
		
		this.menuName = "jobManage";
			
		if(cameraInfo!=null){
			/*淇敼鎴愬懡浠ゆ柟寮�*/
			//10.25 Thys 更新掉的老引擎
			/*String cmd="cd "+cameraInfo.filePath+" && ls *.xml";
			PbsExecute pbs=new PbsExecute(cmd);
			String result=pbs.executeCmd();
			result=result.trim();
			
			String[] filenames=result.split("\\n");
			
			Map<Integer,String> mapScenePath=new HashMap<Integer,String>();;
			for(int i=0;i<filenames.length;i++){
				String[] frameNo=filenames[i].split("\\.");

				Integer key;
				try{
					key=Integer.parseInt(frameNo[frameNo.length-2]);
				}catch(NumberFormatException e){
					continue;
				}
				mapScenePath.put(key, filenames[i]);
			}
			int frameNumber=0;
			String[] ss=cameraInfo.frameRange.split(",");
			if(ss!=null&&ss.length>0){
				
				for(int i=0;i<ss.length;i++){

					String[] temp=ss[i].split("-");
					
					if(temp!=null&&temp.length>0){
						
						if(temp.length==1){
							
							if(mapScenePath.get(Integer.parseInt(temp[0]))==null){
					    		this.errorType="error";
					    		this.errorCode="您要渲染的部分帧找不到相应的xml文件";
								return ERROR;
							}
							
							frameNumber++;
						}else if(temp.length==2){
							try{
								Integer end=Integer.parseInt(temp[1]);
								Integer begin=Integer.parseInt(temp[0]);
								
								if(end>begin)
									frameNumber=frameNumber+end-begin+1;
								else{
						    		this.errorType="error";
						    		this.errorCode="你填写的帧范围格式有错误";
									return ERROR;
								}
								
								for(int z=0;z<end-begin+1;z++){
									if(mapScenePath.get(begin+z)==null){
							    		this.errorType="error";
							    		this.errorCode="你要渲染的部分帧找不到对应的xml文件";
										return ERROR;
									}
								}
							}catch(Exception e){
					    		this.errorType="error";
					    		this.errorCode="你填写的帧范围格式错误";
								return ERROR;
							}

						}else{
				    		this.errorType="error";
				    		this.errorCode="你填写的帧范围格式错误";
							return ERROR;
						}
					}else{
			    		this.errorType="error";
			    		this.errorCode="你填写的帧范围格式错误";
						return ERROR;
					}
				}
				
				
			}else{
	    		this.errorType="error";
	    		this.errorCode="你填写的帧范围格式错误";
				return ERROR;
			}*/
			
			
			String filePath=cameraInfo.filePath;
			
			// 10.25 Thys 更换引擎，更改检测帧范围方式
			String cmd = "cd "+filePath+" && cat frameMax.txt";
			ShellLocal shell = new ShellLocal(cmd);
			String result = shell.executeCmd();
			result = result.trim();
			int frameMax = Integer.parseInt(result);
			
			Map<Integer,String> mapScenePath=new HashMap<Integer,String>();
			
			int frameNumber = 0;
			String[] ss = cameraInfo.frameRange.split(",");
			if(ss != null && ss.length > 0){
				
				for(int i = 0;i < ss.length;i++){

					String[] temp = ss[i].split("-");
					
					if(temp != null && temp.length > 0){
						
						if(temp.length == 1){
							
							if(Integer.parseInt(temp[0]) > frameMax){
					    		this.errorType = "error";
					    		this.errorCode = "您要渲染的部分帧超过可渲染最大范围";
								return ERROR;
							}
							
							mapScenePath.put(Integer.parseInt(temp[0]), "frame"+temp[0]+".xml");
							
							frameNumber++;
						}else if(temp.length == 2){
							try{
								Integer end=Integer.parseInt(temp[1]);
								Integer begin=Integer.parseInt(temp[0]);
								
								if(end>begin)
									frameNumber = frameNumber + end - begin + 1;
								else{
						    		this.errorType="error";
						    		this.errorCode="你填写的帧范围格式错误";
									return ERROR;
								}
								
								for(int z = begin;z < end + 1;z++){
									if(z > frameMax){
							    		this.errorType="error";
							    		this.errorCode="您要渲染的部分帧超过可渲染最大范围";
										return ERROR;
									}
									
									mapScenePath.put(z, "frame"+z+".xml");
								}
							}catch(Exception e){
					    		this.errorType="error";
					    		this.errorCode="你填写的帧范围格式错误";
								return ERROR;
							}

						}else{
				    		this.errorType="error";
				    		this.errorCode="你填写的帧范围格式错误";
							return ERROR;
						}
					}else{
			    		this.errorType="error";
			    		this.errorCode="你填写的帧范围格式错误";
						return ERROR;
					}
					
				}
			}else{
	    		this.errorType="error";
	    		this.errorCode="你填写的帧范围格式错误";
				return ERROR;
			}
			
			
			job=jobService.findJobsById(jobId);
			job.setCameraName(cameraInfo.cameraName);
			job.setFrameRange(cameraInfo.frameRange);
			job.setFrameNumbers(frameNumber);	
    	    job.setFilePath(cameraInfo.filePath);
    	    if(cameraInfo.isrUnit==true){
    	    	job.setUnitsNumber(cameraInfo.unitsNumber);
    	    }else{
    	    	job.setNodesNumber(cameraInfo.nodesNumber);
    	    }
    	 
    	    
    	    if((cameraInfo.preRenderingTag != null)&&(cameraInfo.preRenderingTag==1)){
        	    job.setPreRenderingTag(cameraInfo.preRenderingTag);
        	    job.setxResolution(Integer.parseInt(cameraInfo.xResolution));
        	    job.setyResolution(Integer.parseInt(cameraInfo.yResolution));
        	    job.setSampleRate(Integer.parseInt(cameraInfo.sampleRate));  	
    	    }else{
        	    job.setPreRenderingTag(0);
    	    	job.setxResolution(0);
        	    job.setyResolution(0);
        	    job.setSampleRate(0);  	
        	    }
   	
        	jobService.updateJobInfo(job);
        	
        	if(currentProject!=null){
        		searchType="accurateProjectId";
        		searchText=currentProject.getId();
        	}
    	    return SUCCESS;	
    	 }else 
    		return ERROR;
	}
	
	
	
	
	
	
	
	
	
	
	@Action(value = "inNewJob", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location ="/web/manager/jobNew.jsp")})
	public String inNewJob(){
	
		this.menuName = "jobManage";		
		users=userService.findAllUsers();
		renderEngines=renderEngineService.findAllRenderEngines();
		return SUCCESS;
		
	}	
		
	@Action(value = "doNewJob", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location ="jobList", type="chain"),
    		@Result(name = ERROR, location ="inNewJob", type="chain")})
	public String doNewJob(){
	
		user = (User)session.get("user");
		this.menuName = "jobManage";
		
		if(user!=null){
			if(cameraInfo!=null&&job_project_id!=null){
				
				Project p=projectService.findProjectById(job_project_id);
				
				if(p!=null){
									
					String filePath=cameraInfo.filePath;
					
					
					//filePath=filePath.substring(0,filePath.lastIndexOf("/"));
					
			
					//String cmd="cd "+cameraInfo.filePath+" && ls *.rib";
					
					//10.25 Thys 更换下的老引擎
					/*String cmd="cd "+filePath+" && ls *.xml";
					
					ShellLocal pbs=new ShellLocal(cmd);
					String result=pbs.executeCmd();
					result=result.trim();
					
					String[] filenames=result.split("\\n");
					
					
					
					System.out.println("filenames is "+Arrays.toString(filenames));
					Map<Integer,String> mapScenePath=new HashMap<Integer,String>();
					for(int i=0;i<filenames.length;i++){
						String[] frameNo=filenames[i].split("\\.");
						Integer key;
						try{
							String frameID=StringUtil.getNumb(frameNo[frameNo.length-2]);
							key=Integer.parseInt(frameID);
							//key=Integer.parseInt(frameNo[frameNo.length-2]);
						}catch(NumberFormatException e){
							continue;
						}
						mapScenePath.put(key, filenames[i]);
					}
					
					
					
					int frameNumber=0;
					String[] ss=cameraInfo.frameRange.split(",");
					if(ss!=null&&ss.length>0){
						
						for(int i=0;i<ss.length;i++){
	
							String[] temp=ss[i].split("-");
							
							if(temp!=null&&temp.length>0){
								
								if(temp.length==1){
									
									if(mapScenePath.get(Integer.parseInt(temp[0]))==null){
							    		this.errorType="error";
							    		this.errorCode="您要渲染的部分帧找不到相应的xml文件";
										return ERROR;
									}
									
									frameNumber++;
								}else if(temp.length==2){
									try{
										Integer end=Integer.parseInt(temp[1]);
										Integer begin=Integer.parseInt(temp[0]);
										
										if(end>begin)
											frameNumber=frameNumber+end-begin+1;
										else{
								    		this.errorType="error";
								    		this.errorCode="你填写的帧范围格式错误";
											return ERROR;
										}
										
										for(int z=0;z<end-begin+1;z++){
											if(mapScenePath.get(begin+z)==null){
									    		this.errorType="error";
									    		this.errorCode="您要渲染的部分帧找不到相应的xml文件";
												return ERROR;
											}
										}
									}catch(Exception e){
							    		this.errorType="error";
							    		this.errorCode="你填写的帧范围格式错误";
										return ERROR;
									}
		
								}else{
						    		this.errorType="error";
						    		this.errorCode="你填写的帧范围格式错误";
									return ERROR;
								}
							}else{
					    		this.errorType="error";
					    		this.errorCode="你填写的帧范围格式错误";
								return ERROR;
							}
							
						}
					}else{
			    		this.errorType="error";
			    		this.errorCode="你填写的帧范围格式错误";
						return ERROR;
					}*/
					
					
					// 10.25 Thys 更换引擎，更改检测帧范围方式
					String cmd = "cd "+filePath+" && cat frameMax.txt";
					ShellLocal shell = new ShellLocal(cmd);
					String result = shell.executeCmd();
					result = result.trim();
					int frameMax = Integer.parseInt(result);
					
					Map<Integer,String> mapScenePath=new HashMap<Integer,String>();
					
					int frameNumber = 0;
					String[] ss = cameraInfo.frameRange.split(",");
					if(ss != null && ss.length > 0){
						
						for(int i = 0;i < ss.length;i++){
	
							String[] temp = ss[i].split("-");
							
							if(temp != null && temp.length > 0){
								
								if(temp.length == 1){
									
									if(Integer.parseInt(temp[0]) > frameMax){
							    		this.errorType = "error";
							    		this.errorCode = "您要渲染的部分帧超过可渲染最大范围";
										return ERROR;
									}
									
									mapScenePath.put(Integer.parseInt(temp[0]), "frame"+temp[0]+".xml");
									
									frameNumber++;
								}else if(temp.length == 2){
									try{
										Integer end=Integer.parseInt(temp[1]);
										Integer begin=Integer.parseInt(temp[0]);
										
										if(end>begin)
											frameNumber = frameNumber + end - begin + 1;
										else{
								    		this.errorType="error";
								    		this.errorCode="你填写的帧范围格式错误";
											return ERROR;
										}
										
										for(int z = begin;z < end + 1;z++){
											if(z > frameMax){
									    		this.errorType="error";
									    		this.errorCode="您要渲染的部分帧超过可渲染最大范围";
												return ERROR;
											}
											
											mapScenePath.put(z, "frame"+z+".xml");
										}
									}catch(Exception e){
							    		this.errorType="error";
							    		this.errorCode="你填写的帧范围格式错误";
										return ERROR;
									}
		
								}else{
						    		this.errorType="error";
						    		this.errorCode="你填写的帧范围格式错误";
									return ERROR;
								}
							}else{
					    		this.errorType="error";
					    		this.errorCode="你填写的帧范围格式错误";
								return ERROR;
							}
							
						}
					}else{
			    		this.errorType="error";
			    		this.errorCode="你填写的帧范围格式错误";
						return ERROR;
					}
					
					Job j=new Job();
					j.setId(UUIDGenerator.getUUID());
					j.setCameraName(cameraInfo.cameraName);
					j.setCreateTime(new Date());
					j.setJobStatus(JobStatus.notStart);
					j.setQueueNum(-1);
					j.setCameraProgress(0);
					j.setJobPriority(JobPriority.medium);					
					j.setFrameRange(cameraInfo.frameRange);					
					j.setFrameNumbers(frameNumber);															
					j.setFilePath(cameraInfo.filePath);										
					j.setRenderCost(0.0);
					
					if(cameraInfo.preRenderingTag!=null&&cameraInfo.preRenderingTag == 1){
						j.setPreRenderingTag(cameraInfo.preRenderingTag);
						j.setxResolution(Integer.parseInt(cameraInfo.xResolution));
						j.setyResolution(Integer.parseInt(cameraInfo.yResolution));
						j.setSampleRate(Integer.parseInt(cameraInfo.sampleRate));
					}else{
						j.setPreRenderingTag(0);
						j.setxResolution(0);
						j.setyResolution(0);
						j.setSampleRate(0);
					}
					j.setProject(p);
					
					/*榛樿娓叉煋寮曟搸*/
					if(cameraInfo.renderEngineId==null){
						
						
						//e45c905c33eb4bfd9e42403d2566abad   rUnit
						//e45c905c33eb4bfd9e42403d2566abae   rUnit-hustdm
						cameraInfo.renderEngineId="e45c905c33eb4bfd9e42403d2566abad";
						RenderEngine re=renderEngineService.findRenderEnginebyId(cameraInfo.renderEngineId);
						j.setRenderEngine(re);
						j.setUnitsNumber(cameraInfo.unitsNumber);//change this becoming coreNmber
						}
					else{
						RenderEngine re=renderEngineService.findRenderEnginebyId(cameraInfo.renderEngineId);
						j.setRenderEngine(re);               
						         //杩欓噷璁剧疆寮曟搸涓簉Unit锛屽惁鍒欒缃簲鐢ㄥ崟鍏冧负1
							if(cameraInfo.renderEngineId.equals("e45c905c33eb4bfd9e42403d2566abad")){
							j.setUnitsNumber(cameraInfo.unitsNumber);
						}
						else{
							j.setUnitsNumber(1);
							j.setNodesNumber(cameraInfo.unitsNumber);
						}
					}
					
				
					
					jobService.addJob(j);
					
					p.setCamerasNum(p.getCamerasNum()+1);
					projectService.updateProjectInfo(p);
					
					//浣挎坊鍔犱綔涓氭垚鍔熷悗杩斿洖瀵瑰簲宸ョ▼涓嬬殑浣滀笟鍒楄〃
					if(currentProject!=null){
						searchType="accurateProjectId";
						searchText=currentProject.getId();
					}
					
					this.errorType="success";
					this.errorCode="作业添加成功";
					return SUCCESS;
					
				}else{
		    		this.errorType="error";
		    		this.errorCode="该工程不存在";
					return ERROR;
				}
										
			}else{
				
	    		this.errorType="error";
	    		this.errorCode="请将作业信息填写完整";
				
				return ERROR;
			}
		}else{
			return "login";
		}
		
	}
	
	@Action(value = "doChangeJobPriority", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location ="jobList", type="chain"),
    		@Result(name = ERROR, location ="jobList", type="chain")})
	public String doChangeJobPriority(){
	
		this.menuName = "jobManage";
		
		Job job=jobService.findJobsById(jobId);
		if(job!=null){
			
			if(job.getJobStatus()==JobStatus.inQueue){

				job.setJobPriority(newJobPriority);
				int queueNum=jobService.findMaxQueueNumByPriority(newJobPriority)+1;
				job.setQueueNum(queueNum);
				jobService.updateJobInfo(job);
				
			}else{
				job.setJobPriority(newJobPriority);
				jobService.updateJobInfo(job);
			}
			
			return SUCCESS;
		}else{
			return ERROR;
		}
		
	}
	
	@Action(value = "jobToTop", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location ="jobList", type="chain"),
    		@Result(name = ERROR, location ="jobList", type="chain")})
	public String jobToTop(){
	
		user = (User)session.get("user");
		this.menuName = "jobManage";
		
		if(user!=null){

			Job job=jobService.findJobsById(jobId);
			//瀛樺湪鐨勪綔涓氶』鍦ㄦ帓闃熺姸鎬佹墠鑳界疆椤�
			if(job!=null&&job.getJobStatus()==JobStatus.inQueue){

				jobService.changeQueuingJobToTop(jobId);
				
				return SUCCESS;
			}else{
				
	    		this.errorType="error";
	    		this.errorCode="你的作业已不在队列中";
				return ERROR;
			}
		}else{
			return "login";
		}
		
	}
	
	
	
	@Action(value = "downloadPreRenderResult", results = { @Result(name = ERROR, type="httpheader",params={"status","204"}),
    		@Result(name = SUCCESS, type = "stream", params = {  
    	            "contentType", "application/zip", "inputName",  
    	            "renderResult", "contentDisposition",  
    	            "attachment;filename=${downloadFileName}.zip", "bufferSize", "1024" })}) 
    public String downloadPreRenderResult() throws Exception{
		long startTime=System.currentTimeMillis();   //鑾峰彇寮�濮嬫椂闂�
		
		Job job=jobService.findJobsById(jobId);
		if(job.getPreRenderingTag()==1){
			ByteArrayOutputStream output = new ByteArrayOutputStream();   	
			ZipOutputStream zos = new ZipOutputStream(output);
			String returnStr="";
			//淇敼鍚庣殑璺緞
			String tmp_path=job.getFilePath();
			//tmp_path=tmp_path.substring(0,tmp_path.lastIndexOf("/"));
			
			String validateCmd="ls "+tmp_path+" |grep -w \"Pictures_pre\"";
			ShellLocal pbs=new ShellLocal(validateCmd);
			
			returnStr=pbs.executeCmd().trim();
			if(returnStr==null&&!returnStr.equals("Pictures_pre")||returnStr.length()==0){
				pbs.setCmd("mkdir -p "+tmp_path+"/Pictures_pre");
				pbs.executeCmd();
			}
			/*File f=new File(tmp_path+"/Pictures_pre");
			if(!f.exists()){
				f.mkdirs();
			}*/
			//姝ゅ闇�瑕佷竴涓嚱鏁板惂鐢ㄦ埛璺緞涓嬬殑瀹ｈ█缁撴灉闆嗕腑璧锋潵锛屾墍鏈夌殑鍥剧墖闆嗕腑鍦ㄤ竴璧�
			//sh /home/pbsuser1/collection.sh /home/export/online1/systest/swsdu/xijiao/mitsuba-c/user1
			//String cmd="sh /home/pbsuser1/collection.sh "+tmp_path;
			String cmd="";
			try {
				//cmd="mv "+tmp_path+"/*_pre.png "+tmp_path+"/Pictures_pre/";
				cmd="mv "+tmp_path+"/*copy.png "+tmp_path+"/Pictures_pre/";
				pbs.setCmd(cmd);
				pbs.executeCmd();
			} catch (Exception e) {
				cmd="ls "+tmp_path+"/*_pre.png";
			}
			
			
			String path=tmp_path+"/Pictures_pre/";
			File file = new File(path);
			if(!file.exists())file.mkdirs();
			
			this.downloadFileName="Preresult";
			zip(file,zos,"result");
			//娓呯┖缂撳啿鍖烘暟鎹紝杩欎竴姝ュ姟蹇呭厛鎵ц
			zos.flush();
			zos.close();

			byte[] ba = output.toByteArray();
			renderResult=new ByteArrayInputStream(ba);
			output.flush();
			output.close();
			
			return SUCCESS;
		}
		return ERROR;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
    @Action(value = "downloadRenderResult", results = { @Result(name = ERROR, type="httpheader",params={"status","204"}),
    		@Result(name = SUCCESS, type = "stream", params = {  
    	            "contentType", "application/zip", "inputName",  
    	            "renderResult", "contentDisposition",  
    	            "attachment;filename=${downloadFileName}.zip", "bufferSize", "1024" })}) 
    public String downloadRenderResult() throws Exception{
    	long startTime=System.currentTimeMillis();   //鑾峰彇寮�濮嬫椂闂�
		Job job=jobService.findJobsById(jobId);
		if(job.getJobStatus().equals(JobStatus.finished)){
            String returnStr="";
			ByteArrayOutputStream output = new ByteArrayOutputStream();   	
			ZipOutputStream zos = new ZipOutputStream(output);
			//淇敼鍚庣殑璺緞
			String tmp_path=job.getFilePath();
			//tmp_path=tmp_path.substring(0,tmp_path.lastIndexOf("/"));
			String validateCmd="ls "+tmp_path+" |grep -w \"Pictures\"";
			ShellLocal pbs=new ShellLocal(validateCmd);
			
			returnStr=pbs.executeCmd().trim();
			if(returnStr==null&&!returnStr.equals("Pictures")||returnStr.length()==0){
				pbs.setCmd("mkdir -p "+tmp_path+"/Pictures");
				pbs.executeCmd();
			}
			/*File f=new File(tmp_path+"/Pictures");
			if(!f.exists())f.mkdirs();*/
			
			//姝ゅ闇�瑕佷竴涓嚱鏁板惂鐢ㄦ埛璺緞涓嬬殑瀹ｈ█缁撴灉闆嗕腑璧锋潵锛屾墍鏈夌殑鍥剧墖闆嗕腑鍦ㄤ竴璧�
			
			//sh /home/pbsuser1/collection.sh /home/export/online1/systest/swsdu/xijiao/mitsuba-c/user1
			//String cmd="sh /home/pbsuser1/collection.sh "+tmp_path;
			String cmd="";
			try {
				//cmd="sh "+collectScriptPath+" "+tmp_path;
				 SystemConfig systemConfig=new SystemConfig();
	    		  String renderingWorkDir=systemConfig.getRenderingWorkDir();
				//sh /home/export/online1/systest/swsdu/xijiao/Users/collection.sh /home/export/online1/systest/swsdu/xijiao/Users/xjzhang/camera1
				//cmd="sh "+collectScriptPath+" "+tmp_path;
	    		  cmd="mv "+tmp_path+"/frame*[1-9].png "+tmp_path+"/Pictures";
	    		  //cmd="sh "+renderingWorkDir+"/collection.sh "+tmp_path;
	    		  log.info("collect script is "+cmd);
	    		  pbs.setCmd(cmd);
	    		  pbs.executeCmd();
			} catch (Exception e) {
				cmd="ls "+tmp_path+"/*.png";
			}
			String path=tmp_path+"/Pictures/";
			File file = new File(path);
			if(!file.exists())file.mkdirs();
			this.downloadFileName="result";
			zip(file,zos,"result");
			//娓呯┖缂撳啿鍖烘暟鎹紝杩欎竴姝ュ姟蹇呭厛鎵ц
			zos.flush();
			zos.close();
			byte[] ba = output.toByteArray();
			renderResult=new ByteArrayInputStream(ba);
			output.flush();
			output.close();
			return SUCCESS;
		}
    	
		return ERROR;

    }
    
	@Action(value = "doRenderTest", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location ="jobList", type="chain"),
    		@Result(name = ERROR, location ="jobList", type="chain")})
	public String doRenderTest(){
	
		this.menuName = "jobManage";
		
		if(xmlName!=null&&!xmlName.equals("")){			
			
			Xml2Model<XmlJobInfo> x2m=new Xml2Model<XmlJobInfo>();
			
			XmlJobInfo xji=new XmlJobInfo();
			
			List<XmlJobInfo> xjis=x2m.readXML(this.getClass().getResource("/").getPath()+"shell/"+xmlName,xji);
			
	        System.out.println("XML reading success!");
	
	        if(xjis!=null&&xjis.size()!=0){
	   
	        	User adminUser=userService.findUserByName("shanda");
	        	
	        	Project p=new Project();
	        	p.setId(UUIDGenerator.getUUID());
	        	p.setName("test_"+new Date().getTime());
	        	p.setCreateTime(new Date());
	        	p.setCamerasNum(xjis.size());
	        	p.setProjectStatus(ProjectStatus.notStart);
	        	p.setProjectProgress(0);
	        	p.setUser(adminUser);
	        	
	        	projectService.addProject(p);
	        	
	        	RenderEngine re=renderEngineService.findRenderEnginebyName("RWing");
	        	int queueNum=jobService.findMaxQueueNumByPriority(JobPriority.medium)+1;
	        	
		        for(int i =0;i<xjis.size();i++){
		        	XmlJobInfo xi=(XmlJobInfo)xjis.get(i);
		        	
					Job j=new Job();
					j.setId(UUIDGenerator.getUUID());
					j.setCameraName(xi.getCameraName());
					j.setCreateTime(new Date());
					j.setJobStatus(JobStatus.inQueue);
					j.setQueueNum(queueNum+i);
					j.setCameraProgress(0);
					j.setJobPriority(JobPriority.medium);					
					j.setFrameRange(xi.getFrameRange());					
					j.setFrameNumbers(Integer.parseInt(xi.getFrameNumbers()));															
					j.setFilePath(xi.getFilePath());										
					j.setRenderCost(0.0);
					j.setPreRenderingTag(0);
					j.setxResolution(0);
					j.setyResolution(0);
					j.setSampleRate(0);
					
					j.setProject(p);
					j.setRenderEngine(re);
					j.setUnitsNumber(Integer.parseInt(xi.getUnitsNumber()));
					
					jobService.addJob(j);
	 
		        }
	        }
		}

        return SUCCESS;
		
	}

	@Action(value = "doRenderTestdm", results = { @Result(name = "login", location = "/web/login.jsp"),
    		@Result(name = SUCCESS, location ="jobList", type="chain"),
    		@Result(name = ERROR, location ="jobList", type="chain")})
	public String doRenderTestdm(){
	
		this.menuName = "jobManage";
		
		if(xmlName!=null&&!xmlName.equals("")){			
			
			Xml2Model<XmlJobInfo> x2m=new Xml2Model<XmlJobInfo>();
			
			XmlJobInfo xji=new XmlJobInfo();
			
			List<XmlJobInfo> xjis=x2m.readXML(this.getClass().getResource("/").getPath()+"shell/"+xmlName,xji);
			
	        System.out.println("XML reading success!");
	
	        if(xjis!=null&&xjis.size()!=0){
	   
	        	User adminUser=userService.findUserByName("shanda");
	        	
	        	Project p=new Project();
	        	p.setId(UUIDGenerator.getUUID());
	        	p.setName("test_"+new Date().getTime());
	        	p.setCreateTime(new Date());
	        	p.setCamerasNum(xjis.size());
	        	p.setProjectStatus(ProjectStatus.notStart);
	        	p.setProjectProgress(0);
	        	p.setUser(adminUser);
	        	
	        	projectService.addProject(p);
	        	
	        	RenderEngine re=renderEngineService.findRenderEnginebyName("rUnit-hustdm");
	        	int queueNum=jobService.findMaxQueueNumByPriority(JobPriority.medium)+1;
	        	
		        for(int i =0;i<xjis.size();i++){
		        	XmlJobInfo xi=(XmlJobInfo)xjis.get(i);
		        	
					Job j=new Job();
					j.setId(UUIDGenerator.getUUID());
					j.setCameraName(xi.getCameraName());
					j.setCreateTime(new Date());
					j.setJobStatus(JobStatus.inQueue);
					j.setQueueNum(queueNum+i);
					j.setCameraProgress(0);
					j.setJobPriority(JobPriority.medium);					
					j.setFrameRange(xi.getFrameRange());					
					j.setFrameNumbers(Integer.parseInt(xi.getFrameNumbers()));															
					j.setFilePath(xi.getFilePath());										
					j.setRenderCost(0.0);
					j.setPreRenderingTag(0);
					j.setxResolution(0);
					j.setyResolution(0);
					j.setSampleRate(0);
					
					j.setProject(p);
					j.setRenderEngine(re);
					j.setUnitsNumber(Integer.parseInt(xi.getUnitsNumber()));
					
					jobService.addJob(j);
	 
		        }
	        }
		}

        return SUCCESS;
		
	}	
	
    private synchronized void zip(File inputFile, ZipOutputStream out, String base)   
            throws IOException {   
        if (inputFile.isDirectory()) {   
            File[] inputFiles = inputFile.listFiles();   
            out.putNextEntry(new ZipEntry(base + "/"));   
            base = base.length() == 0 ? "" : base + "/";   
            for (int i = 0; i < inputFiles.length; i++) {   
                zip(inputFiles[i], out, base + inputFiles[i].getName());   
            }   
  
        } else {   
            if (base.length() > 0) {   
                out.putNextEntry(new ZipEntry(base));   
            } else {   
                out.putNextEntry(new ZipEntry(inputFile.getName()));   
            }   
  
            FileInputStream in = new FileInputStream(inputFile);   
            try {   
                int c;   
                byte[] by = new byte[BUFFEREDSIZE];   
                while ((c = in.read(by)) != -1) {   
                    out.write(by, 0, c);   
                }   
            } catch (IOException e) {   
                throw e;   
            } finally {   
                in.close();   
            }   
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
	
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	} 

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public List<String> getJobIds() {
		return jobIds;
	}

	public void setJobIds(List<String> jobIds) {
		this.jobIds = jobIds;
	}
		
	public CameraInfo getCameraInfo() {
		return cameraInfo;
	}

	public void setCameraInfo(CameraInfo cameraInfo) {
		this.cameraInfo = cameraInfo;
	}

	public Project getCurrentProject() {
		return currentProject;
	}


	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}


	public int getJobStatus() {
		return jobStatus;
	}


	public void setJobStatus(int jobStatus) {
		this.jobStatus = jobStatus;
	}


	public int getNewJobPriority() {
		return newJobPriority;
	}


	public void setNewJobPriority(int newJobPriority) {
		this.newJobPriority = newJobPriority;
	}


	public Map<String, String> getMapScenePath() {
		return mapScenePath;
	}


	public void setMapScenePath(Map<String, String> mapScenePath) {
		this.mapScenePath = mapScenePath;
	}


	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public String getSubMenuName() {
		return subMenuName;
	}

	public void setSubMenuName(String subMenuName) {
		this.subMenuName = subMenuName;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getJob_project_id() {
		return job_project_id;
	}

	public void setJob_project_id(String job_project_id) {
		this.job_project_id = job_project_id;
	}

	public ByteArrayInputStream getRenderResult() {
		return renderResult;
	}

	public void setRenderResult(ByteArrayInputStream renderResult) {
		this.renderResult = renderResult;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}


	public List<RenderEngine> getRenderEngines() {
		return renderEngines;
	}


	public void setRenderEngines(List<RenderEngine> renderEngines) {
		this.renderEngines = renderEngines;
	}


	public List<JobListInfo> getJobListInfos() {
		return jobListInfos;
	}


	public void setJobListInfos(List<JobListInfo> jobListInfos) {
		this.jobListInfos = jobListInfos;
	}


	public String getXmlName() {
		return xmlName;
	}


	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}

  
}
