package cn.com.liandisys.infa.entity.job;

import java.math.BigDecimal;

public class TaskPlan {
	private String ID; 
	private BigDecimal TASKID;
	private BigDecimal PLANID;
	private String NAME;
	private String TYPE;
	private String EXPLAIN;
	

	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getEXPLAIN() {
		return EXPLAIN;
	}
	public void setEXPLAIN(String eXPLAIN) {
		EXPLAIN = eXPLAIN;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public BigDecimal getTASKID() {
		return TASKID;
	}
	public void setTASKID(BigDecimal tASKID) {
		TASKID = tASKID;
	}
	public BigDecimal getPLANID() {
		return PLANID;
	}
	public void setPLANID(BigDecimal pLANID) {
		PLANID = pLANID;
	}
	
}
