$(function(){
	getMessage();
	setInterval(getMessage, 5000);

});

function getMessage() {
	$.post("queryNewMsg.action", {}, function(json) {
		
		var msgs = json.msgs;
		
		if(msgs!=null)
		$.each(msgs, function(i, n){
			
			pageHtml="<div class=\"message\"><p>"+n.contents+"</p><p><strong>"+n.createTime+"</strong></p></div>";
			
			$('.module_content').prepend(pageHtml);
		});
	},"json");
}
