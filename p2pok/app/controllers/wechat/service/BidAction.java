package controllers.wechat.service;

import java.text.DecimalFormat;
import java.util.List;

import models.t_bids;
import payment.PaymentProxy;
import play.cache.Cache;
import utils.CaptchaUtil;
import utils.ErrorInfo;
import utils.Security;
import business.BackstageSet;
import business.Bid;
import business.Product;
import business.Supervisor;
import business.TemplateEmail;
import business.Bid.Purpose;
import business.User;
import constants.Constants;
import constants.OptionKeys;
import controllers.BaseController;
import controllers.wechat.account.WechatAccountHome;

/**
 * 我要借款相关
 * 
 * @author Administrator
 *
 */
public class BidAction extends BaseController {

	/**
	 * 查询微信端显示的产品
	 */
	public static void queryAllProducts() {
		ErrorInfo error = new ErrorInfo();

		List<Product> products = Product.queryProduct(Constants.SHOW_TYPE_3,
				error);
		if (error.code < 0) {
			flash.error(error.msg);
			WechatAccountHome.errorShow(error.msg);
		}

		render(products);
	}

	/**
	 * 产品详细信息
	 */
	public static void queryProductDetail(long productId) {
		/*
		 * 包装方法没有写出来
		 */
		User user = User.currUser();
		if (null == user) {
			RegistAndLogin.login();
		} 
		user.id = user.id;
		/*未激活 就发送邮件提示用户*/
		if (!user.isEmailVerified) {
//			ErrorInfo error = new ErrorInfo();
//			TemplateEmail.activeEmail(user, error);
//			flash.error("您邮箱未激活，邮件已经发送到您的："+user.email+"请及时激活");
//			queryAllProducts();
		}
		if (!user.isAddBaseInfo) {
			WechatAccountHome.baseInfo();
		}
		
		Product product = new Product();
		product.id = productId;

		
		/* 手续费常量值 */
	    BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
	    double strfee = backstageSet.borrowFee;
	    double borrowFeeMonth = backstageSet.borrowFeeMonth;
	    double borrowFeeRate = backstageSet.borrowFeeRate;
	    
		if (product.id < 1) {
			flash.error("产品查询失败");
			WechatAccountHome.errorShow("产品查询失败");
		}
		
		render(product, borrowFeeMonth, borrowFeeRate, strfee);
	}

	/**
	 * 发布借款页面
	 */
	public static void bidPage(long productId, int code, int status) {
		User user = User.currUser();
		if (null == user) {
			RegistAndLogin.login();
		} 
		String codeStr = params.get("code");
		user.id = user.id;
		/*未激活 就发送邮件提示用户*/
		if (!user.isEmailVerified) {
//			ErrorInfo error = new ErrorInfo();
//			TemplateEmail.activeEmail(user, error);
//			flash.error("您邮箱未激活，邮件已经发送到您的："+user.email+"请及时激活");
//			queryAllProducts();
		}
		if (!user.isAddBaseInfo) {
			WechatAccountHome.baseInfo();
		}
		
		if (User.currUser().simulateLogin != null) {
			if (User.currUser().simulateLogin.equalsIgnoreCase(User.encrypt())) {
				flash.error("模拟登录不能进行该操作");
				String url = request.headers.get("referer").value();
				redirect(url);
			} else {
				flash.error("模拟登录超时，请重新操作");
				String url = request.headers.get("referer").value();
				redirect(url);
			}
		}

		ErrorInfo error = new ErrorInfo();

		Product product = new Product();
		product.createBid = true;
		product.id = productId;

		if (product.id < 1) {
			flash.error("产品查询失败");
			WechatAccountHome.errorShow("产品查询失败");
		}
		List<Purpose> purpose = Purpose.queryLoanPurpose(error, true);

		if (null == purpose) {
			flash.error("借款用途为空!");
			WechatAccountHome.errorShow("借款用途为空!");
		}

		/*
		 * 秒还标未进行自动还款签约 if (Constants.IPS_ENABLE && product.loanType ==
		 * Constants.S_REPAYMENT_BID &&
		 * StringUtils.isBlank(User.currUser().ipsRepayAuthNo)) {
		 * //index(productId, Constants.NOT_REPAY_AUTH, status);
		 * queryAllProducts(); }
		 */

		String key = "bid_" + session.getId();
		Bid loanBid = (Bid) Cache.get(key); // 获取用户输入的临时数据
		Cache.delete(key); // 删除缓存中的bid对象
		String uuid = CaptchaUtil.getUUID(); // 防重复提交UUID
		render(purpose, product, codeStr, uuid, loanBid, status);
		
	}

