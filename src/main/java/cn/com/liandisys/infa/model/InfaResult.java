package cn.com.liandisys.infa.model;

import java.util.List;

public class InfaResult {
	private List rows;
	private int total;
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
