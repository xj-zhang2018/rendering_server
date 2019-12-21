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
<meta charset="utf-8" />
<title>集群渲染管理平台</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/button.css" />
<link rel="stylesheet" type="text/css" href="css/jobList.css" />
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
<script type="text/javascript" src="js/calculate.js"></script>
<script type="text/javascript" src="js/base.js"></script>
<script type="text/javascript" src="js/bPopup.js"></script>

</head>
<body>
<div id="main_container">

	<%@ include file="inc/header.jsp"%>
    
    <div class="main_content">
   
    	<s:if test="errorType == 'success'">
    		<div class="msg msg-success">
    			<h2><s:text name="operateSuccess" /></h2>
    			<h5><s:property value="errorCode" /></h5>
    		</div>
    	</s:if>
    
		<%@ include file="inc/menu.jsp"%>
                                                                               
    	<div class="center_content">
    	
    		<div class="left_content">
    			<div class="sidebar_search"> 
	                <form id="calculate-search-form" class="default-form calculate-search" action="xmlAnalyzeList.action" method="post">
						<input type="hidden" value="<s:property value="searchType"/>" name="searchType"/>
	                	<input type="hidden" value="<s:property value="searchText"/>" name="searchText"/>
	                	<input type="hidden" value="<s:property value="pageNum"/>" name="pageNum"/>
	                	<input type="hidden" value="<s:property value="pageTotal"/>" name="pageTotal"/>
	                	<input type="hidden" value="<s:property value="pageSize"/>" name="pageSize"/>
	                	<%-- <input  type="hidden" value="<s:property value="xmlStatus"/>" name="xmlStatus"/> --%>
	                	
	                	
	                	
	                	<s:if test="searchType == 'accurateCalculateId'">
							<input type="text" id="calculate-search-text" class="search_input" value=""/>
						</s:if>
						<s:else>
							<input type="text" id="calculate-search-text" class="search_input" value="<s:property value="searchText"/>"/>
						</s:else>
		
						<select name="calculate-search-type" style="display:none">
								<option id="calculate-search-name" selected="selected"><s:text name="jobList.byname" /></option>
						</select>
						
						<a href="#" id="searchBtn"><img class="searchImg" src="images/search.png" /></a>
	
					</form>    
                </div>
                <!-- 原作业管理页面上左边的作业列表，点击显示正在运行，未开始，已完成等等 -->
               <%--  <div class="sidebarmenu">           
	                <a class="menuitem submenuheader" href=""><s:text name="jobList.title" /></a>
	                <div class="submenu">
	                    <ul>
	                    <s:if test="subMenuName == 'allJobs'">
	                    	<li><a class="current" href="jobList.action?subMenuName=allJobs?&searchText=<s:property value="searchText"/>&searchType=<s:property value="searchType"/>"><s:text name="jobList.all" /></a></li>
	                    </s:if>	                    	
	                    <s:else>
	                    	<li><a href="jobList.action?subMenuName=allJobs&searchText=<s:property value="searchText"/>&searchType=<s:property value="searchType"/>"><s:text name="jobList.all" /></a></li>
	                    </s:else>	
	                    <s:if test="subMenuName == 'runingJobs'">
	                    	<li><a class="current" href="jobList.action?jobStatus=2&subMenuName=runingJobs&searchText=<s:property value="searchText"/>&searchType=<s:property value="searchType"/>"><s:text name="jobList.running" /></a></li>
	                    </s:if>
	                    <s:else>
	                    	<li><a href="jobList.action?jobStatus=2&subMenuName=runingJobs&searchText=<s:property value="searchText"/>&searchType=<s:property value="searchType"/>"><s:text name="jobList.running" /></a></li>
	                    </s:else>
	                    <s:if test="subMenuName == 'inQueueJobs'">
	                    	<li><a class="current" href="jobList.action?jobStatus=1&subMenuName=inQueueJobs&searchText=<s:property value="searchText"/>&searchType=<s:property value="searchType"/>"><s:text name="jobList.queue" /></a></li>
	                    </s:if>
	                    <s:else>
	                    	<li><a href="jobList.action?jobStatus=1&subMenuName=inQueueJobs&searchText=<s:property value="searchText"/>&searchType=<s:property value="searchType"/>"><s:text name="jobList.queue" /></a></li>
	                    </s:else>
	                    <s:if test="subMenuName == 'notStartJobs'">
	                    	<li><a class="current" href="jobList.action?jobStatus=0&subMenuName=notStartJobs&searchText=<s:property value="searchText"/>&searchType=<s:property value="searchType"/>"><s:text name="jobList.notstart" /></a></li>
	                    </s:if>
	                    <s:else>
	                    	<li><a href="jobList.action?jobStatus=0&subMenuName=notStartJobs&searchText=<s:property value="searchText"/>&searchType=<s:property value="searchType"/>"><s:text name="jobList.notstart" /></a></li>
	                    </s:else>
	                    <s:if test="subMenuName == 'finishedJobs'">
	                    	<li><a class="current" href="jobList.action?jobStatus=3&subMenuName=finishedJobs&searchText=<s:property value="searchText"/>&searchType=<s:property value="searchType"/>"><s:text name="jobList.finished" /></a></li>
	                    </s:if>
	                    <s:else>
	                    	<li><a href="jobList.action?jobStatus=3&subMenuName=finishedJobs&searchText=<s:property value="searchText"/>&searchType=<s:property value="searchType"/>"><s:text name="jobList.finished" /></a></li>
	                    </s:else>
	                    
	                    </ul>
	                </div>
                </div> --%>
    	    </div>     
    	    
    	    
    	    
    	    
	    	<div class="right_content">                   
    			<%-- <h2>
    			<!-- 工程列表，作业管理页面上点击直接跳到工程管理界面上 -->
    			<a href="projectList.action?searchText=<s:property value="currentProject.user.id"/>&searchType=accurateUserId"><s:property value="currentProject.user.name"/><s:text name="jobList.projectList" /></a>
    			&gt;<s:property value="currentProject.name"/><s:text name="jobList.jobList" /></h2> --%>
    			
    			<!-- 这里需要填写一个action -->
                <form id="calculate-manager-form" action="doCalculateDelete.action" method="post">
                
					<!--subMenuName在JobManagerAction中代表子菜单名称  -->
                	<%-- <input type="hidden" name="subMenuName" value="<s:property value="subMenuName"/>"/>  --%>
					
					<table id="rounded-corner" class="calculateTable" summary="2007 Major IT Companies' Profit">
					    <thead>
					    	<tr>
					        	<th scope="col" class="rounded-company"><input type="checkbox" class="checkbox toggle"></th>
					            <th scope="col" class="rounded"><s:text name="xmlAnalyze.fileName" /></th>
					             <!--<th scope="col" class="rounded"><s:text name="jobList.filepath" /></th>--> 
					       		 <th scope="col" class="rounded"><s:text name="xmlAnalyze.createTime" /></th>
					            <th scope="col" class="rounded"><s:text name="xmlAnalyze.statue" /></th>
					            <th scope="col" class="rounded"><s:text name="xmlAnalyze.progress" /></th>
					            
					            <!-- 这里是优先级 -->
					           <%--  <th scope="col" class="rounded"><s:text name="xmlAnalyze.priority" /></th> --%>
					            <%-- <th scope="col" class="rounded"><s:text name="jobList.result" /></th> --%>
					            
					              <%-- <th scope="col" class="rounded"><s:text name="jobList.preResult" /></th> --%>
					              
					            <th scope="col" class="rounded"><s:text name="jobList.edit" /></th>
					            <th scope="col" class="rounded"><s:text name="jobList.delete" /></th>
					             <%-- <th scope="col" class="rounded"><s:text name="jobList.payMoney" /></th>
					              <th scope="col" class="rounded"><s:text name="jobList.payMoneyStatus" /></th> --%>
					            <!--    <th scope="col" class="rounded"><s:text name="jobList.jobStartTime" /></th>
					              <th scope="col" class="rounded"><s:text name="jobList.jobEndTime" /></th>-->
					              
					              <!-- 这里是置顶功能 -->
					           <%--  <th scope="col" class="rounded-q4"><s:text name="jobList.top" /></th> --%>
					        </tr>
					    </thead>
					
					    <tbody>
					       <s:iterator value="calculateInfomas">
						    	<tr>
						        	<td><input type="checkbox" class="checkbox" name="xmlIds" value=<s:property value="id"/> /></td>
									<td>
									<s:property value="xmlName"/>
										<%-- <a href="frameList.action?searchText=<s:property value="id"/>&searchType=accurateJobId"   id="timeInfo" title=<s:property value="timeInfo" escape="false"/>></a> --%> 
									</td>
									<!--<td><s:property value="filePath"/></td> -->
									
									<td><s:property value="xmlCreateTime"/></td>
									
									 <td>
										<s:if test="xmlStatus==4"><s:text name="jobList.inSuspend" /></s:if>
										<s:if test="xmlStatus==3"><s:text name="jobList.finished" /></s:if>
										<s:if test="xmlStatus==2"><s:text name="jobList.running" /></s:if>
										<s:if test="xmlStatus==1"><s:text name="jobList.queue" /></s:if>
										<s:if test="xmlStatus==0"><s:text name="jobList.notstart" /></s:if>
									</td>
									
									<td><s:property value="xmlProgress"/>%</td>
									
									<!-- 这里是优先级 -->
									<%-- <td>
										<select class="calculate-priority">
											<s:if test="xmlPriority == 3">
  												<option value ="3" selected="selected"><s:text name="jobList.high" /></option>
  												<option value ="2"><s:text name="jobList.medium" /></option>
  												<option value="1"><s:text name="jobList.low" /></option>
  											</s:if>
  											<s:if test="xmlPriority == 2">
  												<option value ="3"><s:text name="jobList.high" /></option>
  												<option value ="2" selected="selected"><s:text name="jobList.medium" /></option>
  												<option value="1"><s:text name="jobList.low" /></option>
  											</s:if>
  											<s:if test="xmlPriority == 1">
  												<option value ="3"><s:text name="jobList.high" /></option>
  												<option value ="2"><s:text name="jobList.medium" /></option>
  												<option value="1" selected="selected"><s:text name="jobList.low" /></option>
  											</s:if>
										</select>
									</td> --%>
									<!--下载渲染结果图片 -->
									<%-- <td><a href="<%=basePath%>/web/manager/downloadRenderResult.action?jobId=${id}"><img src="images/photo.png" alt="" title="" border="0" /></a></td> --%>
						          
						         <!-- 下载预渲染图片结果 -->
						         <%--  <s:if test='#jobInfo.isPreRender==1'>
  								<td><a href="<%=basePath%>/web/manager/downloadPreRenderResult.action?jobId=${id}"><img src="images/photo.png" alt="" title="" border="0" /></a></td>
  								</s:if>
  									 <s:else>
                                 <td><p></p></td>
                                </s:else>	 --%>	
  											
						          	<!-- 修改 -->
						            <%-- <td><a href="<%=basePath%>/web/manager/inJobEdit.action?jobId=${id}&currentProject.id=<s:property value="currentProject.id" />"><img src="images/user_edit.png" alt="" title="" border="0" /></a></td> --%>						       					        
						            <!-- 这里的action后面需要传入一个参数，根据参数修改 -->
						            <td><a href="<%=basePath%>/web/manager/inCalculateEdit.action?xmlId=${id}&currentJob.id=<s:property value="currentJob.id" />"><img src="images/user_edit.png" alt="" title="" border="0" /></a></td>
						           
						            <!-- 删除 -->
						            <%-- <td><a href="<%=basePath%>/web/manager/doJobsDelete.action?jobIds=${id}&subMenuName=<s:property value="subMenuName"/>" class="ask"><img src="images/trash.png" alt="" title="" border="0" /></a></td> --%>
						            <td><a href="<%=basePath%>/web/manager/doCalculateDelete.action?xmlIds=${id}&currentUser.id=<s:property value="currentUser.id" />" class="ask"><img src="images/trash.png" alt="" title="" border="0" /></a></td>
						         
						          <%--  <td>
						           		<s:if test="jobStatus==2"><s:text name="计费中" /></s:if>
										<s:if test="jobStatus==3"><s:property value="renderCost"/></s:if>
						           </td>
						           <td>
						           		<s:if test="jobStatus==2"><s:text name="未交费" /></s:if>
										<s:if test="jobStatus==3">		
												<s:if test="paymentStatus==0"><s:text name="未交费" /></s:if>
												<s:if test="paymentStatus==1"><s:text name="余额不总" /></s:if>
												<s:if test="paymentStatus==2"><s:text name="已缴费" /></s:if>
										</s:if>						          
						           </td> --%>
						         <!--  <td><s:property value="startTime"/></td>
						           <td>
						           		<s:if test="jobStatus==3"><s:property value="endTime"/></s:if>
						           		<s:else> <s:text name="未结束"/></s:else>
						           </td> --> 
						           
						           
						           <!-- 这里需要建一个xmlToTop的action -->
						           <%--  <s:if test="xmlStatus==1">
						            	<td><a href="<%=basePath%>/web/manager/xmlToTop.action?xmlId=${id}"><img src="images/toTop.png" alt="" title="" border="0" /></a></td>
						            </s:if>
						            <s:else>
						            	<td><img src="images/toTop.png" alt="" title="" border="0" class="disabled"/></td>
						            </s:else> --%>
						            
						            						            
						            
						        </tr>    
					        </s:iterator>
					    </tbody>
					</table>


				 <!-- 添加新的作业-----添加新的xml解析作业 -->
				 <a href="xmlAnalyzeNew.action?jobId=<s:property value="currentJob.id"/>" class="bt_green"><span class="bt_green_lft"></span><strong><s:text name="xmlAnalyze.add" /></strong><span class="bt_green_r"></span></a>
				  <a href="doCalculateDeleteAll.action" class="bt_green"><span class="bt_green_lft"></span><strong><s:text name="jobList.deleAll" /></strong><span class="bt_green_r"></span></a>
			     
			     <a href="#" class="bt_red" id="deleteAllBtn"><span class="bt_red_lft"></span><strong><s:text name="jobList.deleteSelect" /></strong><span class="bt_red_r"></span></a>			     
			    
			     
			     <a href="#" class="medium awesome" id="beginAllBtn" title="请选择未开始的作业"><strong><s:text name="jobList.start" />  »</a>
			     <a href="#" class="medium magenta awesome" id="stopAllBtn" title="停止后作业只能重新渲染"><strong><s:text name="jobList.stop" />  »</a>
			     <a href="#" class="medium red awesome" id="suspendAllBtn" title="暂停后可以点击继续按钮来继续渲染作业"><strong><s:text name="jobList.suspend" />  »</a>	
			     <a href="#" class="medium orange awesome" id="continueAllBtn"><s:text name="jobList.continue" />  »</a>
			     <a href="#" class="medium yellow awesome" id="copyAllBtn"><s:text name="jobList.copy" />  »</a> 
			     		     	     			     
               </form>
     
		        <div class="pagination">
		        	
		        </div> 
                        
     		</div><!-- end of right content-->
                    
  		</div>   <!--end of center content -->               
                                              
    	<div class="clear"></div>
    </div> <!--end of main content-->
	   
	<%@ include file="inc/footer.jsp"%>

