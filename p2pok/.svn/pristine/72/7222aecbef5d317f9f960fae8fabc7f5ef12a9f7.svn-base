package controllers.supervisor.financeManager;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import models.v_bid_release_funds;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import payment.PaymentProxy;
import play.mvc.With;
import utils.Arith;
import utils.CaptchaUtil;
import utils.ErrorInfo;
import utils.ExcelUtils;
import utils.JsonDateValueProcessor;
import utils.JsonDoubleValueProcessor;
import utils.PageBean;
import utils.Security;
import annotation.SubmitCheck;
import annotation.SubmitOnly;
import business.Bid;
import business.Product;
import business.Supervisor;
import business.User;
import business.UserBankAccounts;
import constants.Constants;
import constants.IPSConstants.IPSDealStatus;
import controllers.MaliceFalsifyCheck;
import controllers.SubmitRepeat;
import controllers.supervisor.SupervisorController;
import controllers.supervisor.bidManager.BidPlatformAction;

/**
 * 放款管理
 * 
 * @author bsr
 * @version 6.0
 * @created 2014-5-28 上午10:13:37
 */
@With({MaliceFalsifyCheck.class, SubmitRepeat.class})
public class LoanManager extends SupervisorController {
	
	/**
	 * 等待放款
	 */
	@SubmitCheck
	public static void readyReleaseList() {
		ErrorInfo error = new ErrorInfo();
		PageBean<v_bid_release_funds> pageBean = new PageBean<v_bid_release_funds>();
		pageBean.page = Bid.queryReleaseFunds(0, pageBean, Constants.BID_EAIT_LOAN, error, BidPlatformAction.getParameter(pageBean, null));

		if (null == pageBean.page) 
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);  
		
		boolean ipsEnable = constants.Constants.IPS_ENABLE;
		
