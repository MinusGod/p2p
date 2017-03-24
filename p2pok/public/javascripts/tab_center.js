// JavaScript Document
$(document).ready(function(e) {
				setInterval(GetWindowWidth,10);
			});
			function GetWindowWidth(){
				var w=$(window).width();
				$(".banner").css("width",w+"px");
				if (w < 1920){
				   $("#container").css({"left":(w-1920)/2+"px"});
				   }else{
					$("#container").css({"left":0+"px"});
				 }
		}
		