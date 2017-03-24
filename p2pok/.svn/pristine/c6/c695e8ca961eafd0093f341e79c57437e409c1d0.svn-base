package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import constants.Constants;
import play.db.jpa.Model;
import utils.Security;

@Entity
public class v_user_loan_info_bill extends Model {

	/** 所属管理员id */
	public long supervisor_id;
	
	/** 类型 */
	public String type;
	
	/** 会员名称 */
	public String name;
	
	/** 注册时间 */
	public Date register_time;
	
	/** 用户余额 */
	public double user_amount;
	
	/** 最后登录时间 */
	public Date last_login_time;
	
	/** 借款标总数 */
	public int bid_count;
	
	/** 借款标总额 */
	public double bid_amount;
	
	/** 借款标总额 */
	public int invest_count;
	
	
	public double invest_amount;
	
	/** 借款中的借款标数量 */
	public int bid_loaning_count;  //
	
	/** 还款中的借款标数量 */
	public int bid_repaymenting_count;  //
	
	
	
	/** 坏账账单总数 */
	public int bad_bid_count;
	
	/** ~未还账单总数 */
	public int beRepaymenting_bill_count;
	
	/** ~未还账单总额 */
	public double beRepaymenting_bill_amount;//本金+利息+逾期罚款
	
	/** ~已还账单总数 */
	public int repayed_bill_count;//除了-1(未还)
	
	/** ~已还账单总额 */
	public double repayed_bill_amount;//本金+利息+逾期罚款
	
	/** 逾期账单总数 */
	public int overdue_bill_count;
	
	/** ~逾期账单总额 */
	public double overdue_bill_amount;//本金+利息+逾期罚款
	
	/** 信用等级图标 */
	public String credit_level_image_filename;
	
	
	@Transient
	public String sign;//加密ID
	
	public String getSign() {
		return Security.addSign(this.id, Constants.USER_ID_SIGN);
	}
	
}
