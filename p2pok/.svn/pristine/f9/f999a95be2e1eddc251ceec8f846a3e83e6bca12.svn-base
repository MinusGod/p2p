package jobs;

import business.Bill;
import play.jobs.Job;
import play.jobs.On;
import utils.ErrorInfo;


/**
 * 每天执行系统标记逾期，执行时间：00:30
 * 
 * @author zhs
 * vesion: 6.0 
 * @date 2014-7-25 下午09:23:33
 */
 @On("0 30 0 * * ?")
public class MakeOverdue extends Job {

	 public void doJob(){
			if(constants.Constants.ISJOB==1){
		 ErrorInfo error = new ErrorInfo();
		 Bill bill = new Bill();		 
		 bill.systemMakeOverdue(error); //系统标记逾期
			}
	 }
}
