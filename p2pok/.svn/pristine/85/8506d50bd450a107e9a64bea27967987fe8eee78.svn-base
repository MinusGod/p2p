package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import constants.Constants;
import constants.SQLTempletes;
import utils.Security;

@Entity
public class v_customers {
	
	/**
		SELECT 
			susr.id as supervisor_id, susr.name AS name,
			(SELECT COUNT(1) FROM t_users usr WHERE usr.assigned_to_supervisor_id = susr.id and usr.master_identity in(1,3)) AS user_count,
			(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1) AS waitdue_bill_count,
			IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1), 0.00) AS waitdue_amount,
			(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 AND bill.overdue_mark=0 AND YEAR(bill.repayment_time)=YEAR(NOW()) AND MONTH(bill.repayment_time)=MONTH(NOW())) AS maturity_bill_count,
			IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 AND bill.overdue_mark=0 AND YEAR(bill.repayment_time)=YEAR(NOW()) AND MONTH(bill.repayment_time)=MONTH(NOW())), 0.00) AS maturity_bill_amount,
			(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 and bill.overdue_mark in(-1,-2)) AS overdue_bill_count,
			IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 and bill.overdue_mark in(-1,-2)), 0.00) AS overdue_amount,
			(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 and bill.overdue_mark=-3) AS baddebt_bill_count,
			IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 and bill.overdue_mark=-3), 0.00) AS baddebt_amount
		FROM
		  t_supervisors susr WHERE susr.is_customer = 1
	 */
	public static String SQL ="SELECT susr.id as supervisor_id, susr.name AS name,(SELECT COUNT(1) FROM t_users usr WHERE usr.assigned_to_supervisor_id = susr.id and usr.master_identity in(1,3)) AS user_count,(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1) AS waitdue_bill_count,IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1), 0.00) AS waitdue_amount,(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 AND bill.overdue_mark=0 AND YEAR(bill.repayment_time)=YEAR(NOW()) AND MONTH(bill.repayment_time)=MONTH(NOW())) AS maturity_bill_count,IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 AND bill.overdue_mark=0 AND YEAR(bill.repayment_time)=YEAR(NOW()) AND MONTH(bill.repayment_time)=MONTH(NOW())), 0.00) AS maturity_bill_amount,(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 and bill.overdue_mark in(-1,-2)) AS overdue_bill_count,IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 and bill.overdue_mark in(-1,-2)), 0.00) AS overdue_amount,(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 and bill.overdue_mark=-3) AS baddebt_bill_count,IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bid.id=bill.bid_id JOIN t_users usr ON bid.user_id=usr.id WHERE usr.assigned_to_supervisor_id=susr.id AND bill.status=-1 and bill.overdue_mark=-3), 0.00) AS baddebt_amount FROM t_supervisors susr WHERE susr.is_customer = 1";
	
	/** 客服id */
	@Id
	public long supervisor_id;
	
	/** 客服名 */
	public String name;

	/** 借款会员数 */
	public int user_count;

	/** 待收账单数 */
	public int waitdue_bill_count;
	
	/** 待收金额 */
	public double waitdue_amount;

	/** 本月将到期期数 */
	public int maturity_bill_count;
	
	/** 本月将到期金额  */
	public double maturity_bill_amount;

	/** 逾期账单数 */
	public int overdue_bill_count;
	
	/** 逾期金额 */
	public double overdue_amount;
	
	/** 坏账账单数 */
	public int baddebt_bill_count;
	
	/** 坏账金额 */
	public double baddebt_amount;
	
	@Transient
	public String sign;
	
	/**
	 * 获取加密ID
	 */
	public String getSign() {
		return Security.addSign(this.supervisor_id, Constants.USER_ID_SIGN);
	}
	
}
