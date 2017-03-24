package controllers.supervisor.bidManager;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import models.t_bids;
import models.v_agencies;
import models.v_bids;
import models.v_user_info;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import payment.PaymentProxy;
import play.cache.Cache;
import utils.CaptchaUtil;
import utils.ErrorInfo;
import utils.ExcelUtils;
import utils.JsonDateValueProcessor;
import utils.JsonDoubleValueProcessor;
import utils.NumberUtil;
import utils.PageBean;
import utils.Security;
import bean.AgencyBid;
import business.Agency;
import business.Bid;
import business.Optimization;
import business.Product;
import business.Supervisor;
import business.User;
import business.Bid.Purpose;
import constants.Constants;
import controllers.supervisor.SupervisorController;

/**
 * 机构合作/机构标管理
 * @author bsr
 * @version 6.0
 * @created 2014-6-26 下午01:52:09
 */
public class BidAgencyAction extends SupervisorController{
	
	/**
	 * 合作结构标列表
	 */
	public static void agencyBidList(int isExport){
		ErrorInfo error = new ErrorInfo();
		
		/* 删除机构标的缓存信息 */
		String key = "agencyBid_" + session.getId();
		Cache.delete(key);
		
		PageBean<AgencyBid> pageBean = new PageBean<AgencyBid>();
		pageBean.page = Optimization.BidOZ.queryAgencyBids(isExport==Constants.IS_EXPORT?Constants.NO_PAGE:0, pageBean, error, BidPlatformAction.getParameter(pageBean, null));

		if(isExport == Constants.IS_EXPORT){
			
			List<AgencyBid> list = pageBean.page;

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
			jsonConfig.registerJsonValueProcessor(Double.class, new JsonDoubleValueProcessor("##,##0.00"));
			JSONArray arrList = JSONArray.fromObject(list, jsonConfig);
			
			for (Object obj : arrList) {
				JSONObject bid = (JSONObject)obj;
				
				String showPeriod = "";
				int period = bid.getInt("period");
				int period_unit = bid.getInt("period_unit");
				if(period_unit == -1){
					showPeriod = period + "[年 ]";
				}else if(period_unit == 1){
					showPeriod = period + "[日]";
				}else{		
					showPeriod = period + "[月]";
				}
				
				DecimalFormat df = new DecimalFormat("#0.0");
				double percent = 0.0;
				int productItem = bid.getInt("product_item_count");
				int userItem = bid.getInt("user_item_count_true");
				if(productItem == 0 || userItem / productItem >= 1){
					percent = 100.0;
				}else{
					percent = (userItem * 100.0 ) / productItem;
				}
				String auditStatus = df.format(percent) + "%";
				
				String productName = Product.queryProductNameByImage(bid.getString("small_image_filename"));
				String creditLevel = User.queryCreditLevelByImage(bid.getString("credit_level_image_filename"));
				
				bid.put("period", showPeriod);
				bid.put("small_image_filename", productName);
				bid.put("credit_level_image_filename", creditLevel);
				bid.put("apr", String.format("%.1f", bid.getDouble("apr")) + "%");
				
				bid.put("loan_schedule", String.format("%.1f", bid.getDouble("loan_schedule")) + "%");
				bid.put("audit_status", auditStatus);
			}
			
			File file = ExcelUtils.export("机构合作标列表",
			arrList,
			new String[] {
			"编号", "标题", "发布者", "信用等级", "借款标金额", "借款标类型", "年利率",
			"借款期限", "发布时间", "合作机构", "借款进度",
			"借款状态", "审核状态"},
			new String[] { "bid_no", "title", "user_name",
			"credit_level_image_filename", "amount", "small_image_filename", "apr",
			"period", "time","agency_name",
			"loan_schedule", "strStatus", "audit_status"});
			   
			renderBinary(file, "机构合作标列表.xls");
		}
		
		render(pageBean);
	}
	

