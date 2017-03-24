var xinm = new Array();

function getInfos(){
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	 $.ajax({
         type: "GET",
         dataType :"json",  
         url: "info",
         data: {"startTime":startTime, 
       	     "endTime":endTime},
         dataType: "json",
         success: function(data){ 
        	      xinm=eval(data); 
        	      $("#queryStatus").text("成功");   	    
                  },
         error: function() {  
                     alert('获取用户列表失败');  

                  },
     });
}

var phone = new Array();
var usernames = $('.usernames');
var telsw = $('.telsw');
var times = $('.times');
//phone[0] = "2016-05-25"
var nametxt = $('.name');
//var phonetxt = $('.phone');
//var pcount = xinm.length-1;//参加人数
var runing = true;
var num = 0;
var t;
//开始停止
function start() {
	if (runing) {
	runing = false;
	$('#btntxt').removeClass('start').addClass('stop');
	$('#btntxt').html('停止');
	startNum();
	} else {
	runing = true;
	$('#btntxt').removeClass('stop').addClass('start');
	$('#btntxt').html('开始');
	stop();

	}
	}
//循环参加名单
function startNum() {
	
	num = Math.floor(Math.random() * (xinm.length));
	usernames.html(xinm[num].name);
	telsw.html(xinm[num].mobile);
	times.html(xinm[num].random);
	t = setTimeout(startNum, 0);
	}
//停止跳动
function stop() {
	//pcount = xinm.length-1;
	xinm.splice(num,1);
	clearInterval(t);
	t = 0;
	}



