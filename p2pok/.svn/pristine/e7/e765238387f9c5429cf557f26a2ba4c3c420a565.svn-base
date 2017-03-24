package payment.hf.util;

import java.util.Map;
import models.t_return_data;
import models.t_user_recharge_details;

public class HFServiceFee {
	public static Map<String, String> getServiceFee(Map<String, String> map){
		t_return_data retur=new t_return_data();
		String sql = "select data from t_return_data where orderNum = ?";
		String data= retur.find(sql, map.get("OrdId")).first();
		map=HfPaymentUtil.jsonToMap(data);				
		return map;
	}

}
