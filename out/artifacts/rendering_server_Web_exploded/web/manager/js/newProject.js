// JavaScript Document

$(document).ready(function() {
	
	$("#newProject-form").Validform({
		tiptype:3
	});
    	
	$('#newProjectBtn').click(function(){
		$('#newProject-form').submit();
	});
	
	$('#project_client_id').change(function(){
		var id=$(this).val();
		
		$("#projectName")[0].validform_valid="false";
		
		$('#projectName').attr('ajaxurl', 'validateProjectName?currentUser.id='+id);
		
	});
});