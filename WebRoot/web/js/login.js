// JavaScript Document

$(document).ready(function() {

	//去除input:-webkit-autofill的样式
	if(navigator.userAgent.toLowerCase().indexOf("chrome")>=0||navigator.userAgent.toLowerCase().indexOf("safari")>=0){
		window.setInterval(function(){
		$('input:-webkit-autofill').each(function(){
		var clone =$(this).clone(true,true);$(this).after(clone).remove();});},20);}
	
	
	$('.close').click(function(){
		$('.notice').remove();
	});
    
});