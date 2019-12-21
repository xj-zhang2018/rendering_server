package org.xjtu.framework.modules.socket;

import java.io.*;
import java.net.*;

import javax.servlet.ServletContext;

public class Server implements Runnable {
	private static short PORT = 30003;
	private ServerSocket serverSocket;
	private Socket client;
	private Runnable runnable;
	private Thread thread;
	private ServletContext context = null;

	// 通过这个算任务总体信息
	// 获得一个镜头分成了哪些任务，在这个结构里面取出来任务的进度然后计算镜头的进度
	public Server(ServletContext context) throws IOException {
		// TODO Auto-generated constructor stub
		serverSocket = new ServerSocket(PORT, 5000);//5000是请求指队列的长度
		client = null;
		runnable = null;
		thread = null;
		this.context = context;
	}
	public void run() {
		try {
			while (true) {
				 //监听服务器端口，一旦有数据发送过来，那么就将数据封装成socket对象
			    //如果没有数据发送过来，那么这时处于线程阻塞状态，不会向下继续执行
				client = serverSocket.accept();
				runnable = new ThreadProcess(client, context);
				thread = new Thread(runnable);
				thread.start();//线程的run()中处理请求
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void setPORT(short pORT) {
		PORT = pORT;
	}

	public static short getPORT() {
		return PORT;
	}

	public void closeServerSocket() {
		try {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
