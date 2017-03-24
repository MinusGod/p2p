package controllers.supervisor.userManager;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import constants.Constants;
import constants.Templets;
import controllers.front.account.AccountHome;
import controllers.supervisor.SupervisorController;
import net.sf.json.JSONObject;
import business.BackstageSet;
import business.DataSafety;
import business.StationLetter;
import business.Supervisor;
import business.TemplateEmail;
import business.User;
import models.t_users;
import models.v_user_info;
import utils.DateUtil;
import utils.ErrorInfo;
import utils.PageBean;
import utils.SMSUtil;
import utils.Security;

/**
 * 
 * 类名:AllUser
 * 功能:全部会员列表
 */

public class AllUser extends SupervisorController {

	/**
	 * 所有会员列表
	 */
	public static void allUser() {
		String name = params.get("name");
		String email = params.get("email");
		String beginTime = params.get("beginTime");
		String endTime = params.get("endTime");
		String orderType = params.get("orderType");
		String key = params.get("key");
		String curPage = params.get("currPage");
		String pageSize = params.get("pageSize");
		
		ErrorInfo error = new ErrorInfo(); 
		PageBean<v_user_info> page = User.queryUserBySupervisor(name, email, beginTime, endTime, key, orderType,
				curPage, pageSize, error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		
		render(page);
	}
	
	/**
	 * 详情
	 * @param id
	 */
	public static void detail(String sign){
		ErrorInfo error = new ErrorInfo();
		
		long id = Security.checkSign(sign, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0) {
			flash.error(error.msg);
			
			allUser();
		}
		
		User user = new User();
		user.id = id;

		render(user);

	}
	
	/**
	 * 站内信
	 */
	public static void stationLetter(String sign, String content, String title){
		ErrorInfo error = new ErrorInfo();
		JSONObject json = new JSONObject();
		
		if(content.length() > 1000) {
			error.code = -1;
			error.msg = "内容超出字数范围";
			json.put("error", error);
			renderJSON(json);
		}
		
		long receiverUserId = Security.checkSign(sign, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			json.put("error", error);
			renderJSON(json);
		}
		
		StationLetter message = new StationLetter();
		
		message.senderSupervisorId = Supervisor.currSupervisor().id;
		message.receiverUserId = receiverUserId;
		message.title = title;
		message.content = content;
		
		message.sendToUserBySupervisor(error); 
		
		
		json.put("error", error);
		
		renderJSON(json);
	}

	/**
	 * 邮件
	 */
	public static void email(String email, String content){
		ErrorInfo error = new ErrorInfo();
		TemplateEmail.sendEmail(1, email, null, Templets.replaceAllHTML(content), error);
		
		JSONObject json = new JSONObject();
		json.put("error", error);
		
		renderJSON(json);
	}

	/**
	 * 发信息
	 * @param mobile
	 * @param content
	 */
	public static void sendMsg(String mobile, String content){
		ErrorInfo error = new ErrorInfo();
		JSONObject json = new JSONObject();
		
		if(StringUtils.isBlank(mobile)){
			error.code = -1;
			error.msg = "请选择正确的手机号码!";
			json.put("error", error);
		}
		
		SMSUtil.sendSMS(mobile, content, error);
		json.put("error", error);
		
		renderJSON(json);
	}
	
	/**
	 * 重置密码
	 */
	public static void resetPassword(String userName, String email){
		ErrorInfo error = new ErrorInfo();
		JSONObject json = new JSONObject();
		if(StringUtils.isBlank(userName) || StringUtils.isBlank(email)) {
			error.code = -1;
			error.msg = "参数传入有误";
			json.put("error", error);
			
			renderJSON(json);
		}
		
		User.isEmailExist(email, null, error);

		if (error.code != -2) {
			error.code = -1;
			error.msg = "对不起，该邮箱没有注册";
			json.put("error", error);
			
			renderJSON(json);
		}
		
		t_users user = User.queryUserByEmail(email, error);
		
		TemplateEmail tEmail = new TemplateEmail();
		tEmail.id = 3;

		BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
		String sign = Security.addSign(user.id, Constants.PASSWORD);
		String url = Constants.RESET_PASSWORD_EMAIL + sign;

		String content = tEmail.content;

		content = content.replace(Constants.EMAIL_NAME, user.name);
		content = content.replace(Constants.EMAIL_TELEPHONE, backstageSet.companyTelephone);
		content = content.replace(Constants.EMAIL_PLATFORM, backstageSet.platformName);
		content = content.replace(Constants.EMAIL_URL, "<a href = "+url+">点击此处重置密码</a>");
		content = content.replace(Constants.EMAIL_TIME, DateUtil.dateToString(new Date()));

		TemplateEmail.sendEmail(2, email, tEmail.title, content, error);
		
		json.put("error", error);
		
		renderJSON(json);
	}
	
	/**
	 * 编辑用户信息弹框
	 * @param sign
	 */
	public static void editUserInfoWin(String sign){
		ErrorInfo error = new ErrorInfo();
		long userId = Security.checkSign(sign, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			renderJSON(error);
		}
		
		Map<String, Object> user = User.queryIpsInfo(error, userId);
		
		if (error.code < 0) {
			renderJSON(error);
		}
		
		//资金托管模式下，如果已成功开通自己托管账户，不允许修改
		if(user.containsKey("ips_acct_no") && user.get("ips_acct_no") != null && StringUtils.isNotBlank(user.get("ips_acct_no").toString()) ){

			error.code = -1;
			error.msg = "资金托管账户已开通成功，不能修改！";
			
			renderJSON(error);
		}

		render(user, sign);
	}
	
	/**
	 * 编辑用户信息
	 * @param sign
	 */
	public static void editUserInfo(String sign, String realityName, String idNumber, String email, String mobile){
		ErrorInfo error = new ErrorInfo();
		long userId = Security.checkSign(sign, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			renderJSON(error);
		}

		User.updateIpsInfo(error, userId, realityName, idNumber, email, mobile);
		
		renderJSON(error);
	}
	
	/**
	 * 模拟登录
	 */
	public static void simulateLogin(String sign){
		ErrorInfo error = new ErrorInfo();
		long id = Security.checkSign(sign, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			flash.error(error.msg);
			allUser();
		}
		
		User user = new User();
		
		user.id = id;
		user.simulateLogin = user.encrypt();
		user.setCurrUser(user);
		AccountHome.home();
	}
	
	/**
	 * 锁定用户
	 */
	public static void lockUser(String sign){
		ErrorInfo error = new ErrorInfo();
		JSONObject json = new JSONObject();
		long id = Security.checkSign(sign, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			json.put("error", error);
			renderJSON(json);
		}
		
		User.lockUser(id, error);
		
		json.put("error", error);
		
		renderJSON(json);
	}
	/**
	 * 更新签名
	 * @param id
	 */
	public static void changeSign(String sign){
		ErrorInfo error = new ErrorInfo();
		
		long id = Security.checkSign(sign, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0) {
			renderJSON(error);
		}
		
		DataSafety data = new DataSafety();
		data.updateSignWithLock(id, error);
		
		if(error.code == 0){
			error.msg = "更新用户签名成功！";
		}
		renderJSON(error);
	}
}
