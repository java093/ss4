package cn.com.liandisys.infa.entity.job;

import java.math.BigDecimal;

public class TaskPlans {
	private String ID; 
	private BigDecimal TASKID;
	private BigDecimal PLANID;
	private String NAME;
	private String TYPE;
	private String EXPLAIN;
	private String TYPEID;
	private String STARTTIME;
	private String TIME;
	
	

	public String getTYPEID() {
		return TYPEID;
	}
	public void setTYPEID(String tYPEID) {
		TYPEID = tYPEID;
	}
	public String getSTARTTIME() {
		return STARTTIME;
	}
	public void setSTARTTIME(String sTARTTIME) {
		STARTTIME = sTARTTIME;
	}
	public String getTIME() {
		return TIME;
	}
	public void setTIME(String tIME) {
		TIME = tIME;
	}
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
