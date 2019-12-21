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
<script type="text/javascript" src="js/newProject.js"></script>
<script type="text/javascript" src="js/Validform_v5.3_min.js"></script>

</head>
<body>
	<div id="main_container">

		<%@ include file="inc/header.jsp"%>

		<div class="main_content">

			<%@ include file="inc/menu.jsp"%>

			<div class="center_content">
				<div class="right_content">
					<h2>新建工程</h2>

					<div class="ui-form-sitefont">
						<form id="newProject-form" action="doNewProject.action" method="post" >
							<input type="hidden" value="<s:property value="currentUser.id"/>" name="currentUser.id"/>
							<fieldset>
								<p class="form-field">
									<label class="input-label" for="project_client_id">所属用户名:</label>
									<select name="project_client_id" id="project_client_id" datatype="*" nullmsg="请选择用户！" sucmsg="&nbsp;">
											<option value="">--请选择用户--</option>
											<s:iterator value="users">
												<option value="<s:property value="id"/>"><s:property value="name"/></option>
											</s:iterator>
									</select>							
								</p>
								<p class="form-field">
									<label class="input-label" for="projectName">工程名:</label>
									<input type="text" name="projectInfo.name" id="projectName" datatype="*" ajaxurl="validateProjectName?currentUser.id=<s:property value="currentUser.id"/>" nullmsg="请输入工程名" sucmsg="&nbsp;" errormsg="必须为3-20位字母或字母数字组合，不含其它符号"/>
								</p>

							</fieldset>
							<p class="form-field"><input type="button" value="提交" id="newProjectBtn"/></p>

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

