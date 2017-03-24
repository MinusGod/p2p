package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * 直客推广码表
 *
 * @author hys
 * @createDate  2015年9月17日 下午5:25:05
 *
 */
@Entity
public class t_spreaders extends Model{

	/** 直客推广码 */
	public String spread_code;  

	/** 创建时间 */
	public Date time;  

	/** 管理员id */
	public long supervisor_id; 

	/** 管理员账号 */
	public String name;  

	/** 管理员真实姓名 */
	public String reality_name;  

	/** 推广会员总数  */
	public int spread_user_count;  

	/** 推广充值会员总数 */
	public int recharge_user_count;  

	/** 推广的投资会员总数 */
	public int invest_user_count;  

	/** 推广会员充值金额 */
	public double recharge_amount;  

	/** 推广会员投资金额 */
	public double invest_amount;  

	/** 状态。1启用，0停用 */
	public boolean status;  

}