		render(pageBean, ipsEnable);
	}

	/**
	 * 已放款
	 */
	public static void alreadyReleaseList(int isExport) {
		ErrorInfo error = new ErrorInfo();
		PageBean<v_bid_release_funds> pageBean = new PageBean<v_bid_release_funds>();
		pageBean.page = Bid.queryReleaseFunds(isExport==Constants.IS_EXPORT?Constants.NO_PAGE:0, pageBean, Constants.BID_RELEASED, error, BidPlatformAction.getParameter(pageBean, null));

		if (null == pageBean.page){ 
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);  
		}
		
		if(isExport == Constants.IS_EXPORT){
			
			List<v_bid_release_funds> list = pageBean.page;
			 
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
			jsonConfig.registerJsonValueProcessor(Double.class, new JsonDoubleValueProcessor("##,##0.00"));
			JSONArray arrList = JSONArray.fromObject(list, jsonConfig);
			
			for (Object obj : arrList) {
				JSONObject bid = (JSONObject)obj;			

				String creditLevel = User.queryCreditLevelByImage(bid.getString("credit_level_image_filename"));
				String bidType = Product.queryProductNameByImage((bid.getString("small_image_filename")));
				
				bid.put("credit_level_image_filename", creditLevel);
				bid.put("small_image_filename", bidType);
				bid.put("apr", String.format("%.1f", bid.getDouble("apr")));
				bid.put("status", bid.getInt("status")==4?"已放款":"数据有误");
			}
			
			File file = ExcelUtils.export("已放款借款标管理",
			arrList,
			new String[] {
			"编号", "标题", "借款人", "信用等级", "借款标类型",
			"借款金额", "年利率", "申请时间", "满标时间", "必审科目数", "已审科目数",
			"放款时间", "审核人", "状态"},
			new String[] {"bid_no", "title", "user_name",
			"credit_level_image_filename", "small_image_filename", 
			"amount", "apr",
			"time", "real_invest_expire_time",
			"product_item_count", "user_item_count_true",
			"audit_time", "supervisor_name", "status"});
			   
			renderBinary(file, "已放款借款标.xls");
		}

		render(pageBean);
	}
	
	/**
	 * 用户账户信息
	 */
	public static void userBank(long id, int bankId){
		UserBankAccounts bank = null;
		List<UserBankAccounts> banks = null;
		
		if(bankId != 0) {
			bank = new UserBankAccounts();
			bank.id = bankId;
		}
		
		Bid bid = new Bid();
		bid.auditBid = true;
		bid.id = id;
		
		if(bid.status == Constants.BID_EAIT_LOAN)
			banks = UserBankAccounts.queryUserAllBankAccount(bid.userId);
		
		render(bank, banks, bid);
	}
	
	/**
	 * 放款
	 */
	
	public static void releaseAudit(String sign, String uuidRepeat) {
		/* 解密BidId */
		ErrorInfo error = new ErrorInfo();
		long bidId = Security.checkSign(sign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		//获取分批放款百分比   add by xd.wang
		String Level = params.get("releaseLevel").trim();
		double  releaseLevel = 1d;
		if(StringUtils.isNotEmpty(Level) && Long.parseLong(Level)<=100) {
			releaseLevel =Arith.mul(Double.parseDouble(Level),0.01) ;
		}
		
		if(bidId < 1){
			flash.error(error.msg); 

			readyReleaseList();
		}
		/* 防重复提交 */
		if(!StringUtils.isBlank(uuidRepeat)){
			if(!CaptchaUtil.checkUUID(uuidRepeat)){
				error.msg ="请求已提交或请求超时!";
				error.code = -2;
				renderJSON(error);
				//readyReleaseList(error);
			}
		}
		
		
		Bid bid = new Bid();
		bid.id = bidId;
		if(Arith.add(bid.releaseLevel, releaseLevel) > 1){
			error.msg ="分批放款比例有误!";
			error.code = -3;
			renderJSON(error);
		}
		bid.setReleaseLevel(releaseLevel);
		if(bid.ipsStatus == IPSDealStatus.LOAN_HANDING){  //放款处理中
			error.msg = "放款处理中，请勿重复操作！";
			
			readyReleaseList();
		}
		
		bid.allocationSupervisorId = Supervisor.currSupervisor().id; // 审核人
		
		if (Constants.IPS_ENABLE) {
			if (error.code < 0) {
				flash.error(error.msg); 
				
				readyReleaseList();
			}
			//满标复审调佣托管接口, 处理业务逻辑在托管实现类回调方法中
			PaymentProxy.getInstance().bidAuditSucc(error, Constants.PC, bid);
			
			flash.error(error.msg);			
			readyReleaseList();
		}
		//处理普通网关逻辑
		bid.eaitLoanToRepayment(error);
		flash.error(error.msg); 

		readyReleaseList();
	}
	
	/**
	 * 放款标记
	 */
	public static void releaseSign(String sign){
		if(Constants.IPS_ENABLE){
			flash.error("资金托管模式不允放款标记!"); 
			readyReleaseList();
		}
		/* 解密BidId */
		ErrorInfo error = new ErrorInfo();
		long bidId = Security.checkSign(sign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if(bidId < 1){
			flash.error(error.msg); 
			
			readyReleaseList();
		}
		
		Bid bid = new Bid();
		bid.auditBid = true;
		bid.id = bidId;
		bid.isReleaseSign = true;
		bid.allocationSupervisorId = Supervisor.currSupervisor().id; // 审核人
		bid.releaseSign(error);
		
		flash.error(error.msg); 
		
		readyReleaseList();
	}
	
	/**
	 * 详情
	 */
	public static void detail(long bidid, int type) { 
		
		if(0 == bidid)  render();
		
		Bid bid = new Bid();
		bid.bidDetail = true;
		bid.upNextFlag = Constants.BID_DFK; // 这里至为4和5是等价的
		bid.id = bidid;
		
		render(bid, type);
	}
}
