package cn.com.liandisys.infa.entity.job;
/***quartz触发器关联的plan*/
public class QuartzPlan {
	private String type; // 计划类型
	private String time; //执行时间
	private String startedtime;//开始时间
	private String enddate;//结束时间
	private String  cron_expression;//CRON表达式
	private String  planid;
	private String  taskid;
	public String getPlanid() {
		return planid;
	}
	public void setPlanid(String planid) {
		this.planid = planid;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCron_expression() {
		return cron_expression;
	}
	public void setCron_expression(String cron_expression) {
		this.cron_expression = cron_expression;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getStartedtime() {
		return startedtime;
	}
	public void setStartedtime(String startedtime) {
		this.startedtime = startedtime;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

}
