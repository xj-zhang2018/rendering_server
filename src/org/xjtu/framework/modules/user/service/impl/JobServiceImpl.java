package org.xjtu.framework.modules.user.service.impl;

import com.xj.framework.ssh.ShellLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xjtu.framework.core.base.constant.*;
import org.xjtu.framework.core.base.model.*;
import org.xjtu.framework.core.util.PbsExecute;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.modules.protocol.TaskDiscription;
import org.xjtu.framework.modules.socket.Client;
import org.xjtu.framework.modules.user.dao.*;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.ConfigurationService;
import org.xjtu.framework.modules.user.service.JobService;
import org.xjtu.framework.modules.user.service.UnitService;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;



@Service("jobService")
public class JobServiceImpl implements JobService {
	private @Resource JobDao jobDao;
	private @Resource ProjectDao projectDao;
	
	private @Resource UserDao userDao;
	
	private @Resource FrameDao frameDao;
	
	private @Resource TaskDao taskDao;
	
	private @Resource UnitDao unitDao;
	
	private @Resource ConfigurationDao configurationDao;
	
	private @Resource ClusterManageCommand pbsCommand;
	
	private @Resource ClusterManageCommand shenweiCommand;
	
	private @Resource SystemConfig systemConfig;
	
		
	private static final Log log = LogFactory.getLog(JobServiceImpl.class);	
	private @Resource UnitService unitService;
	private @Resource ClusterManageService clusterManageService;
	private @Resource ConfigurationService configurationService;	
	private String pbsFilePath=this.getClass().getResource("/").getPath()+"shell/render_building_rUnit.pbs";
	
	@Override
	public List<Job> findJobsByProjectId(String projectId) {
		return jobDao.queryJobsByProjectId(projectId);
	}
	
	@Override
	public void addJob(Job job){
		jobDao.persist(job);
	}
		
	@Override
	public void deleteJob(Job job) {
		jobDao.removeJob(job);
	}
	
	@Override
	public Job findJobsById(String jobId){
		return jobDao.queryJobsById(jobId);
	}

	@Override
	public void updateJobInfo(Job job){
		jobDao.updateJob(job);
	}
	
	@Override
	public int findTotalCountByQuery(String searchText,String searchType,int jobStatus) {
		int num = 0;
		if(StringUtils.isBlank(searchText)){
			num = jobDao.queryCount(jobStatus);
		}else if(StringUtils.isBlank(searchType)){
			num = jobDao.queryCount(jobStatus);
		}else{
			if(searchType.equals("name")){
				num = jobDao.queryCountByJobName(searchText,jobStatus);
			}else if(searchType.equals("accurateProjectId")){
				num = jobDao.queryCountByAccurateProjectId(searchText,jobStatus);
			}else{
				num = jobDao.queryCount(jobStatus);
			}
		}
		return num;
	}
	
	@Override
	public List<Job> listJobsByQuery(String searchText,String searchType, int pageNum, int pageSize,int jobStatus) {
		List<Job> jobs = new ArrayList<Job>();
		if(StringUtils.isBlank(searchText)){
			jobs = jobDao.pagnate(pageNum, pageSize, jobStatus);
		}else if(StringUtils.isBlank(searchType)){
			jobs = jobDao.pagnate(pageNum, pageSize, jobStatus);
		}else{
			if(searchType.equals("name")){
				jobs = jobDao.pagnateByJobName(searchText, pageNum, pageSize, jobStatus);
			}else if(searchType.equals("accurateProjectId")){
				jobs = jobDao.pagnateByAccurateProjectId(searchText, pageNum, pageSize, jobStatus);
			}else{
				jobs = jobDao.pagnate(pageNum, pageSize, jobStatus);
			}
		}
		
		return jobs;
	}
	
	@Override
	public int findMaxQueueNumByPriority(int jobPriority) {
		return jobDao.queryMaxQueueNumByPriority(jobPriority);
	}
	
	@Override
	public int findMinQueueNumByPriority(int jobPriority) {
		return jobDao.queryMinQueueNumByPriority(jobPriority);
	}
	
	@Override
	public int findNewJobCountsByDate(Date date){
		return jobDao.queryNewJobCountsByDate(date);
	}
	@Override
	public int findStartJobCountsByDate(Date date){
		return jobDao.queryStartJobCountsByDate(date);
	}
	@Override
	public int findEndJobCountsByDate(Date date){
		return jobDao.queryEndJobCountsByDate(date);
	}

	@Override
	public List<Job> findQueueJobsByJobPriority(int jobPriority) {
		return jobDao.queryQueueJobsByJobPriority(jobPriority);
	}

	@Override
	public Job findJobByJobNameAndProjectId(String jobName, String projectId) {
		return jobDao.queryJobByJobNameAndProjectId(jobName,projectId);
	}
	
