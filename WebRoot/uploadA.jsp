<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
			
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>渲染系统上传页面</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/myform.css" />
<script type="text/javascript" src="js/clockp.js"></script>
<script type="text/javascript" src="js/clockh.js"></script>
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/Validform_v5.3_min.js"></script>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/stream-v1.css" />
<style>
.divcss5 {
	margin: 0 auto;
	width: 1205px;
	position: fixed;
	top: 50px;
	left: 0px;
	right: 0px;
	bottom: 220px;
	margin: auto;
}
</style>
</head>

<body>
	<div class="divcss5">
	
	<%@ include file="web/manager/inc/menu.jsp"%>
	
		<div class="content_css main_content">
			<div id="i_select_files"
				style="width: 1100px; margin-left: 15px; margin-right: 15px; "></div>
			<div id="i_stream_files_queue"
				style="width: 1100px; margin-left: 15px;margin-top: 50px; margin-right: 15px; margin-bottom: 20px;"></div>
			&nbsp; &nbsp;  <button onclick="javascript:_t.upload();">开始上传</button>
			|
			<button onclick="javascript:_t.stop();">停止上传</button>
			|
			<button onclick="javascript:_t.cancel();">取消</button>
			|
			<button onclick="javascript:_t.disable();">禁用文件选择</button>
			|
			<button onclick="javascript:_t.enable();">启用文件选择</button>
			|
			<button onclick="javascript:_t.hideBrowseBlock();">隐藏文件选择按钮</button>
			|
			<button onclick="javascript:_t.showBrowseBlock();">显示文件选择按钮</button>
			|
			<button
				onclick="javascript:_t.destroy();_t=null;_t=new Stream(config);">销毁重新生成按钮</button>
			<br> <br> <strong>&nbsp; &nbsp;  提示信息:</strong> <br>
			<div id="i_stream_message_container" class="stream-main-upload-box"
				style="overflow: auto; height: 200px; margin-left: 15px; margin-right: 5px; width: 1100px; margin-top: 20px;"></div>
			<br>

			<script type="text/javascript"
				src="${pageContext.request.contextPath}/js/stream-v1.js"></script>
			<script type="text/javascript">
				var config = {
					browseFileId : "i_select_files",
					/** 选择文件的ID, 默认: i_select_files */
					browseFileBtn : "<div>请选择文件</div>",
					/** 显示选择文件的样式, 默认: `<div>请选择文件</div>` */
					dragAndDropArea : "i_select_files",
					/** 拖拽上传区域，Id（字符类型"i_select_files"）或者DOM对象, 默认: `i_select_files` */
					dragAndDropTips : "<span>把文件(文件夹)拖拽到这里</span>",
					/** 拖拽提示, 默认: `<span>把文件(文件夹)拖拽到这里</span>` */
					filesQueueId : "i_stream_files_queue",
					/** 文件上传容器的ID, 默认: i_stream_files_queue */
					filesQueueHeight : 200,
					/** 文件上传容器的高度（px）, 默认: 450 */

					swfURL : "${pageContext.request.contextPath}/swf/FlashUploader.swf",
					/** SWF文件的位置 */
					tokenURL : "${pageContext.request.contextPath}/tk",
					/** 根据文件名、大小等信息获取Token的URI（用于生成断点续传、跨域的令牌） */
					frmUploadURL : "${pageContext.request.contextPath}/fd;",
					/** Flash上传的URI */
					uploadURL : "${pageContext.request.contextPath}/upload",
					/** HTML5上传的URI */
					simLimit : 200,
					/** 单次最大上传文件个数 */
					autoUploading : false,
					/** 选择文件后是否自动上传, 默认: true */

					messagerId : "i_stream_message_container",
					/** 消息显示容器的ID, 默认: i_stream_message_container */
					multipleFiles : true,
					/** 多个文件一起上传, 默认: false */
					onRepeatedFile : function(f) {
						alert("文件：" + f.name + " 大小：" + f.size + " 已存在于上传队列中。");
						return false;
					}
				};
				var _t = new Stream(config);
			</script>
		</div>
		<!-- 添加页尾 -->
		<div class="footer" >
			<div class="left_footer">
				<s:text name="footer.title" />
				| <a href="http://www.xjtu.edu.cn">XJTU</a>
			</div>
		</div>
	</div>
</body>
</html>
