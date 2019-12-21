package org.xjtu.framework.modules.user.webservice.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
//import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.constant.JobAddReturnInfo;
import org.xjtu.framework.core.base.constant.JobPriority;
import org.xjtu.framework.core.base.constant.JobStartReturnInfo;
import org.xjtu.framework.core.base.constant.JobStatus;
import org.xjtu.framework.core.base.constant.LoginReturnInfo;
import org.xjtu.framework.core.base.constant.NewPasswdReturnInfo;
import org.xjtu.framework.core.base.constant.ProjectAddReturnInfo;
import org.xjtu.framework.core.base.constant.ProjectDelReturnInfo;
import org.xjtu.framework.core.base.constant.ProjectStatus;
import org.xjtu.framework.core.base.constant.RegistReturnInfo;
import org.xjtu.framework.core.base.constant.SystemConfig;
import org.xjtu.framework.core.base.constant.UserType;
import org.xjtu.framework.core.base.model.EmailLink;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.RenderEngine;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.GenerateLinkUtils;
import org.xjtu.framework.core.util.LinuxInvoker;
import org.xjtu.framework.core.util.MD5;
import org.xjtu.framework.core.util.MailUtil;
import org.xjtu.framework.core.util.Thumbnail;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.modules.user.service.EmailLinkService;
import org.xjtu.framework.modules.user.service.FrameService;
import org.xjtu.framework.modules.user.service.JobService;
import org.xjtu.framework.modules.user.service.ProjectService;
import org.xjtu.framework.modules.user.service.RenderEngineService;
import org.xjtu.framework.modules.user.service.TaskService;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.vo.CameraInfo;
import org.xjtu.framework.modules.user.vo.FrameInfo;
import org.xjtu.framework.modules.user.vo.LoginUserInfo;
import org.xjtu.framework.modules.user.vo.ProjectInfo;
import org.xjtu.framework.modules.user.vo.RegisterUserInfo;
import org.xjtu.framework.modules.user.webservice.RenderUserService;

import com.xj.framework.ssh.ShellLocal;

@Service("renderUserService")
public class RenderUserServiceImpl implements RenderUserService{

	private @Resource UserService userService;
	private @Resource ProjectService projectService;
	private @Resource JobService jobService;
	private @Resource FrameService frameService;
	private @Resource EmailLinkService emailLinkService;
	private @Resource TaskService taskService;
	private @Resource RenderEngineService renderEngineService;
	
	private @Resource SystemConfig  systemConfig;
	
