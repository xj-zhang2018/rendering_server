package org.xjtu.framework.core.util;

import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
  
import javax.servlet.ServletRequest;  

import org.xjtu.framework.core.base.model.EmailLink;
import org.xjtu.framework.core.base.model.User;
  

/** 
 * 生成帐户激活、重新设置密码的链接 
 */  
public class GenerateLinkUtils {  
      
    private static final String CHECK_CODE = "checkCode";  
      
    /** 
     * 生成帐户激活链接 
     */  
    public static String generateActivateLink(EmailLink emailLink) {  
        return "http://202.117.10.244:8080/rendering_server/activateAccount?id="   
                + emailLink.getId() + "&" + CHECK_CODE + "=" + emailLink.getId();  
    }  
      
    /** 
     * 生成重设密码的链接 
     */  
    public static String generateResetPwdLink(EmailLink emailLink,String basePath) {
    	if(basePath!=null){
    		return basePath+"/web/passwordResetCheck.action?username="   
                + emailLink.getName() + "&" + CHECK_CODE + "=" + emailLink.getId();  
    	}
    	else{
            return "http://202.117.10.244:8080/rendering_server/web/passwordResetCheck.action?username="   
                    + emailLink.getName() + "&" + CHECK_CODE + "=" + emailLink.getId();  
    	}
    }  
      
     
}  