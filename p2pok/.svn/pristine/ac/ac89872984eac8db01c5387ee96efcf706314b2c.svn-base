package controllers.supervisor.billCollectionManager;

import java.io.File;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import constants.Constants;
import controllers.supervisor.SupervisorController;
import models.v_supervisors;
import models.v_user_loan_info_bill_d;
import models.v_user_loan_user_unassigned;
import business.Bid;
import business.Product;
import business.Supervisor;
import business.User;
import utils.ErrorInfo;
import utils.ExcelUtils;
import utils.JsonDateValueProcessor;
import utils.JsonDoubleValueProcessor;
import utils.PageBean;
import utils.Security;

/**
 * 
 * 类名:ToAssignLoanUsers
 * 功能:待分配借款会员列表
 */

public class ToAssignLoanUsers extends SupervisorController {

	/**
	 * 借款会员分配--待分配借款会员列表
	 */
	public static void toAssignUsers(int isExport) {
		
		ErrorInfo error = new ErrorInfo();
		String currPageStr = params.get("currPage");
		String pageSizeStr = params.get("pageSize");
		
		String name= params.get("name");
		
		PageBean<v_user_loan_user_unassigned> page = User.queryUserUnassigned(isExport==Constants.IS_EXPORT?Constants.NO_PAGE:0,name,currPageStr, pageSizeStr, error);
		
		if(isExport == Constants.IS_EXPORT){
			
			List<v_user_loan_user_unassigned> list = page.page;
			 
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
			jsonConfig.registerJsonValueProcessor(Double.class, new JsonDoubleValueProcessor("##,##0.00"));
			JSONArray arrList = JSONArray.fromObject(list, jsonConfig);
			
			File file = ExcelUtils.export("待分配的借款会员列表",
			arrList,
			new String[] {"借款人", "待还账单期数", "待还账单金额", "本月将到期期数", "本月将到期金额 ", "账户可用余额"},
			new String[] {"name", "unpaid_bill_count", "unpaid_bill_amount","maturity_bill_count","maturity_bill_amount","balance"});
			   
			renderBinary(file, "待分配的借款会员列表.xls");
		}
		
		if(page == null) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		render(page);
		
	}
	
	/**
	 * 借款会员管理分配
	 * @param currPage
	 * @param pageSize
	 * @param keyword
	 */
	public static void loanUserAssign(int currPage, int pageSize, String keyword, String userIdSign) {
		ErrorInfo error = new ErrorInfo();
		PageBean<v_supervisors> page = Supervisor.queryCustomers(currPage, pageSize, keyword, error);
		
		if (error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}

		render(page,userIdSign);
	}

	/**
	 * 分配单个借款标
	 */
	public static void assignBid( String typeStr,String tosSupervisorIdStr, String bidIdStr) {
		ErrorInfo error = new ErrorInfo();
		long toSupervisorId = Security.checkSign(tosSupervisorIdStr, Constants.SUPERVISOR_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			renderJSON(error);
		}
		
		long bidId = Security.checkSign(bidIdStr, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			renderJSON(error);
		}
		
		Bid.assignBidToSupervisor(toSupervisorId, typeStr, bidId, error);
		
		JSONObject json = new JSONObject();
		json.put("error", error);
		
		renderJSON(json);
	}
	
	/**
	 * 分配用户所有的标
	 */
	public static void assignUser(String tosSupervisorIdStr, String userIdSign) {
		ErrorInfo error = new ErrorInfo();
		long toSupervisorId = Security.checkSign(tosSupervisorIdStr, Constants.SUPERVISOR_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			renderJSON(error);
		}
		
		long userId = Security.checkSign(userIdSign, Constants.USER_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			renderJSON(error);
		}
		
		User.assignUser(toSupervisorId, userId, error);
		JSONObject json = new JSONObject();
		json.put("error", error);
		
		renderJSON(json);
	}
	
	/**
	 * 重新分配用户所有的标
	 */
	public static void assignUserAgain(String typeStr, String tosSupervisorIdStr, String userIdStr) {
		ErrorInfo error = new ErrorInfo();
		long toSupervisorId = Security.checkSign(tosSupervisorIdStr, Constants.SUPERVISOR_ID_SIGN, Constants.VALID_TIME, error);
		
		if (error.code < 0) {
			renderJSON(error);
		}
		
		Supervisor supervisor = Supervisor.currSupervisor();
		
		User.assignUserAgain(supervisor.id, typeStr, toSupervisorId+"", userIdStr, error);
		JSONObject json = new JSONObject();
		json.put("error", error);
		
		renderJSON(json);
	}

}
