package jobs;

import java.util.Date;

import business.SpreadCode;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;
import play.jobs.OnApplicationStart;
import utils.DateUtil;
import utils.ErrorInfo;

/**
 * 每天更新直客统计数据，执行时间：02：20
 *
 */
@On("0 20 2 * * ?") 
@OnApplicationStart
public class SpreaderDataUpdateJob extends Job{
	
	/** 当前业务执行的时间 */
	public static Date date;
	
	@Override
	public void doJob() throws Exception {
		if(constants.Constants.ISJOB==1){
		date = new Date();
		Logger.info("#### 直客模块相关数据更新定时任务，时间: "+DateUtil.dateToString(date)+" ####");
		
		//1.更新直客推广名单的数据
		SpreadCode.updateSpreadUserRecord();
				
		//2.直客推广月度统计表数据更新
		SpreadCode.updateStatisticSpreadRecord(date);
		
		//3.更新直客管理数据
		SpreadCode.updateSpreader();
		}
	}
}
