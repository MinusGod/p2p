package controllers.front.invest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import models.t_bids;
import models.t_dict_ad_citys;
import models.t_dict_ad_provinces;
import models.t_dict_cars;
import models.t_dict_educations;
import models.t_dict_houses;
import models.t_dict_maritals;
import models.v_front_all_bids;
import models.v_front_user_attention_bids;
import models.v_invest_records;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import payment.PaymentProxy;
import play.cache.Cache;
import play.libs.Codec;
import play.mvc.With;
import utils.CaptchaUtil;
import utils.ErrorInfo;
import utils.NumberUtil;
import utils.PageBean;
import utils.Security;
import annotation.InactiveUserCheck;
import annotation.IpsAccountCheck;
import annotation.RealNameCheck;
import business.Bid;
import business.BidQuestions;
import business.CreditLevel;
import business.Invest;
import business.Product;
import business.User;
import business.UserAuditItem;

import com.shove.security.Encrypt;

import constants.Constants;
import constants.IPSConstants.IpsCheckStatus;
import controllers.BaseController;
import controllers.front.account.CheckAction;
import controllers.front.account.LoginAndRegisterAction;
import controllers.interceptor.UserStatusInterceptor;

/**
 * 
 * @author liuwenhui
 *
 */
@With(UserStatusInterceptor.class)
public class InvestAction extends BaseController{
	
	/**
	 * 我要投资首页
	 */
	
