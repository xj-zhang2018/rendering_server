// JavaScript Document

$(document).ready(function() {
	
	$("#editCalculate-form").Validform({
		tiptype:3
	});
    	
	$('#editCalculateBtn').click(function(){
		$('#editCalculate-form').submit();
	});
});