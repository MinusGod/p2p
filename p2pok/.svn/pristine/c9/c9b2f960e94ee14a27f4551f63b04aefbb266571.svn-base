// JavaScript Document
/*banner*/
$(function(){
	var wid=$(window).width(),
		width=$(".y_bannerul").width();
		$(".y_bannerul").css("left",(wid-width)/2+"px");	
	$(window).resize(function(){
		var wid=$(window).width(),
		width=$(".y_bannerul").width();
		$(".y_bannerul").css("left",(wid-width)/2+"px");
		
	})
})
/*banner图效果*/
$(function(){
	    var i=0,						/*初始化*/
		timeid,
		bannerbox=$(".y_bannerul"),
		banner=$(".y_bannerul li"),
		width=banner.width(),
		len=banner.length,
		clickadd=$("<span></span>"),
		index=0;
	for(i;i<len;i++){			/*添加click点击按钮*/
		clickadd.clone().appendTo(".y_listul");
		banner.eq(i).css("z-index","len-i");
	}
	/*click点击按钮始终居中*/
	liswid=$(".y_listul span").width()+20,
	lisul_ml=liswid*len/2;
	$(".y_listul").css("margin-left",-lisul_ml+"px");
	/*默认第一张banner显示设置*/
	var click=$(".y_listul span");
	click.removeClass("click").eq(0).addClass("click");
	banner.hide().eq(0).show();
	/*click按钮点击banner切换*/
	click.click(function(){
		var i=$(this).index();
		$(this).addClass("click").siblings().removeClass("click");
		if(i>index){							/*下一张切换*/
			banner.eq(i).css("left",width).show();
			if(!banner.is(":animated")){
				banner.eq(index).animate({"left":-width},300);
				banner.eq(i).animate({"left":0},300,function(){
					index=i;
				});
			}
		}else if(i<index){					/*上一张切换*/
			banner.eq(i).css("left",-width).show();
			if(!banner.is(":animated")){
				banner.eq(index).animate({"left":width},300);
				banner.eq(i).animate({"left":0},300,function(){
					index=i;
				});
			}
		}
	})
	/*banner hover清除切换，模拟点击*/
	$(".y_banner").hover(function(){
		clearInterval(timeid);
	},function(){
		timeid=setInterval(function(){
			next();
		},4000);
	}).trigger("mouseleave");
	/*下一张banner切换函数*/
	function next(){
	 	if(index<len-1){
	 		banner.eq(index+1).css("left",width).show();
	 		if(!banner.is(":animated")){
	 			banner.eq(index).animate({"left":-width},300);
	 			banner.eq(index+1).animate({"left":0},300);
	 			index++;
	 			click.removeClass("click").eq(index).addClass("click");
	 		}
	 	}else{
	 		banner.eq(0).css("left",width).show();
	 		if(!banner.is(":animated")){
	 			banner.eq(index).animate({"left":-width},300);
	 			banner.eq(0).animate({"left":0},300);
	 			index=0;
	 			click.removeClass("click").eq(index).addClass("click");
	 		}
	 	}
	}
	/*上一张banner切换函数*/
	function prev(){
	 	if(index<len&&index>0){
	 		banner.eq(index-1).css("left",-width).show();
	 		if(!banner.is(":animated")){
	 			banner.eq(index).animate({"left":width},300);
	 			banner.eq(index-1).animate({"left":0},300);
	 			index--;
	 			click.removeClass("click").eq(index).addClass("click");
	 		}
	 	}else{
	 		banner.eq(len-1).css("left",-width).show();
	 		if(!banner.is(":animated")){
	 			banner.eq(index).animate({"left":width},300);
	 			banner.eq(len-1).animate({"left":0},300);
	 			index=len-1;
	 			click.removeClass("click").eq(index).addClass("click");
	 		}
	 	}
	}
})
/*dd*/
$(function(){
    var m = 0;
    $(window).scroll(function(){
        var scrollTop = $(window).scrollTop();
        if(scrollTop > 0){
            if(m == 0){
                if(!$(".ff_top2").is(":animated")){
                    $(".ff_top2").animate({
                        "width":"100%",
                        "top":0,
                        "left":0
                    },500,function(){
                        m = 1;
                    });
                }
            }
        }else{
            if(!$(".ff_top2").is(":animated")){
                $(".ff_top2").animate({
                    "width":"100%",
                    "top":"0px",
                    "left":"0%"
                },500,function(){
                    m = 0;
                });
            }



        }
        if(scrollTop > 553){
            $(window).mousewheel(function(e,d){
                if(d>0){
                    $(".ff_top2").show();
                }else{
                    $(".ff_top2").hide();
                }
            });
        }else{
            $(".ff_top2").show();
        }
    })

})
/*头部-导航*/
$(function(){
	var m = 0;
	$(window).scroll(function(){
		var scrollTop = $(window).scrollTop();
		if(scrollTop > 0){
			if(m == 0){
				if(!$(".y_top").is(":animated")){
					$(".y_top").animate({
						"width":"100%",
						"top":"30px",
						"left":0
					},500,function(){
						m = 1;
					});
				}
			}
		}else{
			if(!$(".y_top").is(":animated")){
				$(".y_top").animate({
					"width":"100%",
					"top":"30px",
					"left":"0%"
				},500,function(){
					m = 0;
				});
			}



		}
		if(scrollTop > 553){
			$(window).mousewheel(function(e,d){
	            if(d>0){ 
	               $(".y_top").show(); 
	            }else{
					$(".y_top").hide();
	            }
	      	});
		}else{
			$(".y_top").show();
		}
	})
	$(".y_nav span").map(function(){
		var ww=$(this).parent().width();
		$(this).width(ww)
	})
})
/*登录*/
$(function(){
	var dla=$(".y_navbox .ydl_dl"),
		zca=$(".y_navbox .y_topzc");


    $(".y_top").animate({"left":0},200);
    $(".ff_top2").animate({"left":0},200);
	/*登录*/
	dla.click(function(){
		$(".y_tcmain, .dl_box").show();
		$(".zc_box").hide();
		if(! $(".y_tcdlbox").is(":animated")){
			$(".y_mainmax").animate({"left":-420},200).css("position","fixed");
			$(".y_top").animate({"left":-420},200)
			$(".ff_top2").animate({"left":-420},200)
			$(".y_tcdlbox").animate({"right":0},200)
		}
	})
	/*注册*/
	zca.click(function(){
		$(".y_tcmain, .zc_box").show();
		$(".dl_box").hide();
		if(! $(".y_tcdlbox").is(":animated")){
			$(".y_mainmax").animate({"left":-420},200).css("position","fixed")
			$(".y_top").animate({"left":-420},200)
			$(".ff_top2").animate({"left":-420},200)
			$(".y_tcdlbox").animate({"right":0},200)
		}
	})
	/*隐藏*/
	$(".y_tcbg").click(function(){
		if(! $(".y_tcdlbox").is(":animated")){
			$(".y_mainmax").animate({"left":0},200).css("position","relative")
			$(".y_top").animate({"left":"0%","top":30,"width":"100%"},200)
			$(".ff_top2").animate({"left":"0%","top":0,"width":"100%"},200)

			$(".y_tcdlbox").animate({"right":-420},200,function(){
				$(".y_tcmain").hide();
			})
		}
	})
	/*登录、注册-切换*/
	$(".y_dlbox .y_dlaa").click(function(){
		$(this).parent().parent().hide().siblings(".y_dlbox").show();
	})

})

