package cn.com.liandisys.infa.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;
import cn.com.liandisys.infa.model.InfaResult;

/**
 * Json工具类
 * 
 * @author jinxl
 * 
 */
public class JasonUtil {

	/**
	 * List对象转Json
	 * 
	 * @param list
	 * @return
	 */
	public static String JasonTOString(List list) {
		InfaResult rsult = new InfaResult();
		rsult.setTotal(list.size());
		rsult.setRows(list);
		return JSONObject.fromObject(rsult).toString();
	}
	
	/**
	 * 设定返回结果
	 * @param rsult(True:成功,False:失败)
	 * @return
	 */
	public static String getJsonResultEdit(Map map,boolean rsult) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (rsult) {
			resultMap.put("success", true);
		} else {
			resultMap.put("success", false);
		}
		
		Set keys = map.keySet();
		Iterator it = keys.iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			resultMap.put(key,map.get(key));
		}
		return JSONObject.fromObject(resultMap).toString();
	}

	/**
	 * 设定返回结果
	 * 
	 * @param rsult
	 *            (True:成功,False:失败)
	 * @return
	 */
	public static String getJsonResult(boolean rsult) {
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		if (rsult) {
			resultMap.put("success", true);
		} else {
			resultMap.put("success", false);
		}
		return JSONObject.fromObject(resultMap).toString();
	}

	/**
	 * 设定返回结果
	 * 
	 * @param rsult
	 * 
	 * @return
	 */
	public static String getJsonResult(String rsult) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("success", rsult);
		return JSONObject.fromObject(resultMap).toString();
	}

	/**
	 * 
	 * @param o
	 * @return
	 */
	public static String JasonTOString(Object o) {

		return JSONObject.fromObject(o).toString();
	}
}
