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
<script type="text/javascript" src="js/base.js"></script>
<script type="text/javascript" src="js/Validform_v5.3_min.js"></script>

<script type="text/javascript" src="js/newCalculate1.js"></script>
</head>
<body>
<s:debug></s:debug>
	<div id="main_container">

		<%@ include file="inc/header.jsp"%>

		<div class="main_content">
		
		    <s:if test="errorType == 'success'">
	    		<div class="msg msg-success">
	    			<h2>操作成功！</h2>
	    			<h5><s:property value="errorCode" /></h5>
	    		</div>
    		</s:if>
    		<s:elseif test="errorType == 'error'">
	    		<div class="msg msg-failed">
	    			<h2>操作失败！</h2>
	    			<h5><s:property value="errorCode" /></h5>
	    		</div>
    		</s:elseif>

			<%@ include file="inc/menu.jsp"%>

			<div class="center_content">
				<div class="right_content">
					<h2>新建解析任务</h2>

					<div class="ui-form-sitefont">
						<form id="newCalculate-form" action="doNewCalculate.action" method="post">
							
							<input type="hidden" value="<s:property value="currentUser.id"/>" name="currentUser.id"/> 

							<fieldset>
								<p class="form-field">
									<label class="input-label" for="calculate_client_id">所属用户名:</label>	
																						
										<select name="calculate_client_id" id="calculate_client_id" datatype="*" nullmsg="请选择用户！" sucmsg="&nbsp;">
												<option value="">--请选择用户--</option>
												<s:iterator value="users">
													<option value="<s:property value="id"/>"><s:property value="name"/></option>
												</s:iterator>
										</select>				
								</p>
								
								
								
								<p class="form-field">
									<label class="input-label" for="job_project_id">所属工程名:</label>		
									<div id="selectProjectInfo" style="display:block">								
										<select name="job_project_id" id="job_project_id" datatype="*" nullmsg="请选择工程！" sucmsg="&nbsp;">
											<option value="">--请选择工程--</option>
										</select>	
									</div>	
									
									<div id="loadingProject" style="display:none">
										<img alt="" src="images/loading.gif" width="28px" height="28px" />正在加载中......
									</div>				
								</p>

								
								
								<!-- 为了测试占时不使用上传功能 -->
								 <p class="form-field">
									<label class="input-label" for="filePath">场景文件路径:</label>
											
										<select name="calculateInfo.xmlFilePath" id="filePath" datatype="*" nullmsg="请选择xml文件！" sucmsg="&nbsp;">
											<option value="">--请选择xml场景文件--</option>
										</select>
										<span class="help">没有找到xml文件？<a class="redirect-fileUpload" id="upLoadFileID" href="inFileUpload.action?currentProject.id=<s:property value="currentProject.id"/>">xml场景上传</a></span>
										
								</p>
								
								<p class="form-field">
									<label class="input-label" for="calculateName">解析任务名:</label>
									<input type="text" name="calculateInfo.xmlName" id="calculateName" datatype="*" ajaxurl="validateCalculateName?currentUser.id=<s:property value="currentUser.id"/>" nullmsg="请输入解析任务名" sucmsg="&nbsp;" errormsg="必须为3-20位字母或字母数字组合，不含其它符号"/>
								</p>

							</fieldset>												
							<p class="form-field"><input type="button" value="提交" id="newCalculateBtn"/></p>

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