<div class="beginSuccess begin-success popup-box success">
	<span class="bclose popup-close"><s:text name="jobList.close" /></span>

	<p class="tips-t"><s:text name="jobList.jobInQueue" /> <i class="icon-tips-success"></i></p>
    <p class="tips-c"><s:text name="jobList.sendEmail" /></p>
    <p class="tips-btn"><a class="bclose popup-btn" href="#"><s:text name="jobList.enter" /></a></p>    
    
</div>

<div class="stopSuccess begin-success popup-box success">
	<span class="bclose popup-close"><s:text name="jobList.close" /></span>

	<p class="tips-t"><s:text name="jobList.jobStopped" /> <i class="icon-tips-success"></i></p>
    <p class="tips-c"><s:text name="jobList.reStartRendering" /></p>
    <p class="tips-btn"><a class="bclose popup-btn" href="#"><s:text name="jobList.enter" /></a></p>    
    
</div>

<div class="suspendSuccess begin-success popup-box success">
	<span class="bclose popup-close"><s:text name="jobList.close" /></span>

	<p class="tips-t"><s:text name="jobList.jobInSuspend" /> <i class="icon-tips-success"></i></p>
    <p class="tips-c"><s:text name="jobList.suspendJobCanContinue" /></p>
    <p class="tips-btn"><a class="bclose popup-btn" href="#"><s:text name="jobList.enter" /></a></p>    
    
