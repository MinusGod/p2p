package jobs;

import java.util.List;

import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import utils.ErrorInfo;
import utils.SMSUtil;
import bean.FundraiseingBid;
import business.BackstageSet;
import business.Bid;
import constants.Constants;
@Every("60s")
public class NoticeFullBid extends Job{
	public void doJob(){
		ErrorInfo error=new ErrorInfo();
		List<FundraiseingBid> raiseings = Bid.queryBid(Constants.V_FUNDRAISEING,error);
		Long curTime=System.currentTimeMillis();
		BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
		Long time;
		for(FundraiseingBid bid:raiseings){
			time=bid.time.getTime()+172800000;
			
			if((time-5460000<=curTime&&curTime<=time-5400000)||(time-3660000<=curTime&&curTime<=time-3600000)||(time-2460000<=curTime&&curTime<=time-2400000)){
				String content=bid.title+"(J"+bid.id+")"+"借款标即将到期请及时关注借款标资金动态！";
				SMSUtil.sendSMS(backstageSet.fullBid, content, error);
			}
		}
		
	}

}
