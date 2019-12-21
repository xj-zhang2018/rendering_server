$(document).ready(function () {
	
    //确保全屏，并根据比例设置字体大小
    var wh = document.documentElement.clientHeight,
        ww = document.documentElement.clientWidth;
    var cmp = 1366,
        scale = ww / cmp,
        newsize = 10 * scale;
    document.body.style.fontSize = newsize + "px";
    $('.outer').css({ width: ww, height: 820 });

    var liw = $(".table>ul>li").width(),
        lih = $(".table>ul>li").height();
    
    //确保.center居中
    $(".center").css("left", ($(".power").width() - $(".power .center").width()) / 2);

    $(window).resize(function () {
        var wh = document.documentElement.clientHeight,
        ww = document.documentElement.clientWidth;
        $('.outer').css({ width: ww, height: 820 });
        scale = ww / cmp,
        newsize = 10 * scale;
        document.body.style.fontSize = newsize + "px";

        liw = $(".table>ul>li").width();
        lih = $(".table>ul>li").height();

        $(".center").css("left", ($(".power").width() - $(".power.center").width()) / 2);
    });
    
	var  instrumsUrl="listNodeGroupLength.action";
	var  framesrc="listNodesInfo.action?pageNum=";
	var  instrindex=1;
	  
	$.ajax({
		url: instrumsUrl, 
		success: function(data){

			instrsLength=data.pageTotal; 
	    	if(instrsLength<=0)return;

	    	for(var i=0;i<instrsLength-1;i++){
	    		$(".table>ul").prepend("<li><div></div></li>");
	    	}

	    	$(".table>ul>li").last().children("div").load(framesrc+1 );

	    	instrindex++;
	    	if(instrindex>instrsLength)instrindex=1; 
    		$(".table>ul>li:eq(-2)").children("div").load(framesrc+instrindex);

	    	var atimer = setInterval(function () {
	    		instrindex++;
	    		if(instrindex>instrsLength)instrindex=1;
	    		$(".table>ul>li:eq(-3)").children("div").load(framesrc+instrindex);
		        move_left();
		    }, 6000);
      	}
	});
	
	//end
   
    function move_left() {
        var parent = $(".table>ul");
        var curr = $(".table>ul>li").last();
        curr.animate({ left: -liw }, 1000, "swing", function () {
            curr.css("left", 0).prependTo(parent);
        });
    }

});