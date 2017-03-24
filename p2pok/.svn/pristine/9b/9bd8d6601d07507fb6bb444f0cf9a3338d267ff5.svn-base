package jobs;

import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import reports.StatisticCPS;
import business.Debt;
import constants.Constants;

/**
 * 每37分钟判断正在转让的债权是否到达流拍时间
 */
@Every("37min")
public class CheckDebtIsFlow extends Job{
	
	public void doJob() {
		if(constants.Constants.ISJOB==1){
		if(Constants.DEBT_USE) {
			Logger.info("--------------定时判断债权流拍,开始---------------------");
			Debt.judgeDebtFlow();
			Logger.info("--------------定时判断债权流拍,结束---------------------");
		}
		}
	  
	}
}
