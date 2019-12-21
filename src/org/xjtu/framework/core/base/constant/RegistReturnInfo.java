package org.xjtu.framework.core.base.constant;

public class RegistReturnInfo {
	public static final int success = 0;
	
	public static final int nullInfo = 1;     /*注册信息不能为空*/
	
	public static final int nullName = 2;    /*用户名不能为空*/
	
	public static final int illegalName=3;   /*用户名长度需在20位以下*/
	
	public static final int nullPassword=4;  /*密码不能为空*/
	
	public static final int illegalPassword=5;  /*密码需在6到20位之间*/
	
	public static final int diffRepassword=6;  /*重复密码须一致*/
	
	public static final int nullEmail=7;    /*邮箱不能为空*/
	
	public static final int illegalEmail=8;  /*邮箱格式非法*/
	
	public static final int illegalMobile=9;  /*手机可以为空，当不为空时，手机号格式须合法*/
	
	public static final int existedUser = 10;
	
}
