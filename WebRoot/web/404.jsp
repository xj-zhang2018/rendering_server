<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>

<html>
	<head>
		<title>XJTU Render Farm</title>
	</head>	
	<link href="<%=basePath%>/web/css/error.css" rel="stylesheet" type="text/css" />

	
	<body id="e404">
	
	<div id="content2">
		<div id="wrapper">
			<h1 class="e404">Missing</h1>
			<h2>The page you were looking for could not be found.</h2>
			<a href="http://www.xjtu.edu.cn/"class="gohome">XJTU Render Farm</a>
		</div>
	</div>
	
	</body>

</html>
