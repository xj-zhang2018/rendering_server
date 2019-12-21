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
<script language="javascript" type="text/javascript" src="js/niceforms.js"></script>
<script type="text/javascript" src="js/unit.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="css/niceforms-default.css" />

</head>
<body>
<div id="main_container">

	<%@ include file="inc/header.jsp"%>
    
    <div class="main_content">
    
		<%@ include file="inc/menu.jsp"%>
                       
                                                                           
    	<div class="center_content">          
    	
	    	<div class="left_content">                   
    	
    			<div class="sidebar_search"> 
	                <form id="unit-search-form" class="default-form unit-search" action="unitList.action" method="post">
						<input type="hidden" value="<s:property value="searchType"/>" name="searchType"/>
	                	<input type="hidden" value="<s:property value="searchText"/>" name="searchText"/>
	                	<input type="hidden" value="<s:property value="pageNum"/>" name="pageNum"/>
	                	<input type="hidden" value="<s:property value="pageTotal"/>" name="pageTotal"/>
	                	<input type="hidden" value="<s:property value="pageSize"/>" name="pageSize"/>
	                	<input  type="hidden" value="<s:property value="unitStatus"/>" name="unitStatus"/>
	                	
	                	
						<input type="text" id="unit-search-text" class="search_input" value="<s:property value="searchText"/>"/>
		
						<select name="unit-search-type" style="display:none">
								<option id="unit-search-name" selected="selected">按单元名</option>
						</select>
						
						<a href="#" id="searchBtn"><img class="searchImg" src="images/search.png" /></a>
	
					</form>    
                </div>
                
                 <div class="sidebarmenu">           
	                <a class="menuitem submenuheader" href="">单元列表</a>
	                <div class="submenu">
	                    <ul>
	                    <li><a href="unitList.action">所有</a></li>
	                    <li><a href="unitList.action?unitStatus=3">空闲</a></li>
	                    <li><a href="unitList.action?unitStatus=1">忙碌</a></li>
	                    <li><a href="unitList.action?unitStatus=2">已销毁</a></li>
	                    <li><a href="unitList.action?unitStatus=-1">不可用</a></li>
	                    </ul>
	                </div>
                </div>
    	    </div>     
    	    
    	    <div class="right_content">                   
    			<h2>单元列表</h2>			
                <form id="unit-delete-form" action="unitsDelete.action" method="post">   
                
					<table id="rounded-corner" class="unitTable" summary="2007 Major IT Companies' Profit">
					    <thead>
					    	<tr>
					        	<th scope="col" class="rounded-company"><input type="checkbox" class="checkbox toggle"></th>
					            <th scope="col" class="rounded">单元主节点名</th>
					            <th scope="col" class="rounded">单元中节点信息</th>
					            <th scope="col" class="rounded">单元状态</th>
					            <th scope="col" class="rounded">当前正在渲染镜头</th>				        
					            <th scope="col" class="rounded-q4">注销</th>
					        </tr>
					    </thead>
					
					    <tbody>
					       <s:iterator value="units" >
						    	<tr>
						        	<td><input type="checkbox" class="checkbox" name="unitIds" value=<s:property value="id"/> /></td>
									<td><a href="unitNodesList.action?unitNodesInfo=<s:property value="unitNodesInfo"/>" ><s:property value="unitMasterName"/></a></td>
									<td><s:property value="unitNodesInfo"/></td>
									<td>
										<s:if test="unitStatus==2">已销毁</s:if>
										<s:if test="unitStatus==1">忙碌</s:if>
										<s:if test="unitStatus==0">空闲</s:if>
										<s:if test="unitStatus==-1">不可用</s:if>
										<s:if test="unitStatus==3">有空闲</s:if>
									</td>
									
									<td>
										<s:property value="tasks[0].job.cameraName" />
									</td>					       
						        
						            <td><a href="<%=basePath%>/web/manager/unitsDelete.action?unitIds=${id} " class="ask"><img src="images/trash.png" alt="" title="" border="0" /></a></td>
						        </tr>    
					        </s:iterator>
					    </tbody>
					</table>
				
        
			     <a href="#" class="bt_red" id="deleteAllBtn"><span class="bt_red_lft"></span><strong>注销所选</strong><span class="bt_red_r"></span></a> 
               </form>
     
		        <div class="pagination">
		        	
		        </div> 
                        
     		</div><!-- end of right content-->
                               
  		</div>   <!--end of center content -->               
                                              
    	<div class="clear"></div>
    </div> <!--end of main content-->
	   
	<%@ include file="inc/footer.jsp"%>

</div>	


<script>
	window.onload=function(){window.setInterval(checkStatus,5000);};

     function checkStatus(){
		$.ajax({
			type: "get",
            dataType: "json",
            url: 'unitStatus.action',
            data: {searchType:$('[name=searchType]').val(),searchText:$('[name=searchText]').val(),pageNum:$('[name=pageNum]').val()},
            success: function(msg){
            	var data = msg.units
            	$("#rounded-corner tr").each(function(){
            		var trID=$(this).find("td").eq(0).find("input").val();
            		var trStatus="";

            		$.each(data, function(i, n){
            			var unitId = n.id;

            			var unitStatus = n.unitStatus;
            			if(trID==unitId){
            				trStatus=unitStatus;
            				return false;
            			}
            		});
            		  
            			if(trStatus=='0'){
            			    $(this).find("td").eq(3).text("空闲");
            			}
            			else if(trStatus=='1'){
            			     $(this).find("td").eq(3).text("忙碌");
            			}
            			else if(trStatus=='2'){
            			     $(this).find("td").eq(3).text("已销毁");
            			}
            				
            		
            		
            	});
            	
            }
		});		

	};
</script>		
	
</body>
</html>