	@Override
	public List<Job> findDistributedJob(){
		return jobDao.queryDistributedJob();
		
	}

	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.SERIALIZABLE)
	public void suspendJobByJobId(String jobId) {
		Job job=jobDao.queryJobsById(jobId);
		if(job!=null&&job.getJobStatus()==JobStatus.distributed){
			List<Unit> units=unitDao.queryUnitsByJobId(jobId);
			if(units!=null){
				for(int j=0;j<units.size();j++){
					try {
						//鍏堥攢姣佹覆鏌撳崟鍏�
						if(systemConfig.getJobManageService().equals("openPBS")){
							pbsCommand.delJob(units.get(j).getPbsId());
						}else{
							shenweiCommand.delJob(units.get(j).getPbsId());
						}
						System.out.println(units.get(j).getPbsId()+" has been killed");
						//涓嶅垹闄よ鏁版嵁椤规槸涓轰簡淇濈暀瀵瑰簲鐨勫抚淇℃伅鍜屼换鍔′俊鎭�
						Unit u=units.get(j);
						u.setUnitStatus(UnitStatus.dead);
						unitDao.updateUnit(u);
					} catch (Exception e) {
						e.printStackTrace();
					}
											
					//鎵惧埌璇ュ崟鍏冧笂鎵�鏈夋湭瀹屾垚鐨勫抚锛屼笖鐢变簬鍗曞厓璺焧ask鏄竴涓�瀵瑰簲鐨勶紝鎵�浠ヨ繖浜涘抚灞炰簬鍚屼竴task
					List<Frame> frames = frameDao.queryUnfinishedFramesByUnitId(units.get(j).getId());
					if(frames!=null&&frames.size()!=0){		        	
						//鏇存柊鍘熷厛杩欎簺甯ф墍灞炵殑task淇℃伅
						Task beforeTask=frames.get(0).getTask();
						beforeTask.setFrameNumber(beforeTask.getFrameNumber()-frames.size());
						beforeTask.setTaskProgress(100);//鍘熸潵鐨則ask涓嬪墿浣欑殑甯ц偗瀹氬凡瀹屾垚鐨勪簡锛屾墍浠ヨ繘搴︿负100%
						beforeTask.setTaskStatus(TaskStatus.finished);
						taskDao.updateTask(beforeTask);
						//鏇存柊杩欎簺闇�瑕佽閲嶆柊璁＄畻鐨勫抚
						for(int k =0 ;k < frames.size(); k++){
							Frame frame = frames.get(k);		        						
							frame.setFrameProgress(0);
							frame.setFrameStatus(FrameStatus.notStart);
							frameDao.updateFrame(frame);
						}		        																	
					
					}
				}
			}
			
			//job鐨勮繘搴︿篃闇�瑕佹洿鏂�
			job.setJobStatus(JobStatus.suspended);
			jobDao.updateJob(job);

		}
	}
	
	
	
	
	
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.SERIALIZABLE)
	public void doStartCameraRender(List<String> jobIds) {
		
		for(int i=0;i<jobIds.size();i++){
			Job job=jobDao.queryJobsById(jobIds.get(i));
			if(job!=null){
				if(job.getJobStatus()==JobStatus.inQueue||job.getJobStatus()==JobStatus.distributed){
					continue;
				}
				else{
					//当前渲染文件夹下有压缩文件zip的话，先解压才可以
					String filePath = job.getFilePath();
					
				//	log.info("decompression ZipCmd is"+"cd "+filePath+" && unzip mydata.zip");
				//	String decompressionZipCmd="cd "+filePath+" && unzip mydata.zip";
     			//	ShellLocal shell=new ShellLocal(decompressionZipCmd);
					
					int queueNum=jobDao.queryMaxQueueNumByPriority(job.getJobPriority())+1;
					job.setQueueNum(queueNum);
					job.setJobStatus(JobStatus.inQueue);
					jobDao.updateJob(job);
				}
			}
		}
	}
	
	@Override
	public void doCopyjobs(List<String> jobIds){
		for(int i=0;i<jobIds.size();i++){
			Job job=jobDao.queryJobsById(jobIds.get(i));
			if(job!=null){
				Job j=new Job();
				int num;
				j.setId(UUIDGenerator.getUUID());
				num=jobDao.queryCountcopyJobName(job.getCameraName());
				j.setCameraName(job.getCameraName()+"-副本"+num);
				j.setCreateTime(new Date());
				j.setJobStatus(JobStatus.notStart);
				j.setQueueNum(-1);
				j.setCameraProgress(0);
				j.setJobPriority(job.getJobPriority());					
				j.setFrameRange(job.getFrameRange());					
				j.setFrameNumbers(job.getFrameNumbers());															
				j.setFilePath(job.getFilePath());
				j.setRenderCost(0.0);
				j.setPreRenderingTag(job.getPreRenderingTag());
				j.setxResolution(job.getxResolution());
				j.setyResolution(job.getyResolution());
				j.setSampleRate(job.getSampleRate());
				j.setRenderEngine(job.getRenderEngine());
				j.setUnitsNumber(job.getUnitsNumber());
				Project p= job.getProject();
				j.setProject(p);
				jobDao.persist(j);
				p.setCamerasNum(p.getCamerasNum()+1);
				projectDao.updateProject(p);
				
				
				}
			
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.SERIALIZABLE)
	public void doStopCameraRender(String jobId) {
		
		Job job=jobDao.queryJobsById(jobId);
			
		List<Unit> units=unitDao.queryUnitsByJobId(jobId);
		if(units!=null&&units.size()>0){
			for(int j=0;j<units.size();j++){
				try {
					if(systemConfig.getJobManageService().equals("openPBS")){
						pbsCommand.delJob(units.get(j).getPbsId());
					}else{
						shenweiCommand.delJob(units.get(j).getPbsId());
					}
					unitDao.removeUnit(units.get(j));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		job.setEndTime(null);
		job.setStartTime(null);
		job.setCameraProgress(0);
		job.setJobStatus(JobStatus.notStart);
		jobDao.updateJob(job);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.SERIALIZABLE)
	public void changeQueuingJobToTop(String jobId) {
		Job j=jobDao.queryJobsById(jobId);
		if(j!=null&&j.getJobStatus()==JobStatus.inQueue){
			int priority=j.getJobPriority();
			int queueNum=jobDao.queryMinQueueNumByPriority(priority)-1;
			j.setQueueNum(queueNum);
			jobDao.updateJob(j);

		}
	}

	@Override
	public Job findHeadQueuingJob() {
		return jobDao.queryHeadQueuingJob();
	}
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.SERIALIZABLE)
	public Boolean distributeRunitJob(Job job, int unitsNumber, int nodesNumPerUnit, String renderEngineName,int nodes) {
		Job existedJob=jobDao.queryJobsById(job.getId());
		log.info("distribute job is starting"); 
		if(job.equals(existedJob)){
			String filePath = existedJob.getFilePath();
			String frameRange = existedJob.getFrameRange();
			int frameNums = existedJob.getFrameNumbers();							
			//filePath=filePath.substring(0,filePath.lastIndexOf("/"));
			
			//10.25 Thys 新版本只有一个XML文件，获取文件名到mainXML里
			String mainXML = "";
			
			
			//10.25 Thys 新版本帧范围开始
			/*String cmd = "cd "+filePath+" && cat frameMax.txt";
			ShellLocal shell = new ShellLocal(cmd);
			String result = shell.executeCmd();
			result = result.trim();
			int frameMax = Integer.parseInt(result);*/
			
			Map<Integer,String> mapScenePath=new HashMap<Integer,String>();
			
			//10.25 Thys 指令运行指定的所有帧
			String allFrame = "";
			
			//预渲染帧值
			ArrayList<Integer> frameToPreRender = new ArrayList<Integer>();
			
			//Thys 预渲染所有帧
			String preAllFrame = "";
			
			//原来预渲染变量位置
			//ArrayList<Integer> frameToPreRender = new ArrayList<Integer>();
			
			
			//10.25 Thys 把每帧的数值存进frameToRender[],把每帧的帧名存进mapScenePath
			int[] frameToRender = new int[frameNums];
			int k=0;
			if(frameToRender != null && frameToRender.length > 0) {
				String[] ss = frameRange.split(",");
				for(int i = 0;i < ss.length;i++){
					String[] temp = ss[i].split("-");
					if(temp.length == 1){
						Integer tempToInteger = Integer.parseInt(temp[0]);
						frameToRender[k] = tempToInteger;
						k++;
						
						mapScenePath.put(tempToInteger, "frame"+temp[0]+".xml");
						
						allFrame = allFrame + tempToInteger + ",";
					}else{
							Integer end = Integer.parseInt(temp[1]);
							Integer begin = Integer.parseInt(temp[0]);
							for(int z = begin;z < end + 1;z++){
								frameToRender[k] = z;
								k++;
								
								mapScenePath.put(z, "frame"+z+".xml");
								
								allFrame = allFrame + z + ",";
							}
					}
				}
			}
			
			//10.25 Thys 去除尾部多余的逗号
			allFrame = allFrame.substring(0, allFrame.length()-1);
			//10.26 Thys 不知名原因会在allFrame头部添加null
			allFrame = allFrame.replaceAll("null", "");
		
			
			//10.25 Thys 新版本帧范围结束
			
			//10.25 Thys 获取作业的唯一XML文件名保存到mainXML
			String cmdForXML = "cd "+filePath+" && ls *.xml";
			ShellLocal shellForXML = new ShellLocal(cmdForXML);
			String resultForXML = shellForXML.executeCmd();
			resultForXML = resultForXML.trim();
			String[] xmlFileNames = resultForXML.split("\\n");
			//正则匹配
			String pattern = "frame.*";
			for(int i = 0;i < xmlFileNames.length;i++) {
				if(!Pattern.matches(pattern, xmlFileNames[i]))
					mainXML = xmlFileNames[i];
			}
			
			
			//老版本帧范围开始
			/*String cmd="cd "+filePath+" && ls *.xml";
			ShellLocal shell=new ShellLocal(cmd);
			String result=shell.executeCmd();
			result=result.trim();
			
			String[] filenames=result.split("\\n");//娓叉煋鐨勬枃浠跺悕+甯у彿
			Map<Integer,String> mapScenePath=new HashMap<Integer,String>();
			for(int i=0;i<filenames.length;i++){
				String[] frameNo=filenames[i].split("\\.");
				Integer key;
				try{
					//eg锛沠rame1.xml
					String frameID=StringUtil.getNumb(frameNo[frameNo.length-2]);
					key=Integer.parseInt(frameID);
					//key=Integer.parseInt(frameNo[frameNo.length-2]);
				}catch(NumberFormatException e){
					continue;
				}
				mapScenePath.put(key, filenames[i]);//(甯у彿鍜屾枃浠跺悕瀛�)
			}
		
			
			int[] frameToRender=new int[frameNums];
			int k=0;
			if(frameToRender!=null&&frameToRender.length>0) {
			String[] ss=frameRange.split(",");
				for(int i=0;i<ss.length;i++){
					String[] temp=ss[i].split("-");
					if(temp.length==1){
						frameToRender[k]=Integer.parseInt(temp[0]);
						k++;
					}else{
							Integer end=Integer.parseInt(temp[1]);
							Integer begin=Integer.parseInt(temp[0]);
							for(int z=0;z<end-begin+1;z++){
								frameToRender[k]=begin+z;
								k++;
							}
					}
				}
			}*/
			//老版本帧范围结束
			
			
			// 闇�瑕佸皢unitsNumber鍒嗘垚涓ょ被锛屽叾涓竴绫昏澶氭覆鏌�1甯х殑鐢婚潰銆�
			int between = frameNums / unitsNumber; // 瀹氫箟姣忎釜鑺傜偣闇�瑕佹壙鎷呯殑浠诲姟閲�
			int remain = frameNums % unitsNumber; // 璁＄畻鍑轰綑鏁�,鏍规嵁鏁板鐭ヨ瘑锛屼綑鏁扮殑涓暟涓洪渶瑕佸娓叉煋涓�甯х殑鑺傜偣鏁伴噺
			int preRenderTag = existedJob.getPreRenderingTag();
			int s=0;
			int e=frameNums;
			int NodesNumPerUnit=configurationService.findAllConfiguration().getNodesNumPerUnit();
			String fuWuListName=configurationService.findAllConfiguration().getFuWuListName();
			String sceneMem=configurationService.findAllConfiguration().getSceneMemory();
			String hostStack=configurationService.findAllConfiguration().getHostStack();
			String shareMen=configurationService.findAllConfiguration().getShareSize();
			//杩欓噷鏄敤鎴锋寚瀹氫簡鍑犱釜鍗曞厓锛屾瘡涓崟鍏冨垎閰嶅嚑绁换鍔�
			for (int i = 0; i < unitsNumber; i++) {
				int temp;
				// 纭畾鎵ц鑺傜偣
				String stdout="";
				String newUnitId=UUIDGenerator.getUUID();
				log.info("preRender handal....");
				/*鍦ㄨ繖閲屽仛涓や欢浜嬶紝1锛屽垎閰嶆甯哥殑浣滀笟娓叉煋浠诲姟銆�
				2鍒嗛厤棰勬覆鏌擄紝棰勬覆鏌撻鍏堟牴鎹噰鏍疯矾鎵惧埌瑕侀娓叉煋鐨勭ク锛岀劧鍚庝慨鏀硅繖浜涚ク鐨勫儚绱犵偣鏁板拰鐓х墖瀹藉拰楂�,,
				鏋勬垚鏂扮殑棰勬覆鏌撴覆鏌撴枃浠秞ml,鍛藉悕涓篺rame1_pre.xml,鏈�鍚庣粰鐢ㄦ埛灞曠幇*/
				int PreRenderingTag=job.getPreRenderingTag();
				List<String>PreFramePath=new ArrayList<String>();//鐢ㄦ潵瀛樺偍棰勬覆鏌撶殑鐨勮矾寰�
				if(PreRenderingTag==1){
					//鍒嗛厤棰勬覆鏌撲换鍔�
					int FrameNumbers=job.getFrameNumbers();
					String FrameRange=job.getFrameRange();
					int SampleRate=job.getSampleRate();
					int x=job.getxResolution();
					int y=job.getyResolution();
					
					/*
					 * int preFrameNum = (int) Math.rint(frameNums * SampleRate / 100);
					 * 
					 * //Thys 新预渲染 //预渲染抽帧参数 int level = 0; int levelNum = 1;
					 * 
					 * //用来存每层帧数 ArrayList<Integer> frameNumToPerLevel = new ArrayList<Integer>();
					 * //用来记录每层起始和终止位置S--start,E--end ArrayList<Integer> perLevelSE = new
					 * ArrayList<Integer>(); perLevelSE.add(frameToRender[0]); //记录未分配余数时，已分配的帧数 int
					 * frameNumNoRemainder = 0; //设置层最大值,根据渲染帧数数量级来确定 int levelNumMax; int
					 * levelNumMaxK; levelNumMax = 1; levelNumMaxK = 10; while (frameNums /
					 * levelNumMaxK != 0) { levelNumMax *= 10; levelNumMaxK *= 10; }
					 * 
					 * //先确定预渲染层数level，和每层分配的初始帧数（不处理余数） for (int j = 1;j < frameToRender.length;j
					 * ++) { if (frameToRender[j] == (frameToRender[j - 1] + 1)) { if (levelNum <
					 * (levelNumMax / 10)) { levelNum ++; } else { perLevelSE.add(frameToRender[j -
					 * 1]);
					 * 
					 * int preFrameTemp = (int) ((double) levelNum / frameNums * preFrameNum);
					 * frameNumToPerLevel.add(preFrameTemp); frameNumNoRemainder += preFrameTemp;
					 * 
					 * level ++; perLevelSE.add(frameToRender[j]); levelNum = 1; }
					 * 
					 * if (j == frameToRender.length - 1) { perLevelSE.add(frameToRender[j]);
					 * 
					 * int preFrameTemp = (int) ((double) levelNum / frameNums * preFrameNum);
					 * frameNumToPerLevel.add(preFrameTemp); frameNumNoRemainder += preFrameTemp; }
					 * } else { perLevelSE.add(frameToRender[j - 1]);
					 * 
					 * int preFrameTemp = (int) ((double) levelNum / frameNums * preFrameNum);
					 * frameNumToPerLevel.add(preFrameTemp); frameNumNoRemainder += preFrameTemp;
					 * 
					 * level ++; perLevelSE.add(frameToRender[j]); levelNum = 1; } }
					 * 
					 * //对抽取预渲染总帧数预处理,提高精度 int preFrameNumRemainder = preFrameNum % (level + 1);
					 * //对于预渲染帧数与层数相差不多preFrameNumRemainder得改为差值 if(Math.abs(preFrameNum - level) <=
					 * 5) { preFrameNumRemainder = Math.abs(preFrameNum - frameNumNoRemainder); }
					 * 
					 * //System.out.println("preFrameNumRemainder " + preFrameNumRemainder);
					 * 
					 * //确定每层所取帧数 //把余数先放奇数层帧数，多出来的再放偶数层，直到分配完 int frameNumToPerLevelFlag = 0; while
					 * (preFrameNumRemainder > 0) { if(frameNumToPerLevelFlag <= level) {
					 * frameNumToPerLevel.set(frameNumToPerLevelFlag,
					 * frameNumToPerLevel.get(frameNumToPerLevelFlag) + 1); } else {
					 * //如果是奇数层，就减去level + 1，注意数组从0起 if (level % 2 == 0) {
					 * frameNumToPerLevel.set(frameNumToPerLevelFlag - level - 1,
					 * frameNumToPerLevel.get(frameNumToPerLevelFlag - level - 1) + 1); } else {
					 * frameNumToPerLevel.set(frameNumToPerLevelFlag - level,
					 * frameNumToPerLevel.get(frameNumToPerLevelFlag - level) + 1); } }
					 * 
					 * frameNumToPerLevelFlag += 2; preFrameNumRemainder --; }
					 * 
					 * 
					 * 
					 * //System.out.println("frameNumToPerLevel " + frameNumToPerLevel);
					 * 
					 * 
					 * //System.out.println("perLevelSE " + perLevelSE);
					 * 
					 * //调用状态转移的方法，保存每层要预渲染的帧数 int levelSEFlag = -1; for (int j = 0;j <= level;j ++)
					 * { frameToPreRender.addAll(statusRandom(perLevelSE.get(++
					 * levelSEFlag),perLevelSE.get(++ levelSEFlag),frameNumToPerLevel.get(j))); }
					 * 
					 * for(int j = 0 ; j < frameToPreRender.size(); j ++) { preAllFrame +=
					 * (frameToPreRender.get(i) + ","); }
					 * 
					 * //10.25 Thys 去除尾部多余的逗号 preAllFrame = preAllFrame.substring(0,
					 * preAllFrame.length()-1);
					 */
					
					
					//新预渲染结束
					
					/*//sed -i -e '1 iaaaa\nbbbb\ncccc'  test
					String sedContent="";

					int PreframeNum=(int)(FrameNumbers*SampleRate*0.01);
					String PreframeNo[]=StringUtil.getSelectFrame(FrameRange,PreframeNum); //瑕佸厛瀵瑰瓧绗︿覆鎺掑簭
					for(String str:PreframeNo){
					try {
						//澶囦唤娓叉煋鏂囦欢锛屼慨鏀归娓叉煋鐨勫垎杈ㄧ巼鍜屽儚绱狅紝鍋囧畾璁剧疆涓�100涓儚绱�,绗竴娆″鍒跺悗淇敼锛岀浜屾鍦ㄦ柊鏂囦欢淇敼
						//鏇挎崲閲囨牱鐜�
						//String cpCmd="cp "+filePath+"/frame"+str+".xml"+" "+filePath+"/frame"+str+"_pre.xml";
						
						
						//String sampleCountCmd="sed -i 's;<integer name=\"sampleCount\" value=\".*\"/>;<integer name=\"sampleCount\" value=\"100\"/>;g' "+ filePath+"/frame"+str+"_pre.xml";
						//String heightCmd="sed -i 's;<integer name=\"height\" value=\".*\"/>;<integer name=\"height\" value=\""+x+"\"/>;g' "+filePath+"/frame"+str+"_pre.xml";
						//String widthCmd="sed -i 's;<integer name=\"width\" value=\".*\"/>;<integer name=\"width\" value=\""+y+"\"/>;g' "+filePath+"/frame"+str+"_pre.xml";
						
						//String Cmd=cpCmd+" && "+sampleCountCmd+" && "+heightCmd+" && "+widthCmd;
						//ShellLocal ink = new ShellLocal();
		    			//ink.setCmd(Cmd);
						//ink.executeCmd();
						//sedContent+="";
						//PreFramePath.add(filePath+"/frame"+str+"_pre.xml");
						
						ShellLocal ink = new ShellLocal();
						
						
						String cpCmd="cp "+filePath+"/frame"+str+".xml"+" "+filePath+"/frame"+str+"copy.xml";//<integer name="sampleCount" value="128" />
					//	String sampleCountCmd="sed -i 's;<integer name=\"sampleCount\" value=\".*\"/>;<integer name=\"sampleCount\" value=\"100\"/>;g' "+ filePath+"/frame"+str+"copy.xml";
					//	String heightCmd="sed -i 's;<integer name=\"height\" value=\".*\"/>;<integer name=\"height\" value=\""+x+"\"/>;g' "+filePath+"/frame"+str+"copy.xml";
					//	String widthCmd="sed -i 's;<integer name=\"width\" value=\".*\"/>;<integer name=\"width\" value=\""+y+"\"/>;g' "+filePath+"/frame"+str+"copy.xml";
					//	ink.setCmd(cpCmd+" && "+sampleCountCmd+" && "+heightCmd+" && "+widthCmd);
						
		    			ink.setCmd(cpCmd);
						ink.executeCmd();
						PreFramePath.add("frame"+str+"copy.xml");
					//鏀堕泦璧锋柊澶囦唤鐨勪慨鏀瑰儚绱犲悗鐨勭ク锛屼氦缁欑濞佹覆鏌撱��
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					}
					
					//瀹為獙闃舵锛氭澶勬坊鍔犳覆鏌撶殑甯ф暟锛岄粯璁よ寖鍥存槸鍏ㄦ覆鏌擄紝鍚ч娓叉煋鐨勫抚鍔犲埌frameInfo.txt
					String Range=StringUtil.FormateFrameNumberStr(frameRange);
					int frameCount=0;//寰�frameInfo.txt鍐欑殑绗竴琛屾暟鎹�
					StringBuilder builder=new StringBuilder();
					String context="";//寰�frameInfo.txt鍐欑殑娓叉煋甯х殑鍚嶅瓧
					if(Range!=null&&Range.length()>0){
						String []frameNumbers=Range.split(",");
						frameCount=frameNumbers.length+PreFramePath.size();
						builder.append(frameCount+"\n");
						for(String number:frameNumbers){
							builder.append("frame"+number+".xml\n");
						}
						for(String preframe:PreFramePath){
							builder.append(preframe+"\n");
						}
						System.out.println("追加的内容："+builder.toString());
						
						String clearCmd="cat /dev/null > "+filePath+"/frameInfo.txt";
						String appendCmd="echo '"+builder.toString()+"' > "+filePath+"/frameInfo.txt";
						ShellLocal ink = new ShellLocal();
						System.out.println("cmd is"+clearCmd+" && "+appendCmd);
		    			ink.setCmd(clearCmd+" && "+appendCmd);
						ink.executeCmd();
					}
				
					//end//
					
					log.info("preRender frame list is"+PreFramePath);*/
					
				}
				//------------------------------------澶勭悊闃舵-------------------------------------------
				if(systemConfig.getJobManageService().equals("openPBS")){	
					stdout = clusterManageService.submit(pbsFilePath);
				}else{
					//杩欓噷鍙互鍒犻櫎锛屼篃鍙互鍋氫竴浜涘垵濮嬪寲鐨勫伐浣滐紝鍚庨潰鍐嶇‘瀹�
					 SystemConfig systemConfig=new SystemConfig();
					 ///home/export/online1/systest/swsdu/xijiao/Users閰嶇疆鏂囦欢鐨勫伐浣滅洰褰�
		    		String renderingWorkDir=systemConfig.getRenderingWorkDir();
	                    //鎸囦护鍦板潃鍚庢湡鏇存敼  
		    		//bsub -q q_sw_share -I -b -share_size 7168 -n 31 /home/export/online1/systest/swsdu/xijiao/mitsuba-0420/dist/mitsuba /home/export/online1/systest/swsdu/xijiao/framedir2/ |tee result-frame-n21
		    	
		    		
		    		//new renderInstruct
		    		//String renderInstruct="/home/export/online1/systest/swsdu/xijiao/mitsuba-xxf-test/dist/mitsuba";
		    		//String Cmd="cd /home/export/online1/systest/swsdu/xijiao/mitsuba-xxf-test";//杩涘叆寮曟搸鐩綍
					
		    		
		    		
		    		//old renderInstruct
		    		//String renderInstruct="/home/export/online1/systest/swsdu/xijiao/mitsuba-diao-end/dist/mitsuba";
		    		//String Cmd="cd /home/export/online1/systest/swsdu/xijiao/mitsuba-diao-end";
		    		//10.25之前的引擎
		    		/*String renderInstruct="/home/export/online1/systest/swsdu/xijiao/RWing-demo/dist/RWing";
		    		String Cmd="cd /home/export/online1/systest/swsdu/xijiao/RWing-demo";
		    		String shenweiPbsCommand="";*/
		    		
		    		//10.25 Thys更换的新版引擎
		    		String renderInstruct="/home/export/online1/systest/swsdu/xijiao/RWing1021/dist/RWing";
		    		String Cmd="cd /home/export/online1/systest/swsdu/xijiao/RWing1021";
		    		String shenweiPbsCommand="";
						Date stime=new Date();//姝ゅ鍏堝叏閮ㄦ覆鏌擄紝鍚庨潰鏇存敼
						String jobName=job.getCameraName();
						if(renderEngineName.equals("RWing"))
							//shenweiPbsCommand="-q "+fuWuListName+" -o /home/export/online1/systest/swsdu/xijiao/Outprint/"+newUnitId+".out"+"-l -b -share_size 7168 -n 31 "+ renderInstruct+" "+filePath+"/";
							//上一个版本(10.25老引擎的上一个)
							//shenweiPbsCommand="-cross -q "+fuWuListName+" -b -o /home/export/online1/systest/swsdu/xijiao/Outprint/Renderhouse-202-Job-"+jobName+".out -J "+ jobName+" -n 202 -np 1 -sw3runarg \"-a 1\" -host_stack 2600 -cross_size 20000 "+renderInstruct+" "+filePath+"/";
						
							//10.25更换掉的老引擎
							//shenweiPbsCommand="-cross -q "+fuWuListName+" -b -o /home/export/online1/systest/swsdu/xijiao/Outprint/deskroom-2043-Job-"+jobName+".out -J "+ jobName+" -n "+nodes+" -share_size 7168 "+renderInstruct+" "+filePath+"/";
							
							//10.25 Thys更换的新引擎
							shenweiPbsCommand = "-cross -q " + fuWuListName + " -b -o /home/export/online1/systest/swsdu/xijiao/Outprint/" + jobName + ".out -J " + jobName+" -n " + nodes + " -np 1 -sw3runarg \"-a 1\" -host_stack 2600 -cross_size 20000 " + renderInstruct + " " + filePath+"/" + mainXML + " " + allFrame;
						
						else
							shenweiPbsCommand="-b -m 1 -p -q "+fuWuListName+" -o /home/export/online1/systest/swsdu/Outprint/"+newUnitId+".out -host_stack "+hostStack+" -share_size "+shareMen+" -n "+NodesNumPerUnit+" -cgsp 64 /home/export/online1/systest/swsdu/RenderWing_hust/bin/rUnit -t 1 -b "+sceneMem+" -s "; 
						//stdout = clusterManageService.submit(shenweiPbsCommand);
						PbsExecute  pbs=new PbsExecute(Cmd+" && bsub "+shenweiPbsCommand);
					     stdout=pbs.executeCmd().trim();
					     log.info("return stdout is "+stdout);
					     log.info("bsub comand is "+shenweiPbsCommand);
					     if(stdout!=null&&stdout.length()>0)
					    	 stdout=stdout.substring(stdout.indexOf('<')+1,stdout.indexOf('>'));
						/*Date etime=new Date();
						log.info("WLBSUB"+ (etime.getTime()-stime.getTime()));*/
				          }
				log.info("Initializing Rendering Unit..."+stdout);
				Unit unit=new Unit();
				unit.setId(newUnitId);
				unit.setPbsId(stdout);
				//unit.setUnitStatus(UnitStatus.unavailable);
				unit.setUnitStatus(UnitStatus.busy);
				unit.setUnitNodesNum(nodesNumPerUnit);
				unit.setIdleNumber(0);
				unitService.addUnit(unit);
				log.info("Rendering Unit initialized");
				
				if (i < unitsNumber - remain) {
					temp = s + between - 1;
				} else {
					temp = s + between;
				}
				// 璁板綍浠诲姟淇℃伅
				Task task=new Task();
				task.setId(UUIDGenerator.getUUID());
				System.out.println("temp :"+temp+",s:"+s+",between:"+between+",:remain:"+remain);
				task.setFrameNumber(temp-s+1);
				task.setTaskProgress(0);
				task.setTaskStatus(TaskStatus.started);
				task.setStartTime(new Date());
				task.setJob(existedJob);
				task.setUnit(unit);
				task.setUpdateTime(new Date());
				taskDao.persist(task);
				for (int j = s; j < temp + 1; j++) {
					Frame frame = new Frame();
					frame.setStartTime(new Date());
					frame.setId(UUIDGenerator.getUUID());
					frame.setFrameName(mapScenePath.get(frameToRender[j]));				
					frame.setFrameStatus(FrameStatus.notStart);
					frame.setFrameProgress(0);
					frame.setTask(task);
					frameDao.persist(frame);
				}								
			
				s = temp + 1; 
			}
		
			existedJob.setStartTime(new Date());
			existedJob.setJobStatus(JobStatus.distributed);
			jobDao.updateJob(existedJob);
			log.info("分配渲染任务已完成，distibute is finish=====================>");
			return true;
		}else{
			return false;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.SERIALIZABLE)
	public Boolean continueRunitJobtoTask(Job job, int unitsNumber, int nodesNumPerUnit, List<Frame> fs, String renderEngineName) {
		Job existedJob=jobDao.queryJobsById(job.getId());
		if(job.equals(existedJob)){
			int s = 0;
			int e = fs.size()-1;			
			String filePath = existedJob.getFilePath();
			int preRenderTag = existedJob.getPreRenderingTag();
			
			// 杩涜浣滀笟鍒嗗彂锛屼綔涓氬垎鍙戠殑鎬濇兂鏄湪澶氫釜娓叉煋鑺傜偣涓婅繘琛屽潎鍒�
			// 璁捐浜嗕竴涓柊鐨勬洿鑳戒綋鐜板潎鍒嗘�濇兂鐨勭畻娉曪紙闄ゆ硶瀹氱悊锛�
			int all = e - s + 1; // 瀹氫箟闇�瑕佹覆鏌撶殑甯ф暟鎬婚噺
			// 闇�瑕佸皢unitsNumber鍒嗘垚涓ょ被锛屽叾涓竴绫昏澶氭覆鏌�1甯х殑鐢婚潰銆�
			int between = all / unitsNumber; // 瀹氫箟姣忎釜鑺傜偣闇�瑕佹壙鎷呯殑浠诲姟閲�
			int remain = all % unitsNumber; // 璁＄畻鍑轰綑鏁�,鏍规嵁鏁板鐭ヨ瘑锛屼綑鏁扮殑涓暟涓洪渶瑕佸娓叉煋涓�甯х殑鑺傜偣鏁伴噺
			int NodesNumPerUnit=configurationService.findAllConfiguration().getNodesNumPerUnit();
			String fuWuListName=configurationService.findAllConfiguration().getFuWuListName();
			String sceneMem=configurationService.findAllConfiguration().getSceneMemory();
			SystemConfig config=new SystemConfig();
			//String renderingWorkDir=config.getRenderingWorkDir();
			for (int i = 0; i < unitsNumber; i++) {
				int temp;
				// 纭畾鎵ц鑺傜偣
				String stdout="";
				String newUnitId=UUIDGenerator.getUUID();
				if(systemConfig.getJobManageService().equals("openPBS")){	
					stdout = clusterManageService.submit(pbsFilePath);
				}else{
					//璁＄畻绁殑鎿嶄綔
					 SystemConfig systemConfig=new SystemConfig();
		    		String renderingWorkDir=systemConfig.getRenderingWorkDir();
	                    //鎸囦护鍦板潃鍚庢湡鏇存敼  
		    		String camraPath="/home/export/online1/systest/swsdu/xijiao/framedir2/";
		    		String renderInstruct="/home/export/online1/systest/swsdu/xijiao/mitsuba-0420/dist/mitsuba";
		    		String Cmd="cd /home/export/online1/systest/swsdu/xijiao/mitsuba-0420";//杩涘叆寮曟搸鐩綍
					String shenweiPbsCommand="";
						Date stime=new Date();//姝ゅ鍏堝叏閮ㄦ覆鏌擄紝鍚庨潰鏇存敼
							//涓存椂浣跨敤灏卞紩鎿庯紝鍚庨潰鏇存崲杩囨潵鏂扮殑闀滃ご缁勭粐缁撴瀯
						//shenweiPbsCommand="-q q_sw_share"+" -o /home/export/online1/systest/swsdu/xijiao/Outprint/"+newUnitId+".out"+" -b -share_size 7168 -n 31 "+ renderInstruct+" "+camraPath+" / |tee result-frame-n21";
						//stdout = clusterManageService.submit(shenweiPbsCommand);
						/*shenweiPbsCommand="-q q_sw_expr"+" -o /home/export/online1/systest/swsdu/xijiao/Outprint/"+newUnitId+".out"+" -b -share_size 7168 -n 31 "+ renderInstruct+" "+filePath+"/";
						PbsExecute pbs=new PbsExecute(Cmd+" && bsub "+shenweiPbsCommand);
					     stdout=pbs.executeCmd().trim();
					     log.info("return stdout is "+stdout);
					     if(stdout!=null&&stdout.length()>0)
					    	 stdout=stdout.substring(stdout.indexOf('<')+1,stdout.indexOf('>'));
						Date etime=new Date();
						log.info("WLBSUB"+ (etime.getTime()-stime.getTime()));*/
				}					
				log.info("Initializing Rendering Unit..."+stdout);	
				Unit unit=new Unit();
				unit.setId(newUnitId);
				unit.setPbsId(stdout);
				unit.setUnitStatus(UnitStatus.unavailable);
				unit.setUnitNodesNum(nodesNumPerUnit);
				unit.setIdleNumber(0);
				unitService.addUnit(unit);
				
				log.info("Rendering Unit initialized");
				
				// 杩涜浣滀笟鐨勫垝鍒嗘墽琛岀紪鍐�
				if (i < unitsNumber - remain) {
					// 鎵挎媴浠诲姟閲忎负between鐨勮妭鐐逛釜鏁颁负nu-remain
					temp = s + between - 1;// 瀹氫箟涓�涓复鏃剁粓鐐瑰彉閲�
				} else {
					temp = s + between;// 瀹氫箟涓�涓复鏃剁粓鐐瑰彉閲�
				}
				
				// 璁板綍浠诲姟淇℃伅
				Task task=new Task();
				task.setId(UUIDGenerator.getUUID());
				task.setFrameNumber(temp-s+1);
				task.setTaskProgress(0);
				task.setTaskStatus(TaskStatus.started);
				task.setStartTime(new Date());
				task.setJob(existedJob);
				task.setUnit(unit);
				taskDao.persist(task);
				
				for (int j = s; j < temp + 1; j++) {
					Frame frame=fs.get(j);
					frame.setFrameStatus(FrameStatus.notStart);
					frame.setFrameProgress(0);
					frame.setTask(task);
					frameDao.updateFrame(frame);
				}						
				// 杩涜鍙橀噺鐨勬洿鏂�
				s = temp + 1; // 姣忔鑺傜偣鍒嗗彂瀹屼互鍚庯紝杩涜璧峰鑺傜偣鐨勬洿鏂帮紱
			}
			/*鏇存柊浣滀笟鐘舵�佷俊鎭�*/
			existedJob.setStartTime(new Date());
			existedJob.setJobStatus(JobStatus.distributed);
			existedJob.setPaymentStatus(PaymentStatus.notPayment);
			jobDao.updateJob(existedJob);
			return true;
		}else{
			return false;
		}
	}

	
	
	
	
	
	@Override
	public List<Job> findJobs() {
		return jobDao.queryJobs();
	}
	
	
	@Override
	public double JobRenderPrice(String jodId){
		//int frameFinishNum;
		double RenderPrice;
		Job job;
		long mins=0;
		List<Frame> finishFrame;
			finishFrame=frameDao.queryFramesByJobId(jodId);
			if(finishFrame!=null &&finishFrame.size()>0){
				for(int j=0 ; j<finishFrame.size() ; j++){
					long temp=finishFrame.get(j).getEndTime().getTime() -finishFrame.get(j).getStartTime().getTime();//鐩稿樊姣鏁�
					long temp2 = temp % (1000 * 3600);
			        mins += temp2 / 1000 / 60;                    //鐩稿樊鍒嗛挓鏁�
				}
			}
			if(mins==0){
				RenderPrice=1*configurationDao.queryUnitPrice();
			}else{
				RenderPrice=mins*configurationDao.queryUnitPrice();
			}
			DecimalFormat df=new DecimalFormat(".##");//绮剧‘鍒板皬鏁扮偣鍚庝袱浣�
			String st=df.format(RenderPrice);
			RenderPrice= Double.parseDouble(st);
		return RenderPrice;
	}
	
	@Override
	public void JobPayment(String jobId){
		Job job=jobDao.queryJobsById(jobId);
		User user=job.getProject().getUser();
		if(user.getCardBalances()==null){
			user.setCardBalances(0.0);//榛樿璧嬪��
			userDao.updateUser(user);
		}
		if(job.getPaymentStatus()==null){
			job.setPaymentStatus(PaymentStatus.notPayment);
			//jobDao.updateJob(job);
		}		
		if(job.getPaymentStatus()!=PaymentStatus.hasPayment){
			if(job.getJobStatus()==JobStatus.finished){
				if(user.getCardBalances()<job.getRenderCost()){
					job.setPaymentStatus(PaymentStatus.notEnoughPayment);
				}
				else{
					user.setCardBalances(user.getCardBalances() - job.getRenderCost());//鏇存柊鐢ㄦ埛浣欓
					job.setPaymentStatus(PaymentStatus.hasPayment);
				}
			}
			jobDao.updateJob(job);
			userDao.updateUser(user);
		}	
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.SERIALIZABLE)
	public void unitBalaing(String existedJobId , String taskBalacingId){
		
	
	List<Task> alltasks =jobDao.queryJobsById(existedJobId).getTasks();
	ArrayList<Task> tasks = new ArrayList<Task>();
	for(int i =0; i<alltasks.size() ; i++){
		if(alltasks.get(i).getTaskStatus()==TaskStatus.started){
			tasks.add(alltasks.get(i));
		}
	}
	
	
	Task taskBalacing=taskDao.queryTasksById(taskBalacingId);
	Task task;
	int unitIdleNum=taskBalacing.getUnit().getIdleNumber();
	 Random rand = new Random();
	 int recycle;
	
	 if(tasks != null && tasks.size()>0 ){
		 if(tasks.size()>20){
			 recycle= 20;
		 }else{
			 recycle=tasks.size();
		 }
		 
		 for(int j =0 ; j<recycle ; j++){//寰幆灏忎簬5娆�
			 if(tasks.size()>20){
				 task=tasks.get(rand.nextInt(tasks.size()));
			 }else{
				 task=tasks.get(j);
			 }
			 if(!task.getId().equals(taskBalacing.getId())){
				 Unit unitForThisTask =task.getUnit();
				 if(task.getTaskStatus()==TaskStatus.started && unitForThisTask.getIdleNumber()==0){//task娌℃湁鍓╀綑甯у垯璋冨害鍒皌askBalacing
					 List<Frame> NotStartframesForThisTask=frameDao.queryByFrameStatusAndTaskId(FrameStatus.notStart, task.getId());
					 if(NotStartframesForThisTask.size()>0 && NotStartframesForThisTask!=null ){
						 int taskFrameNumber=task.getFrameNumber();
						 int taskBalacintFrameNumber = taskBalacing.getFrameNumber();
						 if (NotStartframesForThisTask.size() < unitIdleNum){
							 
							 for(int k =0 ; k<NotStartframesForThisTask.size(); k++){
								 NotStartframesForThisTask.get(k).setTask(taskBalacing);
								 frameDao.updateFrame(NotStartframesForThisTask.get(k));
							 }
																											 
							 task.setFrameNumber(taskFrameNumber-NotStartframesForThisTask.size());
							 log.info("1wanglei_task"+task.getId()+"  ");
							 
							taskBalacing.setFrameNumber(taskBalacintFrameNumber+NotStartframesForThisTask.size());
							 log.info("1wanglei_tasktaskBalacing"+taskBalacing.getId()+"  ");
							 taskDao.updateTask(task);
							 taskDao.updateTask(taskBalacing);
							 
							 log.info("task.getFrameNumber()");
							 log.info(task.getFrameNumber());
							 log.info("taskBalacing.getFrameNumber()");
							 log.info(taskBalacing.getFrameNumber());
							 break;
							 
						 }//end if
						 else{
								
							 for(int k =0 ; k<unitIdleNum; k++){
								 NotStartframesForThisTask.get(k).setTask(taskBalacing);
								 frameDao.updateFrame(NotStartframesForThisTask.get(k));
							 }
									
							 task.setFrameNumber(taskFrameNumber-unitIdleNum);
							 log.info("2wanglei_task"+task.getId()+"  ");
							
							 
							 taskBalacing.setFrameNumber(taskBalacintFrameNumber+unitIdleNum);
							 log.info("2wanglei_taskBalacing"+taskBalacing.getId()+"  ");
						
							 log.info("unitIdleNum");
							 log.info(unitIdleNum);
							
							 
							 taskDao.updateTask(task);
							 taskDao.updateTask(taskBalacing);
							 
							 
							 log.info(" task.getFrameNumber()");
							 log.info(task.getFrameNumber());
							 log.info("taskBalacing.getFrameNumber()");
							 log.info(taskBalacing.getFrameNumber());
							 
							 break;
							
							 
							 
						 }
						 
					 }
					 
					 
				 }
			 }
		 }
	 }
	}
	@Override
	public void taskadding(String existedJobId , String taskBalacingId){//浠诲姟杩藉姞
		Job existedJob = jobDao.queryJobsById(existedJobId);
		Task taskBalacing = taskDao.queryTasksById(taskBalacingId);
		List<Frame> frames=frameDao.queryByFrameStatusAndTaskId(FrameStatus.notStart, taskBalacing.getId());
		Unit existunit= taskBalacing.getUnit();
		int notStartFramesNum = 0;
		int unitIdleNum = existunit.getIdleNumber();
		
		 if (frames!=null && frames.size()>0){
			 notStartFramesNum = frames.size();		
		 }   
				
		
		ArrayList<String> frameNames = new ArrayList<String>();
		TaskDiscription td = new TaskDiscription();
	
	 	td.setTaskID(taskBalacing.getId());
		td.setCameraPath(existedJob.getFilePath());
		td.setMemorySize(10240);
		td.setPreRenderTag(existedJob.getPreRenderingTag());
		if(existedJob.getPreRenderingTag() == 1){
			td.setX_Resolution(existedJob.getxResolution());
			td.setY_Resolution(existedJob.getyResolution());
			td.setSampleRate(existedJob.getSampleRate());
		}
		if (unitIdleNum < notStartFramesNum+1 || unitIdleNum == notStartFramesNum){
			td.setFrameNumber(unitIdleNum);//(2015-05-06淇敼鍙戦�佷换鍔＄殑甯ф暟)
			for(int i=0;i<unitIdleNum;i++){
				frameNames.add(frames.get(i).getFrameName());
				frames.get(i).setFrameStatus(FrameStatus.unfinished);//瀹炴椂璺熸柊鏁版嵁搴�
				frames.get(i).setStartTime(new Date());
				frameDao.updateFrame(frames.get(i));
				
			}
			td.setFrameNames(frameNames);
			existunit.setIdleNumber(0);
			existunit.setUnitStatus(UnitStatus.busy);
		
			unitDao.updateUnit(existunit);
		}else{
			td.setFrameNumber(notStartFramesNum);//(2015-05-06淇敼鍙戦�佷换鍔＄殑甯ф暟)
			for(int i=0;i<notStartFramesNum;i++){
				frameNames.add(frames.get(i).getFrameName());
				frames.get(i).setFrameStatus(FrameStatus.unfinished);//瀹炴椂璺熸柊鏁版嵁搴�
				frames.get(i).setStartTime(new Date());
				frameDao.updateFrame(frames.get(i));
			}
			td.setFrameNames(frameNames);
			existunit.setIdleNumber(unitIdleNum-notStartFramesNum);
			unitDao.updateUnit(existunit);
		}
		String message = td.connectString();
		
		
		boolean isSucceed=Client.sendToC(message, existunit.getUnitMasterName(), 5169);//鍙戦�佷换鍔′俊鎭�
		if(!isSucceed){
			log.info("wangleiUnitsendfail"+existunit.getId());
		}
			
		
			
		
		
	}

	@Override
	public Job FindNotStartJob() {
		return jobDao.queryNotStartJob();

	}
	
	/*
	 * //状态转移算法 public ArrayList<Integer> statusRandom(int start,int end,int num) {
	 * //用来记录抽取值 ArrayList<Integer> temp = new ArrayList<Integer>();
	 * 
	 * int[] status = new int[end + 1]; for (int j = 0 ; j < num ; j ++) { int
	 * random = (int) (Math.random() * (end - start) + start); if (status[random] ==
	 * 0) { temp.add(random);
	 * 
	 * status[random] = random == end ? start : (random + 1); //
	 * 不可能有在levelStart之前的数字 //System.out.println("status" + status[random]); } else
	 * { // 状态转移 int index = random; do { index = status[index]; } while
	 * (status[index] != 0);
	 * 
	 * temp.add(index);
	 * 
	 * status[index] = index == end ? start : (index + 1); // 不可能有在levelStart之前的数字 }
	 * }
	 * 
	 * return temp; }
	 */
	
}


