package org.xjtu.framework.core.util;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.xj.framework.ssh.Shell;

public class LinuxInvoker {
 
 private String cmd = null;
 private String stdOut = null;
 private String stdErr = null;
 private int exitValue = -1;
 private Process subProcess = null;
 
	private static final Log log = LogFactory.getLog(LinuxInvoker.class);	
 public LinuxInvoker(){
	 this.cmd = null;
	 this.exitValue = -1;
	 this.stdErr = null;
	 this.stdOut = null;
	 this.subProcess = null;
 }
 public LinuxInvoker(String cmd){
	 this.cmd = cmd;
	 this.exitValue = -1;
	 this.stdErr = null;
	 this.stdOut = null;	
	 this.subProcess = null;
 }
 
private String getStream(InputStream instream) throws Exception {
		int rtvalue = 0;
		StringBuffer streamvalue = new StringBuffer();
		while((rtvalue = instream.read()) != -1) {
			streamvalue.append((char)rtvalue);
		}
		return streamvalue.toString();
	}
 



public void getFile(String src,String dist) {
	if(src==null||dist==null)return;
	  Shell shell = new Shell("41.0.0.188", "swsdu", "123456");
	  shell.getFile(src, dist);
}




 //为通用调用linux方法写的函数
public void executeCommand() throws Exception{
	if(cmd != null)
	{
		try{
		
			String outPut="";
			  Shell shell = new Shell("41.0.0.188", "swsdu", "123456");
		        shell.execute(cmd);
		        outPut = shell.getStdout();
		      log.info("return stdout"+outPut);
			
			/*String[] exc = {"/bin/sh", "-c", cmd};
		subProcess = Runtime.getRuntime().exec(exc);
		
		stdOut = getStream(subProcess.getInputStream());   //获取执行的结果输出
		stdErr = getStream(subProcess.getErrorStream());
		
		exitValue = subProcess.waitFor();	
		
		if(exitValue != 0){
			throw new Exception("throw Command " +cmd+ " Failed: Error Message:" +stdErr);
		}*/
		      
		      
		      
	}catch(Exception e){
		throw new Exception("catch Command " +cmd+ " Failed: Error Message:" + e.getMessage());
		}
	}
	else
	{
		throw new Exception("throw  Command Failed: the command hasn't been specified.");
	}			
	return;
}




//在用户创建的过程中，由于要用命令判断用户是否存在，需要调用命令进行，但是如果查不到，java中的exitValue认为出错，所以新写一个函数
public void executeComandUser() throws Exception{
	if(cmd != null)
	{
		try{
		String[] exc = {"/bin/sh", "-c", cmd};
		subProcess = Runtime.getRuntime().exec(exc);
		
		stdOut = getStream(subProcess.getInputStream());   //获取执行的结果输出
		stdErr = getStream(subProcess.getErrorStream());
		
	}catch(Exception e){
		throw new Exception("catch Command " +cmd+ " Failed: Error Message:" + e.getMessage());
		}
	}
	else
	{
		throw new Exception("throw  Command Failed: the command hasn't been specified.");
	}			
	
}



//将运算的结果的渲染文件都收集到一个目录下面去





public String getCmd() {
	return cmd;
}


public void setCmd(String cmd) {
	this.cmd = cmd;
}


public String getStdOut() {
	return stdOut;
}


public void setStdOut(String stdOut) {
	this.stdOut = stdOut;
}


public String getStdErr() {
	return stdErr;
}


public void setStdErr(String stdErr) {
	this.stdErr = stdErr;
}


public int getExitValue() {
	return exitValue;
}


public void setExitValue(int exitValue) {
	this.exitValue = exitValue;
}
	
}
