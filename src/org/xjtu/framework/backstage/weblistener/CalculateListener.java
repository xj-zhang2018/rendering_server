package org.xjtu.framework.backstage.weblistener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xjtu.framework.backstage.schedule.CalculateSchedule;
import org.xjtu.framework.backstage.schedule.JobSchedule;
import org.xjtu.framework.backstage.schedule.UnitFaultTolerance;

public class CalculateListener implements ServletContextListener{
	
	private static final Log log = LogFactory.getLog(CalculateListener.class);
		
	public void contextDestroyed(ServletContextEvent arg0) {
		   log.info("timer destroyed");
	}
	
	public void contextInitialized(ServletContextEvent event){
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
		long initialDelay1 = 1;
		long period1 = 5;
		service.scheduleAtFixedRate(new CalculateSchedule(event.getServletContext()), initialDelay1, period1, TimeUnit.SECONDS);//安排所提交的Runnable任务按指定的间隔重复执行

	/*	
		long initialDelay2 = 1;
		long period2 = 600;
		service.scheduleAtFixedRate(new UnitFaultTolerance(event.getServletContext()), initialDelay2, period2, TimeUnit.SECONDS);
		*/
	}
}
