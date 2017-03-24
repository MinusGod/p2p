package jobs;

import java.util.List;

import play.jobs.Every;
import play.jobs.Job;
import utils.ErrorInfo;
import utils.SMSUtil;
import bean.FundraiseingBid;
import business.BackstageSet;
import business.Bid;
import business.User;
import constants.Constants;
@Every("10min")
public class CheckBidAmount extends Job{
	public static boolean flag=true;
	public void doJob(){
		ErrorInfo error=new ErrorInfo();
		//modify by FLY 提现回退
		User.rollbackWithdrawal(568,0D,error);
		error.clear();
		List<FundraiseingBid> raiseings = Bid.queryBid(Constants.V_FUNDRAISEING,error);
		if(raiseings.size()>2){
			flag=true;
		}
		if(raiseings.size()<=2&&flag){
			BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
			String content="平台借款标已不足2个，请尽快安排发标！";
			SMSUtil.sendSMS(backstageSet.publishBid1, content, error);
			SMSUtil.sendSMS(backstageSet.publishBid2, content, error);
			flag=false;
		}
	}

}
