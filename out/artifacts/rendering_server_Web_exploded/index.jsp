<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<html>
<head>

	<title>Login Form</title>
	
	<!-- Stylesheets -->
	<link rel="stylesheet" href="css/base.css">
	<link rel="stylesheet" href="css/skeleton.css">
	<link rel="stylesheet" href="css/layout.css">
	<script src="js/jquery-1.8.2.min.js"></script>
	<script src="js/app.js"></script>
	
</head>
<body>

	<div class="notice">
		<a href="" class="close">close</a>
		<p class="warn">Whoops! We didn't recognise your username or password. Please try again.</p>
	</div>

	<!-- Primary Page Layout -->

	<div class="container">
		
		<div class="form-bg">
			<form>
				<h2>Login</h2>
				<p><input type="text" placeholder="Username"></p>
				<p><input type="password" placeholder="Password"></p>
				<label for="remember">
				  <input type="checkbox" id="remember" value="remember" />
				  <span>Remember me on this computer</span>
				</label>
				<button type="submit"></button>
			<form>
		</div>

	
		<p class="forgot">Forgot your password? <a href="">Click here to reset it.</a></p>


	</div><!-- container -->
	
<!-- End Document -->
</body>
</html>