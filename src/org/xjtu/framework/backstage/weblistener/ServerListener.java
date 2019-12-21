package org.xjtu.framework.backstage.weblistener;

import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xjtu.framework.modules.socket.Server;

public class ServerListener implements ServletContextListener {

	private static final Log log = LogFactory.getLog(ServerListener.class);

	private Server server;
	private Thread thread;

	public void contextDestroyed(ServletContextEvent arg0) {

		server.closeServerSocket();
		thread.stop();
		log.info("Server closed!");
	}

	public void contextInitialized(ServletContextEvent event) {

		try {
			server = new Server(event.getServletContext());
			thread = new Thread(server);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("socket server is start");
		log.info("Server start!");
	}
}
