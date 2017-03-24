package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import utils.Security;
import constants.Constants;

/**
 * 借款会员分配-待分配借款会员
 */

@Entity
public  class v_user_loan_user_unassigned {
	
	/** 待分配借款会员列表SQL:
		SELECT
			usr.id as user_id,
			usr.name as name,
			usr.balance as balance,
			(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1) as unpaid_bill_count,
			IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1),0) as unpaid_bill_amount,
      		(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark=0 AND YEAR(bill.repayment_time)=YEAR(NOW()) AND MONTH(bill.repayment_time)=MONTH(NOW())) as maturity_bill_count,
			IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark=0 AND YEAR(bill.repayment_time)=YEAR(NOW()) AND MONTH(bill.repayment_time)=MONTH(NOW())),0) as maturity_bill_amount
		FROM
			t_users usr
		WHERE
			master_identity IN (1, 3)
			AND (assigned_to_supervisor_id <= 0 OR assigned_to_supervisor_id IS NULL)
	 */
	public static String SQL = "SELECT usr.id as user_id,usr.name as name,usr.balance as balance,(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1) as unpaid_bill_count,IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1),0) as unpaid_bill_amount,(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark=0 AND YEAR(bill.repayment_time)=YEAR(NOW()) AND MONTH(bill.repayment_time)=MONTH(NOW())) as maturity_bill_count,IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark=0 AND YEAR(bill.repayment_time)=YEAR(NOW()) AND MONTH(bill.repayment_time)=MONTH(NOW())),0) as maturity_bill_amount FROM t_users usr WHERE master_identity IN (1,3) AND (assigned_to_supervisor_id <= 0 OR assigned_to_supervisor_id IS NULL)";
  
	/** 待分配借款会员计数SQL_COUNT */
	public static String SQL_COUNT = "SELECT COUNT(1) FROM t_users usr WHERE master_identity IN (1,3) AND (assigned_to_supervisor_id <= 0 OR assigned_to_supervisor_id IS NULL)";
    
    /** 借款人id */
    @Id
	public long user_id;
	
	/** 借款人账号 */
	public String name;
	
	/** 待还账单期数 */
	public int unpaid_bill_count;
	
	/** 待还账单金额  */
	public double unpaid_bill_amount;
	
	/** 本月将到期期数 */
	public int maturity_bill_count;
	
	/** 本月将到期金额  */
	public double maturity_bill_amount;
	
	/** 借款人账户可用余额  */
	public double balance;
	
	@Transient
	public String sign;
	
	/**
	 * 获取加密ID
	 */
	public String getSign() {
		return Security.addSign(this.user_id, Constants.USER_ID_SIGN);
	}
}