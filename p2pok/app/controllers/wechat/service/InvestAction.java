package controllers.wechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;

import payment.PaymentProxy;
import play.db.jpa.JPA;

import com.shove.security.Encrypt;

import net.sf.json.JSONObject;
import models.t_bids;
import models.v_front_all_bids;
import models.v_invest_records;
import utils.CaptchaUtil;
import utils.ErrorInfo;
import utils.NumberUtil;
import utils.PageBean;
import utils.QueryUtil;
import utils.Security;
import business.Bid;
import business.CreditLevel;
import business.Invest;
import business.Product;
import business.User;
import business.UserAuditItem;
import constants.Constants;
import controllers.BaseController;
import controllers.wechat.account.WechatAccountHome;

/**
 * 我要投资相关
 * 
 * @author Administrator
 *
 */
public class InvestAction extends BaseController {

	/**
	 * 查询所有的标,第一次显示10条数据
	 */
	public static void queryAllBids() {
		ErrorInfo error = new ErrorInfo();
		List<Product> list = Product.queryProductNames(true, error);

		if (error.code < 0) {
			flash.error(error.msg);
			WechatAccountHome.errorShow(error.msg);
		}

		List<Product> products = Product.queryProductNames(true, error);

		List<CreditLevel> creditLevels = CreditLevel
				.queryAllCreditLevels(error);

		String orderType = params.get("orderType");

		String keywords = params.get("keywords");

		PageBean<v_front_all_bids> pageBean = new PageBean<v_front_all_bids>();

		pageBean = Invest.queryAllBids(Constants.SHOW_TYPE_1,
				Constants.WECHAT_CURRPAGE, Constants.WECHAT_PAGESIZE, null,
				null, null, null, null, null, null, null, orderType, keywords,
				error);

		if (error.code < 0) {
			flash.error(error.msg);
			WechatAccountHome.errorShow(error.msg);;
		}
        String bidSql = "select 1 from t_bids where status IN (1, 2) and show_type&?<>0";
        List<Object> params = new ArrayList<Object>();
		params.add(Constants.SHOW_TYPE_1);
		EntityManager em = JPA.em();
		int bidCount =  QueryUtil.getQueryCountByCondition(em, bidSql, params);
		String transferSql = "select 1 from t_invest_transfers where status='1' and type='2'";
		  List<Object> transferParams = new ArrayList<Object>();
		  int transferCount = QueryUtil.getQueryCountByCondition(em, transferSql, transferParams);
		render(list, creditLevels, products, pageBean,bidCount,transferCount);
	}

