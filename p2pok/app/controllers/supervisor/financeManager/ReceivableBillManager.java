package controllers.supervisor.financeManager;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import payment.PaymentProxy;
import models.t_bills;
import models.v_bid_repayment;
import models.v_bill_detail;
import models.v_bill_has_received;
import models.v_bill_receiving;
import models.v_bill_receiving_overdue;
import models.v_bill_receviable_statistical;
import models.v_bill_repayment_record;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import utils.ErrorInfo;
import utils.ExcelUtils;
import utils.JsonDateValueProcessor;
import utils.JsonDoubleValueProcessor;
import utils.PageBean;
import utils.Security;
import business.BackstageSet;
import business.Bid;
import business.Bill;
import business.Supervisor;
import constants.Constants;
import constants.IPSConstants.CompensateType;
import constants.IPSConstants.IPSDealStatus;
import constants.IPSConstants.RegisterGuarantorType;
import controllers.supervisor.SupervisorController;
import controllers.supervisor.bidManager.BidPlatformAction;

/**
 * 
 * 类名:ReceivableBillManager
 * 功能:应收账单管理
 */

public class ReceivableBillManager extends SupervisorController {

	/**
	 * 待收款借款账单列表
	 */
	public static void toReceiveBills(){
		String yearStr = params.get("yearStr");
		String monthStr = params.get("monthStr");
		String typeStr = params.get("typeStr");
		String key = params.get("key");
		String orderType = params.get("orderType");
		String currPageStr = params.get("currPageStr");
		String pageSizeStr = params.get("pageSizeStr");
		
		ErrorInfo error = new ErrorInfo();
		Supervisor supervisor = Supervisor.currSupervisor();
		
		PageBean<v_bill_receiving> page = Bill.queryBillReceiving(supervisor.id, yearStr, monthStr,
				typeStr, key, orderType, currPageStr, pageSizeStr, error);
		
		if(page == null) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		
		render(page);
	}

	/**
	 * 逾期账单列表
	 */
	public static void overdueBills(){
		String yearStr = params.get("yearStr");
		String monthStr = params.get("monthStr");
		String typeStr = params.get("typeStr");
		String key = params.get("key");
		String orderType = params.get("orderType");
		String currPageStr = params.get("currPageStr");
		String pageSizeStr = params.get("pageSizeStr");
		
		ErrorInfo error = new ErrorInfo();
		Supervisor supervisor = Supervisor.currSupervisor();
		
		PageBean<v_bill_receiving_overdue> page = Bill.queryBillReceivingOverdue(supervisor.id, yearStr, monthStr,
				typeStr, key, orderType, currPageStr, pageSizeStr, error);
		
		if(page == null) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		
		render(page);
	}

	/**
	 * 已收款借款账单列表
	 */
	public static void receivedBills(){
		String yearStr = params.get("yearStr");
		String monthStr = params.get("monthStr");
		String typeStr = params.get("typeStr");
		String key = params.get("key");
		String orderType = params.get("orderType");
		String currPageStr = params.get("currPageStr");
		String pageSizeStr = params.get("pageSizeStr");
		
		ErrorInfo error = new ErrorInfo();
		Supervisor supervisor = Supervisor.currSupervisor();
		
		PageBean<v_bill_has_received> page = Bill.queryBillHasReceived(supervisor.id, yearStr, monthStr,
				typeStr, key, orderType, currPageStr, pageSizeStr, error);
		
		if(page == null) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		
		render(page);
	}

	/**
	 * 已完成借款标列表
	 */
	public static void repaymentList(){
		ErrorInfo error = new ErrorInfo();
		
		PageBean<v_bid_repayment> pageBean = new PageBean<v_bid_repayment>();
		pageBean.page = Bid.queryBidRepayment(0, pageBean, 0, error, BidPlatformAction.getParameter(pageBean, null));

		if (null == pageBean.page) 
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR); 
		
