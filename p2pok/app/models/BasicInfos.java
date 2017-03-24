package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;
/**
 * ERP基础数据实体
 * @author FLY
 *
 */
@Entity
public class BasicInfos extends Model{
	
	
	public String startTime;//起息日期
	public String endTime;//到期收益期
	public String userName;//姓名
	public String interestType;//返利模式
	public String investAmount;//投资金额
    public String stages;//封闭期
    public String intetrest;//利率
    public String realInetrest;//实际利率
    public String inetetrestAmount;//总利息
    public String payInetrest;//每期收益
    public String tatalAmount;//本息
    public String partment;//部门
    public String commissioner;//客户服务人员
    public String recomMobile;//推荐人电话
    public String groupLeader;//主管
    public String manager;//经理
    public String contractType;//合同签署方式
    public String action;//公司活动
    public String inBank;//汇入银行
    public String receiveType;//收款类型
    public String posNumber;//POST次数
    public String posMoney;//POST金额
    public String userId;//用户ID
    public String agreementId;//协议号
    public String contractId;//合同号
    public String receiveAccount;//收款账号
    public String receiveBank;//收款银行
    public String indentityid;//身份证
    public String sex;//性别
    public String age;//年龄
    public String profession;//职业
    public String mobile;//手机号
    public String mail;//邮箱
    public String address;//通讯地址
    public String addressId;//身份地址
    public String recomId;//推荐人ID
    public String bidTitle;//借款标题
    public String realName;//真实姓名
    
    
    
    
    

}
