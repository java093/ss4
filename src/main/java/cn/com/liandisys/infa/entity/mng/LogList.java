package cn.com.liandisys.infa.entity.mng;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.liandisys.infa.entity.IdEntity;
import cn.com.liandisys.infa.util.TimeUtil;

public class LogList extends IdEntity {
	private String name;
	private BigDecimal task_id;
	private String state;
	private String run_status_code;
	private Date start_time;
	private Date end_time;
	private String start;
	private String end;
	private String run_count;
	private String detail;
	//子任务中的父LOG ID
	private BigDecimal task_id_id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getTask_id() {
		return task_id;
	}
	public void setTask_id(BigDecimal task_id) {
		this.task_id = task_id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRun_status_code() {
		return run_status_code;
	}
	public void setRun_status_code(String run_status_code) {
		this.run_status_code = run_status_code;
	}
	public Date getStart_time() {
		return start_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public String getStart() {
		String datetime = "";
		if (null != getStart_time()) {
			datetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
					.format(getStart_time());
			return datetime;
		}else{
			return start;
		}
	}
	
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		String datetime = "";
		if (null != getEnd_time()) {
			datetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
					.format(getEnd_time());
			return datetime;
		}else{
			return end;
		}
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getRun_count() {
		if(run_count != "" && null != run_count){
			return TimeUtil.changetime(Long.valueOf(run_count.trim()));
		}
		return run_count;
	}
	public void setRun_count(String run_count) {
		this.run_count = run_count;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public BigDecimal getTask_id_id() {
		return task_id_id;
	}
	public void setTask_id_id(BigDecimal task_id_id) {
		this.task_id_id = task_id_id;
	}


}
