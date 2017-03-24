package payment.ips.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import payment.hf.util.HfPaymentUtil;
import play.Logger;
import play.mvc.Before;
import utils.DateUtil;
import utils.ErrorInfo;
import business.BackstageSet;
import business.Supervisor;

import com.shove.security.License;

import constants.OptionKeys;
import controllers.BaseController;
import controllers.supervisor.account.AccountAction;
import controllers.supervisor.login.LoginAction;
import controllers.supervisor.systemSettings.SoftwareLicensAction;

public class IpsPayService extends BaseController{
	@Before
	public static void initsuper() {
		try{
			if(HfPaymentUtil.vertiSign()) return;
			License.update(BackstageSet.getCurrentBackstageSet().registerCode);
			if(!(License.getDomainNameAllow() && License.getAdminPagesAllow())) {
				flash.put("error", "");
				SoftwareLicensAction.notRegister();
			}
		}catch (Exception e) {
			e.printStackTrace();
			Logger.info("");
			flash.put("error", "");
			SoftwareLicensAction.notRegister();
		}
		
		if (Supervisor.isLogin()) {
			return; 
		}
		
		LoginAction.loginInit();
	}
	
	@Before
	public static void loadsup() {
		if(HfPaymentUtil.vertiSign()) return;
		if (!Supervisor.isLogin()) {
			return;
		}
		
		renderArgs.put("supervisor", Supervisor.currSupervisor());
		renderArgs.put("systemOptions", BackstageSet.getCurrentBackstageSet());
	}
	
	
}
