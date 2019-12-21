package org.xjtu.framework.backstage.distribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
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
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.ConfigurationService;
import org.xjtu.framework.modules.user.service.FrameService;
import org.xjtu.framework.modules.user.service.JobService;
import org.xjtu.framework.modules.user.service.TaskService;

public class JobDistribute {
	
	private static Log log = LogFactory.getLog(JobDistribute.class);
	
	private @Resource JobService jobService;
	private @Resource TaskService taskService;
	private @Resource FrameService frameService;
	private @Resource ClusterManageService clusterManageService;
	private @Resource ConfigurationService configurationService;
	
	private int nodesNumPerUnit;

	public boolean distributeJob(Job job) throws Exception {
		nodesNumPerUnit = configurationService.findAllConfiguration().getNodesNumPerUnit();
		List<Frame> fs=frameService.findNotStartFramesByJobId(job.getId());
		log.info("get frames is "+fs); 
		String renderEngineName=job.getRenderEngine().getName();
		System.out.println("renderEngineName is"+renderEngineName);
	    //different renderEngine
		if(renderEngineName.equals("RWing")||renderEngineName.equals("rUnit-hustdm")){//绗竴绉嶆覆鏌撳紩鎿�
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

}
