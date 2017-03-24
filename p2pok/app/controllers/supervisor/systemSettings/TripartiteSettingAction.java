package controllers.supervisor.systemSettings;

import org.apache.commons.lang.StringUtils;
import constants.SupervisorEvent;
import controllers.supervisor.SupervisorController;
import business.BackstageSet;
import business.DealDetail;
import business.Supervisor;
import play.db.jpa.JPA;
import utils.ErrorInfo;

/**
 * 第三方通道设置
 * 
 * @author bsr
 * 
 */
public class TripartiteSettingAction extends SupervisorController {
	/**
	 * 系统通知设置
	 */
	public static void saveNotice (){
		   String publishBid1=params.get("publishBid1");
		   String publishBid2=params.get("publishBid2");
		   String fullBid=params.get("fullBid");
		   String auditBid=params.get("auditBid");	
		   String payment1=params.get("payment1");
		   String payment2=params.get("payment2");
		   String dueover1=params.get("dueover1");
		   String dueover2=params.get("dueover2");
		   ErrorInfo error = new ErrorInfo();	        
	        if(StringUtils.isBlank(publishBid1)) {
	        	flash.error("手机号不能为空");	        	
	        	noticePassage();
	        }
	        
	        if(StringUtils.isBlank(publishBid2)) {
	        	flash.error("手机号不能为空");	        	
	        	noticePassage();
	        }
	        
	        if(StringUtils.isBlank(fullBid)) {
	        	flash.error("手机号不能为空");        	
	        	noticePassage();
	        }
	        
	        if(StringUtils.isBlank(auditBid)) {
	        	flash.error("手机号不能为空");        	
	        	noticePassage();
	        }
	        if(StringUtils.isBlank(payment1)) {
	        	flash.error("手机号不能为空");        	
	        	noticePassage();
	        }
	        if(StringUtils.isBlank(payment2)) {
	        	flash.error("手机号不能为空");        	
	        	noticePassage();
	        }
	        if(StringUtils.isBlank(dueover1)) {
	        	flash.error("手机号不能为空");        	
	        	noticePassage();
	        }
	        if(StringUtils.isBlank(dueover2)) {
	        	flash.error("手机号不能为空");        	
	        	noticePassage();
	        }
	        
			BackstageSet backstageSet = new BackstageSet();
			
			backstageSet.publishBid1 = publishBid1;
			backstageSet.publishBid2 = publishBid2;
			backstageSet.fullBid = fullBid;
			backstageSet.auditBid = auditBid;
			
			backstageSet.payment1 = payment1;
			backstageSet.payment2 = payment2;
			backstageSet.dueover1 = dueover1;
			backstageSet.dueover2 = dueover2;
			
			
	        backstageSet.noticeSystems(error);
			
			if(error.code < 0){
				flash.error(error.msg);
			}
			
			BackstageSet.setCurrentBackstageSet(backstageSet);
			
			flash.success(error.msg);
			
			noticePassage();
		
	}
	
	public static void noticePassage(){
        BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();		
		render(backstageSet);
		
	}

	/**
	 * 短信通道
	 */
	public static void SMSPassage() {
		BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
		
		render(backstageSet);
	}
	
	/**
	 * 保存短信通道
	 */
	public static void saveSMS(String smsAccount, String smsPassword) {
        ErrorInfo error = new ErrorInfo();
        
        if(StringUtils.isBlank(smsAccount)) {
        	flash.error("请填写短信通道用户名");
        	
        	SMSPassage();
        }
        
        if(StringUtils.isBlank(smsAccount)) {
        	flash.error("请填写短信通道密码");
        	
        	SMSPassage();
        }
        
		BackstageSet backstageSet = new BackstageSet();
		
		backstageSet.smsAccount = smsAccount;
		backstageSet.smsPassword = smsPassword;
		
        backstageSet.SMSChannels(error);
		
		if(error.code < 0){
			flash.error(error.msg);
		}
		
		BackstageSet.setCurrentBackstageSet(backstageSet);
		
		flash.success(error.msg);
		
		SMSPassage();
	}

	/**
	 * 邮件通道
	 */
	public static void mailPassage() {
		BackstageSet backstageSet = BackstageSet.getCurrentBackstageSet();
		
		render(backstageSet);
	}
	
	/**
	 * 保存邮件通道
	 */
	public static void saveMail() {
        ErrorInfo error = new ErrorInfo();
		
		BackstageSet backstageSet = new BackstageSet();
		
		backstageSet.mailAccount = params.get("mailAccount");
		backstageSet.mailPassword = params.get("mailPassword");
		backstageSet.emailWebsite = params.get("emailWebsite");
		backstageSet.POP3Server = params.get("POP3Server");
		backstageSet.STMPServer = params.get("STMPServer");
		String isChargesChannels = params.get("isChargesChannels");
		String value = isChargesChannels == null ? "0" : "1";
		backstageSet.isChargesChannels = value;
		
        backstageSet.MAILChannels(error);
		
		if(error.code < 0){
			flash.error(error.msg);
		}
		
		flash.success(error.msg);
		
		DealDetail.supervisorEvent(Supervisor.currSupervisor().id, SupervisorEvent.MAIL_CHANNEL, 
				"修改短信通道设置", error);
		
		if (error.code < 0) {
			JPA.setRollbackOnly();
			
			return ;
		}
		
		BackstageSet.setCurrentBackstageSet(backstageSet);
		
		mailPassage();
	}
}