	/**
	 * 发布合作机构标 页面跳转
	 */
	public static void addAgencyBid(){
		ErrorInfo error = new ErrorInfo();
		
		/* 得到所有借款用途  */
		List<Purpose> purpose = Purpose.queryLoanPurpose(error, true);
		
		/* 机构标产品 */
		Product product = Product.queryAgencyProduct(error);
		
		if(null == product){
			flash.error(error.msg);
			
			agencyBidList(0);
		}
		
		/* 机构列表 */
		List<Agency> agencys = Agency.queryAgencys(error);
		
		String key = "agencyBid_" + session.getId();
		Bid loanBid = (Bid) Cache.get(key);  // 获取用户输入的临时数据
		Cache.delete(key); // 删除缓存中的bid对象
		String uuid = CaptchaUtil.getUUID(); // 防重复提交UUID
		int once_repayment = Constants.ONCE_REPAYMENT;  //一次性还款方式

		render(purpose, product, agencys, uuid, loanBid, once_repayment);
	}
	
	/**
	 * 发布合作机构标
	 */
	public static void addingAgencyBid(Bid bid, long productId, String uuid){
		/* 有效表单验证  */
		checkAuthenticity(); 

		/* 将合作机构标信息放在cache中,如果错误带会到页面中 */
		Cache.set("agencyBid_" + session.getId(), bid);
		
		if(!CaptchaUtil.checkUUID(uuid)){
			flash.error("请求已提交或请求超时!");
			
			addAgencyBid();
		}
		
		if(bid.agencyId <= 0) { 
			flash.error("机构名称有误!"); 
			
			addAgencyBid();
		}
		
		String userName = params.get("userName");
		String signUserId = params.get("sign");
		
		if(StringUtils.isBlank(signUserId) && StringUtils.isBlank(userName)){
			flash.error("直接借款人有误!");
			
			addAgencyBid();
		}
		
		ErrorInfo error = new ErrorInfo();
		long userId = 0;
		
		if(StringUtils.isNotBlank(userName)){
			userId = User.queryIdByUserName(userName, error);
		}else{
			userId = Security.checkSign(signUserId, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		}
		
		if(userId < 1){
			flash.error(error.msg);
			
			addAgencyBid();
		}
		
		bid.createBid = true;
		bid.productId = productId;  // 填充产品对象
		bid.userId = userId;

		/* 非友好提示 */
		if( bid.user.id < 1 ||
			null == bid.product || 
			!bid.product.isUse || 
			!bid.product.isAgency 
		){
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR); 
		}
		
		if(!(bid.user.isEmailVerified || bid.user.isMobileVerified)){
			flash.error("借款人未激活!");
			
			addAgencyBid();
		}
		
		/* 需要填写基本资料 */
		if(!bid.user.isAddBaseInfo) {
			flash.error("借款人未填写基本资料!");
			
			addAgencyBid();
		}
		
		/* 秒还标未进行自动还款签约 */
		if (Constants.IPS_ENABLE && bid.product.loanType == Constants.S_REPAYMENT_BID && StringUtils.isBlank(bid.user.ipsRepayAuthNo)) {
			flash.error("直接借款人未自动还款签约!");
			
			addAgencyBid();
		}
		
		/* 发布借款 */
		t_bids tbid = new t_bids();
		
		//标的发布前校验， 及组装标的信息，不插入数据库
		bid.createBid(Constants.CLIENT_PC, tbid, error);
		if(error.code < 0){
			flash.error(error.msg);
			addAgencyBid();
		}
		
		//资金托管发布机构合作标的，业务逻辑在回调方法中处理
		if(Constants.IPS_ENABLE){
			
			PaymentProxy.getInstance().bidCreate(error, Constants.CLIENT_PC, tbid, bid);
			
			flash.error(error.msg);				
			addAgencyBid();
		}
		/* 发布借款 */
		bid.afterCreateBid(tbid, null, Constants.CLIENT_PC, Supervisor.currSupervisor().getId(), error);		
		if(error.code < 0) {
			flash.error(error.msg);
			
			addAgencyBid();
		}		
		flash.error(tbid.id > 0 ? "发布成功!" : error.msg);
		
		agencyBidList(0);
	}

