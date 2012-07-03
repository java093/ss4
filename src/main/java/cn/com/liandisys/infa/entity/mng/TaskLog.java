package cn.com.liandisys.infa.entity.mng;

import java.math.BigDecimal;
import java.util.Date;

import cn.com.liandisys.infa.entity.IdEntity;

public class TaskLog extends IdEntity {
	private BigDecimal task_id;

	private String run_status_code;
	private Date start_time;
	private Date end_time;
	private String detail;

	public BigDecimal getTask_id() {
		return task_id;
	}
	public void setTask_id(BigDecimal task_id) {
		this.task_id = task_id;
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
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TaskLog [task_id=" + task_id + ", run_status_code="
				+ run_status_code + ", start_time=" + start_time
				+ ", end_time=" + end_time + ", detail=" + detail + ", id="
				+ id + "]";
	}

}
