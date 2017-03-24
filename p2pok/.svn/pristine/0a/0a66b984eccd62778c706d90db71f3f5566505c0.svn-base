package controllers.app;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.t_bids;
import models.t_bill_invests;
import models.t_content_advertisements;
import models.t_content_advertisements_partner;
import models.t_content_news;
import models.t_dict_ad_citys;
import models.t_dict_ad_provinces;
import models.t_dict_audit_items;
import models.t_dict_cars;
import models.t_dict_educations;
import models.t_dict_houses;
import models.t_dict_maritals;
import models.t_products;
import models.t_user_attention_users;
import models.t_user_automatic_invest_options;
import models.t_user_cps_income;
import models.t_user_over_borrows;
import models.t_users;
import models.v_bid_attention;
import models.v_bid_auditing;
import models.v_bid_fundraiseing;
import models.v_bid_repayment;
import models.v_bid_repaymenting;
import models.v_bill_detail;
import models.v_bill_invest;
import models.v_bill_invest_detail;
import models.v_bill_invest_statistics;
import models.v_bill_loan;
import models.v_bill_repayment_record;
import models.v_credit_levels;
import models.v_debt_auction_records;
import models.v_debt_user_receive_transfers_management;
import models.v_debt_user_transfer_management;
import models.v_front_all_bids;
import models.v_front_all_debts;
import models.v_invest_records;
import models.v_messages_system;
import models.v_messages_user_inbox;
import models.v_messages_user_outbox;
import models.v_news_types;
import models.v_receiving_invest_bids;
import models.v_user_attention_info;
import models.v_user_attention_invest_transfers;
import models.v_user_audit_items;
import models.v_user_blacklist;
import models.v_user_cps_users;
import models.v_user_detail_credit_score_audit_items;
import models.v_user_detail_credit_score_invest;
import models.v_user_detail_credit_score_loan;
import models.v_user_detail_credit_score_normal_repayment;
import models.v_user_detail_credit_score_overdue;
import models.v_user_details;
import models.v_user_for_details;
import models.v_user_success_invest_bids;
import models.v_user_waiting_full_invest_bids;
import models.v_user_withdrawals;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.cache.Cache;
import utils.Arith;
import utils.CharUtil;
import utils.DateUtil;
import utils.EmailUtil;
import utils.ErrorInfo;
import utils.FileUtil;
import utils.IDCardValidate;
import utils.JSONUtils;
import utils.NumberUtil;
import utils.PageBean;
import utils.RegexUtils;
import utils.SMSUtil;
import utils.Security;
import utils.ServiceFee;
import bean.QualityBid;
import business.Ads;
import business.AuditItem;
import business.BackstageSet;
import business.Bid;
import business.Bid.Purpose;
import business.Bid.Repayment;
import business.BidAnswers;
import business.BidQuestions;
import business.Bill;
import business.BillInvests;
import business.CreditLevel;
import business.Debt;
import business.DictBanksDate;
import business.Invest;
import business.News;
import business.NewsType;
import business.Optimization.BidOZ;
import business.Optimization.UserOZ;
import business.OverBorrow;
import business.Product;
import business.SecretQuestion;
import business.StationLetter;
import business.TemplateEmail;
import business.User;
import business.UserAuditItem;
import business.UserBankAccounts;
import business.Vip;

import com.shove.security.Encrypt;

import constants.Constants;
import constants.IPSConstants;
import constants.Constants.DeleteType;
import constants.Constants.NewsTypeId;
import constants.IPSConstants.IPSDealStatus;
import constants.OptionKeys;
import payment.PaymentProxy;

/**
 * APP数据处理类
 * Description:对app端传过来的参数进行处理并返回数据
 * @author zhs
 * vesion: 6.0 
 * @date 2014-10-29 上午11:34:47
 */
public class RequestData {
	
    /**
     * 判断系统是否授权
     * @return 
     * @throws IOException
     */
	public static String checkAuthorize() throws IOException{
		Map<String, Object>  jsonMap = new HashMap<String, Object>();
		jsonMap.put("error", "-4");
		jsonMap.put("msg", "此版本非正版授权，请联系晓风软件购买正版授权！");
		

		return JSONUtils.printObject(jsonMap+"");
	}
	
