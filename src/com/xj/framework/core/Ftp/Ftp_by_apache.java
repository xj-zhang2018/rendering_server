package com.xj.framework.core.Ftp;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.net.*; 
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


public class Ftp_by_apache {
    
    
    FTPClient f=null;
    //榛樿鏋勯�犲嚱鏁�
    public Ftp_by_apache(String url,String username,String password,String workingDirectory) 
    {
        f=new FTPClient();
        try {
			//寰楀埌杩炴帴
			this.get_connection(url,username,password);
			f.changeWorkingDirectory(workingDirectory);
			
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        
        
        
        
        
    }
    
    
    //杩炴帴鏈嶅姟鍣ㄦ柟娉�
    public void get_connection(String url,String username,String password){
        
    
        try {
            //杩炴帴鎸囧畾鏈嶅姟鍣紝榛樿绔彛涓�21  
            f.connect(url);
            System.out.println("connect success!");
            
            //璁剧疆閾炬帴缂栫爜锛寃indows涓绘満UTF-8浼氫贡鐮侊紝闇�瑕佷娇鐢℅BK鎴杇b2312缂栫爜  
            f.setControlEncoding("GBK");
            
            //鐧诲綍
            boolean login=f.login(username, password);
            if(login)
                System.out.println("登录成功!");
            else
                System.out.println("登陆失败");
        
        }
        catch (IOException e) {
            
            e.printStackTrace();
        }
        
        
    }    
    
    public void close_connection() {
        
         boolean logout=false;
        try {
            logout = f.logout();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }  
         if (logout) {  
             System.out.println("注销成功!");  
         } else {  
             System.out.println("注销失败!");  
         }  
        
        if(f.isConnected())
            try {
                System.out.println("关闭连接");
                f.disconnect();
            } catch (IOException e) {
            
                e.printStackTrace();
            }
        
    }
    
    
    //鑾峰彇鎵�鏈夋枃浠跺拰鏂囦欢澶圭殑鍚嶅瓧
    public FTPFile[] getAllFile(){
        
        
        FTPFile[] files = null;
        try {
            files = f.listFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for(FTPFile file:files)
        {
            
//            if(file.isDirectory())
//                System.out.println(file.getName()+"鏄枃浠跺す");
//            if(file.isFile())
//                System.out.println(file.getName()+"鏄枃浠�");
        }
        return files;
        
    }
    
    
    //鐢熸垚InputStream鐢ㄤ簬涓婁紶鏈湴鏂囦欢  
    public void upload(String File_path) throws IOException{
        
        InputStream input=null;
        String[] File_name = null;
        try {
            input = new FileInputStream(File_path);
            File_name=File_path.split("\\\\");
            System.out.println(File_name[File_name.length-1]);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
         //涓婁紶鏂囦欢  
        System.out.println(File_name[File_name.length-1]);
        f.storeFile(File_name[File_name.length-1], input);
        System.out.println("上传成功!");
        
        if(input!=null)
        input.close();
        
        
        
    }
    
    
    //涓嬭浇 from_file_name鏄笅杞界殑鏂囦欢鍚�,to_path鏄笅杞藉埌鐨勮矾寰勫湴鍧�
    public void download(String from_file_name,String to_path) throws IOException{
        
        
        
        OutputStream output=null;
        try {
            output = new FileOutputStream(to_path+from_file_name);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        f.retrieveFile(from_file_name, output);
        if(output!=null)
        {
            try {
                if(output!=null)
                output.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
    }
      
}