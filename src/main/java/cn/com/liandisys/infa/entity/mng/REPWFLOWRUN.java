package cn.com.liandisys.infa.entity.mng;

public class REPWFLOWRUN {

     
     private String  run_status_code;
     private String  start_time ;
     private String  end_time;
     private String  run_count;
     private String  workflow_run_id;
     

	public String getRUN_STATUS_CODE() {
		return run_status_code;
	}
	public void setRUN_STATUS_CODE(String run_status_code) {
		this.run_status_code = run_status_code;
	}
	public String getSTART_TIME() {
		return start_time ;
	}
	public void setSTART_TIME(String start_time ) {
		this.start_time  = start_time ;
	}
	public String getEND_TIME() {
		return end_time;
	}
	public void setEND_TIME(String end_time) {
		this.end_time = end_time;
	}

	public String getRun_Count() {
		return run_count;
	}
	public void setRun_Count(String run_count) {
		this.run_count = run_count;
	}

	public String getWORKFLOW_RUN_ID() {
		return this.workflow_run_id;
	}
	public void setWORKFLOW_RUN_ID(String workflow_run_id) {
		this.workflow_run_id = workflow_run_id;
	}
     
}
