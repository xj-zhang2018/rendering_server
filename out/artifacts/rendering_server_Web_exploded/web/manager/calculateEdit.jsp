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

<!-- 此处需要添加修改操作的动作 -->
<script type="text/javascript" src="js/editCalculate.js"></script>



<script type="text/javascript" src="js/Validform_v5.3_min.js"></script>

</head>
<body>
	<div id="main_container">

		<%@ include file="inc/header.jsp"%>

		<div class="main_content">

			<%@ include file="inc/menu.jsp"%>

			<div class="center_content">
				<div class="right_content">
					<h2>编辑解算任务</h2>

					<div class="ui-form-sitefont">
						<form id="editCalculate-form" action="calculateUpdate.action" method="post">
							<input type="hidden" value="<s:property value="currentProject.id"/>" name="currentProject.id"/>
							<input type="hidden" value="<s:property value="xmlId"/>" name="xmlId"/>
							<fieldset>
								<p class="form-field">
									<label class="input-label" for="xmlName">任务名:</label>
									<input type="text" value="<s:property value="calculateInfo.xmlName"/>" name="calculateInfo.xmlName" id="xmlName" />
								</p>
								<p class="form-field">
									<label class="input-label" for="filePath">场景文件路径:</label>
									<select name="calculateInfo.xmlFilePath" id="filePath">
											<s:iterator value="mapScenePath">
												<s:if test="value==calculateInfo.xmlFilePath">
													<option selected="selected" value="<s:property value="value"/>"><s:property value="key"/></option>
												</s:if>
												<s:else>
													<option value="<s:property value="value"/>"><s:property value="key"/></option>
												</s:else>
											</s:iterator>
									</select>
								</p>
							</fieldset>
							
							<p class="form-field"><input type="button" value="提交" id="editCalculateBtn"/></p>
							
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