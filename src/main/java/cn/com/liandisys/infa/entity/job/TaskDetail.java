package cn.com.liandisys.infa.entity.job;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.liandisys.infa.entity.IdEntity;
import cn.com.liandisys.infa.web.job.TaskController;

public class TaskDetail extends IdEntity{
	private static Logger logger = LoggerFactory.getLogger(TaskController.class);
	private String  TASKNAME;
	private String ID; 
	private BigDecimal TASK_ID;
	private BigDecimal WORKFLOWID;
	private BigDecimal SUBTASKID;
	private BigDecimal ORDERS;
	private String ALIAS;
	private String EXPLAIN;
    private String NAME;
    private String WORKFLOWEXPLAIN;
    private String TASKEXPLAIN;
    
 	public String getWORKFLOWEXPLAIN() {
		return WORKFLOWEXPLAIN;
	}
	public void setWORKFLOWEXPLAIN(String wORKFLOWEXPLAIN) {
		WORKFLOWEXPLAIN = wORKFLOWEXPLAIN;
	}
	public String getTASKEXPLAIN() {
		return TASKEXPLAIN;
	}
	public void setTASKEXPLAIN(String tASKEXPLAIN) {
		TASKEXPLAIN = tASKEXPLAIN;
	}
	public String getTASKNAME() {
		return TASKNAME;
	}
	public void setTASKNAME(String tASKNAME) {
		TASKNAME = tASKNAME;
	}
	private String getNAME(){
//		logger.info("TASKNAME:"+getTASKNAME()+"WORKFLOWNAME:"+getALIAS());
// 		 if(null==getTASKNAME()){
// 			 return getALIAS();
// 		 }else if(null==getALIAS()){
// 			 return getTASKNAME();
// 		 }
// 		 return "";
		logger.info("ALIAS"+getALIAS());
		return getALIAS();
 	 }
	public String getALIAS() {
		if(null!=getTASKNAME()){
			return getTASKNAME();
		}else{
            return	ALIAS;		
		}
//		logger.info("TASKNAME:"+getTASKNAME());
//		return ALIAS;
	}
	public void setALIAS(String aLIAS) {
		ALIAS = aLIAS;
	}
	public String getEXPLAIN() {
		logger.info("taskexplain:"+getTASKEXPLAIN()+"workflowexplain:"+getWORKFLOWEXPLAIN());
		if(null==getTASKEXPLAIN()){
			 return getWORKFLOWEXPLAIN();
		 }else if(null==getWORKFLOWEXPLAIN()){
			 return getTASKEXPLAIN();
		 }
		return "";
	}

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}

	public BigDecimal getTASK_ID() {
		return TASK_ID;
	}
	public void setTASK_ID(BigDecimal tASK_ID) {
		TASK_ID = tASK_ID;
	}
	public BigDecimal getWORKFLOWID() {
		return WORKFLOWID;
	}
	public void setWORKFLOWID(BigDecimal wORKFLOWID) {
		WORKFLOWID = wORKFLOWID;
	}
	public BigDecimal getSUBTASKID() {
		return SUBTASKID;
	}
	public void setSUBTASKID(BigDecimal sUBTASKID) {
		SUBTASKID = sUBTASKID;
	}
	public BigDecimal getORDERS() {
		return ORDERS;
	}
	public void setORDERS(BigDecimal oRDERS) {
		ORDERS = oRDERS;
	}


}
