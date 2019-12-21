<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"  import="org.xjtu.framework.core.base.constant.SystemConfig"   %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
	    String ftpPath = "ftp://"+ request.getServerName() + ":21";	
	   
	   
	    SystemConfig systemConfig=new SystemConfig();
		String username=systemConfig.getSystemUserName();
		String password=systemConfig.getSystemPasswd();
	
	//String ftpPath = "ftp://root:bing1314@"+ request.getServerName() + ":21";	
	//String ftpPath = "ftp://"+username+":"+password+"@"+ request.getServerName() + ":21";	
	//String ftpPath = "ftp://"+username+":"+password+"@41.0.0.188:21";	
	
	//Note 3: FTP URL should looks like: ftp://username:password@myhost.com:21/directory
%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>集群渲染管理平台</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<script type="text/javascript" src="js/clockp.js"></script>
<script type="text/javascript" src="js/clockh.js"></script>
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
</head>
<body> 

<div id="main_container">
		
		<%@ include file="inc/header.jsp"%>

		<div class="main_content">

			<%@ include file="inc/menu.jsp"%>

			<div class="center_content">
				<div class="right_content">
					<table width="640" cellpadding="7" cellspacing="0" border="0">
					    <tr>
					        <td colspan="2" align="center"><font face="arial" size="+1" color="red">
					        	<b><u>场景文件上传应用</u></b>
					        	</font>
					        </td>
					    </tr>
					    <tr height="20"></tr>
					    <tr>
					        <td colspan="2" bgcolor="#3F7C98">
					        <center><font color="#FFFFFF">The applet :
					        default mode </font></center>
					        </td>
					    </tr>
					    <tr>
					        <td colspan="2" align="center">
					<!-- --------------------------------------------------------------------------------------------------- -->
					<!-- --------     A DUMMY APPLET, THAT ALLOWS THE NAVIGATOR TO CHECK THAT JAVA IS INSTALLED   ---------- -->
					<!-- --------               If no Java: Java installation is prompted to the user.            ---------- -->
					<!-- --------                                                                                 ---------- -->
					<!-- --------               THIS IS NOT THE JUpload APPLET TAG !   See below                  ---------- -->
					<!-- --------------------------------------------------------------------------------------------------- -->
					<!--"CONVERTED_APPLET"-->
					<!-- HTML CONVERTER -->
					<script language="JavaScript" type="text/javascript"><!--
					    var _info = navigator.userAgent;
					    var _ns = false;
					    var _ns6 = false;
					    var _ie = (_info.indexOf("MSIE") > 0 && _info.indexOf("Win") > 0 && _info.indexOf("Windows 3.1") < 0);
					//--></script>
					    <comment>
					        <script language="JavaScript" type="text/javascript"><!--
					        var _ns = (navigator.appName.indexOf("Netscape") >= 0 && ((_info.indexOf("Win") > 0 && _info.indexOf("Win16") < 0 && java.lang.System.getProperty("os.version").indexOf("3.5") < 0) || (_info.indexOf("Sun") > 0) || (_info.indexOf("Linux") > 0) || (_info.indexOf("AIX") > 0) || (_info.indexOf("OS/2") > 0) || (_info.indexOf("IRIX") > 0)));
					        var _ns6 = ((_ns == true) && (_info.indexOf("Mozilla/5") >= 0));
					//--></script>
					    </comment>
					
					<script language="JavaScript" type="text/javascript"><!--
					    if (_ie == true) document.writeln('<object classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" WIDTH = "0" HEIGHT = "0" NAME = "JUploadApplet"  codebase="http://java.sun.com/update/1.5.0/jinstall-1_5-windows-i586.cab#Version=5,0,0,3"><noembed><xmp>');
					    else if (_ns == true && _ns6 == false) document.writeln('<embed ' +
						    'type="application/x-java-applet;version=1.5" \
					            CODE = "wjhk.jupload2.EmptyApplet" \
					            ARCHIVE = "wjhk.jupload.jar" \
					            NAME = "JUploadApplet" \
					            WIDTH = "0" \
					            HEIGHT = "0" \
					            type ="application/x-java-applet;version=1.6" \
					            scriptable ="false" ' +
						    'scriptable=false ' +
						    'pluginspage="http://java.sun.com/products/plugin/index.html#download"><noembed><xmp>');
					//--></script>
					<applet  CODE = "wjhk.jupload2.EmptyApplet" ARCHIVE = "wjhk.jupload.jar" WIDTH = "0" HEIGHT = "0" NAME = "JUploadApplet"></xmp>
					    <PARAM NAME = CODE VALUE = "wjhk.jupload2.EmptyApplet" >
					    <PARAM NAME = ARCHIVE VALUE = "wjhk.jupload.jar" >
					    <PARAM NAME = NAME VALUE = "JUploadApplet" >
					    <param name="type" value="application/x-java-applet;version=1.5">
					    <param name="scriptable" value="false">
					    <PARAM NAME = "type" VALUE="application/x-java-applet;version=1.6">
					    <PARAM NAME = "scriptable" VALUE="false">
					</xmp>
					    
					Java 1.5 or higher plugin required.
					</applet>
					</noembed>
					</embed>
					</object>
					
					<!--
					<APPLET CODE = "wjhk.jupload2.EmptyApplet" ARCHIVE = "wjhk.jupload.jar" WIDTH = "0" HEIGHT = "0" NAME = "JUploadApplet">
						<PARAM NAME = "type" VALUE="application/x-java-applet;version=1.6">
						<PARAM NAME = "scriptable" VALUE="false">
						</xmp>
					Java 1.5 or higher plugin required.
					</APPLET>
					-->
					<!-- "END_CONVERTED_APPLET" -->
					<!-- ---------------------------------------------------------------------------------------------------- -->
					<!-- --------------------------     END OF THE DUMMY APPLET TAG            ------------------------------ -->
					<!-- ---------------------------------------------------------------------------------------------------- -->
					
					
					<!---------------------------------------------------------------------------------------------------------
					-------------------     A SIMPLE AND STANDARD APPLET TAG, to call the JUpload applet  --------------------- 
					----------------------------------------------------------------------------------------------------------->
					        <applet
						            code="wjhk.jupload2.JUploadApplet"
						            name="JUpload"
						            archive="applet/wjhk.jupload.jar,applet/jakarta-commons-oro.jar,applet/jakarta-commons-net.jar"
						            width="640"
						            height="300"
						            mayscript="true"
						            alt="The java pugin must be installed.">
					            <!-- param name="CODE"    value="wjhk.jupload2.JUploadApplet" / -->
					            <!-- param name="ARCHIVE" value="wjhk.jupload.jar" / -->
					            <!-- param name="type"    value="application/x-java-applet;version=1.5" /  -->
					            <param name="postURL" value="<%=ftpPath%><s:property value='existedUser.homeDir' />" />
					            <!-- Optionnal, see code comments -->
					            <param name="showLogWindow" value="false" />
					            <param name="ftpCreateDirectoryStructure" value="true" />
					            Java 1.5 or higher plugin required. 
					        </applet>
					<!-- --------------------------------------------------------------------------------------------------------
					----------------------------------     END OF THE APPLET TAG    ---------------------------------------------
					---------------------------------------------------------------------------------------------------------- -->
					        </td>
					    </tr>
					</table>

				</div>
				<!-- end of right content-->

			</div>
			<!--end of center content -->
			<div class="clear"></div>
		</div>
		<!--end of main content-->

		<%@ include file="inc/footer.jsp"%>
</div>
</body>
</html>
