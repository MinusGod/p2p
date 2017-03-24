package models;
// default package

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import business.Bill;
import constants.Constants;
import play.db.jpa.Model;
import utils.Security;
import utils.ServiceFee;

/**
 * 我的借款账单
 */

@Entity
public  class v_bill_loan extends Model {
	
	public long user_id;
	public long bid_id;
	public String title;
	public int is_overdue;
	public String bid_no;
	public double repayment_amount;
	//借款管理费  add by xd.wang
	public double manager_amount;
	//本期需还款金额
	public double sum_amount;
	
	public int status;
	public Date repayment_time;
	public Date real_repayment_time;
	public int period;
	
	@Transient
	public String sign;
	
	/**
	 * 获取加密ID
	 */
	public String getSign() {
		return Security.addSign(this.id, Constants.BILL_ID_SIGN);
	}
	
	@Transient
	public int chechPeriod;

	public int getChechPeriod() {
		return Bill.checkPeriod(this.bid_id, this.period);
	}
	
}