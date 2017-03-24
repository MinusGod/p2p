package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import business.BackstageSet;
import constants.Constants;
import play.db.jpa.Model;
import utils.Security;

/**
 * 我的会员账单---已还款账单列表
 */
@Entity
public class v_bill_haspayed{
	
    /**
		SELECT
			bid.title AS bid_title,
			usr. NAME AS user_name,
			bill.id AS bill_id,
			bill.bid_id AS bid_id,
			bill.repayment_corpus + bill.repayment_interest + bill.overdue_fine AS bill_amount,
			bill.periods AS period,
			(SELECT COUNT(1) FROM t_bills WHERE t_bills.bid_id = bill.bid_id) AS periods,
			bill.repayment_time AS repayment_time,
		    bill.real_repayment_time AS real_repayment_time,
			IFNULL(usr.assigned_to_supervisor_id,0) AS supervisor_id
		FROM
			t_bills bill
		JOIN t_bids bid ON bill.bid_id = bid.id
		JOIN t_users usr ON bid.user_id = usr.id
		WHERE
			bill. STATUS IN (-3 , 0)
	*/
	
	public static String SQL = "SELECT bid.title AS bid_title, usr. NAME AS user_name, bill.id AS bill_id, bill.bid_id AS bid_id, bill.repayment_corpus + bill.repayment_interest + bill.overdue_fine AS bill_amount,bill.periods AS period,(SELECT COUNT(1) FROM t_bills WHERE t_bills.bid_id = bill.bid_id) AS periods, bill.repayment_time AS repayment_time, bill.real_repayment_time AS real_repayment_time,IFNULL(usr.assigned_to_supervisor_id,0) AS supervisor_id FROM t_bills bill JOIN t_bids bid ON bill.bid_id = bid.id JOIN t_users usr ON bid.user_id = usr.id WHERE bill. STATUS IN (-3 , 0)";
	public static String SQL_COUNT = "SELECT COUNT(1) FROM t_bills bill JOIN t_bids bid ON bill.bid_id = bid.id JOIN t_users usr ON bid.user_id = usr.id WHERE bill. STATUS IN (-3 , 0)";
	
	
	/** 账单id */
	@Id
    public long bill_id;
	
	/** 借款标标题  */
    public String bid_title;
    
    /** 借款人 */
    public String user_name;
    
    /**
     * 账单金额：本金+利息+罚息
     */
    public double bill_amount;
    
    /** 账单期数 */
    public int period;

    /** 该借款标账单总期数 */
    public int periods;
    
    /** 到期还款时间 */
    public Date repayment_time;
    
    /** 实际还款时间 */
    public Date real_repayment_time;
    
    /** 客服id */
    public long supervisor_id;
    
    /** 借款标id */
    public long bid_id;

    @Transient
	public String sign;
    
    /** 账单编号 */
    @Transient
    public String bill_no;
    
    public String getBill_no(){
    	return BackstageSet.getCurrentBackstageSet().loanBillNumber + this.bill_id;
    }


	/**
	 * 获取加密ID
	 */
	public String getSign() {
		return Security.addSign(this.bill_id, Constants.BILL_ID_SIGN);
	}
}