package org.xjtu.framework.backstage.schedule;

import com.xj.framework.ssh.ShellLocal;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.modules.user.service.impl.JobServiceImpl;
import org.xjtu.framework.core.util.PbsExecute;
import org.xjtu.framework.modules.user.service.ConfigurationService;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import static org.xjtu.framework.backstage.schedule.JobSchedule.*;

import java.util.ArrayList;

public class PreRenderwing  implements Runnable {


    private static final Logger log = Logger.getLogger(PreRenderwing.class);
    private ServletContext context = null;
    private static boolean isRunning = false;
    
    private @Resource ConfigurationService configurationService;
//    private int flag=0;

    public PreRenderwing(ServletContext context){
        this.context = context;
    }

    @Override
    public void run() {
                if (jobs_render.size()>0){
                    log.info("说明当前有渲染任务被提交");

                    if (!isRunning) {
                        isRunning = true;
                        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);

                        String filePath = jobs_render.get(0).getFilePath();
                        String FrameRange = jobs_render.get(0).getFrameRange();
                        String RenderId = jobs_render.get(0).getId();
                        String jobName=jobs_render.get(0).getCameraName();
                        int nodes = jobs_render.get(0).getUnitsNumber();
                        
                        String fuWuListName = configurationService.findAllConfiguration().getFuWuListName();
                        
                        String stdout = "";

                        log.info("渲染目录文件的地址filePath="+filePath+"渲染帧的范围："+FrameRange+"job_render的size为="+jobs_render.size());
                        log.info("job_render的size为=\""+jobs_render.size());

                        for(int i=0;i<jobs_render.size();i++){
                            log.info("jobs_render["+i+"]"+"的渲染进程名字是："+jobs_render.get(i).getCameraName());
                        }
                        
                        //预渲染帧值
                        ArrayList<Integer> frameToPreRender = new ArrayList<Integer>();
                        frameToPreRender = JobServiceImpl.frameToPreRender;
                        
//                        //获取最大帧值
//                        String cmdToMaxFrame = "cd "+ filePath +" && cat frameMax.txt";
//    					ShellLocal shellToMaxFrame = new ShellLocal(cmdToMaxFrame);
//    					String result = shellToMaxFrame.executeCmd();
//    					result = result.trim();
//    					int frameMax = Integer.parseInt(result) + 1;
                        
    					//获取预渲染图片长宽
    					int height = jobs_render.get(0).getxResolution();
    					int width = jobs_render.get(0).getyResolution();
                        
                        //获取帧范围中最后一个也是最大的一个帧的数字。
                        String FrameMax=FrameRange.substring(FrameRange.length()-1);
                        String FrameName="frame"+FrameMax+".xml";
                        log.info("最大的帧的名字是："+FrameName);

                        ShellLocal shell=new ShellLocal( "cd " + filePath + " && ls *.xml");
                        String result=shell.executeCmd();
                        result=result.trim();

                        log.info("result的内容是："+result);

                        String[]filesName=result.split("\n");

                        for(int i = 0;i < filesName.length;i ++){
                            log.info("当前的filesName是=" + filesName[i]);
                            if (filesName[i].equals(FrameName)){
                            	
                            	//更新frameInfo.txt文件
                            	//清空frameInfo.txt，填写文件总数
                            	String clearCmd = "cat /dev/null > " + filePath + "/frameInfo.txt";
        						String appendCmd = "echo '" + frameToPreRender.size() + "' > " + filePath + "/frameInfo.txt";
        						ShellLocal shellToInfo = new ShellLocal();
        		    			shellToInfo.setCmd(clearCmd + " && " + appendCmd);
        						shellToInfo.executeCmd();
                            	
                                //复制需要预渲染的xml文件            					
                            	for (int j = 0 ; j < frameToPreRender.size() ; j ++) {
                            		String cmdToCopy = "cp " + filePath + " frame" + frameToPreRender.get(j) + ".xml " + filePath + "/frame" + frameToPreRender.get(j) + "_pre.xml";
                					ShellLocal shellToCopy = new ShellLocal(cmdToCopy);
                					shellToCopy.executeCmd();
                					
                					//更改预渲染参数
                                	ShellLocal shellToChange = new ShellLocal();
                                	String sampleCountCmd = "sed -i 's;<integer name=\"sampleCount\" value=\".*\"/>;<integer name=\"sampleCount\" value=\"100\"/>;g' "+ filePath +"/frame"+ frameToPreRender.get(j) +"_pre.xml";
                					String heightCmd = "sed -i 's;<integer name=\"height\" value=\".*\"/>;<integer name=\"height\" value=\""+ height +"\"/>;g' "+ filePath +"/frame"+ frameToPreRender.get(j) +"_pre.xml";
                					String widthCmd = "sed -i 's;<integer name=\"width\" value=\".*\"/>;<integer name=\"width\" value=\""+ width +"\"/>;g' "+ filePath +"/frame"+ frameToPreRender.get(j) +"_pre.xml";
                					shellToChange.setCmd(sampleCountCmd + " && " + heightCmd + " && " + widthCmd);
                                	//更改预渲染参数结束
                					
                					//添加xml文件名到frameInfo
                					String cmdToAddXml = "echo 'frame" + frameToPreRender.get(j) + "_pre.xml' > " + filePath + "/frameInfo.txt";
                					ShellLocal shellToAddxml = new ShellLocal(cmdToAddXml);
                					shellToAddxml.executeCmd();
                            	}                            	
                            	//复制结束
                            	
                            	//更新frameInfo.txt文件结束
                            	
                            	//开始预渲染
                            	String renderInstruct="/home/export/online1/systest/swsdu/xijiao/RWing-demo/dist/PreRWing";
            		    		String cmdCD = "cd /home/export/online1/systest/swsdu/xijiao/RWing-demo";
            		    		String shenweiPbsCommand="";
            		    		
            		    		shenweiPbsCommand="-cross -q " + fuWuListName + " -b -o /home/export/online1/systest/swsdu/xijiao/Outprint/" + jobName + "_pre.out -J " + jobName + "_pre -n " + nodes + " -np 1 -sw3runarg \"-a 1\"  -host_stack 2600 -cross_size 20000 " + renderInstruct + " " + filePath;
            		    		//bsub            -cross -q q_sw_share            -b -o deskroom82                                                                               -J deskroom-Pre    -n 82           -np 1 -sw3runarg "-a 1"  -host_stack 2600 -cross_size 20000 ./dist/PreRWing  /home/export/online1/systest/swsdu/xijiao/Users/xjtu/deskroom120/	
            		    		PbsExecute  pbs=new PbsExecute(cmdCD + " && bsub " + shenweiPbsCommand);
								stdout = pbs.executeCmd().trim();
								log.info("return stdout is " + stdout);
								log.info("bsub comand is " + shenweiPbsCommand);
								if(stdout != null && stdout.length() > 0)
       					    	stdout = stdout.substring(stdout.indexOf('<')+1,stdout.indexOf('>'));
                            	//预渲染结束
                            	
                                log.info(filesName[i]+"和"+FrameName+" 相等哦"+"job_render的size为="+jobs_render.size());
                                jobs_render.remove(0);
                                log.info("移除index0后，job_render的size为="+jobs_render.size());
                                break;
                            }else{
                                log.info(filesName[i]+"和"+FrameName+" 不相等哦");
                            }
                            //log.info("存在文件--》"+filesNamelesName[i]+"  \n"+"  ");

                        }
                        
                        
                        isRunning = false;
                    }else{
                    log.info("last scheduling not finished!");
                }
                }else{
                        log.info("当前无渲染进程！！！！！！！！！！！！！！！！！！");
                    }
    }
}