</div>

<div class="continueSuccess begin-success popup-box success">
	<span class="bclose popup-close"><s:text name="jobList.close" /></span>

	<p class="tips-t"><s:text name="jobList.jobInQueue" /> <i class="icon-tips-success"></i></p>
    <p class="tips-c"><s:text name="jobList.jobToContinue" /></p>
    <p class="tips-btn"><a class="bclose popup-btn" href="#"><s:text name="jobList.enter" /></a></p>    
    
</div>

<div class="copySuccess begin-success popup-box success">
	<span class="bclose popup-close"><s:text name="jobList.close" /></span>

	<p class="tips-t"><s:text name="jobList.jobInCopy" /> <i class="icon-tips-success"></i></p>
    <p class="tips-c"><s:text name="jobList.jobToCopy" /></p>
    <p class="tips-btn"><a class="bclose popup-btn" href="#"><s:text name="jobList.enter" /></a></p>    
    
</div>

<div class="begin-success popup-box fail">
	<span class="bclose popup-close"><s:text name="jobList.close" /></span>
	<p class="tips-t"><s:text name="jobList.requestFail" /><i class="icon-tips-fail"></i></p>
    <p class="tips-c"><s:text name="jobList.netFailed" /></p>
    <p class="tips-btn"><a class="bclose popup-btn" href="#"><s:text name="jobList.return" /></a></p>    
    
