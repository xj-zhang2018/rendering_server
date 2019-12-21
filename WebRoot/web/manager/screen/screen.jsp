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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%-- <link href="<%=basePath%>/display/css/base.css" rel="stylesheet" type="text/css" /> --%>
<%-- <link href="<%=basePath%>/display/css/schedule.css" rel="stylesheet" type="text/css" /> --%>
<%-- <script src="<%=basePath%>/display/js/jquery-1.8.2.min.js" type="text/javascript"></script> --%>
<%-- <script src="<%=basePath%>/display/js/hashtable.js" type="text/javascript"></script> --%>
<%-- <script src="<%=basePath%>/display/js/schedule.js" type="text/javascript"></script> --%>
</head>
<body class="Schedule-Page" >

<div id="content" style="backgroud:#FFF">
	 
<!--     <div id="schdl-hd"> -->
<%--     	<h2 class="intru-name"><s:property value="instrument.name"/></h2>  --%>
       
<!--     </div> -->

    <div id="schdl-bd">         
         <div class="cal-table-bd-wrap  user-allDayUse" -style="overflow:hidden; height:270px; position:relative; border-bottom:1px solid #c9cbcc;">
         <!--   user-readOnly/user-workTimeUse/user-allDayUse/user-instrManager -->
         <table class="cal-tab-bd" -style="margin-top:-255px;">
			<tbody>
				<tr>
					<s:iterator value="llnodesInfos" id="nodesInfos">
						<td>
	                    	<div class="tdwrap">
	                    		<s:iterator value="#nodesInfos" id="nodesInfo" status="status">
	                    			<s:if test="#nodesInfo.state == 'free' || #nodesInfo.state == 'idle'">
			                    		<div class="event manager-resv" style="height:99px;top:<s:property value="#status.index*100+1"/>px;">
											<div class="event-out-wrap" style="height:99px;">
												<div class="event-wrap">
													<div class="event-bd">
														<div class="resv-time"><s:property value="#nodesInfo.name"/></div>
														<div class="resv-type">			                                				                                				                                				                                				                                				                                	
														</div>                                
														<div class="resv-user-t">
															<p class="memory">
																<i class="icon">memory:</i>
																<span class="number"><s:property value="#nodesInfo.nodesAvailmem"/></span>
															</p>
															<p class="processor">
																<i class="icon">processor:</i>
																<span class="number"><s:property value="#nodesInfo.nodesNcpus"/></span>
															</p>
														 </div>
													</div>
													<!-- /event-bd -->
			 
												</div>
												<!-- /event-wrap -->
											</div>
											<!-- event-out-wrap -->    
		                        		</div>
	                        			<!-- event -->
	                        		</s:if>
	                        		<s:elseif test="#nodesInfo.state == 'busy'">
			                    		<div class="event my-resv" style="height:99px;top:<s:property value="#status.index*100+1"/>px;">
											<div class="event-out-wrap" style="height:99px;">
												<div class="event-wrap">
													<div class="event-bd">
														<div class="resv-time"><s:property value="#nodesInfo.name"/></div>
														<div class="resv-type">			                                				                                				                                				                                				                                				                                	
														</div>                                
														<div class="resv-user-t">
															<p class="memory">
																<i class="icon">memory:</i>
																<span class="number"><s:property value="#nodesInfo.nodesAvailmem"/></span>
															</p>
															<p class="processor">
																<i class="icon">processor:</i>
																<span class="number"><s:property value="#nodesInfo.nodesNcpus"/></span>
															</p>
														 </div>
													</div>
													<!-- /event-bd -->
			 
												</div>
												<!-- /event-wrap -->
											</div>
											<!-- event-out-wrap -->    
		                        		</div>
	                        			<!-- event -->
	                        		</s:elseif>
	                    			<s:else>
			                    		<div class="event user-resv" style="height:99px;top:<s:property value="#status.index*100+1"/>px;">
											<div class="event-out-wrap" style="height:99px;">
												<div class="event-wrap">
													<div class="event-bd">
														<div class="resv-time"><s:property value="#nodesInfo.name"/></div>
														<div class="resv-type">			                                				                                				                                				                                				                                				                                	
														</div>                                
														<div class="resv-user-t">
														 </div>
													</div>
													<!-- /event-bd -->
			 
												</div>
												<!-- /event-wrap -->
											</div>
											<!-- event-out-wrap -->    
		                        		</div>
	                        			<!-- event -->
	                        		</s:else>	                        		
	                    		</s:iterator>
	                    	</div>
                    		<!-- /tdwrap -->
                    	</td>
					</s:iterator>	                    	                       	 										               
				</tr>
			</tbody>        
        </table>
        <!-- /cal-tab-bd -->
        </div>
        <!-- /cal-tab-bd-wrap -->
                        

    </div>
    <!-- /#schdl-bd -->


</div>
<!-- /#content -->
 
</body>
</html>
