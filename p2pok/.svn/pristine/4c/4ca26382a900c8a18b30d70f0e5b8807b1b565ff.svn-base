package jobs;

import java.util.List;

import models.t_bids;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import utils.ErrorInfo;
import utils.Security;
import bean.FundraiseingBid;
import business.Bid;
import constants.Constants;
import controllers.supervisor.bidManager.BidPlatformAction;
@Every("5min")
public class AutoAuditBids extends Job {
	public void doJob(){
		ErrorInfo error=new ErrorInfo();
		List<FundraiseingBid> raiseings = Bid.queryBid(Constants.V_FULL,error);
		Long currTime=System.currentTimeMillis();
		Logger.info("执行满标自动的审核!");
		for(FundraiseingBid bid:raiseings){
			Bid bid0 = new Bid();
			bid0.bidDetail = true;
			bid0.upNextFlag = Constants.BID_MBZ;
			bid0.id = bid.id;
			Long time=bid0.investExpireTime.getTime()-600000;
			if(currTime>=time){			
				autoAudit(bid0.sign);
				Logger.info("#######借款标:"+bid.title+"("+bid.id+")被自动满标放款审核！");
			}
		}
	}
	
	public void autoAudit(String sign){		
		ErrorInfo error = new ErrorInfo();
		long bidId = Security.checkSign(sign, Constants.BID_ID_SIGN, Constants.VALID_TIME, error);				
		Bid bid = new Bid();
		bid.auditBid = true;
		bid.id = bidId;
		bid.allocationSupervisorId = 1; // 审核人		
		bid.fundraiseToEaitLoan(error);
		
	}

}