	/*娉ㄥ唽:鐢ㄦ埛淇℃伅瀵嗙爜闇�瑕佹槑鏂囷紝鍥犱负闇�瑕佸湪Linux鏈嶅姟鍣ㄤ笂娣诲姞鐢ㄦ埛骞惰缃瘑鐮�*/
	@Override
	public int doRegister(RegisterUserInfo registerUserInfo) {

    	/*楠岃瘉鎻愪氦鐨勬瘡涓睘鎬ф槸鍚︽纭�*/ 
		int ret=userService.validateUser(registerUserInfo);
		
    	if(ret==RegistReturnInfo.success){
    		User existedUser = userService.findUserByName(registerUserInfo.getName());
    		if(existedUser != null){
    			return RegistReturnInfo.existedUser;
    		}
    		
    	
    	 	/*鏈濇暟鎹簱涓彃鍏ヨ褰�*/
    		User user=new User();
    		user.setId(UUIDGenerator.getUUID());
    		user.setName(registerUserInfo.getName());
    		user.setPassword(MD5.encode(registerUserInfo.getPassword()));
    		user.setMobile(registerUserInfo.getMobile());
    		user.setEmail(registerUserInfo.getEmail());
    		
    		  SystemConfig systemConfig=new SystemConfig();
    		  String renderingWorkDir=systemConfig.getRenderingWorkDir();
    		//user.setHomeDir(System.getProperty("catalina.base")+"/webapps/rendering_storage/userStorage/"+registerUserInfo.getName()+"/");
    			try {  
    		user.setHomeDir(renderingWorkDir+"/"+registerUserInfo.getName()+"/");
    		ShellLocal ink = new ShellLocal();
    		ink.setCmd("mkdir -p "+user.getHomeDir());
    		ink.executeCmd();
    		user.setCreateTime(new Date());
    		user.setLastAccessTime(new Date());
    		user.setCardBalances(0.0);
    		user.setType(UserType.MANAGER);//杩欓噷灞曠ず閮借缃垚鐗规潈鐢ㄦ埛
    		userService.addUser(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
        	return RegistReturnInfo.success;
    	}else{
    		return ret;
    	}
	}
	

	/*鐧诲綍:鍙互鏄敤鎴峰悕鎴栭偖绠辩櫥褰�,*/
	@Override
	public int doLogin(LoginUserInfo loginUserInfo) {
		String username=loginUserInfo.getName();
		String password=loginUserInfo.getPassword();
		User user;

		/*鍏堥獙璇佹槸鍚︿负閭*/
		if(userService.validateEmail(username)){
			user = userService.loginByEmail(username, password);

			if(user!=null){
				return LoginReturnInfo.success;
			}else{
				/*妫�楠屾槸瀵嗙爜閿欒杩樻槸鐢ㄦ埛涓嶅瓨鍦�*/
				user=userService.findUserByEmail(username);
				if(user!=null)
					return LoginReturnInfo.errorPasswd;
				else
					return LoginReturnInfo.noneUser;
			}

		}

		/*鍚庨獙璇佹槸鍚︿负鏅�氱敤鎴峰悕*/
		else if(userService.validateUsername(username)){
			user = userService.loginByLoginName(username, password);
 
			if(user != null){
    			return LoginReturnInfo.success;
    		}else{
    			/*妫�楠屾槸瀵嗙爜閿欒杩樻槸鐢ㄦ埛涓嶅瓨鍦�*/
    			user=userService.findUserByName(username);
				if(user!=null)
					return LoginReturnInfo.errorPasswd;
				else
					return LoginReturnInfo.noneUser;
    		}
		}else
			return LoginReturnInfo.noneUser;
	}


	/*濡傛灉鏄偖绠辩櫥褰曪紝鍒欏鎴风鏍规嵁閭鍚嶅彲浠ュ緱鍒扮敤鎴峰悕锛岀敤鏉ヤ綔涓哄悗缁彇寰楀伐绋嬪垪琛ㄧ瓑淇℃伅鐨勫弬鏁�*/
	@Override
	public String doGetUsernameByEmail(String email){
		
		User user=userService.findUserByEmail(email);
		
		if(user!=null){
			return user.getName();
		}
		else
			return null;
		
	}
	
	/*鏀瑰瘑鐮�*/
	@Override
	public int doGetNewPasswdByUsername(String username){
		User user = userService.findUserByName(username);
		if(user!=null){

			EmailLink emailLink=new EmailLink();
			emailLink.setId(UUIDGenerator.getUUID());
			emailLink.setName(username);
			emailLink.setCreateTime(new Date());
			
			boolean temp=MailUtil.sendMail(user.getEmail(),"西交渲染农场取回密码信息","要使用新密码，请使用一下连接启用密码:<br/><a href='" + GenerateLinkUtils.generateResetPwdLink(emailLink,null) +"'>点击重新设置密码</a>","text/html;charset=utf-8",null,null);
			if(!temp){
				/*閭欢鍙戦�佸け璐�*/
				return NewPasswdReturnInfo.emailFailed;
			}
			else{
				/*鑻ラ偖浠跺彂閫佹垚鍔燂紝鍒欏線EmailLink琛ㄤ腑娣诲姞涓�鏉′俊鎭紝鐢ㄦ潵楠岃瘉鐢ㄦ埛閲嶇疆瀵嗙爜鐨勯摼鎺�*/
				emailLinkService.addEmailLink(emailLink);
				return NewPasswdReturnInfo.success;
			}
		}
		else
			return NewPasswdReturnInfo.illegalUser;
	}

	
	/*娣诲姞宸ョ▼*/
	@Override
	public int doAddProject(ProjectInfo projectInfo,String userName){
		User existedUser = userService.findUserByName(userName);
		if(existedUser!=null){
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
			return ProjectAddReturnInfo.success;
		}
		else
			return ProjectAddReturnInfo.illegalUser;
	}
	
	
	/*鍒犻櫎宸ョ▼*/
	@Override
	public int doDelProject(String projectId){
		Project project=projectService.findProjectById(projectId);
		if(project!=null){
			projectService.deleteProject(project);
			return ProjectDelReturnInfo.success;
		}
		else
			return ProjectDelReturnInfo.unexistedProject;
	}
	

	/*寰楀埌鐢ㄦ埛宸ョ▼鍒楄〃*/
	@Override
	public List<ProjectInfo> doGetProjectList(String userName) {
		List<Project> projects=projectService.findProjectsByUserName(userName);
		
		if(projects!=null){
			List<ProjectInfo> projectInfos=null;
			projectInfos=new ArrayList<ProjectInfo>();
			for(Project p:projects){
				ProjectInfo pi=new ProjectInfo();
				pi.setId(p.getId());
				pi.setName(p.getName());
				pi.setCamerasNum(p.getCamerasNum());
				pi.setCreateTime(p.getCreateTime());
				pi.setProjectProgress(p.getProjectProgress());
				pi.setProjectStatus(p.getProjectStatus());
				projectInfos.add(pi);
			}
			return projectInfos;
		}
		else
			return null;		
	}

	
	/*寰楀埌鏌愬伐绋嬬殑闀滃ご鍒楄〃*/
	@Override
	public List<CameraInfo> doGetCameraList(String projectId) {
		List<Job> jobs=jobService.findJobsByProjectId(projectId);
		
		if(jobs!=null){
			List<CameraInfo> cameraInfos=new ArrayList<CameraInfo>();
			for(Job j:jobs){
				CameraInfo ci=new CameraInfo();
				ci.id=j.getId();
				ci.cameraName=j.getCameraName();
				ci.filePath=j.getFilePath();
				ci.jobStatus=j.getJobStatus();
				ci.xResolution=j.getxResolution()+"";
				ci.yResolution=j.getyResolution()+"";
				cameraInfos.add(ci);
			}
			return cameraInfos;
		}
		else
			return null;
	}
	

	/*瀵规煇宸ョ▼娣诲姞闀滃ご*/
	@Override
	public int doAddCamera(CameraInfo cameraInfo,String projectId){
		Project project = projectService.findProjectById(projectId);
		if(project!=null){
			Job j=new Job();
			j.setId(UUIDGenerator.getUUID());
			j.setCameraName(cameraInfo.cameraName);
			j.setCreateTime(new Date());
			j.setJobStatus(JobStatus.notStart);
			j.setQueueNum(-1);
			j.setCameraProgress(0);
			j.setJobPriority(JobPriority.medium);
			
			j.setFrameRange(cameraInfo.frameRange);
			
			String[] ss=cameraInfo.frameRange.split(",");
			
			int frameNumber=0;
			if(ss!=null&&ss.length>0){
				for(int i=0;i<ss.length;i++){
					
					String[] temp=ss[i].split("-");
					
					if(temp!=null&&temp.length>0){
						
						if(temp.length==1){
							frameNumber++;
						}else if(temp.length==2){
							try{
								Integer end=Integer.parseInt(temp[1]);
								Integer begin=Integer.parseInt(temp[0]);
								
								if(end>begin)
									frameNumber=frameNumber+end-begin+1;
								else
									return JobAddReturnInfo.illegalFrameRange;
							}catch(Exception e){
								return JobAddReturnInfo.illegalFrameRange;
							}
						}else{
							return JobAddReturnInfo.illegalFrameRange;
						}
						
					}else{
						return JobAddReturnInfo.illegalFrameRange;
					}
					
				}
			}else{
				return JobAddReturnInfo.illegalFrameRange;
			}
			
			j.setFrameNumbers(frameNumber);
			
			j.setFilePath(cameraInfo.filePath);
			j.setUnitsNumber(cameraInfo.unitsNumber);
			
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
			j.setProject(project);
			
			/*榛樿娓叉煋寮曟搸*/
			if(cameraInfo.renderEngineId==null){
				cameraInfo.renderEngineId="e45c905c33eb4bfd9e42403d2566abad";
				RenderEngine re=renderEngineService.findRenderEnginebyId(cameraInfo.renderEngineId);
				j.setRenderEngine(re);
			}
			
			jobService.addJob(j);
			
			project.setCamerasNum(project.getCamerasNum()+1);
			projectService.updateProjectInfo(project);
			return JobAddReturnInfo.success;
		}
		else
			return JobAddReturnInfo.unexistedProject;
	}

	
	/*杩斿洖鐢ㄦ埛鐩綍涓嬬殑鎵�鏈夐暅澶磋矾寰�*/
	@Override
	public List<String> doGetCameraPathList(String userName){
		User user=userService.findUserByName(userName);
		if(user!=null){
			try {
				List<String> list=new ArrayList<String>();
				Process process = Runtime.getRuntime().exec("find /home/"+user.getName()+"/ -name cam*");
				InputStreamReader ir = new InputStreamReader(process.getInputStream());
				LineNumberReader input = new LineNumberReader(ir);
				
				String line;
				while ((line = input.readLine()) != null){
					list.add(line);
				}
		        return list;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		else
			return null;
	}

	/*寮�濮嬫煇浣滀笟鍗虫煇闀滃ご娓叉煋*/
	@Override
	public int doStartCameraRender(String cameraId){
		
		String jobId=cameraId;
		Job job=jobService.findJobsById(jobId);
		if(job!=null){
			if(job.getJobStatus()==JobStatus.inQueue)
				return JobStartReturnInfo.hasStarted;
			else{

				//鎵惧搴旈槦鍒楃殑鏈�澶у��
				int queueNum=jobService.findMaxQueueNumByPriority(job.getJobPriority())+1;

				job.setQueueNum(queueNum);
				job.setJobStatus(JobStatus.inQueue);
				jobService.updateJobInfo(job);
				return JobStartReturnInfo.success;
			}
		}
		else{
			return JobStartReturnInfo.unexistedJob;
		}
	}


	@Override
	public List<FrameInfo> doGetFrameList(String cameraId) {
		String jobId=cameraId;
		
		List<Frame> frames=frameService.findFramesByJobId(jobId);
		
		if(frames!=null){
			List<FrameInfo> frameInfos=new ArrayList<FrameInfo>();
			for(Frame f:frames){
				FrameInfo fi=new FrameInfo();
				fi.setId(f.getId());
				fi.setFrameName(f.getFrameName());
				fi.setFrameProgress(f.getFrameProgress());
				fi.setFrameStatus(f.getFrameStatus());
				fi.setErrorInfo(f.getErrorInfo());
				frameInfos.add(fi);
			}
			return frameInfos;
		}
		else
			return null;
	}
	
	@Override
	public DataHandler doGetRenderResult(String userName,String frameId,int xResolution,int yResolution){
		try {
			Thumbnail thum = new Thumbnail("/home/shanda/Desert.jpg");			
			thum.resizeFix(500, 300);
			DataHandler dataHandler = new DataHandler(new FileDataSource(thum.getDestFile()));
			return dataHandler;			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public double doGetCameraProgress(String cameraId){
		String jobId=cameraId;
		List<Task> ts=taskService.findTasksByJobId(jobId);
		
		double progress=0.0;
		double frameNums=0.0;
		
		for(int i=0; i<ts.size();i++){
			progress+=ts.get(i).getTaskProgress()*ts.get(i).getFrameNumber();
			frameNums+=ts.get(i).getFrameNumber();
		}
		
		progress=progress/frameNums;
		
		return progress;

	}
	
}
