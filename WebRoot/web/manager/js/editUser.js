// JavaScript Document

$(document).ready(function() {
	
	$("#editUser-form").Validform({
		ignoreHidden:true,
		tiptype:3
	});
	
	$('#isChgPasswd').click(function(){

		if(this.checked) {
			$('#passwdInfo').attr("style","display:block");
		}
		else {
			$('#passwdInfo').attr("style","display:none");
		}
	});
	
	$('#editUserBtn').click(function(){
		
		$('#editUser-form').submit();

	});
});