package reports;

import java.util.Calendar;

import javax.persistence.Query;



import models.t_statistic_borrow;
import models.t_users;
import play.Logger;
import play.db.jpa.JPA;
import utils.Arith;
import utils.ErrorInfo;

import com.shove.Convert;

/**
 * 借款情况统计分析表
 * @author lzp
 * @version 6.0
 * @created 2014-7-16
 */
public class StatisticBorrow {
	/**
	 * 周期性执行
	 * @param error
	 * @return
	 */
	public static int executeUpdate(int year,int month,ErrorInfo error) {
		error.clear();
		boolean isAdd = isAdd(error);
		
		if (error.code < 0) {
			return error.code;
		}
		
		if (isAdd) {
			update(year,month,error);
		} else {
			add(year,month,error);
		}
		
		error.code = 0;
		
		return error.code;
	}
	
	/**
	 * 添加本月统计数据
	 * @param error
	 * @return
	 */
	private static int add(int year,int month,ErrorInfo error) {
		error.clear();
		

		t_statistic_borrow entity = new t_statistic_borrow();
		entity.year = year;
		entity.month=month;
		
		//累计借款总额
		entity.total_borrow_amount = queryTotalBorrowAmount(error);
		entity.total_borrow_amount_pc = queryTotalBorrowAmountPC(error);
		entity.total_borrow_amount_app = queryTotalBorrowAmountAPP(error);
		entity.total_borrow_amount_wechat = queryTotalBorrowAmountWechat(error);
		
		//本月借款总额
		entity.this_month_borrow_amount = queryThisMonthBorrowAmount(year,month,error);
		entity.this_month_borrow_amount_pc = queryThisMonthBorrowAmountPC(year,month,error);
		entity.this_month_borrow_amount_app = queryThisMonthBorrowAmountAPP(year,month,error);
		entity.this_month_borrow_amount_wechat = queryThisMonthBorrowAmountWechat(year,month,error);
		
		entity.total_borrow_user_num = queryTotalBorrowUserNum(error);
		entity.total_borrow_user_num_pc = queryTotalBorrowUserNumPC(error);
		entity.total_borrow_user_num_app = queryTotalBorrowUserNumAPP(error);
		entity.total_borrow_user_num_wechat = queryTotalBorrowUserNumWechat(error);
		entity.new_borrow_user_num = queryNewBorrowUserNum(year,month,error);
		entity.new_borrow_user_num_pc = queryNewBorrowUserNumPC(year,month,error);
		entity.new_borrow_user_num_app = queryNewBorrowUserNumAPP(year,month,error);
		entity.new_borrow_user_num_wechat = queryNewBorrowUserNumWechat(year,month,error);
		entity.finished_borrow_amount = queryFinishedBorrowAmount(year,month,error);
		entity.finished_borrow_amount_pc = queryFinishedBorrowAmountPC(year,month,error);
		entity.finished_borrow_amount_app = queryFinishedBorrowAmountAPP(year,month,error);
		entity.finished_borrow_amount_wechat = queryFinishedBorrowAmountWechat(year,month,error);
		
		//还款中的借款总额
		entity.repaying_borrow_amount = queryRepayingBorrowAmount(year,month,error);
		
		entity.released_bids_num_pc = queryReleasedBidsNumPC(year,month,error);
		entity.released_bids_num_app = queryReleasedBidsNumAPP(year,month,error);
		entity.released_bids_num_wechat = queryReleasedBidsNumWechat(year,month,error);
		
		//已成功借款标数量
		entity.released_bids_num = queryReleasedBidsNum(year,month,error);
		
		//已成功借款总额
		entity.released_borrow_amount = queryReleasedBorrowAmount(year,month,error);
		
		entity.average_annual_rate_pc = queryAverageAnnualRatePC(year,month,error);
		entity.average_annual_rate_app = queryAverageAnnualRateAPP(year,month,error);
		entity.average_annual_rate_wechat = queryAverageAnnualRateWechat(year,month,error);
		
		//平均年利率
		entity.average_annual_rate = queryAverageAnnualRate(year,month,error);
		
		//平均借款金额
		entity.average_borrow_amount = queryAverageBorrowAmount(year,month,error);
		
		entity.average_borrow_amount_pc = queryAverageBorrowAmountPC(year,month,error);
		entity.average_borrow_amount_app = queryAverageBorrowAmountAPP(year,month,error);
		entity.average_borrow_amount_wechat = queryAverageBorrowAmountWechat(year,month,error);
		
		//逾期账单数量
		entity.overdue_bids_num = queryOverduedBidsNum(error);
		//逾期账单总额
		entity.overdue_amount = queryOverdueAmount(year,month,error);
		//逾期总额占比
		entity.overdue_per = entity.repaying_borrow_amount == 0 ? 0 : Arith.div(entity.overdue_amount, entity.repaying_borrow_amount, 2);
		
		//坏账借款标数量
		entity.bad_bids_num = queryBadBidsNum(error);
		//坏账总额
		entity.bad_bill_amount = queryBadBillAmount(year,month,error);
		
		//坏账总额占比
		entity.bad_bill_amount_per = entity.repaying_borrow_amount == 0 ? 0 : Arith.div(entity.bad_bill_amount, entity.repaying_borrow_amount, 2);

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
	 * 更新本月统计数据
	 * @param error
	 * @return
	 */
	private static int update(int year,int month,ErrorInfo error) {
		error.clear();
		
		t_statistic_borrow entity = null;
		
		try {
			entity = t_statistic_borrow.find("year = ? and month = ?", year, month).first();
		} catch (Exception e) {
			Logger.error(e.getMessage());
			e.printStackTrace();
			error.code = -1;
			error.msg = "数据库异常";

			return error.code;
		}
		
		if (entity == null) {
			error.code = -1;
			error.msg = "本月借款情况统计不存在";
			
			return error.code;
		}
		
		entity.year = year;
		entity.month = month;
		entity.total_borrow_amount = queryTotalBorrowAmount(error);
		entity.total_borrow_amount_pc = queryTotalBorrowAmountPC(error);
		entity.total_borrow_amount_app = queryTotalBorrowAmountAPP(error);
		entity.total_borrow_amount_wechat = queryTotalBorrowAmountWechat(error);
		entity.this_month_borrow_amount = queryThisMonthBorrowAmount(year,month,error);
		entity.this_month_borrow_amount_pc = queryThisMonthBorrowAmountPC(year,month,error);
		entity.this_month_borrow_amount_app = queryThisMonthBorrowAmountAPP(year,month,error);
		entity.this_month_borrow_amount_wechat = queryThisMonthBorrowAmountWechat(year,month,error);
		entity.total_borrow_user_num = queryTotalBorrowUserNum(error);
		entity.total_borrow_user_num_pc = queryTotalBorrowUserNumPC(error);
		entity.total_borrow_user_num_app = queryTotalBorrowUserNumAPP(error);
		entity.total_borrow_user_num_wechat = queryTotalBorrowUserNumWechat(error);
		entity.new_borrow_user_num = queryNewBorrowUserNum(year,month,error);
		entity.new_borrow_user_num_pc = queryNewBorrowUserNumPC(year,month,error);
		entity.new_borrow_user_num_app = queryNewBorrowUserNumAPP(year,month,error);
		entity.new_borrow_user_num_wechat = queryNewBorrowUserNumWechat(year,month,error);
		entity.finished_borrow_amount = queryFinishedBorrowAmount(year,month,error);
		entity.finished_borrow_amount_pc = queryFinishedBorrowAmountPC(year,month,error);
		entity.finished_borrow_amount_app = queryFinishedBorrowAmountAPP(year,month,error);
		entity.finished_borrow_amount_wechat = queryFinishedBorrowAmountWechat(year,month,error);
		entity.repaying_borrow_amount = queryRepayingBorrowAmount(year,month,error);
		entity.released_bids_num_pc = queryReleasedBidsNumPC(year,month,error);
		entity.released_bids_num_app = queryReleasedBidsNumAPP(year,month,error);
		entity.released_bids_num_wechat = queryReleasedBidsNumWechat(year,month,error);
		entity.released_bids_num = queryReleasedBidsNum(year,month,error);
		entity.released_borrow_amount = queryReleasedBorrowAmount(year,month,error);
		entity.average_annual_rate_pc = queryAverageAnnualRatePC(year,month,error);
		entity.average_annual_rate_app = queryAverageAnnualRateAPP(year,month,error);
		entity.average_annual_rate_wechat = queryAverageAnnualRateWechat(year,month,error);
		entity.average_annual_rate = queryAverageAnnualRate(year,month,error);
		entity.average_borrow_amount = queryAverageBorrowAmount(year,month,error);
		entity.average_borrow_amount_pc = queryAverageBorrowAmountPC(year,month,error);
		entity.average_borrow_amount_app = queryAverageBorrowAmountAPP(year,month,error);
		entity.average_borrow_amount_wechat = queryAverageBorrowAmountWechat(year,month,error);
		entity.overdue_bids_num = queryOverduedBidsNum(error);
		entity.overdue_amount = queryOverdueAmount(year,month,error);
		entity.overdue_per = entity.this_month_borrow_amount == 0 ? 0 : entity.overdue_amount / entity.this_month_borrow_amount;
		entity.bad_bids_num = queryBadBidsNum(error);
		entity.bad_bill_amount = queryBadBillAmount(year,month,error);
		entity.bad_bill_amount_per = entity.this_month_borrow_amount == 0 ? 0 : entity.bad_bill_amount / entity.this_month_borrow_amount;

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
	 * 是否添加了本月数据
	 * @return
	 */
	private static boolean isAdd(ErrorInfo error) {
		error.clear();
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int count = 0;
		
		try {
			count = (int)t_statistic_borrow.count("year = ? and month = ?", year, month);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			e.printStackTrace();
			error.code = -1;
			error.msg = "数据库异常";

			return false;
		}
		
		error.code = 0;
		
		return (count > 0);
	}
	
	/**
	 * 查询累计借款总额
	 * @param error
	 * @return
	 */
	public static double queryTotalBorrowAmount(ErrorInfo error) {
		error.clear();
		String sql = "select sum(amount) from t_bids where status in (4, 5, 14)";
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * PC查询累计借款总额
	 * @param error
	 * @return
	 */
	public static double queryTotalBorrowAmountPC(ErrorInfo error) {
		error.clear();
		String sql = "select sum(amount) from t_bids where status in (4, 5, 14) and client = 1";
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * APP查询累计借款总额
	 * @param error
	 * @return
	 */
	public static double queryTotalBorrowAmountAPP(ErrorInfo error) {
		error.clear();
		String sql = "select sum(amount) from t_bids where status in (4, 5, 14) and client = 2";
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * wechat查询累计借款总额
	 * @param error
	 * @return
	 */
	public static double queryTotalBorrowAmountWechat(ErrorInfo error) {
		error.clear();
		String sql = "select sum(amount) from t_bids where status in (4, 5, 14) and client = 3";
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 查询本月借款总额
	 * @param error
	 * @return
	 */
	public static double queryThisMonthBorrowAmount(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(`t_bids`.`amount`) AS `sum(amount)` from `t_bids` where status in (4, 5, 14) and (YEAR(time)=? AND MONTH(time)=?)";
		Query query = JPA.em().createNativeQuery(sql);
		query.setParameter(1, year);
		query.setParameter(2, month);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询本月借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询本月借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * PC查询本月借款总额
	 * @param error
	 * @return
	 */
	public static double queryThisMonthBorrowAmountPC(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(`t_bids`.`amount`) AS `sum(amount)` from `t_bids` where status in (4, 5, 14) and ((YEAR(time)=? AND MONTH(time)=?) AND client = 1)";
		Query query = JPA.em().createNativeQuery(sql);
		query.setParameter(1, year);
		query.setParameter(2, month);

		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询本月借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询本月借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * APP查询本月借款总额
	 * @param error
	 * @return
	 */
	public static double queryThisMonthBorrowAmountAPP(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(`t_bids`.`amount`) AS `sum(amount)` from `t_bids` where status in (4, 5, 14) and ((YEAR(time)=? AND MONTH(time)=?) AND client = 2)";
		Query query = JPA.em().createNativeQuery(sql);
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
		
			Logger.error("查询本月借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询本月借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * wechat查询本月借款总额
	 * @param error
	 * @return
	 */
	public static double queryThisMonthBorrowAmountWechat(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(`t_bids`.`amount`) AS `sum(amount)` from `t_bids` where status in (4, 5, 14) and ((YEAR(time)=? AND MONTH(time)=?) and client = 3)";
		Query query = JPA.em().createNativeQuery(sql);
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询本月借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询本月借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 查询累计借款会员数
	 * @param error
	 * @return
	 */
	public static int queryTotalBorrowUserNum(ErrorInfo error) {
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("master_identity = 1 or master_identity = 3");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计借款会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计借款会员数出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return count;
	}
	
	/**
	 * PC查询累计借款会员数
	 * @param error
	 * @return
	 */
	public static int queryTotalBorrowUserNumPC(ErrorInfo error) {
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("client = 1 and master_identity = 1 or master_identity = 3");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计借款会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计借款会员数出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return count;
	}
	
	/**
	 * APP查询累计借款会员数
	 * @param error
	 * @return
	 */
	public static int queryTotalBorrowUserNumAPP(ErrorInfo error) {
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("client = 2 and master_identity = 1 or master_identity = 3");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计借款会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计借款会员数出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return count;
	}
	
	/**
	 * 	wechat查询累计借款会员数
	 * @param error
	 * @return
	 */
	public static int queryTotalBorrowUserNumWechat(ErrorInfo error) {
		error.clear();
		int count = 0;
		
		try {
			count = (int) t_users.count("client = 3 and master_identity = 1 or master_identity = 3");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询累计借款会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询累计借款会员数出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return count;
	}
	
	/**
	 * 查询新增借款会员数
	 * @param error
	 * @return
	 */
	public static int queryNewBorrowUserNum(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select count(*) from t_users where (YEAR(master_time_loan)=? and MONTH(master_time_loan)=?) or (master_time_loan = null and master_identity = 3 and YEAR(master_time_complex)=? and MONTH(master_time_complex)=?)";
		Query query = JPA.em().createNativeQuery(sql);
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, year);
		query.setParameter(4, month);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增借款会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增借款会员数出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * 查询PC新增借款会员数
	 * @param error
	 * @return
	 */
	public static int queryNewBorrowUserNumPC(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select count(*) from t_users where master_client = 1 and (YEAR(master_time_loan)=? AND MONTH(master_time_loan)=?) or (master_time_loan = null and master_identity = 3 and YEAR(master_time_complex)=? AND MONTH(master_time_complex)=?)";
		
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, year);
		query.setParameter(4, month);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增借款会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增借款会员数出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * 查询APP新增借款会员数
	 * @param error
	 * @return
	 */
	public static int queryNewBorrowUserNumAPP(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select count(*) from t_users where master_client = 2 and (YEAR(master_time_loan)=? AND MONTH(master_time_loan)=?) or (master_time_loan = null and master_identity = 3 and YEAR(master_time_complex)=? AND MONTH(master_time_complex)=?)";
		Query query = JPA.em().createNativeQuery(sql);

		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, year);
		query.setParameter(4, month);

		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增借款会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增借款会员数出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * 查询wechat新增借款会员数
	 * @param error
	 * @return
	 */
	public static int queryNewBorrowUserNumWechat(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select count(*) from t_users where master_client = 3 and (YEAR(master_time_loan)=? AND MONTH(master_time_loan)=?) or (master_time_loan = null and master_identity = 3 and YEAR(master_time_complex)=? AND MONTH(master_time_complex)=?)";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		query.setParameter(3, year);
		query.setParameter(4, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询新增借款会员数时："+e.getMessage());
			error.code = -1;
			error.msg = "查询新增借款会员数出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * 查询已完成借款总额
	 * @param error
	 * @return
	 */
	public static double queryFinishedBorrowAmount(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(amount) from t_bids where ((status = 5) and (YEAR(time)=? AND MONTH(time)=?))";
		Query query = JPA.em().createNativeQuery(sql);
		query.setParameter(1, year);
		query.setParameter(2, month);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已完成借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已完成借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 查询PC已完成借款总额
	 * @param error
	 * @return
	 */
	public static double queryFinishedBorrowAmountPC(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(amount) from t_bids where ((status = 5) and (YEAR(time)=? AND MONTH(time)=?) and client = 1)";
		Query query = JPA.em().createNativeQuery(sql);
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已完成借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已完成借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 查询APP已完成借款总额
	 * @param error
	 * @return
	 */
	public static double queryFinishedBorrowAmountAPP(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(amount) from t_bids where ((status = 5) and (YEAR(time)=? AND MONTH(time)=?) and client = 2)";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已完成借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已完成借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * wechat查询已完成借款总额
	 * @param error
	 * @return
	 */
	public static double queryFinishedBorrowAmountWechat(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(amount) from t_bids where ((status = 5) and (YEAR(time)=? AND MONTH(time)=?) and client = 3)";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已完成借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已完成借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 查询还款中的借款总额
	 * @param error
	 * @return
	 */
	public static double queryRepayingBorrowAmount(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(repayment_corpus+repayment_interest+overdue_fine) from t_bills where status in (-1, -2) and YEAR(repayment_time)=? AND MONTH(repayment_time)=?";
		Query query = JPA.em().createNativeQuery(sql);
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询还款中的借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询还款中的借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	/**
	 * 查询还款中的借款总额
	 * @param error
	 * @return
	 */
	public static double queryAllRepayingBorrowAmount(ErrorInfo error) {
		error.clear();
		String sql = "select sum(repayment_corpus+repayment_interest+overdue_fine) from t_bills where status in (-1, -2) ";
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询还款中的借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询还款中的借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 查询已放款借款标数量
	 * @param error
	 * @return
	 */
	public static int queryReleasedBidsNum(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select count(*) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=?";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已放款借款标数量时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已放款借款标数量出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * 查询PC已放款借款标数量
	 * @param error
	 * @return
	 */
	public static int queryReleasedBidsNumPC(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select count(*) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=? and client = 1";
		Query query = JPA.em().createNativeQuery(sql);
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已放款借款标数量时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已放款借款标数量出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * 查询APP已放款借款标数量
	 * @param error
	 * @return
	 */
	public static int queryReleasedBidsNumAPP(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select count(*) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=? and client = 2";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);

		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已放款借款标数量时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已放款借款标数量出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * wechat查询已放款借款标数量
	 * @param error
	 * @return
	 */
	public static int queryReleasedBidsNumWechat(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select count(*) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=? and client = 3";
		Query query = JPA.em().createNativeQuery(sql);

		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已放款借款标数量时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已放款借款标数量出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * 查询已放款借款总额
	 * @param error
	 * @return
	 */
	public static double queryReleasedBorrowAmount(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(amount) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=?";
		Query query = JPA.em().createNativeQuery(sql);

		query.setParameter(1, year);
		query.setParameter(2, month);

		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已放款借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已放款借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 平均年利率(基于已放款借款标数量来算)
	 * @param error
	 * @return
	 */
	public static double queryAverageAnnualRate(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select avg(apr) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=?";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
				
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已放款借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已放款借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * PC平均年利率(基于已放款借款标数量来算)
	 * @param error
	 * @return
	 */
	public static double queryAverageAnnualRatePC(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select avg(apr) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=? and client = 1";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已放款借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已放款借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * APP平均年利率(基于已放款借款标数量来算)
	 * @param error
	 * @return
	 */
	public static double queryAverageAnnualRateAPP(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select avg(apr) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=? and client = 2";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已放款借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已放款借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * wechat平均年利率(基于已放款借款标数量来算)
	 * @param error
	 * @return
	 */
	public static double queryAverageAnnualRateWechat(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select avg(apr) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=? and client = 3";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询已放款借款总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询已放款借款总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 均借款金额(基于已放款借款标数量来算)
	 * @param error
	 * @return
	 */
	public static double queryAverageBorrowAmount(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select avg(amount) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=?";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询均借款金额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询均借款金额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * PC均借款金额(基于已放款借款标数量来算)
	 * @param error
	 * @return
	 */
	public static double queryAverageBorrowAmountPC(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select avg(amount) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=? and client = 1";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询均借款金额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询均借款金额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * APP均借款金额(基于已放款借款标数量来算)
	 * @param error
	 * @return
	 */
	public static double queryAverageBorrowAmountAPP(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select avg(amount) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=? and client = 2";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询均借款金额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询均借款金额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * wechat均借款金额(基于已放款借款标数量来算)
	 * @param error
	 * @return
	 */
	public static double queryAverageBorrowAmountWechat(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select avg(amount) from t_bids where status in (4, 5, 14) and YEAR(audit_time)=? AND MONTH(audit_time)=? and client = 3";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询均借款金额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询均借款金额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 查询逾期借款标数量
	 * @param error
	 * @return
	 */
	public static int queryOverduedBidsNum(ErrorInfo error) {
		error.clear();
		String sql = "select count(distinct bid_id) from t_bills where status in (-1,-2) and overdue_mark in (-1,-2,-3)";
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询逾期借款标数量时："+e.getMessage());
			error.code = -1;
			error.msg = "查询逾期借款标数量出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * 查询逾期总额
	 * @param error
	 * @return
	 */
	public static double queryOverdueAmount(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(repayment_corpus+real_repayment_interest+overdue_fine) from t_bills where status in (-1,-2) and overdue_mark in (-1,-2,-3) and YEAR(repayment_time)=? AND MONTH(repayment_time)=?";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询逾期总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询逾期总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 查询坏账借款标数量
	 * @param error
	 * @return
	 */
	public static int queryBadBidsNum(ErrorInfo error) {
		error.clear();
		String sql = "select count(distinct bid_id) from t_bills where status in (-1,-2) and overdue_mark = -3";
		Query query = JPA.em().createNativeQuery(sql);
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询坏账借款标数量时："+e.getMessage());
			error.code = -1;
			error.msg = "查询坏账借款标数量出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToInt(obj+"", 0);
	}
	
	/**
	 * 查询坏账总额
	 * @param error
	 * @return
	 */
	public static double queryBadBillAmount(int year,int month,ErrorInfo error) {
		error.clear();
		String sql = "select sum(repayment_corpus+real_repayment_interest+overdue_fine) from t_bills where status in (-1,-2) and overdue_mark = -3 and YEAR(repayment_time)=? AND MONTH(repayment_time)=?";
		Query query = JPA.em().createNativeQuery(sql);
		
		query.setParameter(1, year);
		query.setParameter(2, month);
		
		Object obj = null;
		
		try {
			obj = query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询坏账总额时："+e.getMessage());
			error.code = -1;
			error.msg = "查询坏账总额出现异常！";
			
			return 0;
		}
		
		error.code = 0;
		
		return Convert.strToDouble(obj+"", 0);
	}
	
	/**
	 * 查询某一个月的借款总额
	 * @param year
	 * @param month
	 * @param error
	 * @return
	 */
	public static Double queryTotalBorrowAmount(int year, int month, ErrorInfo error){
		error.clear();
		
		Double amount = null;
		try {
			amount = t_statistic_borrow.find("select t.total_borrow_amount from t_statistic_borrow t where t.year = ? and t.month = ?", year,month).first();
		} catch (Exception e) {
			Logger.error("查询某一个月的借款总额时：" + e.getMessage());
			
			error.code = -1;
			error.msg = "查询某一个月的借款总额时有误！";
			
			return 0d;
		}
		
		//每个月的第一天没有记录，此时需要查上月的记录（这里只需要查最后一条记录即可）
		if(amount == null){
			try {
				amount = t_statistic_borrow.find("select total_borrow_amount from t_statistic_borrow order by year desc, month desc").first();
			} catch (Exception e) {
				Logger.error("查询某一个月的借款总额时：" + e.getMessage());
				
				error.code = -1;
				error.msg = "查询某一个月的借款总额时有误！";
				
				return 0d;
			}
		}
		
		error.code = 0;
		
		return amount == null ? 0 : amount;

	} 
	
}
