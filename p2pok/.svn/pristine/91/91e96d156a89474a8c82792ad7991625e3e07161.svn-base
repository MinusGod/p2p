package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import utils.Security;
import constants.Constants;

/**
 * 借款会员分配-逾期借款会员
 */
@Entity
public class v_bill_department_overdue {
	
	/** 逾期借款会员列表SQL：
	SELECT
		DISTINCT usr.id as user_id,
		usr.name as name,
		usr.balance as balance,
		(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark IN (-1,-2)) as overdue_bill_count,
		IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark IN (-1,-2)),0) as overdue_bill_amount,
		(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark=-3) as bad_bill_count,
		IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark=-3),0) as bad_bill_amount,	   
		IFNULL(susr.name,'未分配') AS supervisor_name
	FROM
		t_users usr LEFT JOIN t_supervisors susr ON susr.id=usr.assigned_to_supervisor_id JOIN t_bids bid ON bid.user_id=usr.id JOIN t_bills bill ON bill.bid_id=bid.id
	WHERE
		usr.master_identity IN (1, 3) AND bill.status=-1 AND bill.overdue_mark IN (-1,-2,-3)
	 */
	public static String SQL = "SELECT DISTINCT usr.id as user_id,usr.name as name,usr.balance as balance,(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark IN (-1,-2)) as overdue_bill_count,IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark IN (-1,-2)),0) as overdue_bill_amount,(SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark=-3) as bad_bill_count,IFNULL((SELECT SUM(bill.repayment_corpus+bill.repayment_interest+bill.overdue_fine) FROM t_bills bill JOIN t_bids bid ON bill.bid_id=bid.id WHERE bid.user_id=usr.id AND bill.status=-1 AND bill.overdue_mark=-3),0) as bad_bill_amount,	   IFNULL(susr.name,'未分配') AS supervisor_name FROM t_users usr LEFT JOIN t_supervisors susr ON susr.id=usr.assigned_to_supervisor_id JOIN t_bids bid ON bid.user_id=usr.id JOIN t_bills bill ON bill.bid_id=bid.id WHERE usr.master_identity IN (1, 3) AND bill.status=-1 AND bill.overdue_mark IN (-1,-2,-3)";
	
	/** 逾期借款会员计数SQL_COUNT */
	public static String SQL_COUNT = "SELECT COUNT(DISTINCT usr.id) FROM t_users usr JOIN t_bids bid ON bid.user_id=usr.id JOIN t_bills bill ON bill.bid_id=bid.id WHERE usr.master_identity IN (1, 3) AND bill.status=-1 AND bill.overdue_mark IN (-1,-2,-3)";
	
	/** 借款人id */
	@Id
	public long user_id;
	
	/** 借款人账号 */
	public String name;
	
	/** 逾期未还期数 */
	public int overdue_bill_count;
	
	/** 逾期未还金额  */
	public double overdue_bill_amount;
	
	/** 坏账未还期数 */
	public int bad_bill_count;
	
	/** 坏账未还金额  */
	public double bad_bill_amount;
	
	/** 借款人账户可用余额  */
	public double balance;
	
	/** 客服 */
	public String supervisor_name;
	
	@Transient
	public String sign;
	
	/**
	 * 获取加密ID
	 */
	public String getSign() {
		return Security.addSign(this.user_id, Constants.USER_ID_SIGN);
	}
}