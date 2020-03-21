package org.xjtu.framework.backstage.distribute;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xjtu.framework.backstage.initialize.MayaJobInitialize;
import org.xjtu.framework.backstage.initialize.RndrJobInitialize;
import org.xjtu.framework.core.base.constant.FrameStatus;
import org.xjtu.framework.core.base.constant.JobStatus;
import org.xjtu.framework.core.base.constant.TaskStatus;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.core.util.PbsExecute;
import org.xjtu.framework.core.util.StringUtil;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.modules.user.dao.JobDao;
import org.xjtu.framework.modules.user.service.*;
import org.xjtu.framework.modules.user.service.impl.JobServiceImpl;

import com.xj.framework.ssh.ShellLocal;

import javax.annotation.Resource;
import java.util.*;

public class JobDistribute {
	
	private static Log log = LogFactory.getLog(JobDistribute.class);
	
	private @Resource JobService jobService;
	private @Resource TaskService taskService;
	private @Resource FrameService frameService;
	private @Resource ClusterManageService clusterManageService;
	private @Resource ConfigurationService configurationService;
	
	private @Resource JobDao jobDao;
	
	private int nodesNumPerUnit;

	public boolean distributeJob(Job job) throws Exception {
		nodesNumPerUnit = configurationService.findAllConfiguration().getNodesNumPerUnit();
		List<Frame> fs=frameService.findNotStartFramesByJobId(job.getId());
		log.info("get frames is "+fs); 
		String renderEngineName=job.getRenderEngine().getName();
		System.out.println("renderEngineName is"+renderEngineName);
	    //different renderEngine
		if(renderEngineName.equals("RWing")||renderEngineName.equals("rUnit-hustdm")){
			int freeNodesNum=clusterManageService.getFreeNodesNum();
			int unitsNumber = job.getUnitsNumber();	
			int nodes=unitsNumber;
			unitsNumber=1;//这里设置定死，后面需要修改
			if(freeNodesNum<unitsNumber*nodesNumPerUnit){
				log.info("there is no enough unit avaliable"); 
				return false;
			}
			//褰撴浣滀笟涓烘殏鍋滃悗缁х画鐨勪綔涓�
			if(fs!=null&&fs.size()!=0){
				log.info("鏆傚仠鐨勭ク缁х画杩愯");
				return false;
				//return jobService.continueRunitJobtoTask(job, unitsNumber, nodesNumPerUnit, fs,renderEngineName);
			}else{
				log.info("begin distributeJob first"); 
				return jobService.distributeRunitJob(job, unitsNumber, nodesNumPerUnit,renderEngineName,nodes);
			}
		}
		else if(job.getRenderEngine().getName().equals("rndr")){	
			log.info("renderman is starting");
			return disRndrJobtoTask(job);
		}
		else if(job.getRenderEngine().getName().equals("maya")){
			return disMayaJobtoTask(job);
		}
		else{
			return false;
		}

	}
	
