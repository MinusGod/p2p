package controllers.payment.hf;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import models.t_bids;
import net.sf.json.JSONObject;

import org.json.JSONException;

import payment.hf.service.HfPaymentCallBackService;
import payment.hf.service.HfPaymentService;
import payment.hf.util.HFServiceFee;
import payment.hf.util.HfConstants;
import payment.hf.util.HfPaymentUtil;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.results.RenderHtml;
import utils.ErrorInfo;
import utils.NumberUtil;
import utils.ServiceFee;
import business.Bid;
import business.User;

import com.shove.Convert;
import com.shove.security.Encrypt;

import constants.Constants;
import constants.OptionKeys;
import constants.PayType;
import controllers.front.bid.BidAction;
import controllers.front.invest.InvestAction;
import controllers.payment.PaymentBaseAction;
import controllers.supervisor.bidManager.BidAgencyAction;
import controllers.supervisor.financeManager.LoanManager;
import controllers.supervisor.financeManager.MerchantAccountManager;
import controllers.wechat.account.WechatAccountHome;

/**
 * 环讯托管回调实现类
 * 
 * @author liuwenhui
 *
 */
public class HfPaymentCallBackAction extends PaymentBaseAction{
	
	private static HfPaymentCallBackService hfPaymentCallBackService = new HfPaymentCallBackService(); 

	private static HfPaymentCallBackAction instance = null;
	
	private HfPaymentCallBackAction(){
		
	}
	
	public static HfPaymentCallBackAction getInstance(){
		if(instance == null){
			synchronized (HfPaymentReqAction.class) {
				if(instance == null){
					instance = new HfPaymentCallBackAction();
				}
			}
		}
		
		return instance;
	}

