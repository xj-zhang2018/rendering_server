package org.xjtu.framework.backstage.schedule;

import com.xj.framework.ssh.ShellLocal;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

import static org.xjtu.framework.backstage.schedule.JobSchedule.*;

public class PreRenderwing  implements Runnable {


    private static final Logger log = Logger.getLogger(PreRenderwing.class);
    private ServletContext context = null;
    private static boolean isRunning = false;
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

                        String RenderDir=jobs_render.get(0).getFilePath();
                        String FrameRange=jobs_render.get(0).getFrameRange();
                        String RenderId=jobs_render.get(0).getId();

                        log.info("渲染目录文件的地址RenderDir="+RenderDir+"渲染帧的范围："+FrameRange+"job_render的size为="+jobs_render.size());
                        log.info("job_render的size为=\""+jobs_render.size());

                        for(int i=0;i<jobs_render.size();i++){
                            log.info("jobs_render["+i+"]"+"的渲染进程名字是："+jobs_render.get(i).getCameraName());
                        }

                        //获取帧范围中最后一个也是最大的一个帧的数字。
                        String FrameMax=FrameRange.substring(FrameRange.length()-1);
                        String FrameName="frame"+FrameMax+".xml";
                        log.info("最大的帧的名字是："+FrameName);

                        ShellLocal shell=new ShellLocal( "cd "+RenderDir+" && ls *.xml");
                        String result=shell.executeCmd();
                        result=result.trim();

                        log.info("result的内容是："+result);

                        String[]filesName=result.split("\n");

                        for(int i=0;i<filesName.length;i++){
                            log.info("当前的filesName是="+filesName[i]);
                            if (filesName[i].equals(FrameName)){
                                //开始预渲染
                                /**
                                 *开始预渲染代码段
                                 *
                                 */
                                log.info(filesName[i]+"和"+FrameName+" 相等哦"+"job_render的size为="+jobs_render.size());
                                jobs_render.remove(0);
                                log.info("移除index0后，job_render的size为="+jobs_render.size());
                                break;
                            }else{
                                log.info(filesName[i]+"和"+FrameName+" 不相等哦");
                            }
            //                log.info("存在文件--》"+filesNamelesName[i]+"  \n"+"  ");

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
