package org.xjtu.framework.modules.normalUser.action;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.constant.UserType;
import org.xjtu.framework.core.base.model.EmailLink;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.GenerateLinkUtils;
import org.xjtu.framework.core.util.MD5;
import org.xjtu.framework.core.util.MailUtil;
import org.xjtu.framework.core.util.StringUtil;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.modules.user.service.EmailLinkService;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.vo.RegisterUserInfo;

@ParentPackage("struts-default")
@Namespace("/web/user")
public class UseLoginAction extends UserBaseAction {

	Logger log = Logger.getLogger(UseLoginAction.class);
	
    private String errorCode;
	
	private String rememberMe;
	
    private String username;
    
    private String password;
    
    private User user;
    
   private RegisterUserInfo registerUserInfo;
    
    private String checkCode;
    
    private String newPassword;
    
    private @Resource UserService userService;
   
	private @Resource EmailLinkService emailLinkService;
	

    @Action(value = "userLogin", results = { @Result(name = "login", location = "/web/user/index.jsp" ),
    		@Result(name = "adminSuccess", type="redirectAction", location = "../manager/home"),
    		@Result(name = "userSuccess", location = "/web/user/index.jsp")})
    		
    public String userLogin() {
    	//鍏堜粠session涓彇淇℃伅锛岄槻姝㈤噸澶嶇櫥褰曪紝鐒跺悗鍙栦笉鍒帮紝鍐嶅幓cookie涓彇淇℃伅
    	if(session.get("user") != null){
    		user = (User)session.get("user");
    		if(user.getType() == UserType.NORMAL){//娴嬭瘯鎴愬姛锛屼慨鏀筸ark
    			return "userSuccess";
    		}
    	}
    	Cookie[] cookies = httpServletRequest.getCookies();
    	String md5Password = "";
    	if(cookies == null){
    		return "login";
    	}
   		for(Cookie cookie : cookies){
			if(cookie.getName().equals("rememberMe") && cookie.getValue() != null && cookie.getValue().equals("true")){
				rememberMe = cookie.getValue();
			}else if(cookie.getName().equals("username") && cookie.getValue() != null){
				username = cookie.getValue();
			}else if(cookie.getName().equals("password") && cookie.getValue() != null){
				md5Password = cookie.getValue();
			}
		}
   		
   		//cookie涓惈鏈夎浣忔垜锛屽氨鎵ц鑷姩鐧诲綍锛屽幓鏁版嵁搴撻獙璇佺敤鎴峰悕瀵嗙爜
   		if(!StringUtils.isEmpty(rememberMe)){
   			if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(md5Password)){
   	    		
   	    		if(username.contains("@")){
   					user = userService.loginByEmail(username, md5Password);
   				}else{
   					user = userService.loginByLoginName(username, md5Password);
   				}
   	    		if(user!=null){
   	    	    	if(user.getType()!=null&&user.getType()==UserType.NORMAL){//娴嬭瘯鎴愬姛锛屼慨鏀筸ark
   	    	    		session.put("user", user);
   	    	    		log.info("普通用户" + user.getName() + "登陆成功！");
   	    	    		return "userSuccess";
   	    	    	}
   	        	}
   	    	}
   		}
    	return "login";
    }
    
    
    @Action(value = "normalUserDoLogin", results = { @Result(name = "userSuccess", location = "/web/user/index.jsp"),
    		@Result(name = ERROR, location = "/web/user/login.jsp") })
    public String normalUserDoLogin() {
    	
    	if(validateUsername(username) && validatePassword(password)){
    		password = MD5.encode(password);
    		
    		if(username.contains("@")){
				user = userService.loginByEmail(username, password);
			}else{
				user = userService.loginByLoginName(username, password);
			}
    		
    		if(user!=null){
    	    	if(user.getType()!=null&&user.getType()==UserType.NORMAL){//娴嬭瘯鎴愬姛锛屼慨鏀筸ark
    	    		session.put("user", user);
    	    		if("true".equals(rememberMe)){
        				Cookie cookieName = new Cookie("username", user.getName());    
        				cookieName.setPath("/"); 
        				cookieName.setMaxAge(30*24*60*60);
        				
        				Cookie cookiePassword = new Cookie("password", user.getPassword());    
        				cookiePassword.setPath("/"); 
        				cookiePassword.setMaxAge(30*24*60*60);
        				
        				Cookie cookieRememberMe = new Cookie("rememberMe", rememberMe);    
        				cookieRememberMe.setPath("/"); 
        				cookieRememberMe.setMaxAge(30*24*60*60);
        				        				
        				httpServletResponse.addCookie(cookieName);
        				httpServletResponse.addCookie(cookiePassword);
        				httpServletResponse.addCookie(cookieRememberMe);
    	    		}
    	    		log.info("普通用户" + user.getName() + "登陆成功！");
    	    		 user.setLastAccessTime(new Date());
    	    		 userService.updateUserInfo(user);
    	    		return "userSuccess";
    	    	}else{
    	    		this.errorType="error";
    	    		this.errorCode="您不是普通用户，其他用户暂时无法登陆！";
    	    		return ERROR;
    	    	}
        	}else{
        		this.errorType="error";
        		this.errorCode="用户名和密码错误，请重新输入！";
        		return ERROR;
        	}
    	}else{
    		this.errorType="error";
    		return ERROR;
    	}
    	
    }
    
    @Action(value = "userLogout", results = { @Result(name = SUCCESS, location = "/web/user/index.jsp") })
    public String userLogout() {
    	
    	if(session.get("user") != null){
    		session.remove("user");    		
    	}
    	    	
    	return SUCCESS;
    }

    @Action(value = "findPassword", results = {@Result(name = SUCCESS, location = "/web/user/forgetpass.jsp")})
    public String inFindPassword() {
    	
    	return SUCCESS;
    }
    
    
    @Action(value = "doFindPassword", results = {@Result(name = SUCCESS, location = "/web/user/login.jsp")})
    public String doFindPassword() {

    	if(username.contains("@")){
			user = userService.findUserByEmail(username);
		}else{
			user = userService.findUserByName(username);
		}

		if(user!=null){

			EmailLink emailLink=new EmailLink();
			emailLink.setId(UUIDGenerator.getUUID());
			emailLink.setName(user.getName());
			emailLink.setCreateTime(new Date());
			
    		String basePath = httpServletRequest.getScheme() + "://"
    				+ httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort()
    				+ httpServletRequest.getContextPath();
			
			boolean temp=MailUtil.sendMail(user.getEmail(),"渲染农场取回密码信息","要使用新密码，请使用以下链接启用密码:<br/><a href='" + GenerateLinkUtils.generateResetPwdLink(emailLink,basePath) +"'>点击重新设置密码</a>","text/html;charset=utf-8",null,null);
			if(!temp){
				/*閭欢鍙戦�佸け璐�*/
				log.info("找回密码邮件发送失败！");
				this.errorType="error";
        		this.errorCode="找回密码邮件发送失败！";
				return ERROR;
			}
			else{
				/*鑻ラ偖浠跺彂閫佹垚鍔燂紝鍒欏線EmailLink琛ㄤ腑娣诲姞涓�鏉′俊鎭紝鐢ㄦ潵楠岃瘉鐢ㄦ埛閲嶇疆瀵嗙爜鐨勯摼鎺�*/
				emailLinkService.addEmailLink(emailLink);
				return SUCCESS;
			}
		}
		else{
			log.info("不存在该用户！");
			this.errorType="error";
    		this.errorCode="不存在该用户！";
			return ERROR;
		}
    }
    

    
    private boolean validateUsername(String username){
    	if(StringUtils.isEmpty(username)){
    		this.errorCode="用户名为空！";
    		return false;
    	}else{
    		if(username.length() <= 20){
    			if(StringUtil.isLetterOrNumber(username)|| StringUtil.isEmail(username)){
    				return true;
    			}else{
    				this.errorCode="用户名长度必须是数字和字母的组合或者是邮箱!";
        			return false;    				
    			}
    		}else{
    			this.errorCode="用户长度超过20个字符!";
    			return false;
    		}
    	}
    }
    
    private boolean validatePassword(String password){
    	if(StringUtils.isEmpty(password)){
    		this.errorCode="密码为空！";
    		return false;
    	}else{
    		if(password.length() <= 20 && password.length() >= 6){
    			return true;
    		}else{
    			this.errorCode="密码长度必须在6-20个字符间！";
    			return false;
    		}
    	}
    	
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(String rememberMe) {
		this.rememberMe = rememberMe;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