/*首页-投资列表*/
$(function(){
	$(".y_dexlcli li:nth-child(3n)").css("margin-right",0);
	$(".y_dexmtbox li:nth-child(3n)").css("margin-right",0);
	
})
//********************选项卡*******************************
$(function(){
	public_select($(".y_dexlcul>li"),$(".y_dexlcli ul"),"click");
})

	function public_select(control,scrolldiv,classname){
		scrolldiv.hide().eq(0).show();
		control.click(function(){
			$(this).addClass(classname).siblings().removeClass(classname);
			var index=$(this).index();
			scrolldiv.hide().eq(index).show();	
		})
	}

$(function(){
	$(".y_fotico .y_foico").click(function(){
		var ewm=$(this).siblings(".y_ewmbox");
		if(! ewm.is(":animated")){
			if(ewm.is(":hidden")){
				ewm.slideDown(100);
			}else{
				ewm.slideUp(100);
			}
		}
	})
	$(".y_fotico .y_foico").hover(function(){
		
	},function(){
		$(this).siblings(".y_ewmbox").hide();
	})
})
/*文本焦点*/
$(function(){
	$(".y_dluls input[type='text'],.y_dluls input[type='password']").map(function(){
		var that = $(this);
		var timeid = setInterval(function(){
			if(!that.val()==""){
				that.siblings("span").hide();
				clearInterval(timeid);
			}
		},10);
		var text = $(this).siblings("span").text();
		$(this).bind({
         focus:function(){
         	$(this).css("border-color","#FA7676");
		    if (this.value == ""){
				$(this).siblings("span").hide();
				$(this).attr("placeholder",text);

				}
			},
		blur:function(){
			if (this.value == ""){
                $(this).siblings("span").show();
                $(this).removeAttr("placeholder");

                }
            $(this).css("border-color","transparent");
            $(".y_page input").css("border-color","#ddd");
           }
      });
	})
	$(".y_zcwro input").map(function(){
		var that = $(this);
		var timeid = setInterval(function(){
			if(!that.val()==""){
				that.siblings("span").hide();
				clearInterval(timeid);
			}
		},10);
		var text = $(this).siblings("span").text();
		$(this).bind({
         focus:function(){
         	$(this).css({
         		"border-color":"#FA7676",
         		"color":"#999"
         	});
         	$(this).siblings(".y_zcwrts").hide();
         	$(this).parent().css("background","#fff");
		    if (this.value == ""){
				$(this).siblings("span").hide();
				$(this).attr("placeholder",text);

				}
			},
		blur:function(){
			if (this.value == ""){
                $(this).siblings("span").show();
                $(this).removeAttr("placeholder");
                $(this).siblings(".y_zcwrts").show();
            	$(this).parent().css("background","#FED9D9");
                }
            $(this).css("border-color","transparent");

           }
      });
	})
})