	//distribute PreRenderJob
	public boolean distributePreRenderJob(String filePath,String jobName,String jobId,int SampleRate,int height,int width) throws Exception {
		String stdout = "";
		
		//get fuWuLIstName
		String fuWuListName = configurationService.findAllConfiguration().getFuWuListName();
		
		//set nodes=82
		int nodes = 82;
		
		
		//预渲染抽取帧值开始
		Job existedJob = jobDao.queryJobsById(jobId);
		
		int frameNums = existedJob.getFrameNumbers();
		String frameRange = existedJob.getFrameRange();
		
		
		//预渲染帧值
        ArrayList<Integer> frameToPreRender = new ArrayList<Integer>();        
        
        int preFrameNum = (int) Math.rint(frameNums * SampleRate / 100);
        
        
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
				}else{
						Integer end = Integer.parseInt(temp[1]);
						Integer begin = Integer.parseInt(temp[0]);
						for(int z = begin;z < end + 1;z++){
							frameToRender[k] = z;
							k++;					
						}
				}
			}
		}
        
		//Thys 新预渲染
		//预渲染抽帧参数
		int level = 0;
		int levelNum = 1;
		
		//用来存每层帧数
		ArrayList<Integer> frameNumToPerLevel = new ArrayList<Integer>();
		//用来记录每层起始和终止位置S--start,E--end
		ArrayList<Integer> perLevelSE = new ArrayList<Integer>();
		perLevelSE.add(frameToRender[0]);
		//记录未分配余数时，已分配的帧数
		int frameNumNoRemainder = 0;
		//设置层最大值,根据渲染帧数数量级来确定
		int levelNumMax;
		int levelNumMaxK;
		levelNumMax = 1;
		levelNumMaxK = 10;
		while (frameNums / levelNumMaxK != 0) {
			levelNumMax *= 10;
			levelNumMaxK *= 10;
		}
		
		//先确定预渲染层数level，和每层分配的初始帧数（不处理余数）
		for (int j = 1;j < frameToRender.length;j ++) {			
			if (frameToRender[j] == (frameToRender[j - 1] + 1)) {				
				if (levelNum < (levelNumMax / 10)) {					
					levelNum ++;					
				} else {
					perLevelSE.add(frameToRender[j - 1]);
					
					int preFrameTemp = (int) ((double) levelNum / frameNums * preFrameNum);
					frameNumToPerLevel.add(preFrameTemp);
					frameNumNoRemainder += preFrameTemp;
					
					level ++;
					perLevelSE.add(frameToRender[j]);
					levelNum = 1;					
				}
				
				if (j == frameToRender.length - 1) {
					perLevelSE.add(frameToRender[j]);
					
					int preFrameTemp = (int) ((double) levelNum / frameNums * preFrameNum);
					frameNumToPerLevel.add(preFrameTemp);
					frameNumNoRemainder += preFrameTemp;
				}
			} else {
				perLevelSE.add(frameToRender[j - 1]);
				
				int preFrameTemp = (int) ((double) levelNum / frameNums * preFrameNum);
				frameNumToPerLevel.add(preFrameTemp);
				frameNumNoRemainder += preFrameTemp;
				
				level ++;
				perLevelSE.add(frameToRender[j]);
				levelNum = 1;				
			}
		}
		
		//对抽取预渲染总帧数预处理,提高精度
		int preFrameNumRemainder = preFrameNum % (level + 1);
		//对于预渲染帧数与层数相差不多preFrameNumRemainder得改为差值
		if(Math.abs(preFrameNum - level) <= 5) {
			preFrameNumRemainder = Math.abs(preFrameNum - frameNumNoRemainder);
		}
		
		//System.out.println("preFrameNumRemainder " + preFrameNumRemainder);
		
		//确定每层所取帧数
		//把余数先放奇数层帧数，多出来的再放偶数层，直到分配完
		int frameNumToPerLevelFlag = 0;
		while (preFrameNumRemainder > 0) {				
			if(frameNumToPerLevelFlag <= level) {
				frameNumToPerLevel.set(frameNumToPerLevelFlag, frameNumToPerLevel.get(frameNumToPerLevelFlag) + 1);
			} else {
				//如果是奇数层，就减去level + 1，注意数组从0起
				if (level % 2 == 0) {
					frameNumToPerLevel.set(frameNumToPerLevelFlag - level - 1, frameNumToPerLevel.get(frameNumToPerLevelFlag - level - 1) + 1);
				} else {
					frameNumToPerLevel.set(frameNumToPerLevelFlag - level, frameNumToPerLevel.get(frameNumToPerLevelFlag - level) + 1);
				}
			}
			
			frameNumToPerLevelFlag += 2;
			preFrameNumRemainder --;
		}

		
		
		//System.out.println("frameNumToPerLevel " + frameNumToPerLevel);
		
		
		//System.out.println("perLevelSE " + perLevelSE);
		
		//调用状态转移的方法，保存每层要预渲染的帧数
		int levelSEFlag = -1;
		for (int j = 0;j <= level;j ++) {
			frameToPreRender.addAll(statusRandom(perLevelSE.get(++ levelSEFlag),perLevelSE.get(++ levelSEFlag),frameNumToPerLevel.get(j)));
		}
		
		//抽取预渲染帧值结束
		
		//log.info("预渲染长度：" + frameToPreRender.size());
    	
    	//更新frameInfo.txt文件
    	//清空frameInfo.txt，填写文件总数
    	String clearCmd = "cat /dev/null > " + filePath + "/frameInfo.txt";
		String appendCmd = "echo '" + frameToPreRender.size() + "' >> " + filePath + "/frameInfo.txt";
		ShellLocal shellToInfo = new ShellLocal();
		shellToInfo.setCmd(clearCmd + " && " + appendCmd);
		shellToInfo.executeCmd();
		
        //复制需要预渲染的xml文件            					
    	for (int j = 0 ; j < frameToPreRender.size() ; j ++) {
    		String cmdToCopy = "cp " + filePath + "/frame" + frameToPreRender.get(j) + ".xml " + filePath + "/frame" + frameToPreRender.get(j) + "_pre.xml";
			ShellLocal shellToCopy = new ShellLocal(cmdToCopy);
			shellToCopy.executeCmd();
			
			//更改预渲染参数
        	ShellLocal shellToChange = new ShellLocal();
        	String sampleCountCmd = "sed -i 's;<integer name=\"sampleCount\" value=\".*\"/>;<integer name=\"sampleCount\" value=\"100\"/>;g' "+ filePath +"/frame"+ frameToPreRender.get(j) +"_pre.xml";
			String heightCmd = "sed -i 's;<integer name=\"height\" value=\".*\"/>;<integer name=\"height\" value=\""+ height +"\"/>;g' "+ filePath +"/frame"+ frameToPreRender.get(j) +"_pre.xml";
			String widthCmd = "sed -i 's;<integer name=\"width\" value=\".*\"/>;<integer name=\"width\" value=\""+ width +"\"/>;g' "+ filePath +"/frame"+ frameToPreRender.get(j) +"_pre.xml";
			shellToChange.setCmd(sampleCountCmd + " && " + heightCmd + " && " + widthCmd);
			shellToChange.executeCmd();
        	//更改预渲染参数结束
			
			//添加xml文件名到frameInfo
			String cmdToAddXml = "echo 'frame" + frameToPreRender.get(j) + "_pre.xml' >> " + filePath + "/frameInfo.txt";
			ShellLocal shellToAddxml = new ShellLocal(cmdToAddXml);
			shellToAddxml.executeCmd();
			
			log.info("frameInfo添加的：frame" + frameToPreRender.get(j) + "_pre.xml");
    	}                            	
    	//复制结束
    	
    	//更新frameInfo.txt文件结束
    	
    	//开始预渲染
    	String renderInstruct="/home/export/online1/systest/swsdu/xijiao/RWing-demo/dist/PreRWing";
		String cmdCD = "cd /home/export/online1/systest/swsdu/xijiao/RWing-demo";
		String shenweiPbsCommand="";
		
		shenweiPbsCommand="-cross -q " + fuWuListName + " -b -o /home/export/online1/systest/swsdu/xijiao/Outprint/" + jobName + "_pre.out -J " + jobName + "_pre -n " + nodes + " -np 1 -sw3runarg \"-a 1\"  -host_stack 2600 -cross_size 20000 " + renderInstruct + " " + filePath + "/";
		//bsub            -cross -q q_sw_share            -b -o deskroom82                                                                               -J deskroom-Pre    -n 82           -np 1 -sw3runarg "-a 1"  -host_stack 2600 -cross_size 20000 ./dist/PreRWing  /home/export/online1/systest/swsdu/xijiao/Users/xjtu/deskroom120/	
		PbsExecute  pbs = new PbsExecute(cmdCD + " && bsub " + shenweiPbsCommand);
		stdout = pbs.executeCmd().trim();
		
		log.info("pre stdout is " + stdout);
		log.info("pre bsub comand is " + cmdCD + " && bsub " + shenweiPbsCommand);
		
		if(stdout != null && stdout.length() > 0)
    	stdout = stdout.substring(stdout.indexOf('<')+1,stdout.indexOf('>'));
    	//预渲染结束
		
		return true;
		

	}
	
	public boolean disRndrJobtoTask(Job job) throws Exception{

		log.info("Begin to distribute job to task...");
		log.info("first,get the jobinfo");

		
		/*鍒濆鍖栧彉閲�*/
		int nodesNumber = job.getNodesNumber();
		String filePath = job.getFilePath();
		int preRenderTag = job.getPreRenderingTag();
		int frameNums = job.getFrameNumbers();
		String frameRange = job.getFrameRange();		
		
		/*鏌ョ湅鑺傜偣鏄惁澶熺敤*/		
		int freeNodesNum=clusterManageService.getFreeNodesNum();
		log.info("my test"+ nodesNumber+" freeNodesNum:" +freeNodesNum);
		if(freeNodesNum<nodesNumber){
			log.info("there is no enough nodes avaliable"); 
			return false;
		}
		
		/*淇敼鎴愬懡浠ゆ柟寮�*/
		
		filePath=filePath.substring(0,filePath.lastIndexOf("/"));
		
		
		String cmd="cd "+filePath+" && ls *.xml";
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		result=result.trim();
		
		String[] filenames=result.split("\\n");
		
		Map<Integer,String> mapScenePath=new HashMap<Integer,String>();
		for(int i=0;i<filenames.length;i++){
			String[] frameNo=filenames[i].split("\\.");
			Integer key;
			try{
				
				String frameID=StringUtil.getNumb(frameNo[frameNo.length-2]);
				key=Integer.parseInt(frameID);
				
			}catch(NumberFormatException e){
				continue;
			}
			mapScenePath.put(key, filenames[i]);
		}
		
		
	   log.info("get frameScenceMap is"+mapScenePath);
		
		/*灏嗚娓叉煋鐨勫抚鍙锋斁鍏ユ暟缁勪腑*/
		int[] frameToRender=new int[frameNums];
		int k=0;
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
		
		
		log.info("start distributing...");
		
		// 闇�瑕佸皢unitsNumber鍒嗘垚涓ょ被锛屽叾涓竴绫昏澶氭覆鏌�1甯х殑鐢婚潰銆�
		int between = frameNums / nodesNumber; // 瀹氫箟姣忎釜鑺傜偣闇�瑕佹壙鎷呯殑浠诲姟閲�
		int remain = frameNums % nodesNumber; // 璁＄畻鍑轰綑鏁�,鏍规嵁鏁板鐭ヨ瘑锛屼綑鏁扮殑涓暟涓洪渶瑕佸娓叉煋涓�甯х殑鑺傜偣鏁伴噺
				
		int s=0;
		int e=frameNums;
		
		for (int i = 0; i < nodesNumber; i++) {

			int temp;
			// 杩涜浣滀笟鐨勫垝鍒嗘墽琛岀紪鍐�
			if (i < nodesNumber - remain) {
				// 鎵挎媴浠诲姟閲忎负between鐨勮妭鐐逛釜鏁颁负nu-remain
				temp = s + between - 1;// 瀹氫箟涓�涓复鏃剁粓鐐瑰彉閲�
			} else {
				temp = s + between;// 瀹氫箟涓�涓复鏃剁粓鐐瑰彉閲�
			}
			log.info("temp begin is "+temp);

			ArrayList<Frame> frames = new ArrayList<Frame>();
			
			for (int j = s; j < temp + 1; j++) {
				Frame frame = new Frame();
				frame.setId(UUIDGenerator.getUUID());
				frame.setFrameName(mapScenePath.get(frameToRender[j]));
				frame.setFrameStatus(FrameStatus.unfinished);
				frame.setStartTime(new Date());
				frame.setFrameProgress(0);
				frames.add(frame);
			}
			//RndrJobInitialize ri=new RndrJobInitialize(frames,job.getFilePath());//淇敼鍚�
			log.info("renderman frame  length is "+frames.size());
			RndrJobInitialize ri=new RndrJobInitialize(frames,filePath);
			ri.dobegin();
			// 璁板綍浠诲姟淇℃伅
			Task task=new Task();
			task.setId(UUIDGenerator.getUUID());
			task.setFrameNumber(temp-s+1);
			task.setTaskProgress(0);
			task.setTaskStatus(TaskStatus.started);
			task.setStartTime(new Date());
			task.setJob(job);
			task.setUnit(ri.getUnit());
			taskService.addTask(task);
			
			
			
			for(int j=0;j<frames.size();j++){
				frames.get(j).setTask(task);
				frameService.addFrame(frames.get(j));
			}
									
			// 杩涜鍙橀噺鐨勬洿鏂�
			s = temp + 1; // 姣忔鑺傜偣鍒嗗彂瀹屼互鍚庯紝杩涜璧峰鑺傜偣鐨勬洿鏂帮紱
			log.info("temp is "+temp);
			
		}
		
		
		
		
		
		log.info("job is ditributed over...");
		
		
		
		
		
		
		/*鏇存柊浣滀笟鐘舵�佷俊鎭�*/
		job.setStartTime(new Date());
		job.setJobStatus(JobStatus.distributed);
		jobService.updateJobInfo(job);
		
		return true;

	}
	public boolean disMayaJobtoTask(Job job) throws Exception{

		log.info("Begin to distribute job to task...");
		log.info("first,get the jobinfo");

		
		/*鍒濆鍖栧彉閲�*/
		int nodesNumber = job.getNodesNumber();
		String filePath = job.getFilePath();
		int preRenderTag = job.getPreRenderingTag();
		int frameNums = job.getFrameNumbers();
		String frameRange = job.getFrameRange();
		
		/*淇敼鎴愬懡浠ゆ柟寮�*/
		String cmd="cd "+filePath+" && ls *.mb";
		PbsExecute pbs=new PbsExecute(cmd);
		String result=pbs.executeCmd();
		result=result.trim();
		
		String[] filenames=result.split("\\n");
		
		String mayafile = null;
		if(filenames!=null&&filenames.length>0){
			mayafile=filenames[0];
		}
					
		/*鏌ョ湅鑺傜偣鏄惁澶熺敤*/		
		int freeNodesNum=clusterManageService.getFreeNodesNum();
		log.info("my test"+ nodesNumber+" freeNodesNum:" +freeNodesNum);
		if(freeNodesNum<nodesNumber){
			log.info("there is no enough nodes avaliable"); 
			return false;
		}		
		
		/*灏嗚娓叉煋鐨勫抚鍙锋斁鍏ユ暟缁勪腑*/
		int[] frameToRender=new int[frameNums];
		int k=0;
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
		
		log.info("start distributing...");
		
		// 闇�瑕佸皢unitsNumber鍒嗘垚涓ょ被锛屽叾涓竴绫昏澶氭覆鏌�1甯х殑鐢婚潰銆�
		int between = frameNums / nodesNumber; // 瀹氫箟姣忎釜鑺傜偣闇�瑕佹壙鎷呯殑浠诲姟閲�
		int remain = frameNums % nodesNumber; // 璁＄畻鍑轰綑鏁�,鏍规嵁鏁板鐭ヨ瘑锛屼綑鏁扮殑涓暟涓洪渶瑕佸娓叉煋涓�甯х殑鑺傜偣鏁伴噺
				
		int s=0;
		int e=frameNums;
		for (int i = 0; i < nodesNumber; i++) {

			int temp;

			// 杩涜浣滀笟鐨勫垝鍒嗘墽琛岀紪鍐�
			if (i < nodesNumber - remain) {
				// 鎵挎媴浠诲姟閲忎负between鐨勮妭鐐逛釜鏁颁负nu-remain
				temp = s + between - 1;// 瀹氫箟涓�涓复鏃剁粓鐐瑰彉閲�
			} else {
				temp = s + between;// 瀹氫箟涓�涓复鏃剁粓鐐瑰彉閲�
			}

			ArrayList<Frame> frames = new ArrayList<Frame>();
			
			for (int j = s; j < temp + 1; j++) {

				Frame frame = new Frame();
				frame.setId(UUIDGenerator.getUUID());
				
				frame.setFrameName(""+frameToRender[j]);
				
				frame.setFrameStatus(FrameStatus.unfinished);
				frame.setStartTime(new Date());
				frame.setFrameProgress(0);
				
				frames.add(frame);

			}
			
			MayaJobInitialize ma=new MayaJobInitialize(frames,job.getFilePath(),mayafile);
			ma.dobegin();
			
			// 璁板綍浠诲姟淇℃伅
			Task task=new Task();
			task.setId(UUIDGenerator.getUUID());
			task.setFrameNumber(temp-s+1);
			task.setTaskProgress(0);
			task.setTaskStatus(TaskStatus.started);
			task.setStartTime(new Date());
			task.setJob(job);
			task.setUnit(ma.getUnit());
			
			taskService.addTask(task);
			
			for(int j=0;j<frames.size();j++){
				frames.get(j).setTask(task);
				frameService.addFrame(frames.get(j));
			}
									
			// 杩涜鍙橀噺鐨勬洿鏂�
			s = temp + 1; // 姣忔鑺傜偣鍒嗗彂瀹屼互鍚庯紝杩涜璧峰鑺傜偣鐨勬洿鏂帮紱
		}
		log.info("job is ditributed over...");
		
		/*鏇存柊浣滀笟鐘舵�佷俊鎭�*/
		job.setStartTime(new Date());
		job.setJobStatus(JobStatus.distributed);
		jobService.updateJobInfo(job);
		
		return true;

	}
	
	//状态转移算法
	public ArrayList<Integer> statusRandom(int start,int end,int num) {
	//用来记录抽取值
	ArrayList<Integer> temp = new ArrayList<Integer>();
	
	int[] status = new int[end + 1];
	for (int j = 0 ; j < num ; j ++) {
		int random = (int) (Math.random() * (end - start) + start);
		if (status[random] == 0) {
			temp.add(random);
			
			status[random] = random == end ? start : (random + 1); // 不可能有在levelStart之前的数字 
			//System.out.println("status" + status[random]);
		} else { 
			// 状态转移 
			int index = random; 
			do { 
				index = status[index];
			} while (status[index] != 0);
			
			temp.add(index);
		     
			status[index] = index == end ? start : (index + 1); // 不可能有在levelStart之前的数字 
		} 
	}
	
	return temp;
}

}
