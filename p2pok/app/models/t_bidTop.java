package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * 投标王model
 * @author cp
 */
@Entity
public class t_bidTop extends Model {
	
	
	public String name;
	
	public String bidMobile;
	
	//投标次数
	public double bidCount;
		
	//投标金额
	public double sumBidAmount;
	

	public t_bidTop() {
	}


}
