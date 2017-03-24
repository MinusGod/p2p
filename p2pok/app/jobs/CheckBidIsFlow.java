package jobs;

import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import business.Bid;
import constants.Constants;

/**
 * 每29分钟检查一次是否流标
 * 
 * @author bsr
 * @version 6.0
 * @created 2014-8-1 上午10:32:52
 */
@Every("29min")
public class CheckBidIsFlow extends Job {

	@Override
	public void doJob() throws Exception {
		if(constants.Constants.ISJOB==1){
		if(Constants.IS_FLOW_BID) {
			Logger.info("--------------检查是否流标,开始---------------------");
			Bid.checkBidIsFlow();
			Logger.info("--------------检查是否流标,结束---------------------");
		}
		
		if(Constants.IPS_ENABLE && Constants.TRUST_FUNDS_TYPE.equals("HX")){
			Bid.verifyToFail();
		}
		}
	}
}
