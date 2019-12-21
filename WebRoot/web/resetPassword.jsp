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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重新设置密码</title>
<link rel="stylesheet" href="css/signup.css">
<script src="js/jquery-1.8.2.min.js"></script>
<script src="js/resetPassword.js"></script>
<script src="js/Validform_v5.3_min.js"></script>

</head>
<body class="Signup-Page">

<div id="wrap">
	<div id="content">
		<h1 id="logo">西交渲染农场</h1>
		<h2 id="page-title">重置密码</h2>
		
		<div id="signup-form-box" class="ui-form-sitefont">
		<form action="<%=basePath%>/web/resetPassword.action" id="signup-form" method="post">

			<s:if test="errorType=='correct'">
				<input type="hidden" name="checkCode" value="${checkCode}" />
				<input type="hidden" name="username" value="${username}" />
					<fieldset> 	
						<p style="margin:0 0 20px 110px; font-size:15px;"><strong style="color:#abfc47;">${username}</strong>，请在此输入你的新密码</p>	        	
			            <p class="form-field">
			                <label class="input-label" for="password">新密码</label>
			                <input type="password" id="password" name="newPassword" datatype="*5-20" sucmsg="&nbsp;" nullmsg="请输入密码"/>
			            </p>
			            <p class="form-field">
			                <label class="input-label" for="re-password">确认新密码</label>
			                <input type="password" id="re-password" name="newPassword2" datatype="*" recheck="newPassword" nullmsg="请再次输入密码" sucmsg="&nbsp;"/>
			            </p>    
			        </fieldset>	   						
					<p class="form-field"><input type="button" value="修改" id="resetBtn"/></p>
			</s:if>
			<s:if test="errorType=='error'">
				<div class="rest-pw-success" style="text-align:center;">
					<p>此重置密码链接无效！</p>
				</div>

			</s:if>			
			<s:if test="errorType=='success'">
				<div class="rest-pw-success" style="text-align:center;">
					<p>修改密码成功！</p>
				</div>
			</s:if>
		
		</form>
		</div>
	</div>
</div>
</body>
</html>