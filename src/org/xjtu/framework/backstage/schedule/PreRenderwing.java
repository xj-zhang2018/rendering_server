package org.xjtu.framework.backstage.schedule;

import com.xj.framework.ssh.ShellLocal;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xjtu.framework.modules.user.service.impl.JobServiceImpl;
import org.xjtu.framework.backstage.distribute.JobDistribute;
import org.xjtu.framework.core.util.PbsExecute;
import org.xjtu.framework.modules.user.service.*;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import static org.xjtu.framework.backstage.schedule.JobSchedule.*;

import java.util.ArrayList;

public class PreRenderwing  implements Runnable {


    private static final Logger log = Logger.getLogger(PreRenderwing.class);
    private ServletContext context = null;
    private static boolean isRunning = false;
    
    
    
    //private ConfigurationService configurationService;
//    private int flag=0;

    public PreRenderwing(ServletContext context){
        this.context = context;
    }

    @Override
    public void run() {
        if (jobs_render.size()>0){
            log.info("说明当前有渲染任务被提交");

            if (!isRunning) {
            	log.info("预渲染任务开始");
                isRunning = true;
                ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
    			JobDistribute jobDistribute=(JobDistribute)ctx.getBean("jobDistribute");  
    			
    			
                String filePath = jobs_render.get(0).getFilePath();
                String FrameRange = jobs_render.get(0).getFrameRange();
                String jobId = jobs_render.get(0).getId();
                String jobName=jobs_render.get(0).getCameraName();
                
                //int nodes = jobs_render.get(0).getUnitsNumber();
                
                
//                //String fuWuListName = configurationService.findAllConfiguration().getFuWuListName();
//                String fuWuListName = "q_sw_share";
//                log.info("fuWuListName为:" + fuWuListName);
                
                
//                String stdout = "";

                log.info("渲染目录文件的地址filePath="+filePath+"渲染帧的范围："+FrameRange+"job_render的size为="+jobs_render.size());
                log.info("job_render的size为=\""+jobs_render.size());

                for(int i=0;i<jobs_render.size();i++){
                    log.info("jobs_render["+i+"]"+"的渲染进程名字是："+jobs_render.get(i).getCameraName());
                }              
                
                //get SampleRate
                int SampleRate=jobs_render.get(0).getSampleRate();
                
				//获取预渲染图片长宽
				int height = jobs_render.get(0).getxResolution();
				int width = jobs_render.get(0).getyResolution();
                
                //获取帧范围中最后一个也是最大的一个帧的数字。
                int frameNums = jobs_render.get(0).getFrameNumbers();
                String frameRange = jobs_render.get(0).getFrameRange();

                log.info("渲染的帧数量=："+frameNums+"渲染的帧范围=："+frameRange);
                //获取最大帧
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
                //打印渲染的帧数
                for(int i=0;i<frameToRender.length;i++){
                    log.info("frameToRender["+i+"]="+frameToRender[i]);
                }


//                String FrameMax=FrameRange.substring(FrameRange.length()-1);
                int FrameMax=frameToRender[frameNums-1];

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
                    	   //预渲染代码段
                    	boolean preFlag = false;
						try {
							preFlag = jobDistribute.distributePreRenderJob(filePath,jobName,jobId,SampleRate,height,width);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(preFlag == false){
							log.info("preFlag=" + preFlag + ",fail to distribute PreRenderJob！");
						}else{
							log.info("preFlag=" + preFlag + ",success distribute PreRenderJob！");
							
						}
                    	
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