	/**
	 * 开户同步
	 */
	public static void userRegisterSyn(){
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		hfPaymentCallBackService.userRegister(resultMap, "开户同步回调", error);
		
		String orderId = resultMap.get("MerPriv");
		Map<String,String> maps = hfPaymentCallBackService.queryRequestData(orderId,error);
		int client = Convert.strToInt(maps.get("client").toString(), -1);
		if(client == Constants.CLIENT_APP){
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			if (error.code < 0  ) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", error.msg);
			} else {
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "开户成功");
			}
			Logger.info("------------返回回调内容给app端(开户)------------2");
			//renderJSON(jsonMap);
			String s = JSONObject.fromObject(jsonMap).toString();
			render("/front/appinfo/notice/suscessCharge.html",s);
			return;
		}
		if(client == Constants.WECHAT){
			if(error.code<0){
				WechatAccountHome.accountInfo();
			}
			WechatAccountHome.accountInfo();
		}
		payErrorInfo(error.code, error.msg, PayType.REGISTER);

	}
	
	/**
	 * 开户异步
	 */
	public static void userRegisterAyns(){
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		hfPaymentCallBackService.userRegister(resultMap, "开户异步回调", error);
		
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	
	}
	
	/**
	 * 充值同步回调
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void netSaveSyn(){
		
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		//业务调用
		hfPaymentCallBackService.netSave(resultMap, error);
		Map<String, String> dataMap = hfPaymentCallBackService.queryRequestData(resultMap.get("OrdId"), new ErrorInfo());
		int client = Convert.strToInt(dataMap.get("client").toString(), -1);
		if(client == Constants.CLIENT_APP){//防重复后所跳转的页面
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			if (error.code < 0  &&error.code!= Constants.ALREADY_RUN) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", error.msg);
			} else {
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "充值成功");
			}
			Logger.info("------------返回回调内容给app端(充值)------------1");
			String s = JSONObject.fromObject(jsonMap).toString();
			render("/front/appinfo/notice/suscessCharge.html",s);
			//renderJSON(jsonMap);
			return;
		}
		if(client == Constants.WECHAT){
			/*if(error.code<0){
				flash.error("充值失败，请重新充值！");
			}else{
				flash.error( "充值成功！");
			}*/
			payErrorInfo(error.code, error.msg, PayType.RECHARGEWX);
		}
		
		payErrorInfo(error.code, error.msg, PayType.RECHARGE);
	}
	
	/**
	 * 充值异步回调
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void netSaveAyns(){
		
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		//业务调用
		hfPaymentCallBackService.netSave(resultMap, error);
		
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	/**
	 * 标的发布成功，页面跳转，提示完善资料
	 */
	public void addBidInfoWS(ErrorInfo error, t_bids bid,int client){
		
		String sql = "SELECT id FROM t_bids WHERE bid_no = ?";
		
		Object bidId = null;
		
		try {
			bidId = JPA.em().createNativeQuery(sql).setParameter(1, bid.bid_no).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		flash.put("msg", "发布借款标成功");
		
		if(bidId != null){
			flash.put("no", OptionKeys.getvalue(OptionKeys.LOAN_NUMBER, error) + bidId.toString());
			flash.put("status", Bid.queryBidStatus(Long.parseLong(bidId.toString())));
		}
		
		flash.put("title", bid.title);
		flash.put("amount", HfPaymentUtil.formatAmount2(bid.amount));
		flash.put("mobile", User.queryUserMobile(bid.user_id));

		long agencyId = bid.agency_id;
		
		/*发布机构合作标*/
		if (agencyId > 0) {
			flash.error(error.code < 0 ? error.msg : "发布成功!");
			
			BidAgencyAction.agencyBidList(0);
		}
		if(client == Constants.APP){ return;}
		else if(client == Constants.CLIENT_WECHAT){
		   controllers.wechat.service.BidAction.bidPage(bid.product_id, error.code==0?1:error.code, 1);
		}else{
			BidAction.applyNow(bid.product_id, error.code==0?1:error.code, 1);
		}
		
	}
	
	/**
	 * 标的登记异步回调
	 */
	public static void addBidInfoAyns(){
		ErrorInfo error = new ErrorInfo();

		// 获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);

		// 业务调用
		hfPaymentCallBackService.addBidInfo(resultMap, "标的登记异步回调", error);
		
		// 打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	/**
	 * 冻结保证金异步回调
	 */
	public static void freezeBailAmountAyns(){
		ErrorInfo error = new ErrorInfo();
		
		// 获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		// 业务调用
		hfPaymentCallBackService.freezeBailAmount(resultMap, "冻结保证金异步回调", error);
		
		// 打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	/**
	 * 主动投标同步
	 */
	public static void initiativeTender(){
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		String orderNo = resultMap.get("OrdId");  //这里没有通过MerPriv查询请求参数。①MerPriv=OrdId，②交易失败时，汇付可能不返回MerPriv
		Map<String, String> maps = hfPaymentCallBackService.queryRequestData(orderNo, error);
		long bidId = Convert.strToLong(maps.get("bidId") + "", 0);
		double transAmt = Convert.strToDouble(maps.get("transAmt"),0.00);
		
		hfPaymentCallBackService.doInvest(resultMap, "主动投标同步回调", PayType.INVEST, error);
		
		//20151023 jacky
		int client = Convert.strToInt(maps.get("client").toString(), -1);
		if(client == Constants.CLIENT_APP){
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			if (error.code < 0 && error.code != Constants.ALREADY_RUN ) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", error.msg);
			} else {
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "投标成功");
			}
			Logger.info("------------返回回调内容给app端(投标)------------2");
			//renderJSON(jsonMap);
			String s = JSONObject.fromObject(jsonMap).toString();
			render("/front/appinfo/notice/suscessCharge.html",s);
			return;
			
		}else if(client == Constants.CLIENT_WECHAT){
			/*if(error.code<0){
				WechatAccountHome.accountInfo();
			}
			WechatAccountHome.accountInfo();*/
			payErrorInfo(error.code, error.msg, PayType.INVESTWX);
		}else{//PC端
		
			if(error.code < 0 && error.code != Constants.ALREADY_RUN){
				
				flash.error(error.msg);
				InvestAction.invest(bidId, "");
			}
			
			flash.put("amount", NumberUtil.amountFormat(transAmt));
			String showBox = Encrypt.encrypt3DES(Constants.SHOW_BOX, bidId + Constants.ENCRYPTION_KEY);
			InvestAction.invest(bidId, showBox);
		}
	
	}
	
	/**
	 * 主动投标异步
	 */
	public static void initiativeTenderAyns(){
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		hfPaymentCallBackService.doInvest(resultMap, "主动投标异步回调", PayType.INVEST, error);
		
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	/**
	 * 自动投标计划同步回调
	 */
	public static void autoInvestSignature(){
		
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		//业务调用
		hfPaymentCallBackService.autoInvestSignature(resultMap, "自动投标计划同步回调", error);
		
		payErrorInfo(error.code, error.msg, PayType.AUTO_INVEST_SIGNATURE);
	}
	
	/**
	 * 自动投标异步回调
	 */
	public static void autoTenderAyns(){
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		hfPaymentCallBackService.doInvest(resultMap, "自动投标异步回调", PayType.AUTO_INVEST, error);
		
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	/**
	 * 绑卡异步
	 */
	public static void userBindCardAyns(){
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		hfPaymentCallBackService.userBindCard(resultMap, error);
		
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	/**
	 * 提现同步
	 */
	public static void cash(){
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		hfPaymentCallBackService.withdraw(resultMap, "提现同步回调", error);
		String orderId=resultMap.get("OrdId");
		Map<String,String> maps = hfPaymentCallBackService.queryRequestData(orderId,error);
		int client = Convert.strToInt(maps.get("client").toString(), -1);
		if(client == Constants.CLIENT_APP){//防重复后所跳转的页面
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			if (error.code < 0  ) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", error.msg);
			} else {
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "提现成功");
			}
			Logger.info("------------返回回调内容给app端(提现)------------1");
			//renderJSON(jsonMap);
			String s = JSONObject.fromObject(jsonMap).toString();
			render("/front/appinfo/notice/suscessCharge.html",s);
			return;
		}
		if(client == Constants.WECHAT){
			/*if(error.code<0){
				WechatAccountHome.accountInfo();
			}
			WechatAccountHome.accountInfo();*/
			payErrorInfo(error.code, error.msg, PayType.WITHDRAWWX);
		}
		payErrorInfo(error.code, error.msg, PayType.WITHDRAW);
		
	}
	
	/**
	 * 提现异步
	 */
	public static void cashAyns(){
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		if((resultMap.get("FeeAmt")+resultMap.get("ServFee")).equals("")){			
			hfPaymentCallBackService.withdraw0(resultMap, "提现异步回调", error);
		}
		hfPaymentCallBackService.withdraw(resultMap, "提现异步回调", error);
		
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	/**
	 * 放款结束，跳转页面。
	 */
	public void bidAuditSuccWS(ErrorInfo error){
		
		if(error.code < 0 && error.code != Constants.ALREADY_RUN){  //失败
			flash.error(error.msg);
			
			LoanManager.readyReleaseList();
		}
		
		flash.error("放款成功");

		LoanManager.alreadyReleaseList(0);
	}
	
	/**
	 * 商户提现同步
	 */
	public static void merCash() {
		ErrorInfo error = new ErrorInfo();
		// 获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);

		hfPaymentCallBackService.merWithdrawal(resultMap, error);
		MerchantAccountManager.dealDetails(0, null, null, 0);
		
		if (error.code < 0 && error.code != Constants.ALREADY_RUN) {
			
			flash.error(error.msg);
			MerchantAccountManager.merWithdrawal();
		}
		
		flash.error("提现成功！");
		
		MerchantAccountManager.dealDetails(-1, "", "", 1);
		
	}
	
	/**
	 * 商户提现异步
	 */
	public static void merCashAyns() {
		ErrorInfo error = new ErrorInfo();

		// 获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);

		hfPaymentCallBackService.merWithdrawal(resultMap, error);
		MerchantAccountManager.dealDetails(0, null, null, 0);
		// 打印第三方需要的成功标示
		if (error.code == Constants.ALREADY_RUN) {
			printFlagBatch(params.allSimple(), error);
		}
	}
	
	/**
	 * 商户充值同步
	 */
	public static void merNetSave() {
		ErrorInfo error = new ErrorInfo();
		// 获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		hfPaymentCallBackService.merchantRecharge(resultMap, error);
		MerchantAccountManager.dealDetails(0, null, null, 0);
		
		if (error.code < 0 && error.code != Constants.ALREADY_RUN) {
			
			flash.error(error.msg);
			MerchantAccountManager.merRecharge();
		}
		
		flash.error("提现成功！");
		
		MerchantAccountManager.dealDetails(-1, "", "", 1);
	}
	
	/**
	 * 商户充值异步
	 */
	public static void merNetSaveAyns() {
		ErrorInfo error = new ErrorInfo();
		// 获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		hfPaymentCallBackService.merchantRecharge(resultMap, error);
		MerchantAccountManager.dealDetails(0, null, null, 0);
		// 打印第三方需要的成功标示
		if (error.code == Constants.ALREADY_RUN) {
			printFlagBatch(params.allSimple(), error);
		}
	}

	
	/**
	 * 转账异步回调
	 */
	public static void transferAyns(){
		
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		//业务调用
		hfPaymentCallBackService.transfer(resultMap, "转账异步回调", error);
		
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	/**
	 * 解冻资金异步
	 */
	public static void usrUnFreezeAyns(){
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}

	/**
	 * 用户账户支付同步回调
	 */
	public static void usrAcctPay(){
		ErrorInfo error = new ErrorInfo();
		
		// 获取返回参数
		Map<String,String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		//业务调用
		hfPaymentCallBackService.usrAcctPay(resultMap, "用户账户支付同步回调", error);
	
		payErrorInfo(error.code, error.msg, PayType.ADVANCE_REPAYMENT);
	}

	/**
	 * 用户账户支付异步回调
	 */
	public static void usrAcctPayAyns(){
		ErrorInfo error = new ErrorInfo();
		
		// 获取返回参数
		Map<String,String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		//业务调用
		hfPaymentCallBackService.usrAcctPay(resultMap, "用户账户支付异步回调", error);
		
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
		
	}
	
	/**
	 * 批量还款异步通知
	 */
	public static void batchRepaymentAyns(){
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		//业务调用
		hfPaymentCallBackService.batchRepayment(resultMap, "批量还款异步回调", error);
		
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	/**
	 * 债权转让同步回调
	 */
	public static void creditAssign() {
		ErrorInfo error = new ErrorInfo();

		// 获取返回参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);

		// 业务调用
		hfPaymentCallBackService.doDebtTransfe(resultMap, "债权转让同步回调", error);
		String orderId = resultMap.get("OrdId");
		Map<String,String> maps = hfPaymentCallBackService.queryRequestData(orderId,error);
		int client = Convert.strToInt(maps.get("client").toString(), -1);
		if(client == Constants.CLIENT_APP){
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			if (error.code < 0  ) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", error.msg);
			} else {
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "债权转让成功");
			}
			Logger.info("------------返回回调内容给app端(债权转让)------------2");
			renderJSON(jsonMap);
			return;
			
		}
		if(client == Constants.CLIENT_WECHAT){
			if(error.code<0){
				WechatAccountHome.accountInfo();
			}
			WechatAccountHome.accountInfo();
		}
		payErrorInfo(error.code, error.msg, PayType.DEBTOR_TRANSFER);

	}
	
	/**
	 * 债权转让异步回调
	 */
	public static void creditAssignAyns() {
		ErrorInfo error = new ErrorInfo();

		// 获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);

		// 业务调用
		hfPaymentCallBackService.doDebtTransfe(resultMap, "债权转让异步回调", error);

		// 打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}

	/**
	 * 放款异步回调
	 */
	public static void loansAyns(){
		ErrorInfo error = new ErrorInfo();

		// 获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
		
		//业务调用（单笔放款，无异步业务）

		// 打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	/** add by feng
	 * 转账异步回调
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void transferAynszh(){
		
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
	
		hfPaymentCallBackService.transferhb(resultMap, "转账响应参数", error);
		if (error.code < 0) {
			return;
		}
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	
	/** add by feng
	 * 校正金额异步回调
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void transferAynszhAdj(){
		
		ErrorInfo error = new ErrorInfo();
		
		//获取参数
		Map<String, String> resultMap = hfPaymentCallBackService.getRespParams(params);
	
		//打印第三方需要的成功标示
		printFlagBatch(resultMap, error);
	}
	
	
	
	/**
	 * 打印第三方需要的成功标示
	 * @param respParams
	 */
	private static void printFlagBatch(Map<String,String> respParams, ErrorInfo error){		
		if(error.code >= 0 || error.code == Constants.ALREADY_RUN){
			StringBuffer buffer = new StringBuffer();
			String TRXID=respParams.get(HfConstants.TRXID);
			String ORDID=respParams.get(HfConstants.ORDID);
			String PROID=respParams.get(HfConstants.PROID);
			String BatchId=respParams.get(HfConstants.BatchId);
			if(TRXID!=null){
				buffer.append( "RECV_ORD_ID_" + TRXID);				
			}
			if(ORDID!=null){
				buffer.append( "RECV_ORD_ID_" + ORDID);				
			}
			if(PROID!=null){
				buffer.append( "RECV_ORD_ID_" + PROID);				
			}
			if(BatchId!=null){
				buffer.append( "RECV_ORD_ID_" + BatchId);				
			}			
			Logger.info("异步应答打印："+buffer.toString());
			renderText(buffer.toString());
		}
		
	}
}
