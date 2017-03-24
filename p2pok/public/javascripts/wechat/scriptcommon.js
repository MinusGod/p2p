// JavaScript Document
// <script type="text/javascript">
       // window.onload = window.onresize = function(){
//            var width = $(window).width();
//			if(width<640){
//            $("body").css({
//                "font-size" : 13*width/320+"px"
//            });}
//			else{
//				$("body").css({
//                "font-size" : 24+"px"
//            });
//				}
//        }
    
$(".ly_regwrap input,.ly_passwordbox input,.ly_openuser input").each(function(){
	$(this).focus(function(){
		$(this).parent().parent().addClass("focusbox");
	}).blur(function(){
		$(this).parent().parent().removeClass("focusbox");
	})
})

/*======我要投资=====*/
$(".ly_myselect").click(function(e){
	$(this).children("ul").slideDown();
	e.stopPropagation();
})
$(".ly_myselect li").each(function(){
	$(this).click(function(e){	
		$(this).parent().prev().prev().text($(this).text())
		$(this).parent().slideUp();
		e.stopPropagation();
	})
})
$("body").click(function(){
	$(".ly_myselect ul").slideUp();
})

$(function(){
 var timeid,
       scroll=$(".ly_newslist "),height=scroll.children("ul").children("li").eq(0).height();
       scroll.hover(function(){
 				clearInterval(timeid);
			},
		    function(){
		        timeid=setInterval(function(){
		      if(!scroll.children("ul").is(":animated")){
		         scroll.children("ul").animate({"margin-top":-height+"px"},function(){
		         scroll.children("ul").css("margin-top",0).children("li:first").appendTo(scroll.children("ul"));
		        });
                            }
                        },3000);
                    }).trigger("mouseleave");
$(".lookpassword").hover(function(){
 	$(this).prev().attr("type","text");
},function(){
	
$(this).prev().attr("type","password");
})
     
})
