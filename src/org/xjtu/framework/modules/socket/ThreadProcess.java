package org.xjtu.framework.modules.socket;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.core.base.constant.FrameStatus;
import org.xjtu.framework.core.base.constant.UnitStatus;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.modules.protocol.ProgressMessage;
import org.xjtu.framework.modules.protocol.TaskDiscription;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.FrameService;
import org.xjtu.framework.modules.user.service.TaskService;
import org.xjtu.framework.modules.user.service.UnitService;

public class ThreadProcess implements Runnable {
	
	private static Log log = LogFactory.getLog(ThreadProcess.class);
	
	private Socket client;
	private InputStream inputStream;
	private OutputStream outputStream;
	private BufferedReader inBufferedReader; 
	private PrintWriter outPrintWriter;
	private static int count=0;
	private ServletContext context = null;

	public ThreadProcess(Socket aClient, ServletContext context) {
		client=aClient;
		log.info("thread start-------------------------------->");
		try {
			inputStream=client.getInputStream();
			inBufferedReader=new BufferedReader(new InputStreamReader(inputStream));
			outputStream=client.getOutputStream();
			outPrintWriter=new PrintWriter(outputStream);
			this.context = context;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

			
	//server端开启的一个socket通信处理线程
	@Override
	public void run() {

		String str="";
		try {
			try {
				
				log.info("thread Run  start-------------------------------->");
				
				outPrintWriter.print("#helloClient|end");
				outPrintWriter.flush();
				
				int ch;
				while ((ch=inBufferedReader.read())!=-1) {//这里出错
	
					str=str+(char)ch;					

					if (str.endsWith("|end")) {
						
						outPrintWriter.print("#serverProgressing|end");
						outPrintWriter.flush();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				/*inputStream.close();
				outputStream.close();
				client.close();*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("get client's ster -------------"+str);
		
		if(str!=null)
		{
				
			if (str.startsWith("#initMessage")) {
				
				System.out.println("ining Message\\\\\\\\\\\\\\\\\\");
				String ipAddr=client.getInetAddress().getHostAddress();
				ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);				
				UnitService unitService=(UnitService)ctx.getBean("unitService");
				TaskService taskService=(TaskService)ctx.getBean("taskService");
				FrameService frameService=(FrameService)ctx.getBean("frameService");
				ClusterManageService clusterManageService=(ClusterManageService)ctx.getBean("clusterManageService");
				log.info("WLinitMessageL");
				String pbsId=clusterManageService.getJobnameByIp(ipAddr);
				log.info("WLpbsId "+pbsId);
				Unit unit=unitService.findUnitsByPbsId(pbsId);
				log.info("WLpbsunit"+unit.getId());
				
				while(unit==null){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					unit=unitService.findUnitsByPbsId(pbsId);
				}
				log.info("WLtaska");
				
				List<Task> tasks=taskService.findTasksByUnitId(unit.getId());
				
				if(tasks==null || tasks.size()==0){
					return;
				}
				Task task=tasks.get(0);
				Job existedJob=task.getJob();
				List<Frame> frames=frameService.findFramesByTaskId(task.getId());
			/*	
			 * 2016-01
			 * ArrayList<Frame> distributeFrames = new ArrayList<Frame>();
				Frame frame ;
		*/
				if(frames==null || frames.size()==0){
					return;
				}
				
				/*2016-01
				 * int unitNum = unit.getUnitNodesNum();
				int taskDiscriptionFrameNumber;//每个节点渲染四帧以上就先发送task一半的帧，否则先发送（unitNum-1）的帧
				
				if(frames.size()/(unitNum-1) > 3 ){
					taskDiscriptionFrameNumber = frames.size()/2;
				}else {
					if(frames.size() < unitNum-1)
						taskDiscriptionFrameNumber=frames.size();
					else{
						taskDiscriptionFrameNumber= unitNum-1;
					}
			
				}*/
				
				
				TaskDiscription td = new TaskDiscription();					
				
				td.setTaskID(tasks.get(0).getId());
				td.setCameraPath(existedJob.getFilePath());
				td.setFrameNumber(frames.size());
				td.setMemorySize(10240);
				td.setPreRenderTag(existedJob.getPreRenderingTag());
				if(existedJob.getPreRenderingTag() == 1){
					td.setX_Resolution(existedJob.getxResolution());
					td.setY_Resolution(existedJob.getyResolution());
					td.setSampleRate(existedJob.getSampleRate());
				}
				
				ArrayList<String> frameNames = new ArrayList<String>();
				
				for(int i=0;i<frames.size();i++){
					frameNames.add(frames.get(i).getFrameName());
					//distributeFrames.add(frames.get(i));
				}
				
				td.setFrameNames(frameNames);
				
				String message = td.connectString();
				
				boolean isSucceed=Client.sendToC(message, ipAddr, 5169);
				
				
				
				try {
					//System.out.println("get initMessage1");
					unit.setUnitNodesInfo(clusterManageService.getNodeString(pbsId));
				} catch (Exception e) {
					//System.out.println("get initMessage2");
					e.printStackTrace();
				}
				Frame frame;
				for(int j=0;j<frames.size();j++){//(2015-05-06修改发送任务的帧 )
					frame = frames.get(j);
					frame.setFrameStatus(FrameStatus.unfinished);
					frame.setStartTime(new Date());
					frameService.updateFrameInfo(frame);		
				}
				unit.setUnitMasterName(ipAddr);
				unit.setUnitStatus(UnitStatus.busy);
				unitService.updateUnitInfo(unit);	
				
					
			} else if (str.startsWith("#taskstatus")) {

				ProgressMessage progressMessage=new ProgressMessage(str);//处理进度信息
				
				ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);				
				FrameService frameService=(FrameService)ctx.getBean("frameService");
				frameService.updateFrameInfoByProgressMessage(progressMessage);
							
															
			} else {
				System.out.println("unknown message type");
			}
			
		}
	}

}
