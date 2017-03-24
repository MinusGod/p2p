package reports;

import java.util.Calendar;

import javax.persistence.Query;

import com.shove.Convert;

import models.t_statistic_member;
import models.t_user_vip_records;
import models.t_users;
import play.Logger;
import play.db.jpa.JPA;
import utils.Arith;
import utils.ErrorInfo;

/**
 * 会员数据统计分析表
 * @author zhs
 * @version 6.0
 * @created 2014-7-18
 */
public class StatisticMember {

	/**
	 * 周期性执行
	 * @param error
	 * @return
	 */
	public static int executeUpdate(int year,int month,int day,ErrorInfo error) {
		error.clear();
		boolean isAdd = isAdd(year, month, day,error);
		
		if (error.code < 0) {
			return error.code;
		}
		
		if (isAdd) {
			update(year, month, day,error);
		} else {
			add(year, month, day,error);
		}
		
		error.code = 0;
		return error.code;
	}
	
	/**
	 * 添加某日统计数据
	 * @param error
	 * @return
	 */
	private static int add(int year,int month,int day,ErrorInfo error) {
		error.clear();
		
		t_statistic_member entity = new t_statistic_member();
		
		entity.year = year;
		entity.month = month;
		entity.day = day;
		entity.new_member = queryNewMember(year, month, day,error);
		entity.new_member_pc = queryNewMemberPC(year, month, day,error);
		entity.new_member_app = queryNewMemberAPP(year, month, day,error);
		entity.new_member_wechat = queryNewMemberWechat(year, month, day,error);
		entity.new_recharge_member = queryNewRechargeMember(year, month, day,error);//已验证
		entity.new_recharge_member_pc = queryNewRechargeMemberPC(year, month, day,error);//已验证
		entity.new_recharge_member_app = queryNewRechargeMemberAPP(year, month, day,error);//已验证
		entity.new_recharge_member_wechat = queryNewRechargeMemberWechat(year, month, day,error);//已验证
		entity.new_member_recharge_rate = entity.new_member == 0 ? 0 : Arith.div(entity.new_recharge_member, entity.new_member, 2);
		entity.new_member_recharge_rate_pc = entity.new_member_pc == 0 ? 0 : Arith.div(entity.new_recharge_member_pc, entity.new_member_pc, 2);
		entity.new_member_recharge_rate_app = entity.new_member_app == 0 ? 0 : Arith.div(entity.new_recharge_member_app, entity.new_member_app, 2);
		entity.new_member_recharge_rate_wechat = entity.new_member_wechat == 0 ? 0 : Arith.div(entity.new_recharge_member_wechat, entity.new_member_wechat, 2);
		entity.new_vip_count = queryNewVipCount(year, month, day,error);
		entity.new_vip_count_pc = queryNewVipCountPC(year, month, day,error);
		entity.new_vip_count_app = queryNewVipCountAPP(year, month, day,error);
		entity.new_vip_count_wechat = queryNewVipCountWechat(year, month, day,error);
		entity.member_count = queryMemberCount(error);
		entity.member_count_pc = queryMemberCountPC(error);
		entity.member_count_app = queryMemberCountAPP(error);
		entity.member_count_wechat = queryMemberCountWechat(error);
		entity.member_activity = queryMemberActivity(year, month, day,error);//已验证
		entity.member_activity_pc = queryMemberActivityPC(year, month, day,error);//已验证
		entity.member_activity_app = queryMemberActivityAPP(year, month, day,error);//已验证
		entity.member_activity_wechat = queryMemberActivityWechat(year, month, day,error);//已验证
		entity.borrow_member_count = queryBorrowMemberCount(error);
		entity.invest_member_count = queryInvestMemberCount(year, month, day,error);
		entity.composite_member = queryCompositeMemberCount(year, month, day,error);
		entity.vip_count = queryVipCouont(error);

		try {
			entity.save();
		} catch (Exception e) {
			Logger.error(e.getMessage());
			e.printStackTrace();
			error.code = -1;
			error.msg = "数据库异常";

			return error.code;
		}

		error.code = 0;
		return error.code;
	}
	
