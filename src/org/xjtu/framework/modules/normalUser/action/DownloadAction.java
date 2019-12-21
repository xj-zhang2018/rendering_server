package org.xjtu.framework.modules.normalUser.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.model.User;

@ParentPackage("struts-default")
@Namespace("/web/user")
public class DownloadAction extends UserBaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(DownloadAction.class);
	private static final int BUFFEREDSIZE = 1024;
    ByteArrayInputStream renderResult; 
    String downloadFileName = "";
 
    private User user;
    
	
	@Action(value = "downloadRenderResult", results = { @Result(name = ERROR, type="httpheader",params={"status","204"}),
    		@Result(name = SUCCESS, type = "stream", params = {  
    	            "contentType", "application/zip", "inputName",  
    	            "renderResult", "contentDisposition",  
    	            "attachment;filename=${downloadFileName}.zip", "bufferSize", "1024" })}) 
    public String downloadRenderResult() throws Exception{
  
		   // user = (User)session.get("user");
		    
			ByteArrayOutputStream output = new ByteArrayOutputStream();   	
			ZipOutputStream zos = new ZipOutputStream(output);
			
			String path="/home/RenderFarm/apache-tomcat-6.0.37/lib/";
				//user.getHomeDir()+"building/Pictures";
			log.info(path);//打印路径，看是否有误
			
			File file = new File(path);
			this.downloadFileName="result";
						
			zip(file,zos,"result");
			
			//清空缓冲区数据，这一步务必先执行
			zos.flush();
			zos.close();

			byte[] ba = output.toByteArray();
			renderResult=new ByteArrayInputStream(ba);
			
			if(renderResult==null)//确定renderResult是否为空
				{
				log.info("renderResult�ǿյ�");
				}else {
					log.info("renderResult���ǿյ�");
				}
			
			output.flush();
			output.close();
			
			return SUCCESS;
			
    }
    
  
	private synchronized void zip(File inputFile, ZipOutputStream out, String base)   
            throws IOException {   
        if (inputFile.isDirectory()) {   
            File[] inputFiles = inputFile.listFiles();   
            out.putNextEntry(new ZipEntry(base + "/"));   
            base = base.length() == 0 ? "" : base + "/";   
            for (int i = 0; i < inputFiles.length; i++) {   
                zip(inputFiles[i], out, base + inputFiles[i].getName());   
            }   
  
        } else {   
            if (base.length() > 0) {   
                out.putNextEntry(new ZipEntry(base));   
            } else {   
                out.putNextEntry(new ZipEntry(inputFile.getName()));   
            }   
  
            FileInputStream in = new FileInputStream(inputFile);   
            try {   
                int c;   
                byte[] by = new byte[BUFFEREDSIZE];   
                while ((c = in.read(by)) != -1) {   
                    out.write(by, 0, c);   
                }   
            } catch (IOException e) {   
                throw e;   
            } finally {   
                in.close();   
            }   
        }   
    }
	
	public ByteArrayInputStream getRenderResult() {
		return renderResult;
	}

	public void setRenderResult(ByteArrayInputStream renderResult) {
		this.renderResult = renderResult;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
}