		render(pageBean);
	}

	/**
	 * 应收款借款账单统计表
	 */
	public static void receivableBills(int isExport){
		String yearStr = params.get("yearStr");
		String monthStr = params.get("monthStr");
		String orderType = params.get("orderType");
		String currPageStr = params.get("currPageStr");
		String pageSizeStr = params.get("pageSizeStr");
		
		ErrorInfo error = new ErrorInfo();
		Supervisor supervisor = Supervisor.currSupervisor();
		
		PageBean<v_bill_receviable_statistical> page = Bill.queryBillReceivedStatical(isExport==Constants.IS_EXPORT?Constants.NO_PAGE:0,supervisor.id, yearStr, monthStr,
				 orderType, currPageStr, pageSizeStr, error);
		
		if(page == null) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		
		if(isExport == Constants.IS_EXPORT){
			
			List<v_bill_receviable_statistical> list = page.page;
			 
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
			jsonConfig.registerJsonValueProcessor(Double.class, new JsonDoubleValueProcessor("##,##0.00"));
			JSONArray arrList = JSONArray.fromObject(list, jsonConfig);
			
			for (Object obj : arrList) {
				JSONObject bill = (JSONObject)obj;			

				bill.put("bills_timely_completion_rate", String.format("%.1f", bill.getDouble("bills_timely_completion_rate")) + "%");
				bill.put("bills_overdue_rate", String.format("%.1f", bill.getDouble("bills_overdue_rate")) + "%");
				bill.put("bills_completed_rate", String.format("%.1f", bill.getDouble("bills_completed_rate")) + "%");
				bill.put("uncollected_rate", String.format("%.1f", bill.getDouble("uncollected_rate")) + "%");
			}
			
			File file = ExcelUtils.export("应收款借款账单统计表",
			arrList,
			new String[] {
			"年", "月", "应收账单数", "应收金额", "关联借款标总额",
			"实际已收账单数", "实际已收金额", "应收账单按时完成率", "逾期账单数",
			"逾期占比", "总应收完成率","未收逾期数量", "未收金额", "未收逾期占比"},
			new String[] {"year", "month", "bill_accounts",
			"amounts_receivable", "bids_amount", 
			"bills_received", "amount_received",
			"bills_timely_completion_rate", "overdue_counts",
			"bills_overdue_rate", "bills_completed_rate",
			"bills_overdue_noreceive", "uncollected_amount", "uncollected_rate"});
			   
			renderBinary(file, "应收款借款账单统计表.xls");
		}
		
		render(page);
	}

	/**
	 * 账单详情
	 */
	public static void billDetail(String billId, int type) { 
		int currPage = 1;
		String curPage = params.get("currPage");
		
		if(curPage != null) {
			currPage = Integer.parseInt(curPage);
		}
		
		ErrorInfo error = new ErrorInfo();
		
		long id = Security.checkSign(billId, Constants.BILL_ID_SIGN, 3600, error);
		
		v_bill_detail billDetail = Bill.queryBillDetails(id, error);
		PageBean<v_bill_repayment_record> page = Bill.queryBillReceivables(billDetail.bid_id, currPage, 0, error);
		BackstageSet backSet = BackstageSet.getCurrentBackstageSet();
		
		render(billDetail, page, backSet, type);
	}

	/**
	 * 借款标详情
	 */
	public static void bidDetail(long bidid, int type, int flag) { 
		Bid bid = new Bid();
		bid.bidDetail = true;
		bid.upNextFlag = flag;
		bid.id = bidid;
		
		render(bid, type, flag);
	}

	//线下收款
	public static void offlineReceive(String billId, int type){
		//国付宝支持线下收款
		if(!Constants.IS_OFFLINERECEIVE) {
			overdueBills();
		}
		
		ErrorInfo error = new ErrorInfo();
		long id = Security.checkSign(billId, Constants.BILL_ID_SIGN, 3600, error);
		
		if (error.code < 0) {
			flash.error(error.msg);
			overdueBills();
		}
		
		Bill bill = new Bill();
		bill.id = id;	
		v_bill_detail billDetail = Bill.queryBillDetails(id, error);

		Map<String,Object> maps = PaymentProxy.getInstance().queryAmountByMerchant(error, Constants.PC, null);
		Double merBalance = Double.valueOf(maps.get("AvlBal")+"");
		if(billDetail.current_pay_amount>merBalance){
			error.msg="代还失败，平台余额不足！";
			flash.error(error.msg);
			overdueBills();
			
		}
		
		String billSql=" bid_id = ? and status = ? order by bid_id asc";
 		t_bills tbill=t_bills.find(billSql, bill.bidId ,-1).first();
 		if(bill.periods!=tbill.periods){
 			flash.error("代还失败,请选择最近一期还款！");
 			if (type == RegisterGuarantorType.OFFLINE_REPAYMENT) {
				toReceiveBills();
			} else {
				overdueBills();
			}
 		}
		
		if(bill.ipsStatus == IPSDealStatus.OFFLINEREPAYMENT_HANDING){  //线下收款处理中
			flash.error("线下收款处理中，请勿重复操作！");
			
			if (type == RegisterGuarantorType.OFFLINE_REPAYMENT) {
				toReceiveBills();
			} else {
				overdueBills();
			}
		}
		
		if(bill.ipsStatus == IPSDealStatus.REPAYMENT_HANDING){  //借款人还款中
			flash.error("借款人还款中，不能线下收款");
			
			if (type == RegisterGuarantorType.OFFLINE_REPAYMENT) {
				toReceiveBills();
			} else {
				overdueBills();
			}
		}
		
		if(bill.ipsStatus == IPSDealStatus.COMPENSATE_HANDING){  //本金垫付处理中
			flash.error("本金垫付处理中，不能线下收款");
			
			if (type == RegisterGuarantorType.OFFLINE_REPAYMENT) {
				toReceiveBills();
			} else {
				overdueBills();
			}
		}
		
		//本金垫付后线下收款
		if (bill.status == Constants.ADVANCE_PRINCIIPAL_REPAYMENT) {
			bill.offlineReceive(error);
			
			flash.error(error.msg);
			
			if (type == RegisterGuarantorType.OFFLINE_REPAYMENT) {
				toReceiveBills();
			} else {
				overdueBills();
			}
		}
				
		if (Constants.IPS_ENABLE) {
			
			//线下收款
			PaymentProxy.getInstance().offlineRepayment(error, Constants.PC, id, CompensateType.OFFLINE_REPAYMENT, Supervisor.currSupervisor().getId());
	
			flash.error(error.msg);
			
			if (type == RegisterGuarantorType.OFFLINE_REPAYMENT) {
				toReceiveBills();
			} else {
				overdueBills();
			}
			return;
			
		} else {
			bill.offlineCollection(Supervisor.currSupervisor().id, error);
		}
		
		flash.error(error.msg);
		
		if (type == RegisterGuarantorType.OFFLINE_REPAYMENT) {
			toReceiveBills();
		} else {
			overdueBills();
		}
	}
	
}
