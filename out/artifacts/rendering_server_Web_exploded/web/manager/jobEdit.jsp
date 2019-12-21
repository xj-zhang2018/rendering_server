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
<script type="text/javascript" src="js/editJob.js"></script>
<script type="text/javascript" src="js/Validform_v5.3_min.js"></script>

</head>
<body>
	<div id="main_container">

		<%@ include file="inc/header.jsp"%>

		<div class="main_content">

			<%@ include file="inc/menu.jsp"%>

			<div class="center_content">
				<div class="right_content">
					<h2>编辑镜头</h2>

					<div class="ui-form-sitefont">
						<form id="editJob-form" action="jobUpdate.action" method="post">
							<input type="hidden" value="<s:property value="currentProject.id"/>" name="currentProject.id"/>
							<input type="hidden" value="<s:property value="jobId"/>" name="jobId"/>
							<input type="hidden" value="<s:property value="cameraInfo.preRenderingTag"/>" name="preRender"/>	
							<input type="hidden" value="<s:property value="cameraInfo.isrUnit"/>" name="cameraInfo.isrUnit"/>							

							<fieldset>
								<p class="form-field">
									<label class="input-label" for="cameraName">镜头名:</label>
									<input type="text" value="<s:property value="cameraInfo.cameraName"/>" name="cameraInfo.cameraName" id="cameraName" />
								</p>
								<p class="form-field">
									<label class="input-label" for="filePath">场景文件路径:</label>
									<select name="cameraInfo.filePath" id="filePath">
											<s:iterator value="mapScenePath">
												<s:if test="value==cameraInfo.filePath">
													<option selected="selected" value="<s:property value="value"/>"><s:property value="key"/></option>
												</s:if>
												<s:else>
													<option value="<s:property value="value"/>"><s:property value="key"/></option>
												</s:else>
											</s:iterator>
									</select>
								</p>
								<p class="form-field">
									<label class="input-label" for="frameRange">帧范围:</label>
									<input type="text" value="<s:property value="cameraInfo.frameRange"/>" name="cameraInfo.frameRange" id="frameRange" datatype="/^((\d+)|(\d+)-(\d+))(,((\d+)|(\d+)-(\d+)))*$/" nullmsg="请输入要渲染的帧" sucmsg="&nbsp;" errormsg="格式不符合" ajaxurl="validateFrameRange?filePath=<s:property value="cameraInfo.filePath"/>"/>
								</p>
								<s:if test='cameraInfo.isrUnit==true' >
								<p class="form-field">
									<label class="input-label" for="unitsNumber">所需渲染单元数:</label>
									<input type="text" value="<s:property value="cameraInfo.unitsNumber"/>" name="cameraInfo.unitsNumber" id="unitsNumber" datatype="n" nullmsg="请输入所需单元数" sucmsg="&nbsp;" errormsg="必须为整数"/>
								</p>
								</s:if>
								<s:else>
								<p class="form-field">
									<label class="input-label" for="nodesNumber">所需渲染节点数:</label>
									<input type="text" value="<s:property value="cameraInfo.nodesNumber"/>" name="cameraInfo.nodesNumber" id="nodesNumber" datatype="n" nullmsg="请输入所需单元数" sucmsg="&nbsp;" errormsg="必须为整数"/>
								</p>
								</s:else>
								<p class="form-field">
			                        <label class="input-label" for="isPreRender">同意预渲染</label>
			                       	<input type="checkbox" name="cameraInfo.preRenderingTag" id="isPreRender" value="1" /><span>是</span>
			                    </p>								
		        		                     
 			                    <div id="preRenderInfo" style="display:none">								
									<p class="form-field">
										<label class="input-label" for="xResolution">预渲染图片长度:</label>
										<input type="text" value="<s:property value="cameraInfo.xResolution"/>" name="cameraInfo.xResolution" id="xResolution" datatype="n" ignore="ignore" sucmsg="&nbsp;" errormsg="必须为整数"/>
									</p>
									<p class="form-field">
										<label class="input-label" for="yResolution">预渲染图片宽度:</label>
										<input type="text" value="<s:property value="cameraInfo.yResolution"/>" name="cameraInfo.yResolution" id="yResolution" datatype="n" ignore="ignore" sucmsg="&nbsp;" errormsg="必须为整数"/>
									</p>
									<p class="form-field">
										<label class="input-label" for="sampleRate">采样率:</label>
										<input type="text" value="<s:property value="cameraInfo.sampleRate"/>" name="cameraInfo.sampleRate" id="sampleRate" datatype="n" ignore="ignore" sucmsg="&nbsp;" errormsg="必须为整数"/>
									</p>
								</div>
																			
							</fieldset>
							
							<p class="form-field"><input type="button" value="提交" id="editJobBtn"/></p>
							
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