<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String basePath2 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();	
%>
<div class="header">
	<div class="logo">
		<a href="#"><img src="images/logo.png" alt="" title="" border="0" />
		</a>
	</div>

	<div class="right_header">
		<s:text name="header.welcome" /> <s:property value="#session.user.name"/> | <a href="<%=basePath2%>/web/logout.action" class="logout"><s:text name="header.logout" /></a>
	</div>
	<div id="clock_a"></div>
</div>
<!-- /#header -->