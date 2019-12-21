<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<html>
<head>
<meta charset="utf-8" />
<title>集群渲染管理平台</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/scriptupload.css" />
<script type="text/javascript" src="js/clockp.js"></script>
<script type="text/javascript" src="js/clockh.js"></script>
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/scriptUpload.js"></script>

</head>
<body>
	<div id="main_container">

		<%@ include file="inc/header.jsp"%>

		<div class="main_content">

			<%@ include file="inc/menu.jsp"%>

			<div class="center_content">
				<div class="right_content">
				    

					<form id="scriptUpload-form" action="scriptUpload.action" method="POST" enctype="multipart/form-data">

						<fieldset>
							<legend>作业脚本上传</legend>
							
							<input type="hidden" id="MAX_FILE_SIZE" name="MAX_FILE_SIZE" value="300000">
							
							<div>
								<label for="file-attachment">上传脚本:</label>
								<input type="file" id="file-attachment" name="attachment" multiple="multiple">
								<div id="filedrag" class="">
									或拖动脚本到这里
								</div>
							</div>
							<input id="attachmentName" name="attachmentName" type="hidden" />
							<div id="submitbutton">
								<button type="submit">上传脚本</button>
							</div>
						
						</fieldset>
					
					</form>
					
					<div id="messages">
						<p>Status Messages</p>
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