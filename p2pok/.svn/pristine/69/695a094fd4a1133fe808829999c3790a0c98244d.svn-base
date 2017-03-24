package jobs;

import business.Invest;
import business.User;
import play.jobs.Every;
import play.jobs.Job;

/**
 * 自动投标，每15分钟执行一次
 *
 * @author hys
 * @createDate  2015年10月9日 下午3:49:29
 *
 */
@Every("15min")
public class AutoInvest extends Job {
	
	public void doJob() {
		if(constants.Constants.ISJOB==1){
			Invest.automaticInvest();  //自动投标
		}
	}

}
