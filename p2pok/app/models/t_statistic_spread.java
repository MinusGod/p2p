package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * 直客推广月度统计表
 *
 * @author hys
 * @createDate  2015年9月17日 下午5:40:58
 *
 */
@Entity
public class t_statistic_spread extends Model{
	
	/** 年 */
	public int year;
	
	/** 月 */
	public int month;
	
	/** 直客推广码id */
	public long spread_code_id;
	
	/** 直客推广码 */
	public String spread_code;
	
	/** 管理员账号 */
	public String name;
	
	/** 管理员真实姓名 */
	public String reality_name;
	
	/** 当月推广会员数 */
	public int spread_user_count;
	
	/** 当月推广新增充值会员数 */
	public int recharge_user_count;
	
	/** 当月推广新增投资会员数 */
	public int invest_user_count;
	
	/** 被推广会员当月充值金额 */
	public double recharge_amount;
	
	/** 被推广会员当月投资金额 */
	public double invest_amount;
	
	/** 直客(管理员）当月提成金额 */
	public double cut_amount;
	
}
	
	


