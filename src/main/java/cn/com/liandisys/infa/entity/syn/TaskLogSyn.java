package cn.com.liandisys.infa.entity.syn;

import java.sql.Timestamp;

import cn.com.liandisys.infa.entity.sys.InfaDetail;

public class TaskLogSyn extends InfaDetail {

	private Timestamp start_time;
	private	String sub_tasklogid;
	private Timestamp sub_end_time;
	private String sub_code;
	private Timestamp wf_endtime;
	private String wf_code;
	
	public Timestamp getStart_time() {
		return start_time;
	}
	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}
	public String getSub_tasklogid() {
		return sub_tasklogid;
	}
	public void setSub_tasklogid(String sub_tasklogid) {
		this.sub_tasklogid = sub_tasklogid;
	}
	public Timestamp getSub_end_time() {
		return sub_end_time;
	}
	public void setSub_end_time(Timestamp sub_end_time) {
		this.sub_end_time = sub_end_time;
	}

	public String getSub_code() {
		return sub_code;
	}
	public void setSub_code(String sub_code) {
		this.sub_code = sub_code;
	}
	public Timestamp getWf_endtime() {
		return wf_endtime;
	}
	public void setWf_endtime(Timestamp wf_endtime) {
		this.wf_endtime = wf_endtime;
	}
	public String getWf_code() {
		return wf_code;
	}
	public void setWf_code(String wf_code) {
		this.wf_code = wf_code;
	}
	
}
