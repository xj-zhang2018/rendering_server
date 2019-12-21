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
<title><s:text name="title"></s:text></title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<script type="text/javascript" src="js/clockp.js"></script>
<script type="text/javascript" src="js/clockh.js"></script> 
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/ddaccordion.js"></script>
<script type="text/javascript">
ddaccordion.init({
	headerclass: "submenuheader", //Shared CSS class name of headers group
	contentclass: "submenu", //Shared CSS class name of contents group
	revealtype: "click", //Reveal content when user clicks or onmouseover the header? Valid value: "click", "clickgo", or "mouseover"
	mouseoverdelay: 200, //if revealtype="mouseover", set delay in milliseconds before header expands onMouseover
	collapseprev: true, //Collapse previous content (so only one open at any time)? true/false 
	defaultexpanded: [], //index of content(s) open by default [index1, index2, etc] [] denotes no content
	onemustopen: false, //Specify whether at least one header should be open always (so never all headers closed)
	animatedefault: false, //Should contents open by default be animated into view?
	persiststate: true, //persist state of opened contents within browser session?
	toggleclass: ["", ""], //Two CSS classes to be applied to the header when it's collapsed and expanded, respectively ["class1", "class2"]
	togglehtml: ["suffix", "<img src='images/plus.gif' class='statusicon' />", "<img src='images/minus.gif' class='statusicon' />"], //Additional HTML added to the header when it's collapsed and expanded, respectively  ["position", "html1", "html2"] (see docs)
	animatespeed: "fast", //speed of animation: integer in milliseconds (ie: 200), or keywords "fast", "normal", or "slow"
	oninit:function(headers, expandedindices){ //custom code to run when headers have initalized
		//do nothing
	},
	onopenclose:function(header, index, state, isuseractivated){ //custom code to run whenever a header is opened or closed
		//do nothing
	}
});
</script>

<script type="text/javascript" src="js/jconfirmaction.jquery.js"></script>

<script type="text/javascript">
	
	$(document).ready(function() {
		$('.ask').jConfirmAction();
	});
	
</script>
<script type="text/javascript" src="js/user.js"></script>

</head>
<body>
<div id="main_container">

	<%@ include file="inc/header.jsp"%>
    
    <div class="main_content">
    
		<%@ include file="inc/menu.jsp"%>
                                                                               
    	<div class="center_content">          
	    	<div class="right_content">                   
    			<h2><s:text name="userList.title" /></h2>
    			<div class="sidebar_search"> 
	                <form id="user-search-form" class="default-form user-search" action="userList.action" method="post">
						<input type="hidden" value="<s:property value="searchType"/>" name="searchType"/>
	                	<input type="hidden" value="<s:property value="searchText"/>" name="searchText"/>
	                	<input type="hidden" value="<s:property value="pageNum"/>" name="pageNum"/>
	                	<input type="hidden" value="<s:property value="pageTotal"/>" name="pageTotal"/>
	                	<input type="hidden" value="<s:property value="pageSize"/>" name="pageSize"/>
	                	
						<input type="text" id="user-search-text" class="search_input" value="<s:property value="searchText"/>"/>
		
						<select name="user-search-type" style="display:none">
								<option id="user-search-name" selected="selected"><s:text name="userList.byname" /></option>
						</select>
						
						<a href="#" id="searchBtn"><img class="searchImg" src="images/search.png" /></a>
	
					</form>    
                </div>
                
                <form id="user-delete-form" action="usersDelete.action" method="post">    
					<table id="rounded-corner" class="userTable" summary="2007 Major IT Companies' Profit">
					    <thead>
					    	<tr>
					        	<th scope="col" class="rounded-company"><input type="checkbox" class="checkbox toggle"></th>
					            <th scope="col" class="rounded"><s:text name="userList.username" /></th>
					            <th scope="col" class="rounded"><s:text name="userList.email" /></th>
					            <th scope="col" class="rounded"><s:text name="userList.phone" /></th>
					            <th scope="col" class="rounded"><s:text name="userList.edit" /></th>
					            <th scope="col" class="rounded"><s:text name="userList.delete" /></th>
					            <th scope="col" class="rounded-q4"><s:text name="userList.upload" /></th>
					        </tr>
					    </thead>
					
					    <tbody>
					    	<s:iterator value="users">
						    	<tr>
						        	<td><input type="checkbox" class="checkbox" name="userIds" value=<s:property value="id"/> /></td>
								 	<td><a href="projectList.action?searchText=<s:property value="id"/>&searchType=accurateUserId"><s:property value="name"/></a></td>
									
									<td><s:property value="email"/></td>
									<td><s:property value="mobile"/></td>
						
						            <td><a href="<%=basePath%>/web/manager/inUserEdit.action?userId=${id}"><img src="images/user_edit.png" alt="" title="" border="0" /></a></td>
						            <td><a href="<%=basePath%>/web/manager/usersDelete.action?userIds=${id}" class="ask"><img src="images/trash.png" alt="" title="" border="0" /></a></td>
						            <td><a href="<%=basePath%>/web/manager/inFileUpload.action?userId=${id}"><img src="images/upload.png" alt="" title="" border="0" /></a></td>
						        </tr>    
					        </s:iterator>
					    </tbody>
					</table>
								

				 <a href="<%=basePath%>/web/manager/userNew.jsp" class="bt_green"><span class="bt_green_lft"></span><strong><s:text name="userList.addUser" /></strong><span class="bt_green_r"></span></a>
			     <a href="#" class="bt_red" id="deleteAllBtn"><span class="bt_red_lft"></span><strong><s:text name="userList.deleteUsers" /></strong><span class="bt_red_r"></span></a> 
                 
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