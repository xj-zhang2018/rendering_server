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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集群渲染管理平台</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<script type="text/javascript" src="js/clockp.js"></script>
<script type="text/javascript" src="js/clockh.js"></script> 
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/jconfirmaction.jquery.js"></script>

<script type="text/javascript">
	
	$(document).ready(function() {
		$('.ask').jConfirmAction();
	});
	
</script>
<script type="text/javascript" src="js/project.js"></script>

</head>
<body>
<div id="main_container">

	<%@ include file="inc/header.jsp"%>
    
    <div class="main_content">
    
		<%@ include file="inc/menu.jsp"%>
                                                                               
    	<div class="center_content">          
	    	<div class="right_content">                   
    			<h2><s:property value="currentUser.name"/>工程列表</h2>
    			<div class="sidebar_search"> 
	                <form id="project-search-form" class="default-form project-search" action="projectList.action" method="post">
						<input type="hidden" value="<s:property value="searchType"/>" name="searchType"/>
	                	<input type="hidden" value="<s:property value="searchText"/>" name="searchText"/>
	                	<input type="hidden" value="<s:property value="pageNum"/>" name="pageNum"/>
	                	<input type="hidden" value="<s:property value="pageTotal"/>" name="pageTotal"/>
	                	<input type="hidden" value="<s:property value="pageSize"/>" name="pageSize"/>
	                	
	                	<s:if test="searchType == 'accurateUserId'">
							<input type="text" id="project-search-text" class="search_input" value=""/>
						</s:if>
						<s:else>
							<input type="text" id="project-search-text" class="search_input" value="<s:property value="searchText"/>"/>
						</s:else>
		
						<select name="project-search-type" style="display:none">
								<option id="project-search-name" selected="selected">按工程名</option>
						</select>
						
						<a href="#" id="searchBtn"><img class="searchImg" src="images/search.png" /></a>
	
					</form>    
                </div>
                <form id="project-delete-form" action="projectsDelete.action" method="post">    
					<table id="rounded-corner" class="projectTable" summary="2007 Major IT Companies' Profit">
					    <thead>
					    	<tr>
					        	<th scope="col" class="rounded-company"><input type="checkbox" class="checkbox toggle"></th>
					            <th scope="col" class="rounded">工程名</th>
	
					            <th scope="col" class="rounded">镜头数</th>
					             <th scope="col" class="rounded">开始时间</th>
					              <th scope="col" class="rounded">结束时间</th>
					               <th scope="col" class="rounded">完成桢数</th>
					         
					            <th scope="col" class="rounded">编辑</th>
					            <th scope="col" class="rounded-q4">删除</th>
					        </tr>
					    </thead>
					
					    <tbody>
					    	<s:iterator value="projectInfomas">
						    	<tr>
						        	
						            
						            <td><input type="checkbox" class="checkbox" name="projectIds" value=<s:property value="id"/> /></td>
						            <td><a href="jobList.action?searchText=<s:property value="id"/>&searchType=accurateProjectId"><s:property value="name"/></a></td>
						             
						             <td><s:property value="camerasNum"/></td>
						             <td><s:property value="StartTime"/></td>
						             <td><s:property value="EndTime"/></td>
						             <td><s:property value="AllFramesNum"/></td>
						             

						         
						            <td><a href="<%=basePath%>/web/manager/inProjectEdit.action?projectId=${id}&currentUser.id=<s:property value="currentUser.id" /> "><img src="images/user_edit.png" alt="" title="" border="0" /></a></td>
						            <td><a href="<%=basePath%>/web/manager/projectsDelete.action?projectIds=${id}&currentUser.id=<s:property value="currentUser.id" />" class="ask"><img src="images/trash.png" alt="" title="" border="0" /></a></td>
						        </tr>    
					        </s:iterator>
					    </tbody>
					</table>
				              
				 
				 <a href="inNewProject.action?userId=<s:property value="currentUser.id"/>" class="bt_green"><span class="bt_green_lft"></span><strong>添加新的工程</strong><span class="bt_green_r"></span></a>
				 
			     <a href="#" class="bt_red" id="deleteAllBtn"><span class="bt_red_lft"></span><strong>删除所选</strong><span class="bt_red_r"></span></a> 
     
                 </form>
		        <div class="pagination">
		        	
		        </div> 
                        
     		</div><!-- end of right content-->
                               
  		</div>   <!--end of center content -->               
                                              
    	<div class="clear"></div>
    </div> <!--end of main content-->
	   
	<%@ include file="inc/footer.jsp"%>

</div>		
</body>
</html>