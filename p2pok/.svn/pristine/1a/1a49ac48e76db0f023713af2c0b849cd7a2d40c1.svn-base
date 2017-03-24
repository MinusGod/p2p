
此文件夹是生成IOS和Android共用的二维码所用到文件

使用分3步走：
1.
编辑app_download.html文件的IOS和Android安装包路径：内部代码如下
//IOS 此地址由IOS开发同学提供
var ipa_path = "https://appstore.qiwangyun.com/sp2p/ios/yiniuniu/download.html";

//Android 此地址是Apk（apk由Android开发同学提供）文件所在服务器的路径
var apk_path = "http://172.16.6.173:8080/test1/sp2p_5.apk"; 

//qq扫描是否可以下载。默认true，如不行改为false 。
var isQQok = true; 

2.
把appDowload整个文件夹下的文件（包括图片）放到布署好的项目某个目录下（如：public）。

3.
以app_download.html文件的绝对路径（如：http://www.niumail.com/public/appDowload/app_download.html）生成二维码供客户扫描即大功告成。

注：IOS和Android共用这个二维码，不须分别给IOS和Android生成两个不同的二维码。
