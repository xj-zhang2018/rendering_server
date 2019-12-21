package org.xjtu.framework.core.base.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemConfig{

	private String jobManageService;
	private String SystemUserName;
	private String SystemPasswd;
	private String renderingWorkDir;
	 private Properties props=null;
	
	 
	 
	public SystemConfig() {
		 InputStream in = SystemConfig.class.getClassLoader().getResourceAsStream("system_config.properties");
	        props = new Properties();
	        try {
				props.load(in);
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        //关闭资源
	}

	public String getJobManageService() {
		return jobManageService;
	}

	public void setJobManageService(String jobManageService) {
		this.jobManageService = jobManageService;
	}

	public String getSystemUserName() throws IOException {
		
		 return  props.getProperty("SystemUserName");
	}

	

	public String getSystemPasswd() {
		 return  props.getProperty("SystemPasswd");
	}

	public String getRenderingWorkDir() {
		 return  props.getProperty("renderingWorkDir");
	}

	
	
	
	
	
		
}
