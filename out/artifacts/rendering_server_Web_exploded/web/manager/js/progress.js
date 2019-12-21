/* WebAppers Progress Bar, version 0.2
* (c) 2007 Ray Cheung
*
* WebAppers Progress Bar is freely distributable under the terms of an Creative Commons license.
* For details, see the WebAppers web site: http://wwww.Webappers.com/
*
/*--------------------------------------------------------------------------*/

var initial = -120;
var imageWidth=240;
var eachPercent = (imageWidth/2)/100;
/************************************************************\
*
\************************************************************/
function setText (id, percent)
{
    $(id+'Text').innerHTML = percent+"%";
}
/************************************************************\
*
\************************************************************/
function display ( id, percentage,color )
{	
	if (typeof color == "undefined") {
    color = "1";
  	}
	if ( color == "1"){
    var percentageWidth = eachPercent * percentage;
    var actualWidth = initial + percentageWidth ;
    document.write('<img id="'+id+'" src="images/percentImage.png" alt="'+percentage+'%" class="percentImage'+color+'" style="background-position: '+actualWidth+'px 0pt;"/> <span id="'+id+'Text">'+percentage+'%</span>');
	}
	if ( color == "2"){
		 var percentageWidth = eachPercent * percentage;
		 var actualWidth = initial + percentageWidth ;
		 document.write('<img id="'+id+'" src="images/percentImage.png" alt="'+percentage+'%" class="percentImage'+color+'" style="background-position: '+actualWidth+'px 0pt;"/>  <span id="'+id+'Text">0%</span>');	
	}
}
/************************************************************\
*
\************************************************************/

function setProgress(id, percentage)
{
    var percentageWidth = eachPercent * percentage;
    var newProgress = eval(initial)+eval(percentageWidth)+'px';
    $(id).style.backgroundPosition=newProgress+' 0';
    setText(id,percentage);
}