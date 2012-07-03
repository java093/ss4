package cn.com.liandisys.infa.entity.job;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.liandisys.infa.entity.IdEntity;
public class WorkFlow extends IdEntity {
	
	private String SUBJECT_AREA;
	private String WORKFLOW_NAME;
	private String SCHEDULER_NAME;
	private String START_TIME;
	private String END_TIME;
	private String RUN_COUNT;
	private String ALIAS;
	private String EXPLAIN;
	private Date CREATETIME;
	private Date UPDATETIME;
	private String UPDATEUSERID;
	private String NAME; //主任务添加workflow使用
	private Long DB_ID;
	private Double WORKFLOW_ID;
	private String CRTTIME; //页面展示时的生成时间
    private String UPTTIME; //页面展示时的修改时间
    private Long INFA_ID;
    private String INFAMATIC;
    
    
    
    public String getINFAMATIC() {
		return INFAMATIC;
	}

	public void setINFAMATIC(String iNFAMATIC) {
		INFAMATIC = iNFAMATIC;
	}

	public Long getINFA_ID() {
		return INFA_ID;
	}

	public void setINFA_ID(Long iNFA_ID) {
		INFA_ID = iNFA_ID;
	}

	public String getCRTTIME() {
   	 String datetime="";
   	 if(null!=getCREATETIME()){
   		datetime=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(getCREATETIME());
  	 }
		return datetime;
	}
    
    public String getUPTTIME() {
   	 String datetime="";
   	 if(null!=getUPDATETIME()){
   		datetime=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(getUPDATETIME());
  	 }
		return datetime;
	}
	
	public Double getWORKFLOW_ID() {
		return WORKFLOW_ID;
	}
	public void setWORKFLOW_ID(Double wORKFLOW_ID) {
		WORKFLOW_ID = wORKFLOW_ID;
	}


	public String getNAME() {
		return getALIAS();
	}
	
	
	public Long getDB_ID() {
		return DB_ID;
	}
	public void setDB_ID(Long dB_ID) {
		DB_ID = dB_ID;
	}
	public String getRUN_COUNT() {
		return RUN_COUNT;
	}
	public void setRUN_COUNT(String run_COUNT) {
		RUN_COUNT = run_COUNT;
	}
	public String getALIAS() {
		return ALIAS;
	}
	public void setALIAS(String aLIAS) {
		ALIAS = aLIAS;
	}
	public String getEXPLAIN() {
		return EXPLAIN;
	}
	public void setEXPLAIN(String eXPLAIN) {
		EXPLAIN = eXPLAIN;
	}
	
	public Date getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(Date cREATETIME) {
		CREATETIME = cREATETIME;
	}
	public Date getUPDATETIME() {
		return UPDATETIME;
	}
	public void setUPDATETIME(Date uPDATETIME) {
		UPDATETIME = uPDATETIME;
	}
	public String getUPDATEUSERID() {
		return UPDATEUSERID;
	}
	public void setUPDATEUSERID(String uPDATEUSERID) {
		UPDATEUSERID = uPDATEUSERID;
	}
	public String getSUBJECT_AREA() {
		return SUBJECT_AREA;
	}
	public void setSUBJECT_AREA(String sUBJECT_AREA) {
		SUBJECT_AREA = sUBJECT_AREA;
	}
	public String getWORKFLOW_NAME() {
		return WORKFLOW_NAME;
	}
	public void setWORKFLOW_NAME(String wORKFLOW_NAME) {
		WORKFLOW_NAME = wORKFLOW_NAME;
	}
	public String getSCHEDULER_NAME() {
		return SCHEDULER_NAME;
	}
	public void setSCHEDULER_NAME(String sCHEDULER_NAME) {
		SCHEDULER_NAME = sCHEDULER_NAME;
	}
	public String getSTART_TIME() {
		return START_TIME;
	}
	public void setSTART_TIME(String sTART_TIME) {
		START_TIME = sTART_TIME;
	}
	public String getEND_TIME() {
		return END_TIME;
	}
	public void setEND_TIME(String eND_TIME) {
		END_TIME = eND_TIME;
	}
   
     
}
