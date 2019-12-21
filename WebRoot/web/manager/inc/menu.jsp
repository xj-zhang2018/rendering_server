<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String basePath1 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();	
%>
<div class="menu">		
<ul>
	<li>
		<s:if test="menuName == 'home'">
			<a class="current" href="<%=basePath1%>/web/manager/home.action"><s:text name="menu.home" /></a>
		</s:if>
		<s:else>
			<a href="<%=basePath1%>/web/manager/home.action"><s:text name="menu.home" /></a>
		</s:else>
	</li>
	<li>
		<s:if test="menuName == 'projectManage'">
			<a class="current" href="<%=basePath1%>/web/manager/projectList.action"><s:text name="menu.projects" /></a>
		</s:if>
		<s:else>
			<a href="<%=basePath1%>/web/manager/projectList.action"><s:text name="menu.projects" /></a>
		</s:else>
	</li>
		
	<li>
		<s:if test="menuName == 'jobManage'">
			<a class="current" href="<%=basePath1%>/web/manager/jobList.action"><s:text name="menu.jobs" /></a>
		</s:if>
		<s:else>
			<a href="<%=basePath1%>/web/manager/jobList.action"><s:text name="menu.jobs" /></a>
		</s:else>
	</li>

	<li>
		<s:if test="menuName == 'userManage'">
			<a class="current" href="<%=basePath1%>/web/manager/userList.action"><s:text name="menu.users" /></a>
		</s:if>
		<s:else>
			<a href="<%=basePath1%>/web/manager/userList.action"><s:text name="menu.users" /></a>
		</s:else>
	</li>
	
	<!--宋韦2018/10/23写的关于xml文件解析页面 -->
	<li>
		<s:if test="menuName == 'xmlAnalyze'">
			<a class="current" href="<%=basePath1%>/web/manager/xmlAnalyzeList.action"><s:text name="menu.xmlAnalyze" /></a>
		</s:if>
		<s:else>
			<a href="<%=basePath1%>/web/manager/xmlAnalyzeList.action"><s:text name="menu.xmlAnalyze" /></a>
		</s:else>
	</li>
		
	<%-- <li>
		<s:if test="menuName == 'unitManage'">
			<a class="current" href="<%=basePath1%>/web/manager/unitList.action"><s:text name="menu.units" /></a>
		</s:if>
		<s:else>
			<a href="<%=basePath1%>/web/manager/unitList.action"><s:text name="menu.units" /></a>
		</s:else>
	</li> --%>
	
	
	<li>
		<s:if test="menuName == 'configurationManage'">
			<a class="current" href="<%=basePath1%>/web/manager/configurationList.action"><s:text name="menu.configuration" /></a>
		</s:if>
		<s:else>
			<a href="<%=basePath1%>/web/manager/configurationList.action"><s:text name="menu.configuration" /></a>
		</s:else>
	</li>
	<%-- <li>
		<s:if test="menuName == 'scriptUpload'">
			<a class="current" href="<%=basePath1%>/web/manager/inScriptUpload.action"><s:text name="menu.scriptUpload" /></a>
		</s:if>
		<s:else>
			<a href="<%=basePath1%>/web/manager/inScriptUpload.action"><s:text name="menu.scriptUpload" /></a>
		</s:else>
	</li> --%>
</ul>
</div>
<!-- /#header -->