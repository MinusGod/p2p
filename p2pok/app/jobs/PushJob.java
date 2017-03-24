package jobs;

import play.jobs.Job;
import play.jobs.On;
import business.Bill;
import constants.Constants;

/**
 * 推送消息，执行时间：06：00
 * @author lwh
 *
 */
/*正式测试或上线请打开此任务*/
@On("0 0 6 * * ?")
public class PushJob extends Job{
	
	public void doJob() {
		// 账单提醒--推送
		if(constants.Constants.ISJOB==1){
		Bill.queryRecentlyBillsForCast(Constants.PUSH_BILL);
		}
	}
}
