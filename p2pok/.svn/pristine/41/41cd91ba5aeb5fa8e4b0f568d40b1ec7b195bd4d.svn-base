package jobs;

import java.util.List;

import models.t_users;
import play.Logger;
import play.jobs.Job;
import play.jobs.On;
import utils.ErrorInfo;
import utils.SMSUtil;

/**
 * 发送担心通知平台所有用户
 * @author FLY
 */

@On("0 0 10 28 1 ? 2017")
public class ActionSMS extends Job {
	
	public void doJob() {
		List<t_users> list = t_users.findAll();
		ErrorInfo error = new ErrorInfo();
		int sucess = 0;
		int fail = 0;
		for (t_users user : list) {
		String content = "金鸡起舞辞旧岁，春回大地迎新年。感谢您一直以来对融友网的支持和厚爱，值此新春佳节之际，融友网全体员工给您拜年啦！恭祝幸福安康，万事如意，财源滚滚、鸡年大吉！";
		
		error.clear();
			try {
				SMSUtil.sendSMS(user.mobile, content, error);
				sucess++;
			} catch (Exception e) {
				Logger.info(user.name + "*****failed");
				fail++;
			}
		}
		
		Logger.info("sucess:" + sucess);
		Logger.info("fail:" + fail);
		
	}
	
}