	/**
	 * 更新某日统计数据
	 * @param error
	 * @return
	 */
	private static int update(int year,int month,int day,ErrorInfo error) {
		error.clear();
		
		
		t_statistic_member entity = null;
		
		try {
			entity = t_statistic_member.find("year = ? and month = ? and day = ?", year, month, day).first();
		} catch (Exception e) {
			Logger.error(e.getMessage());
			e.printStackTrace();
			error.code = -1;
			error.msg = "数据库异常";

			return error.code;
		}
		
		if (entity == null) {
			error.code = -1;
			error.msg = "本日本金保障统计分析表统计不存在";
			
			return error.code;
		}
		
		entity.year = year;
		entity.month = month;
		entity.day = day;
		entity.new_member = queryNewMember(year, month, day,error);
		entity.new_member_pc = queryNewMemberPC(year, month, day,error);
		entity.new_member_app = queryNewMemberAPP(year, month, day,error);
		entity.new_member_wechat = queryNewMemberWechat(year, month, day,error);
		entity.new_recharge_member = queryNewRechargeMember(year, month, day,error);
		entity.new_recharge_member_pc = queryNewRechargeMemberPC(year, month, day,error);
		entity.new_recharge_member_app = queryNewRechargeMemberAPP(year, month, day,error);
		entity.new_recharge_member_wechat = queryNewRechargeMemberWechat(year, month, day,error);
		entity.new_member_recharge_rate = entity.new_member == 0 ? 0 : Arith.div(entity.new_recharge_member, entity.new_member, 2);
		entity.new_member_recharge_rate_pc = entity.new_member_pc == 0 ? 0 : Arith.div(entity.new_recharge_member_pc, entity.new_member_pc, 2);
		entity.new_member_recharge_rate_app = entity.new_member_app == 0 ? 0 : Arith.div(entity.new_recharge_member_app, entity.new_member_app, 2);
		entity.new_member_recharge_rate_wechat = entity.new_member_wechat == 0 ? 0 : Arith.div(entity.new_recharge_member_wechat, entity.new_member_wechat, 2);
		entity.new_vip_count = queryNewVipCount(year, month, day,error);
		entity.new_vip_count_pc = queryNewVipCountPC(year, month, day,error);
		entity.new_vip_count_app = queryNewVipCountAPP(year, month, day,error);
		entity.new_vip_count_wechat = queryNewVipCountWechat(year, month, day,error);
		entity.member_count = queryMemberCount(error);
		entity.member_count_pc = queryMemberCountPC(error);
		entity.member_count_app = queryMemberCountAPP(error);
		entity.member_count_wechat = queryMemberCountWechat(error);
		entity.member_activity = queryMemberActivity(year, month, day,error);
		entity.member_activity_pc = queryMemberActivityPC(year, month, day,error);
		entity.member_activity_app = queryMemberActivityAPP(year, month, day,error);
		entity.member_activity_wechat = queryMemberActivityWechat(year, month, day,error);
		entity.borrow_member_count = queryBorrowMemberCount(error);
		entity.invest_member_count = queryInvestMemberCount(year, month, day,error);
		entity.composite_member = queryCompositeMemberCount(year, month, day,error);
		entity.vip_count = queryVipCouont(error);

		try {
			entity.save();
		} catch (Exception e) {
			Logger.error(e.getMessage());
			e.printStackTrace();
			error.code = -1;
			error.msg = "数据库异常";

			return error.code;
		}

		error.code = 0;
		return error.code;
	}
	
	/**
	 * 是否添加了某日数据
	 * @return
	 */
	private static boolean isAdd(int year,int month,int day,ErrorInfo error) {
		error.clear();
		
		int count = 0;
		
		try {
			count = (int)t_statistic_member.count("year = ? and month = ? and day = ?", year, month, day);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			e.printStackTrace();
			error.code = -1;
			error.msg = "数据库异常";

			return false;
		}
		
		return (count > 0);
	}
	