	/**
	 * 检查数据
	 */
	@Deprecated
	public static boolean checkAgencyBid(Bid bid){
		if(bid.agencyId <= 0) { flash.error("机构名称有误!"); return true; }
		if(StringUtils.isBlank(bid.title) || bid.title.length() > 24){ flash.error("借款标题有误!"); return true; }
		int _amount = (int)bid.amount;
		if(bid.amount <= 0 || bid.amount != _amount || bid.amount < bid.product.minAmount || bid.amount > bid.product.maxAmount){flash.error("借款金额有误!"); return true; }
		if (bid.apr <= 0 || bid.apr > 100 || bid.apr < bid.product.minInterestRate || bid.apr > bid.product.maxInterestRate) { flash.error("年利率有误!"); return true; }
		if (bid.product.loanImageType == Constants.USER_UPLOAD && (StringUtils.isBlank(bid.imageFilename) || bid.imageFilename.contains(Constants.DEFAULT_IMAGE))) { flash.error("借款图片有误!"); return true; }
		if(bid.purpose.id < 0){ flash.error("借款用途有误!"); return true; }
		if(bid.repayment.id < 0){ flash.error("借款类型有误!"); return true; }
		if(bid.period <= 0){ flash.error("借款期限有误!"); return true; }
		switch (bid.periodUnit) {
		case Constants.YEAR:
			if(bid.period > Constants.YEAR_PERIOD_LIMIT){ flash.error("借款期限超过了" + Constants.YEAR_PERIOD_LIMIT + "年"); return true; }
			break;
		case Constants.MONTH:
			if(bid.period > Constants.YEAR_PERIOD_LIMIT * 12){ flash.error("借款期限超过了" + Constants.YEAR_PERIOD_LIMIT + "年"); return true; }
			break;
		case Constants.DAY:
			if(bid.period > Constants.YEAR_PERIOD_LIMIT * 12 * 30){ flash.error("借款期限超过了" + Constants.YEAR_PERIOD_LIMIT + "年"); return true; }
			if(bid.investPeriod > bid.period){ flash.error("天标满标期限不能大于借款期限 !"); return true; }
			break;
		default: flash.error("借款期限单位有误!"); return true;
		}
		
		if((bid.minInvestAmount > 0 && bid.averageInvestAmount > 0) || (bid.minInvestAmount <= 0 && bid.averageInvestAmount <= 0)){ flash.error("最低投标金额和平均招标金额有误!"); return true; }
		if(bid.averageInvestAmount > 0 && bid.amount % bid.averageInvestAmount != 0){ flash.error("平均招标金额有误!"); return true;}
		if(bid.investPeriod <= 0) { flash.error("投标期限有误!"); return true; }
		if(StringUtils.isBlank(bid.description)) { flash.error("借款描述有误!"); return true; }
		if (bid.minInvestAmount > 0 && (bid.minInvestAmount < bid.product.minInvestAmount)){ flash.error("最低投标金额不能小于产品最低投标金额!"); return true; }
		if (bid.averageInvestAmount > 0 && (bid.amount / bid.averageInvestAmount > bid.product.maxCopies)){ flash.error("平均投标份数不能大于产品的最大份数限制 !"); return true; }
	
		return false;
	}
	
	/**
	 * 异步选择用户
	 */
	public static void selectUsersInit(String currPage, String pageSize, String keyword) {
		ErrorInfo error = new ErrorInfo();
		PageBean<v_user_info> pageBean = User.queryLoanableUser(keyword, currPage, Constants.PAGE_SIZE_EIGHT+"", error);
		
		if(error.code < 0) 
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		
		render(pageBean);
	}
	
