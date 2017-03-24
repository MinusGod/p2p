package controllers.front.account;

import java.io.File;
import java.util.UUID;

import com.google.zxing.BarcodeFormat;
import com.shove.code.Qrcode;
import com.shove.security.License;

import models.t_user_cps_income;
import models.t_users;
import models.v_user_cps_user_count;
import models.v_user_cps_users;
import payment.hf.util.HfPaymentUtil;
import play.Logger;
import play.db.jpa.Blob;
import play.mvc.Before;
import play.mvc.With;
import utils.ErrorInfo;
import utils.PageBean;
import business.BackstageSet;
import business.Supervisor;
import business.User;
import constants.Constants;
import controllers.BaseController;
import controllers.interceptor.AccountInterceptor;
import controllers.supervisor.login.LoginAction;
import controllers.supervisor.systemSettings.SoftwareLicensAction;

@With({AccountInterceptor.class})
public class Spread extends BaseController {

	//-------------------------------GPS推广-------------------------
	//我的GPS链接
	public static void spreadLink(){
		User user =new User();		
		user.id= User.currUser().id;
		
		t_users tuser=new t_users();
		tuser=tuser.findById(user.id);
		String uuid = UUID.randomUUID().toString();
		Qrcode code = new Qrcode();
		
		try {
			Blob blob = new Blob();
			code.create(user.getSpreadCodeLink(), BarcodeFormat.QR_CODE, 200, 200, new File(blob.getStore(), uuid).getAbsolutePath(), "png");
			code.create(user.getAppCodeLink(), BarcodeFormat.QR_CODE, 200, 200, new File(blob.getStore(), uuid+"app").getAbsolutePath(), "png");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("刷新二维码图片失败"+e.getMessage());			
		}
		
		tuser.qr_code = uuid;
		tuser.save();
		
		
		BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
		
		render(user, backstageSet);
	}
	
	//我成功推广的会员
	public static void spreadUser(){
		User user = User.currUser();
		long userId = user.id;
		User recUser=new User();
		recUser.id=user.recommendUserId;
		String type = params.get("type");
		String key = params.get("key");
		String year = params.get("year");
		String month = params.get("month");
		String currPage = params.get("currPage");
		String pageSize = params.get("currSize");
		
		ErrorInfo error = new ErrorInfo();
		PageBean<v_user_cps_users> page = User.queryCpsSpreadUsers(userId ,type, key, 
				year, month, currPage, pageSize, error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		v_user_cps_user_count cpsCount = User.queryCpsCount(userId, error);
		
		/*查询用户所有的CPS收入*/
		double totalCpsIncome = User.queryTotalCpsIncome(userId);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		render(user, page, cpsCount, totalCpsIncome,recUser);
	}
	
	//推广会员详情
	public static void userDetail(){
		render();
	}
	
	/**
	 * 我的推广会员收入
	 */
	public static void spreadIncome(){
		User user = User.currUser();
		long userId = user.id;
		
		String year = params.get("year");
		String month = params.get("month");
		String currPage = params.get("currPage");
		String pageSize = params.get("currSize");
		
		ErrorInfo error = new ErrorInfo();
		
		PageBean<t_user_cps_income> page = User.queryCpsSpreadIncome(userId, 
				year,month,currPage,pageSize, error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		v_user_cps_user_count cpsCount = User.queryCpsCount(userId, error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		double totalCpsIncome = User.queryTotalCpsIncome(userId);
		
		render(user, page, cpsCount, totalCpsIncome);
	}
	
	/**
	 * 推广收入明细
	 */
	public static void incomeDetail(){
		User user = User.currUser();
		long userId = user.id;
		
		String type = params.get("type");
		String key = params.get("key");
		String year = params.get("year");
		String month = params.get("month");
		String currPage = params.get("currPage");
		String pageSize = params.get("currSize");
		
		ErrorInfo error = new ErrorInfo();
		PageBean<v_user_cps_users> page = User.queryCpsSpreadUsers(userId ,type, key, 
				year, month, currPage, pageSize, error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		render(user, page, year, month);
	}
	
	
	
	@Before
	public static void getData() {
		try{
			if(HfPaymentUtil.vertiSign()) return;
			License.update(BackstageSet.getCurrentBackstageSet().registerCode);
			if(!(License.getDomainNameAllow() && License.getAdminPagesAllow())) {
				flash.put("error", "");
				SoftwareLicensAction.notRegister();
			}
		}catch (Exception e) {
			e.printStackTrace();
			Logger.info("");
			flash.put("error", "");
			SoftwareLicensAction.notRegister();
		}
		
		if (Supervisor.isLogin()) {
			return; 
		}
		
		LoginAction.loginInit();
	}
	
	
	/**
	 * 重新生成二维码
	 */
	public static void weiCode(){
		User user=User.currUser();
		t_users tuser=new t_users();
		tuser=tuser.findById(user.id);
		String uuid = UUID.randomUUID().toString();
		Qrcode code = new Qrcode();
		
		try {
			Blob blob = new Blob();
			code.create(user.getSpreadCodeLink(), BarcodeFormat.QR_CODE, 200, 200, new File(blob.getStore(), uuid).getAbsolutePath(), "png");
			code.create(user.getAppCodeLink(), BarcodeFormat.QR_CODE, 200, 200, new File(blob.getStore(), uuid+"app").getAbsolutePath(), "png");

		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("刷新二维码图片失败"+e.getMessage());			
		}
		
		tuser.qr_code = uuid;
		tuser.save();
		spreadLink();
	}
	
}
