// JavaScript Document


$(document).ready(function() {
	
	
	$("#newJob-form").Validform({
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
	
	
	$('#newJobBtn').click(function(){
		$('#newJob-form').submit();
	});
	

	$('#project_client_id').change(function(){
		var id=$(this).val();
		var dataString = 'project_client_id='+ id;
		
		$.ajax({
			type: "get",
            dataType: "json",
            cache:false,
            url: "doGetProjectsByUser.action",
            data: dataString,
            beforeSend:function(){
            	$('#selectProjectInfo').attr("style","display:none");
            	$('#loadingProject').attr("style","display:block");           	
            	},
            	
            success: function(msg){  
            	$('#loadingProject').attr("style","display:none");
            	$('#selectProjectInfo').attr("style","display:block");
            	$("#job_project_id").empty();
            	$("#job_project_id").append("<option value=\"\">--请选择工程--</option>");
            	
            	var data1 = msg.projects;
            	
            	if(data1!=null&&data1.length>0)
				$.each(data1, function(i, n){
					$("#job_project_id").append("<option value='"+n.id+"'>"+n.name+"</option>");
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
           
				$("#filePath").empty();
				$("#filePath").append("<option value=\"\">--请选择场景文件--</option>");
				
				var data2 = msg.mapScenePath;
				if(data2!=null)
				$.each(data2, function(key, value){
					if(key.indexOf("camera")== 0 )  
					$("#filePath").append("<option value='"+value+"'>"+key+"</option>");
				});
            	
            }
		
		});
	});

	
	$('#job_project_id').change(function(){
		
		var jpi=$('#job_project_id').val();
		
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