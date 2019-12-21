// JavaScript Document

$(document).ready(function() {
	
	var isPR=parseInt($('[name=preRender]').val());;
	
	if(isPR==1){
		$('#isPreRender').attr("checked","checked");
		$('#preRenderInfo').attr("style","display:block");
	}
	
	
	$("#editJob-form").Validform({
		tiptype:3
	});
    
	$('#isPreRender').click(function(){

		if(this.checked) {
			$('#preRenderInfo').attr("style","display:block");
		}
		else {
			$('#preRenderInfo').attr("style","display:none");
		}
	});
	
	$('#filePath').change(function(){
		
		var fp=$('#filePath').val();
		
		$("#frameRange")[0].validform_valid="false";
		
		$('#frameRange').attr('ajaxurl', 'validateFrameRange?filePath='+fp);
		
	});
	
	$('#editJobBtn').click(function(){
		$('#editJob-form').submit();
	});
});