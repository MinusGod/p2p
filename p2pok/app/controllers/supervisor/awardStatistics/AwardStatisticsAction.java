package controllers.supervisor.awardStatistics;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.t_bidTop;
import models.v_count_top;
import models.v_speed_top;
import payment.PaymentProxy;
import payment.hf.service.HfPaymentService;
import play.db.jpa.JPA;
import play.mvc.With;
import utils.CaptchaUtil;
import utils.ErrorInfo;
import utils.PageBean;
import business.AgentPayment;
import business.User;
import constants.Constants;
import constants.IPSConstants;
import constants.SQLTempletes;
import constants.IPSConstants.AgentPayStatus;
import controllers.SubmitRepeat;
import controllers.interceptor.AccountInterceptor;
import controllers.supervisor.SupervisorController;

@With({SubmitRepeat.class})
public class AwardStatisticsAction extends SupervisorController {
	
    /**
     * 速度王
     */
	public static void speedTop(){
		ErrorInfo error = new ErrorInfo(); 
	    error.clear();		
		List<v_speed_top> speedTopes = null;
		StringBuffer sql = new StringBuffer("");
		sql.append(SQLTempletes.V_SPEED_TOP);
		String bidTitle=params.get("bidTitle");
		String startTime=params.get("startTime");
		String serverFlag=params.get("serverFlag");
		
		if("true".equals(serverFlag)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			startTime=sdf.format(System.currentTimeMillis());
			render(startTime);
		}
		System.out.println(params.get("serverFlag"));
		if(bidTitle==null||"".equals(bidTitle)){
			
			render(bidTitle,startTime);
		}
		
		if(startTime==null||"".equals(startTime)){
			render(bidTitle,startTime);
		}		
		sql.append(" AND  b.title = '"+params.get("bidTitle")+"' ");
		sql.append(" AND  i.time >= '"+params.get("startTime")+"' ");
		sql.append(" ORDER BY i.time , i.id asc ");
		System.out.println(sql);
		try {
			 EntityManager em = JPA.em();
			 Query query = em.createNativeQuery(sql.toString(), v_speed_top.class);
			 speedTopes = query.getResultList();
			    
		} catch (Exception e) {
			e.printStackTrace();
			error.code = -1;
			error.msg = "数据库异常";
			
		}
		
		error.code=0;
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		Integer index=0;
		render(speedTopes,index,bidTitle,startTime);
	}
	/**
	 * 投标王统计
	 */
	public static void bidTop(){
		String beginTime = params.get("beginTime");
		String endTime = params.get("endTime");
		ErrorInfo error = new ErrorInfo(); 
		PageBean<t_bidTop> page = User.queryBidTop(beginTime,endTime,error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		render(page);
	}
	
    /**
     * 粉丝王
     * */
	public static void countTop(){
		
		String beginTime = params.get("beginTime");
		String endTime = params.get("endTime");
		String curPage = params.get("currPage");
		String pageSize = params.get("pageSize");
		String orderType = params.get("orderType");	
		
		
		ErrorInfo error = new ErrorInfo(); 
		
		PageBean<v_count_top> page = User.queryCountTop(beginTime,endTime,curPage,pageSize,orderType,error);
		
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		render(page);
	}
	
	/**
	 * 推送红包
	 */
	public static void voucherSend(){
		String mobile = params.get("mobile");
		User user = new User();
		user.findUserByMobile(mobile);
		String uuid = CaptchaUtil.getUUID(); // 防重复提交UUID
		render(user, uuid);
	}
	
	/**
	 * 推送红包
	 */
	public static void voucherConfirm(){
		String userId = params.get("userId");
		String amount = params.get("amount");
		//String uuid = params.get("uuid");
		ErrorInfo error = new ErrorInfo();
		/* 防重复提交 */
		/*if(!CaptchaUtil.checkUUID(uuid)){
			error.msg = "请求已提交或请求超时!";		
			renderJSON(error);
		}*/
		
		User user = new User();
		user.id = Long.valueOf(userId);
		String merOrderNo = user.createBillNo();
		AgentPayment.insertUserAgentPay(user, merOrderNo, merOrderNo,
			Double.valueOf(amount), AgentPayStatus.AGENT_SUBMIT, 0, error);
		if (error.code < 0) {
			renderJSON(error);
		}
		
		if (user.ipsAcctNo == null || "".equals(user.ipsAcctNo)) {
			error.msg = "该用户未开通第三方账户，推送红包失败！";
			renderJSON(error);
		}
		PaymentProxy.getInstance().agentRecharge(error, Constants.PC,    
		          user.ipsAcctNo, Double.valueOf(amount), merOrderNo, "");
		if (error.code < 0) {
			renderJSON(error);
		}
		error.msg = "发放现金红包成功！";
		renderJSON(error);
	}
	
	
}
