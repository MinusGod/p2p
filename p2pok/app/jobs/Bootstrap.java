package jobs;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import payment.PaymentProxy;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.WS;
import business.BackstageSet;

import com.shove.security.Encrypt;

import constants.Constants;
import constants.IPSConstants;
import constants.IPSConstants.IPSOperation;

@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {
		    new BackstageSet();
		     
		    BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
		     
		    Play.configuration.setProperty("mail.smtp.host",backstageSet.emailWebsite);
		    Play.configuration.setProperty("mail.smtp.user",backstageSet.mailAccount);
		   Play.configuration.setProperty("mail.smtp.pass",backstageSet.mailPassword);
		    
		    if(Constants.IPS_ENABLE){
		    	this.initPayment();
		    }
    }
    
    /**
     * 初始化支付网关
     */
    private void initPayment(){
    	PaymentProxy.getInstance().init();
    }
 
}