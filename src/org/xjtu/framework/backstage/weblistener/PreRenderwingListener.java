package org.xjtu.framework.backstage.weblistener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xjtu.framework.backstage.schedule.PreRenderwing;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PreRenderwingListener implements ServletContextListener {

    private static final Log log = LogFactory.getLog(PreRenderwingListener.class);

    public void contextDestroyed(ServletContextEvent arg0) {
        log.info("timer destroyed");
    }

    public void contextInitialized(ServletContextEvent event){
        //实时检测渲染XML文件的生成，准备开始预渲染
//        echo "" > catalina.out
        log.info("已经启动PreRenderwingListener监听器");
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        long initialDelay1 = 1;
        long period1 = 3;
        service.scheduleAtFixedRate(new PreRenderwing(event.getServletContext()), initialDelay1, period1, TimeUnit.SECONDS);//安排所提交的Runnable任务按指定的间隔重复执行

    }

}
