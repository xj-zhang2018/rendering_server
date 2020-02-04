package com.xj.framework.ssh;
import com.jcraft.jsch.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Shell {
    //杩滅▼涓绘満鐨刬p鍦板潃
    private String ip;
    //杩滅▼涓绘満鐧诲綍鐢ㄦ埛鍚�
    private String username;
    //杩滅▼涓绘満鐨勭櫥褰曞瘑鐮�
    private String password;
    //璁剧疆ssh杩炴帴鐨勮繙绋嬬鍙�
    public static final int DEFAULT_SSH_PORT = 22;  
    //淇濆瓨杈撳嚭鍐呭鐨勫鍣�
    private String stdout;
    private static final Log log = LogFactory.getLog(Shell.class);
    /**
     * 鍒濆鍖栫櫥褰曚俊鎭�
     * @param ip
     * @param username
     * @param password
     */
    public Shell(final String ip, final String username, final String password) {
         this.ip = ip;
         this.username = username;
         this.password = password;
         stdout = "";
    }
    /**
     * 鎵цshell鍛戒护
     * @param command
     * @return
     */
    public int execute(final String command) {
        int returnCode = 0;
        JSch jsch = new JSch();
      //  String pubKeyPath = "/home/export/online1/systest/swsdu/.ssh/id_rsa";
        MyUserInfo userInfo = new MyUserInfo();
        log.info(" 测试点1");
        try {
        	//jsch.addIdentity(pubKeyPath);
            //鍒涘缓session骞朵笖鎵撳紑杩炴帴锛屽洜涓哄垱寤簊ession涔嬪悗瑕佷富鍔ㄦ墦寮�杩炴帴
            Session session = jsch.getSession(username, ip, DEFAULT_SSH_PORT);
            log.info(" 测试点2");
            session.setPassword(password);
            session.setUserInfo(userInfo);
            session.setConfig( "StrictHostKeyChecking" , "no" ); // 涓嶉獙璇乭ost-key锛岄獙璇佷細澶辫触銆�
            log.info(" 测试点3");
            session.connect();
            log.info(" 测试点4");
            //鎵撳紑閫氶亾锛岃缃�氶亾绫诲瀷锛屽拰鎵ц鐨勫懡浠�
            Channel channel = session.openChannel("exec");
            ChannelExec channelExec = (ChannelExec)channel;
            log.info(" 测试点5");
            channelExec.setCommand(command);
            channelExec.setInputStream(null);
            log.info(" 测试点6");
            InputStream input=channelExec.getInputStream();

            log.info(" 测试点7");
            channelExec.connect();
            log.info(" 测试点8");
            System.out.println("远程主机命令 :" + command);

            //鎺ユ敹杩滅▼鏈嶅姟鍣ㄦ墽琛屽懡浠ょ殑缁撴灉
            log.info(" 测试点9");
            stdout=getStream(input);
            log.info(" 测试点10");
            input.close();  

            // 寰楀埌returnCode
            if (channelExec.isClosed()) {  
                returnCode = channelExec.getExitStatus();
                log.info(" 测试点11");
            }  

            // 鍏抽棴閫氶亾
            channelExec.disconnect();
            //鍏抽棴session
            session.disconnect();
            log.info(" 测试点12");
        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnCode;
    }
   
    
    
    
    
    //灏嗚繙绋嬬殑鏂囦欢涓嬭浇鍒版湰鍦�
    public void getFile(String src,String dist) {
    	 JSch jsch = new JSch();
         Session session=null;;
         ChannelSftp channelSftp = null;
		try {
			session = jsch.getSession(username, ip, DEFAULT_SSH_PORT);
	         session.setPassword(password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channelSftp = (ChannelSftp)session.openChannel("sftp");
			channelSftp.connect();
			// channelSftp.setFilenameEncoding("gbk");
			channelSftp.get(src,dist);
			System.out.println("download successful");   
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			if(channelSftp!=null && channelSftp.isConnected()){
				channelSftp.disconnect();
			}
			if(session!=null && session.isConnected()){
				session.disconnect();
			}
		}
 	}
    
    private String getStream(InputStream stream) throws IOException {//灏嗚緭鍏ユ祦璇诲嚭杩斿洖瀛楃涓�
		int i=0;
		StringBuffer streamBuffer=new StringBuffer();
		while ((i=stream.read())!=-1) {
			streamBuffer.append((char)i);
		}
		return streamBuffer.toString();
	}

    
    
    
    public String getStdout() {
		return stdout;
	}
	public void setStdout(String stdout) {
		this.stdout = stdout;
	}
	public static void main(final String [] args) {  
        Shell shell = new Shell("41.0.0.188", "swsdu", "swsdu@9012");
        shell.execute(" ls /home/export/online1/systest/swsdu/xijiao/Users/xjtu/camera1");
        System.out.println("res is"+shell.getStdout());
    }  
}