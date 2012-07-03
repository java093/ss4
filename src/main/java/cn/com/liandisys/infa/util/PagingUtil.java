package cn.com.liandisys.infa.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagingUtil {
	private List list;
	private int rows;
	private int page;
	private long total;

	/**
	 * 初始化一览数据
	 * 
	 * @param list
	 *            页面显示的数据
	 * @param rows
	 *            一页显示条数
	 * @param page
	 *            当前第几页
	 */
	public PagingUtil(List list, int rows, int page , long total) {
		this.list = list;
		this.rows = rows;
		this.page = page;
		this.total = total;
	}

	public static Map<String,Object> pageCondition(int page,int limit){
		Map<String, Object> pageCondition = new HashMap<String, Object>();
	    if(page>0){
	    	pageCondition.put("preNum", limit *(page - 1));
	    	pageCondition.put("backNum", limit *(page));
	    }
		return pageCondition;
	}
	
	public static Map<String,Object> pageCondition(int page,int limit, Object cond){
		Map<String, Object> pageCondition = pageCondition(page,limit);
        pageCondition.put("search", cond);
		return null;
	}
	
	
	public static Map<String,Object> getPage(List list,long total){
		Map<String, Object> page = new HashMap<String, Object>();	 
		page.put("total", total);
		page.put("rows",list); 
		return page;
	}
	
	
	
	public Map<String, Object> getList() {
		List showList = new ArrayList();
		Map<String, Object> result = new HashMap<String, Object>();
		if (list.size() <= rows) {
			result.put("total", total);
	        result.put("rows", list);
			return result;
		} else {
			if (list.size() > rows * page) {
				for (int i = 0; i < rows; i++) {
					showList.add(list.get(i + (rows * (page - 1))));
				}
			} else {
				for (int i = 0; i < rows-(rows * page - list.size()); i++) {
					showList.add(list.get(i + (rows * (page - 1))));
				}
			}
		}
		result.put("total", total);
        result.put("rows", showList);
		return result;
	}
}
