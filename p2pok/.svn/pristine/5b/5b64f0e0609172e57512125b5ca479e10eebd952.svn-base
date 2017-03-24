package controllers.front.home;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import jobs.CheckBillJob;

import models.t_bills;
import models.t_content_advertisements;
import models.t_content_news;
import models.t_content_news_types;
import models.t_invests;
import models.t_users;
import models.v_bill_board;
import models.v_front_all_bids;
import models.v_front_all_debts;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Play;
import utils.Arith;
import utils.ErrorInfo;
import utils.PageBean;
import business.Ads;
import business.AdsEnsure;
import business.AdsPartner;
import business.AuditItem;
import business.BackstageSet;
import business.Bid;
import business.Bill;
import business.CreditLevel;
import business.Debt;
import business.Invest;
import business.News;
import business.NewsType;
import business.Product;
import business.Bid.Repayment;

import com.shove.Convert;

import constants.Constants;
import constants.OptionKeys;
import controllers.BaseController;

/**
 * 
 * @author liuwenhui
 *
 */
public class HomeAction extends BaseController{
	
	public static void testJob(){
		Bill.checkBill();
		renderHtml("成功");
	}
	

	/*网站首页*/
	public static void home(String login){
		boolean registerFlag="registerDirect".equals(login);
		boolean logoutNotice="logoutNotice".equals(login);
		if(registerFlag||logoutNotice){
			login=null;
		}
		ErrorInfo error = new ErrorInfo();
				
		List<t_content_advertisements> homeAds = Ads.queryAdsByLocation(Constants.HOME_PAGE_PC, error); // 广告条
		List<v_front_all_bids> bidList = Invest.queryBids();//首页最新五个借款标
		List<v_front_all_debts> debtList = Debt.queryDebt(error);//首页最新五个借款标
		
		List<v_front_all_bids> agencyBids = Invest.queryAgencyBids();//机构借款标
		
		List<v_bill_board> investBillboard = Invest.investBillboard();//投资风云榜
		//债券市场动态    modify by xd.wang
		List<t_content_news> marketDynamics = News.queryNewForFront(8l, 5, error) ;
		
		List<t_content_news> investSkills = News.queryNewForFront(10l, 5, error) ;//首页借款技巧
		
		List<t_content_news> loanSkills = News.queryNewForFront(11l, 5, error) ;//首页投资技巧
		
		List<t_content_news> news = News.queryNewForFront(7l, 5, error) ;//首页官方公告
		
		List<Bid> bids = Bid.queryAdvertisement(error); // 最新投资资讯
		
		List<Map<String,String>> maps = Invest.queryNearlyInvest(error);
		
		List<AdsEnsure> adsEnsure = AdsEnsure.queryEnsureForFront(error); //四大安全保障
		
		List<AdsPartner>  adsPartner = AdsPartner.qureyPartnerForFront(error);//合作伙伴
		
		List<NewsType> types = NewsType.queryChildTypes(1, error);
	
		long userCount = 0;
		try {
			userCount = t_users.count();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		double investAmount =0;
		try {
			investAmount = t_invests.find(" select sum(amount) from t_invests").first();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		double  interest=0;
		try{
			interest = t_bills.find("select sum(repayment_interest) from t_bills ").first();
		}catch(Exception e){
			
		}
		
		
		render(homeAds, bidList,debtList,agencyBids,investBillboard,marketDynamics,investSkills,loanSkills,news, 
				bids, adsEnsure, adsPartner, types ,maps,login,userCount,investAmount,interest,registerFlag,logoutNotice);
	}
	
	public static void banner(){
		//ErrorInfo error = new ErrorInfo();
		//List<t_content_advertisements> homeAds = Ads.queryAdsByLocation(Constants.HOME_PAGE, error); // 广告条
		
		//renderJSON(homeAds);
	}
	
	/**
	 * 财富工具箱
	 */
	public static void wealthToolkit(int key){
		ErrorInfo error = new ErrorInfo();
		List<Product> products = Product.queryProductNames(true, error);
		
		List<CreditLevel> creditLevels = CreditLevel.queryAllCreditLevels(error);
		
		render(key, products, creditLevels);
	}
	
	/**
	 * 信用计算器
	 */
	public static void wealthToolkitCreditCalculator(){
		ErrorInfo error = new ErrorInfo();
		
		List<AuditItem> auditItems = AuditItem.queryAuditItems(error);
		
		String value = OptionKeys.getvalue(OptionKeys.CREDIT_LIMIT, error); // 得到积分对应的借款额度值
		double amountKey = StringUtils.isBlank(value) ? 0 : Double.parseDouble(value);
		
		render(auditItems, amountKey);
	}
	
	/**
	 * 还款计算器
	 */
	public static void wealthToolkitRepaymentCalculator(){
		List<Repayment> rtypes = Repayment.queryRepaymentType(null, new ErrorInfo()); // 还款类型
		
		render(rtypes);
	}
	
	/**
	 * 还款明细(异步)
	 */
	public static void repaymentCalculate(double amount, double apr, int period, int periodUnit, int repaymentType){
		List<Map<String, Object>> payList = null;
		
		payList = Bill.repaymentCalculate(amount, apr, period, periodUnit, repaymentType);
		
		render(payList);
	}
	
	/**
	 * 净值计算器
	 */
	public static void wealthToolkitNetValueCalculator(){
		ErrorInfo error = new ErrorInfo();
		
		double bailScale = Product.queryNetValueBailScale(error); // 得到净值产品的保证金比例
		
		render(bailScale);
	}
	
	/**
	 * 利率计算器
	 */
	public static void wealthToolkitAPRCalculator(){
		ErrorInfo error = new ErrorInfo();
		
		List<Repayment> rtypes = Repayment.queryRepaymentType(null, error); // 还款类型
		
		String value = OptionKeys.getvalue(OptionKeys.CREDIT_LIMIT, error); // 得到积分对应的借款额度值
		double serviceFee = StringUtils.isBlank(value) ? 0 : Double.parseDouble(value);

		render(rtypes, serviceFee);
	}
	
	/**
	 * 利率计算器,计算年华收益、总利益(异步)
	 */
	public static void aprCalculator(double amount, double apr,int repaymentType,double award,int rperiod){
		ErrorInfo error = new ErrorInfo();
		DecimalFormat df = new DecimalFormat("#.00");
		
		double managementRate = BackstageSet.getCurrentBackstageSet().investmentFee / 100;//系统管理费费率
		double earning = 0;
		
		if(repaymentType == 1){/* 按月还款、等额本息 */
			double monRate = apr / 12;// 月利率
			int monTime = rperiod;
			double val1 = amount * monRate * Math.pow((1 + monRate), monTime);
			double val2 = Math.pow((1 + monRate), monTime) - 1;
			double monRepay = val1 / val2;// 每月偿还金额
			
			/**
			 * 年化收益
			 */
			 earning = Arith.excelRate((amount - award),
					Double.parseDouble(df.format(monRepay)), monTime, 200, 15)*12*100;
			 earning = Double.parseDouble(df.format(earning)+"");
		}
		
		if(repaymentType == 2 || repaymentType == 3){ /* 按月付息、一次还款   */
			double monRate = apr / 12;// 月利率
			int monTime = rperiod;// * 12;借款期限填月
			double borrowSum = Double.parseDouble(df.format(amount));
			double monRepay = Double.parseDouble(df.format(borrowSum * monRate));// 每月偿还金额
			double allSum = Double.parseDouble(df.format((monRepay * monTime)))
					+ borrowSum;// 还款本息总额
			 earning = Arith.rateTotal(allSum,
					(borrowSum - award), monTime)*100;
			 earning = Double.parseDouble(df.format(earning)+"");
		}
		
		
		JSONObject obj = new JSONObject();
		obj.put("managementRate", managementRate < 0 ? 0 : managementRate); 
		obj.put("earning", earning); 
		
		renderJSON(obj);
	}
	
	/**
	 * 服务手续费
	 */
	public static void wealthToolkitServiceFee(){
		ErrorInfo error = new ErrorInfo();
		String content = News.queryContent(-1011L, error);
		flash.error(error.msg);
		
		renderText(content);
	}
	
	/**
	 * 超额借款
	 */
	public static void wealthToolkitOverLoad(){
		ErrorInfo error = new ErrorInfo();
		
		List<AuditItem> auditItems = AuditItem.queryAuditItems(error);
		
		String value = OptionKeys.getvalue(OptionKeys.CREDIT_LIMIT, error); // 得到积分对应的借款额度值
		double amountKey = StringUtils.isBlank(value) ? 0 : Double.parseDouble(value);
		
		render(auditItems, amountKey);
	}
	
	/**
	 * 新手入门
	 */
	public static void getStart(int id){
		ErrorInfo error = new ErrorInfo();
		
		String content = News.queryContentById(id, error);
		
		List<Product> products = Product.queryProductNames(true, error);
		
		List<CreditLevel> creditLevels = CreditLevel.queryAllCreditLevels(error);
		
		List<t_content_news_types> newsTypes = NewsType.queryNewsTypeByPid(id);
		t_content_news_types news = t_content_news_types.findById((long)id);
		render(content, products, creditLevels, id, newsTypes,news);              
	}
	
	/**
	 * 关于我们
	 */
	public static void aboutUs(int id){
		ErrorInfo error = new ErrorInfo();

		List<Product> products = Product.queryProductNames(true, error);
		List<CreditLevel> creditLevels = CreditLevel.queryAllCreditLevels(error);
		Object [] investData =  News.queryInvestDataSum();
		
		NewsType parent = new NewsType();
		parent.id = 3;
		List<NewsType> types = NewsType.queryChildTypes(3, error);
		List<String> contentList = News.queryContentList(id, error);
		t_content_news_types news = t_content_news_types.findById((long)id);
		render(contentList, investData, products, creditLevels, parent, types, id,news);
	}
	
	/**
	 * 投资风云榜（更多）
	 */
	public static void moreInvest(int currPage) {
		ErrorInfo error = new ErrorInfo();
		PageBean<v_bill_board> page = Invest.investBillboards(currPage, error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_FRONT);
		}
		
		render(page);
	}
	
	/**
	 * 招贤纳士
	 */
	public static void careers(){

	}
	
	/**
	 * 管理团队
	 */
	public static void managementTeam(){

	}
	
	/**
	 *专家顾问
	 */
	public static void expertAdvisor(){

	}
	
	public static void soolcenter(){
		render("/front/wealthinfomation/WealthInfomationAction/soolcenter.html");
	}
	public static void safeguardhelp(){
		render("/front/principal/PrincipalGuaranteeAction/safeguardhelp.html");
	}
	public static void safeguardmoney(){
		render("/front/principal/PrincipalGuaranteeAction/safeguardmoney.html");
	}
	/**
	 * 禁止收录
	 */
	public static void robots(){
		boolean is_robot = Convert.strToBoolean(Play.configuration.getProperty("is.robots"), true);
		String path = Play.configuration.getProperty("trust.funds.path") + "/robots.txt";
    	InputStream is = null;
		try {
			is = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(!is_robot){			
			renderBinary(is);
		}else{
			renderText("百度收录已开启");
		}
	}
}
