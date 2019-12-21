<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集群渲染管理平台</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/myform.css" />
<script type="text/javascript" src="js/clockp.js"></script>
<script type="text/javascript" src="js/clockh.js"></script>

<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/newUser.js"></script>
<script type="text/javascript" src="js/Validform_v5.3_min.js"></script>

</head>
<body>
	<div id="main_container">

		<%@ include file="inc/header.jsp"%>

		<div class="main_content">

			<%@ include file="inc/menu.jsp"%>

			<div class="center_content">
				<div class="right_content">
					<h2>注册用户</h2>

					<div class="ui-form-sitefont">
						<form id="newUser-form" action="doNewUser.action" method="post">
                          
							<fieldset>
								<p class="form-field">
									<label class="input-label" for="userName">用户名:</label>
									<input type="text" name="resgisterUserInfo.name" id="userName" datatype="/^[a-z0-9A-Z]*[a-zA-Z]+[a-z0-9A-Z]*$/,*3-20" nullmsg="请输入用户名" sucmsg="&nbsp;" errormsg="必须为3-20位字母或字母数字组合，不含其它符号" ajaxurl="validateUserName"/>
								</p>
								
								<p class="form-field">
									<label class="input-label" for="userEmail">Email:</label>
									<input type="text"  name="resgisterUserInfo.email" id="userEmail" datatype="e" nullmsg="请输入电子邮箱" sucmsg="&nbsp;" ajaxurl="validateEmail"/>
								</p>
								
								<p class="form-field">
									<label class="input-label" for="userMobile">手机号码:</label>
									<input type="text"  name="resgisterUserInfo.mobile" id="userMobile" datatype="m" nullmsg="请输入手机号码" errormsg="请输入正确的手机号码" sucmsg="&nbsp;"/>
								</p>
								
								<p class="form-field">
									<label class="input-label" for="userPassword">密码:</label>
									<input type="password"  name="resgisterUserInfo.password" id="userPassword" datatype="*5-20" sucmsg="&nbsp;" nullmsg="请输入密码" />
								</p>
								
								<p class="form-field">
									<label class="input-label" for="userRepassword">重新输入密码:</label>
									<input type="password"  name="resgisterUserInfo.repassword" id="userRepassword" datatype="*" recheck="resgisterUserInfo.password" nullmsg="请再次输入密码" sucmsg="&nbsp;"/>
								</p>

							</fieldset>

							<p class="form-field"><input type="button" value="提交" id="newUserBtn"/></p>
						</form>
					</div>

				</div>
				<!-- end of right content-->

			</div>
			<!--end of center content -->

			<div class="clear"></div>
		</div>
		<!--end of main content-->

		<%@ include file="inc/footer.jsp"%>

	</div>
</body>
</html>