	/**
	 * 查询借款标列表
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public static String queryAllbids(Map<String, String> parameters) throws IOException{
		
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		ErrorInfo error = new ErrorInfo();
		Map<String, Object>  jsonMap = new HashMap<String, Object>();
		
		String apr = (String)parameters.get("apr");
		String amount = (String)parameters.get("amount");
		String loanSchedule = (String)parameters.get("loanSchedule");
		String startDate = (String)parameters.get("startDate");
		String endDate = (String)parameters.get("endDate");
		String loanType = (String)parameters.get("loanType");
		String minLevelStr = (String)parameters.get("minLevelStr");
		String maxLevelStr = (String)parameters.get("maxLevelStr");
		String orderType = (String)parameters.get("orderType");
		String keywords = (String)parameters.get("keywords");
		
		PageBean<v_front_all_bids> bids = Invest.queryAllBids(Constants.SHOW_TYPE_2, currPage, pageSize, apr, amount, loanSchedule, startDate, endDate, loanType, minLevelStr, maxLevelStr, orderType, keywords, error);

		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg",error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", -1);
		map.put("msg", "查询成功");
		map.put("totalNum", bids.totalCount);
		map.put("list",bids.page);
		
		return  JSONObject.fromObject(map).toString();
	}
	
	
	/**
	 * 借款标详情
	 * @param parameters
	 * @return
	 */
	public static String bidDetail(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String borrowIdStr = parameters.get("borrowId");
		String userIdStr = parameters.get("userId");
		
		if(StringUtils.isBlank(borrowIdStr)){
			jsonMap.put("error", -3);
			jsonMap.put("msg", "借款id有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		long bidId = Long.parseLong(borrowIdStr);
		
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		Bid bid = new Bid();
		bid.id=bidId;
		
		long userId = 0;
		t_user_attention_users attentionUser = null;
		long attentionCode = 0;
		
		if(StringUtils.isNotBlank(userIdStr)) {
			userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
			
			if(error.code < 0 || userId < 0){
				jsonMap.put("error", "-2");
				//jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
				jsonMap.put("msg", error.msg);
				
				return JSONObject.fromObject(jsonMap).toString();
			}
			
			attentionUser = User.queryAttentionUser(userId, bid.userId, error);
			attentionCode = Bid.isAttentionBid(userId, bidId);
		}
		
		Map<String,String> historySituationMap = User.historySituation(bid.userId,error);//借款者历史记录情况
		List<UserAuditItem> uItems = UserAuditItem.queryUserAuditedItem(bid.userId, bid.mark); // 用户正对产品上传的资料集合
		
		if(uItems.size() > 0){
			for(int i = 0;i < uItems.size();i++){
				Map<String,Object> itemMap = new HashMap<String, Object>();
				itemMap.put("AuditSubjectName", uItems.get(i).auditItem.name);
				itemMap.put("auditStatus", uItems.get(i).strStatus);
				itemMap.put("imgpath", uItems.get(i).imageFileName);
				itemMap.put("statusNum", uItems.get(i).status);
				itemMap.put("isVisible", uItems.get(i).isVisible);
				itemMap.put("items", uItems.get(i).getItems());
				items.add(itemMap);
			}
		}
		
		jsonMap.put("attentionId", attentionUser == null ? "" : attentionUser.id);
		jsonMap.put("attentionBidId", attentionCode <= 0 ? "" : attentionCode);
		jsonMap.put("borrowid", bidId);
		jsonMap.put("borrowTitle", bid.title);
		jsonMap.put("borrowStatus", bid.status);
		jsonMap.put("purpose", bid.purpose.name);
		jsonMap.put("borrowtype", bid.product.smallImageFilename);//图片
		jsonMap.put("schedules", bid.loanSchedule);
		jsonMap.put("borrowAmount", bid.amount);
		jsonMap.put("annualRate", bid.apr);
		jsonMap.put("period", bid.period);
		jsonMap.put("periodUnit", bid.periodUnit);
		jsonMap.put("imageFilename", bid.imageFilename);
		
		if(bid.periodUnit == -1){
			jsonMap.put("deadline", bid.period+"年");
		}else if(bid.periodUnit == 0){
			jsonMap.put("deadline", bid.period+"个月");
		}else{
			jsonMap.put("deadline", bid.period+"天");
		}
		
		jsonMap.put("isQuality", bid.isQuality);
		jsonMap.put("paymentType", bid.repayment.id);
		jsonMap.put("paymentMode", bid.repayment.name);
		jsonMap.put("paymentTime", bid.recentRepayTime+"");
		
		if(bid.isAgency && bid.isShowAgencyName){
			jsonMap.put("associates", bid.agency.name);
		}else{
			jsonMap.put("associates", "");
		}
		
		jsonMap.put("remainTime", bid.investExpireTime+"");//结束时间
		jsonMap.put("borrowerId", bid.userId);
		jsonMap.put("borrowerheadImg", bid.user.photo);
		jsonMap.put("creditRating", bid.user.myCredit.imageFilename);//图片
		jsonMap.put("borrowername", bid.user.name);
		jsonMap.put("vipStatus", bid.user.vipStatus);
		
		jsonMap.put("borrowSuccessNum", historySituationMap.get("successBidCount"));
		jsonMap.put("borrowFailureNum",historySituationMap.get("flowBids"));
		jsonMap.put("repaymentNormalNum",historySituationMap.get("normalRepaymentCount"));
		jsonMap.put("repaymentOverdueNum",historySituationMap.get("overdueRepaymentCount"));
		jsonMap.put("borrowDetails", bid.description);
		jsonMap.put("CBOAuditDetails", bid.auditSuggest);
		
		jsonMap.put("registrationTime", bid.user.time+"");
		jsonMap.put("SuccessBorrowNum",historySituationMap.get("successBidCount"));
		jsonMap.put("NormalRepaymentNum", historySituationMap.get("normalRepaymentCount"));
		jsonMap.put("OverdueRepamentNum", historySituationMap.get("overdueRepaymentCount"));//图片
		jsonMap.put("reimbursementAmount", historySituationMap.get("pendingRepaymentAmount"));
		jsonMap.put("BorrowingAmount", historySituationMap.get("loanAmount"));
		
		jsonMap.put("FinancialBidNum", historySituationMap.get("financialCount"));
		jsonMap.put("paymentAmount", historySituationMap.get("receivingAmount"));
		jsonMap.put("BorrowingAmount", historySituationMap.get("loanAmount"));
		
		jsonMap.put("bonusType",bid.bonusType);//奖励方式
		jsonMap.put("bonus",bid.bonus);//固定奖金
		jsonMap.put("awardScale",bid.awardScale);//比列奖金
		
		jsonMap.put("no",bid.no);
		jsonMap.put("bidIdSign",bid.sign);
		jsonMap.put("bidUserIdSign",bid.signUserId);
		jsonMap.put("remainAmount",bid.amount-bid.hasInvestedAmount);
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("list", items);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 查询借款标投标记录
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public static String queryBidInvestRecords(Map<String, String> parameters) throws IOException{
		
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		ErrorInfo error = new ErrorInfo();
		Map<String, Object>  jsonMap = new HashMap<String, Object>();
		String bidIdStr = parameters.get("borrowId");
		
		if(StringUtils.isBlank(bidIdStr)){
			jsonMap.put("error", -3);
			jsonMap.put("msg", "借款标id参数有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		long bidId = Long.parseLong(bidIdStr);
		
		PageBean<v_invest_records> pageBean = Invest.queryBidInvestRecords(currPage, pageSize, bidId,error);
		List<v_invest_records> page = pageBean.page;
		if(null != page) {
			for(v_invest_records record : page) {
				String name = record.name;
				if(null != name && name.length() > 1) {
					record.name = record.name.substring(0, 1) + "***";
				}
			}
		}
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg","查询出现异常，给您带来的不便敬请谅解！");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", -1);
		map.put("msg", "查询成功");
		map.put("totalNum",  pageBean.totalCount);
		map.put("list",page);
		return  JSONObject.fromObject(map).toString();
	}
	
	/**
	 * 查询借款标提问记录
	 * @param parameters
	 * @return
	 */
	public static String addQuestion(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String userIdStr = parameters.get("id");
		String bidIdStr = parameters.get("borrowId");
		String content = parameters.get("questions");
		String toUserIdStr = parameters.get("bidUserIdSign");
		
		if (StringUtils.isBlank(userIdStr)) {
			map.put("error", "-2");
			map.put("msg", "请传入用户ID");
			return JSONObject.fromObject(map).toString();
		}
		if (StringUtils.isBlank(bidIdStr)) {
			map.put("error", "-3");
			map.put("msg", "请传入借款标ID");
			return JSONObject.fromObject(map).toString();
		}
		if (StringUtils.isBlank(toUserIdStr)) {
			map.put("error", "-3");
			map.put("msg", "请传入被提问用户ID");
			return JSONObject.fromObject(map).toString();
		}
		if (StringUtils.isBlank(content)) {
			map.put("error", "-3");
			map.put("msg", "请输入提问内容");
			return JSONObject.fromObject(map).toString();
		}
		
		long bidId = Long.parseLong(bidIdStr);
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			map.put("error", "-2");
			map.put("msg", "用户的id解析有误");
			return JSONObject.fromObject(map).toString();
		}
		long toUserId = Security.checkSign(toUserIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || toUserId < 0){
			map.put("error", "-3");
			map.put("msg", "被提问用户ID解析有误");
			return JSONObject.fromObject(map).toString();
		}
		
		BidQuestions question = new BidQuestions();
		question.bidId = bidId;
		question.userId = userId;
		question.time = new Date();
		question.content = content;
		question.questionedUserId = toUserId;
		
		int result = question.addQuestion(userId, error);
		
		if(result < 0){
			map.put("error", -8);
			map.put("msg",error.msg);
		}else{
			map.put("error", -1);
			map.put("msg",error.msg);
		}
		
		return  JSONObject.fromObject(map).toString(); 
	}
	
	/**
	 * 投标操作
	 * @param parameters
	 * @return
	 */
	public static String invest(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String borrowIdStr = parameters.get("borrowId");
		String userIdStr = parameters.get("userId");
		String amountStr = parameters.get("amount");
		String dealPwd = parameters.get("dealPwd");
		
		if (StringUtils.isBlank(amountStr)) {
			map.put("error", "-3");
			map.put("msg", "请输入投标金额");
			return JSONObject.fromObject(map).toString();
		}
		
		if (StringUtils.isBlank(borrowIdStr)||"null".equals(borrowIdStr)) {
			map.put("error", "-3");
			map.put("msg", "请传入借款标ID");
			return JSONObject.fromObject(map).toString();
		}
		if (StringUtils.isBlank(userIdStr)) {
			map.put("error", "-2");
			map.put("msg", "请传入用户ID");
			return JSONObject.fromObject(map).toString();
		}
		
		boolean b=amountStr.matches("^[1-9][0-9]*$");
    	if(!b){
    		map.put("error", "-3");
    		map.put("msg", "对不起！投标金额只能是正整数！");
			return JSONObject.fromObject(map).toString();
    	} 
    	
    	long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
    	
    	User user = new User();
		user.id = userId;
    	//20151023 jacky
    	if(Constants.IPS_ENABLE){
    	 	if (!User.isIPSacctNoExist(userId, error)) {
    			map.put("error", "-31");
    			map.put("msg", "用户未开通资金托管账号");
    			Map<String, Object> maps=new HashMap<String, Object>();
    			maps = PaymentProxy.getInstance().register(error,Constants.CLIENT_APP, user);
    			map.put("htmlParam", maps.get("htmlParam"));
    			return JSONObject.fromObject(map).toString();
    		}
    	}
   
		
    	if(error.code < 0 || userId < 0){
			map.put("error", "-2");
			map.put("msg", "解析用户id有误");
			return JSONObject.fromObject(map).toString();
		}
		
		
		
//		if (!(user.isEmailVerified || user.isMobileVerified)) {
//			map.put("error", "-888");
//			map.put("msg", "用户未激活账号");
//			return JSONObject.fromObject(map).toString();
//		}
		
		long bidId = Long.parseLong(borrowIdStr);
		int amount = Integer.parseInt(amountStr);
		if(amount%1000!=0){
			map.put("error", "-3");
    		map.put("msg", "对不起！投标金额只能是1000的倍数！");
			return JSONObject.fromObject(map).toString();
		}
		dealPwd = Encrypt.decrypt3DES(dealPwd, Constants.ENCRYPTION_KEY);
		
		Invest.invest(userId, bidId, amount, dealPwd, false, Constants.CLIENT_APP, error);
		
		if(error.code == -999){
			map.put("error", "-999");
			map.put("msg", "您余额不足，请充值");
			return JSONObject.fromObject(map).toString();
		} else if(error.code < 0){
			map.put("error", "-3");
			map.put("msg", error.msg);
			return JSONObject.fromObject(map).toString();
		}else{
			map.put("error", -1);
			map.put("msg", "投标成功");
		}
		
		//20151023 jacky
	    t_bids tb =  t_bids.findById(bidId);
		double minInvestAmount = tb.min_invest_amount;
		double averageInvestAmount =tb.average_invest_amount;
		if(minInvestAmount == 0){//认购模式
			amount = (int) (amount*averageInvestAmount);
		}
		map = PaymentProxy.getInstance().invest(error, Constants.CLIENT_APP, tb, user,amount);
		map.put("error", "-1");
		map.put("msg", "投标成功");
		map.put("htmlParam", map.get("htmlParam"));
		return JSONObject.fromObject(map).toString(); 
	}
	
	
	/**
	 * 查询借款标提问以及回答列表
	 * @return
	 */
	public static String queryAllQuestions(Map<String, String> parameters){
		
		int currPage = 1;
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		
		Map<String,Object> map = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String borrowIdStr = parameters.get("borrowId");
		
		if(StringUtils.isBlank(borrowIdStr)){
			map.put("error", "-3");
    		map.put("msg", "请传入借款标ID！");
			return JSONObject.fromObject(map).toString();
		}
		
		long bidId = Long.parseLong(borrowIdStr);
		PageBean<BidQuestions> page = BidQuestions.queryQuestion(currPage, pageSize, bidId, "", Constants.SEARCH_ALL, -1, error);
		List<BidQuestions> list = page.page;
		if(null != list) {
			for(BidQuestions question : list) {
				String name = question.name;
				if(null != name && name.length() > 1) {
					question.name = question.name.substring(0, 1) + "***";
				}
			}
		}
		if(error.code < 0){
			map.put("error", -4);
			map.put("msg", "查询失败");
			return JSONObject.fromObject(map).toString();
		}
		
		map.put("questionList", list);
		map.put("error", -1);
		map.put("msg", "查询成功");
		map.put("questionsNum", page.totalCount);
		return JSONObject.fromObject(map).toString();
	}
	
	
	/**
	 * 投标详情
	 * @param parameters
	 * @return
	 */
	public static String investDetail(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String borrowIdStr = parameters.get("borrowId");
		String idStr = parameters.get("id");
		
		if(StringUtils.isBlank(borrowIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入借款标ID！");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(idStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID！");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long bidId = Long.parseLong(borrowIdStr);
		
		long id = Security.checkSign(idStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || id < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		/*APP进入详情页面增加浏览次数*/
		Invest.updateReadCount(bidId, error);
		
		Bid bid = new Bid();
		bid.createBid = true;
		bid.id = bidId;
		
		if(bid.id < 0) {
			jsonMap.put("isDealPassword", false);
		} else {
			jsonMap.put("isDealPassword", bid.product.isDealPassword);
		}
		
		User user = new User();
		user.id = id;
		
		jsonMap.put("borrowid", bidId);
		
		if(null != bid.userName && bid.userName.length() > 1) {
			bid.userName = bid.userName.substring(0,1) + "***";
		}
		jsonMap.put("Name", bid.userName);
		jsonMap.put("creditRating", user.myCredit.imageFilename);//图片
		jsonMap.put("accountAmount", user.balance + user.freeze);
		jsonMap.put("availableBalance", user.balance);
		jsonMap.put("schedules", bid.loanSchedule);
		
		jsonMap.put("borrowAmount", bid.amount);
		jsonMap.put("annualRate", bid.apr);
		jsonMap.put("period", bid.period);
		jsonMap.put("unit", bid.periodUnit);
		jsonMap.put("repayType", bid.repayment.id);
		
		if(bid.periodUnit == -1){
			jsonMap.put("deadline", bid.period+"年");
		}else if(bid.periodUnit == 0){
			jsonMap.put("deadline", bid.period+"个月");
		}else{
			jsonMap.put("deadline", bid.period+"天");
		}
		
		if(user.payPassword != null){
			jsonMap.put("payPassword", true);
		}else{
			jsonMap.put("payPassword", false);
		}
		
		jsonMap.put("error","-1");
		jsonMap.put("msg","投标详情查询成功");
		jsonMap.put("title",bid.title);
		jsonMap.put("paymentMode",bid.repayment.name);
		jsonMap.put("paymentTime", bid.recentRepayTime + "");
		jsonMap.put("InvestmentAmount", bid.hasInvestedAmount);
		jsonMap.put("needAmount",bid.amount - bid.hasInvestedAmount);
		jsonMap.put("minTenderedSum", bid.minAllowInvestAmount);
		jsonMap.put("investNum", bid.investCount);
		jsonMap.put("views", bid.readCount);
		jsonMap.put("isDealPassword", bid.product.isDealPassword);
		jsonMap.put("averageInvestAmount",bid.averageInvestAmount);
		jsonMap.put("needAccount",bid.averageInvestAmount > 0 ? Arith.round((bid.amount-bid.hasInvestedAmount)/bid.averageInvestAmount,0) :  0);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 查询所有债权
	 * @param parameters
	 * @return
	 */
	public static String queryAllDebts(Map<String, String> parameters){
		
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		ErrorInfo error = new ErrorInfo();
		Map<String, Object>  jsonMap = new HashMap<String, Object>();
		
		String apr = (String)parameters.get("apr");
		String debtAmount = (String)parameters.get("debtAmount");
		String loanType = (String)parameters.get("loanType");
		String orderType = (String)parameters.get("orderType");
		String keywords = (String)parameters.get("keywords");
		
		if(StringUtils.isBlank(apr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "年利率有误");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(debtAmount)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "金额有误");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
//		if(StringUtils.isBlank(loanType)){
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "类型有误！");
//			return JSONObject.fromObject(jsonMap).toString();
//		}
		
		if(StringUtils.isBlank(orderType)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "排序有误");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
//		if(StringUtils.isBlank(keywords)){
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "关键字有误");
//			return JSONObject.fromObject(jsonMap).toString();
//		}
		
		PageBean<v_front_all_debts>  page = Debt.queryAllDebtTransfers( currPage,pageSize,loanType, debtAmount, apr, orderType,keywords,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询异常，给您带来的不便敬请谅解！");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put("list", page.page);
		map.put("totalNum", page.totalCount);
		map.put("error", -1);
		map.put("msg", "查询成功");
		
		return JSONObject.fromObject(map).toString();
	}
	
	/**
	 * 债权转让标详情
	 * @param parameters
	 * @return
	 */
	public static String debtDetail(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
		Map<String, Object>  jsonMap = new HashMap<String, Object>();
		String debtIdStr = parameters.get("id");
		String userIdStr = parameters.get("userId");
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", -3);
			jsonMap.put("msg", "请传入债权ID！");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		long debtId = Long.parseLong(debtIdStr);
		
		Debt debt = new Debt();
		debt.id = debtId;
		
		Long investUserId = Debt.getInvestUserId(debtId, error);
		
		if(error.code < 0){
			jsonMap.put("error", -4);
			jsonMap.put("msg", "查询出现异常，给您带来的不便敬请谅解！");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		
		Map<String,String>  debtUserhistorySituationMap = User.debtUserhistorySituation(investUserId,error);//债权者历史记录情况
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询出现异常，给您带来的不便敬请谅解！");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Long bidUserId = Debt.getBidUserId(debtId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询出现异常，给您带来的不便敬请谅解！");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Map<String,String> historySituationMap = User.historySituation(bidUserId,error);//借款者历史记录情况
		
		if(error.code < 0){
			jsonMap.put("error", -4);
			jsonMap.put("msg", "查询出现异常，给您带来的不便敬请谅解！");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = 0;
		t_user_attention_users attentionUser = null;
		long attentionDebtId = 0;
		
		if(StringUtils.isNotBlank(userIdStr)) {
			userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
			
			if(error.code < 0 || userId < 0){
				jsonMap.put("error", "-2");
				jsonMap.put("msg", "解析用户id出现错误");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
			
			attentionUser = User.queryAttentionUser(userId, investUserId, error);
			attentionDebtId = Debt.isAttentionDebt(userId, debtId, error);
		}
		
		List<UserAuditItem> uItems = UserAuditItem.queryUserAllAuditItem(bidUserId, debt.invest.bid.mark); // 用户正对产品上传的资料集合
		
		if(uItems.size() > 0){
			for(int i = 0;i < uItems.size();i++){
				Map<String,Object> itemMap = new HashMap<String, Object>();
				itemMap.put("AuditSubjectName", uItems.get(i).auditItem.name);
				itemMap.put("auditStatus", uItems.get(i).strStatus);
				itemMap.put("imgpath", uItems.get(i).imageFileName);
				itemMap.put("isVisible", uItems.get(i).isVisible);
				items.add(itemMap);
			}
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("attentionId", attentionUser == null ? "" : attentionUser.id);
		map.put("attentionDebtId", attentionDebtId <= 0 ? "" : attentionDebtId);
		map.put("creditorid", debt.id);
		map.put("creditorTitle", debt.title);
		map.put("creditorStatus", debt.status);
		map.put("remainTime", debt.endTime + "");
		map.put("principal", debt.debtAmount);
		map.put("auctionBasePrice", debt.transferPrice);
		map.put("creditorReason", debt.transerReason);
		
		map.put("borrowerId", debt.invest.bid.user.sign);
		map.put("borrowerheadImg", debt.invest.user.photo);
		map.put("creditRating", debt.invest.user.myCredit.imageFilename);
		map.put("borrowername", debt.invest.user.name);
		map.put("vipStatus",  debt.invest.user.vipStatus);
		
		map.put("borrowSuccessNum", debtUserhistorySituationMap.get("successBidCount"));
		map.put("borrowFailureNum", debtUserhistorySituationMap.get("flowBids"));
		map.put("repaymentNormalNum", debtUserhistorySituationMap.get("normalRepaymentCount"));
		map.put("repaymentOverdueNum", debtUserhistorySituationMap.get("overdueRepaymentCount"));
		
		map.put("borrowDetails", debt.invest.bid.description);
		map.put("CBOAuditDetails", debt.invest.bid.auditSuggest);
		map.put("registrationTime", debt.invest.user.time + "");
		
		map.put("SuccessBorrowNum", historySituationMap.get("successBidCount"));
		map.put("NormalRepaymentNum", historySituationMap.get("normalRepaymentCount"));
		map.put("OverdueRepamentNum", historySituationMap.get("overdueRepaymentCount"));
		map.put("reimbursementAmount", historySituationMap.get("pendingRepaymentAmount"));
		
		map.put("BorrowingAmount", historySituationMap.get("loanAmount"));
		map.put("FinancialBidNum", historySituationMap.get("financialCount"));
		map.put("paymentAmount", historySituationMap.get("receivingAmount"));
		
		map.put("bonusType",debt.invest.bid.bonusType);//奖励方式
		map.put("bonus",debt.invest.bid.bonus);//固定奖金
		map.put("awardScale",debt.invest.bid.awardScale);//比列奖金
		
		map.put("amount",debt.invest.bid.amount);
		map.put("corpus",debt.invest.amount);//
		map.put("maxOfferPrice",debt.maxOfferPrice);//目前拍价
		map.put("apr",debt.invest.bid.apr);
		map.put("receiveMoney",debt.map.get("receive_money"));
		map.put("hasReceiveMoney",debt.map.get("has_receive_money"));//
		map.put("remainReceiveMoney",debt.map.get("remain_receive_money"));
		map.put("receiveCorpus",debt.map.get("receive_corpus"));
		map.put("hasOverdue",debt.invest.bid.hasOverdue);//
		map.put("receiveTime",debt.map.get("receive_time"));
		
		map.put("sign",debt.sign);//债权加密ID
		map.put("debtUserIdSign",debt.invest.userIdSign);//债权所有者加密ID
		
		map.put("error", -1);
		map.put("msg", "查询成功");
		map.put("list", items);
		
		map.put("debtNo", debt.no);
		
		return JSONObject.fromObject(map).toString();
	}
	
	/**
	 * 债权竞拍记录
	 * @param parameters
	 * @return
	 */
	public static String debtAuctionRecords(Map<String, String> parameters){
		
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String debtIdStr = parameters.get("creditorId");
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", -3);
			jsonMap.put("msg", "请传入债权ID！");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		int currPage = 1;
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		long debtId = Long.parseLong(debtIdStr);
		
		PageBean<v_debt_auction_records> page = Debt.queryDebtAllAuctionRecords( currPage,  pageSize, debtId,error);
		List<v_debt_auction_records> list = page.page;
		if(null != list) {
			for(v_debt_auction_records record : list) {
				String name = record.name;
				if(null != name && name.length() > 1) {
					record.name = record.name.substring(0, 1) + "***";
				}
			}
		}
		if(error.code < 0){
			jsonMap.put("error", -4);
			jsonMap.put("msg", "查询出现异常，给您带来的不便敬请谅解！");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("totalNum",page.totalCount);
		jsonMap.put("list", list);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	
	/**
	 * 获取竞拍相关信息接口
	 * @param parameters
	 * @return
	 */
	public static String auctionDebtDetail(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String debtIdStr = parameters.get("creditorId");
		String userIdStr = parameters.get("id");
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", -3);
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", -2);
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		
		long debtId = Long.parseLong(debtIdStr);
		
		Debt debtBussiness = new Debt();
		debtBussiness.id = debtId;
		jsonMap.put("isDealPassword", debtBussiness.invest.bid.product.isDealPassword);
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", -2);
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Debt debt = new Debt();
		debt.id = debtId;
		
		
		User user = new User();
		user.id = userId;
		
		
		//判断用户是否需要交易密码
		if(user.payPassword != null){
			jsonMap.put("payPassword", true);
		}else{
			jsonMap.put("payPassword", false);
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("creditorid", debtId);
		
		if(null != debt.invest.user.name && debt.invest.user.name.length() > 1) {
			debt.invest.user.name = debt.invest.user.name.substring(0, 1) + "***";
		}
		
		jsonMap.put("Name", debt.invest.user.name);
		jsonMap.put("creditRating", user.myCredit.imageFilename);
		jsonMap.put("accountAmount", user.balance + user.freeze);
		jsonMap.put("availableBalance", user.balance);
		jsonMap.put("principal",debt.debtAmount);
		//jsonMap.put("isDealPassword", bid.isDealPassword);
		jsonMap.put("auctionBasePrice", debt.transferPrice);
		
		jsonMap.put("title", debt.title);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	
	/**
	 * 债权竞拍
	 * @param parameters
	 * @return
	 */
	public static String auction(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		String debtIdStr = parameters.get("creditorId");
		String userIdStr = parameters.get("id");
		String amountStr = parameters.get("amount");
		String dealpwdStr = parameters.get("dealPwd");
		
		if (StringUtils.isBlank(amountStr)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请输入竞拍金额");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if (StringUtils.isBlank(debtIdStr)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if (StringUtils.isBlank(userIdStr)) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		boolean b=amountStr.matches("^[1-9][0-9]*$");
    	if(!b){
    		jsonMap.put("error", "-3");
			jsonMap.put("msg", "对不起！竞拍价格只能是正整数！");
			return JSONObject.fromObject(jsonMap).toString();
    	} 
		
		int amount = Integer.parseInt(amountStr);
		
		long debtId = Long.parseLong(debtIdStr);
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);;
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		User user = new User();
		user.id = userId;
		
		if (!(user.isEmailVerified || user.isMobileVerified)) {
			jsonMap.put("error", "-888");
			jsonMap.put("msg", "用户未激活账号");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		dealpwdStr = Encrypt.decrypt3DES(dealpwdStr, Constants.ENCRYPTION_KEY);
		
		Debt.auctionDebt(userId, amount, debtId,dealpwdStr, Constants.CLIENT_APP, error);
		
		if(error.code == -999 ){
			jsonMap.put("error", "-999");
			jsonMap.put("msg", "您余额不足，请充值");
			
			return JSONObject.fromObject(jsonMap).toString();
		} else if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		} else{
			jsonMap.put("error", "-1");
			jsonMap.put("msg","竞拍成功");
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 投资子账户--投资账单
	 * @return
	 */
	public static String investBills(Map<String, String> parameters){
		
		
		int currPage = 1;
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String payTypeStr = parameters.get("payType");
		String isOverTypeStr = parameters.get("isOverType");
		String keyTypeStr = parameters.get("keyType");
		String key = parameters.get("key");
		String userIdStr = parameters.get("id");
		
		if(!(NumberUtil.isNumericInt(payTypeStr))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传人参数有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(!(NumberUtil.isNumericInt(payTypeStr) || NumberUtil.isNumericInt(isOverTypeStr) || NumberUtil.isNumericInt(keyTypeStr))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传人参数有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(!(NumberUtil.isNumericInt(payTypeStr) || NumberUtil.isNumericInt(isOverTypeStr) || NumberUtil.isNumericInt(keyTypeStr))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传人参数有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(NumberUtil.isNumericInt(parameters.get("currPage"))) {
 			currPage = Integer.parseInt(parameters.get("currPage"));
 		}
		
		int payType = Integer.parseInt(payTypeStr);
		int isOverType = Integer.parseInt(isOverTypeStr);
		int keyType = Integer.parseInt(keyTypeStr);
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_bill_invest> page = BillInvests.queryMyInvestBills(payType, isOverType, keyType, key, currPage,userId, error);
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		
		
		jsonMap.put("list", page.page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 账单详情
	 * @param parameters
	 * @return
	 */
	public static String billDetail(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		business.BackstageSet  currBackstageSet = business.BackstageSet.getCurrentBackstageSet();
		String userIdStr = parameters.get("user_id");
		String billIdStr = parameters.get("billId");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(billIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入账单ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long billId = Security.checkSign(billIdStr, Constants.BILL_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || billId < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "解析账单id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_bill_invest_detail investDetail = BillInvests.queryMyInvestBillDetails(billId, userId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "系统繁忙，给您带来的不便敬请谅解");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		String userName = User.queryUserNameById(userId, error);

		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "系统繁忙，给您带来的不便敬请谅解");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("userName", userName);
		jsonMap.put("billTitle", investDetail.title);
		jsonMap.put("dueDate", investDetail.receive_time + "");
		jsonMap.put("billId", investDetail.sign);
		jsonMap.put("billDate", investDetail.audit_time + "");
		jsonMap.put("platformName", currBackstageSet.platformName);
		jsonMap.put("hotline", currBackstageSet.platformTelephone);
		jsonMap.put("user_id", investDetail.user_id);
		jsonMap.put("billNo", investDetail.invest_number);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 本期账单明细
	 * @param parameters
	 * @return
	 */
	public static String currentBillDetail(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(parameters.get("user_id"))){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "传入用户ID有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		String userIdStr = parameters.get("user_id");
		String billIdStr = parameters.get("billId");
		
		if(StringUtils.isBlank(billIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入账单ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
//		long userId = Long.(userIdStr);
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long billId = Security.checkSign(billIdStr, Constants.BILL_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || billId < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "解析账单id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_bill_invest_detail investDetail = BillInvests.queryMyInvestBillDetails(billId, userId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "系统繁忙，给您带来的不便敬请谅解");
			
			return JSONObject.fromObject(jsonMap).toString();
		}

		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("repayAmount", investDetail.should_received_amount);
		jsonMap.put("expiryDate", investDetail.receive_time + "");
		jsonMap.put("repayWay", investDetail.repayment_type);
		jsonMap.put("repayCapital", investDetail.invest_amount);
		jsonMap.put("annualRate", investDetail.apr);
		
		jsonMap.put("interestSum", investDetail.current_receive_amount);
		jsonMap.put("receivedAmount", investDetail.has_received_amount);
		jsonMap.put("receivedNum", investDetail.has_received_periods);
		jsonMap.put("remainNum", investDetail.loan_periods - investDetail.has_received_periods);
		jsonMap.put("remainAmount", investDetail.should_received_amount);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 账单借款标详情
	 * @param parameters
	 * @return
	 */
	public static String  billBidDetail(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		String userIdStr = parameters.get("id");
		String billIdStr = parameters.get("billId");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(billIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入账单ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long billId = Security.checkSign(billIdStr, Constants.BILL_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || billId < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "解析账单id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_bill_invest_detail investDetail = BillInvests.queryMyInvestBillDetails(billId, userId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "系统繁忙，给您带来的不便敬请谅解");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("borrowTitle", investDetail.title);
		jsonMap.put("borrowAmount", investDetail.amount);
		jsonMap.put("interestSum", investDetail.current_receive_amount);
		jsonMap.put("borrowNum", investDetail.loan_periods);
		jsonMap.put("annualRate", investDetail.apr);
		jsonMap.put("eachPayment", investDetail.should_received_amount);
		jsonMap.put("paidPeriods", investDetail.has_received_periods);
		jsonMap.put("remainPeriods", investDetail.loan_periods - investDetail.has_received_periods);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 账单历史收款情况
	 * @return
	 */
	public static String historicalRepayment(Map<String, String> parameters){
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		
		int pageSize = Constants.APP_PAGESIZE;
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		List<Map<String,Object>> investBills = new ArrayList<Map<String,Object>>();
		
		String userIdStr = parameters.get("user_id");
		String billIdStr = parameters.get("billId");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(billIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入账单ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long billId = Security.checkSign(billIdStr, Constants.BILL_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || billId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析账单id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_bill_invest_detail investDetail = BillInvests.queryMyInvestBillDetails(billId, userId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "系统繁忙，给您带来的不便敬请谅解");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<t_bill_invests> page = BillInvests.queryMyInvestBillReceivables(investDetail.bid_id,investDetail.user_id, investDetail.invest_id, currPage, pageSize, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "系统繁忙，给您带来的不便敬请谅解");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		int totle = page.page.size();
		
		if(totle > 0){
			for(int i = 0;i < totle;i++){
				t_bill_invests bill = page.page.get(i);
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("borrowTitle", bill.title);
				map.put("repayAmount", bill.receive_amount);
				map.put("isOverdue", bill.status);
				map.put("isRepay", bill.status);
				investBills.add(map);
			}
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("totalNum",  page.totalCount);
		jsonMap.put("list", investBills);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 投资子账户--投标记录
	 * @param parameters
	 * @return
	 */
	public static String investRecords(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		
		int pageSize = Constants.APP_PAGESIZE;
		String type = "0";
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		String userIdStr = parameters.get("id");
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_invest_records> page = Invest.queryUserInvestRecords(userId, currPage + "",pageSize + "", type, "",error);
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("totalNum", page.totalCount);
		jsonMap.put("list", page.page);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 投资子账户--等待满标的投资标
	 * @return
	 */
	public static String queryUserAllloaningInvestBids(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		
		Map<String,Object> jsonMap = new JSONObject();
		String userIdStr = parameters.get("id");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_user_waiting_full_invest_bids> page = Invest.queryUserWaitFullBids(userId, null, null, currPage, pageSize,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "系统繁忙，给您带来的不便敬请谅解");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("totalNum", page.totalCount);
		jsonMap.put("list", page.page);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 投资子账户---收款中的投资标列表
	 * @param parameters
	 * @return
	 */
	public static String queryUserAllReceivingInvestBids(Map<String, String> parameters){
		
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		String userIdStr = parameters.get("id");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		
		PageBean<v_receiving_invest_bids> page = Invest.queryUserAllReceivingInvestBids(userId, null, null, currPage, pageSize,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "系统繁忙，给您带来的不便敬请谅解");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("list", page.page);
		jsonMap.put("totalNum", page.totalCount);
		Logger.info(JSONObject.fromObject(jsonMap).toString());
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 转让债权
	 * @return
	 */
	public static String transferDebt(Map<String, String> parameters){
		
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id"))){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "传入用户id参数有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("transferTitle"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "标题有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("transferBP"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入参数有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("transferWay"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "转让方式有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("transferPeriods"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "转让期数有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("transferReason"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "转让原因有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("invest_id"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "投资id有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		System.out.println("invest_id"+parameters.get("invest_id"));
		String userIdStr = parameters.get("id");
		String transferTitle = parameters.get("transferTitle");
		String transferBPStr = parameters.get("transferBP");
		String transferWayStr = parameters.get("transferWay");
		String transferPeriodsStr = parameters.get("transferPeriods");
		String transferReason = parameters.get("transferReason");
		String assigneeName = parameters.get("assigneeName");
		long investId = Long.parseLong(parameters.get("invest_id"));
		
		boolean b = transferBPStr.matches("^[1-9][0-9]*$");
    	if(!b){
    		jsonMap.put("error", -3);
    		jsonMap.put("msg", "对不起！转让底价只能输入正整数");
			return JSONObject.fromObject(jsonMap).toString();
			
    	} 
    	
    	if(StringUtils.isBlank(transferTitle) || StringUtils.isBlank(transferPeriodsStr) || StringUtils.isBlank(transferReason) || StringUtils.isBlank(transferBPStr) ||
    			StringUtils.isBlank(transferWayStr)){
    		jsonMap.put("error", -3);
    		jsonMap.put("msg", "对不起！请正确设置各种参数");
			return JSONObject.fromObject(jsonMap).toString();
    	}
    	
    	if(error.code < 0 || investId < 0){
    		jsonMap.put("error", -3);
    		jsonMap.put("msg", "投资id有误");
    		
			return JSONObject.fromObject(jsonMap).toString();
    	}
    	
    	long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
    	if(error.code < 0){
    		jsonMap.put("error", "-2");
    		jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
    		
			return JSONObject.fromObject(jsonMap).toString();
    	}
    	
		double transferBP = Double.parseDouble(transferBPStr);
		int transferWay = Integer.parseInt(transferWayStr);
		int transferPeriods = Integer.parseInt(transferPeriodsStr);
		
		double debtAmount = Debt.getDebtAmount(Long.parseLong(parameters.get("invest_id")),error);
		
		Debt.transferDebt(userId, investId, transferTitle, transferReason, transferPeriods, debtAmount, transferBP, transferWay, assigneeName, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
    		jsonMap.put("msg", error.msg);
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
    		jsonMap.put("msg", error.msg);
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 投资子账户--已成功的投资标
	 * @param parameters
	 * @return
	 */
	public static String queryUserSuccessInvestBids(Map<String, String> parameters){
		
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		String userIdStr = parameters.get("id");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error","-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_user_success_invest_bids> page = Invest.queryUserSuccessInvestBids(userId, null, null, currPage, pageSize,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "对不起，系统繁忙");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("list", page.page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 债权转让管理
	 * @param parameters
	 * @return
	 */
	public static String queryUserAllDebtTransfers(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		String userIdStr = parameters.get("id");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error","-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_debt_user_transfer_management> page = Debt.queryUserAllDebtTransfersByConditions( userId, null, null, null, currPage,pageSize);
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("list", page.page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 债权转让成功详情页面
	 * @return
	 */
	public static String debtDetailsSuccess(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String creditorIdStr = parameters.get("sign");
		
		if(StringUtils.isBlank(creditorIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(creditorIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || debtId < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_debt_user_transfer_management debt = Debt.transferDetails(debtId,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "对不起，系统繁忙");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Debt debtBussiness = new Debt();
		debtBussiness.id = debtId;
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("transferStatus", debt.status);
		jsonMap.put("transferType", debt.type);
		jsonMap.put("assigneeName", debtBussiness.invest.user.name);
		jsonMap.put("successTransferTime", debtBussiness.transactionTime + "");
		
		if(debt.remain_received_corpus == null){
			jsonMap.put("collectCapital", 0);
		}else{
			jsonMap.put("collectCapital", debt.remain_received_corpus);
		}
		
		jsonMap.put("collectBid", debtBussiness.transactionPrice);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 债权转让中详情页面
	 * @param parameters
	 * @return
	 */
	public static String debtDetailsTransfering(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String creditorIdStr = parameters.get("sign");
		
		if(StringUtils.isBlank(creditorIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(creditorIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || debtId < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_debt_user_transfer_management debt = Debt.transferDetails(debtId,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "对不起，系统繁忙");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Debt debtBussiness = new Debt();
		debtBussiness.id = debtId;
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("bidRemainTime", debt.end_time + "");
		jsonMap.put("hightestBid", debtBussiness.maxOfferPrice);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 债权转让不通过详情页面
	 * @param parameters
	 * @return
	 */
	public static String debtDetailsNoPass(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String creditorIdStr = parameters.get("sign");
		
		if(StringUtils.isBlank(creditorIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(creditorIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || debtId < 0){
			jsonMap.put("error", -3);
			jsonMap.put("msg", "解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_debt_user_transfer_management debt = Debt.transferDetails(debtId,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "对不起，系统繁忙");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		Debt debtBussiness = new Debt();
		debtBussiness.id = debtId;
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("auditResult", debt.status);
		jsonMap.put("auditTime", debtBussiness.startTime +"");
		jsonMap.put("nopassReason", debtBussiness.noThroughReason);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 债权转让详情
	 * @param parameters
	 * @return
	 */
	public static String debtTransferDetail(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String creditorIdStr = parameters.get("sign");
		
		if(StringUtils.isBlank(creditorIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(creditorIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || debtId < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_debt_user_transfer_management debt = Debt.transferDetails(debtId,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "对不起，系统繁忙");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Debt debtBussiness = new Debt();
		debtBussiness.id = debtId;
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("transferTitle", debtBussiness.title);
		jsonMap.put("transferBP", debt.transfer_price);
		jsonMap.put("transferDeadline", debt.end_time + "");
		jsonMap.put("transferReason", debtBussiness.transerReason);
		jsonMap.put("receiveCorpus",debtBussiness.map.get("receive_corpus"));
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 债权转让借款标详情页面
	 * @param parameters
	 * @return
	 */
	public static String debtTransferBidDetail(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String creditorIdStr = parameters.get("sign");
		
		if(StringUtils.isBlank(creditorIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(creditorIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || debtId < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_debt_user_transfer_management debt = Debt.transferDetails(debtId,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "对不起，系统繁忙");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Debt debtBussiness = new Debt();
		debtBussiness.id = debtId;
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("borrowid", debt.bid_id);
		jsonMap.put("borrowerName", debt.name);
		jsonMap.put("borrowType", debtBussiness.invest.bid.product.name);
		jsonMap.put("borrowTitle", debt.title);
		jsonMap.put("bidCapital", debtBussiness.invest.amount);
		jsonMap.put("annualRate", debt.apr);
		jsonMap.put("interestSum", debt.receiving_amount);
		jsonMap.put("receivedAmount", debt.has_received_amount);
		jsonMap.put("expiryDate", debtBussiness.invest.bid.recentRepayTime + "");
		jsonMap.put("collectCapital", debt.remain_received_corpus);
		jsonMap.put("remain_received_amount", debt.remain_received_amount);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 成交债权
	 * @param parameters
	 * @return
	 */
	public static String transact(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String creditorIdStr = parameters.get("sign");
		String dealpwd = parameters.get("dealpwd");
		if(StringUtils.isBlank(creditorIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(creditorIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || debtId < 0){
			jsonMap.put("error","-3");
			jsonMap.put("msg", "解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}


		Integer status = Debt.queryStatusById(debtId);

		if(status != Constants.DIRECTIONAL_MODE && status != Constants.AUCTION_MODE && status != Constants.WAIT_CONFIRM){
			jsonMap.put("error","-3");
			jsonMap.put("msg", "当前债权已失败，操作无效");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		
		String pMerBillNo = User.createBillNo();
		
		//2015年9月30日14:47:01 zqs
		Map<String, Object> map = Debt.queryTransferInfo(debtId, error);
		User user = new User();
		user.id = Long.parseLong(map.get("toUserId").toString()) ;
		if (error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
//		double maxOfferPrice= Debt.queryMaxPrice(debtId, error);
//		if(maxOfferPrice>user.balance){
//			jsonMap.put("error", "-999");
//			jsonMap.put("msg", "您余额不足，请充值");
//			
//			return JSONObject.fromObject(jsonMap).toString();
//		}
		
		Debt.updateMerBillNo(debtId, pMerBillNo, error);
		
		if (error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Map<String, Object> maps = new HashMap<String, Object>();
		maps = PaymentProxy.getInstance().debtorTransfer(error, Constants.CLIENT_APP, debtId, dealpwd);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "竞拍成功");
		jsonMap.put("htmlParam", maps.get("htmlParam"));
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 债权用户初步成交债权，之后等待竞拍方确认成交
	 * @param sign
	 */
	public static String firstDealDebt(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String creditorIdStr = parameters.get("sign");
		
		if(StringUtils.isBlank(creditorIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(creditorIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || debtId < 0){
			jsonMap.put("error","-2");
			jsonMap.put("msg", "解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}

		Debt.firstDealDebt(debtId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 查询债权竞拍记录
	 * @param parameters
	 * @return
	 */
	public static String queryAuctionRecords(Map<String, String> parameters){
		
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String sign = parameters.get("sign");
		
		if(StringUtils.isBlank(sign)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(sign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || debtId < 0){
			jsonMap.put("error","-3");
			jsonMap.put("msg", "解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_debt_auction_records> page = Invest.viewAuctionRecords(currPage,pageSize, debtId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "对不起，系统繁忙");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("list",page.page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 查询用户受让债权管理列表
	 * @param parameters
	 * @return
	 */
	public static String queryUserAllReceivedDebtTransfers(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		int currPage = 1;
		
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String userIdStr = parameters.get("id");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg","解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_debt_user_receive_transfers_management> page = Debt.queryUserAllReceivedDebtTransfersByConditions(userId, null, null,  currPage,pageSize);
		
		jsonMap.put("totalNum", page.totalCount);
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询正常");
		jsonMap.put("list", page.page);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 受让债权的详情 [竞拍成功]
	 * @return
	 */
	public static String receiveDebtDetailSuccess(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String debtIdStr = parameters.get("signId");
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(debtIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg","解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_debt_user_receive_transfers_management debt = Debt.details(debtId);
		
		Debt debtBussiness = new Debt();
		debtBussiness.id = debt.transer_id;
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("assigneeStatus", debt.status);
		jsonMap.put("assigneeWay", debt.type);
		jsonMap.put("assigneeName", debtBussiness.invest.user.name);
		jsonMap.put("successTransferTime", debt.transaction_time + "");
		jsonMap.put("collectCapital", debtBussiness.debtAmount);
		jsonMap.put("collectBid", debtBussiness.transactionPrice);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 受让债权的详情 [竞拍中]
	 * @return
	 */
	public static String receiveDebtDetailAuction(Map<String, String> parameters){
		
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String debtIdStr = parameters.get("signId");
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(debtIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg","解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_debt_user_receive_transfers_management debt = Debt.details(debtId);
		
		//目前我的竞拍出价
		Double offerPrice = Debt.getMyAuctionPrice(debt.transer_id, debt.user_id, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "对不起，系统繁忙");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Debt debtBussiness = new Debt();
		debtBussiness.id = debt.transer_id;
		
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("assigneeStatus", debt.status);
		jsonMap.put("assigneeWay", debt.type);
		jsonMap.put("collectCapital", debtBussiness.debtAmount);
		jsonMap.put("hightestBid", debtBussiness.maxOfferPrice);
		jsonMap.put("offerPrice", offerPrice);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 债权受让详情 [竞拍成功,竞拍中,定向转让]
	 * @return
	 */
	public static String receiveDebtDetail(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String debtIdStr = parameters.get("signId");
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(debtIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg","解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_debt_user_receive_transfers_management debt = Debt.details(debtId);
		
		Debt debtBussiness = new Debt();
		debtBussiness.id = debt.transer_id;
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("transferName", debtBussiness.invest.user.name);
		jsonMap.put("transferTitle", debtBussiness.title);
		jsonMap.put("transferBP", debt.transfer_price);
		jsonMap.put("transferDeadline", debtBussiness.endTime + "");
		jsonMap.put("transferReason", debtBussiness.transerReason);
		jsonMap.put("debtAmount", debtBussiness.debtAmount);
		jsonMap.put("sign", debt.sign);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 受让的借款标详情 [竞拍成功,竞拍中,定向转让]
	 * @param parameters
	 * @return
	 */
	public static String receiveDebtBidDetail(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String debtIdStr = parameters.get("signId");
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(debtIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error","-3");
			jsonMap.put("msg","解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		v_debt_user_receive_transfers_management debt = Debt.details(debtId);
		
		Debt debtBussiness = new Debt();
		debtBussiness.id = debt.transer_id;
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("borrowid",  OptionKeys.getvalue(OptionKeys.LOAN_NUMBER, error).concat(debtBussiness.invest.bidId + ""));
		jsonMap.put("borrowerName", debt.name);
		jsonMap.put("borrowType", debtBussiness.invest.bid.product.name);
		jsonMap.put("borrowTitle", debt.title);
		jsonMap.put("bidCapital", debtBussiness.invest.amount);
		jsonMap.put("annualRate", debt.apr);
		jsonMap.put("expiryDate", debtBussiness.invest.bid.recentRepayTime + "");
		
		if (debt.status != 1) {
			jsonMap.put("interestSum", debt.receiving_amount);
			jsonMap.put("remain_received_amount", debt.remain_received_amount);
			jsonMap.put("receivedAmount", debt.has_received_amount);
			jsonMap.put("collectCapital", debt.remain_received_corpus);
		} else {
			jsonMap.put("interestSum", debt.receiving_amount_success);
			jsonMap.put("remain_received_amount", debt.remain_received_amount_success);
			jsonMap.put("receivedAmount", debt.has_received_amount_success);
			jsonMap.put("collectCapital", debt.remain_received_corpus_success);
		}
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 加价竞拍
	 * @param parameters
	 * @return
	 */
	public static String increaseAuction(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
 		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String debtIdStr = parameters.get("sign");
		String userIdStr = parameters.get("id");
		String offerPriceStr = parameters.get("NewBid");
		String dealpwdStr = parameters.get("dealpwd");
		
//		if(StringUtils.isBlank(dealpwdStr)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "请输入交易密码");
//			
//			return JSONObject.fromObject(jsonMap).toString();
//		}
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(offerPriceStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "对不起，出价不能为空");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		boolean b=offerPriceStr.matches("^[1-9][0-9]*$");
		
    	if(!b){
    		jsonMap.put("error", "-3");
			jsonMap.put("msg", "对不起，出价只能输入正整数");
			
			return JSONObject.fromObject(jsonMap).toString();
    	} 
    	
		long debtId = Security.checkSign(debtIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || debtId < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		int offerPrice = Integer.parseInt(offerPriceStr);
		
		Debt.auctionDebt(userId, offerPrice, debtId, dealpwdStr, Constants.CLIENT_APP, error);
		
		if(error.code == -999 ){
			jsonMap.put("error", "-999");
			jsonMap.put("msg", "您余额不足，请充值");
			
			return JSONObject.fromObject(jsonMap).toString();
		}else if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg",error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg",error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 接受定向转让债权
	 * @param parameters
	 * @return
	 */
	public static String acceptDebts(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String debtIdStr = parameters.get("sign");
		String dealpwd = parameters.get("dealpwd");
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(debtIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg","解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
	
		Integer status = Debt.queryStatusById(debtId);

		if(status != Constants.DIRECTIONAL_MODE && status != Constants.AUCTION_MODE && status != Constants.WAIT_CONFIRM){
			jsonMap.put("error","-3");
			jsonMap.put("msg", "当前债权已失败，操作无效");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		String pMerBillNo = User.createBillNo();
		//Map<String, Object> map = Debt.queryTransferInfo(debtId, error);
		
		if (error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Debt.updateMerBillNo(debtId, pMerBillNo, error);
		
		if (!Debt.isEnoughToDealDebt(debtId, error)) {
			jsonMap.put("error", error.code);
			jsonMap.put("msg", error.msg);
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		
		if (error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map = PaymentProxy.getInstance().debtorTransfer(error, Constants.CLIENT_APP, debtId, dealpwd);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询债权转让");
		jsonMap.put("htmlParam", map.get("htmlParam"));
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 拒绝接受定向债权转让
	 * @param parameters
	 * @return
	 */
	public static String notAccept(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String debtIdStr = parameters.get("sign");
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(debtIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg","解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Debt.refuseAccept(debtId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg", error.msg);
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 投资情况统计表
	 */
	public static String investStatistics(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String userIdStr = parameters.get("id");
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		List<v_bill_invest_statistics> statistic = v_bill_invest_statistics.find(" user_id = ?", userId).fetch(1, 100);
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("list", statistic);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 进入自动投标页面
	 * @param parameters
	 * @return
	 */
	public static String autoInvest(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		List<Map<String,Object>> creditLevelList = new ArrayList<Map<String,Object>>();
		String userIdStr = parameters.get("id");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		double balance = Invest.getUserBalance(userId);// 个人可用余额
		
		t_user_automatic_invest_options robot = Invest.getUserRobot(userId,error);
		
		if(null == robot){
			
			jsonMap.put("loanType", "");
		}else{
		
		    jsonMap.put("loanType", robot.loan_type.split(","));
		}
		
		if(null == robot){
			jsonMap.put("robot", robot);
			jsonMap.put("robotStatus", 2);
		}else{
			jsonMap.put("robot", robot);
			jsonMap.put("robotStatus", robot.status);
			if(robot.status){
				jsonMap.put("robotStatus", 1);
			}else{
				jsonMap.put("robotStatus", 0);
			}
		}
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "对不起，系统繁忙");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
        List<CreditLevel> creditLevels = CreditLevel.queryAllCreditLevels(error);//获取所有信用等级
		
		if(error.code < 0){
			jsonMap.put("error","-4");
			jsonMap.put("msg", "对不起，系统繁忙");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		int totle = creditLevels.size();
		
		if(totle > 0){
			for(int i = 0;i < totle;i++){
			  CreditLevel creditLevel = creditLevels.get(i);
			  Map<String,Object> map = new HashMap<String, Object>();
			  map.put("optionValue", creditLevel.order_sort);
			  map.put("optionText", creditLevel.name);
			  creditLevelList.add(map);
			}
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("creditLevelList",creditLevelList);
		jsonMap.put("balance", balance);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 设置投标机器人
	 * @param parameters
	 * @return
	 */
	public static String saveOrUpdateRobot(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String userIdStr = parameters.get("id");
		String bidAmountStr = parameters.get("bidAmount");
		String rateStartStr = parameters.get("rateStart");
		String rateEndStr = parameters.get("rateEnd");
		String deadlineStartStr = parameters.get("deadlineStart");
		String deadlineEndStr = parameters.get("deadlineEnd");
		
		String creditStartStr = parameters.get("creditStart");
		String creditEndStr = parameters.get("creditEnd");
		String remandAmountStr = parameters.get("remandAmount");
		String borrowWay = parameters.get("borrowWay");
		 
        if(StringUtils.isBlank(parameters.get("validType"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请正确设置有效期类型");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
        if(StringUtils.isBlank(parameters.get("validDate"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请正确设置有效期");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
        
        if(StringUtils.isBlank(parameters.get("minAmount"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请正确设置最小投资金额");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
        
        if(StringUtils.isBlank(parameters.get("maxAmount"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请正确设置最大投资金额");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
        
        int validType = Integer.parseInt(parameters.get("validType"));
        int validDate = Integer.parseInt(parameters.get("validDate"));
        double minAmount = Double.parseDouble(parameters.get("minAmount"));
        double maxAmount = Double.parseDouble(parameters.get("maxAmount"));
		
		if(StringUtils.isBlank(bidAmountStr) || StringUtils.isBlank(rateStartStr) || StringUtils.isBlank(rateEndStr) || StringUtils.isBlank(borrowWay)){
			
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请正确设置各种参数");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		boolean b=bidAmountStr.matches("^[1-9][0-9]*$");
    	if(!b){
    		jsonMap.put("error", "-3");
			jsonMap.put("msg", "对不起！投标金额只能输入正整数");
			
			return JSONObject.fromObject(jsonMap).toString();
    	} 
		
    	
    	if(!NumberUtil.isNumericDouble(rateStartStr)){
    		jsonMap.put("error", "-3");
			jsonMap.put("msg", "对不起！设置的最低利率必须是数字");
			
			return JSONObject.fromObject(jsonMap).toString();
    	}
    	
    	if(!NumberUtil.isNumericDouble(rateEndStr)){
    		jsonMap.put("error", "-3");
			jsonMap.put("msg", "对不起！设置的最高利率必须是数字");
			
			return JSONObject.fromObject(jsonMap).toString();
    	}
    	
    	if(!NumberUtil.isNumeric(remandAmountStr)){
    		jsonMap.put("error", "-3");
			jsonMap.put("msg", "对不起！设置的保留金额必须是数字");
			
			return JSONObject.fromObject(jsonMap).toString();
    	}
		
    	long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Invest.saveOrUpdateRobot(userId, validType, validDate, minAmount, maxAmount, bidAmountStr, rateStartStr, rateEndStr, deadlineStartStr, deadlineEndStr, creditStartStr, creditEndStr, remandAmountStr, borrowWay, error);
		
		t_user_automatic_invest_options robot = Invest.getUserRobot(userId,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "对不起！设置自动投标机器人失败");
			jsonMap.put("robotId", robot.id);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "设置自动投标机器人成功");
			jsonMap.put("robotId", robot.id);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 关闭投标机器人
	 * @param parameters
	 * @return
	 */
	public static String closeRobot(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String bobotIdStr = parameters.get("robotId");
		
		if(StringUtils.isBlank(bobotIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入机器人ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("user_id"))){
			jsonMap.put("error", "-2");
						jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long robotId = Long.parseLong(bobotIdStr);
		Invest.closeRobot(userId, robotId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			jsonMap.put("robotId", robotId);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg", error.msg);
			jsonMap.put("robotId", robotId);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 收藏的债权列表
	 * @param parameters
	 * @return
	 */
	public static String attentionDebts(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String userIdStr = parameters.get("id");
		String keywords = parameters.get("keywords");
		
		int currPage = 1;
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
			
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_user_attention_invest_transfers> page = new PageBean<v_user_attention_invest_transfers>();
		if(StringUtils.isBlank(keywords)){
		    page = Debt.queryUserAttentionDebtTransfers(userId, currPage, null, null, pageSize,error);
			
			if(error.code < 0){
				jsonMap.put("error", "-4");
				jsonMap.put("msg", "对不起，系统繁忙");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
		}else{
			page = Debt.queryUserAttentionDebtTransfers(userId, currPage, "1", keywords, pageSize,error);
			
			if(error.code < 0){
				jsonMap.put("error", "-4");
				jsonMap.put("msg", "对不起，系统繁忙");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("totalNum", page.totalCount);
		jsonMap.put("list", page.page);
		
		return JSONObject.fromObject(jsonMap).toString(); 
	}
	
	/**
	 * 收藏的借款标
	 * @param parameters
	 * @return
	 */
	public static String attentionBids(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String userIdStr = parameters.get("id");
		String keywords = parameters.get("keywords");
		
		int currPage = 1;
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
			
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_bid_attention> page = new PageBean<v_bid_attention>();
		
		if(StringUtils.isBlank(keywords)){
			page.totalCount = (int) v_bid_attention.count("  user_id = ?  ",userId);
			page.page = v_bid_attention.find("  user_id = ?  ",userId).fetch(currPage,pageSize);
		}else{
			page.totalCount = (int) v_bid_attention.count("  user_id = ? and  title like ?",userId,"%"+keywords+"%");
			page.page = v_bid_attention.find("  user_id = ? and  title like ?",userId,"%"+keywords+"%").fetch(currPage,pageSize);
		}
		jsonMap.put("error",-1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("totalNum",page.totalCount);
		jsonMap.put("list",page.page);
		
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 查询用户关注用户列表
	 * @param parameters
	 * @return
	 */
	public static String myAttentionUser(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String userIdStr = parameters.get("id");
		
		int currPage = 1;
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
			
		}
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_user_attention_info> page= User.queryAttentionUsers(userId, currPage, pageSize, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg","查询成功");
		jsonMap.put("totalNum",page.totalCount);
		jsonMap.put("list",page.page);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 用户黑名单
	 * @param parameters
	 * @return
	 */
	public static String blackList(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String userIdStr = parameters.get("id");
		
		int currPage = 1;
		if (parameters.get("currPage") != null) {
			currPage = Integer.parseInt(parameters.get("currPage"));
		}
		int pageSize = Constants.APP_PAGESIZE;
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
			
		}
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		PageBean<v_user_blacklist> page = User.queryBlacklist(userId, "", currPage,pageSize, error);
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("totalNum", page.totalCount);
		jsonMap.put("list", page.page);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 举报用户
	 * @param parameters
	 * @return
	 */
	public static String reportUser(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String userIdStr = parameters.get("id");
		String bidIdSign = parameters.get("bidIdSign");
		String reportedUserIdSign = parameters.get("bidUserIdSign");
		String investTransferIdSign = parameters.get("sign");
		String reason = parameters.get("reason");
		
		if(StringUtils.isBlank(reason)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "举报原因不能为空");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "举报人ID为空");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(reportedUserIdSign)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "被举报人ID为空");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error","-2");
			jsonMap.put("msg", "解析举报人ID出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long reportedUserId = Security.checkSign(reportedUserIdSign, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || reportedUserId < 0){
			jsonMap.put("error","-2");
			jsonMap.put("msg", "解析被举报人ID出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long investTransferId = 0;
		
		if(!StringUtils.isBlank(investTransferIdSign)){
		    investTransferId = Security.checkSign(investTransferIdSign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
			
			if(error.code < 0 || investTransferId < 0){
				jsonMap.put("error", "-2");
				jsonMap.put("msg", "解析债权id出现错误");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
		}
		
		long bidId = 0;
		if(!StringUtils.isBlank(bidIdSign)){
		    bidId = Security.checkSign(bidIdSign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
			
			if(error.code < 0 || bidId < 0){
				jsonMap.put("error", "-2");
				jsonMap.put("msg", "解析标id出现错误");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
		}
		
		String userName = User.queryUserNameById(reportedUserId,error);
		
		User user = new User();
		user.id = userId;
		user.addReportAUser(userName, reason, bidId, investTransferId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
	}
	
	/**
	 * 拉黑对方
	 * @param parameters
	 * @return
	 */
	public static String addBlack(Map<String, String> parameters){
		
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String userIdStr = parameters.get("id");
		String bidIdStr = parameters.get("bid_id");
		String reason = parameters.get("reason");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(bidIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入借款标ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long bidId = Long.parseLong(bidIdStr);
		
		if(error.code < 0 || bidId < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "标id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		User user = new User();
		user.id = userId;
		user.addBlacklist(bidId, reason, error);
		
		if(error.code < 0){
			jsonMap.put("error", -4);
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 关注用户
	 * @return
	 */
	public static String attentionUser(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String userIdStr = parameters.get("id");
		
		long attentionUserId = 0;
		
		String attentionBidUserIdStr = parameters.get("bidUserIdSign");
		String attentionDebtUserIdStr = parameters.get("debtUserIdSign");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(!StringUtils.isBlank(attentionBidUserIdStr)){
			 attentionUserId = Security.checkSign(attentionBidUserIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
				
				if(error.code < 0 || attentionUserId < 0){
					jsonMap.put("error", "-3");
					jsonMap.put("msg", "解析关注用户id出现错误");
					
					return JSONObject.fromObject(jsonMap).toString();
				}
		}
		
		if(!StringUtils.isBlank(attentionDebtUserIdStr)){
			 attentionUserId = Security.checkSign(attentionDebtUserIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
				
				if(error.code < 0 || attentionUserId < 0){
					jsonMap.put("error", "-3");
					jsonMap.put("msg", "解析关注用户id出现错误");
					
					return JSONObject.fromObject(jsonMap).toString();
				}
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(attentionUserId == userId){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "您不能关注您自己");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long attentionId = User.attentionUser(userId, attentionUserId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("attentionId", attentionId);
			jsonMap.put("msg", "关注成功");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 收藏借款标
	 * @return
	 */
	public static String collectBid(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String userIdStr = parameters.get("id");
		String bidIdStr = parameters.get("bidIdSign");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(bidIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入借款标ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long bidId = Security.checkSign(bidIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		 
		if(error.code < 0 || bidId < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "解析标id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long attentionBidId = Bid.collectBid(userId, bidId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error",-1);
			jsonMap.put("msg", error.msg);
			jsonMap.put("attentionBidId", attentionBidId);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 收藏债权
	 * @param parameters
	 * @return
	 */
	public static String collectDebt(Map<String, String> parameters){
		
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String userIdStr = parameters.get("id");
		String debtIdStr = parameters.get("sign");
		
		if(StringUtils.isBlank(userIdStr)){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请传入用户ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(debtIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入债权ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long debtId = Security.checkSign(debtIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		 
		if(error.code < 0 || debtId < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "解析债权id出现错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long attentionDebtId = Debt.collectDebt(userId, debtId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg", error.msg);
			jsonMap.put("attentionDebtId", attentionDebtId);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 进入帮助中心页面
	 * @return
	 */
	public static String helpCenter(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		List<v_news_types> types = NewsType.queryTypeAndCount(NewsTypeId.HELP_CENTER, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("list", types);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 帮助中心内容列表
	 * @param parameters
	 * @return
	 */
	public static String helpCenterContent(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		int currPage = 1;
		
		String typeId = parameters.get("id");
		String currPageStr = parameters.get("currPage");
		
		if(StringUtils.isBlank(typeId)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入帮助中心栏目ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		int pageSize = Constants.APP_PAGESIZE;
		
		if(StringUtils.isNotBlank(currPageStr)){
			currPage = Integer.parseInt(currPageStr);
		}
		
		PageBean <t_content_news> pageBean = News.queryNewsByTypeId(typeId+"", currPage +  "", pageSize + "", null, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("list", pageBean.page);
		jsonMap.put("totleNum", pageBean.totalCount);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 帮助中心列表详情
	 * @param parameters
	 * @return
	 */
	public static String helpCenterDetail(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		String newsIdStr = parameters.get("id");
		
		if(StringUtils.isBlank(newsIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入新闻ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long newsId = Long.parseLong(newsIdStr);
		News news = new News();
		news.id = newsId;
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("title", news.title);
		jsonMap.put("time", news.time);
		jsonMap.put("content", news.content);
		jsonMap.put("author", news.author);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 关于我们---公司介绍
	 * @param parameters
	 * @return
	 */
	public static String companyIntroduction(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		String content = News.queryContent(199, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg", "查询成功");
			jsonMap.put("content", content);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 关于我们---管理团队
	 * @param parameters
	 * @return
	 */
	public static String managementTeam(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		List<String> contentList = News.queryContentList(17, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg", "查询成功");
			jsonMap.put("content", contentList);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 关于我们---专家顾问
	 * @param parameters
	 * @return
	 */
	public static String expertAdvisor(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		List<String> contentList = News.queryContentList(18, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg", "查询成功");
			jsonMap.put("content", contentList);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 关于我们---招贤纳士
	 * @param parameters
	 * @return
	 */
	public static String recruitment(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		String content = News.queryContent(200, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}else{
			jsonMap.put("error", -1);
			jsonMap.put("msg", "查询成功");
			jsonMap.put("content", content);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
	}
	
	/**
	 * 关于我们---合作伙伴
	 * @param parameters
	 * @return
	 */
	public static String partners(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String currPageStr = parameters.get("currPage");
		
		int currPage = 1;
		
		if(StringUtils.isNotBlank(currPageStr)){
			currPage = Integer.parseInt(currPageStr);
		}
		int pageSize = Constants.APP_PAGESIZE;
		
		PageBean<t_content_advertisements_partner> page = News.queryPartners(currPage, pageSize, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("totle", page.totalCount);
		jsonMap.put("list", page.page);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 获取APP版本
	 * @param parameters
	 * @return
	 */
	public static String appVersion(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		String deviceTypeStr = parameters.get("deviceType");
		
		if(StringUtils.isBlank(deviceTypeStr)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入设备参数");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(!NumberUtil.isNumeric(parameters.get("deviceType"))) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入正确的设备参数");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		int deviceType = Integer.parseInt(parameters.get("deviceType"));
		
		BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
		
		jsonMap.put("version", deviceType == 1 ? backstageSet.androidVersion : backstageSet.iosVersion);
		jsonMap.put("code", deviceType == 1 ? backstageSet.androidCode : backstageSet.iosCode);
		jsonMap.put("path", deviceType == 1 ? "/public/sp2p6.0.apk" : "/public/sp2p6.0.apk");
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 获取客服热线
	 * @param parameters
	 * @return
	 */
	public static String serviceHotline(Map<String, String> parameters){
		BackstageSet  currBackstageSet = BackstageSet.getCurrentBackstageSet();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("hotline", currBackstageSet.platformTelephone);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 财富资讯新闻详情
	 * @return
	 */
	public static String newsDetail(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		String newsIdStr = parameters.get("id");
		
		if(StringUtils.isBlank(newsIdStr)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请传入新闻ID");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long newsId = Long.parseLong(newsIdStr);
		News news = new News();
		news.id = newsId;
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("title", news.title);
		jsonMap.put("time", news.time);
		jsonMap.put("content", news.content);
		jsonMap.put("author", news.author);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 财富资讯首页
	 * @param parameters
	 * @return
	 */
	public static String wealthinfoHome(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		List<NewsType> types = NewsType.queryChildTypes(1L, error);//获取财富资讯首页所有栏目
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		List<t_content_news> homeNews = News.queryNewForFrontHome(error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("types",types);
		jsonMap.put("ads",homeNews);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 财富资讯各个栏目下的新闻列表
	 * @param parameters
	 * @return
	 */
	public static String wealthinfoNewsList(Map<String, String> parameters){
		ErrorInfo error = new ErrorInfo();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(parameters.get("currPage"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "当前页数有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		int pageSize = Constants.APP_PAGESIZE;
		String typeId = parameters.get("id");
		PageBean <t_content_news>  newsList = News.queryNewsByTypeId(typeId, parameters.get("currPage"), pageSize + "", "", error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("totleNum",newsList.totalCount);
		jsonMap.put("list", newsList.page);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * 首页
	 * @param parameters
	 * @return
	 */
	public static String home(Map<String, String> parameters){
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		List<t_content_advertisements> homeAds = Ads.queryAdsByLocation(Constants.HOME_PAGE_APP, error); // 广告条
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询广告条失败");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		//List<Bid> bids = Bid.queryAdvertisement(error); //最新三条借款资讯
//		if(null != bids) {
//			for(Bid bid : bids) {
//				String name = bid.userName;
//				if(null != name && name.length() > 1) {
//					bid.userName = bid.userName.substring(0, 1) + "***";
//				}
//			}
//		}
		
		
//		if(error.code < 0){
//			jsonMap.put("error", "-4");
//			jsonMap.put("msg", "查询最新三条借款资讯失败");
//			
//			return JSONObject.fromObject(jsonMap).toString();
//		}
		
		//List<QualityBid> qualityBids = BidOZ.queryQualityBid2(3, error);//三个优质借款标
		List<QualityBid> qualityBids = BidOZ.queryBidsFive(error);//五个标的
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询借款标失败");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
//		List<Map<String,String>> maps = Invest.queryNearlyInvest(error); // 最新投资资讯
//		if(null != maps) {
//			for(Map<String, String> map : maps) {
//				String userName = map.get("userName");
//				if(null != userName && userName.length() > 1) {
//					map.put("userName", map.get("userName").substring(0, 1) + "***");
//				}
//			}
//		}
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询 最新投资资讯失败");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
//		List<FullBidsApp> fullBids = BidOZ.queryFullBid(error); // 最新满标借款标
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询最新满标借款标失败");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询成功");
		jsonMap.put("homeAds", homeAds);//
		//jsonMap.put("bids", bids);
		jsonMap.put("qualityBids", qualityBids);//
		//jsonMap.put("investInfo",maps);
//		jsonMap.put("fullBids", fullBids);
		//jsonMap.put("invests", maps);
		
		return JSONObject.fromObject(jsonMap).toString();
	}

	/**
	 * 用户登录(opt=1)
	 * @param name 用户名
	 * @param pwd 密码
	 * @throws IOException
	 */
	public static String login(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String name = parameters.get("name");
		String password = parameters.get("pwd");
		String userId = parameters.get("userId");
		String channelId = parameters.get("channelId");
		String deviceType = parameters.get("deviceType");
		
		if (StringUtils.isBlank(name)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请输入用户名");
			return JSONUtils.printObject(jsonMap);
		}
		
		if (StringUtils.isBlank(password)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请输入密码");
			return JSONUtils.printObject(jsonMap);
		}
		
		password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);
//		if (StringUtils.isBlank(userId)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "无法获取设备参数");
//			return JSONUtils.printObject(jsonMap);
//		}
//		
//		if (StringUtils.isBlank(channelId)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "无法获取设备参数");
//			return JSONUtils.printObject(jsonMap);
//		}
//		
//		if (!NumberUtil.isNumeric(deviceType)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "无法获取设备参数");
//			return JSONUtils.printObject(jsonMap);
//		}
		
		int device = Integer.parseInt(deviceType);
		
		if(device < 0 || device > 2) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "获取设备参数有误");
			return JSONUtils.printObject(jsonMap);
		}
		
		User user = new User();
		name = StringUtils.trimToEmpty(name);
		if(RegexUtils.isEmail(name)){
			user.findUserByEmail(name);
		}else if(RegexUtils.isMobileNum(name)){
			user.findUserByMobile(name);
		}else{
			user.name = name;
		}
		
		if (user.id < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该用户不存在");
			return JSONUtils.printObject(jsonMap);
		}
		
		if (user.login(password,false, Constants.CLIENT_APP, error) < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(channelId) && NumberUtil.isNumeric(deviceType)) {
			user.updateChannel(userId, channelId, device, error);
		}
		BackstageSet bs = BackstageSet.getCurrentBackstageSet();
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "登录成功");
		jsonMap.put("id", user.sign);
		jsonMap.put("username", user.name);
		jsonMap.put("headImg", user.photo);
		jsonMap.put("vipStatus", user.vipStatus);
		jsonMap.put("isEmailVerified", user.isEmailVerified);
		jsonMap.put("isAddBaseInfo", user.isAddBaseInfo);
		jsonMap.put("creditRating", user.myCredit.imageFilename);
		jsonMap.put("creditLimit", user.balanceDetail.credit_line);
		jsonMap.put("accountAmount", user.balance + user.freeze);
		jsonMap.put("availableBalance", user.balance);
		
		jsonMap.put("hasEmail", StringUtils.isNotBlank(user.email));
		jsonMap.put("hasMobile",StringUtils.isNotBlank(user.mobile));
		jsonMap.put("sum_income", BillInvests.querySumIncome(user.id));
		jsonMap.put("telephone",bs.platformTelephone);
		jsonMap.put("pwd",user.getPassword());
		//登录成功记录用户信息
		Cache.set("userId_"+user.id, user, Constants.CACHE_TIME_HOURS_12);
		return JSONUtils.printObject(jsonMap);
	}
	
	/**
	 * 账户基本信息(opt=2)
	 * @param id 用户id
	 * @throws IOException
	 */
	public static String queryBaseInfo(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		User user = new User();
		
		if (StringUtils.isBlank(parameters.get("id"))) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请求用户id参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		user.id = userId;
		
		if (user.id < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该用户不存在");
			return JSONUtils.printObject(jsonMap);
		}
		
		List<t_dict_cars> cars = t_dict_cars.findAll();
		List<t_dict_ad_provinces> provinces = t_dict_ad_provinces.findAll();
		List<t_dict_educations> educations = t_dict_educations.findAll();
		List<t_dict_houses> houses = t_dict_houses.findAll();
		List<t_dict_maritals> maritals = t_dict_maritals.findAll();
		List<t_dict_ad_citys> cityList = t_dict_ad_citys.findAll();
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "该用户存在");
		jsonMap.put("realName", user.realityName);
		jsonMap.put("email", user.email);
		jsonMap.put("sex", user.sex);
		
		if(user.age <= 0) {
			jsonMap.put("age", "");
		} else {
			jsonMap.put("age", user.age);
		}
		if (!User.isIPSacctNoExist(userId, error)) {
			jsonMap.put("error", "-31");
			jsonMap.put("msg", "用户未开通资金托管账号");
			Map<String, Object> map=new HashMap<String, Object>();
			map = PaymentProxy.getInstance().register(error,Constants.CLIENT_APP, user);
			jsonMap.put("htmlParam", map.get("htmlParam"));
		}
		jsonMap.put("idNo", user.idNumber);
		jsonMap.put("higtestEdu", user.educationId);
		jsonMap.put("registedPlacePro", user.provinceId);
		jsonMap.put("maritalStatus", user.maritalId);
		jsonMap.put("housrseStatus", user.houseId);
		jsonMap.put("CarStatus", user.carId);
		jsonMap.put("cellPhone1", user.mobile);
		jsonMap.put("randomCode1", null);
		jsonMap.put("randomCode2", null);
		jsonMap.put("registedPlaceCity", user.cityId);
		jsonMap.put("carList", cars);
		jsonMap.put("provinceList", provinces);
		jsonMap.put("educationsList", educations);
		jsonMap.put("housesList", houses);
		jsonMap.put("maritalsList", maritals);
		jsonMap.put("cityList", cityList);
		jsonMap.put("isAddBaseInfo", user.isAddBaseInfo);
		
		return JSONUtils.printObject(jsonMap);
	}

	/*
	 * 注册用户(opt=3)
	 * name 用户名
	 * email 邮箱
	 * pwd 密码
	 * referrerName
	 */
	public static String register(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String name = (String)parameters.get("name");
		//String email = (String)parameters.get("email");
		String mobile = parameters.get("mobile");
		String password = (String)parameters.get("pwd");
		String referrerName = (String)parameters.get("referrerName");
		String randomCode = parameters.get("code");
		if(Constants.CHECK_MSG_CODE) {
			String cCode = (Cache.get(mobile)).toString();
			
			if(cCode == null) {
				jsonMap.put("error", "-4");
				jsonMap.put("msg","验证码已失效，请重新点击发送验证码");
				
				return JSONUtils.printObject(jsonMap);
			}
			
			if(!randomCode.equals(cCode)) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg","手机验证错误");
				
				return JSONUtils.printObject(jsonMap);
			}
		}
		
		User.isNameExist(name, error);
		
		password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);
		
		if (!RegexUtils.isValidPassword(password)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请填写符合要求的密码");
			return JSONUtils.printObject(jsonMap);
		}
		
		if (error.code == -2) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该用户名已存在");
			return JSONUtils.printObject(jsonMap);
		}
		
		if (!RegexUtils.isValidUsername(name)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请填写符合要求的用户名");
			return JSONUtils.printObject(jsonMap);
		}

//		if (!RegexUtils.isEmail(email)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "请填写正确的邮箱地址");
//			return JSONUtils.printObject(jsonMap);
//		}
		
		if(StringUtils.isNotBlank(referrerName)) {
			
			if (name.equalsIgnoreCase(referrerName)) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "推荐人不能为自己");
				return JSONUtils.printObject(jsonMap);
			}
			
			User.isNameExist(referrerName, error);
			if (-2 != error.code) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "推荐人不存在");
				return JSONUtils.printObject(jsonMap);
			}
			
		}
		
		//User.isEmailExist(email, null, error);

//		if (error.code == -2) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "该邮箱已注册");
//			return JSONUtils.printObject(jsonMap);
//		}

		User user = new User();

		user.time = new Date();
		user.name = name;
		user.mobile = mobile;
		user.password = password;
		//user.email = email;
		user.recommendUserName = referrerName;
		
		user.register(Constants.CLIENT_APP, error);

		if (error.code < 0 ) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "数据库异常");
			return JSONUtils.printObject(jsonMap);
		}
		
		user.name = name;
		
		if (user.id < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "数据库异常");
			return JSONUtils.printObject(jsonMap);
		}
		
		
		Map<String, Object> map=new HashMap<String, Object>();
		map = PaymentProxy.getInstance().register(error,Constants.CLIENT_APP, user);
		jsonMap.put("htmlParam", map.get("htmlParam"));
		
		
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "注册成功");
		jsonMap.put("id", user.sign);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 发送短信验证码(opt=4)
	 * cellPhone 手机号码
	 */
	public static String findPwdBySms(Map<String, String> parameters) throws IOException{
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		String mobile = parameters.get("mobile");
		String verify = parameters.get("verify");
		if(!RegexUtils.isMobileNum(mobile)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请输入正确的手机号码");
			
			return JSONUtils.printObject(jsonMap);
		}
		User.isMobileExist(mobile, null, error);
		
		
		if("0".equals(verify)){
			if(error.code==-2){
				jsonMap.put("error", "-5");
				jsonMap.put("msg", error.msg);
				
				return JSONUtils.printObject(jsonMap);
			}
		}
		if("1".equals(verify)){
			if(error.code!=-2){
				jsonMap.put("error", "-5");
				jsonMap.put("msg", "手机号码不存在");
				
				return JSONUtils.printObject(jsonMap);
			}
		}
		SMSUtil.sendCodeForfindPwd(mobile, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", error.msg);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 找回密码-验证码确认(opt=5)
	 * cellPhone 手机号码
	 * randomCode 验证码
	 */
	public static String confirmCode(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String mobile = parameters.get("cellPhone");
		String randomCode = parameters.get("randomCode");
		
		if(StringUtils.isBlank(mobile)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg","请输入手机号码");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(randomCode)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg","请输入验证码");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(!RegexUtils.isMobileNum(mobile)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg","请输入正确的手机号码");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        User.queryIdByMobile(mobile, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该手机号码不存在");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(Constants.CHECK_MSG_CODE) {
			String cCode = (Cache.get(mobile)).toString();
			
			if(cCode == null) {
				jsonMap.put("error", "-4");
				jsonMap.put("msg","验证码已失效，请重新点击发送验证码");
				
				return JSONUtils.printObject(jsonMap);
			}
			
			if(!randomCode.equals(cCode)) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg","手机验证错误");
				
				return JSONUtils.printObject(jsonMap);
			}
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg","手机验证成功");
		
		return JSONUtils.printObject(jsonMap);
		
	}
	
	/*
	 * 重置密码-提交新密码(opt=6)
	 * cellPhone 手机号码
	 * newpwd 新密码
	 */
	public static String commitPassword(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("cellPhone"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "手机号码有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("newpwd"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "新密码有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		String mobile = parameters.get("cellPhone");
		String password = parameters.get("newpwd");
		
		password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);
		
		User.updatePasswordByMobileApp(mobile, password, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg",error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg",error.msg);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 绑定手机号码接口(opt=7)
	 * cellPhone 手机号码
	 * randomCode 验证码
	 * id 用户id
	 */
	public static String saveCellphone(Map<String, String> parameters) throws IOException{
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(parameters.get("cellPhone"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "手机号码有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("randomCode"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "验证码有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("id"))){
			jsonMap.put("error", "-2");
						jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		String mobile = parameters.get("cellPhone");
		String code = parameters.get("randomCode");
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		User user = new User();
		user.id = userId;
		
        user.checkMoible(mobile, code, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg",error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg",error.msg);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 注册服务协议(opt=8)
	 */
	public static String ServiceAgreement(Map<String, String> parameters) throws IOException{
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		String content = News.queryContent(Constants.NewsTypeId.REGISTER_AGREEMENT2, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg",error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg","获取协议成功");
		jsonMap.put("content",content);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 还款计算器(opt=9)
	 */
	public static String RepaymentCalculator(Map<String, String> parameters) throws IOException{
		
		List<Map<String, Object>> payList = new ArrayList<Map<String, Object>>();
//		List<Object> listOrder = new ArrayList<Object>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		double dayRate = 0;
		double monRate = 0;
		double monPay = 0;
		double paySum = 0;
		
		if(StringUtils.isBlank(parameters.get("borrowSum"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款金额有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("yearRate")) || Double.parseDouble(parameters.get("yearRate")) <= 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "年利率有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("borrowTime")) || !NumberUtil.isNumeric(parameters.get("borrowTime"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款期限有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(Integer.parseInt(parameters.get("borrowTime")) > 60){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "最大借款期限为60个月，请重新输入");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("isDay"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "是否为天标有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("repayWay"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "还款方式有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		double amount = Double.parseDouble(parameters.get("borrowSum"));
		double apr = Double.parseDouble(parameters.get("yearRate"));
		int period = Integer.valueOf(parameters.get("borrowTime"));
		int periodUnit = Integer.valueOf(parameters.get("isDay"));
		int repaymentType = Integer.valueOf(parameters.get("repayWay"));
		monRate = apr /1200;
		
		if(periodUnit == 1){//天标
			dayRate = Arith.div(apr, 36000, 2);
			paySum = amount + Arith.mul(dayRate, period); 
			monPay = paySum;
		}else{
			if(repaymentType == Constants.PAID_MONTH_EQUAL_PRINCIPAL_INTEREST){//等额本息
				double monPays = Double.valueOf(Arith.mul(amount, monRate) * Math.pow((1 + monRate), period))/ 
				Double.valueOf(Math.pow((1 + monRate), period) - 1);//每个月要还的本金和利息
				paySum = Arith.round(Arith.mul(monPays, period),2);
				monPay = monPays;
			}
			
			else if(repaymentType == Constants.PAID_MONTH_ONCE_REPAYMENT){// 先息后本
				monPay = Arith.round(Arith.mul(amount, monRate), 2);
				paySum = Arith.add(amount, Arith.mul(monPay, period));
				
			}
			
			else if(repaymentType == Constants.ONCE_REPAYMENT){
				double payMon = Arith.round(Arith.mul(amount, monRate), 2);
				paySum = Arith.add(amount, Arith.mul(payMon, period));
				monPay = paySum;
				
			}
		}
		
		payList = Bill.repaymentCalculate(amount, apr, period, periodUnit, repaymentType);
		
		jsonMap.put("monRate", Arith.round(monRate*100,2));
		jsonMap.put("allPay", paySum);
		jsonMap.put("monPay", monPay);
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "计算成功");
		jsonMap.put("list", payList);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/*
	 * 借款产品列表
	 */
	public static String loanProduct(Map<String, String> parameters) throws IOException{
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		List<Product> products = Product.queryProduct(Constants.SHOW_TYPE_2, error);
		
        JsonConfig config = new JsonConfig();  
		
		config.setExcludes(new String[]{
				"sign", 
				"repaymentType",
				"strLoanType",
				"periodYearArray",
				"periodMonthArray",
				"periodDayArray",
				"investPeriodArray",
				"requiredAuditItem",
				"selectAuditItem",
				"lables"
		}); 
		JSONArray array = JSONArray.fromObject(products, config);
		
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "数组数据异常");
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询借款产品列表成功");
		jsonMap.put("totalNum", products.size());
		jsonMap.put("list", array);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/*
	 * 借款标产品详情
	 * productId 产品id
	 */
	public static String productInfo(Map<String, String> parameters) throws IOException{
		String productId = parameters.get("productId");
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(parameters.get("productId"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传参借款产品id有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(parameters.get("id") == null) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "传入参数id有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		long userId = 0;
		
		if(!"".equals(parameters.get("id"))) {
			userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		}
		
		Product product = new Product();
		product.id = Long.valueOf(productId);
		
		User user = null;
		
		if(userId > 0) {
			user = new User();
			user.id = userId;
		}

		if (user!=null&&!User.isIPSacctNoExist(userId, error)) {
			jsonMap.put("error", "-31");
			jsonMap.put("msg", "用户未开通资金托管账号");
			Map<String, Object> map=new HashMap<String, Object>();
			map = PaymentProxy.getInstance().register(error,Constants.CLIENT_APP, user);
			jsonMap.put("htmlParam", map.get("htmlParam"));
		}
		
		
		/* 手续费常量值 */
	    BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
	    double strfee = backstageSet.borrowFee;
	    double borrowFeeMonth = backstageSet.borrowFeeMonth;
	    double borrowFeeRate = backstageSet.borrowFeeRate;

		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询借款产品详情成功");
		jsonMap.put("isLogin", userId > 0 ? true : false);
		jsonMap.put("isEmailVerified", userId > 0 ? user.isEmailVerified : false);
		jsonMap.put("isAddBaseInfo", userId > 0 ? user.isAddBaseInfo : false);
		jsonMap.put("productFeatures", product.characteristic);
		jsonMap.put("suitsCrowd", product.fitCrowd);
		jsonMap.put("limitRange", new DecimalFormat("###,##0.00").format(product.minAmount)+"-"+new DecimalFormat("###,##0.00").format(product.maxAmount));
		jsonMap.put("loanRate", Double.toString(product.minInterestRate)+"-"+ Double.toString(product.maxInterestRate));
		jsonMap.put("monRate", Double.toString(product.monthMinApr)+"-"+ Double.toString(product.monthMaxApr));
		jsonMap.put("periodYear", product.periodYear);
		jsonMap.put("periodYearArray", product.periodYearArray);
		jsonMap.put("periodMonth", product.periodMonth);
		jsonMap.put("periodMonthArray", product.periodMonthArray);
		jsonMap.put("periodDay", product.periodDay);
		jsonMap.put("periodDayArray", product.periodDayArray);
		jsonMap.put("tenderTime", product.investPeriod);
		jsonMap.put("tenderTimeArray", product.investPeriodArray);
		jsonMap.put("uditTime", product.auditCycle);
		jsonMap.put("repayWay", product.repaymentType);
		//jsonMap.put("poundage", "借款期限"+borrowFeeMonth+"个月（含）以下，借款成功后，收取本金的"+strfee+"%；借款期限"+borrowFeeMonth+"个月以上，借款成功后，收取本金的"+strfee+"%以外，还另外收取超过月份乘本金的"+borrowFeeRate+"%（不成功不收取成交服务费）");
		jsonMap.put("poundage", "借款成功后，由借贷服务端根据借款金额按月一次性收取；（不成功不收取借款服务费）");
		jsonMap.put("reviewMaterial", product.requiredAuditItem);
		jsonMap.put("optReviewMaterial", product.selectAuditItem);
		jsonMap.put("applyconditons", product.applicantCondition);
	
		jsonMap.put("isIps", user!=null?user.getIpsStatus():4);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/*
	 * 获取借款产品信息
	 * productId 产品id
	 */
	public static String productDetails(Map<String, String> parameters) throws IOException{
		String productId = parameters.get("productId");
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		Product product = new Product();
		
		if(StringUtils.isBlank(parameters.get("productId"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传参有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		product.id = Long.valueOf(productId);

		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询借款产品详情成功");
		jsonMap.put("maxCopies", product.maxCopies);
		jsonMap.put("periodYear", product.periodYear);
		jsonMap.put("periodYearArray", product.periodYearArray);
		jsonMap.put("periodMonth", product.periodMonth);
		jsonMap.put("periodMonthArray", product.periodMonthArray);
		jsonMap.put("periodDay", product.periodDay);
		jsonMap.put("periodDayArray", product.periodDayArray);
		/* 借款用途  */
		List<Purpose> purpose = Purpose.queryLoanPurpose(error, true);
		jsonMap.put("purpose", purpose);
		jsonMap.put("minInvestAmount", product.minInvestAmount);
		jsonMap.put("fullyTimeLimit", product.investPeriod);//满标期限
		jsonMap.put("tenderTimeArray", product.investPeriodArray);
		jsonMap.put("minInterestRate", product.minInterestRate);
		jsonMap.put("maxInterestRate", product.maxInterestRate);
		jsonMap.put("repaymentTypeId", product.repaymentTypeId);
		jsonMap.put("repayWay", product.repaymentType);
		jsonMap.put("loanImageType", product.loanImageType);
		jsonMap.put("loanImageFilename", product.loanImageFilename);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/*
	 * 发布借款
	 * productId 产品id
	 * purposeId 借款目的id
	 * title 标题
	 * amount 借款金额
	 * periodUnit 期限单位
	 * period 期限
	 * investPeriod  天标满标期限
	 * repaymentId 还款方式id
	 * minInvestAmount 最小投资金额
	 * averageInvestAmount 平均投资金额
	 * apr 年利率
	 * bonusType 奖励方式
	 */
	public static String createBid(Map<String, String> parameters) throws IOException{
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Bid bid = new Bid();
		
		bid.purpose = new Purpose();
		bid.repayment = new Repayment();
		
		bid.productId = Long.valueOf(parameters.get("productId"));  // 填充产品对象
		
		if(StringUtils.isBlank(parameters.get("purposeId")) || Long.valueOf(parameters.get("purposeId")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款用途有误!");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("title")) || parameters.get("title").length() > 24 ){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款标题有误!");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
//		int _amount = Integer.valueOf(parameters.get("amount"));
		
//		if(StringUtils.isBlank(parameters.get("amount")) || Double.parseDouble(parameters.get("amount")) <= 0 ||
//				 Double.parseDouble(parameters.get("amount"))
//				< bid.product.minAmount || Double.parseDouble(parameters.get("amount")) > bid.product.maxAmount){
//			jsonMap.put("error", "1");
//			jsonMap.put("msg", "借款金额有误!");
//			
//			return JSONObject.fromObject(jsonMap).toString();
//		}
		
		if(StringUtils.isBlank(parameters.get("periodUnit"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款期限单位有误!");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("period"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款期限有误!");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		switch (Integer.valueOf(parameters.get("periodUnit"))) {
		case Constants.YEAR:
			
			if (Integer.valueOf(parameters.get("period")) > Constants.YEAR_PERIOD_LIMIT) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "借款期限超过了" + Constants.YEAR_PERIOD_LIMIT + "年");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
			
			break;
		case Constants.MONTH:
			
			if (Integer.valueOf(parameters.get("period")) > Constants.YEAR_PERIOD_LIMIT * 12) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "借款期限超过了" + Constants.YEAR_PERIOD_LIMIT + "年");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
			
			break;
		case Constants.DAY:
			
			if (Integer.valueOf(parameters.get("period")) > Constants.YEAR_PERIOD_LIMIT * 12 * 30) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "借款期限超过了" + "借款期限超过了" + Constants.YEAR_PERIOD_LIMIT + "年");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
			
			if (Integer.valueOf(parameters.get("investPeriod")) > Integer.valueOf(parameters.get("period"))) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "天标满标期限不能大于借款期限 !");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
			
			break;
		default:
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款期限单位有误!");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("repaymentId")) || Long.valueOf(parameters.get("repaymentId")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "还款方式id有误!");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(!StringUtils.isBlank(parameters.get("minInvestAmount"))){
			bid.minInvestAmount = Double.parseDouble(parameters.get("minInvestAmount"));
			
			if (Double.parseDouble(parameters.get("minInvestAmount")) > 0 && (Double.parseDouble(parameters.get("minInvestAmount")) 
					< bid.product.minInvestAmount)) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "最低投标金额不能小于产品最低投标金额!");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
		}
		
//		if ((Double.parseDouble(parameters.get("minInvestAmount")) > 0 && Double.parseDouble(parameters.get("averageInvestAmount")) 
//				> 0) || (Double.parseDouble(parameters.get("minInvestAmount")) <= 0 && Double.parseDouble(parameters.get("averageInvestAmount")) <= 0)) {
//			jsonMap.put("error", "1");
//			jsonMap.put("msg", "最低投标金额和平均招标金额有误!");
//			
//			return JSONObject.fromObject(jsonMap).toString();
//		}
		
		if(!StringUtils.isBlank(parameters.get("averageInvestAmount"))){
			bid.averageInvestAmount = Double.parseDouble(parameters.get("averageInvestAmount"));
			
			if (Double.parseDouble(parameters.get("averageInvestAmount")) > 0 && Double.parseDouble(parameters.get("amount"))
					% Double.parseDouble(parameters.get("averageInvestAmount")) != 0) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "平均招标金额有误!");
				
				return JSONObject.fromObject(jsonMap).toString();
			}
		}
		
		if(StringUtils.isBlank(parameters.get("investPeriod")) || Integer.valueOf(parameters.get("investPeriod")) <= 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "投标期限有误!");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("apr")) || Double.parseDouble(parameters.get("apr")) <= 0 || 
				Double.parseDouble(parameters.get("apr")) > 100 || Double.parseDouble(parameters.get("apr"))
				< bid.product.minInterestRate || Double.parseDouble(parameters.get("apr")) > bid.product.maxInterestRate){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "年利率有误!");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
//		if(StringUtils.isBlank(parameters.get("imageFilename"))){
//			jsonMap.put("error", "1");
//			jsonMap.put("msg", "图片有误");
//			
//			return JSONObject.fromObject(jsonMap).toString();
//		}
		
		if(StringUtils.isBlank(parameters.get("description"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "内容描述有误!");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(!StringUtils.isBlank(parameters.get("bonusType"))){
			bid.bonusType = Integer.valueOf(parameters.get("bonusType"));
		}
		
		if(!StringUtils.isBlank(parameters.get("awardScale")) && !StringUtils.isBlank(parameters.get("bonusType"))){
			
			bid.awardScale = Double.parseDouble(parameters.get("awardScale"));
		}
		
		if(!StringUtils.isBlank(parameters.get("bonus")) && !StringUtils.isBlank(parameters.get("bonusType"))){
			
			bid.bonus = Double.parseDouble(parameters.get("bonus"));
		}
		
		if(StringUtils.isBlank(parameters.get("productId")) || Long.valueOf(parameters.get("productId")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传参productId有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("userId"))){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "传参用户userId有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
        long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		bid.purpose.id = Long.valueOf(parameters.get("purposeId"));
		bid.title = parameters.get("title");
		bid.amount = Double.parseDouble(parameters.get("amount"));
		bid.periodUnit = Integer.valueOf(parameters.get("periodUnit"));
		bid.period = Integer.valueOf(parameters.get("period"));
		bid.repayment.id = Long.valueOf(parameters.get("repaymentId"));
		bid.investPeriod = Integer.valueOf(parameters.get("investPeriod"));
		bid.apr = Double.parseDouble(parameters.get("apr"));
		bid.imageFilename = parameters.get("imageFilename");
		bid.description = parameters.get("description");
		
		bid.createBid = true; // 优化加载
		bid.userId = userId; // 填充用户对象
		
		t_bids tbid = new t_bids();
		bid.createBid(Constants.CLIENT_APP, tbid, error);
		//bid.createBid(Constants.CLIENT_PC, tbid, error);
		
		if(error.code < 0){
			
			if(error.code == Constants.BALANCE_NOT_ENOUGH) {
				jsonMap.put("error", "-999");
				jsonMap.put("msg", "您余额不足，请充值！");
				return JSONObject.fromObject(jsonMap).toString();
			}
			
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		String bidNo = OptionKeys.getvalue(OptionKeys.LOAN_NUMBER, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询数据有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		//资金托管接口调用
				if(Constants.IPS_ENABLE){					
					//资金托管调用标的发布接口
					PaymentProxy.getInstance().bidCreate(error, Constants.CLIENT_APP, tbid, bid);
					
					jsonMap.put("msg", error.msg);
					jsonMap.put("error", error.msg);
					//applyNow(productId, error.code, status);
				}else{			
					
					//普通网关标的发布的实际业务			
					//标的发布的实际业务逻辑
					bid.afterCreateBid(tbid, null, Constants.CLIENT_PC, 0, error);			
					jsonMap.put("no", OptionKeys.getvalue(OptionKeys.LOAN_NUMBER, error) + tbid.id);
					jsonMap.put("title", bid.title);
					DecimalFormat myformat = new DecimalFormat();
					myformat.applyPattern("##,##0.00");
					jsonMap.put("amount", myformat.format(bid.amount));
					jsonMap.put("status", bid.status);
					//applyNow(productId, error.code, status);
				}
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "发布借款成功");
		jsonMap.put("bidNo", bidNo + bid.id);
		jsonMap.put("requiredAuditItem", bid.product.requiredAuditItem);
		jsonMap.put("selectAuditItem", bid.product.selectAuditItem);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/*
	 * 获取完善用户资料状态接口
	 * id 用户id
	 */
	public static String UserStatus(Map<String, String> parameters) throws IOException{
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        ErrorInfo error = new ErrorInfo();
        
		User user = new User();
		
		if (StringUtils.isBlank(parameters.get("id"))) {
			jsonMap.put("error", "-2");
						jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		user.id = userId;
		
		if (user.id < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该用户不存在");
			return JSONUtils.printObject(jsonMap);
		}
		
		if(!user.isAddBaseInfo){
			jsonMap.put("error", -1);
			jsonMap.put("msg", "未激活");
			jsonMap.put("accountStates", 2);
			
			return JSONUtils.printObject(jsonMap);
			
		}else{
			if((user.isEmailVerified || user.isMobileVerified) && user.isAddBaseInfo){
				jsonMap.put("error", -1);
				jsonMap.put("msg", "已激活已完善资料");
				jsonMap.put("accountStates", 2);
				
				return JSONUtils.printObject(jsonMap);
			}
		}
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/**
	 * 完善用户资料接口
	 * @param id 用户id
	 * registedPlaceCity 城市id
	 * higtestEdu 学历
	 * @throws IOException
	 */
	public static String saveBaseInfo(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		User user = new User();
		
		if (StringUtils.isBlank(parameters.get("id")) ) {
			jsonMap.put("error", "-2");
						jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		user.id = userId;
		
		if (user.id < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该用户不存在");
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("realName"))){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "姓名不能为空");
			return JSONUtils.printObject(jsonMap);
		}
		user.realityName = parameters.get("realName").trim();
		
		if (!CharUtil.isChinese(user.realityName)) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "真实姓名必须是中文");
			return JSONUtils.printObject(jsonMap);
		}
		
		
		if(StringUtils.isBlank(parameters.get("sex")) || Integer.valueOf(parameters.get("sex")) < 0 ){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "性别有误");
			return JSONUtils.printObject(jsonMap);
		}
		user.setSex(Integer.valueOf(parameters.get("sex")));
		
		if(StringUtils.isBlank(parameters.get("age")) || Integer.valueOf(parameters.get("age")) < 0 ){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "年龄有误");
			return JSONUtils.printObject(jsonMap);			
		}
		user.age = Integer.valueOf(parameters.get("age"));
		
		if(StringUtils.isBlank(parameters.get("registedPlaceCity")) || Integer.valueOf(parameters.get("registedPlaceCity")) < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "城市有误");
			return JSONUtils.printObject(jsonMap);
		}
		user.cityId =  Integer.valueOf(parameters.get("registedPlaceCity"));
		Logger.info("idNum"+parameters.get("idNo"));
		if(StringUtils.isBlank(parameters.get("idNo"))){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "身份证号有误");
			return JSONUtils.printObject(jsonMap);
		}
		
	/*	if("".equals(IDCardValidate.chekIdCard(0, parameters.get("idNo")))) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "身份证号有误");
			return JSONUtils.printObject(jsonMap);
		}*/
		user.idNumber = parameters.get("idNo");
		
		if(StringUtils.isBlank(parameters.get("higtestEdu")) || Integer.valueOf(parameters.get("higtestEdu")) < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "学历有误");
			return JSONUtils.printObject(jsonMap);
		}
		user.educationId = Integer.valueOf(parameters.get("higtestEdu"));
		
		if(StringUtils.isBlank(parameters.get("maritalStatus")) || Integer.valueOf(parameters.get("maritalStatus")) < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "请选择婚姻情况");
			return JSONUtils.printObject(jsonMap);
		}
		user.maritalId = Integer.valueOf(parameters.get("maritalStatus"));
		
		if(StringUtils.isBlank(parameters.get("housrseStatus")) || Integer.valueOf(parameters.get("housrseStatus")) < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "请选择购房情况");
			return JSONUtils.printObject(jsonMap);
		}
		user.houseId = Integer.valueOf(parameters.get("housrseStatus"));
		
		if(StringUtils.isBlank(parameters.get("CarStatus")) || Integer.valueOf(parameters.get("CarStatus")) < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "请选择购车情况");
			return JSONUtils.printObject(jsonMap);
		}
		user.carId = Integer.valueOf(parameters.get("CarStatus"));
		
		user.updateBaseInfo(error);
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "保存成功");
		
		return JSONUtils.printObject(jsonMap);
	}

	/*
	 *通过后台发送激活邮件
	 *id 用户id
	 */
	public static String activeEmail(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		User user = new User();
		String mail = parameters.get("mail");
		if(mail==null||mail.length()<1||!mail.contains("@")){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "邮箱地址有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		if (StringUtils.isBlank(parameters.get("id"))) {
			jsonMap.put("error", "-2");
						jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		user.id = userId;
		
		if (user.id < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该用户不存在");
			
			return JSONUtils.printObject(jsonMap);
		}
		user.updateEmail(user.id, mail, error);
		//TemplateEmail.activeEmail(user, error);
		TemplateEmail.activeEmail(user.id,user.name,mail, error);
		
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "邮箱激活成功");
		jsonMap.put("activationLink", EmailUtil.emailUrl(user.email));
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *申请vip
	 */
	public static String vipApply(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		User user = new User();
		
		if(StringUtils.isBlank(parameters.get("openTime")) || Integer.valueOf(parameters.get("openTime")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "申请时间有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if (StringUtils.isBlank(parameters.get("id")) ) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		user.id = userId;
		
		if (user.id < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该用户不存在");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		Vip vip = new Vip();
		vip.serviceTime = Integer.valueOf(parameters.get("openTime"));
		vip.renewal(user, Constants.CLIENT_APP, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "申请vip成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *VIP会员服务条款接口
	 */
	public static String vipAgreement(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String agreement = News.queryVipAgreement();
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "VIP会员服务条款查询成功");
		jsonMap.put("content", agreement);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *二维码
	 */
	public static String TwoDimensionalCode(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		User user = new User();
		user.id = userId;
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "二维码查询成功");
		jsonMap.put("promoteImg", Constants.HTTP_PATH + "/images?uuid="+user.qrcode);
		jsonMap.put("spreadLink", user.spreadLink);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *我推广的会员列表接口
	 *id 用户id
	 */
	public static String spreadUser(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_user_cps_users> page = User.queryCpsSpreadUsers(userId ,"", "", 
				"", "", parameters.get("currPage"), "36", error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询成功");
		jsonMap.put("page", page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/* 我推广的收入接口
	 * id 用户id
	 */
	public static String spreadUserIncome(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<t_user_cps_income> page = User.queryCpsSpreadIncome(userId, 
				"","",parameters.get("currPage"),"36", error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询成功");
		jsonMap.put("page", page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/* 发送站内信
	 */
	public static String sendStation(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("receiverName"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "接收人名称有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("title"))) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "标题不能为空");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("content"))) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "内容不能为空");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		StationLetter message = new StationLetter();
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		message.senderUserId = userId;
		message.receiverUserName = parameters.get("receiverName");
		message.title = parameters.get("title");
		message.content = parameters.get("content");
		
		message.sendToUserByUser(error); 
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "发送成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *系统信息接口
	 *id 用户id
	 */
	public static String systemSms(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Integer.valueOf(parameters.get("currPage")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "参数当前页currPage有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_messages_system> page = 
			StationLetter.queryUserSystemMsgs(userId, Integer.valueOf(parameters.get("currPage")), Constants.APP_PAGESIZE, "", 5, error);
	
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		
		List<v_messages_system> list= page.page;
		for (v_messages_system v : list) {
			String content = v.content;
			v.content = content.replaceAll("<{1}.*>{1}", "");
			
		}
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询成功");
		jsonMap.put("page", page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *删除系统信息接口
	 *id 用户id
	 */
	public static String deleteSystemSmgs(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if (StringUtils.isBlank(parameters.get("ids"))) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请选择要删除的站内信");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}

		String arr[] = parameters.get("ids").split(",");
		
		for (String str : arr) {
			long id = Long.parseLong(str);
			StationLetter.deleteInboxMsgByUser(userId, id, DeleteType.DELETE, error);
			
			if (error.code < 0) {
				break;
			}
		}
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "删除失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "删除成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *收件箱信息
	 *id 用户id
	 */
	public static String inboxMsgs(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Integer.valueOf(parameters.get("currPage")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "分页数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_messages_user_inbox> page = 
			StationLetter.queryUserInboxMsgs(userId , Integer.valueOf(parameters.get("currPage")), Constants.APP_PAGESIZE, "", 5, error);
	
		if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询成功");
		jsonMap.put("page", page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *删除收件箱信息接口
	 */
	public static String deleteInboxMsgs(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if (StringUtils.isBlank(parameters.get("ids"))) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请选择要删除的站内信");
			
			return JSONUtils.printObject(jsonMap);
		}

        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String arr[] = parameters.get("ids").split(",");

		for (String str : arr) {
			long id = Long.parseLong(str);
			StationLetter.deleteInboxMsgByUser(userId, id, DeleteType.DELETE, error);
			if (error.code < 0) {
				break;
			}
		}
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "删除失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "删除成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *删除发件箱信息接口
	 */
	public static String deleteOutboxMsgByUser(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if (StringUtils.isBlank(parameters.get("ids"))) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请选择要删除的站内信");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		String arr[] = parameters.get("ids").split(",");

		for (String str : arr) {
			long id = Long.parseLong(str);
			StationLetter.deleteOutboxMsgByUser(userId, id, DeleteType.DELETE, error);
			if (error.code < 0) {
				break;
			}
		}
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "删除失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "删除成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *标记为已读
	 *id 用户id
	 */
	public static String markMsgsReaded(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if (StringUtils.isBlank(parameters.get("ids"))) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请选择要标记为已读的站内信");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}

		String arr[] = parameters.get("ids").split(",");

		for (String str : arr) {
			long id = Long.parseLong(str);
			StationLetter.markUserMsgReaded(userId, id, error);
		}
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "标记为已读失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "标记为已读成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *标记为未读
	 *id 用户id
	 */
	public static String markMsgsUnread(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if (StringUtils.isBlank(parameters.get("ids"))) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请选择要标记为未读的站内信");
			
			return JSONUtils.printObject(jsonMap);
		}

		String arr[] = parameters.get("ids").split(",");

		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		for (String str : arr) {
			long id = Long.parseLong(str);
			StationLetter.markUserMsgUnread(userId, id, error);
		}
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "标记为未读失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "标记为未读成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *借款账单
	 *id 用户id
	 */
	public static String queryMyLoanBills(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String payTypeStr = parameters.get("payType");
		String isOverTypeStr = parameters.get("isOverType");
		String keyTypeStr = parameters.get("keyType");
		String key = parameters.get("key");
		
		if(!(NumberUtil.isNumericInt(isOverTypeStr))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传人参数是否逾期有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(!(NumberUtil.isNumericInt(payTypeStr))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传人参数还款方式有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(!(NumberUtil.isNumericInt(keyTypeStr))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传人参数关键字有误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Integer.valueOf(parameters.get("currPage")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		int payType = Integer.parseInt(payTypeStr);
		int isOverType = Integer.parseInt(isOverTypeStr);
		int keyType = Integer.parseInt(keyTypeStr);
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_bill_loan> page = Bill.queryMyLoanBills(userId, payType, isOverType, keyType, key, Integer.valueOf(parameters.get("currPage")), Constants.APP_PAGESIZE, error);
	
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询成功");
		jsonMap.put("page", page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *借款账单详情
	 */
	public static String loanBillDetails(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("billId"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "账单id有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long billId = Security.checkSign(parameters.get("billId"), Constants.BILL_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        v_bill_detail billDetail = Bill.queryBillDetails(billId, userId, error);
        
        if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_bill_repayment_record> page = Bill.queryBillReceivables(billDetail.bid_id, 1, Constants.APP_PAGESIZE, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		BackstageSet backSet = BackstageSet.getCurrentBackstageSet();
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询成功");
		jsonMap.put("page", page);
		jsonMap.put("totalNum", page.totalCount);
		jsonMap.put("billDetail", billDetail);
		jsonMap.put("platformName", backSet.platformName);
		jsonMap.put("hotline", backSet.platformTelephone);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *还款
	 **id 用户id
	 *billId 账单id
	 *dealPwd 交易密码
	 *
	 */
	public static String submitRepayment(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("billId"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "账单id有误");
			
			return JSONUtils.printObject(jsonMap);
		}
//		String dealPwd = parameters.get("dealPwd");
//		
//		dealPwd = Encrypt.decrypt3DES(dealPwd, Constants.ENCRYPTION_KEY);
//		
//		if(StringUtils.isBlank(dealPwd)){
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "交易密码有误");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
		
		if(StringUtils.isBlank(parameters.get("payAmount")) || Double.valueOf(parameters.get("payAmount")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "还款金额有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long billId = Security.checkSign(parameters.get("billId"), Constants.BILL_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		User user = new User();
		user.id = userId;
//		int code = user.verifyPayPassword(dealPwd, error);
//		
//		if(code < 0) {
//			jsonMap.put("error", "-4");
//			jsonMap.put("msg",error.msg);
//			
//			return JSONUtils.printObject(jsonMap);
//		}
		
		Bill bill = new Bill();
		bill.setId(billId);
		
		if(bill.ipsStatus == IPSDealStatus.REPAYMENT_HANDING){  //还款处理中
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "还款处理中，请勿重复操作！");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(bill.ipsStatus == IPSDealStatus.OFFLINEREPAYMENT_HANDING){  //线下收款处理中
			
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "线下收款处理中，不能还款！");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(bill.ipsStatus == IPSDealStatus.COMPENSATE_HANDING){  //本金垫付处理中
			
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "本金垫付处理中，不能还款！");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		/* 2014-12-29 限制还款需要从第一期逐步开始还款 */
		if(bill.checkPeriod(bill.bidId, bill.periods) > 0){
			
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请您从第一期逐次还款!");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		/*本金垫付还款*/
		if (Constants.IPS_ENABLE && bill.status == Constants.ADVANCE_PRINCIIPAL_REPAYMENT) {
			
			//垫付还款，本地业务逻辑，在垫付还款回调方法中，这里直接跳出
			PaymentProxy.getInstance().advanceRepayment(error, Constants.PC, bill, user.id);
			
		}
		
		/* 余额不足则提示跳转去充值页面，故此不能把错误值放入flash.error */
		if(error.code == -999) {
			jsonMap.put("error", "-999");
			jsonMap.put("msg", "您余额不足，请充值");
			
			return JSONUtils.printObject(jsonMap);
		} else if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		bill.repaymentCheck(user.id, error);
		
		if(error.code == -999) {
			jsonMap.put("error", "-999");
			jsonMap.put("msg", "您余额不足，请充值");
			
			return JSONUtils.printObject(jsonMap);
		} else if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map=PaymentProxy.getInstance().repayment(error,Constants.CLIENT_APP, bill, user.id);
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "还款成功");
		jsonMap.put("htmlParam", map.get("htmlParam"));
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *审核中的借款标列表
	 **id 用户id
	 *currPage 分页数据
	 */
	public static String auditingLoanBids(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Integer.valueOf(parameters.get("currPage")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "分页数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_bid_auditing> pageBean = new PageBean<v_bid_auditing>();
		pageBean.currPage = parameters.get("currPage") == null ? 1 : Integer.valueOf(parameters.get("currPage"));
		pageBean.pageSize = Constants.APP_PAGESIZE;
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
			
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("userId", String.valueOf(userId));
		
		pageBean.page = Bid.queryBidAuditing(pageBean, error, map);

		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "审核中的借款标查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "审核中的借款标查询成功");
		jsonMap.put("page", pageBean.page);
		jsonMap.put("totalNum", pageBean.page.size());

		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *等待满标的借款标列表
	 **id 用户id
	 *currPage 分页数据
	 */
	public static String loaningBids(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Integer.valueOf(parameters.get("currPage")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "分页数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_bid_fundraiseing> pageBean = new PageBean<v_bid_fundraiseing>();
		pageBean.currPage = parameters.get("currPage") == null ? 1 : Integer.valueOf(parameters.get("currPage"));
		pageBean.pageSize = Constants.APP_PAGESIZE;
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		pageBean.page = Bid.queryBidFundraiseing(pageBean, -1, error,  String.valueOf(userId),"","","","","");
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "招标中的借款标查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "招标中的借款标查询成功");
		jsonMap.put("page", pageBean.page);
		jsonMap.put("totalNum", pageBean.page.size());
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *还款中的借款标列表
	 **id 用户id
	 *currPage 分页数据
	 */
	public static String repaymentBids(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Integer.valueOf(parameters.get("currPage")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "分页数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_bid_repaymenting> pageBean = new PageBean<v_bid_repaymenting>();
		pageBean.currPage = parameters.get("currPage") == null ? 1 : Integer.valueOf(parameters.get("currPage"));
		pageBean.pageSize = Constants.APP_PAGESIZE;
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		pageBean.page = Bid.queryBidRepaymenting(0, pageBean, 0, error,  String.valueOf(userId),"","","","","");
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "还款中的借款标查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "还款中的借款标查询成功");
		jsonMap.put("page", pageBean.page);
		jsonMap.put("totalNum", pageBean.page.size());
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *已成功的借款标列表
	 *id 用户id
	 *currPage 分页数据
	 */
	public static String successBids(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Integer.valueOf(parameters.get("currPage")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "分页数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_bid_repayment> pageBean = new PageBean<v_bid_repayment>();
		pageBean.currPage = parameters.get("currPage") == null ? 1 : Integer.valueOf(parameters.get("currPage"));
		pageBean.pageSize = Constants.APP_PAGESIZE;
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		pageBean.page = Bid.queryBidRepayment(0, pageBean, 0, error,  String.valueOf(userId),"","","","","");
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "已成功的借款标查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "已成功的借款标查询成功");
		jsonMap.put("page", pageBean.page);
		jsonMap.put("totalNum", pageBean.page.size());
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *审核资料认证
	 *id 用户id
	 *currPage 分页数据
	 */
	public static String auditMaterials(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Integer.valueOf(parameters.get("currPage")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "分页数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
		PageBean<v_user_audit_items> pageBean = UserAuditItem.queryUserAuditItem(parameters.get("currPage"), Constants.APP_PAGESIZE2, userId, error,
				parameters.get("status"),null,null,parameters.get("productId"),null);
        
		List<Product> products = Product.queryProductNames(true, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "审核资料认证查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "审核资料认证查询成功");
		jsonMap.put("page", pageBean.page);
		jsonMap.put("totalNum", pageBean.page.size());
		jsonMap.put("products", products);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *审核资料认证详情
	 *id 用户id
	 *mark 唯一标识
	 */
	public static String auditMaterialsSameItem(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("mark"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "唯一标识有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
        UserAuditItem item = new UserAuditItem();
		item.userId = userId;
		item.mark = parameters.get("mark");
        List<v_user_audit_items> items = UserAuditItem.querySameAuditItem(userId, item.auditItemId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "审核资料认证详情查询成功");
		jsonMap.put("items", items);
		jsonMap.put("auditItemName", item.auditItem.name);
	    jsonMap.put("creditScore", item.auditItem.creditScore);
	    jsonMap.put("period", item.auditItem.period);
	    jsonMap.put("auditCycle", item.auditItem.auditCycle);
	    jsonMap.put("suggestion", item.suggestion);
	    jsonMap.put("productNames", item.productNames);
	    jsonMap.put("status", item.status);
	    jsonMap.put("expireTime", item.expireTime);
	    jsonMap.put("time", item.time);
	    jsonMap.put("auditTime", item.auditTime);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *交易记录
	 *id 用户id
	 *purpose 借款用途
	 *startTime 开始查询时间
	 *lastTime   结束查询时间
	 *
	 */
	public static String dealRecord(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String beginTime = null;
		String endTime = null;
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("purpose")) ){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款用途有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
//		if(StringUtils.isNotBlank(parameters.get("startTime")) && DateUtil.){
//			jsonMap.put("error", "1");
//			jsonMap.put("msg", "查询初始时间有误");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
//		
//		if(StringUtils.isBlank(parameters.get("lastTime")) ){
//			jsonMap.put("error", "1");
//			jsonMap.put("msg", "查询终止时间有误");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) ){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "当前页有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);

        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
        if(RegexUtils.isDate(parameters.get("startTime"))) {
 			beginTime = parameters.get("startTime");
 		}
 		
 		if(RegexUtils.isDate(parameters.get("lastTime"))) {
 			endTime = parameters.get("lastTime");
 		}
        
        PageBean<v_user_details> page = User.queryUserDetails(userId, Long.valueOf(parameters.get("purpose"))
        		,beginTime, endTime,Integer.valueOf(parameters.get("currPage")), Constants.APP_PAGESIZE);
		
        if(page == null ){
        	jsonMap.put("error", "-4");
    		jsonMap.put("msg", "交易记录查询失败");
    		
    		return JSONUtils.printObject(jsonMap);
        }
        
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "交易记录查询成功");
		jsonMap.put("page", page);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *银行卡管理
	 *id 用户id
	 */
	public static String bankInfos(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
//		String ss = parameters.get("id");
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
        
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
        List<UserBankAccounts> userBanks = UserBankAccounts.queryUserAllBankAccount(userId);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "银行卡查询成功");
		jsonMap.put("userBanks", userBanks);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *添加银行卡
	 *id 用户id
	 *bankName 银行名称
	 *bankCardNum 银行卡号
	 *cardUserName 银行卡持有人
	 */
	public static String addBank(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("bankName"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "银行名称有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("bankCardNum"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "银行卡账号有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("cardUserName"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "收款人有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
        UserBankAccounts bankUser =  new UserBankAccounts();
		
		bankUser.userId = userId;
		bankUser.bankName = parameters.get("bankName");
		bankUser.account = parameters.get("bankCardNum");
		bankUser.accountName = parameters.get("cardUserName");
		
		bankUser.addUserBankAccount(error, false);  //目前不支持提现银行卡支行信息
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "银行卡添加成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *编辑银行卡
	 *bankId 银行卡ID
	 *bankName 银行名称
	 *bankCardNum 银行卡号
	 *cardUserName 银行卡持有人
	 */
	public static String editBank(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("bankId")) || Long.valueOf(parameters.get("bankId")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "银行卡ID有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("bankName"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "银行名称有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("bankCardNum"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "银行卡账号有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("cardUserName"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "收款人有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
        UserBankAccounts userAccount = new UserBankAccounts();
		
		userAccount.bankName = parameters.get("bankName");
		userAccount.account = parameters.get("bankCardNum");
		userAccount.accountName = parameters.get("cardUserName");

		userAccount.editUserBankAccount(Long.valueOf(parameters.get("bankId")), userId, false, error);  
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "银行卡编辑成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *校验安全问题
	 *id 用户id
	 *answer1 问题1
	 *answer2 问题2
	 *answer3 问题3
	 */
	public static String verifySafeQuestion(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String answer1 = parameters.get("answer1");
		answer1 = Encrypt.decrypt3DES(answer1, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(answer1)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "问题1有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String answer2 = parameters.get("answer2");
		answer2 = Encrypt.decrypt3DES(answer2, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(answer2)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "问题2有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String answer3 = parameters.get("answer3");
		answer3 = Encrypt.decrypt3DES(answer3, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(answer3)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "问题3有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
		User  user = new User();
		user.id = userId;
		user.verifySafeQuestion(answer1, answer2, answer3, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "安全问题回答正确");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *查询安全问题
	 *id 用户id
	 */
	public static String queryAnswers(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
		User  user = new User();
		user.id = userId;
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询安全问题成功");
		jsonMap.put("question1",user.questionName1);
		jsonMap.put("question2",user.questionName2);
		jsonMap.put("question3",user.questionName3);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *保存交易密码
	 *id 用户id
	 *newdealpwd 新交易密码
	 */
	public static String savePayPassword(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String newdealpwd = parameters.get("newdealpwd");
		newdealpwd = Encrypt.decrypt3DES(newdealpwd, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(newdealpwd)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "新密码有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}		
		
		String mobile = parameters.get("cellPhone");
		String randomCode = parameters.get("randomCode");
		
		if(StringUtils.isBlank(mobile)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg","请输入手机号码");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(randomCode)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg","请输入验证码");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(!RegexUtils.isMobileNum(mobile)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg","请输入正确的手机号码");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long id = User.queryIdByMobile(mobile, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该手机号码不存在");
			
			return JSONUtils.printObject(jsonMap);
		}
        
        if(id != userId) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请输入正确的绑定手机");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(Constants.CHECK_MSG_CODE) {
			String cCode = (Cache.get(mobile)).toString();
			
			if(cCode == null) {
				jsonMap.put("error", "-4");
				jsonMap.put("msg","验证码已失效，请重新点击发送验证码");
				
				return JSONUtils.printObject(jsonMap);
			}
			
			if(!randomCode.equals(cCode)) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg","手机验证错误");
				
				return JSONUtils.printObject(jsonMap);
			}
		}
		
		User  user = new User();
		user.id = userId;
		
		user.addPayPassword(true, newdealpwd, newdealpwd, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "交易密码保存成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *修改交易密码
	 *id 用户id
	 *currentdealpwd 原交易密码
	 *newdealpwd 新交易密码
	 */
	public static String editPayPassword(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String newdealpwd = parameters.get("newdealpwd");
		newdealpwd = Encrypt.decrypt3DES(newdealpwd, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(newdealpwd)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "新交易密码有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String currentdealpwd = parameters.get("currentdealpwd");
		currentdealpwd = Encrypt.decrypt3DES(currentdealpwd, Constants.ENCRYPTION_KEY);
		if(StringUtils.isBlank(currentdealpwd)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "原交易密码有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
//		String mobile = parameters.get("cellPhone");
//		String randomCode = parameters.get("randomCode");
//		
//		if(StringUtils.isBlank(mobile)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg","请输入手机号码");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
//		
//		if(StringUtils.isBlank(randomCode)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg","请输入验证码");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
		
//		if(!RegexUtils.isMobileNum(mobile)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg","请输入正确的手机号码");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
//		
//        User.queryIdByMobile(mobile, error);
//		
//		if(error.code < 0) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "该手机号码不存在");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
//		
//		if(Constants.CHECK_MSG_CODE) {
//			String cCode = (Cache.get(mobile)).toString();
//			
//			if(cCode == null) {
//				jsonMap.put("error", "-4");
//				jsonMap.put("msg","验证码已失效，请重新点击发送验证码");
//				
//				return JSONUtils.printObject(jsonMap);
//			}
//			
//			if(!randomCode.equals(cCode)) {
//				jsonMap.put("error", "-3");
//				jsonMap.put("msg","手机验证错误");
//				
//				return JSONUtils.printObject(jsonMap);
//			}
//		}
		
		User  user = new User();
		user.id = userId;
		
		user.editPayPassword(currentdealpwd,newdealpwd,newdealpwd,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "交易密码修改成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *保存登录密码
	 *id 用户id
	 *oldloginpwd 原登录密码
	 *newloginpwd 新登录密码
	 */
	public static String savePassword(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String oldloginpwd = parameters.get("oldloginpwd");
		
		oldloginpwd = Encrypt.decrypt3DES(oldloginpwd, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(oldloginpwd)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "原登录密码有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String newloginpwd = parameters.get("newloginpwd");
		newloginpwd = Encrypt.decrypt3DES(newloginpwd, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(newloginpwd)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "新登录密码有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
	
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
		User  user = new User();
		user.id = userId;
		
//		String mobile = parameters.get("cellPhone");
//		String randomCode = parameters.get("randomCode");
		
//		if(StringUtils.isBlank(mobile)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg","请输入手机号码");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
//		
//		if(StringUtils.isBlank(randomCode)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg","请输入验证码");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
//		
//		if(!RegexUtils.isMobileNum(mobile)) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg","请输入正确的手机号码");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
//		
//        long id = User.queryIdByMobile(mobile, error);
//		
//		if(error.code < 0) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "该手机号码不存在");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
//		
//		if(id != userId) {
//			jsonMap.put("error", "-3");
//			jsonMap.put("msg", "请输入正确的绑定手机");
//			
//			return JSONUtils.printObject(jsonMap);
//		}
		
//		if(Constants.CHECK_MSG_CODE) {
//			String cCode = (Cache.get(mobile)).toString();
//			
//			if(cCode == null) {
//				jsonMap.put("error", "-3");
//				jsonMap.put("msg","验证码已失效，请重新点击发送验证码");
//				
//				return JSONUtils.printObject(jsonMap);
//			}
//			
//			if(!randomCode.equals(cCode)) {
//				jsonMap.put("error", "-3");
//				jsonMap.put("msg","手机验证错误");
//				
//				return JSONUtils.printObject(jsonMap);
//			}
//		}
		
        user.editPassword(oldloginpwd, newloginpwd,newloginpwd,error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "登录密码修改成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *安全问题设置的状态
	 *id 用户id
	 */
	public static String questionStatus(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
		User user = new User();
		user.id = userId;
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "安全问题设置状态查询成功");
		jsonMap.put("questionStatus", user.isSecretSet);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *获取安全问题内容
	 *id 用户id
	 */
	public static String questionContent(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		List<SecretQuestion> questions = SecretQuestion.queryUserQuestion();
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "获取安全问题内容成功");
		jsonMap.put("questionArr", questions);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *保存安全问题
	 *id 用户id
	 *question1 安全问题1
	 *question2 安全问题2
	 *question3 安全问题3
	 *answer1 安全问题答案1
	 *answer2 安全问题答案2
	 *answer3 安全问题答案3
	 */
	public static String saveSafeQuestion(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("question1")) || Long.valueOf(parameters.get("question1")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "安全问题1有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("question2")) || Long.valueOf(parameters.get("question2")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "安全问题2有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("question3")) || Long.valueOf(parameters.get("question3")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "安全问题3有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String answer1 = parameters.get("answer1");
		answer1 = Encrypt.decrypt3DES(answer1, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(answer1)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "安全问题答案1有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String answer2 = parameters.get("answer2");
		answer2 = Encrypt.decrypt3DES(answer2, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(answer2)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "安全问题答案2有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String answer3 = parameters.get("answer3");
		answer3 = Encrypt.decrypt3DES(answer3, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(answer3)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "安全问题答案3有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
		User  user = new User();
		user.id = userId;
		
		user.secretQuestionId1 = Long.valueOf(parameters.get("question1"));
		user.secretQuestionId2 = Long.valueOf(parameters.get("question2"));
		user.secretQuestionId3 = Long.valueOf(parameters.get("question3"));
		user.answer1 = answer1;
		user.answer2 = answer2;
		user.answer3 = answer3;
		
		user.updateSecretQuestion(true, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "安全问题设置成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *邮箱激活状态
	 *id 用户id
	 */
	public static String emailStatus(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
		User user = new User();
		user.id = userId;
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "邮箱激活状态查询成功");
		jsonMap.put("status", user.isEmailVerified);
		jsonMap.put("emailaddress", user.email==null?"":user.email);
		String s = JSONUtils.printObject(jsonMap);
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *修改邮箱
	 *id 用户id
	 *emailaddress 邮箱地址
	 */
	public static String saveEmail(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String emailaddress = parameters.get("emailaddress");
		//emailaddress = Encrypt.decrypt3DES(emailaddress, Constants.ENCRYPTION_KEY);
		
		if(StringUtils.isBlank(emailaddress)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "邮箱地址有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
		User  user = new User();
		user.id = userId;
		user.email = emailaddress;
		
		if(user.editEmail(error) < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		TemplateEmail.activeEmail(user, error);
		//发送失败不提示
//		if(error.code < 0){
//			jsonMap.put("error", "-4");
//			jsonMap.put("msg", error.msg);
//			
//			return JSONUtils.printObject(jsonMap);
//		}
		
		String emailUrl = EmailUtil.emailUrl(emailaddress);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "修改邮箱成功");
		jsonMap.put("emailUrl", emailUrl);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *安全手机详情及状态
	 *id 用户id
	 */
	public static String phoneStatus(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
		User user = new User();
		user.id = userId;
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "安全问题设置状态查询成功");
		jsonMap.put("status", user.isMobileVerified);
		jsonMap.put("phoneNum", user.mobile);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *查看信用等级规则
	 */
	public static String viewCreditRule(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		List<v_credit_levels> CreditLevels = CreditLevel.queryCreditLevelList(error);
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "规则查询成功");
		jsonMap.put("list", CreditLevels);
		jsonMap.put("totalNum", CreditLevels.size());
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *我的信用等级
	 *id 用户id
	 */
	public static String myCredit(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
        
		User user = new User();
		user.id = userId;
		
		//是否有未审核的超额借款
//		boolean isOverBorrow = OverBorrow.haveAuditingOverBorrow(user.id, error);
//		
//		if (error.code < 0) {
//			jsonMap.put("error", "-2");
//			jsonMap.put("msg", error.msg);
//			
//			return JSONUtils.printObject(jsonMap);
//		}
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "我的信用等级查询成功");
		jsonMap.put("creditRating", user.myCredit.imageFilename);
		jsonMap.put("creditScore", user.userScore.credit_score);
		jsonMap.put("creditLimit", user.balanceDetail.credit_line);
		jsonMap.put("lastCreditLine", user.lastCreditLine);
		jsonMap.put("overCreditLine", user.balanceDetail.credit_line - user.lastCreditLine);
		//jsonMap.put("isOverBorrow", isOverBorrow);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *查看信用积分规则
	 */
	public static String creditintegral(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
		
        long auditItemCount = AuditItem.auditItemCount();
		
		String value = OptionKeys.getvalue(OptionKeys.CREDIT_LIMIT, error); 
		double amountKey = StringUtils.isBlank(value) ? 0 : Double.parseDouble(value); // 积分对应额度
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "查看信用积分规则成功");
		jsonMap.put("auditItemCount", auditItemCount);
		jsonMap.put("normalPayPoints", backstageSet.normalPayPoints);
		jsonMap.put("fullBidPoints", backstageSet.fullBidPoints);
		jsonMap.put("investpoints", backstageSet.investpoints);
		jsonMap.put("overDuePoints", backstageSet.overDuePoints);
		jsonMap.put("creditLimit", amountKey);//积分对应的信用额度
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *审核科目积分明细
	 */
	public static String creditItem(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Double.parseDouble(parameters.get("currPage")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "当前页数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        PageBean<t_dict_audit_items> page = AuditItem.queryEnableAuditItems("", Integer.parseInt(parameters.get("currPage")), Constants.APP_PAGESIZE, error); // 审核资料
		
		String value = OptionKeys.getvalue(OptionKeys.CREDIT_LIMIT, error); 
		double amountKey = StringUtils.isBlank(value) ? 0 : Double.parseDouble(value); // 积分对应额度
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "查看信用积分规则成功");
		jsonMap.put("list", page);
		jsonMap.put("totalNum", page.totalCount);
		jsonMap.put("creditLimit", amountKey);//积分对应的信用额度
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *审核资料积分明细
	 */
	public static String auditItemScore(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_user_detail_credit_score_audit_items> page = User.queryCreditDetailAuditItem(userId, Integer.valueOf(parameters.get("currPage")), Constants.APP_PAGESIZE, "", error);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查看审核资料积分明细成功");
		jsonMap.put("list", page);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *正常还款积分明细
	 */
	public static String creditDetailRepayment(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_user_detail_credit_score_normal_repayment> page = User.queryCreditDetailRepayment(userId, Integer.valueOf(parameters.get("currPage")), Constants.APP_PAGESIZE, "");
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "查看正常还款积分明细成功");
		jsonMap.put("list", page);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *成功借款积分明细
	 */
	public static String creditDetailLoan(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_user_detail_credit_score_loan> page = User.queryCreditDetailLoan(userId, Integer.valueOf(parameters.get("currPage")), Constants.APP_PAGESIZE, "");
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "查看成功借款积分明细成功");
		jsonMap.put("list", page);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *成功投标积分明细
	 */
	public static String creditDetailInvest(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_user_detail_credit_score_invest> page = User.queryCreditDetailInvest(userId, Integer.valueOf(parameters.get("currPage")), Constants.APP_PAGESIZE, "");
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "查看成功投标积分明细成功");
		jsonMap.put("list", page);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *逾期扣分积分明细
	 */
	public static String creditDetailOverdue(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_user_detail_credit_score_overdue> page = User.queryCreditDetailOverdue(userId, Integer.valueOf(parameters.get("currPage")), Constants.APP_PAGESIZE, "");
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "查看逾期扣分积分明细成功");
		jsonMap.put("list", page);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *申请超额借款
	 *excessAmount 申请金额
	 *applyReason 申请原因
	 *jsonAuditItems 审核资料
	 */
	public static String applyForOverBorrow(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("excessAmount")) || Double.parseDouble(parameters.get("excessAmount")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "申请金额有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("applyReason"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "申请原因有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        User user = new User();
        user.id = userId;
        
		JSONArray jsonArray = JSONArray.fromObject(parameters.get("jsonAuditItems"));
		
		List<Map<String,String>> auditItems = (List)jsonArray;
		
//System.out.println(auditItems.get(0).get("id"));
		
		if(Long.valueOf(parameters.get("excessAmount")) >= Integer.MAX_VALUE) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "申请超额借款已超过最大申请金额");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		new OverBorrow().applyFor(user, Integer.valueOf(parameters.get("excessAmount")), parameters.get("applyReason"), auditItems, error);
		
		if(error.code == -999){
			jsonMap.put("error", "-999");
			jsonMap.put("msg", "您余额不足，请充值");
			
			return JSONUtils.printObject(jsonMap);
		} else if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "您的超额借款申请已提交，请耐心等待审核结果。");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *申请超额借款记录列表
	 *id 用户id
	 */
	public static String overBorrowLists(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		List<t_user_over_borrows> overBorrows = OverBorrow.queryUserOverBorrows(userId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "申请超额借款记录列表查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "申请超额借款记录列表查询成功");
		jsonMap.put("list", overBorrows);
		jsonMap.put("totalNum", overBorrows.size());
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *选择超额借款审核资料库
	 *id 用户id
	 */
	public static String selectAuditItemsInit(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		List<AuditItem> auditItems = UserAuditItem.queryAuditItemsOfOverBorrow(userId, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "超额借款审核资料查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String value = OptionKeys.getvalue(OptionKeys.CREDIT_LIMIT, error); 
		double amountKey = StringUtils.isBlank(value) ? 0 : Double.parseDouble(value); // 积分对应额度
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "超额借款审核资料查询成功");
		jsonMap.put("list", auditItems);
		jsonMap.put("totalNum", auditItems.size());
		jsonMap.put("creditLimit", amountKey);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *信用计算器规则
	 */
	public static String wealthToolkitCreditCalculator(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
        List<AuditItem> auditItems = AuditItem.queryAuditItems(error);
        
        if(error.code < 0){
        	jsonMap.put("error", "-4");
			jsonMap.put("msg", "信用计算器规则查询失败");
			
			return JSONUtils.printObject(jsonMap);
        }
		
		 String value = OptionKeys.getvalue(OptionKeys.CREDIT_LIMIT, error); // 得到积分对应的借款额度值
		
		 if(error.code < 0){
        	jsonMap.put("error", "-4");
			jsonMap.put("msg", "信用计算器规则查询失败");
			
			return JSONUtils.printObject(jsonMap);
	        }
		
		double amountKey = StringUtils.isBlank(value) ? 0 : Double.parseDouble(value);
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "信用计算器规则查询成功");
		jsonMap.put("list", auditItems);
		jsonMap.put("amountKey", amountKey);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *利率计算器
	 *amount 借款金额
	 *deadline 借款期限
	 *apr 年利率
	 *repayType 还款方式
	 *awardScale 奖金比例
	 *bonus 固定奖金
	 */
	public static String aprCalculator(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
//		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("apr")) || Double.parseDouble(parameters.get("apr")) < 0 || Double.parseDouble(parameters.get("apr")) > 100){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "年利率有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("amount")) || Double.parseDouble(parameters.get("amount")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款金额有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("loadType")) || Integer.valueOf(parameters.get("loadType")) < 0 || Integer.valueOf(parameters.get("loadType")) > 2){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款类型有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("deadline")) || Integer.valueOf(parameters.get("deadline")) < 0 || Integer.valueOf(parameters.get("deadline")) > 1000){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "借款期限有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("repayType")) || Integer.valueOf(parameters.get("repayType")) < 0 ) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "还款方式有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		double monthRate = Double.parseDouble(parameters.get("apr")) / 1200;
		double amount = Double.parseDouble(parameters.get("amount"));
		int deadline = Integer.valueOf(parameters.get("deadline"));
		int repayType = Integer.valueOf(parameters.get("repayType"));
		int loadType = Integer.valueOf(parameters.get("loadType"));
		
		double monPay = 0;
		double dayRate = 0;//日利率
		double award = 0;//奖金
		double interest = 0;
//		double earning = 0;
		double sum = 0;
//		DecimalFormat df = new DecimalFormat("#.00");
		
		if(loadType == 1){
			//dayRate = Arith.div(Double.parseDouble(parameters.get("apr")), 36000,4);
			dayRate = Double.parseDouble(parameters.get("apr"))/36000;
			
			if(repayType == 1){//等额本息还款
				monPay = Double.valueOf(Arith.mul(amount, monthRate) * Math.pow((1 + monthRate), 1))/ 
						Double.valueOf(Math.pow((1 + monthRate), 1) - 1);//每个月要还的本金和利息
						monPay = Arith.round(monPay, 2);
		        interest = Arith.sub(Arith.mul(monPay, 1), amount); 	
		        
	//	        earning = Arith.excelRate((amount - award),
	//					Double.parseDouble(df.format(monPay)), deadline, 200, 15)*12*100;
						
			}else if(repayType == 2){//先息后本
				interest = Arith.round(Arith.mul(amount, dayRate * deadline), 2);
				monPay = interest;
	//			earning = Arith.rateTotal(interest + amount,
	//					(amount - award), deadline)*100;
			}else{
				interest = Arith.round(Arith.mul(amount, dayRate * deadline), 2);
				monPay = interest + amount;
	//			earning = Arith.rateTotal(interest + amount,
	//					(amount - award), deadline)*100;
			}
			sum = interest + amount;
		} else {
			if(repayType == 1){//等额本息还款
				monPay = Double.valueOf(Arith.mul(amount, monthRate) * Math.pow((1 + monthRate), deadline))/ 
						Double.valueOf(Math.pow((1 + monthRate), deadline) - 1);//每个月要还的本金和利息
						monPay = Arith.round(monPay, 2);
		        interest = Arith.sub(Arith.mul(monPay, deadline), amount); 	
	//	        earning = Arith.excelRate((amount - award),
	//					Double.parseDouble(df.format(monPay)), deadline, 200, 15)*12*100;
			}else if(repayType == 2){//先息后本
				interest = Arith.round(Arith.mul(amount, monthRate * deadline), 2);
				monPay = Arith.round(Arith.mul(amount, monthRate), 2);
	//			earning = Arith.rateTotal(interest + amount,
	//					(amount - award), deadline)*100;
				
			}else{
				interest = Arith.round(Arith.mul(amount, monthRate * deadline), 2);
				monPay = interest + amount;
	//			earning = Arith.rateTotal(interest + amount,
	//					(amount - award), deadline)*100;
			}
			
			sum = interest + amount;
		}
		
//		earning = Double.parseDouble(df.format(earning)+"");
		
		if(!StringUtils.isBlank(parameters.get("awardScale"))){
			award = Double.parseDouble(parameters.get("awardScale")) * amount;
		}
		
		if(!StringUtils.isBlank(parameters.get("bonus"))){
			award = Double.parseDouble(parameters.get("bonus"));
		}
		
		double serviceFee  = interest * BackstageSet.getCurrentBackstageSet().investmentFee / 100; // 服务费
		
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "利率计算器查询成功");
		jsonMap.put("amount", amount);
		jsonMap.put("monPay", monPay);
		jsonMap.put("serviceFee", serviceFee);
		jsonMap.put("award", award);
		jsonMap.put("interest", interest);
		jsonMap.put("earning", monthRate*1200);
		jsonMap.put("sum", sum - Arith.round(serviceFee,2)+award);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *通过邮箱重置安全问题
	 *id 用户id
	 */
	public static String resetSafeQuestion(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("user_id"))){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			jsonMap.put("error", "-2"); 
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		User user = new User();
		user.id = userId;
		
		TemplateEmail tEmail = new TemplateEmail();
		tEmail.id = 4;

		BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
		String sign = Security.addSign(user.id, Constants.SECRET_QUESTION);
		String url = Constants.RESET_QUESTION_EMAIL + sign;

		String content = tEmail.content;

		content = content.replace("<p","<div");
		content = content.replace("</p>","</div>");
		content = content.replace(Constants.EMAIL_NAME, user.name);
		content = content.replace(Constants.EMAIL_LOGIN, "<a href = "+Constants.LOGIN+">登录</a>");
		content = content.replace(Constants.EMAIL_TELEPHONE, backstageSet.companyTelephone);
		content = content.replace(Constants.EMAIL_PLATFORM, backstageSet.platformName);
		content = content.replace(Constants.EMAIL_URL, "<a href = "+url+">"+url+"</a>");
		content = content.replace(Constants.EMAIL_TIME, DateUtil.dateToString(new Date()));

		TemplateEmail.sendEmail(0, user.email, tEmail.title, content, error);

		if (error.code < 0) {
			jsonMap.put("error", "-4"); 
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "通过邮箱重置安全问题成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *删除银行卡
	 *accountId 银行卡id
	 */
	public static String deleteBank(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("user_id"))){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("accountId"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "银行卡id有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		UserBankAccounts.deleteUserBankAccount(userId, Long.parseLong(parameters.get("accountId")), error);
		
		if (error.code < 0) {
			jsonMap.put("error", "-4"); 
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1"); 
		jsonMap.put("msg", "删除银行卡成功");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *发件箱信息
	 *id 用户id
	 */
	public static String outboxMsgs(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("user_id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Integer.valueOf(parameters.get("currPage")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "分页数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_messages_user_outbox> page = 
			StationLetter.queryUserOutboxMsgs(userId, Integer.valueOf(parameters.get("currPage")), Constants.APP_PAGESIZE, "", error);
	
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "发件箱信息查询成功");
		jsonMap.put("page", page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *发件箱详情信息
	 *id 用户id
	 *index 当前邮件索引
	 *status 操作状态(上一条，下一条)
	 */
	public static String outboxMsgDetail(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("user_id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("index")) || Integer.valueOf(parameters.get("index")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "数据索引index有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("status"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "状态数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		int mark = Integer.parseInt(parameters.get("status"));
		int index = Integer.parseInt(parameters.get("index"));
		
		if(mark == 1){
			index += 1;
			
		}
		
		if(mark == 2){
			index -= 1;
			
		}
		
        long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_messages_user_outbox> page = StationLetter.queryUserOutboxMsgDetail(userId, index, "", error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(page.totalCount == 1){
			jsonMap.put("page", page.page.get(0));
			jsonMap.put("up", false);
			jsonMap.put("down", false);
		}else if(page.currPage == 1){
			jsonMap.put("page", page.page.get(0));
			jsonMap.put("up", false);
			jsonMap.put("down", true);
		}else if(page.currPage == page.totalCount){
			jsonMap.put("page", page.page.get(1));
			jsonMap.put("up", true);
			jsonMap.put("down", false);
		}else{
			jsonMap.put("page", page.page.get(1));
			jsonMap.put("up", true);
			jsonMap.put("down", true);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "发件箱详情查询成功");
		jsonMap.put("totalNum", page.totalCount);
		jsonMap.put("index", index);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *系统邮件详情信息
	 *id 用户id
	 *currPage 当前邮件索引
	 *status 操作状态(上一条，下一条)
	 */
	public static String systemMsgDetail(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("user_id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("index")) || Integer.valueOf(parameters.get("index")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "数据索引index有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("status"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "状态数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		int mark = Integer.parseInt(parameters.get("status"));
		int index = Integer.parseInt(parameters.get("index"));
		
		if(mark == 1){
			index += 1;
			
		}
		
		if(mark == 2){
			index -= 1;
			
		}
		
        long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_messages_system> page = 
			StationLetter.queryUserSystemMsgDetail(userId, index, "",0, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(page.totalCount == 1){
			jsonMap.put("page", page.page.get(0));
			jsonMap.put("up", false);
			jsonMap.put("down", false);
		}else if(page.currPage == 1){
			jsonMap.put("page", page.page.get(0));
			jsonMap.put("up", false);
			jsonMap.put("down", true);
		}else if(page.currPage == page.totalCount){
			jsonMap.put("page", page.page.get(1));
			jsonMap.put("up", true);
			jsonMap.put("down", false);
		}else{
			jsonMap.put("page", page.page.get(1));
			jsonMap.put("up", true);
			jsonMap.put("down", true);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "系统邮件详情信息查询成功");
		jsonMap.put("totalNum", page.totalCount);
		jsonMap.put("index", index);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *收件箱消息详情
	 *id 用户id
	 *index 当前邮件索引
	 *status 操作状态(上一条，下一条)
	 */
	public static String inboxMsgDetail(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("user_id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("index")) || Integer.valueOf(parameters.get("index")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "索引数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("status"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "状态数据有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		int mark = Integer.parseInt(parameters.get("status"));
		int index = Integer.parseInt(parameters.get("index"));
		
		if(mark == 1){
			index += 1;
			
		}
		
		if(mark == 2){
			index -= 1;
			
		}
		
        long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_messages_user_inbox> page = 
			StationLetter.queryUserInboxMsgDetail(userId, index, "",0, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(page.totalCount == 1){
			jsonMap.put("page", page.page.get(0));
			jsonMap.put("up", false);
			jsonMap.put("down", false);
		}else if(page.currPage == 1){
			jsonMap.put("page", page.page.get(0));
			jsonMap.put("up", false);
			jsonMap.put("down", true);
		}else if(page.currPage == page.totalCount){
			jsonMap.put("page", page.page.get(1));
			jsonMap.put("up", true);
			jsonMap.put("down", false);
		}else{
			jsonMap.put("page", page.page.get(1));
			jsonMap.put("up", true);
			jsonMap.put("down", true);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "收件箱详情信息查询成功");
		jsonMap.put("totalNum", page.totalCount);
		jsonMap.put("index", index);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *用户邮箱，手机，安全问题，交易密码状态
	 *id 用户id
	 */
	public static String userInfoStatus(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("user_id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
        long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		User user = new User();
		user.id = userId;
		
		if(StringUtils.isBlank(user.payPassword)){
			jsonMap.put("payPasswordStatus", false);
		}else{
			jsonMap.put("payPasswordStatus", true);
		}
		
		jsonMap.put("email", user.email);
		jsonMap.put("emailStatus", user.isEmailVerified);
		
		jsonMap.put("mobile", user.mobile);
		jsonMap.put("emailStatus", user.isMobileVerified);


		jsonMap.put("error", "-1");
		jsonMap.put("msg", "用户状态查询成功");
		jsonMap.put("teleStatus", user.isMobileVerified);
		jsonMap.put("SecretStatus", user.isSecretSet);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *净值计算器
	 */
	public static String kitNetValueCalculator(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		double bailScale = Product.queryNetValueBailScale(error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "获取净值产品的保证金比例有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("balance")) || Double.parseDouble(parameters.get("balance")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "可用金额有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("receive")) || Double.parseDouble(parameters.get("receive")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "待收金额有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("pay")) || Double.parseDouble(parameters.get("pay")) < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "待付金额有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		double balance = Double.parseDouble(parameters.get("balance"));
		double receive = Double.parseDouble(parameters.get("receive"));
		double pay = Double.parseDouble(parameters.get("pay"));
		
		double amount = Arith.round(((balance + receive - pay) * 0.7)/(1 + (bailScale/100)), 2);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "借款金额查询成功");
		jsonMap.put("amount", amount < 0 ? 0 :amount);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *针对当前用户的所有借款提问
	 *id 用户id
	 *currPage 当前页
	 */
	public static String bidQuestions(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("user_id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("currPage")) || Integer.parseInt(parameters.get("currPage")) <= 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "当前页数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		PageBean<BidQuestions> page = BidQuestions.queryQuestion(Integer.parseInt(parameters.get("currPage")),
				Constants.APP_PAGESIZE, 0, "", 0, userId, error);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "借款提问查询成功");
		jsonMap.put("list", page);
		jsonMap.put("totalNum", page.totalCount);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *提问详情
	 *Id 提问id
	 */
	public static String bidQuestionDetail(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		BidQuestions bidQuestion = BidQuestions.queryBidQuestionDetail(Long.parseLong(parameters.get("id")), error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(null == bidQuestion){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		Bid bid = new Bid();
		bid.bidDetail = true;
		bid.id = bidQuestion.bidId;
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "提问详情查询成功");
		jsonMap.put("bidQuestion", bidQuestion);
		jsonMap.put("bidNo", bid.no);
		jsonMap.put("bidAmont", bid.amount);
		jsonMap.put("bidApr", bid.apr);
		jsonMap.put("bidPeriod", bid.period);
		jsonMap.put("bidRepayName", bid.repayment.name);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *提问详情
	 *Id 提问id
	 */
	public static String creditItemd(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("Id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		BidQuestions bidQuestion = BidQuestions.queryBidQuestionDetail(Long.parseLong(parameters.get("Id")), error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(null == bidQuestion){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "查询失败");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		Bid bid = new Bid();
		bid.bidDetail = true;
		bid.id = bidQuestion.bidId;
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "提问详情查询成功");
		jsonMap.put("bidQuestion", bidQuestion);
		jsonMap.put("bidNo", bid.no);
		jsonMap.put("bidAmont", bid.amount);
		jsonMap.put("bidApr", bid.apr);
		jsonMap.put("bidPeriod", bid.period);
		jsonMap.put("bidRepayName", bid.repayment.name);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *查看超额申请详情
	 */
	public static String viewOverBorrow(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("overBorrowId")) ){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "id有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		List<v_user_audit_items> auditItems = OverBorrow.queryAuditItems(Long.parseLong(parameters.get("overBorrowId")), error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		t_user_over_borrows overBorrows = OverBorrow.queryOverBorrowById(Long.parseLong(parameters.get("overBorrowId")), error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "超额申请详情查询成功");
		jsonMap.put("auditItems", auditItems);
		jsonMap.put("overBorrows", overBorrows);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *申请提现
	 */
	public static String submitWithdrawal(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("userId"))){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("amount"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "申请金额有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		double money = Double.parseDouble(parameters.get("amount"));
		if(money < 1){
		jsonMap.put("error", "-3");
		jsonMap.put("msg", "最低提现金额1元");
		return JSONObject.fromObject(jsonMap).toString();
	}
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		/*if(StringUtils.isBlank(parameters.get("bankId"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "银行卡id有误");
			
			return JSONUtils.printObject(jsonMap);
		}*/
		if (!User.isIPSacctNoExist(userId, error)) {
			jsonMap.put("error", "-31");
			User user = new User();
			user.id = userId;
			Map<String, Object> map=new HashMap<String, Object>();
			map = PaymentProxy.getInstance().register(error,Constants.CLIENT_APP, user);
			jsonMap.put("htmlParam", map.get("htmlParam"));
			jsonMap.put("msg", "用户未开通资金托管账号");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		/*String payPassword = parameters.get("payPassword");
		payPassword = Encrypt.decrypt3DES(payPassword, Constants.APP_ENCRYPTION_KEY);
		*/
		/*if(StringUtils.isBlank(payPassword)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "交易密码有误");
			
			return JSONUtils.printObject(jsonMap);
		}*/
		
		if(StringUtils.isBlank(parameters.get("type"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "参数type有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(Integer.parseInt(parameters.get("type")) != 1 && Integer.parseInt(parameters.get("type")) != 2  ){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "参数type有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		User user = new User();
		user.id = userId;
		
		//user.withdrawal(Double.parseDouble(parameters.get("amount")), Integer.parseInt(parameters.get("bankId")), payPassword, Integer.parseInt(parameters.get("type")), false, error);
		long withdrawalId = user.withdrawal(Double.parseDouble(parameters.get("amount")), 0, "", Integer.parseInt(parameters.get("type")), true, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
//		double amount = User.queryRechargeIn(user.id, error);
		
		 if(error.code < 0) {
				jsonMap.put("error", "-4");
				jsonMap.put("msg", error.msg);
				
				return JSONUtils.printObject(jsonMap);
			}
		 
	//	double withdrawalAmount = user.balance - amount;//（最高）可提现余额
		
		Map<String, Object> map = new HashMap<String, Object>();
		map=PaymentProxy.getInstance().withdraw(error, Constants.APP, withdrawalId, Double.parseDouble(parameters.get("amount")),userId);	
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "提现申请成功");
		/*jsonMap.put("withdrawalAmount",withdrawalAmount);
		jsonMap.put("userBalance", user.balanceDetail.user_amount + user.balanceDetail.freeze);*/
		jsonMap.put("htmlParam", map.get("htmlParam"));
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *提现初始信息
	 */
	public static String withdrawal(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("user_id")) ){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}

		long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		User user = new User();
		user.id = userId;
		
        double amount = User.queryRechargeIn(user.id, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		double withdrawalAmount = user.balance - amount;//（最高）可提现余额
		
//		withdrawalAmount = ServiceFee.maxWithdralAmount(withdrawalAmount);
		
		if(withdrawalAmount < 0) {
			withdrawalAmount = 0;
		}
		
		List<UserBankAccounts> banks = UserBankAccounts.queryUserAllBankAccount(user.id);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "提现初始信息查询成功");
		jsonMap.put("withdrawalAmount",withdrawalAmount);
		//真实姓名
		jsonMap.put("realityName",user.realityName);
		//用户名
		jsonMap.put("name",user.name);

		jsonMap.put("bankList", banks);
		jsonMap.put("userBalance", user.balanceDetail.user_amount + user.balanceDetail.freeze);
		
		if(StringUtils.isBlank(user.payPassword)){
			jsonMap.put("payPasswordStatus", false);
		}else{
			jsonMap.put("payPasswordStatus", true);
		}
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *提现记录
	 */
	public static String withdrawalRecords(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("user_id")) ){
			jsonMap.put("error", "-2");
						jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id错误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		PageBean<v_user_withdrawals> page = User.queryWithdrawalRecord(userId, parameters.get("type"), 
				parameters.get("beginTime"), parameters.get("endTime"), parameters.get("currPage"), "18", error);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "提现记录查询成功");
		jsonMap.put("records",page);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 上传文件
	 */
	public static String uploadFile(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		if(StringUtils.isBlank(parameters.get("id"))){
			jsonMap.put("error", "-2");
						jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("type"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入 文件类型有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("imgStr"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入 文件参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("fileExt"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入 文件后缀有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String type = parameters.get("type");
		String imgStr = parameters.get("imgStr");
		String fileExt = parameters.get("fileExt");
		
		byte [] imgByte = null;
		
		try {
			imgByte = new sun.misc.BASE64Decoder().decodeBuffer(imgStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File file = FileUtil.strToFile(imgByte, "tmp\\uploads\\"+System.currentTimeMillis()+"."+fileExt); 
		
		Map<String, Object> fileInfo = FileUtil.uploadFile(file, Integer.parseInt(type), error);
		
		if (error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		User user = new User();
		user.id = userId;
		user.photo = fileInfo.get("fileName").toString();
		user.editPhoto(error);
		
		if (error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", error.msg);
		jsonMap.put("imgStr",user.photo);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 *官方活动
	 */
	public static String queryOfficialActivity(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		PageBean<t_content_news> page = News.queryOfficialActivity(parameters.get("currPageStr"), null, error);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "官方活动查询成功");
		jsonMap.put("records",page.page);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 取消关注
	 */
	public static String cancelAttentionUser(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String attention = parameters.get("attentionId");
		
		if(StringUtils.isBlank(attention) || !NumberUtil.isNumeric(attention)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入关注用户id参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		User.cancelAttentionUser(Long.parseLong(attention), error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", error.msg);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 获取vip相关信息
	 */
	public static String vipInfo(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);

		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "vip信息获取 成功");
		jsonMap.put("vipFee", backstageSet.vipFee);
		jsonMap.put("vipTimeType", backstageSet.vipTimeType);
		jsonMap.put("vipMinTimeType", backstageSet.vipMinTimeType);
		jsonMap.put("vipMinTimeLength", backstageSet.vipMinTimeLength);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 删除黑名单
	 */
	public static String deleteBlackList(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String blacklistId = parameters.get("blacklistId");
		
		if(StringUtils.isBlank(blacklistId) || !NumberUtil.isNumeric(blacklistId)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入黑名单用户id参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);

		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "解析用户id错误");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		User user = new User();
		
		user.deleteBlacklist(userId, Long.parseLong(blacklistId), error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", error.msg);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 删除收藏标
	 */
	public static String deleteAttentionBid(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String bidId = parameters.get("bidId");
		
		if(StringUtils.isBlank(bidId) || !NumberUtil.isNumeric(bidId)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入标id参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);

		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");

			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");

			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Debt.cancleBid(Long.parseLong(bidId), userId, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", error.msg);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 删除收藏债权
	 */
	public static String deleteAttentionBebt(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String attentionId = parameters.get("attentionDebtId");
		
		if(StringUtils.isBlank(attentionId) || !NumberUtil.isNumeric(attentionId)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入关注债权id参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);

		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Debt.canaleDebt(Long.parseLong(attentionId), error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", error.msg);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 保存推送设置
	 */
	public static String pushSetting(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		String billPush = parameters.get("billPush");
		
		if(StringUtils.isBlank(billPush) || !NumberUtil.isNumeric(billPush)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入账单设置参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String investPush = parameters.get("investPush");
		
		if(StringUtils.isBlank(investPush) || !NumberUtil.isNumeric(investPush)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入满标设置参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String activityPush = parameters.get("activityPush");
		
		if(StringUtils.isBlank(activityPush) || !NumberUtil.isNumeric(activityPush)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "传入活动单设置参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);

		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		User user = new User();
		
		user.isBillPush = billPush.equals("1") ? true : false;
		user.isInvestPush = investPush.equals("1") ? true : false;
		user.isActivityPush = activityPush.equals("1") ? true : false;
		
		user.pushSetting(userId, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", error.msg);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 获取推送推送设置
	 */
	public static String queryPushSetting(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);

		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		t_users user = User.queryPushSetting(userId, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", error.msg);
		jsonMap.put("billPush", user.is_bill_push ? 1 : 0);
		jsonMap.put("investPush", user.is_invest_push ? 1 : 0);
		jsonMap.put("activityPush", user.is_activity_push ? 1 : 0);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 提交用户未交费资料
	 */
	public static String createUserAuditItem(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);

		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		String items = parameters.get("items");
		
		if(StringUtils.isBlank(items)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请选择上传的资料");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userItemId = Security.checkSign(parameters.get("sign"), Constants.USER_ITEM_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "上传资料参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		UserAuditItem item = new UserAuditItem();
		item.lazy = true;
		item.userId = userId;
		item.id = userItemId;
		item.imageFileNames = items;
		item.createUserAuditItem(error);
		
		if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "提交资料成功，请等待管理员审核");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 提交用户资料
	 */
	public static String submitUploadedItems(Map<String, String> parameters) throws IOException {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);

		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		Map<String, Object> info = UserAuditItem.queryUploadItems(userId, error);
 		if (error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请选择上传的资料");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userItemId = Security.checkSign(parameters.get("sign"), Constants.USER_ITEM_ID_SIGN, Constants.VALID_TIME, error);
		if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "解析用户资料id有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		double balance = 0;
		
		if(info.get("fees") == null) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "用户没有上传未付款的资料");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		double fees = (Double) info.get("fees");
		User user = new User();
		user.id = userId;
		v_user_for_details details = user.balanceDetail;
		
		if(null == details) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "查询用户资金出现错误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(Constants.IPS_ENABLE){
			balance = details.user_amount2;
		}else{
			balance = details.user_amount;
		}
		
		if(fees > balance){
			jsonMap.put("error", "-999");
			jsonMap.put("msg", "对不起，您可用余额不足");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		
		UserAuditItem item = new UserAuditItem();
		item.id = userItemId;
		
		item.submitUploadedItems(userId, balance, error);
		
		if(error.code == -999) {
			jsonMap.put("error", "-999");
			jsonMap.put("msg", "您余额不足，请充值");
			
			return JSONUtils.printObject(jsonMap);
		} else if(error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "提交成功！");
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/*
	 * 清空用户上传未付款的资料
	 */
	public static String clearAuditItem(Map<String, String> parameters) throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);

		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		UserAuditItem.clearUploadedItems(userId, error);
		
		if( error.code < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "成功清空用户上传未付款的资料");
		
		return JSONUtils.printObject(jsonMap);
	}

	/**
	 * APP端用户启动图
	 * @param parameters
	 * @return
	 * @throws IOException 
	 */
	public static String getStartMap(Map<String, String> parameters) throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		List<String> fileNames = Ads.queryAdsImageNamesByLocation(Constants.STARTUP_BOOT_APP, error);
		
		if(error.code < 0) {
			jsonMap.put("error", -2);
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("fileNames", fileNames);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/**
	 * 查询所有的借款标产品(APP)
	 * @return
	 * @throws IOException
	 */
	public static String queryAllProducts() throws IOException{
		ErrorInfo error = new ErrorInfo();
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<t_products> products = Product.queryAllProducts(error);
		
		if (error.code < 0) {
			jsonMap.put("error", -2);
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询所有借款产品成功！");
		jsonMap.put("products", products);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/**
	 * 查询所有的返款方式
	 * @return
	 * @throws IOException 
	 */
	public static String queryAllRepaymentTypes() throws IOException{
		ErrorInfo error = new ErrorInfo();
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<Repayment> repayments = Bid.Repayment.queryRepaymentTypeApp(error);
		
		if (error.code < 0) {
			
			jsonMap.put("error", -2);
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "查询所有的还款方式成功！");
		jsonMap.put("repayments", repayments);
		
		return JSONUtils.printObject(jsonMap);
	}
	/**
	 * 查询账户资产详情
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public static String queryAccountCapital(Map<String, String> parameters) throws IOException {
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> map = new HashMap<String, Object>();
		String userIdStr = parameters.get("id");
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		if(error.code < 0 || userId < 0){
			map.put("error", "-2");
			map.put("msg", "解析用户id有误");
			
			return JSONObject.fromObject(map).toString();
		}
		
		
		UserOZ accountInfo = new UserOZ(userId);
		
		map.put("balance", accountInfo.user_account-accountInfo.freeze);
		map.put("amount", accountInfo.user_account);
		map.put("frozen_amount", accountInfo.freeze);
		map.put("repay_amount", accountInfo.receive_amount);
		map.put("back_amount", accountInfo.repayment_amount);
		map.put("sum_income", BillInvests.querySumIncome(userId));
		
		List<UserBankAccounts> userBanks = UserBankAccounts.queryUserAllBankAccount(userId);
		
		map.put("hasBanks", userBanks.size() > 0);
		
		map.put("error", "-1");
		map.put("msg", "查询成功");
		
		return JSONUtils.printObject(map);
	}
	
	/**
	 * 查询账户收益
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public static String queryAccountIncome(Map<String, String> parameters) throws IOException {
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> map = new HashMap<String, Object>();
		String userIdStr = parameters.get("id");
		long userId = Security.checkSign(userIdStr, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		map.put("sum_income", BillInvests.querySumIncome(userId));
		map.put("month_income", BillInvests.queryMonthSumIncome(userId));
		map.put("year_income", BillInvests.queryYearSumIncome(userId));
		map.put("share_income_url", "");
		map.put("error", "-1");
		map.put("msg", "查询成功");
		return JSONUtils.printObject(map);
	}

	/**
	 * 手机号码注册
	 * @param parameters
	 * @return
	 * @throws IOException 
	 */
	public static String registerMobile(Map<String, String> parameters) throws IOException {
		ErrorInfo error = new ErrorInfo();

		String name = parameters.get("userName");		
		String mobile = parameters.get("mobile");
		String smsCode = parameters.get("smsCode");
		String password = parameters.get("password");
		password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);
		String recommendName = parameters.get("recommendName");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", "-3");
		
		if (StringUtils.isBlank(name)) {
			map.put("msg", "请填写用户名");
			return JSONUtils.printObject(map);
		}

		if (StringUtils.isBlank(password)) {
			map.put("msg", "请输入密码");
			return JSONUtils.printObject(map);
		}
		if(!name.equals(mobile)){
			if (!RegexUtils.isValidUsername(name)) {
				map.put("msg", "请填写符合要求的用户名");
				return JSONUtils.printObject(map);
			}
		}

		if (!RegexUtils.isMobileNum(mobile)) {
			map.put("msg", "请填写正确的手机号码");
			return JSONUtils.printObject(map);
		}
		
		if (StringUtils.isBlank(smsCode)) {
			map.put("msg", "请输入短信校验码");
			return JSONUtils.printObject(map);
		}
		
		if(Constants.CHECK_MSG_CODE) {
			String cCode = (String) Cache.get(mobile);
			
			if(cCode == null) {
				map.put("msg", "校验码已失效，请重新点击发送校验码");
				return JSONUtils.printObject(map);
			}
			
			if(!smsCode.equals(cCode)) {
				map.put("msg", "校验码错误");
				return JSONUtils.printObject(map);
			}
		}
		
		User.isMobileExist(mobile, null, error);

		if (error.code < 0) {
			map.put("msg", error.msg);
			return JSONUtils.printObject(map);
		}
		
		if (!RegexUtils.isValidPassword(password)) {
			map.put("msg", "请填写符合要求的密码");
			return JSONUtils.printObject(map);
		}
		
		User.isNameExist(name, error);

		if (error.code < 0) {
			map.put("msg", error.msg);
			return JSONUtils.printObject(map);
		}
		
		String recoName = "";
		
		if(StringUtils.isNotBlank(recommendName)) {
			User.isNameExist(recommendName, error);
			if (error.code == -2) {
				recoName = recommendName;
		    }
		}
		
		User user = new User();
		user.time = new Date();
		user.name = name;
		user.password = password;
		user.mobile = mobile;
		user.isMobileVerified = true;
		user.recommendUserName = recoName;
		user.register(Constants.CLIENT_PC, error);
		
		if (error.code < 0) {
			map.put("msg", error.msg);
			return JSONUtils.printObject(map);
		}

		map.put("msg", "注册成功");
		map.put("error", "-1");
		return JSONUtils.printObject(map);
	}

	public static String getBankList() throws IOException {
		Map<String,Object> map =new HashMap<String, Object>();
		
		Map<String,String> bankCodeNameTable = DictBanksDate.bankCodeNameTable;
		Object[] bankCodeIds = bankCodeNameTable.keySet().toArray();
		String[] bankCodeNames = new String[bankCodeIds.length];
		for (int i = 0; i < bankCodeNames.length; i++) {
			bankCodeNames[i] = bankCodeNameTable.get(bankCodeIds[i]);
		}
		map.put("bankCodeIds", bankCodeIds);
		map.put("bankCodeNames", bankCodeNames);
		map.put("error", "-1");
		map.put("msg", "查询成功");
		return JSONUtils.printObject(map);
	}
	
	public static String getProvinceList() throws IOException {
		Map<String,Object> map =new HashMap<String, Object>();
		
		Map<String,String> provinceCodeNameTable = DictBanksDate.provinceCodeNameTable;
		Object[] provinceIds = provinceCodeNameTable.keySet().toArray();
		String[] provinceNames = new String[provinceIds.length];
		for (int i = 0; i < provinceNames.length; i++) {
			provinceNames[i] = provinceCodeNameTable.get(provinceIds[i]);
		}
		map.put("provinceIds", provinceIds);
		map.put("provinceNames", provinceNames);
		map.put("error", "-1");
		map.put("msg", "查询成功");
		
		return JSONUtils.printObject(map);
	}
	
	/**
	 * 获取银行，省份列表
	 */
	public static String getBankAndProvinceList() throws IOException {
		Map<String,Object> map =new HashMap<String, Object>();
		
		Map<String,String> provinceCodeNameTable = DictBanksDate.provinceCodeNameTable;
		Object[] provinceIds = provinceCodeNameTable.keySet().toArray();
		String[] provinceNames = new String[provinceIds.length];
		for (int i = 0; i < provinceNames.length; i++) {
			provinceNames[i] = provinceCodeNameTable.get(provinceIds[i]);
		}
		map.put("provinceIds", provinceIds);
		map.put("provinceNames", provinceNames);
		
		map.put("citylist", DictBanksDate.citys);
		
		Map<String,String> bankCodeNameTable = DictBanksDate.bankCodeNameTable;
		Object[] bankCodeIds = bankCodeNameTable.keySet().toArray();
		String[] bankCodeNames = new String[bankCodeIds.length];
		for (int i = 0; i < bankCodeNames.length; i++) {
			bankCodeNames[i] = bankCodeNameTable.get(bankCodeIds[i]);
		}
		map.put("bankCodeIds", bankCodeIds);
		map.put("bankCodeNames", bankCodeNames);
		map.put("error", "-1");
		map.put("msg", "查询成功");
		return JSONUtils.printObject(map);
	}
	
	/**
	 * 按省份ID获取城市
	 */
	public static String getCityeList(Map<String, String> parameters) throws IOException {
		Map<String,Object> map =new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		String provinceCode = parameters.get("provinceCode");
		Map<String,String> cityMaps = DictBanksDate.queryCityCode2NameByProvinceCode(Integer.valueOf(provinceCode), error);
		
		Object[] cityIds = cityMaps.keySet().toArray();
		String[] cityNames = new String[cityIds.length];
		for (int i = 0; i < cityNames.length; i++) {
			cityNames[i] = cityMaps.get(cityIds[i]);
		}
		map.put("cityIds", cityIds);
		map.put("cityNames", cityNames);
		map.put("error", "-1");
		map.put("msg", "查询成功");
		return JSONUtils.printObject(map);
	}

	/**
	 * 添加银行卡
	 * @param parameters
	 * @return
	 * @throws IOException 
	 */
	public static String addBankcardInfo(Map<String, String> parameters) throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(StringUtils.isBlank(parameters.get("id")) ){
			jsonMap.put("error", "-2");
						jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("addBankCode"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "银行不能为空");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(parameters.get("addAccount"))){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "银行卡账号有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		String addAccountName = parameters.get("addAccountName");		
		if(StringUtils.isBlank(addAccountName)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "收款人有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		if (!CharUtil.isChinese(addAccountName)) {
			jsonMap.put("error", "-4");
			jsonMap.put("msg", "收款人必须是中文");
			return JSONUtils.printObject(jsonMap);
		}
		ErrorInfo error = new ErrorInfo();
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        if(error.code < 0){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		
		int addBankCode = Integer.valueOf(parameters.get("addBankCode"));
		int addProviceCode = Integer.valueOf(parameters.get("addProviceCode"));
		int addCityCode = Integer.valueOf(parameters.get("addCityCode"));
		String addBranchBankName = parameters.get("addBranchBankName");
		String addAccount = parameters.get("addAccount");
		
		
		String bankName = DictBanksDate.queryBankByCode(addBankCode);
		String provice = DictBanksDate.queryProvinceByCode(addProviceCode);
		String city = DictBanksDate.queryCityByCode(addCityCode);
		
		UserBankAccounts bankUser =  new UserBankAccounts();
		
		bankUser.userId = userId;
		bankUser.bankName = bankName;
		bankUser.bankCode = addBankCode;
		bankUser.provinceCode = addProviceCode;
		bankUser.cityCode = addCityCode;
		bankUser.branchBankName = addBranchBankName;
		bankUser.province = provice;
		bankUser.city = city;
		bankUser.account = addAccount;
		bankUser.accountName = addAccountName;
		
		bankUser.addUserBankAccount(error, true);
		if(error.code < 0 ){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", error.msg);
		}else{
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "添加成功");
		}
		return JSONUtils.printObject(jsonMap);
	}

	/**
	 * 修改邮箱
	 * @param parameters
	 * @return
	 * @throws IOException 
	 */
	public static String updateEmail(Map<String, String> parameters) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(parameters.get("id")) ){
			map.put("error", "-2");
			map.put("msg", "用户id有误");
			
			return JSONUtils.printObject(map);
		}
		String email = parameters.get("email");
		if(StringUtils.isBlank(email) ){
			map.put("error", "-3");
			map.put("msg", "邮箱不能为空");
			
			return JSONUtils.printObject(map);
		}
		
		ErrorInfo error = new ErrorInfo();
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        
        if(error.code < 0){
			map.put("error", "-3");
			map.put("msg", "解析用户id有误");
			
			return JSONUtils.printObject(map);
		}
        User.updateEmail(userId, email, error);
        
        if(error.code < 0){
        	map.put("error", "-3");
			map.put("msg", error.msg);
			return JSONUtils.printObject(map);
        }
        map.put("error", "-1");
        return JSONUtils.printObject(map);
	}

	/**
	 * 修改手机 
	 * @param parameters
	 * @return
	 * @throws IOException 
	 */
	public static String updateMobile(Map<String, String> parameters) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(parameters.get("id")) ){
			map.put("error", "-2");
			map.put("msg", "用户id有误");
			
			return JSONUtils.printObject(map);
		}
		String mobile = parameters.get("mobile");
		if(StringUtils.isBlank(mobile) ){
			map.put("error", "-3");
			map.put("msg", "手机不能为空");
			return JSONUtils.printObject(map);
		}
		
		ErrorInfo error = new ErrorInfo();
        long userId = Security.checkSign(parameters.get("id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
        
        if(error.code < 0){
			map.put("error", "-3");
			map.put("msg", "解析用户id有误");
			
			return JSONUtils.printObject(map);
		}
        User.updateMobile(userId, mobile, error);
        
        if(error.code < 0){
        	map.put("error", "-3");
			map.put("msg", error.msg);
			return JSONUtils.printObject(map);
        }
        map.put("error", "-1");
        return JSONUtils.printObject(map);
	}

	/**
	 * 获取平台信息
	 * @return
	 * @throws IOException 
	 */
	public static String getPlatformInfo(Map<String, String> parameters) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String deviceTypeStr = parameters.get("deviceType");
		
		if(StringUtils.isBlank(deviceTypeStr)) {
			map.put("error", "-3");
			map.put("msg", "请传入设备参数");
			
			return JSONObject.fromObject(map).toString();
		}
		
		if(!NumberUtil.isNumeric(parameters.get("deviceType"))) {
			map.put("error", "-3");
			map.put("msg", "请传入正确的设备参数");
			
			return JSONObject.fromObject(map).toString();
		}
		int deviceType = Integer.parseInt(parameters.get("deviceType"));
		BackstageSet bs = BackstageSet.getCurrentBackstageSet();
		map.put("platformName",bs.platformName);
		map.put("platformTelephone",bs.platformTelephone);
		map.put("serviceTime",bs.workStatrTime+"~("+bs.workEndTime+")");
		map.put("workStatrTime",bs.workStatrTime);
		map.put("workEndTime",bs.workEndTime);
		map.put("version", deviceType == 1 ? bs.androidVersion : bs.iosVersion);
		map.put("code", deviceType == 1 ? bs.androidCode : bs.iosCode);
		map.put("error", "-1");
		return JSONUtils.printObject(map);
	}
	
	/**
	 * APP 开通资金托管
	 * @param parameters
	 * @return
	 * @throws IOException 
	 */
	public static String createAcct(Map<String, String> parameters) throws IOException {
		//注册成功后需要提示用户完善基本信息，用于开通环迅资金托管账号
		ErrorInfo error = new ErrorInfo();
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
	
		//真实姓名
		String realName = parameters.get("realName");
		
		if (StringUtils.isBlank(realName)) {
			jsonMap.put("error", "2");
			jsonMap.put("msg", "真实姓名不能为空");
			return JSONUtils.printObject(jsonMap);
		}
		
		
		//身份证号码(验证)
		String idNo = parameters.get("idNo");
		
		if (StringUtils.isBlank(idNo)) {
			jsonMap.put("error", "2");
			jsonMap.put("msg", "身份证号码不能为空");
			return JSONUtils.printObject(jsonMap);
		}
		
		User user = new User();
		user.id = userId;
		
        
		//手机号码(验证)
		String mobile = parameters.get("cellPhone1");
		
		if(StringUtils.isBlank(mobile) ) {
			jsonMap.put("error", "2");
			jsonMap.put("msg", "手机号码不能为空");
			return JSONUtils.printObject(jsonMap);
		}
		
		if(!RegexUtils.isMobileNum(mobile)) {
			
			jsonMap.put("error", "2");
			jsonMap.put("msg", "请输入正确的手机号码");
			return JSONUtils.printObject(jsonMap);
		}
		
		
		
		//邮箱(验证)
		String email = parameters.get("email");
		
		if (!RegexUtils.isEmail(email)) {
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "请填写正确的邮箱");
			return JSONUtils.printObject(jsonMap);
		}
		//验证邮箱是否存在
		User.isEmailExist(userId,email, error);
	
		/*if(!user.isEmailVerified){
		jsonMap.put("error", "-30");
		jsonMap.put("msg", "请先激活邮箱");
		return JSONUtils.printObject(jsonMap);
		}*/
		
		if (error.code < 0) {
			jsonMap.put("error", "2");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		
		
		if (User.isIPSacctNoExist(userId, error)) {
			jsonMap.put("error", "-31");
			jsonMap.put("msg", "用户已开通资金托管账号");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
       
		user.checkRealName(realName, idNo,user.idNumber, error);
        
		if (error.code < 0) {
			jsonMap.put("error", "2");
			jsonMap.put("msg", error.msg);
			return JSONUtils.printObject(jsonMap);
		}
		user.idNumber = idNo;
		user.realityName = realName;
		user.mobile = mobile;
		user.email = email;
		user.saveUserCreateInfo(user, error);
		
		if (error.code < 0) {
			jsonMap.put("error", "2");
			jsonMap.put("msg", error.msg);
			
			return JSONUtils.printObject(jsonMap);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map = PaymentProxy.getInstance().register(error, Constants.CLIENT_APP, user);
		
		String key ="createAcct" + map.get("pMerBillNo");
		Cache.add(key, "true",IPSConstants.CACHE_TIME);
		
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "开户成功");
		jsonMap.put("htmlParam", map.get("htmlParam"));
		return JSONObject.fromObject(jsonMap).toString(); 
	}
	
	
	/**
	 * APP 返回充值银行
	 * @param parameters
	 * @return
	 * @throws IOException 
	 */
	public static String rechargeBankList(Map<String, String> parameters) throws IOException {
		ErrorInfo error = new ErrorInfo();
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		long userId = Security.checkSign(parameters.get("user_id"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if (!User.isIPSacctNoExist(userId, error)) {
			jsonMap.put("error", "-31");
			jsonMap.put("msg", "用户未开通资金托管账号");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		User user = new User();
		user.id = userId;
	
		
		List<Map<String, Object>> bankList = PaymentProxy.getInstance().queryBanks(error, Constants.APP);
		JSONArray list = JSONArray.fromObject(bankList);
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "充值初始信息查询成功");
		
		jsonMap.put("balance",user.balance);
		jsonMap.put("amount", user.balanceDetail.user_amount + user.balanceDetail.freeze);
		jsonMap.put("list", list);
		
		System.out.println(bankList);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 * APP 确认充值
	 * @param parameters
	 * @return
	 * @throws IOException 
	 */
	public static String recharge(Map<String, String> parameters) throws IOException {
		ErrorInfo error = new ErrorInfo();
		
		double money = Double.parseDouble(parameters.get("amount"));
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0 || userId < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		if (!User.isIPSacctNoExist(userId, error)) {
			jsonMap.put("error", "-31");
			jsonMap.put("msg", "用户未开通资金托管账号");
			User user = new User();
			user.id = userId;
			Map<String, Object> map=new HashMap<String, Object>();
			map = PaymentProxy.getInstance().register(error,Constants.CLIENT_APP, user);
			jsonMap.put("htmlParam", map.get("htmlParam"));
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		User user = new User();
		user.id = userId;
		user.setCurrUser(user);
		
		BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
			if(money < backstageSet.rechargeLowest){
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "最低充值" + backstageSet.rechargeLowest + "元");
				return JSONObject.fromObject(jsonMap).toString();
			}
			
			if (money > Constants.MAX_VALUE) {
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "充值金额范围需在[" + backstageSet.rechargeLowest + "~" + Constants.MAX_VALUE + "]之间");
				return JSONObject.fromObject(jsonMap).toString();
			}
			
			Map<String, Object> map=new HashMap<String, Object>();
			
			map = PaymentProxy.getInstance().recharge(error,Constants.CLIENT_APP, money);
		
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "充值成功");
			jsonMap.put("htmlParam", map.get("htmlParam"));
			String s = JSONObject.fromObject(jsonMap).toString();
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	
	/**
	 * 查询用户开户信息
	 * @param id 用户id
	 * @throws IOException
	 */
	public static String queryCreateInfo(Map<String, String> parameters) throws IOException{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ErrorInfo error = new ErrorInfo();
		
		User user = new User();
		
		if (StringUtils.isBlank(parameters.get("userId"))) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请求用户id参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		user.id = userId;
		
		if (user.id < 0) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该用户不存在");
			return JSONUtils.printObject(jsonMap);
		}
		
		jsonMap.put("error", -1);
		jsonMap.put("msg", "该用户存在");
		jsonMap.put("realName", user.realityName);
		jsonMap.put("email", user.email);
		
		jsonMap.put("idNo", user.idNumber);
		jsonMap.put("cellPhone1", user.mobile);
		jsonMap.put("randomCode1", null);
		jsonMap.put("isAddBaseInfo", user.isAddBaseInfo);
		
		return JSONUtils.printObject(jsonMap);
	}
	
	/**
	 * 查询本金保障页面
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public static String queryCapitalProtectionPage(Map<String, String> parameters) throws IOException{
		ErrorInfo error = new ErrorInfo();
		
		//查询本金保障
		List<Map<String, String>> list = News.queryPrincipleProtection(error);
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(error.code < 0){
			jsonMap.put("error", "-4");
			jsonMap.put("msg", error.msg);
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "查询本金保障页面成功");
		jsonMap.put("totalNum", list.size());
		jsonMap.put("list", list);
		
		return JSONObject.fromObject(jsonMap).toString();
	}
	
	/**
	 *标的回复
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public static String bidQuest(Map<String, String> parameters) throws IOException{
		ErrorInfo error = new ErrorInfo();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		User user = new User();
		
		if (StringUtils.isBlank(parameters.get("userId"))) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请求用户id参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		if (StringUtils.isBlank(parameters.get("questionId"))) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请求questionId参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		if (StringUtils.isBlank(parameters.get("content"))) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "请求content参数有误");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		long userId = Security.checkSign(parameters.get("userId"), Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "您还没有登录或登录已过时，请重新登录");
			
			return JSONUtils.printObject(jsonMap);
		}
		
		user.id = userId;
		
		BidAnswers answer = new BidAnswers();
		answer.content = parameters.get("content");
		answer.questionId = Long.valueOf(parameters.get("questionId"));
		answer.createAnswers(userId,error);
		if(error.code<0){
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "回复失败");
		}else{
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "回复成功");
		}
		
		return JSONObject.fromObject(jsonMap).toString();
	}

	public static String verifyUnamePhone(Map<String, String> parameters)  throws IOException {
		ErrorInfo error = new ErrorInfo();

		String name = parameters.get("name");		
		String mobile = parameters.get("cellPhone");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", "-3");
		
		if (StringUtils.isBlank(name)) {
			map.put("msg", "请填写用户名");
			return JSONUtils.printObject(map);
		}

		
		if (!RegexUtils.isMobileNum(mobile)) {
			map.put("msg", "请填写正确的手机号码");
			return JSONUtils.printObject(map);
		}
		
		User.isMobileExist(mobile, null, error);

		if (error.code < 0) {
			map.put("msg", error.msg);
			return JSONUtils.printObject(map);
		}
		
		
		
		User.isNameExist(name, error);

		if (error.code < 0) {
			map.put("msg", error.msg);
			return JSONUtils.printObject(map);
		}
		
		SMSUtil.sendCode(mobile, error);
		//map.put("msg", "注册成功");
		map.put("error", "-1");
		return JSONUtils.printObject(map);
	}
}
