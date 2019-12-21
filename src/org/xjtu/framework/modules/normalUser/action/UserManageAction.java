package org.xjtu.framework.modules.normalUser.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.xjtu.framework.core.base.constant.RegistReturnInfo;
import org.xjtu.framework.core.base.constant.UserType;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.LinuxInvoker;
import org.xjtu.framework.core.util.MD5;
import org.xjtu.framework.core.util.StringUtil;
import org.xjtu.framework.modules.user.service.ProjectService;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.vo.RegisterUserInfo;
import org.xjtu.framework.modules.user.webservice.RenderUserService;

@ParentPackage("struts-default")
@Namespace("/web/user")
public class UserManageAction extends UserBaseAction  implements ServletResponseAware{
	
	/**
	 * 
	 */
	
	Logger log = Logger.getLogger(UserManageAction.class);
	
	private static final long serialVersionUID = 1L;
	
	private @Resource UserService userService;
	private @Resource RenderUserService renderUserService;
	private String errorCode;
    private User user;
    private String userId;
	private RegisterUserInfo registerUserInfo;
    private Integer isChgPasswd;
    private javax.servlet.http.HttpServletResponse response;
   

	
	

	 @Action(value="updateUser",results={@Result(name=ERROR,location="/web/user/userinfo.jsp"),
	    		@Result(name=SUCCESS,location="/web/user/userinfo.jsp")})
		public String updateUser(){
		 user = (User)session.get("user");
			
			if(user!=null){
				
				//log.info("璇风湅锛氱敤鎴�"+user.getName()+"鐨別mail鏄�"+registerUserInfo.getEmail());
				
				
				if(StringUtil.isEmail(registerUserInfo.getEmail()))
					user.setEmail(registerUserInfo.getEmail());
				else{
					 this.errorType="error";
					 this.errorCode="邮箱格式错误！";
					 return ERROR;
				}
				
				
				
				if(StringUtil.isMobileNO(registerUserInfo.getMobile()))
					user.setMobile(registerUserInfo.getMobile());
				else{
					 this.errorType="error";
					 this.errorCode="手机号码格式错误！";
					 return ERROR;
				}
				
				
	    	    if(isChgPasswd!=null&&isChgPasswd==1){
	    	    	//System.out.println("鏄淇敼瀵嗙爜鐨勫晩锛佷负浠�涔堟病鏀�");
	    	    	if(registerUserInfo.getPassword().equals(registerUserInfo.getRepassword()))
	    	    		user.setPassword(MD5.encode(registerUserInfo.getPassword()));
	    	    	else{
	    	    		this.errorType="error";
						this.errorCode="两次密码输入不一致，请重新输入！";
					    return ERROR;
	    	    	}
	    	    }
	    	    
	    	    userService.updateUserInfo(user);
	    	    this.errorType="error";
				this.errorCode="您的个人信息修改成功！";
	    	    return SUCCESS;
			}else 
				return ERROR;
				
	    }
	
	
	
	 @Action(value = "doUserRegister", results = { @Result(name = ERROR, location = "/web/user/register.jsp"),
	    		@Result(name = SUCCESS, location = "/web/user/registersuccess.jsp")})
		public String doUserRegister(){
			
			if(registerUserInfo!=null){
				
				int ret=renderUserService.doRegister(registerUserInfo);
				
				if(ret==RegistReturnInfo.success){
					return SUCCESS;
				}else
					{
				   this.errorType="error";
				   this.errorCode="信息填写有误，请重新注册！";
					return ERROR;
					
					}
			}else
			{
				this.errorType="error";
				this.errorCode="注册信息为空，请重新填写！";
				return ERROR;
			}
		}
	 
	 @Action(value = "userinfo", results = { @Result(name = ERROR, location = "/web/user/login.jsp"),
	    	@Result(name = SUCCESS, location = "/web/user/userinfo.jsp")})
	
