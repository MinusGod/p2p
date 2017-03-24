package controllers.supervisor.networkMarketing;

import java.io.File;
import java.security.Policy.Parameters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jobs.SpreaderDataUpdateJob;
import models.t_spread_users;
import models.t_spreaders;
import models.t_statistic_spread;
import models.t_wealthcircle_invite;
import models.v_supervisors;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import business.BackstageSet;
import business.SpreadCode;
import business.Supervisor;
import business.Wealthcircle;
import utils.ErrorInfo;
import utils.ExcelUtils;
import utils.JsonDateValueProcessor;
import utils.JsonDoubleValueProcessor;
import utils.PageBean;
import constants.Constants;
import controllers.supervisor.SupervisorController;


/**
 * 直客:直客设置，直客管理，我的直客，直客统计
 * @author Administrator
 *
 */
public class SpreadAction extends SupervisorController {
	
	/**
	 * 查询直客规则
	 */
	public static void spreaderRuleSetting(){
		
		BackstageSet backstageset = BackstageSet.getCurrentBackstageSet();
		
		Map<String, Object>spreaderRule = new HashMap<String, Object>();
		spreaderRule.put("everyUserFee", backstageset.every_user_fee);
		spreaderRule.put("everyMonthInvestRate", backstageset.every_month_invest_rate);
		spreaderRule.put("transformRate", backstageset.transform_rate);
		spreaderRule.put("investDiscount", backstageset.invest_discount);
		
		render(spreaderRule);
	}
	/**
	 * 保存直客规则设置
	 */
	public static void saveSpreaderRule(Double everyUserFee, Double everyMonthInvestRate, Double transformRate, Integer investDiscount){
		
		ErrorInfo error = new ErrorInfo();
		BackstageSet backstageSet = new BackstageSet();
		
		if(everyUserFee == null){
			error.code = -1;
			error.msg = "月新增投资会员金额输入框必须输入数字";
			
			renderJSON(error);
		}
		
		if(everyMonthInvestRate == null){
			error.code = -1;
			error.msg = "月投资总额百分比输入框必须输入数字";
			
			renderJSON(error);
		}
		
		if(transformRate == null){
			error.code = -1;
			error.msg = "月投资总额转化率百分比输入框必须输入数字";
			
			renderJSON(error);
		}
		
		if(investDiscount == null){
			error.code = -1;
			error.msg = "被推广人投资手续费折扣百分比输入框必须输入数字";
			
			renderJSON(error);
		}
		
		backstageSet.every_user_fee = everyUserFee;
		backstageSet.every_month_invest_rate = everyMonthInvestRate;
		backstageSet.transform_rate = transformRate;
		backstageSet.invest_discount = investDiscount;
		backstageSet.setSpreaderRule(error);

		renderJSON(error);		
	}
	
	public static void spreadRuleSetting (){
		render();
	}
	
	/**
	 * 添加直客
	 */
	public static void addSpreaders(String signs){
		
		ErrorInfo error = new ErrorInfo();
		
		if (StringUtils.isBlank(signs) || StringUtils.split(signs, ",").length < 1) {
			error.code = -1;
			error.msg = "请选择管理员";
			
			renderJSON(error);
		}
		
		String[] arr = StringUtils.split(signs, ",");
		List<Long> supervisorIds = new ArrayList<Long>();
		String sign = null;
		
		for(int i=0; i<arr.length; i++){
			sign = arr[i];
			sign = com.shove.security.Encrypt.decrypt3DES(sign, Constants.SUPERVISOR_ID_SIGN);
			
			if(StringUtils.isBlank(sign)) {
				error.code = -1;
				error.msg = "请求非法!";
				
				return;
			}
			
			long supervisorId = Long.parseLong(sign);
			
			supervisorIds.add(supervisorId);
		}

		SpreadCode.addSpreaders(supervisorIds, error);
		
		renderJSON(error);
	}
	
	/**
	 * 添加直客
	 */
	public static void addSpreaderInit(){
		
		render();
	}
	
