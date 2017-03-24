package jobs;

import javax.persistence.EntityManager;

import play.Logger;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.On;

/**
 * 每天备份并删除已发送的邮件，执行时间：04：00
 *
 * @author hys
 * @createDate  2015年5月26日 下午5:13:32
 *
 */
@On("0 0 4 * * ?")
public class EmailBakupAndClear extends Job {

	public void doJob() {
		if(constants.Constants.ISJOB==1){
		EntityManager em = JPA.em();

		String bakupSql = "insert into t_system_email_send (email, title, body) select email, title, body from t_system_email_sending where is_sent = 1";
		try {
			em.createNativeQuery(bakupSql).executeUpdate();
		} catch (Exception e) {
			Logger.error("备份已发送的邮件时，%s", e.getMessage());
		}
		
		String clearSql = "delete from t_system_email_sending where is_sent = 1";
		try {
			em.createNativeQuery(clearSql).executeUpdate();
		} catch (Exception e) {
			Logger.error("删除已发送的邮件时，%s", e.getMessage());
		}
		}
	}
}