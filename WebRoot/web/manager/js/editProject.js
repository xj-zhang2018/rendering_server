// JavaScript Document

$(document).ready(function() {
	
	$("#editProject-form").Validform({
		tiptype:3
	});
    	
	$('#editProjectBtn').click(function(){
		$('#editProject-form').submit();
	});
});