	/**
	 * 合作结构列表
	 */
	public static void agencyList() {
		ErrorInfo error = new ErrorInfo();
		
		String currPage = params.get("currPage"); // 当前页
		String pageSize = params.get("pageSize"); // 分页行数
		String condition = params.get("condition"); // 条件
		String keyword = params.get("keyword"); // 关键词
		
		PageBean<v_agencies> pageBean = new PageBean<v_agencies>();
		pageBean.currPage = NumberUtil.isNumericInt(currPage)? Integer.parseInt(currPage): 1;
		pageBean.pageSize = NumberUtil.isNumericInt(pageSize)? Integer.parseInt(pageSize): 10;
		pageBean.page = Agency.queryAgencies(pageBean, error, condition, keyword);

		if (null == pageBean.page) render(Constants.ERROR_PAGE_PATH_SUPERVISOR);  

		render(pageBean);
	}
	
	/**
	 * 启用合作机构
	 */
	public static void enanleAgency(long aid){
		ErrorInfo error = new ErrorInfo();
	    Agency.editStatus(aid, Constants.ENABLE, error);
		flash.error(error.msg);
	    
		agencyList();
	}
	
	/**
	 * 暂停合作机构
	 */
	public static void notEnanleAgency(long aid){
		ErrorInfo error = new ErrorInfo();
		Agency.editStatus(aid, Constants.NOT_ENABLE, error);
		flash.error(error.msg);
	    
		agencyList();
	}
	
	/**
	 * 添加合作机构 页面跳转
	 */
	public static void addAgency(){
		//ErrorInfo error = new ErrorInfo();
		
		/* 信用等级名称 */
		//List<CreditLevel> creditLevels = CreditLevel.queryCreditName(error);
		
		render();
	}
	
	/**
	 * 添加合作机构
	 */
	public static void addingAgency(Agency agency){

		if( StringUtils.isBlank(agency.name) ||
			Agency.checkName(agency.name) ||
			Constants.AGENCY_NAME_REPEAT.equals(agency.name) ||
			StringUtils.isBlank(agency.introduction) ||
			StringUtils.isBlank(agency.id_number) ||
			Constants.SEAL_NAME_REPEAT.equals(agency.id_number) ||
			Agency.checkIdNumber(agency.id_number) ||
			StringUtils.isBlank(agency.imageFilenames) ||
			agency.creditLevel <= 0 
		  ){
			flash.error("数据有误!");
			
			agencyList();
		}
		
		ErrorInfo error = new ErrorInfo();
		agency.createAgency(error);
		flash.error(error.msg);
		
		agencyList();
	}
	
	/**
	 * 检查名称是否唯一
	 */
	public static void checkName(String name){
		renderJSON(Agency.checkName(name));
	}
	
	/**
	 * 检查营业执照号是否唯一
	 */
	public static void checkIdNumber(String idNumber){
	    renderJSON(Agency.checkIdNumber(idNumber));
	}
	
	/**
	 * 合作机构标详情(审核操作等,需要去借款标管理中进行)
	 */
	public static void detail(long bidid, int flag) { 
		Bid bid = new Bid();
		bid.bidDetail = true;
		bid.upNextFlag = flag;
		bid.id = bidid;
		
		render(bid, flag);
	}
	
	/**
	 * 合作机构详情(对应的标列表)
	 */
	public static void agencyDetail(long agencyId){
		ErrorInfo error = new ErrorInfo();
		PageBean<v_bids> pageBean = new PageBean<v_bids>();
		pageBean.page = Bid.queryAgencyBid(pageBean, agencyId, error, BidPlatformAction.getParameter(pageBean, null));

		if (null == pageBean.page) render(Constants.ERROR_PAGE_PATH_SUPERVISOR);  

		render(pageBean, agencyId);
	}
}
