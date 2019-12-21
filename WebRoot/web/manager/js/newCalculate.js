// JavaScript Document


$(document).ready(function() {
	
	
	$("#newCalculate-form").Validform({
		tiptype:3
	});
    
	/*$('#isPreRender').click(function(){

		if(this.checked) {
			$('#preRenderInfo').attr("style","display:block");
		}
		else {
			$('#preRenderInfo').attr("style","display:none");
		}
	});*/
	
	
	$('#newCalculateBtn').click(function(){
		$('#newCalculate-form').submit();
	});
	

	$('#calculate_client_id').change(function(){
		var id=$(this).val();
		var dataString = 'calculate_client_id='+ id;
		
		$.ajax({
			type: "get",
            dataType: "json",
            cache:false,
            url: "doGetProjectsByUser.action",
            data: dataString,
            beforeSend:function(){
            	$('#selectJobInfo').attr("style","display:none");
            	$('#loadingJob').attr("style","display:block");           	
            	},
            	
            success: function(msg){  
            	$('#loadingJob').attr("style","display:none");
            	$('#selectJobInfo').attr("style","display:block");
            	$("#calculate_job_id").empty();
            	$("#calculate_job_id").append("<option value=\"\">--请选择作业--</option>");
            	/*此处的jobs是在ajaxAction中的，需要后期再创建一个ajaxAction*/
            	var data1 = msg.jobs;
            	
            	if(data1!=null&&data1.length>0)
				$.each(data1, function(i, n){
					$("#calculate_job_id").append("<option value='"+n.id+"'>"+n.cameraName+"</option>");
				});
            	
            }
		});
		
		$.ajax({
			type: "get",
            dataType: "json",
            timeout:90000,
            cache:false,
            url: "doGetScenePathByUser.action",
            data: dataString,
      
            success: function(msg){
           
				$("#xmlFilePath").empty();
				$("#xmlFilePath").append("<option value=\"\">--请选择场景文件--</option>");
				/*mapScenePath也是在以后创建的ajaxAction中的一个变量*/
				var data2 = msg.mapScenePath;
				if(data2!=null)
				$.each(data2, function(key, value){
					if(key.indexOf("camera")== 0 )  
					$("#xmlfilePath").append("<option value='"+value+"'>"+key+"</option>");
				});
            	
            }
		
		});
	});

	
	$('#calculate_job_id').change(function(){
		
		var jpi=$('#calculate_job_id').val();
		
		$("#cameraName")[0].validform_valid="false";
		
		$('#cameraName').attr('ajaxurl', 'validateCameraName?currentProject.id='+jpi);
		
		getSelectedValue();
		
	});
	
	
	
	$('#filePath').change(function(){
		
		var fp=$('#filePath').val();
		
		$("#frameRange")[0].validform_valid="false";
		
		$('#frameRange').attr('ajaxurl', 'validateFrameRange?filePath='+fp);
		
	});
	
	
});






function getSelectedValue(){
	var sel = document.getElementById("job_project_id");
	var selected_val = sel.options[sel.selectedIndex].value;
	document.getElementById('upLoadFileID').href="inFileUpload.action?currentProject.id="+selected_val;
	var url=document.getElementById('upLoadFileID').href;
	
	
}