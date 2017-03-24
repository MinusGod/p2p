package jobs;

import java.util.Calendar;
import java.util.Date;

import constants.Constants;
import business.Invest;
import business.Vip;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;
import play.jobs.OnApplicationStart;
import reports.StatisticAuditItems;
import reports.StatisticBorrow;
import reports.StatisticDebt;
import reports.StatisticInvest;
import reports.StatisticInvitation;
import reports.StatisticMember;
import reports.StatisticProduct;
import reports.StatisticRecharge;
import reports.StatisticSecurity;
import utils.DateUtil;
import utils.ErrorInfo;
import utils.JPAUtil;

/**
 * 后台数据统计模块更新，执行时间：01：00
 * @author lwh
 *
 */
@On("0 0 1 * * ?")
public class Statistic extends Job{
	
	/**
	 * quartz定时任务时间设置:
	 * 这些星号由左到右按顺序代表 ：     *    *     *     *    *     *   *  
	 *  		                  格式： [秒]  [分]  [小时]  [日]  [月]  [周] [年] 
	 * 序号 说明   是否必填    允许填写的值   允许的通配符 
	 * 1      秒          是                0-59    , - * /    
	 * 2     分           是                0-59    , - * /    
	 * 3   小时         是                0-23    , - * /    
	 * 4     日           是                1-31    , - * ? / L W    
	 * 5     月           是                1-12    , - * /    
     * 6  周          是                1-7     , - * ? / L # 
     * 7  年          否       1970-2099  , - * /                                                 
	 */
	
	public void doJob() {
		if(constants.Constants.ISJOB==1){
		ErrorInfo error = new ErrorInfo();
		Date date = DateUtil.dateAddDay(new Date(), -1);
		int year = DateUtil.getYear(date);
		int month = DateUtil.getMonth(date);
		int day = DateUtil.getDay(date);

		Logger.info("执行每日定时任务,时间:"+DateUtil.dateToString(date));
		
		StatisticAuditItems.executeUpdate(year, month, error);//审核科目库统计,按照审核科目统计
      	
		StatisticProduct.executeUpdate(year,month,error);//借款标产品销售情况, 按照借款产品统计
		
		StatisticBorrow.executeUpdate(year,month,error);//借款情况统计
		
		StatisticInvest.investSituationStatistic(year,month);//投资情况统计表
		
		StatisticInvest.platformIncomeStatistic(year, month, day);//平台收入
		
		StatisticInvest.platformWithdrawStatistic(year, month, day);//系统提现
		
		StatisticInvest.platformFloatstatistics(year, month, day);//平台浮存金统计
		
		StatisticRecharge.executeUpdate(year, month, day,error);//充值统计~
		
		StatisticMember.executeUpdate(year, month, day,error);//会员数据统计分~
		
		StatisticSecurity.executeUpdate(year, month, day,error);//本金保障统计~
		
		Vip.vipExpiredJob(); //vip过期处理
		
		Invest.creatBidPactJob();  //生成借款投资债权协议
		
		Invest.creatDebtPactJob();  //定时执行生成债权协议
		
		if(Constants.DEBT_USE) {
			StatisticDebt.debtSituationStatistics(year, month);//债权转让情况统计分析表
		}
		
		/** 财富圈报表统计 */
		JPAUtil.transactionBegin();
		StatisticInvitation.saveOrUpdateRecord(year, month);
		StatisticInvitation.saveOrUpdateDetailRecord(year, month);

		JPAUtil.transactionCommit();//这里会报错
		}

	}
}
