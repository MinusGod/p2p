package jobs;

import business.Invest;
import business.User;
import play.jobs.Every;
import play.jobs.Job;

/**
 * cps返佣发放，每50分钟执行一次
 *
 * @author hys
 * @createDate  2015年10月9日 下午3:51:13
 *
 */
@Every("50min")
public class PayForCPS extends Job {
	
	public void doJob() {
//		if(constants.Constants.ISJOB==1){
//		User.payForCps();  //cps奖励发放
//		}
	}
}
