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
<script type="text/javascript" src="js/frame.js"></script>
<script type="text/javascript" src="js/progress.js"></script>

</head>
<body>
<div id="main_container">

	<%@ include file="inc/header.jsp"%>
    
    <div class="main_content">
    
		<%@ include file="inc/menu.jsp"%>
                                                                               
    	<div class="center_content">   
    	
    	<!-- 以下加左部列表查找帧 -->     
    	
    		<div class="left_content">                   
    	
    			<div class="sidebar_search"> 
	                <form id="frame-search-form" class="default-form frame-search" action="frameList.action" method="post">
						<input type="hidden" value="<s:property value="searchType"/>" name="searchType"/>
	                	<input type="hidden" value="<s:property value="searchText"/>" name="searchText"/>
	                	<input type="hidden" value="<s:property value="pageNum"/>" name="pageNum"/>
	                	<input type="hidden" value="<s:property value="pageTotal"/>" name="pageTotal"/>
	                	<input type="hidden" value="<s:property value="pageSize"/>" name="pageSize"/>
	                	<input  type="hidden" value="<s:property value="frameStatus"/>" name="frameStatus"/>
	                	
	                	
						<input type="text" id="frame-search-text" class="search_input" value="<s:property value="searchText"/>"/>
						
		
						<select name="frame-search-type" style="display:none">
								<option id="frame-search-name" selected="selected">按帧名</option>
						</select>
						
						<a href="#" id="searchBtn"><img class="searchImg" src="images/search.png" /></a>
	
					</form>    
                </div>
                
                 <div class="sidebarmenu">           
	                <a class="menuitem submenuheader" href="">帧列表</a>
	                <div class="submenu">
	                    <ul>
	                    <li><a href="frameList.action">所有</a></li>
	                    <li><a href="frameList.action?frameStatus=0">未开始</a></li>
	                    <li><a href="frameList.action?frameStatus=1">未完成</a></li>
	                    <li><a href="frameList.action?frameStatus=2">已完成</a></li>
	                    <li><a href="frameList.action?frameStatus=3">出错</a></li>
	                    </ul>
	                </div>
                </div>
    	    </div>     
    	    
    	    <!-- 以上加左部列表查找帧 -->     
    	    
    	      
	    	<div class="right_content">                   
    			<h2>帧列表</h2>
    			
                <form id="frame-delete-form" action="framesDelete.action" method="post">    
					<table id="rounded-corner" class="frameTable" summary="2007 Major IT Companies' Profit">
					    <thead>
					    	<tr>
					        	<th scope="col" class="rounded-company"><input type="checkbox" class="checkbox toggle"></th>
					            <th scope="col" class="rounded">帧名</th>
									
					            <th scope="col" class="rounded">进度</th>
					            <th scope="col" class="rounded">状态</th>
					        </tr>
					    </thead>
					
					    <tbody>
					    	<s:iterator value="frames">
						    	<tr>						        							            
						            <td><input type="checkbox" class="checkbox" name="frameIds" value=<s:property value="id"/> /></td>
						            <td><s:property value="frameName"/></td>
						             <s:if test="frameStatus!=3">
							             <td><script>display ('element1',<s:property value="frameProgress"/>,1);</script></td>
							             <td> 
							            	<s:if test="frameStatus==2"><s:text name="完成" /></s:if>
							            	<s:if test="frameStatus==0"><s:text name="未渲染" /></s:if>
							            	<s:if test="frameStatus==1"><s:text name="未完成" /></s:if>
							            </td>  
						             </s:if>
						              <s:if test="frameStatus==3">
							             <td><script>display ('element1',<s:property value="100"/>,2);</script></td>
							             <td> 						            	
							          		<s:text name="出错" />
							            </td>  
						             </s:if>
						             
						           

						        </tr>    
					        </s:iterator>
					    </tbody>
					</table>				              
     
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
	window.onload=function(){window.setInterval(checkStatus,20000);};

	function checkStatus(){
		$.ajax({
			type: "get",
            dataType: "json",
            url: 'frameProgress.action',
            data: {searchType:$('[name=searchType]').val(),searchText:$('[name=searchText]').val(),pageNum:$('[name=pageNum]').val()},
            success: function(msg){
            	var data = msg.frames;
            	$("#rounded-corner tr").each(function(){
            		var trID=$(this).find("td").eq(0).find("input").val();
            		var trProgress="";

            		$.each(data, function(i, n){
            			var frameId = n.id;

            			var frameProgress = n.frameProgress;
            			if(trID==frameId){
            				trProgress=frameProgress;
            				return false;
            			}
            		});
            		if(trProgress!=""){
            			$(this).find("td").eq(2).find("img").css({backgroundPosition:-120+1.2*trProgress+'px'+' 0'});
            			$(this).find("td").eq(2).find("span").text(trProgress+"%");
            		}
            		
            		
            	});
            	
            }
		});		

	};
</script>		
</body>
</html>