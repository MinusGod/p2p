package reports;

import models.t_statistic_platform_income;
import models.t_user_details;
import constants.DealType;


/**
 * 平台收入情况统计分析表
 * @author liuwenhui
 *
 */
public class StatisticPlatformIncome {
	
	 public static Double queryLoanManagefee(int year,int month,int day){
		 
		Double loan_manage_fee = 0.0;//借款管理费
		String sql = "select sum(amount) from t_user_details where operation = ?  and (YEAR(time)=? AND MONTH(time)=? AND DAY(time)=?)";

		try {
			loan_manage_fee = t_user_details.find(sql,
					DealType.CHARGE_LOAN_SERVER_FEE,year, month, day).first();// 借款管理费
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null == loan_manage_fee || loan_manage_fee == 0) {
			loan_manage_fee = 0.0;
		}
		
		return loan_manage_fee;
	 }
	 
	 public static Double queryRechargeManagefee(int year,int month,int day){
		Double recharge_manage_fee = 0.0;//充值手续费
		String sql = "select sum(amount) from t_user_details where operation = ?  and (YEAR(time)=? AND MONTH(time)=? AND DAY(time)=?)";
		try {
			 recharge_manage_fee = t_user_details.find(sql, DealType.CHARGE_RECHARGE_FEE,year, month, day).first();//充值手续费
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == recharge_manage_fee || recharge_manage_fee == 0){
			recharge_manage_fee = 0.0;
		}
		
		return recharge_manage_fee;
	 }
	
	 
	 public static Double queryWithdrawManagefee(int year,int month,int day){
		Double withdraw_manage_fee = 0.0;//提现手续费
	    String sql = "select sum(amount) from t_user_details where operation = ? and (YEAR(time)=? AND MONTH(time)=? AND DAY(time)=?)";
		try {
			 withdraw_manage_fee = t_user_details.find(sql, DealType.CHARGE_WITHDRAWALT,year, month, day).first();//提现手续费
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == withdraw_manage_fee || withdraw_manage_fee == 0){
			withdraw_manage_fee = 0.0;
		}
		
		return withdraw_manage_fee;
	 }
	 
	 public static Double queryVipManagefee(int year,int month,int day){
		 Double vip_manage_fee = 0.0;//VIP会员费
		 String sql = "select sum(amount) from t_user_details where operation = ? and (YEAR(time)=? AND MONTH(time)=? AND DAY(time)=?)";
		 
		 try {
			 vip_manage_fee = t_user_details.find(sql, DealType.CHARGE_VIP,year, month, day).first();//VIP会员费
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == vip_manage_fee || vip_manage_fee == 0){
			vip_manage_fee = 0.0;
		}
		
		return vip_manage_fee;
	 }
	 
	 
	 public static Double queryInvestManagefee(int year,int month,int day){
		 Double invest_manage_fee = 0.0;//投资管理费
		 String sql = "select sum(amount) from t_user_details where operation = ?  and (YEAR(time)=? AND MONTH(time)=? AND DAY(time)=?)";
		 
		 try {
			 invest_manage_fee = t_user_details.find(sql, DealType.CHARGE_INVEST_FEE,year, month, day).first();//投资管理费
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == invest_manage_fee || invest_manage_fee == 0){
			invest_manage_fee = 0.0;
		}
		
		return invest_manage_fee;
	 }
	
	 
	 public static Double queryDebtTransferManagefee(int year,int month,int day){
		 Double debt_transfer_manage_fee = 0.0;//债权转让管理费
		 String sql = "select sum(amount) from t_user_details where operation = ?  and YEAR(time)=? AND MONTH(time)=? AND DAY(time)=?";
		 
		 try {
			 debt_transfer_manage_fee = t_user_details.find(sql, DealType.CHARGE_DEBT_TRANSFER_MANAGEFEE,year, month, day).first();//债权转让管理费
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == debt_transfer_manage_fee || debt_transfer_manage_fee == 0){
			debt_transfer_manage_fee = 0.0;
		}
		return debt_transfer_manage_fee;
	 }
	 
	 
	 public static Double queryItemauditManagefee(int year,int month,int day){
		
		 Double item_audit_manage_fee = 0.0;//资料审核费
		 String sql = "select sum(amount) from t_user_details where operation = ?  and (YEAR(time)=? AND MONTH(time)=? AND DAY(time)=?)";
		 try {
			 item_audit_manage_fee = t_user_details.find(sql, DealType.CHARGE_AUDIT_ITEM,year, month, day).first();//资料审核费
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == item_audit_manage_fee || item_audit_manage_fee == 0){
			item_audit_manage_fee = 0.0;
		}
		return item_audit_manage_fee;
	 }
	
	 
	 public static Double queryOverdueManagefee(int year,int month,int day){
		 Double overdue_manage_fee = 0.0;//逾期管理费
		 String sql = "select sum(amount) from t_user_details where operation = ?  and (YEAR(time)=? AND MONTH(time)=? AND DAY(time)=?)";
		 
		try {
			overdue_manage_fee = t_user_details.find(sql,
					DealType.CHARGE_OVERDUE_FEE,year, month, day).first();// 逾期管理费
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null == overdue_manage_fee || overdue_manage_fee == 0) {
			overdue_manage_fee = 0.0;
		}
		return overdue_manage_fee;
			
	 }
	
	 
	 //判断记录是否存在
	public static boolean judgeIsnew(int year,int month,int day){
		
		t_statistic_platform_income incom = null;
		try {
			 incom = t_statistic_platform_income.find(" year = ? and month = ? and day = ? ", year,month,day).first();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == incom){
			return true;
		}
		return false;
	}
	
	
	//获取对象
	public static t_statistic_platform_income getTarget(int year,int month,int day){
		
		t_statistic_platform_income incom = null ;
		try {
			 incom = t_statistic_platform_income.find(" year = ? and month = ? and day = ?", year,month,day).first();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return incom;
	}
	
}
