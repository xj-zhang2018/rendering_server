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

<link rel="stylesheet" type="text/css" href="css/960.css" />
<link rel="stylesheet" type="text/css" href="css/template.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/homepage.css" />

	<!--[if lt IE 9]>
	<link rel="stylesheet" href="css/ie.css" type="text/css" media="screen" />
	<script src="js/html5.js"></script>
	<![endif]-->
<script type="text/javascript" src="js/clockp.js"></script>
<script type="text/javascript" src="js/clockh.js"></script>
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/homeHighcharts.js"></script>
<script type="text/javascript" src="js/highcharts.js"></script>
<script type="text/javascript" src="js/exporting.js"></script>
<script type="text/javascript" src="js/draggable-legend.js"></script>
<script type="text/javascript" src="js/comm.js"></script>
<script src="js/glow/1.7.0/core/core.js" type="text/javascript"></script>
<script src="js/glow/1.7.0/widgets/widgets.js" type="text/javascript"></script>
<link href="js/glow/1.7.0/widgets/widgets.css" type="text/css" rel="stylesheet" />
<script type="text/javascript">
	glow.ready(function(){
		new glow.widgets.Sortable(
			'#content .grid_5, #content .grid_6',
			{
				draggableOptions : {
					handle : 'h2'
				}
			}
		);
	});
</script>

</head>
<body>

	<div id="main_container">

		<%@ include file="inc/header.jsp"%>

		<div class="main_content">

			<%@ include file="inc/menu.jsp"%>

			<div class="center_content">
				<h2> </h2>	       
				
				<div id="content" class="container_16 clearfix">
					<div class="grid_9">
						<div class="box">
							<h2>作业</h2>
							<div id="container"></div>
						</div>
						<div class="box">
							<h2>管理节点CPU监控</h2>
							<div id="container2"></div>
						</div>
						<div class="box">
							<div class="bottom-btn bottom-btn-green">
								<a href="yscreen.action" class="shadow">大屏幕监控队列节点</a>
							</div>
						</div>
					</div>
					<div class="grid_7">
						<div class="box">
							<h2>节点统计</h2>
							<div id="container1"></div>
						</div>
						<div class="box">
							<h2>管理节点内存监控</h2>
							<div id="container3"></div>
						</div>
						<div class="box">
							<h2>日志</h2>
							<article class="module width_quarter">
								<div class="message_list" >
									<div class="module_content">
										<s:iterator value="msgs">
											<div class="message"><p><s:property value="contents"/></p>
											<p><strong><s:date name="createTime" format="yyyy-MM-dd hh:mm:ss"/></strong></p></div>							
										</s:iterator>
									</div>
								</div>	
							</article>
						</div>
					</div>
				</div>								  												
			</div>
		
	
			<!--end of center content -->

			<div class="clear"></div>
		</div>
		<!--end of main content-->

		<%@ include file="inc/footer.jsp"%>

	</div>
</body>
</html>
