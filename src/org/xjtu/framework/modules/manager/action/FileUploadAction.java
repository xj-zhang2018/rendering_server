package org.xjtu.framework.modules.manager.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.modules.user.service.UserService;

@ParentPackage("json-default")
@Namespace("/web/manager")
public class FileUploadAction extends ManagerBaseAction {
	
	private @Resource UserService userService;
	
	private String fname;
	
	private String userId;
	
	private String sceneDir;
	
	private String sname;
	
	@Action(value = "fileUpload", results = {@Result(name = SUCCESS, type = "json")})	
	public String fileUpload() throws IOException {
		
		File file;

		if(sceneDir!=null&&sceneDir!=""){

			String filename=httpServletRequest.getHeader("X_FILENAME");
			file = new File(sceneDir+"/"+new String(filename.getBytes("iso8859-1"),"utf-8"));

		}else{
						
			User user=userService.findUserById(userId);
			file = new File(user.getHomeDir()+new String(fname.getBytes("iso8859-1"),"utf-8"));
		}
		
		File parent = file.getParentFile();
		
		if(parent!=null&&!parent.exists()){ 
			parent.mkdirs(); 
		} 

		file.createNewFile();
		
		FileOutputStream fos=new FileOutputStream(file);
		InputStream is=httpServletRequest.getInputStream();
		
		byte[] bytes = new byte[1024];
		int c;
		while ((c = is.read(bytes)) != -1) {
			fos.write(bytes, 0, c);
		}
		is.close();
		fos.close();
		
		return SUCCESS;
	}
	
	@Action(value = "scriptUpload", results = {@Result(name = SUCCESS, type = "json")})	
	public String scriptUpload() throws IOException {
		
		File file;

		String filename=httpServletRequest.getHeader("X_FILENAME");
		

         String filepath=this.getClass().getResource("/").getPath()+"shell/"+new String(filename.getBytes("iso8859-1"),"utf-8");
		
		
		file = new File(filepath); 

			
		
		
		file.createNewFile();
		
		FileOutputStream fos=new FileOutputStream(file);
		InputStream is=httpServletRequest.getInputStream();
		
		byte[] bytes = new byte[1024];
		int c;
		while ((c = is.read(bytes)) != -1) {
			fos.write(bytes, 0, c);
		}
		is.close();
		fos.close();
		
		return SUCCESS;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSceneDir() {
		return sceneDir;
	}

	public void setSceneDir(String sceneDir) {
		this.sceneDir = sceneDir;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}
	
}
