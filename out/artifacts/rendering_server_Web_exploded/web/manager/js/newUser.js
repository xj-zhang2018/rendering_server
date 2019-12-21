// JavaScript Document

$(document).ready(function() {
	
	$("#newUser-form").Validform({
		tiptype:3
	});
    	
	$('#newUserBtn').click(function(){
		$('#newUser-form').submit();
	});
});