	/**
	 * 新增会员数
	 * @return
	 */
	public static int queryNewMember(int year,int month,int day,ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count(" YEAR(time)=? AND MONTH(time)=? AND DAY(time)=? ",year, month, day);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * PC新增会员数
	 * @return
	 */
	public static int queryNewMemberPC(int year,int month,int day,ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count(" YEAR(time)=? AND MONTH(time)=? AND DAY(time)=? and client = 1",year, month, day);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * APP新增会员数
	 * @return
	 */
	public static int queryNewMemberAPP(int year,int month,int day,ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("YEAR(time)=? AND MONTH(time)=? AND DAY(time)=? and client = 2",year, month, day);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * wechat新增会员数
	 * @return
	 */
	public static int queryNewMemberWechat(int year,int month,int day,ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("YEAR(time)=? AND MONTH(time)=? AND DAY(time)=? and client = 3",year, month, day);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * 新增充值会员数
	 * @return
	 */
	public static int queryNewRechargeMember(int year,int month,int day,ErrorInfo error){
		error.clear();
		String sql = "SELECT COUNT(distinct b.id) FROM t_users as b JOIN t_user_recharge_details as a where a.user_id = b.id" +
				" AND YEAR(a.time)=? AND MONTH(a.time)=? AND DAY(a.time)=? " +
				" AND YEAR(b.time)=? AND MONTH(b.time)=? AND DAY(b.time)=?";
		
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, day);
		query.setParameter(4, year);
		query.setParameter(5, month);
		query.setParameter(6, day);
		
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增充值会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增充值会员数出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * PC新增充值会员数
	 * @return
	 */
	public static int queryNewRechargeMemberPC(int year,int month,int day,ErrorInfo error){
		error.clear();
		String sql = "SELECT COUNT(distinct b.id) FROM t_users as b JOIN t_user_recharge_details as a where a.user_id = b.id" +
				" AND YEAR(a.time)=? AND MONTH(a.time)=? AND DAY(a.time)=? " +
				" AND YEAR(b.time)=? AND MONTH(b.time)=? AND DAY(b.time)=? AND b.client = 1";
		
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, day);
		query.setParameter(4, year);
		query.setParameter(5, month);
		query.setParameter(6, day);
		
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增充值会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增充值会员数出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * APP新增充值会员数
	 * @return
	 */
	public static int queryNewRechargeMemberAPP(int year,int month,int day,ErrorInfo error){
		error.clear();
		String sql = "SELECT COUNT(distinct b.id) FROM t_users as b JOIN t_user_recharge_details as a where a.user_id = b.id" +
				" AND YEAR(a.time)=? AND MONTH(a.time)=? AND DAY(a.time)=? " +
				" AND YEAR(b.time)=? AND MONTH(b.time)=? AND DAY(b.time)=? AND b.client = 2";
			
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, day);
		query.setParameter(4, year);
		query.setParameter(5, month);
		query.setParameter(6, day);
		
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增充值会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增充值会员数出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * wechat新增充值会员数
	 * @return
	 */
	public static int queryNewRechargeMemberWechat(int year,int month,int day,ErrorInfo error){
		error.clear();
		String sql = "SELECT COUNT(distinct b.id) FROM t_users as b JOIN t_user_recharge_details as a where a.user_id = b.id" +
				" AND YEAR(a.time)=? AND MONTH(a.time)=? AND DAY(a.time)=? " +
				" AND YEAR(b.time)=? AND MONTH(b.time)=? AND DAY(b.time)=? AND b.client = 3";
		
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, day);
		query.setParameter(4, year);
		query.setParameter(5, month);
		query.setParameter(6, day);
		
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增充值会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增充值会员数出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * 新增会员充值占比
	 * @return
	 */
	public static double queryNewMemberRechargeRate(ErrorInfo error){
		error.clear();
		String sql = "SELECT IFNULL(ROUND(((CASE WHEN (a.user_id = b.id and date_format(a.time, '%y%m%d') = date_format(curdate(), '%y%m%d'))" +
				" THEN COUNT(distinct a.user_id) END))/COUNT(distinct b.id)*100,2),0) FROM t_users as b JOIN " +
				"t_user_recharge_details as a where date_format(b.time, '%y%m%d') = date_format(curdate(), '%y%m%d')";
		
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增会员充值占比时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增会员充值占比出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 新增VIP会员数
	 * @return
	 */
	public static int queryNewVipCount(int year,int month,int day,ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_user_vip_records.count("YEAR(start_time)=? AND MONTH(start_time)=? AND DAY(start_time)=?",year, month, day);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增VIP会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增VIP会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * PC新增VIP会员数
	 * @return
	 */
	public static int queryNewVipCountPC(int year,int month,int day,ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_user_vip_records.count("YEAR(start_time)=? AND MONTH(start_time)=? AND DAY(start_time)=? AND client = 1",year, month, day);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增VIP会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增VIP会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * APP新增VIP会员数
	 * @return
	 */
	public static int queryNewVipCountAPP(int year,int month,int day,ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_user_vip_records.count("YEAR(start_time)=? AND MONTH(start_time)=? AND DAY(start_time)=? AND client = 2",year, month, day);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增VIP会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增VIP会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * wechat新增VIP会员数
	 * @return
	 */
	public static int queryNewVipCountWechat(int year,int month,int day,ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_user_vip_records.count("YEAR(start_time)=? AND MONTH(start_time)=? AND DAY(start_time)=? AND client = 3",year, month, day);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增VIP会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增VIP会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * 累计会员数
	 * @return
	 */
	public static int queryMemberCount(ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * PC累计会员数
	 * @return
	 */
	public static int queryMemberCountPC(ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("client = 1");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * APP累计会员数
	 * @return
	 */
	public static int queryMemberCountAPP(ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("client = 2");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * wechat累计会员数
	 * @return
	 */
	public static int queryMemberCountWechat(ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("client = 3");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * 会员活跃度
	 * @return
	 */
	public static double queryMemberActivity(int year,int month,int day,ErrorInfo error){
		error.clear();
		String sql = "SELECT ROUND((SELECT COUNT(b.id) FROM t_users AS b WHERE (YEAR(b.last_login_time)=? AND MONTH(b.last_login_time)=? AND DAY(b.last_login_time)=?))/count(a.id),2) FROM t_users AS a";
	
		Query query = JPA.em().createNativeQuery(sql);
	
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, day);
		
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询会员活跃度时："+e.getMessage());
			error.code = -1;
			error.msg = "查询会员活跃度出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * PC会员活跃度
	 * @return
	 */
	public static double queryMemberActivityPC(int year,int month,int day,ErrorInfo error){
		error.clear();
		String sql = "SELECT ROUND((SELECT COUNT(b.id) FROM t_users AS b WHERE (YEAR(b.last_login_time)=? AND MONTH(b.last_login_time)=? AND DAY(b.last_login_time)=?) AND login_client = 1)/count(a.id),2) FROM t_users AS a";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, day);
		
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询会员活跃度时："+e.getMessage());
			error.code = -1;
			error.msg = "查询会员活跃度出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * APP会员活跃度
	 * @return
	 */
	public static double queryMemberActivityAPP(int year,int month,int day,ErrorInfo error){
		error.clear();
		String sql = "SELECT ROUND((SELECT COUNT(b.id) FROM t_users AS b WHERE (YEAR(b.last_login_time)=? AND MONTH(b.last_login_time)=? AND DAY(b.last_login_time)=?) AND login_client = 2)/count(a.id),2) FROM t_users AS a";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, day);
		
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询会员活跃度时："+e.getMessage());
			error.code = -1;
			error.msg = "查询会员活跃度出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * wechat会员活跃度
	 * @return
	 */
	public static double queryMemberActivityWechat(int year,int month,int day,ErrorInfo error){
		error.clear();
		String sql = "SELECT ROUND((SELECT COUNT(b.id) FROM t_users AS b WHERE (YEAR(b.last_login_time)=? AND MONTH(b.last_login_time)=? AND DAY(b.last_login_time)=?) AND login_client = 3)/count(a.id),2) FROM t_users AS a";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, day);
		
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询会员活跃度时："+e.getMessage());
			error.code = -1;
			error.msg = "查询会员活跃度出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 借款会员数
	 * @return
	 */
	public static int queryBorrowMemberCount(ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("master_identity  = 1 or master_identity = 3");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询借款会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询借款会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * 投资会员数
	 * @return
	 */
	public static int queryInvestMemberCount(int year,int month,int day,ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("YEAR(master_time_invest)=? AND MONTH(master_time_invest)=? AND DAY(master_time_invest)=?",year, month, day);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询投资会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询投资会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * 复合会员数
	 * @return
	 */
	public static int queryCompositeMemberCount(int year,int month,int day,ErrorInfo error){
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("YEAR(master_time_complex)=? AND MONTH(master_time_complex)=? AND DAY(master_time_complex)=?",year, month, day);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询复合会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询复合会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * VIP会员数
	 * @return
	 */
	public static int queryVipCouont(ErrorInfo error){
		error.clear();
		int count = 0;
	
		try {
			count = (int) t_users.count("vip_status = 1");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询VIP会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询VIP会员数出现异常！";
			
			return 0;
		}
		
		return count;
	}
	
	/**
	 * PC端VIP会员数
	 * @return
	 */
	public static int queryVipCouontPC(ErrorInfo error){
		error.clear();
		
		String sql = "select count(a.id in ((SELECT (c.tid) as tt FROM (SELECT min(b.time) as cc, b.user_id as tid FROM t_user_vip_records as b where  b.client =1 GROUP BY b.user_id ) as c))) as count FROM t_users as a WHERE `a`.vip_status = 1";
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询会员活跃度时："+e.getMessage());
			error.code = -1;
			error.msg = "查询会员活跃度出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * wechat端VIP会员数
	 * @return
	 */
	public static int queryVipCouontWechat(ErrorInfo error){
		error.clear();
		
		String sql = "select count(a.id in ((SELECT (c.tid) as tt FROM (SELECT min(b.time) as cc, b.user_id as tid FROM t_user_vip_records as b where  b.client = 3 GROUP BY b.user_id ) as c))) as count FROM t_users as a WHERE `a`.vip_status = 1";
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询会员活跃度时："+e.getMessage());
			error.code = -1;
			error.msg = "查询会员活跃度出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * APP端VIP会员数
	 * @return
	 */
	public static int queryVipCouontAPP(ErrorInfo error){
		error.clear();
		
		String sql = "select count(a.id in ((SELECT (c.tid) as tt FROM (SELECT min(b.time) as cc, b.user_id as tid FROM t_user_vip_records as b where  b.client = 2 GROUP BY b.user_id ) as c))) as count FROM t_users as a WHERE `a`.vip_status = 1";
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		if(query.getResultList().size() == 0){
			return 0;
		}
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询会员活跃度时："+e.getMessage());
			error.code = -1;
			error.msg = "查询会员活跃度出现异常！";
			
			return 0;
		}
		
		return (obj == null) ? 0 : Convert.strToInt(obj+"", 0);
	}
}
