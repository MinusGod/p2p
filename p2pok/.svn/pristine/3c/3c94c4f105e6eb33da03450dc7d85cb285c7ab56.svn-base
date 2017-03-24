package business;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.shove.Convert;

import play.Logger;
import play.db.jpa.JPA;
import constants.Constants;
import constants.SupervisorEvent;
import constants.Constants.SystemSupervisor;
import models.t_spread_users;
import models.t_spreaders;
import models.t_statistic_spread;
import models.t_supervisors;
import models.t_wealthcircle_invite;
import utils.DateUtil;
import utils.ErrorInfo;
import utils.JPAUtil;
import utils.PageBean;
import utils.RegexUtils;

public class SpreadCode implements Serializable {

	/**
	 * 分页查询已经有的直客
	 */
	public static PageBean<t_spreaders> querySpreaders(int currPage, int pageSize, ErrorInfo error) {
		
		PageBean<t_spreaders> page = new PageBean<t_spreaders>();
		
		if (currPage == 0) {
			currPage = 1;
		}
		if (pageSize == 0) {
			pageSize = Constants.PAGE_SIZE;
		}
		
		page.currPage = currPage;
		page.pageSize = pageSize;
		
		String sqlCount = "select count(1) from t_spreaders";
		StringBuffer sql = new StringBuffer();
		sql.append("select id, spread_code, time, supervisor_id, name, reality_name, spread_user_count, recharge_user_count, invest_user_count,")
		   .append(" recharge_amount, invest_amount, status from t_spreaders");

		Object count = 0;
		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlCount);
		
		try {
			count = query.getSingleResult();
		} catch (Exception e) {
			Logger.error("查询直客信息：:" + e.getMessage());
			error.code = -1;
			error.msg = "查询直客信息时出现异常！";
			return page;
		}
		
		page.totalCount = count == null ? 0 : Integer.parseInt(count.toString());
		if (page.totalCount < 1) {
			return page;
		}
		
		List<t_spreaders> spreaders = new ArrayList<t_spreaders>();
		try {
			query = em.createNativeQuery(sql.toString(), t_spreaders.class);
			query.setFirstResult((currPage - 1) * pageSize);
			query.setMaxResults(pageSize);
			spreaders = query.getResultList();
			
		} catch (Exception e) {
			Logger.error("查询直客信息：" + e.getMessage());
			error.code = -1;
			error.msg = "查询直客信息时出现异常！";
		}
		
