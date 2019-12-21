// JavaScript Document

$(document).ready(function() {
	
	$("#configurationUpdate-form").Validform({
		tiptype:3
	});
    	
	$('#configurationUpdateBtn').click(function(){
		$('#configurationUpdate-form').submit();
	});
});