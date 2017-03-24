package jobs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.BasicInfos;
import models.UserInfo;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.On;
import utils.ExcelStatistic;
import constants.SQLTempletes;
@On("0 10 0 * * ?")
public class ExportExcels extends Job{
	public void doJob(){
		 List<BasicInfos> list=new ArrayList<BasicInfos>();
		 EntityManager em = JPA.em();
		 Query query = em.createNativeQuery(SQLTempletes.V_ERP_INFO, BasicInfos.class);
		 list = query.getResultList();
		 String[] titles   ={ "起息日期","到期收益期","姓名","返利模式","投资金额",
				              "封闭期","利率","实际利率","总利息","每期收益",
				              "本息","部门","客户服务人员","推荐人电话","主管",
				              "经理","合同签署方式","公司活动","汇入银行",
				              "收款类型","POST次数","POST金额","用户ID",
				              "协议号","合同号","收款账号","收款银行",
				              "身份证","性别","年龄","职业","手机号","邮箱",
				              "通讯地址","身份地址","推荐人ID","借款标题","真实姓名"};
		 String[] fieldNames={"startTime","endTime","userName","interestType","investAmount",
				              "stages","intetrest","realInetrest","inetetrestAmount","payInetrest",
				              "tatalAmount","partment","commissioner","recomMobile","groupLeader",
				              "manager","contractType","action","inBank",
		                      "receiveType","posNumber","posMoney","userId",
		                      "agreementId","contractId","receiveAccount","receiveBank",
		                      "indentityid","sex","age","profession","mobile","mail",
		                      "address","addressId","recomId","bidTitle","realName"};
		 ExcelStatistic.export("ERP", list, titles, fieldNames);
	 
		 List<UserInfo> list1 = new ArrayList<UserInfo>();
		 Query query1 = em.createNativeQuery(SQLTempletes.USER_INFO, UserInfo.class);
		 list1 = query1.getResultList();
		 String[] titleUser = {"注册时间", "用户名", "邮箱", "真实姓名", "手机", "身份证", 
				               "地址", "推荐时间", "推荐ID", "汇付账号"};
		 String[] fields = {"time", "name", "email", "reality_name", 
				            "mobile", "id_number", "address", "recommend_time", 
				              "recommend_user_id", "ips_acct_no" };
		 ExcelStatistic.export("user", list1, titleUser, fields);
	}

}
