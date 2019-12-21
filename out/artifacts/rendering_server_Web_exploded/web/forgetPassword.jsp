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
		<link href="css/login.css" type="text/css" media="screen" rel="stylesheet" />
		<style type="text/css">
			img, div { behavior: url(iepngfix.htc) }
		</style>
	</head>
	<body id="login">
		<div id="wrappertop"></div>
			<div id="wrapper">
					<div id="content">
						<div id="header">
							<h1><a href="#"><img src="images/logo.png" alt="RenderWing"></a></h1>
						</div>
						<div id="darkbanner" class="banner320">
							<h2>忘记密码</h2>
						</div>
						<div id="darkbannerwrap">						</div>
					<form name="form1" method="post" action="<%=basePath%>/web/doFindPassword.action">
						<fieldset class="form">
                        	<p>忘记密码？请输入您的注册邮箱，我们将发送给您一封邮件，通过邮件中的链接可以修改您的密码。</p><br>
							<p>
								<label for="username">邮箱:</label>
								<input name="username" id="username" type="text" />
							</p>
							<button type="submit" class="positive" name="Submit">
								<img src="images/key.png" alt="Announcement"/>申请新的密码
							</button>
						</fieldset>
					</form>
					</div>
				</div> 

<div id="wrapperbottom_branding"><div id="wrapperbottom_branding_text"><a href="http://www.xjtu.edu.cn" style='text-decoration:none'>Powered By XJTU</a>.</div></div>