		error.code = 0;
		page.page = spreaders;
		return page;
	}
	
	/**
	 * 查询直客合计信息
	 */
	public static Map<String, Object>querySpreaderCount(String trueName, ErrorInfo error){
		
		StringBuffer sqlCount = new StringBuffer();
		sqlCount.append("select count(1) as spreader_count, IFNULL(sum(spread_user_count), 0) as allspread_user_count, IFNULL(sum(recharge_user_count), 0) as allrecharge_user_count,")
		        .append(" IFNULL(sum(invest_user_count), 0) as allinvest_user_count, IFNULL(sum(invest_amount), 0.00) as allinvest_amount from t_spreaders ");
		
		if(trueName == null || "".equals(trueName)){
			return JPAUtil.getMap(error, sqlCount.toString());
		}
		
		sqlCount.append(" where reality_name like ?");
		
		return JPAUtil.getMap(error, sqlCount.toString(), "%"+trueName+"%");
	}
	
	/**
	 * 添加直客
	 * @param supervisorIds
	 * @param error
	 * @return
	 */
	public static int addSpreaders(List<Long> supervisorIds, ErrorInfo error) {
		error.clear();
		
		if (supervisorIds.size() < 1) {
			error.code = -1;
			error.msg = "请选择管理员";
			
			return error.code;
		}
		String sql = "select count(1) from t_spreaders where supervisor_id=?";
		
		for (long supervisorId : supervisorIds) {
			int flag = 0;
			t_supervisors supervisor = null;
			try {
				flag = Integer.parseInt(JPAUtil.createNativeQuery(sql, supervisorId).getSingleResult().toString());
				supervisor = t_supervisors.findById(supervisorId);
			} catch (Exception e) {
				Logger.error(e.getMessage());
				e.printStackTrace();
				error.code = -1;
				error.msg = "数据库异常";
				
				return error.code;
			}
			
			if(flag > 0){
				continue;
			}
			if(supervisor == null){
				continue;
			}
			
			t_spreaders spreader = new t_spreaders();
			spreader.status = true;
			spreader.spread_code = String.valueOf(supervisor.id).length()<4 ? 
					Constants.NORMAL_SPREAD_PREFIX+String.format("%04d", supervisor.id) : Constants.NORMAL_SPREAD_PREFIX+supervisor.id;
			spreader.supervisor_id = supervisor.id;
			spreader.name = supervisor.name;
			spreader.reality_name = supervisor.reality_name;
			spreader.time = new Date();
			
			try {
				spreader.save();
			} catch (Exception e) {
				Logger.error(e.getMessage());
				e.printStackTrace();
				error.code = -1;
				error.msg = "数据库异常";
				JPA.setRollbackOnly();
				
				return error.code;
			}
		}
		
		DealDetail.supervisorEvent(Supervisor.currSupervisor().id, SupervisorEvent.ADD_SPREADER, "添加直客", error);
		
		if (error.code < 0) {
			JPA.setRollbackOnly();
			
			return error.code;
		}
		
		error.code = 0;
		error.msg = "添加直客成功";
		
		return error.code;
	}
	/**
	 * 查询有多少直客数据
	 */
	public static int hasSpreader(){
		
		String sqlCount = "select count(1) from t_spreaders";
		Object result = null;
		
		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlCount);
		
		try{
			result = query.getSingleResult();
		}catch(Exception e){
			Logger.error("查询直客数据时" + e.getMessage());
			return 0;
		}
		
		return result == null ? 0 : Integer.parseInt(result.toString());
	}
	/**
	 * 定时任务更新t_spreaders表数据
	 */
	public static void updateSpreader(){
		
		int count = hasSpreader();
		if(count<1){
			Logger.info("没有可更新的直客表数据");
			
			return;
		}
		
		String sqlSel = "select spread_code from t_spreaders";
		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlSel);

		List<?>list = null;
		
		try {
			list = query.getResultList();
		} catch (Exception e) {
			Logger.info("查询直客数据时" + e.getMessage());
			
			return;
		}
		
		List<Object[]>userCountlist = null;
		StringBuffer sqlUserCount = new StringBuffer();
		sqlUserCount.append("select (select count(1) from t_spread_users where spread_code=?) as spread_user_count,")
					.append(" (select count(1) from t_spread_users where spread_code=? and recharge_amount!=0.00) as recharge_user_count,")
					.append(" (select count(1) from t_spread_users where spread_code=? and invest_amount!=0.00) as invest_user_count,")
					.append(" (select sum(recharge_amount) from t_spread_users where spread_code=?) as recharge_amount,")
					.append(" (select sum(invest_amount) from t_spread_users where spread_code=?) as invest_amount")
					.append(" from dual");
		
		query = em.createNativeQuery(sqlUserCount.toString());
		
		String sqlUpd = "update t_spreaders set spread_user_count=?, recharge_user_count=?, invest_user_count=?, recharge_amount=?, invest_amount=? where spread_code=?";
		String spread_user_count = "";
		String recharge_user_count = "";
		String invest_user_count = "";
		String recharge_amount = "";
		String invest_amount = "";
		
		for(int i=0; i<count; i++){
			EntityManager em_for_upd = JPA.em();
			String spreadCode = list.get(i).toString();
			query.setParameter(1, spreadCode)
				 .setParameter(2, spreadCode)
				 .setParameter(3, spreadCode)
				 .setParameter(4, spreadCode)
				 .setParameter(5, spreadCode);
			userCountlist = query.getResultList();
			
			spread_user_count = userCountlist.get(0)[0]==null?"0":userCountlist.get(0)[0].toString();
			recharge_user_count = userCountlist.get(0)[1]==null?"0":userCountlist.get(0)[1].toString();
			invest_user_count = userCountlist.get(0)[2]==null?"0":userCountlist.get(0)[2].toString();
			recharge_amount = userCountlist.get(0)[3]==null?"0.00":userCountlist.get(0)[3].toString();
			invest_amount = userCountlist.get(0)[4]==null?"0.00":userCountlist.get(0)[4].toString();
			
			Query upd = em_for_upd.createQuery(sqlUpd);
			upd.setParameter(1, Integer.valueOf(spread_user_count)).setParameter(2, Integer.valueOf(recharge_user_count)).setParameter(3, Integer.valueOf(invest_user_count))
			.setParameter(4, Double.valueOf(recharge_amount)).setParameter(5, Double.valueOf(invest_amount)).setParameter(6, spreadCode);
			
			int k = 0;
			try{
				k = upd.executeUpdate();
			}catch(Exception e){
				Logger.error("更新直客表数据时" + e.getMessage());
				
				JPA.setRollbackOnly();
			}
			
			if(k<1){
				Logger.error("更新直客表数据时,直客码[" + spreadCode+"]更新失败");
				
				JPA.setRollbackOnly();
			}
			
		}
	}
	/**
	 * 更新直客是否启用
	 * @param id
	 * @param status
	 * @return
	 */
	public static int updateSpreaderStatus(long id, int status){
		t_spreaders spreader = t_spreaders.findById(id);
		if (spreader == null ){
			return 0;
		}
		
		spreader.status = status==1 ? true : false;	//非1的情况，设为不启用
		try {
			spreader.save();
		} catch (Exception e) {
			Logger.error("更新直客启停用状态时:"+e.getMessage());
			
			return 0;
		}
		
		return 1;
	}
	/**
	 * 查询直客列表
	 */
	public static PageBean<t_spreaders> queryInviteCodeList(String trueName, int currPage, int pageSize, int isExport, ErrorInfo error){
 		
 		if(currPage == 0 ) {
 			currPage = 1;
 		}
 		if(pageSize == 0) {
 			pageSize = Constants.PAGE_SIZE;
 		}
 		
 		StringBuffer sql = new StringBuffer("SELECT id, spread_code, time, supervisor_id, name, reality_name, spread_user_count, recharge_user_count, invest_user_count, recharge_amount, invest_amount, status FROM t_spreaders WHERE 1=1");
		StringBuffer sqlCount = new StringBuffer("SELECT COUNT(*) FROM t_spreaders WHERE 1 = 1");

		List<Object> params = new ArrayList<Object>();

		if(StringUtils.isNotBlank(trueName)){
			sql.append(" AND reality_name like ?");
			sqlCount.append(" AND reality_name like ?");
			params.add("%" + trueName + "%");
			
		}
		
		List<t_spreaders> invite = new ArrayList<t_spreaders>();
		int count = 0;
		PageBean<t_spreaders> page = new PageBean<t_spreaders>();
		try {
			EntityManager em = JPA.em();
            Query query = em.createNativeQuery(sql.toString(),t_spreaders.class);
            for(int n = 1; n <= params.size(); n++){
                query.setParameter(n, params.get(n-1));
            }
            if (isExport == 1){
            	
            	invite = query.getResultList();
            }else{
            	query.setFirstResult((currPage - 1) * pageSize);
                query.setMaxResults(pageSize);
                invite = query.getResultList();
            }
            
            Query queryCount = em.createQuery(sqlCount.toString());
    		for(int n = 1; n <= params.size(); n++){
    			queryCount.setParameter(n, params.get(n-1));
            }
    		count = Convert.strToInt(queryCount.getResultList().get(0) + "",0);
    		
		} catch (Exception e) {
			Logger.error("查询直客："+e.getMessage());
			error.code = -1;
			error.msg = "查询直客出现异常！";
			return page;
		}
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("name", trueName);
		page.pageSize = pageSize;
		page.currPage = currPage;
		page.totalCount = count;
		page.conditions = condition;
		page.page = invite;
		
		error.code = 0;
		
		return page;
	}
	
	
	
	
	// 0000.直客推广业务员的提成计算公式: double formulaCutAmount(int invest_user_count,double every_user_fee,
	//double invest_amount,double every_month_invest_rate,
	//int invest_user_count_InMonth, int invest_user_count_Total,double transform_rate)

	// 0000.更新直客推广名单的数据(供定时器调用): void updateSpreadUserRecord()
	// 0001.直客推广月度统计表数据更新(供定时器调用): void updateStatisticSpreadRecord()
	
	// 0100.检测直客推广码是否存在 int isSpreadCodeExist(String spreadCode, ErrorInfo error)
	
	// 0200.按月分页查询当月的直客数据 PageBean<Map<String, Object>> querySpreadStatistic(int currPage, int pageSize)
	// 0201.分页查询某年某个月的提成明细 PageBean<t_statistic_spread> querySpreadStatisticByMonthDetail(int year,int month,String reality_name,int currPage,int pageSize)
	// 0202.分页查询某个后台用户的推广的用户 PageBean<t_spread_users> queryMySpreadUsers(long supervisorId,String user_name, int currPage, int pageSize)
	
	// 0300.统计某个业务员某个月(date)的推广会员的投资总金额 double sumMySpreadUsersInvestInMonth(long supervisorId,Date date)
	// 0303.统计业务员的推广用户的投资金额 double sumMySpreadUsersInvest(long supervisorId)
	// 如果不输入时间则表示查询所有
	// 0301.统计业务员的推广用户的充值金额 double sumMySpreadUsersRecharge(long supervisorId)
	// 0302.统计某个业务员某个月(date)的推广会员的充值总金额 double
	// sumMySpreadUsersRechargeInMonth(long supervisorId,Date date)
	// 如果不输入时间则表示查询所有
	
	// 0400.统计某个后台某个时间(月份)用户的推广的用户总数 ， int countMySpreadUsers(long
	// supervisorId,Date date)
	// 如果不输入时间则表示查询所有
	// 0401.统计某个业务员某个月(date)的新增充值会员数， int countMySpreadUsersRecharge(long
	// supervisorId,Date date
	// 如果时间为null则查询所有的
	// 0402.统计某个业务员某个月(date)的新投资会员数， int countMySpreadUsersInvest(long
	// supervisorId,Date date
	// 如果时间为null则查询所有的
	// 0403.统计某个业务员推广推广会员某个月充值的总人数， int countMySpreadUsersInvestTotal(long
	// supervisorId,Date date
	// 如果时间为null则查询所有的

	/**
	 * 直客推广业务员的提成计算公式
	 * 
	 * @param invest_user_count
	 *            月新增投资会员数
	 * @param every_user_fee
	 *            直客推广的每位新增投资会员提成额
	 * @param invest_amount
	 *            月投资总额
	 * @param every_month_invest_rate
	 *            直客推广的会员月投资额提成率
	 * @param invest_user_count_InMonth
	 *            本月投资总人数
	 * @param invest_user_count_Total
	 *            总投资人数
	 * @param transform_rate
	 *            直客推广转化率的提成率
	 * @return
	 */
	public static double formulaCutAmount(int invest_user_count,
			double every_user_fee, double invest_amount,
			double every_month_invest_rate, int invest_user_count_InMonth,
			int invest_user_count_Total, double transform_rate) {
	
		double cut_amount1 = 0;
		invest_user_count = invest_user_count > 0 ? invest_user_count : 0;
		every_user_fee = every_user_fee > 0 ? every_user_fee : 0;
		cut_amount1 = invest_user_count * every_user_fee;

		double cut_amount2 = 0;
		invest_amount = invest_amount > 0 ? invest_amount : 0;
		every_month_invest_rate = every_month_invest_rate > 0 ? every_month_invest_rate : 0;
		cut_amount2 = invest_amount * every_month_invest_rate / 1000;

		double cut_amount3 = 0;
		invest_user_count_InMonth = invest_user_count_InMonth > 0 ? invest_user_count_InMonth : 0;
		transform_rate = transform_rate > 0 ? transform_rate : 0;
		if (invest_user_count_Total > 0) {
			cut_amount3 = (invest_amount * invest_user_count_InMonth * transform_rate)/ (invest_user_count_Total * 1000);
		}

		double cut_amount = cut_amount1 + cut_amount2 + cut_amount3;
		
		return cut_amount;
	}

	/**
	 * 统计某个业务员某个月(date)的推广会员的投资总金额
	 * 
	 * @param supervisorId
	 * @param date
	 * @return
	 */
	public static double sumMySpreadUsersInvestInMonth(long supervisorId, Date date) {
		
		StringBuffer sqlSum = new StringBuffer("select IFNULL(sum(amount),0) from t_invests i where i.transfer_status <>-1 ")
				.append(" and  EXISTS (select su.user_id from t_spread_users su where su.supervisor_id=? and su.user_id=i.user_id) ");

		List<Object> params = new ArrayList<Object>();

		params.add(supervisorId);
		if (date != null) {
			int dateYear = DateUtil.getYear(date);
			int dateMonth = DateUtil.getMonth(date);

			sqlSum.append(" and YEAR(i.time)=? and MONTH(i.time)=? ");
			params.add(dateYear);
			params.add(dateMonth);
		}

		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlSum.toString());

		for (int n = 1; n <= params.size(); n++) {
			query.setParameter(n, params.get(n - 1));
		}

		double sum = 0;
		List<?> list = null;
		try {
			list = query.getResultList();
			sum = list == null ? 0 : Double.parseDouble(list.get(0).toString());
		} catch (Exception e) {
			Logger.error(" 统计某个业务员某个月(date)的推广会员的投资总金额：:" + e.getMessage());
		}
		
		return sum;
	}

	/**
	 * 统计某个业务员某个月(date)的推广会员的充值总金额
	 * 
	 * @param supervisorId
	 * @param date
	 * @return
	 */
	public static double sumMySpreadUsersRechargeInMonth(long supervisorId,Date date) {

		StringBuffer sqlSum = new StringBuffer("select IFNULL(sum(urd.amount),0)  from t_user_recharge_details urd ")
				.append(" where EXISTS ( select su.user_id from t_spread_users su where su.supervisor_id=? and urd.user_id=su.user_id)");

		List<Object> params = new ArrayList<Object>();

		params.add(supervisorId);
		if (date != null) {
			int dateYear = DateUtil.getYear(date);
			int dateMonth = DateUtil.getMonth(date);

			sqlSum.append(" and  YEAR(urd.time)=? and MONTH(urd.time)=?");
			params.add(dateYear);
			params.add(dateMonth);
		}

		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlSum.toString());
		for (int n = 1; n <= params.size(); n++) {
			query.setParameter(n, params.get(n - 1));
		}

		double sum = 0;
		List<?> list = null;
		try {
			list = query.getResultList();
			sum = list == null ? 0 : Double.parseDouble(list.get(0).toString());
		} catch (Exception e) {
			Logger.error("统计业务员的推广用户的充值金额：:" + e.getMessage());
		}
		
		return sum;
	}

	/**
	 * 统计某个业务员推广推广会员某个月充值的总人数
	 * 
	 * @param supervisorId
	 * @param date
	 *            如果时间为null则查询所有的
	 * @return
	 */
	public static int countMySpreadUsersInvestTotal(long supervisorId, Date date) {

		StringBuffer sqlCount = new StringBuffer("select count(*) from ( ")
				.append(" select user_id from t_invests i ")
				.append(" where 1=1 and i.transfer_status<>-1 ")
				.append(" and EXISTS ( select su.user_id from t_spread_users su where su.supervisor_id=? and i.user_id=su.user_id ) ");

		List<Object> params = new ArrayList<Object>();

		params.add(supervisorId);
		if (date != null) {
			int dateYear = DateUtil.getYear(date);
			int dateMonth = DateUtil.getMonth(date);

			sqlCount.append(" and YEAR(i.time)=? and MONTH(i.time)=? ");
			params.add(dateYear);
			params.add(dateMonth);
		}

		sqlCount.append(" group by i.user_id ) as u ");

		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlCount.toString());

		for (int n = 1; n <= params.size(); n++) {
			query.setParameter(n, params.get(n - 1));
		}

		int count = 0;
		List<?> list = null;
		try {
			list = query.getResultList();
			count = list == null ? 0 : Integer.parseInt(list.get(0).toString());
		} catch (Exception e) {
			Logger.error("统计某个业务员推广推广会员某个月充值的总人数：:" + e.getMessage());
		}
		
		return count;
	}

	/**
	 * 统计某个业务员某个月(date)的新增投资会员数
	 * 
	 * @param supervisorId
	 * @param date
	 *            如果时间为null则查询所有的
	 * @return
	 */
	public static int countMySpreadUsersInvest(long supervisorId, Date date) {
		/*
		 SELECT count(id) FROM t_users u
		WHERE master_time_invest IS NOT NULL
		AND year(master_time_invest)=2015 and month(master_time_invest)=9
		and EXISTS (select 1 from t_spread_users s where s.supervisor_id=1 AND s.user_id=u.id)
		 */
		
		StringBuffer sqlCount = new StringBuffer("SELECT count(id) FROM t_users u ")
				.append(" WHERE master_time_invest IS NOT NULL ")
				.append(" and EXISTS (select 1 from t_spread_users s where s.supervisor_id=? AND s.user_id=u.id) ");
				//.append(" and i.time in (	select min(i2.time) from t_invests i2 group by i2.user_id) ");

		List<Object> params = new ArrayList<Object>();

		params.add(supervisorId);
		if (date != null) {
			int dateYear = DateUtil.getYear(date);
			int dateMonth = DateUtil.getMonth(date);

			sqlCount.append(" AND year(master_time_invest)=? and month(master_time_invest)=? ");
			params.add(dateYear);
			params.add(dateMonth);
		}

		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlCount.toString());

		for (int n = 1; n <= params.size(); n++) {
			query.setParameter(n, params.get(n - 1));
		}

		int count = 0;
		List<?> list = null;
		try {
			list = query.getResultList();
			count = list == null ? 0 : Integer.parseInt(list.get(0).toString());
		} catch (Exception e) {
			Logger.error("统计某个业务员某个月(date)的新增投资会员数：:" + e.getMessage());
		}
		
		return count;
	}

	/**
	 * 统计某个业务员某个月(date)的新增充值会员数
	 * 
	 * @param supervisorId
	 * @param date
	 *            如果时间为null则查询所有的
	 * @return
	 */
	public static int countMySpreadUsersRecharge(long supervisorId, Date date) {

		/*
		   
			SELECT min(urd2.time) AS time FROM t_user_recharge_details urd2
GROUP BY urd2.user_id
HAVING 1 = 1
AND YEAR (time) = 2015 and MONTH (time) = 9
AND EXISTS ( SELECT FROM t_spread_users su WHERE su.supervisor_id = 1 AND su.user_id = urd2.user_id )
		 */
		
		StringBuffer sqlCount = new StringBuffer("SELECT min(urd2.time) AS time FROM t_user_recharge_details urd2 GROUP BY urd2.user_id  ")
				.append("HAVING EXISTS ( SELECT 1 FROM t_spread_users su WHERE su.supervisor_id = ? AND su.user_id = urd2.user_id ) ");
			//	.append(" and EXISTS  ( select su.user_id from t_spread_users su  where su.supervisor_id=? and urd.user_id=su.user_id ) ")
			//	.append(" and urd.time in (	select min(urd2.time) from t_user_recharge_details urd2 group by urd2.user_id) ");

		List<Object> params = new ArrayList<Object>();

		params.add(supervisorId);
		if (date != null) {
			int dateYear = DateUtil.getYear(date);
			int dateMonth = DateUtil.getMonth(date);

			sqlCount.append(" AND YEAR (time) = ? and MONTH (time) = ? ");
			params.add(dateYear);
			params.add(dateMonth);
		}

		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlCount.toString());

		for (int n = 1; n <= params.size(); n++) {
			query.setParameter(n, params.get(n - 1));
		}

		int count = 0;
		List<?> list = null;
		try {
			list = query.getResultList();
			count = list == null ? 0 : list.size();
		} catch (Exception e) {
			Logger.error("统计某个业务员某个月(date)的新增充值会员数：:" + e.getMessage());
		}
		
		return count;
	}

	/**
	 * 更新直客推广名单的数据(供定时器调用)
	 */
	public static void updateSpreadUserRecord() {
		/*
		 update t_spread_users su 
		 set su.recharge_amount=(select IFNULL(sum(urd.amount),0) from t_user_recharge_details urd where urd.user_id=su.user_id),
		 su.invest_amount=(select IFNULL(sum(i.amount),0) from t_invests i where transfer_status<>-1 and i.user_id=su.user_id)
		 */

		String sql = "update t_spread_users su set "
				+ " su.recharge_amount=(select IFNULL(sum(urd.amount),0) from t_user_recharge_details urd where urd.user_id=su.user_id), "
				+ " su.invest_amount=(select IFNULL(sum(i.amount),0) from t_invests i where transfer_status<>-1 and i.user_id=su.user_id) ";

		Query query = JPA.em().createNativeQuery(sql);
		try {
			query.executeUpdate();

		} catch (Exception e) {
			Logger.error("更新直客推广名单的数据出错:" + e.getMessage());
		}

	}

	/**
	 * 直客推广月度统计表数据更新(供定时器调用)
	 */
	public static void updateStatisticSpreadRecord(Date date) {

		// 1.查询出所有的业务员
		List<t_spreaders> spreaders = t_spreaders.find(" status=? ", true).fetch();// 所有的业务员

		if (spreaders == null || spreaders.size() == 0) {
			
			return;
		}

		Date yestoday = DateUtil.dateAddDay(date, -1);
		int year = DateUtil.getYear(yestoday);
		int month = DateUtil.getMonth(yestoday);

		// 2.循环进行插入数据或者更新数据操作，以业务员为单位
		for (t_spreaders spreader : spreaders) {
			try {
				long supervisor_id = spreader.supervisor_id;
				long spread_code_id = spreader.id;

				int spread_user_count = SpreadCode.countMySpreadUsers(supervisor_id, yestoday);// 当月推广会员数
				int recharge_user_count = SpreadCode.countMySpreadUsersRecharge(supervisor_id, yestoday);// 当月推广充值会员数
				int invest_user_count = SpreadCode.countMySpreadUsersInvest(supervisor_id, yestoday);// 当月推广新增投资会员数
				double recharge_amount = SpreadCode.sumMySpreadUsersRechargeInMonth(supervisor_id,yestoday);// 被推广会员当月充值金额
				double invest_amount = SpreadCode.sumMySpreadUsersInvestInMonth(supervisor_id, yestoday);// 被推广会员当月投资金额

				int invest_user_count_InMonth = SpreadCode.countMySpreadUsersInvestTotal(supervisor_id, yestoday);
				int spreadUserCount = SpreadCode.countMySpreadUsers(supervisor_id, null);
				
				BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
				double every_user_fee = backstageSet.every_user_fee;
				double every_month_invest_rate = backstageSet.every_month_invest_rate;
				double transform_rate = backstageSet.transform_rate;

				double cut_amount = formulaCutAmount(invest_user_count,every_user_fee, 
						invest_amount, every_month_invest_rate,
						invest_user_count_InMonth, spreadUserCount,transform_rate);// 当月的提成总数(需要根据公式生成)

				// 2.1.查询月度统计表
				t_statistic_spread statisticSpread = t_statistic_spread.find(" spread_code_id=? and year=? and month=?",spread_code_id, year, month).first();

				// 2.2.判断月度统计表中是否含有该业务员的数据
				if (statisticSpread == null) {
					// 2.2.1(1) 为null，则进行数据插入操作
					t_statistic_spread statistic = new t_statistic_spread();
					statistic.year = year;
					statistic.month = month;
					statistic.spread_code_id = spreader.id;
					statistic.spread_code = spreader.spread_code;
					statistic.name = spreader.name;
					statistic.reality_name = spreader.reality_name;

					statistic.spread_user_count = spread_user_count;
					statistic.recharge_user_count = recharge_user_count;
					statistic.invest_user_count = invest_user_count;
					statistic.recharge_amount = recharge_amount;
					statistic.invest_amount = invest_amount;
					statistic.cut_amount = cut_amount;

					statistic.save();
				} else {
					// 2.2.1(2) 不为null，进行数据更新操作
					statisticSpread.spread_user_count = spread_user_count;
					statisticSpread.recharge_user_count = recharge_user_count;
					statisticSpread.invest_user_count = invest_user_count;
					statisticSpread.recharge_amount = recharge_amount;
					statisticSpread.invest_amount = invest_amount;
					statisticSpread.cut_amount = cut_amount;

					statisticSpread.save();
				}
			} catch (Exception e) {
				Logger.error("直客推广月度统计表数据更新:" + e.getMessage());
			}
		}
	}

	/**
	 * 检测直客推广码是否存在
	 * 
	 * @param invitationCode
	 * @param error
	 * @return -3:直客推广码为blank或者不存在<br>
	 *         -1:查询时出现错误<br>
	 *         -2:存在且有效
	 */
	public static int isSpreadCodeExist(String spreadCode, ErrorInfo error) {

		error.clear();
		
		if (StringUtils.isBlank(spreadCode)) {
			error.code = -3;
			error.msg = "直客推广码为空!";
			return error.code;
		}

		String sql = "spread_code = ? and status = 1 ";
		t_spreaders spreaders = null;
		try {
			spreaders = t_spreaders.find(sql, spreadCode).first();

		} catch (Exception e) {
			Logger.error("检测直客推广码是否存在时出错:" + e.getMessage());
			error.code = -1;
			error.msg = "检测直客推广码出错!";
			return error.code;
		}

		if (null == spreaders) {
			error.code = -3;
			error.msg = "直客推广码不存在";

			return error.code;
		}

		error.code = -2;

		return error.code;
	}

	/**
	 * 按月分页查询当月的直客数据
	 * 
	 * @param currPage
	 * @param pageSize
	 * @return Map<br>
	 *         key year<br>
	 *         key month<br>
	 *         key spreader_count 单月新增业务员<br>
	 *         key spread_user_count 单月新增推广会员数<br>
	 *         key recharge_user_count 单月新增充值会员数<br>
	 *         key invest_user_count 单月新增投资会员数<br>
	 *         key recharge_amount 单月充值总金额<br>
	 *         key invest_amount 单月投资总金额<br>
	 *         key cut_amount 当月提成合计<br>
	 */
	public static PageBean<Map<String, Object>> querySpreadStatistic(
			int currPage, int pageSize) {
		
		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();

		if (currPage == 0) {
			currPage = 1;
		}
		if (pageSize == 0) {
			pageSize = Constants.PAGE_SIZE;
		}

		page.currPage = currPage;
		page.pageSize = pageSize;

		StringBuffer sqlCount = new StringBuffer(
				"select count(sl.year)from (select ss.year from t_statistic_spread ss group by ss.year,ss.month) as sl");
		StringBuffer sqlQuery = new StringBuffer("select new Map(ss.year as year,")
						.append(" ss.month as month,")
						.append(" (select count(*) from t_spreaders where YEAR(time)=ss.year and MONTH(time)=ss.month) as spreader_count,")
						.append(" sum(ss.spread_user_count) as spread_user_count,")
						.append(" sum(ss.recharge_user_count) as recharge_user_count,")
						.append(" sum(ss.invest_user_count) as invest_user_count,")
						.append(" sum(ss.recharge_amount) as recharge_amount,")
						.append(" sum(ss.invest_amount) as invest_amount,")
						.append(" sum(ss.cut_amount) as cut_amount )")
						.append(" from t_statistic_spread ss group by ss.year,ss.month")
						.append(" ORDER BY ss.year desc,ss.month desc");

		List<Object> params = new ArrayList<Object>();

		int count = 0;
		EntityManager em = JPA.em();

		Query query = em.createNativeQuery(sqlCount.toString());
		for (int n = 1; n <= params.size(); n++) {
			query.setParameter(n, params.get(n - 1));
		}
		List<?> list = null;

		try {
			list = query.getResultList();
		} catch (Exception e) {
			Logger.error("按月分页查询当月的直客数据:" + e.getMessage());
			
			return page;
		}

		count = list == null ? 0 : Integer.parseInt(list.get(0).toString());
		page.totalCount = count;
		if (count < 1) {
			
			return page;
		}

		List<Map<String, Object>> spread_users = new ArrayList<Map<String, Object>>();
		try {
			query = em.createQuery(sqlQuery.toString());

			query.setFirstResult((currPage - 1) * pageSize);
			query.setMaxResults(pageSize);
			spread_users = query.getResultList();

		} catch (Exception e) {
			Logger.error("按月分页查询当月的直客数据：" + e.getMessage());
		}

		page.page = spread_users;
		
		return page;
	}

	/**
	 * 查询某年某个月的提成明细
	 * 
	 * @param year
	 * @param month
	 * @param user_name
	 *            推广用户的真实姓名
	 * @return
	 */
	public static List<t_statistic_spread> querySpreadStatisticByMonthDetail(int year, int month, String reality_name){

		List<t_statistic_spread> spread_users = null;
		
		StringBuffer sqlQuery = new StringBuffer("select * from t_statistic_spread ss where 1=1");
		List<Object> params = new ArrayList<Object>();
		
		sqlQuery.append(" and ss.year =? ");
		params.add(year);

		sqlQuery.append(" and ss.month =? ");
		params.add(month);

		if (StringUtils.isNotBlank(reality_name)) {
			sqlQuery.append(" and ss.reality_name LIKE ? ");
			params.add("%" + reality_name + "%");
		}

		EntityManager em = JPA.em();
		Query query = null;
		
		
		try {
			spread_users = new ArrayList<t_statistic_spread>();
			query = em.createNativeQuery(sqlQuery.toString(),t_statistic_spread.class);
			for (int n = 1; n <= params.size(); n++) {
				query.setParameter(n, params.get(n - 1));
			}
			
			spread_users = query.getResultList();

		} catch (Exception e) {
			Logger.error("查询某年某个月的提成明细：" + e.getMessage());
		}
		
		return spread_users;
	}
	
	/**
	 * 分页查询某年某个月的提成明细
	 * 
	 * @param year
	 * @param month
	 * @param user_name
	 *            推广用户的真实姓名
	 * @param currPage
	 * @param pageSize
	 * @return
	 */
	public static PageBean<t_statistic_spread> querySpreadStatisticByMonthDetail(
			int year, int month, String reality_name, int currPage, int pageSize) {

		PageBean<t_statistic_spread> page = new PageBean<t_statistic_spread>();

		if (currPage == 0) {
			currPage = 1;
		}
		if (pageSize == 0) {
			pageSize = 5;
		}

		page.currPage = currPage;
		page.pageSize = pageSize;

		StringBuffer sqlCount = new StringBuffer("select count(*) from t_statistic_spread ss where 1=1 ");
		StringBuffer sqlQuery = new StringBuffer("select * from t_statistic_spread ss where 1=1");

		List<Object> params = new ArrayList<Object>();
		Map<String, Object> conditionMap = new HashMap<String, Object>();

		conditionMap.put("reality_name", reality_name);
		conditionMap.put("year", year);
		conditionMap.put("month", month);
		page.conditions = conditionMap;

		sqlCount.append(" and ss.year =? ");
		sqlQuery.append(" and ss.year =? ");
		params.add(year);

		sqlCount.append(" and ss.month =? ");
		sqlQuery.append(" and ss.month =? ");
		params.add(month);

		if (StringUtils.isNotBlank(reality_name)) {
			sqlCount.append(" and ss.reality_name LIKE ? ");
			sqlQuery.append(" and ss.reality_name LIKE ? ");
			params.add("%" + reality_name + "%");
		}

		int count = 0;
		EntityManager em = JPA.em();

		Query query = em.createNativeQuery(sqlCount.toString());
		for (int n = 1; n <= params.size(); n++) {
			query.setParameter(n, params.get(n - 1));
		}
		List<?> list = null;

		try {
			list = query.getResultList();
		} catch (Exception e) {
			Logger.error("查询某年某个月的提成明细的数据总数：:" + e.getMessage());
			
			return page;
		}

		count = list == null ? 0 : Integer.parseInt(list.get(0).toString());
		page.totalCount = count;
		if (count < 1) {
			
			return page;
		}

		List<t_statistic_spread> spread_users = new ArrayList<t_statistic_spread>();
		try {
			query = em.createNativeQuery(sqlQuery.toString(),t_statistic_spread.class);
			for (int n = 1; n <= params.size(); n++) {
				query.setParameter(n, params.get(n - 1));
			}
			query.setFirstResult((currPage - 1) * pageSize);
			query.setMaxResults(pageSize);
			spread_users = query.getResultList();

		} catch (Exception e) {
			Logger.error("分页查询某年某个月的提成明细：" + e.getMessage());
		}

		page.page = spread_users;
		
		return page;
	}

	/**
	 * 统计某个后台某个时间(月份)用户的推广的用户总数 ，如果不输入时间则表示查询所有
	 * 
	 * @param supervisorId
	 * @param date
	 * @return
	 */
	public static int countMySpreadUsers(long supervisorId, Date date) {

		StringBuffer sqlCount = new StringBuffer("select count(*) from t_spread_users su where su.supervisor_id=?");

		List<Object> params = new ArrayList<Object>();

		params.add(supervisorId);

		if (date != null) {
			int dateYear = DateUtil.getYear(date);
			int dateMonth = DateUtil.getMonth(date);

			sqlCount.append(" and YEAR(su.time)=? and MONTH(su.time)=?");
			params.add(dateYear);
			params.add(dateMonth);
		}

		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlCount.toString());

		for (int n = 1; n <= params.size(); n++) {
			query.setParameter(n, params.get(n - 1));
		}

		int count = 0;
		List<?> list = null;
		try {
			list = query.getResultList();
			count = list == null ? 0 : Integer.parseInt(list.get(0).toString());
		} catch (Exception e) {
			Logger.error("查询某个后台用户的推广的用户的总数：:" + e.getMessage());
		}

		return count;
	}

	/**
	 * 统计业务员的推广用户的充值金额
	 * 
	 * @param supervisorId
	 * @param date
	 * @return
	 */
	public static double sumMySpreadUsersRecharge(long supervisorId) {

		StringBuffer sqlCount = new StringBuffer("select IFNULL(sum(recharge_amount),0) from t_spread_users su where su.supervisor_id=?");

		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlCount.toString());
		query.setParameter(1, supervisorId);

		double sum = 0;
		List<?> list = null;
		try {
			list = query.getResultList();
			sum = list == null ? 0 : Double.parseDouble(list.get(0)
					.toString());
		} catch (Exception e) {
			Logger.error("统计业务员的推广用户的充值金额：:" + e.getMessage());
		}

		return sum;
	}

	/**
	 * 统计业务员的推广用户的投资金额
	 * 
	 * @param supervisorId
	 * @return
	 */
	public static double sumMySpreadUsersInvest(long supervisorId) {

		StringBuffer sqlCount = new StringBuffer("select IFNULL(sum(invest_amount),0) from t_spread_users su where su.supervisor_id=?");

		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlCount.toString());
		query.setParameter(1, supervisorId);

		double sum = 0;
		List<?> list = null;
		try {
			list = query.getResultList();
			sum = list == null ? 0 : Double.parseDouble(list.get(0)
					.toString());
		} catch (Exception e) {
			Logger.error("统计业务员的推广用户的投资金额：:" + e.getMessage());
		}

		return sum;
	}

	/**
	 * 分页查询某个后台用户的推广的用户
	 * 
	 * @param userid
	 * @param user_name
	 *            推广用户的真实姓名
	 * @param currPage
	 * @param pageSize
	 * @return
	 */
	public static PageBean<t_spread_users> queryMySpreadUsers(long supervisorId, String user_name, int currPage, int pageSize) {

		PageBean<t_spread_users> page = new PageBean<t_spread_users>();

		if (currPage == 0) {
			currPage = 1;
		}
		if (pageSize == 0) {
			pageSize = Constants.PAGE_SIZE;
		}

		page.currPage = currPage;
		page.pageSize = pageSize;

		StringBuffer sqlCount = new StringBuffer("select count(*) from t_spread_users su where 1 = 1 ");
		StringBuffer sqlQuery = new StringBuffer("select * from t_spread_users su where 1 = 1 ");

		List<Object> params = new ArrayList<Object>();
		Map<String, Object> conditionMap = new HashMap<String, Object>();

		conditionMap.put("user_name", user_name);
		page.conditions = conditionMap;

		sqlCount.append(" and su.supervisor_id=? ");
		sqlQuery.append(" and su.supervisor_id=? ");
		params.add(supervisorId);

		if (user_name != null && (!"".equals(user_name))) {
			sqlCount.append(" and su.user_name LIKE ? ");
			sqlQuery.append(" and su.user_name LIKE ? ");
			params.add("%" + user_name + "%");
		}

		EntityManager em = JPA.em();
		Query query = em.createNativeQuery(sqlCount.toString());
		for (int n = 1; n <= params.size(); n++) {
			query.setParameter(n, params.get(n - 1));
		}

		int count = 0;
		List<?> list = null;

		try {
			list = query.getResultList();
		} catch (Exception e) {
			Logger.error("查询某个后台用户的推广的用户：:" + e.getMessage());
			return page;
		}

		count = list == null ? 0 : Integer.parseInt(list.get(0).toString());
		page.totalCount = count;
		if (count < 1) {
			return page;
		}

		List<t_spread_users> spread_users = new ArrayList<t_spread_users>();
		try {
			query = em.createNativeQuery(sqlQuery.toString(),t_spread_users.class);
			for (int n = 1; n <= params.size(); n++) {
				query.setParameter(n, params.get(n - 1));
			}
			query.setFirstResult((currPage - 1) * pageSize);
			query.setMaxResults(pageSize);
			spread_users = query.getResultList();

		} catch (Exception e) {
			Logger.error("查询某个后台用户的推广的用户：" + e.getMessage());
		}

		page.page = spread_users;
		
		return page;
	}

}
