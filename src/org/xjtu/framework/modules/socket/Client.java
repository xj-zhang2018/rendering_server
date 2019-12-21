package org.xjtu.framework.modules.socket;

import java.io.*;
import java.net.*;
import java.util.*;

import org.xjtu.framework.modules.protocol.TaskDiscription;


public class Client {
	static public boolean sendToC(String message,String ip,int port){

		Socket clientSocket=null;
		boolean isSucceed=false;

		try{
			
			clientSocket=new Socket(InetAddress.getByName(ip),port);//向对应IP，端口发出客户请求
			
			InputStream inputStream=clientSocket.getInputStream();
			BufferedReader inBufferedReader=new BufferedReader(new InputStreamReader(inputStream));
			
			OutputStream outputStream=clientSocket.getOutputStream();
			PrintWriter outPrintWriter=new PrintWriter(outputStream);
					
			String str="";
			int ch;
			while ((ch=inBufferedReader.read())!=-1) {
	
				str=str+(char)ch;					
	
				if (str.endsWith("|end")) {
					if(str.equals("#helloClient|end")){
											
						outPrintWriter.print(message);
						outPrintWriter.flush();
						isSucceed=true;
						
					}else{
						System.out.println("error server response!");
					}
					break;
				}
			}
			
			outPrintWriter.close();
			inBufferedReader.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(clientSocket!=null){
				try {  
					clientSocket.close();  
                } catch (IOException e) { 
                	e.printStackTrace();
                } 
			}
			
		}
		
		return isSucceed;
	}

}