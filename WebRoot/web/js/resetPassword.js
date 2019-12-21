$(function(){

	$("#signup-form").Validform({
		tiptype:3
	});
	
	$('#resetBtn').click(function(){
		$('#signup-form').submit();
	});

});