	public static void investHome(){
		
		ErrorInfo error = new ErrorInfo();
		List<Product> list = Product.queryProductNames(true, error);
		
		if(error.code < 0){
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		List<Product> products = Product.queryProductNames(true, error);
		
		List<CreditLevel> creditLevels = CreditLevel.queryAllCreditLevels(error);
		int currPage = Constants.ONE;
		int pageSize = Constants.SIX;
		
		String currPageStr = params.get("currPage");
		String pageSizeStr = params.get("pageSize");
		
		if(NumberUtil.isNumericInt(currPageStr)) {
			currPage = Integer.parseInt(currPageStr);
 		}
		
 		if(NumberUtil.isNumericInt(pageSizeStr)) {
 			pageSize = Integer.parseInt(pageSizeStr);
 		}
 		
 		String apr = params.get("apr");
 		String amount = params.get("amount");
 		String loanSchedule = params.get("loanSchedule");
 		String startDate = params.get("startDate");
 		String endDate = params.get("endDate");
 		String loanType = params.get("loanType");
 		String minLevel = params.get("minLevel");
 		String maxLevel = params.get("maxLevel");
 		String orderType = params.get("orderType");
 		String keywords = params.get("keywords");
 		
 		
		
		PageBean<v_front_all_bids>  pageBean = new PageBean<v_front_all_bids>();
		pageBean= Invest.queryAllBids(Constants.SHOW_TYPE_1, currPage,pageSize, apr, amount, loanSchedule, startDate, endDate, loanType, minLevel,maxLevel,orderType,keywords,error);
		
		if(error.code < 0){
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		render(list,creditLevels,products,pageBean);
	}
	
	
	
	/**
	 * 前台投资首页借款标分页
	 * @param pageNum
	 */
	public static void homeBids(int pageNum,int pageSize,String apr,String amount,String loanSchedule,String startDate,String endDate,String loanType,String minLevel,String maxLevel,String orderType,String keywords){
		
		ErrorInfo error = new ErrorInfo();
        int currPage = pageNum;
		
		if(params.get("currPage")!=null) {
			currPage = Integer.parseInt(params.get("currPage"));
		}
		
		PageBean<v_front_all_bids>  pageBean = new PageBean<v_front_all_bids>();
		pageBean= Invest.queryAllBids(Constants.SHOW_TYPE_1, currPage,pageSize, apr, amount, loanSchedule, startDate, endDate, loanType, minLevel,maxLevel,orderType,keywords,error);
		
		if(error.code < 0){
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		render(pageBean);
	}
	
	
	
	/**
	 * 用户查看自己所有的收藏标
	 */
	@IpsAccountCheck(true)
	@InactiveUserCheck(true)
	public static void queryUserCollectBids(int pageNum,int pageSize){
		
		ErrorInfo error = new ErrorInfo();
        int currPage = pageNum;
		
		if(params.get("currPage")!=null) {
			currPage = Integer.parseInt(params.get("currPage"));
		}
		User user = User.currUser();
		PageBean<v_front_user_attention_bids>  pageBean = Invest.queryAllCollectBids(user.id,currPage,pageSize,error);
		
		if(error.code < 0){
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		render(pageBean);
		
	}
	
	
	
	
	
	/**
	 * 向借款人提问
	 * @param bidId
	 */
	@IpsAccountCheck(true)
	@InactiveUserCheck(true)
	public static void questionToBorrower(String toUserIdSign,String bidIdSign,String content,String code,String inputCode){
		
		ErrorInfo error = new ErrorInfo();
		User user = User.currUser();
		JSONObject json = new JSONObject();
		
		long bidId = Security.checkSign(bidIdSign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			error.msg = "对不起！非法请求！";
			json.put("error", error);
			flash.put("error", content);
			renderJSON(json);
		}
		
		long toUserId = Security.checkSign(toUserIdSign, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			error.msg = "对不起！非法请求！";
			json.put("error", error);
			flash.put("error", content);
			renderJSON(json);
		}
		
		BidQuestions question = new BidQuestions();
		question.bidId = bidId;
		question.userId = user.id;
		question.time = new Date();
		question.content = content;
		question.questionedUserId = toUserId;
		String codes = (String)Cache.get(code);

		if (Constants.CHECK_CODE) {
			
			if(!codes.equalsIgnoreCase(inputCode)){
				error.msg = "对不起！验证码错误！";
				error.code = -1;
				json.put("error", error);
				flash.put("error", content);
				renderJSON(json);
			}
		}
		
		
		question.addQuestion(user.id,error);
		
		if(error.code < 0){
			json.put("content", content);
		}
		json.put("error", error);
		renderJSON(json);
	}
	
	
	
	
	/**
	 * 进入投标页面
	 * @param bidId
	 * @param investAmount 
	 */
	public static void invest(long bidId, String showBox){
		
		ErrorInfo error = new ErrorInfo();
		Bid bid = new Bid();
		bid.id=bidId;
		
		/*进入详情页面增加浏览次数*/
		Invest.updateReadCount(bidId,error);
		
		
		if(error.code < 0){
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		Map<String,String> historySituationMap = User.historySituation(bid.userId,error);//借款者历史记录情况
		List<UserAuditItem> uItems = UserAuditItem.queryUserAllAuditItem(bid.userId, bid.mark); // 用户正对产品上传的资料集合
		
		if(error.code < 0){
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		User user = User.currUser();
		boolean ipsEnable = Constants.IPS_ENABLE;
		
		String uuid = CaptchaUtil.getUUID(); // 防重复提交UUID
		boolean flag = false;
		
		if(StringUtils.isNotBlank(showBox)){
			showBox = Encrypt.decrypt3DES(showBox, bidId + Constants.ENCRYPTION_KEY);
			
			if(showBox.equals(Constants.SHOW_BOX))
				flag = true;
		}
		
		int status = Constants.INVEST_DETAIL;
		
		
		render(bid, flag, historySituationMap, uItems,user,ipsEnable, uuid, status);
	}
	
	/**
	 * 投标记录分页ajax方法
	 * @param pageNum
	 * @param pageSize
	 * @param bidId
	 */
	public static void viewBidInvestRecords(int pageNum, int pageSize,String bidIdSign){
		
		ErrorInfo error = new ErrorInfo();
	    int currPage = pageNum;
	    
		if(params.get("currPage")!=null) {
			currPage = Integer.parseInt(params.get("currPage"));
		}
		
		long bidId = Security.checkSign(bidIdSign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		PageBean<v_invest_records> pageBean = new PageBean<v_invest_records>();
		pageBean = Invest.queryBidInvestRecords(currPage, pageSize, bidId,error);
		
		if(error.code < 0){
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		render(pageBean);
		
	}
	
	
	
	/**
	 * 查询借款标的所有提问记录ajax分页方法
	 * @param pageNum
	 * @param pageSize
	 * @param bidId
	 */
	public static void viewBidAllQuestion(int pageNum, int pageSize, String bidIdSign) {

		ErrorInfo error = new ErrorInfo();
		
		long bidId = Security.checkSign(bidIdSign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(error.code < 0){
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}

		PageBean<BidQuestions> page = BidQuestions.queryQuestion(pageNum, pageSize, bidId, "", Constants.SEARCH_ALL, -1, error);
		
		if (null == page){
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		render(page);
	}
	
	/**
	 * 确认投标
	 * @param bidId
	 */
	@IpsAccountCheck
	@InactiveUserCheck
	@RealNameCheck
	public static void confirmInvest(String sign, String uuid){
		User user = User.currUser();
		
		if(null == user) 
			LoginAndRegisterAction.login();
		
		if(user.simulateLogin != null){
        	if(User.currUser().simulateLogin.equalsIgnoreCase(user.encrypt())){
            	flash.error("模拟登录不能进行该操作");
            	String url = request.headers.get("referer").value();
            	redirect(url);
            }else{
            	flash.error("模拟登录超时，请重新操作");
            	String url = request.headers.get("referer").value();
            	redirect(url);
            }
        }
		ErrorInfo error = new ErrorInfo();
		
		long bidId = Security.checkSign(sign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(bidId < 1){
			flash.error(error.msg); 

			invest(bidId, "");
		}
		
		/* 防重复提交 */
		if(!CaptchaUtil.checkUUID(uuid)){
			flash.error("请求已提交或请求超时!");
			
			invest(bidId, "");
		}
		
		String investAmountStr = params.get("investAmount");
		String dealpwd = params.get("dealpwd");
		
		if(StringUtils.isBlank(investAmountStr)){
			error.msg = "投标金额不能为空！";
			flash.error(error.msg);
			invest(bidId, "");
		}
		
		boolean b=investAmountStr.matches("^[1-9][0-9]*$");
    	if(!b){
    		error.msg = "对不起！只能输入正整数!";
    		flash.error(error.msg);
    		invest(bidId, "");
    	} 
    	
    	int investAmount = Integer.parseInt(investAmountStr);
		Invest.invest(user.id, bidId, investAmount, dealpwd, false, Constants.CLIENT_PC, error);
		
    	if (error.code == Constants.BALANCE_NOT_ENOUGH) {
			flash.put("code", error.code);
			flash.put("msg", error.msg);
			
			invest(bidId, "");
		}
    	
		if (error.code < 0) {
			flash.error(error.msg);
			invest(bidId, "");
		}
		
		Map<String, String> bid = Invest.bidMap(bidId, error);

		if (error.code < 0) {
			flash.error("对不起！系统繁忙！请您联系平台管理员！");
			invest(bidId, "");
		}

		double minInvestAmount = Double.parseDouble(bid.get("min_invest_amount") + "");
		double averageInvestAmount = Double.parseDouble(bid.get("average_invest_amount") + "");
		
		if (Constants.IPS_ENABLE) {
			if (error.code < 0) {
				flash.error(error.msg);
				invest(bidId, "");
			}
			
			if(minInvestAmount == 0){//认购模式
				investAmount = (int) (investAmount*averageInvestAmount);
			}
			
			//调用托管接口
			PaymentProxy.getInstance().invest(error, Constants.PC, t_bids.findById(bidId), user, investAmount);
			
			flash.error(error.msg);
			invest(bidId, "");
		}
		
		if(minInvestAmount == 0){//认购模式
			investAmount = (int) (investAmount*averageInvestAmount);
		}
		
		if(error.code > 0){
			flash.put("amount", NumberUtil.amountFormat(investAmount));
			String showBox = Encrypt.encrypt3DES(Constants.SHOW_BOX, bidId + Constants.ENCRYPTION_KEY);
			
			invest(bidId, showBox);
		}else{
			flash.error(error.msg);
			invest(bidId, "");
		}
	}
	
	/**
	 * 确认投标(页面底部投标按钮)
	 * @param bidId
	 */
	@IpsAccountCheck
	@InactiveUserCheck
	@RealNameCheck
	public static void confirmInvestBottom(String sign,String uuid){
		User user = User.currUser();
		
		if(null == user) 
			LoginAndRegisterAction.login();
		
		if(user.simulateLogin != null){
        	if(User.currUser().simulateLogin.equalsIgnoreCase(user.encrypt())){
            	flash.error("模拟登录不能进行该操作");
            	String url = request.headers.get("referer").value();
            	redirect(url);
            }else{
            	flash.error("模拟登录超时，请重新操作");
            	String url = request.headers.get("referer").value();
            	redirect(url);
            }
        }
		ErrorInfo error = new ErrorInfo();
		
		long bidId = Security.checkSign(sign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(bidId < 1){
			flash.error(error.msg); 

			invest(bidId, "");
		}
		
		/* 防重复提交 */
		if(!CaptchaUtil.checkUUID(uuid)){
			flash.error("请求已提交或请求超时!");
			
			invest(bidId, "");
		}
		
		String investAmountStr = params.get("investAmountBottom");
		String dealpwd = params.get("dealpwdBottom");
		
		if(StringUtils.isBlank(investAmountStr)){
			error.msg = "投标金额不能为空！";
			flash.error(error.msg);
			invest(bidId, "");
		}
		
		boolean b=investAmountStr.matches("^[1-9][0-9]*$");
    	if(!b){
    		error.msg = "对不起！只能输入正整数!";
    		flash.error(error.msg);
    		invest(bidId, "");
    	} 
    	
    	int investAmount = Integer.parseInt(investAmountStr);
    	
		Invest.invest(user.id, bidId, investAmount, dealpwd, false, Constants.CLIENT_PC, error);
		
    	if (error.code == Constants.BALANCE_NOT_ENOUGH) {
			flash.put("code", error.code);
			flash.put("msg", error.msg);
			
			invest(bidId, "");
		}
    	
		if (error.code < 0) {
			flash.error(error.msg);
			invest(bidId, "");
		}
		
		Map<String, String> bid = Invest.bidMap(bidId, error);

		if (error.code < 0) {
			flash.error("对不起！系统繁忙！请您联系平台管理员！");
			invest(bidId, "");
		}

		double minInvestAmount = Double.parseDouble(bid.get("min_invest_amount") + "");
		double averageInvestAmount = Double.parseDouble(bid.get("average_invest_amount") + "");
		
		if (Constants.IPS_ENABLE) {
			if (error.code < 0) {
				flash.error(error.msg);
				invest(bidId, "");
			}
			
			if(minInvestAmount == 0){//认购模式
				investAmount = (int) (investAmount*averageInvestAmount);
			}
			
			//调用托管接口
			PaymentProxy.getInstance().invest(error, Constants.PC, t_bids.findById(bidId), user, investAmount);
			
			flash.error(error.msg);
			invest(bidId, "");
		}
		
		if(minInvestAmount == 0){//认购模式
			investAmount = (int) (investAmount*averageInvestAmount);
		}
		
		if(error.code > 0){
			flash.put("amount", NumberUtil.amountFormat(investAmount));
			String showBox = Encrypt.encrypt3DES(Constants.SHOW_BOX, bidId + Constants.ENCRYPTION_KEY);
			
			invest(bidId, showBox);
		}else{
			flash.error(error.msg);
			invest(bidId, "");
		}
	}
	
	
	/**
	 * 收藏借款标
	 * @param bidId
	 */
	@IpsAccountCheck(true)
	@InactiveUserCheck(true)
	public static void collectBid(long bidId){
	    if(User.currUser().simulateLogin != null){
        	if(User.currUser().simulateLogin.equalsIgnoreCase(User.currUser().encrypt())){
            	flash.error("模拟登录不能进行该操作");
            	String url = request.headers.get("referer").value();
            	redirect(url);
            }else{
            	flash.error("模拟登录超时，请重新操作");
            	String url = request.headers.get("referer").value();
            	redirect(url);
            }
	      }
		
		ErrorInfo error = new ErrorInfo();
		User user = User.currUser();
		
		Bid.collectBid(user.id, bidId, error);
		
		JSONObject json = new JSONObject();
		json.put("error", error);
		renderJSON(json);
	}
	
	/**
	 * 查看(异步)
	 */
	@IpsAccountCheck(true)
	@InactiveUserCheck(true)
	public static void showitem(String mark, String signUserId){
		/* 解密userId */
		ErrorInfo error = new ErrorInfo();
		long userId = Security.checkSign(signUserId, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if(userId < 1){
			renderText(error.msg);
		}
		
		UserAuditItem item = new UserAuditItem();
		item.lazy = true;
		item.userId = userId;
		item.mark = mark;
		
		render(item);
	}
	
	
	/**
	 * 取消关注借款标
	 * @param attentionId
	 */
	public static void cancleBidAttention(Long attentionId){
		
		ErrorInfo error = new ErrorInfo();
		Invest.canaleBid(attentionId, error);
		
		JSONObject json = new JSONObject();
		
		json.put("error", error);
		renderJSON(json);
	}
}
