<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />	
		<title>集群渲染管理平台登录</title>		
		<link href="<%=basePath%>/web/css/login.css" type="text/css" media="screen" rel="stylesheet" />
		<style type="text/css">
			img, div { behavior: url(iepngfix.htc) }
		</style>
	</head>
	<body id="login">
		<div id="wrappertop"></div>
			<div id="wrapper">
					<div id="content">
						<div id="header">
							<h1><a href="#"><img src="<%=basePath%>/web/images/logo.png" alt="RenderWing"></a></h1>
						</div>
						<div id="darkbanner" class="banner320">
							<h2>登录</h2>
						</div>
						<div id="darkbannerwrap">
						</div>
						<form name="form1" method="post" action="<%=basePath%>/web/doLogin.action">
							
							<fieldset class="form">
								<s:if test="errorType=='error'">
									<p class="error">
										<img src="<%=basePath%>/web/images/error.png" height="16px" width="16px">
										<s:property value="errorCode" />
									</p>
								</s:if>
	                        	<p>
									<label for="username">用户名:</label>
									<input name="username" id="username" type="text" value="" />
								</p>
								<p>
									<label for="password">密码:</label>
									<input name="password" id="password" type="password" />
								</p>
								<button type="submit" class="positive" name="Submit">
									<img src="<%=basePath%>/web/images/key.png" alt="Announcement"/>进入</button>
									<ul id="forgottenpassword">
									<li class="boldtext">|</li>
									<li><a href="<%=basePath%>/web/findPassword.action">忘记密码?</a></li>
								</ul>
	                   		</fieldset>
						</form>
						
					</div>
				</div>   

<div id="wrapperbottom_branding"><div id="wrapperbottom_branding_text"><a href="http://www.xjtu.edu.cn" style='text-decoration:none'>Powered By XJTU</a>.</div></div>