/*常见问题*/
$(function(){
	$(".y_wth3").map(function(){
		$(this).click(function(){
			var div=$(this).siblings(".y_wtdiv")
			if(div.is(":hidden")){
				div.slideDown();
				$(this).css("border-color","transparent");
				$(this).find(".fr").text("-");
			}else{
				div.slideUp();
				$(this).css("border-color","#ccc")
				$(this).find(".fr").text("+");
			}
		})
	})
})

/*标的详情*/
$(function(){
	$(window).load(function(){
		$(".y_bdtop").css({
			"height":0,
			"opacity":0
		}).animate({
			"height":430,
			"opacity":1
		},800)
	})

	/*选项卡*/
	public_select($(".y_bdul li"),$(".y_bdbox"),"click");
	function public_select(control,scrolldiv,classname){
		scrolldiv.hide().eq(0).show();
		control.click(function(){
			$(this).addClass(classname).siblings().removeClass(classname);
			var index=$(this).index();
			scrolldiv.hide().eq(index).show();	
		})
	}
})

/*账户-导航*/
$(function(){
	$(".y_wtnav").find(".clicks").children(".y_zhnav").show();
	$(".y_wtnav>ul>li>a").click(function(){
		var ul=$(this).parent().children("ul");
			uls=$(this).parent().siblings().children("ul");
		if(ul.is(":hidden")){
			ul.slideDown(200);
			uls.slideUp(200);
		}else{
			ul.slideUp(200);
		}
	})
})

/*我要借款*/
$(function(){
	var box=$(".y_jkul li"),
		tops=[];
	/*获取每个li的top值*/
	for(var i=0; i<box.length; i++){
        tops[i]=box.eq(i).offset().top+255;
        console.log(tops[i]);
    }
   
    box.css({		/*css样式定位*/
		"top":100,
		"opacity":0
	})
    $(window).scroll(function(){
    	var wtop=$(window).scrollTop(),
    		whe=$(window).height();
	    for(var i=0; i<box.length; i++){
	        if(wtop>=tops[i]-whe){
	        	box.eq(i).animate({
	        		"top":0,
	        		"opacity":1
	        	},500);
	        }
	    }
	})
})

$(function(){
	var h=$(".y_tcmain").height();
	$(".zc_box").height(h-65);
})





