	  public String userinfo ()throws IOException{
		 
		 response.setContentType("text/html;charset=UTF-8");
         response.setCharacterEncoding("UTF-8");//闃叉寮瑰嚭鐨勪俊鎭嚭鐜颁贡鐮�
         PrintWriter out = response.getWriter();
		 user = (User)session.get("user");
		 if(user==null)
		 {
			   out.print("<script>alert('请先登录！')</script>");
               out.print("<script>window.location.href='login.jsp'</script>");
               out.flush();
               out.close();
			   return ERROR;
		 }
			 
			
		 else
     		 return SUCCESS;	 
	 }
	 
	 @Action(value = "deposit", results = { @Result(name = ERROR, location = "/web/user/login.jsp"),
		    	@Result(name = SUCCESS, location = "/web/user/deposit.jsp")})
		
		  public String deposit()throws IOException{
		 response.setContentType("text/html;charset=UTF-8");
         response.setCharacterEncoding("UTF-8");//闃叉寮瑰嚭鐨勪俊鎭嚭鐜颁贡鐮�
         PrintWriter out = response.getWriter();
		 user = (User)session.get("user");
		 if(user==null)
		 {
			   out.print("<script>alert('请先登录！')</script>");
               out.print("<script>window.location.href='login.jsp'</script>");
               out.flush();
               out.close();
			   return ERROR;
		 }
			 
			
		 else
     		 return SUCCESS;	
		 }
	 
	 @Action(value = "downloadpage", results = { @Result(name = ERROR, location = "/web/user/login.jsp"),
		    	@Result(name = SUCCESS, location = "/web/user/download.jsp")})
		
		  public String downloadpage()throws IOException{
		 response.setContentType("text/html;charset=UTF-8");
         response.setCharacterEncoding("UTF-8");//闃叉寮瑰嚭鐨勪俊鎭嚭鐜颁贡鐮�
         PrintWriter out = response.getWriter();
		 user = (User)session.get("user");
		 if(user==null)
		 {
			   out.print("<script>alert('请先登录！')</script>");
               out.print("<script>window.location.href='login.jsp'</script>");
               out.flush();
               out.close();
			   return ERROR;
		 }
			 
			
		 else
     		 return SUCCESS;	
		 }
	 
	 @Action(value = "loginpage", results = {@Result(name = SUCCESS, location = "/web/user/login.jsp")})
		
	 public String loginpage()throws IOException{
		    response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");//闃叉寮瑰嚭鐨勪俊鎭嚭鐜颁贡鐮�
            PrintWriter out = response.getWriter();
		    user = (User)session.get("user");
		    if(user!=null)
		   {
			out.print("<script>alert('您已登录！')</script>");
            out.print("<script>window.history.back(-1)</script>");
            out.flush();
            out.close();
			   return ERROR;
		 }
			 
			
		 else
  		    return SUCCESS;	
		 }
	 
	 @Action(value = "registerpage", results = { @Result(name = SUCCESS, location = "/web/user/register.jsp")})
		
	 public String registerpage()throws IOException{
		    response.setContentType("text/html;charset=UTF-8");
         response.setCharacterEncoding("UTF-8");//闃叉寮瑰嚭鐨勪俊鎭嚭鐜颁贡鐮�
         PrintWriter out = response.getWriter();
		    user = (User)session.get("user");
		    if(user!=null)
		   {
			out.print("<script>alert('您已登陆，无需重复注册！')</script>");
         out.print("<script>window.history.back(-1)</script>");
         out.flush();
         out.close();
			   return ERROR;
		 }
			 	
		 else
		    return SUCCESS;	
		 }
	 
	 public Integer getIsChgPasswd() {
		return isChgPasswd;
	}

	public void setIsChgPasswd(Integer isChgPasswd) {
		this.isChgPasswd = isChgPasswd;
	}

	public String getErrorCode() {
			return errorCode;
		}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
		
	public RegisterUserInfo getResgisterUserInfo() {
		return registerUserInfo;
	}
	
	public void setResgisterUserInfo(RegisterUserInfo resgisterUserInfo) {
		this.registerUserInfo = resgisterUserInfo;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	 public String getUserId() {
			return userId;
		}


	public void setUserId(String userId) {
		this.userId = userId;
	}
		
	public void setServletResponse(HttpServletResponse response) {
		   this.response = response;
		}
	
}