	/**
	 * 发布借款
	 */
	public static void bid(Bid bid, String signProductId, String uuid,
			int status) {
		// checkAuthenticity();
		User user = User.currUser();
		if (null == user) {
			RegistAndLogin.login();
		} 
		user.id = user.id;
		/*未激活 就发送邮件提示用户*/
		if (!user.isEmailVerified) {
//			ErrorInfo error = new ErrorInfo();
//			TemplateEmail.activeEmail(user, error);
//			flash.error("您邮箱未激活，邮件已经发送到您的："+user.email+"请及时激活");
//			queryAllProducts();
		}
		if (!user.isAddBaseInfo) {
			WechatAccountHome.baseInfo();
		}
		
		if (User.currUser().simulateLogin != null) {
			if (User.currUser().simulateLogin.equalsIgnoreCase(User.encrypt())) {
				flash.error("模拟登录不能进行该操作");
				String url = request.headers.get("referer").value();
				redirect(url);
			} else {
				flash.error("模拟登录超时，请重新操作");
				String url = request.headers.get("referer").value();
				redirect(url);
			}
		}

		ErrorInfo error = new ErrorInfo();
		long productId = Security.checkSign(signProductId,
				Constants.PRODUCT_ID_SIGN, Constants.VALID_TIME, error);

		if (productId < 1) {
			flash.error(error.msg);
			WechatAccountHome.errorShow(error.msg);
		}

		/* 防重复提交 */
		if (!CaptchaUtil.checkUUID(uuid)) {
			flash.put("msg", "请求已提交或请求超时!");
			bidPage(productId, -100, status);
		}

		bid.createBid = true; // 优化加载
		bid.productId = productId; // 填充产品对象
		bid.userId = User.currUser().id; // 填充用户对象

		/* 非友好提示 */
		if (null == bid || null == bid.product || !bid.product.isUse
				|| bid.product.isAgency || bid.user.id < 1
				 || !bid.user.isAddBaseInfo) {

			flash.error("发标异常");
			WechatAccountHome.errorShow("发标异常");
		}

		/*
		 * 秒还标未进行自动还款签约 if (Constants.IPS_ENABLE && bid.product.loanType ==
		 * Constants.S_REPAYMENT_BID &&
		 * StringUtils.isBlank(bid.user.ipsRepayAuthNo)) // index(productId,
		 * Constants.NOT_REPAY_AUTH, status);
		 */
		/* 发布借款 */

		/* 发布借款 */
		t_bids tbid = new t_bids();
		//标的发布前校验， 及组装标的信息，不插入数据库
		bid.createBid(Constants.CLIENT_WECHAT, tbid, error);
		//校验错误信息，提示到页面
		flash.put("msg", error.msg);		
		Cache.set("bid_" + session.getId(), bid); // 缓存用户输入的临时数据
		if(error.code < 0){			
			if(error.code==-1000){
				WechatAccountHome.recharge();
			}
			bidPage(productId, error.code, status);
		}	
		//资金托管接口调用
		if(Constants.IPS_ENABLE){					
			//资金托管调用标的发布接口
			PaymentProxy.getInstance().bidCreate(error, Constants.CLIENT_WECHAT, tbid, bid);
			flash.put("msg", error.msg);
			String key = "bid_" + session.getId();
			Bid loanBid = (Bid) Cache.get(key);  // 获取用户输入的临时数据

			//查询此标还有多少条资料没有通过
			int hasAuditCount = 0;
			if( loanBid != null){
				hasAuditCount = loanBid.queryHasAudit();
				if(hasAuditCount>0){
					Cache.delete(key); // 删除缓存中的bid对象
					WechatAccountHome.uploadAuthDatas(null,null,null,null,null,null,null,0);
				}
			}
			WechatAccountHome.accountInfo();
		}else{
			
			/* 发布借款 */
			bid.afterCreateBid(tbid, null, Constants.CLIENT_PC, 0, error);	

			Cache.delete("bid_" + session.getId()); // 删除错误带回页面数据的缓存

			flash.put("no", OptionKeys.getvalue(OptionKeys.LOAN_NUMBER, error)
					+ bid.id);
			flash.put("title", bid.title);
			DecimalFormat myformat = new DecimalFormat();
			myformat.applyPattern("##,##0.00");
			flash.put("amount", myformat.format(bid.amount));
			flash.put("status", bid.status);
			// }

			flash.put("code", error.code);
			flash.put("msg", "发标成功");
			bidPage(productId, error.code, status);
			
		}
		
		
		



	}

	/**
	 * 验证用户信息，这个方法不应该写在控制层
	 */
	public static void verifyUser() {
		User user = User.currUser();
		if (null == user) {
			RegistAndLogin.login();
		} 
		user.id = user.id;
		/*未激活 就发送邮件提示用户*/
		if (!user.isEmailVerified) {
			ErrorInfo error = new ErrorInfo();
			TemplateEmail.activeEmail(user, error);
			flash.error("您邮箱未激活，邮件已经发送到您的："+user.email+"请及时激活");
			queryAllProducts();
		}
		if (!user.isAddBaseInfo) {
			WechatAccountHome.baseInfo();
		}
	}
}
