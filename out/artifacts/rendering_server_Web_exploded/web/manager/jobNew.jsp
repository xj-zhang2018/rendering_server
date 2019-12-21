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
<script type="text/javascript" src="js/newJob.js"></script>
<script type="text/javascript" src="js/base.js"></script>
<script type="text/javascript" src="js/Validform_v5.3_min.js"></script>
</head>
<body>
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
					<h2>新建镜头</h2>

					<div class="ui-form-sitefont">
						<form id="newJob-form" action="doNewJob.action" method="post">
							
							<input type="hidden" value="<s:property value="currentProject.id"/>" name="currentProject.id"/>

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
								<p class="form-field">
									<label class="input-label" for="cameraName">镜头名:</label>
									<input type="text" name="cameraInfo.cameraName" id="cameraName" datatype="/^[a-z0-9A-Z]*[a-zA-Z]+[a-z0-9A-Z]*$/,*3-20" ajaxurl="validateCameraName?currentProject.id=<s:property value="currentProject.id"/>" nullmsg="请输入镜头名" sucmsg="&nbsp;" errormsg="必须为3-20位字母或字母数字组合，不含其它符号" />								
								</p>
								<p class="form-field">
									<label class="input-label" for="filePath">场景文件路径:</label>
											
										<select name="cameraInfo.filePath" id="filePath" datatype="*" nullmsg="请选择场景文件！" sucmsg="&nbsp;">
											<option value="">--请选择场景文件--</option>
										</select>
										<span class="help">没有找到场景文件？<a class="redirect-fileUpload" id="upLoadFileID" href="inFileUpload.action?currentProject.id=<s:property value="currentProject.id"/>">场景上传</a></span>
										
								</p>
								
								<p class="form-field">
									<label class="input-label" for="frameRange">帧范围:</label>
									<input type="text" name="cameraInfo.frameRange" id="frameRange" datatype="/^((\d+)|(\d+)-(\d+))(,((\d+)|(\d+)-(\d+)))*$/" nullmsg="请输入要渲染的帧" sucmsg="&nbsp;" errormsg="格式不符合" ajaxurl="validateFrameRange"/>
								</p>
							
								
								<p class="form-field">
									<label class="input-label" for="renderEngine">渲染引擎:</label>
									<select name="cameraInfo.renderEngineId" id="cameraInfo.renderEngineId"  datatype="*" nullmsg="请选择渲染引擎！" sucmsg="&nbsp;">
											<option value="">--请选择渲染引擎--</option>
											<s:iterator value="renderEngines">
												<option value="<s:property value="id"/>"><s:property value="name"/></option>
											</s:iterator>
									</select>			
								</p>
																			
								<!-- <p class="form-field" >
									<label class="input-label"  for="unitsNumber">所需渲染单元数:</label>
									<input type="text"  name="cameraInfo.unitsNumber" id="unitsNumber"  datatype="n"   nullmsg="请输入所需单元数" sucmsg="&nbsp;" errormsg="必须为整数"/>
								</p> -->
								
								
								
								
								<p class="form-field" >
									<label class="input-label"  for="unitsNumber">所需节点数:</label>
									<input type="text"  name="cameraInfo.unitsNumber" id="unitsNumber"  datatype="n"   nullmsg="请输入所需节点数" sucmsg="&nbsp;" errormsg="必须为整数"/>
								</p>
																				
			                   <p class="form-field">
			                        <label class="input-label" for="isPreRender">同意预渲染</label>
			                       	<input type="checkbox" name="cameraInfo.preRenderingTag" id="isPreRender" value="1" /><span>是</span>
			                    </p>
			                    <div id="preRenderInfo" style="display:none">								
									<p class="form-field">
										<label class="input-label" for="xResolution">预渲染图片长度:</label>
										<input type="text" name="cameraInfo.xResolution" id="xResolution" datatype="n" ignore="ignore" sucmsg="&nbsp;" errormsg="必须为整数"/>
									</p>
									<p class="form-field">
										<label class="input-label" for="yResolution">预渲染图片宽度:</label>
										<input type="text" name="cameraInfo.yResolution" id="yResolution" datatype="n" ignore="ignore" sucmsg="&nbsp;" errormsg="必须为整数"/>
									</p>
									<p class="form-field">
										<label class="input-label" for="sampleRate">采样率:</label>
										<input type="text" name="cameraInfo.sampleRate" id="sampleRate" datatype="n" ignore="ignore" sucmsg="&nbsp;" errormsg="必须为整数"/>
									</p>
								</div>				
							</fieldset>												
							<p class="form-field"><input type="button" value="提交" id="newJobBtn"/></p>

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