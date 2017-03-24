package jobs;

import business.CreditLevel;
import play.jobs.Job;
import play.jobs.On;
import play.jobs.OnApplicationStart;
import utils.ErrorInfo;

/**
 * 更新所有用户的信用等级，执行时间：02：50
 *
 * @author hys
 * @createDate  2015年6月24日 下午8:17:05
 *
 */
@On("0 50 2 * * ?")
public class UpdateCredictLevels extends Job {

	public void doJob() {
		if(constants.Constants.ISJOB==1){
		CreditLevel.updateAllUserCreditLevels(new ErrorInfo());
		}
	}
}