	/**
	 * 选择直客初始化
	 */
	public static void selectSupervisorInit(int currPage, int pageSize, String keyword){
		
		ErrorInfo error = new ErrorInfo();
		PageBean<v_supervisors> pageBean = 
				Supervisor.queryCandidateSpreaders(currPage, pageSize, keyword, error);
		
		if (error.code < 0) {
			renderJSON(error);
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("keyword", keyword);
		pageBean.conditions = condition;
		
		render(pageBean);
	}
	
	/**
	 * 查询选择的管理员
	 * @param supervisorIds
	 */
	public static void selectedSupervisors(String signs) {
		ErrorInfo error = new ErrorInfo();
		
		if (StringUtils.isBlank(signs) || StringUtils.split(signs, ",").length < 1) {
			error.code = -1;
			error.msg = "管理员列表不能为空";
			
			renderJSON(error);
		}
		
		String[] arr = StringUtils.split(signs, ",");
		List<Long> supervisorIds = new ArrayList<Long>();
		String sign = null;

		for (int i = 0; i < arr.length; i++) {
			sign = arr[i];
			sign = com.shove.security.Encrypt.decrypt3DES(sign, Constants.SUPERVISOR_ID_SIGN);
			
			if(StringUtils.isBlank(sign)) {
				error.code = -1;
				error.msg = "请求非法!";
				
				return;
			}
			
			long supervisorId = Long.parseLong(sign);
			
			supervisorIds.add(supervisorId);
		}
		
		List<v_supervisors> supervisors = Supervisor.querySupervisors(supervisorIds, error);
		
		if (error.code < 0) {
			renderJSON(error);
		}
		
		render(supervisors);
	}
	
	/**
	 * 更新直客是否启用
	 * @param id
	 * @param status
	 */
	public static void updateSpreaderStatus(long id, int acvite){
		
		int result = SpreadCode.updateSpreaderStatus(id, acvite);
		renderText(result);
	}
	
	/**
	 * 直客管理
	 */
	public static void spreaderManager(String trueName, int currPage, int pageSize, int isExport){
		ErrorInfo error = new ErrorInfo();
		
		PageBean<t_spreaders> page = SpreadCode.queryInviteCodeList(trueName, currPage, pageSize, isExport, error);
		
		if(isExport == Constants.IS_EXPORT){
			
			List<t_spreaders> list = page.page;
			
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
			jsonConfig.registerJsonValueProcessor(Double.class, new JsonDoubleValueProcessor("##,##0.00"));
			JSONArray arrList = JSONArray.fromObject(list, jsonConfig);
			
			File file = ExcelUtils.export("直客推广码",
					arrList,
					new String[] {
					"推广码", "经纪人", "真实姓名", "推广会员","充值会员","投资会员","充值金额","投资金额"},
					new String[] {"spread_code", "name", "reality_name", "spread_user_count",
					"recharge_user_count", "invest_user_count", "recharge_amount", "invest_amount"});
			
			renderBinary(file, "直客列表.xls");
		}
		Date lastUpdatetime = SpreaderDataUpdateJob.date;//最后更新时间，直接取job的时间
		Map<String, Object>map = SpreadCode.querySpreaderCount(trueName, error);
		
		render(page, map, lastUpdatetime);
	}
	
	/**
	 * 直客统计
	 */
	public static void spreadStatistic(int currPage,int pageSize){
		
		Logger.debug("参数:[currPage:"+currPage+",pageSize:"+pageSize+"]");
		
		PageBean<Map<String, Object>> page = SpreadCode.querySpreadStatistic(currPage, pageSize);
		
		Date lastUpdatetime = SpreaderDataUpdateJob.date;//最后更新时间，直接取job的时间
		
		render(page,lastUpdatetime);
	}
	
	/**
	 * 直客统计--提成明细(可以选择导出当月的报表)
	 */
	public static void spreadStatisticDetail(int year,int month,String reality_name,int currPage,int pageSize,int isExport){
		Logger.debug("参数:[year:"+year+",month:"+month+",reality_name:"+reality_name+",currPage:"+currPage+",pageSize:"+pageSize+",isExport="+isExport+"]");
		
		PageBean<t_statistic_spread> pageDetail = SpreadCode.querySpreadStatisticByMonthDetail(year,month,reality_name,currPage,pageSize);
	
	
		if(isExport == Constants.IS_EXPORT){

			List<t_statistic_spread> list = SpreadCode.querySpreadStatisticByMonthDetail(year,month,reality_name);
			
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
			jsonConfig.registerJsonValueProcessor(Double.class, new JsonDoubleValueProcessor("##,##0.00"));
			JSONArray arrList = JSONArray.fromObject(list, jsonConfig);
			
			String filename=year+"年"+month+"月直客推广统计表";
			File file = ExcelUtils.export(filename,
					arrList,
					new String[] {
					"推广码", "业务员", "真实姓名", "新增会员","充值会员","投资会员","充值总额","投资总额","提成总额"},
					new String[] {"spread_code", "name", "reality_name",
					"spread_user_count", "recharge_user_count", "invest_user_count", "recharge_amount","invest_amount","cut_amount"});
			
			renderBinary(file, filename+".xls");
		}
		
		render(pageDetail,reality_name);
	}
	
	/**
	 * 我的直客
	 */
	public static void spreadUsers(String user_name,int currPage,int pageSize){

		Logger.debug("参数:[user_name:"+user_name+",currPage:"+currPage+",pageSize:"+pageSize+"]");
		Supervisor supervisor = Supervisor.currSupervisor();
		
		PageBean<t_spread_users> page = SpreadCode.queryMySpreadUsers(supervisor.id, user_name,currPage, pageSize);
		
		int spreadUserCount = SpreadCode.countMySpreadUsers(supervisor.id,null);//推广会员总数
		double spreadUserRecharge = SpreadCode.sumMySpreadUsersRecharge(supervisor.id);//推广会员充值总数
		double spreadUserInvest = SpreadCode.sumMySpreadUsersInvest(supervisor.id);//推广会员充值总数
		
		render(page,spreadUserCount,spreadUserRecharge,spreadUserInvest);
	}
}
