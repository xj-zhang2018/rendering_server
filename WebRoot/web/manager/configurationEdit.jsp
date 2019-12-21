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
<script type="text/javascript" src="js/configurationUpdate.js"></script>
<script type="text/javascript" src="js/Validform_v5.3_min.js"></script>

</head>
<body>
	<div id="main_container">

		<%@ include file="inc/header.jsp"%>

		<div class="main_content">

			<%@ include file="inc/menu.jsp"%>

			<div class="center_content">
		
				<div class="right_content">
					<h2>系统环境配置</h2>
					
					<h4><s:actionmessage/></h4>

					<div class="ui-form-sitefont">
						<form id="configurationUpdate-form" action="configurationUpdate.action" method="post" >
						
							<fieldset>
								<p class="form-field">
									<label class="input-label" for="unitPriceValue" >每帧的单价/分钟.节点:</label>	
									 <input type="text" value=<s:property value="configuration.unitPrice"/> name="configuration.unitPrice" id="unitPriceValue" datatype="/^[0-9]+(.[0-9]{1,3})?$/" nullmsg="请输入每针渲染的单价"  errormsg="请输入合理的数字"/>
								</p>
								<p class="form-field">
									<label class="input-label" for="nodesNumPerUnitValue" >单个渲染单元节点数:</label>	
									 <input type="text" value=<s:property value="configuration.nodesNumPerUnit"/>  name="configuration.nodesNumPerUnit" id="nodesNumPerUnitValue" datatype="/^[0-9]*[1-9][0-9]*$/" nullmsg="请输入单个渲染单元节点数"  errormsg="请输入合理的数字"/>
								</p>
								<p class="form-field">
									<label class="input-label" for="fuWuListNameValue" >服务器队列:</label>	
									 <input type="text" value=<s:property value="configuration.fuWuListName"/>  name="configuration.fuWuListName" id="fuWuListNameValue" nullmsg="请输入服务器渲染队列名"/>
								</p>
								<p class="form-field">
									<label class="input-label" for="sceneMemorytNameValue" >加载场景内存大小:</label>	
									 <input type="text" value=<s:property value="configuration.sceneMemory"/>  name="configuration.sceneMemory" id="sceneMemory" nullmsg="请输入场景加载的内存大小"/>
								</p>
								<p class="form-field">
									<label class="input-label" for="hostStackValue" >栈深:</label>	
									 <input type="text" value=<s:property value="configuration.hostStack"/>  name="configuration.hostStack" id="hostStack" nullmsg="请输入栈深的大小"/>
								</p>
								<p class="form-field">
									<label class="input-label" for="shareSizeValue" >共享内存:</label>	
									 <input type="text" value=<s:property value="configuration.shareSize"/>  name="configuration.shareSize" id="shareSize" nullmsg="请输入共享内存大小"/>
								</p>
								<p class="form-field" style="display:none">
									<label class="input-label" for="newRenderEngine" >新添加渲染引擎:</label>	
									 <input type="text"   name="newRenderEngine" id="newRenderEngine" />
								</p>
							</fieldset>
							<p class="form-field"><input type="button" value="提交" id="configurationUpdateBtn"  /></p>
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

