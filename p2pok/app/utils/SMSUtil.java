package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import play.Play;
import business.BackstageSet;

import com.shove.gateway.sms.EimsSMS;

public class SMSUtil {
	
	public static final String url = Play.configuration.getProperty("jwURL", "");
	public static final String productid = Play.configuration.getProperty("jw_productID", ""); 
	
	/** 
	* @MethodName: sendSMS 
	* @Param: SMSUtil 
	* @Return: 
	* @Descb: 发送短信 
	* @Throws: 
	*/ 
	public static String sendSMS(String userName, String password, String content, 
			String phone) { 

		try{ 

			Map<String, String> map = new HashMap<String, String>(); 
			map.put("username", userName); 
			map.put("password", password); 
			map.put("phone", phone);
			
			
			map.put("message", content); 
			map.put("epid", productid); 
			map.put("linkid", ""); 
			map.put("subcode", ""); 
			String data = MmmUtil.byPostMethodToHttpEntity(url, MmmUtil.putParams(map), "GB2312"); 
			System.out.println(url);
			System.out.println("短信平台返回信息:"+data);
			String status = data.split(",")[0]; 
			if(status.equals("00")){ 
				return "Success"; 
			} 
			new Exception(); 
		}catch (Exception e) { 
			
			return "Fail"; 
		};	
		return "Success"; 

	}

	/**
	 * 发送短信
	 * @param mobile
	 * @param content
	 * @param error
	 */
	public static void sendSMS(String mobile,String content, ErrorInfo error) {
		if(StringUtils.isBlank(content)) {
			error.code = -1;
			error.msg = "请输入短信内容";
			
			return;
		}
		
		BackstageSet backstageSet  = BackstageSet.getCurrentBackstageSet();
		/*String balance = EimsSMS.getBalance(backstageSet.smsAccount, backstageSet.smsPassword);
		double balance_long = Convert.strToDouble(balance, 0);
		
		if(balance_long <= 0.0){
			error.code = -2;
			error.msg = "短信平台已欠费,请联系管理员!";
			
			return;
		}*/
		
		SMSUtil.sendSMS(backstageSet.smsAccount, backstageSet.smsPassword, content, mobile);
		error.code = 1;
		error.msg = "短信发送成功";
	}
	
	/**
	 * 发送校验码
	 * @param mobile
	 * @param error
	 */
	public static void sendCode(String mobile, ErrorInfo error) {
		error.clear();
		
		BackstageSet backstageSet  = BackstageSet.getCurrentBackstageSet();
		/*String balance = EimsSMS.getBalance(backstageSet.smsAccount, backstageSet.smsPassword);
		double balance_long = Convert.strToDouble(balance, 0);
		if(balance_long <= 0.0){
			error.code = -2;
			error.msg = "短信平台已欠费,请联系管理员!";
			
			return;
		}*/
		int randomCode = (new Random()).nextInt(8999) + 1000;// 最大值位9999
		System.out.println("验证码：" + randomCode);
		String content = "您正在注册融友网金融信息服务平台，您的验证码为："+randomCode+"  ,感谢您对融友网的支持，如需帮助请与您的专属服务人员联系或致电客服4000010068（9:00-18:00）！";
		SMSUtil.sendSMS(backstageSet.smsAccount, backstageSet.smsPassword, content, mobile);
		play.cache.Cache.set(mobile, randomCode + "", "2min");
		error.msg = "短信验证码发送成功";
	}
	
	/**
	 * 生成随机码
	 * @return
	 */
	public static int random1=0;
	public static String getRandom(){
		int random0=(int)(Math.random()*10);
		int random2=(int)(Math.random()*10);
		String randoms=""+random1;
		switch(randoms.length()){
		   case 1: randoms="00000"+randoms;
		           break;
		   case 2: randoms="0000"+randoms;
		           break;
		   case 3: randoms="000"+randoms;
		           break;
		   case 4: randoms="00"+randoms;
		            break;
		   case 5: randoms="0"+randoms;
		            break;
		}
		random1++;
		return random0+randoms+random2;
	}

	/**
	 * 密码找回短信提醒   发送校验码
	 * @param mobile
	 * @param error
	 */
	public static void sendCodeForfindPwd(String mobile, ErrorInfo error) {
		error.clear();
		BackstageSet backstageSet  = BackstageSet.getCurrentBackstageSet();
		int randomCode = (new Random()).nextInt(8999) + 1000;// 最大值位9999
		String content = "验证码："+randomCode+"  ,融友网用户，您正在进行通过手机找回密码操作[验证码请勿泄露]，感谢您对融友网的支持，如需帮助请与您的专属服务人员联系或致电客服4000010068（9:00-18:00）！";
		SMSUtil.sendSMS(backstageSet.smsAccount, backstageSet.smsPassword, content, mobile);
		play.cache.Cache.set(mobile, randomCode + "", "2min");
		error.msg = "短信验证码发送成功";
	}
	
}
