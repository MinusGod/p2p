package jobs;

import play.jobs.Job;
import play.jobs.On;
import business.Agency;
import business.Bill;
import constants.Constants;

/**
 * 数据统计，合作机构列表数据。执行时间：02：40
 *
 * @author hys
 * @createDate  2015年10月9日 下午4:05:37
 *
 */
@On("0 40 2 * * ?")
public class UpdateAgency extends Job{
	public void doJob() {
		if(constants.Constants.ISJOB==1){
		Agency.updateData();
		}
	}
}