	/**
	 * 查询标分页（ajax)
	 */
	public static void queryAllBidsAjax() {
		ErrorInfo error = new ErrorInfo();

		int currPage = Constants.WECHAT_CURRPAGE;
		int pageSize = Constants.WECHAT_PAGESIZE;

		String currPageStr = params.get("currPage");
		String pageSizeStr = params.get("pageSize");

		if (NumberUtil.isNumericInt(currPageStr)) {
			currPage = Integer.parseInt(currPageStr);
		}

		if (NumberUtil.isNumericInt(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}

		String orderType = params.get("orderType");
		String keywords = params.get("keywords");

		PageBean<v_front_all_bids> pageBean = new PageBean<v_front_all_bids>();
		pageBean = Invest.queryAllBids(Constants.SHOW_TYPE_1, currPage,
				pageSize, null, null, null, null, null, null, null, null,
				orderType, keywords, error);
		JSONObject json = new JSONObject();
		json.put("pageBean", pageBean);
		json.put("error", error);

		renderJSON(json);
	}

	/**
	 * 查看标的详情
	 * 
	 * @param bidId
	 * @param showBox
	 */
	public static void queryBidDetail(long bidId, String showBox) {
		ErrorInfo error = new ErrorInfo();
		Bid bid = new Bid();
		bid.id = bidId;

		/* 进入详情页面增加浏览次数 */
		Invest.updateReadCount(bidId, error);

		if (error.code < 0) {
			flash.error(error.msg);
			WechatAccountHome.errorShow(error.msg);
		}

		Map<String, String> historySituationMap = User.historySituation(
				bid.userId, error);// 借款者历史记录情况
		List<UserAuditItem> uItems = UserAuditItem.queryUserAllAuditItem(
				bid.userId, bid.mark); // 用户正对产品上传的资料集合

		if (error.code < 0) {
			flash.error(error.msg);
			WechatAccountHome.errorShow(error.msg);
		}
		User user = User.currUser();
		boolean ipsEnable = Constants.IPS_ENABLE;

		String uuid = CaptchaUtil.getUUID(); // 防重复提交UUID
		boolean flag = false;

		if (StringUtils.isNotBlank(showBox)) {
			showBox = Encrypt.decrypt3DES(showBox, bidId
					+ Constants.ENCRYPTION_KEY);

			if (showBox.equals(Constants.SHOW_BOX))
				flag = true;
		}

		/*
		 * 查询截止时间
		 */
		long endTime = 0;
		if (bid != null && bid.investExpireTime != null) {
			endTime = bid.investExpireTime.getTime();
		}

		/*
		 * 查找前10条投标记录
		 */
		PageBean<v_invest_records> pageBean = new PageBean<v_invest_records>();
		pageBean = Invest.queryBidInvestRecords(Constants.WECHAT_CURRPAGE,
				Constants.WECHAT_PAGESIZE, bidId, error);

		render(bid, flag, historySituationMap, uItems, user, ipsEnable, uuid,
				endTime, pageBean);
	}

	/**
	 * ajax查看投标记录
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param bidIdSign
	 */
	public static void queryBidInvestRecordsAjax(String bidIdSign) {

		ErrorInfo error = new ErrorInfo();

		long bidId = Security.checkSign(bidIdSign, Constants.BID_ID_SIGN,
				Constants.VALID_TIME, error);

		if (error.code < 0) {
			flash.error(error.msg);
			WechatAccountHome.errorShow(error.msg);
		}

		int currPage = Constants.WECHAT_CURRPAGE;
		int pageSize = Constants.WECHAT_PAGESIZE;

		String currPageStr = params.get("currPage");
		String pageSizeStr = params.get("pageSize");

		if (NumberUtil.isNumericInt(currPageStr)) {
			currPage = Integer.parseInt(currPageStr);
		}

		if (NumberUtil.isNumericInt(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}

		PageBean<v_invest_records> pageBean = new PageBean<v_invest_records>();
		pageBean = Invest.queryBidInvestRecords(currPage, pageSize, bidId,
				error);

		JSONObject json = new JSONObject();
		json.put("pageBean", pageBean);
		json.put("error", error);
		renderJSON(json);

	}

	/**
	 * 确认投标
	 * 
	 * @param sign
	 * @param uuid
	 */
	public static void confirmInvest(String sign, String uuid) {
		User user = User.currUser();

		if (null == user) {
			RegistAndLogin.login();
		}
		
		if (user.simulateLogin != null) {
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

		long bidId = Security.checkSign(sign, Constants.BID_ID_SIGN,
				Constants.VALID_TIME, error);

		if (bidId < 1) {
			flash.put("code", error.code);
			flash.put("msg", error.msg);

			queryBidDetail(bidId, "");
		}

		/* 防重复提交 */
		if (!CaptchaUtil.checkUUID(uuid)) {
			flash.put("code", "-1");
			flash.put("msg", "请求已提交或请求超时!");

			queryBidDetail(bidId, "");
		}

		/*
			if (Constants.IPS_ENABLE
					&& (User.currUser().getIpsStatus() != IpsCheckStatus.IPS)) {
				CheckAction.approve();
			}
		*/
		String investAmountStr = params.get("investAmount");
		String dealpwd = params.get("dealpwd");

		if (StringUtils.isBlank(investAmountStr)) {
			flash.put("code", "-1");
			flash.put("msg","投标金额不能为空！");
			queryBidDetail(bidId, "");
		}

		boolean b = investAmountStr.matches("^[1-9][0-9]*$");
		
    	if(!b) {
    		flash.put("code", "-1");
			flash.put("msg","对不起！只能输入正整数!");
    		queryBidDetail(bidId, "");
    	} 
    	
    	int investAmount = Integer.parseInt(investAmountStr);
		Invest.invest(user.id, bidId, investAmount, dealpwd, false, Constants.CLIENT_WECHAT, error);
		
		if (error.code == Constants.BALANCE_NOT_ENOUGH) {
			flash.put("code", error.code);
			flash.put("msg", error.msg);
			queryBidDetail(bidId, "");
		}

		//Map<String, String> bid = Invest.bidMap(bidId, error);

		if (error.code < 0) {
			flash.put("code", error.code);
			flash.put("msg", error.msg);
			queryBidDetail(bidId, "");
		}
		//20151023 jacky
	    t_bids tb =  t_bids.findById(bidId);
		double minInvestAmount = tb.min_invest_amount;
		double averageInvestAmount =tb.average_invest_amount;
		if(minInvestAmount == 0){//认购模式
			investAmount = (int) (investAmount*averageInvestAmount);
		}
		PaymentProxy.getInstance().invest(error, Constants.CLIENT_WECHAT, tb, user,investAmount);
		
		
		if (error.code > 0) {
			//flash
			flash.put("code", error.code);
			flash.put("msg", "投标成功");
			/*
			 * 投标成功，统一到标详情页面进行处理
			 */
			WechatAccountHome.myInvestBids(0, 1, null, 0);
			//queryBidDetail(bidId, "");
		}
	}

	/**
	 * 查看审核资料(异步)
	 */
	public static void showitemAjax(String mark, String signUserId){
		/* 解密userId */
		ErrorInfo error = new ErrorInfo();
		long userId = Security.checkSign(signUserId, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(userId < 1){
			flash.error(error.msg);
			WechatAccountHome.errorShow(error.msg);
		}
		
		UserAuditItem item = new UserAuditItem();
		item.lazy = true;
		item.userId = userId;
		item.mark = mark;
		
		JSONObject json = new JSONObject();
		json.put("item", item);
		json.put("error", error);
		
		renderJSON(json);
	}
	/**
	 * 分享页面
	 */
	public static void shareBidPage() {

		render();
	}
}
