package jobs;

import javax.persistence.EntityManager;

import play.Logger;
import play.db.jpa.JPA;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;
import business.TemplateEmail;
import business.TemplateSms;
import business.TemplateStation;

/**
 * 备份并删除已发送的短信，执行时间：04：30
 *
 * @author hys
 * @createDate  2015年5月26日 下午5:13:32
 *
 */
@On("0 30 4 * * ?")
public class SmsBakupAndClear extends Job {

	public void doJob() {
		if(constants.Constants.ISJOB==1){
		EntityManager em = JPA.em();

		String bakupSql = "insert into t_system_mobile_sms_send (mobile, body) select mobile, body from t_system_mobile_sms_sending where is_sent = 1";
		try {
			em.createNativeQuery(bakupSql).executeUpdate();
		} catch (Exception e) {
			Logger.error("备份已发送的短信时，%s", e.getMessage());
		}
		
		String clearSql = "delete from t_system_mobile_sms_sending where is_sent = 1";
		try {
			em.createNativeQuery(clearSql).executeUpdate();
		} catch (Exception e) {
			Logger.error("删除已发送的短信时，%s", e.getMessage());
		}
		}
	}
}
