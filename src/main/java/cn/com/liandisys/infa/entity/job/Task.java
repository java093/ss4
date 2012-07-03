package cn.com.liandisys.infa.entity.job;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.liandisys.infa.entity.IdEntity;
public class Task extends IdEntity {
	private static Logger logger = LoggerFactory.getLogger(Task.class);
     private String  TASKNAME;
     private String  TYPE;
     private String  RUNTYPE;
     private String  MAIL_ID;
     private String  EXPLAIN;
     private Date CREATETIME;
     private Date UPDATETIME;
     private String  UPDATESERID;
     private String CRTTIME;
     private String UPTTIME;
     private String COMPLETEFLAG;
     private String MAILNAME;
     private String NAME; //主任务添加子任务时使用
	  	 
 	public String getNAME() {
 		return getTASKNAME();
 	}
     
     
     public String getMAILNAME() {
		return MAILNAME;
	}

	public void setMAILNAME(String mAILNAME) {
		MAILNAME = mAILNAME;
	}

	public String getCOMPLETEFLAG() {
		return COMPLETEFLAG;
	}

	public void setCOMPLETEFLAG(String cOMPLETEFLAG) {
		COMPLETEFLAG = cOMPLETEFLAG;
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

	public String getTASKNAME() {
		return TASKNAME;
	}
	public void setTASKNAME(String tASKNAME) {
		TASKNAME = tASKNAME;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getRUNTYPE() {
		return RUNTYPE;
	}
	public void setRUNTYPE(String rUNTYPE) {
		RUNTYPE = rUNTYPE;
	}
	public String getMAIL_ID() {
		return MAIL_ID;
	}
	public void setMAIL_ID(String mAIL_ID) {
		MAIL_ID = mAIL_ID;
	}
	public String getEXPLAIN() {
		return EXPLAIN;
	}
	public void setEXPLAIN(String eXPLAIN) {
		EXPLAIN = eXPLAIN;
	}

//	public String getCREATETIME() {
//		return CREATETIME;
//	}
//	public void setCREATETIME(String cREATETIME) {
//		CREATETIME = cREATETIME;
//	}
//	public String getUPDATETIME() {
//		return UPDATETIME;
//	}
//	public void setUPDATETIME(String uPDATETIME) {
//		UPDATETIME = uPDATETIME;
//	}
	
	
	
	public String getUPDATESERID() {
		return UPDATESERID;
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
	public void setUPDATESERID(String uPDATESERID) {
		UPDATESERID = uPDATESERID;
	}


	

	
     


     
     
}