</div>
</div>
<script>
	
	window.onload=function(){window.setInterval(checkStatus,60000);};

     function checkStatus(){
		$.ajax({
			type: "get",
            dataType: "json",
            cache:false,
            url: 'jobProgress.action',
            data: {searchType:$('[name=searchType]').val(),searchText:$('[name=searchText]').val(),pageNum:$('[name=pageNum]').val()},
            success: function(msg){
            	var data = msg.cameraProgressInfos;
            	$("#rounded-corner tr").each(function(){
            		var trID=$(this).find("td").eq(0).find("input").val();
            		var trProgress=null;
            		var trStatus="";
            		var trCost="";
            		var trPaymentStatus="";
            		var trTimeInfo="";
            		

            		$.each(data, function(i, n){
     			
            			var jobId = n.cameraId;
            			var jobProgress = n.cameraProgress;
            			var cameraStatus = n.cameraStatus;
            			var renderCost=n.renderCost;
            			var paymentStatus=n.paymentStatus;
            			var timeInfo=n.timeInfo
            			
            			
            									
            			if(trID==jobId){
            				trProgress=jobProgress;
            				trStatus=cameraStatus;
            				trCost=renderCost;
            				trPaymentStatus=paymentStatus;
            				trTimeInfo=timeInfo
            				
            				return false;
            			}
            		});
            		if(trProgress!=null){
            			           			
            		    $(this).find("td").eq(4).text(trProgress+"%");	
            			if(trStatus=="1"){
            			    $(this).find("td").eq(3).text("排队中");
            			}
            			else if(trStatus=="2"){
            			     $(this).find("td").eq(3).text("正在运行");
            			     $(this).find("td").eq(9).text("计费中");
            			     $(this).find("td").eq(10).text("未交费");
            			}
            			else if(trStatus=="3"){
            			     $(this).find("td").eq(3).text("已完成");
            			     $(this).find("td").eq(9).text(trCost+"元");
            			     $(this).find("td").eq(1).attr({ "title" : trTimeInfo });
            			         		 
            			     if(trPaymentStatus=="2"){
            			      	$(this).find("td").eq(10).text("已缴费");
           			      	}
           			      	else if (trPaymentStatus=="1"){
           			      		$(this).find("td").eq(10).text("余额不足");
           			      	}
           			      	else if (trPaymentStatus=="0"){
           			      		$(this).find("td").eq(10).text("未交费");
           			      	}
            			     
            			}
            			else if(trStatus=="4"){
            			     $(this).find("td").eq(3).text("暂停中");
            			     
            			}   				
            		}            		
            	});          	
            }
		});		
	};
	
	$('#beginAllBtn').bind('click', function(e) {
	
		if($(":checkbox[name=xmlIds]:checked").size()==0){
			alert('请选择您要开始的作业!'); 
			return false;
		}
		
		var success=true;
		$(":checkbox[name=xmlIds]:checked").each(
			function(){
				if($(this).parent().parent().find("td").eq(3).text().trim()!='未开始'){
					alert('只能启动未开始的作业!');
					success=false; 
					return false;
				}
		});
		if(success==false) return false;
		
		e.preventDefault();

		$.ajax({
			url: 'doCalculateBegin.action',
			type: 'POST',
			data:$('#calculate-manager-form').serialize(),
			error: function(){		
				$('.popup-box.fail').bPopup({
				closeClass: 'bclose'
				});
			},
			success: function(){
				$('.beginSuccess').bPopup({
					closeClass: 'bclose'
				});
				
				$(":checkbox[name=xmlIds]:checked").each(function(){$(this).parent().parent().find("td").eq(3).text("排队中");});
			}
		});	
		
	});
	
	$('#stopAllBtn').bind('click', function(e) {
	
		if($(":checkbox[name=xmlIds]:checked").size()==0){
			alert('请选择您要停止的作业!'); 
			return false;
		}
		e.preventDefault();
		$.ajax({
			url: 'doCalculateStop.action',
			type: 'POST',
			data:$('#calculate-manager-form').serialize(),
			error: function(){		
				$('.popup-box.fail').bPopup({
				closeClass: 'bclose'
				});
			},
			success: function(){
				$('.stopSuccess').bPopup({
					closeClass: 'bclose'
				});
				
				$(":checkbox[name=xmlIds]:checked").each(function(){$(this).parent().parent().find("td").eq(3).text("未开始");
																	$(this).parent().parent().find("td").eq(4).text("0%");	
																});
			}
		});	
		
	});
	
	$('#suspendAllBtn').bind('click', function(e) {
	
		if($(":checkbox[name=xmlIds]:checked").size()==0){
			alert('请选择您要暂停的作业!'); 
			return false;
		}
		
		var success=true;
		$(":checkbox[name=xmlIds]:checked").each(
			function(){
				if($(this).parent().parent().find("td").eq(3).text().trim()!='正在运行'){
					alert('只能暂停正在运行的作业!');
					success=false; 
					return false;
				}
		});
		if(success==false) return false;
		
		e.preventDefault();

		$.ajax({
			url: 'doJobSuspend.action',
			type: 'POST',
			data:$('#calculate-manager-form').serialize(),
			error: function(){		
				$('.popup-box.fail').bPopup({
				closeClass: 'bclose'
				});
			},
			success: function(){
				$('.suspendSuccess').bPopup({
					closeClass: 'bclose'
				});
				
				$(":checkbox[name=xmlIds]:checked").each(function(){$(this).parent().parent().find("td").eq(3).text("暂停中");});
			}
		});	
		
	});
	
	$('#continueAllBtn').bind('click', function(e) {
	
		if($(":checkbox[name=xmlIds]:checked").size()==0){
			alert('请选择您要继续的作业!'); 
			return false;
		}
		
		e.preventDefault();

		$.ajax({
			url: 'doJobContinue.action',
			type: 'POST',
			data:$('#calculate-manager-form').serialize(),
			error: function(){		
				$('.popup-box.fail').bPopup({
				closeClass: 'bclose'
				});
			},
			success: function(){
				$('.continueSuccess').bPopup({
					closeClass: 'bclose'
				});
				
				$(":checkbox[name=xmlIds]:checked").each(function(){$(this).parent().parent().find("td").eq(3).text("排队中");});
			}
		});	
		
	});
	
	$('#copyAllBtn').bind('click', function(e) {
	
		if($(":checkbox[name=xmlIds]:checked").size()==0){
			alert('请选择您要复制的作业!'); 
			return false;
		}
		
		e.preventDefault();

		$.ajax({
			url: 'doCalculateCopy.action',
			type: 'POST',
			data:$('#calculate-manager-form').serialize(),
			error: function(){		
				$('.popup-box.fail').bPopup({
				closeClass: 'bclose'
				});
			},
			success: function(){
			
				$('.copySuccess').bPopup({
					onClose: function(){window.location.href="xmlAnalyzeList.action";	},
					closeClass: 'bclose'										
				});						
			}
		});	
		
	});
	

</script>		
</body>
</html>