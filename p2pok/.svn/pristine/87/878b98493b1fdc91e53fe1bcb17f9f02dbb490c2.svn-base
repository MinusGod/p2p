package jobs;

import java.util.Date;
import java.util.List;

import models.t_bids;
import models.t_bills;
import models.t_dict_bid_repayment_types;

import org.apache.commons.lang.StringUtils;

import play.jobs.Job;
import play.jobs.On;
import utils.DataUtil;
import utils.DateUtil;
import utils.ErrorInfo;
import business.Bill;
import business.TemplateEmail;
import business.TemplateSms;
import business.User;
import constants.Templets;


@On("0 10 9 * * ?")
public class CheckBillJob  extends Job {
	@Override
	public void doJob() throws Exception {
		if(constants.Constants.ISJOB==1){
		Bill.checkBill();
		}
	}

}
