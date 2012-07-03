package cn.com.liandisys.infa.entity.plan;
import cn.com.liandisys.infa.entity.IdEntity;
public class Plan extends IdEntity {
	private String name;//	计划名称
	private String explain;//	说明.
	private String  type;//	计划类型
	private String  typeid;//	计划类型
	private String  time;//	计划时间
	private String  startedtime;//	开始计划日期
	private String  enddate;//	结束计划日期
	private String  day_plan;
	private String  day_plan_month;//	月计划天
	private String  start_flag;//	启动标志
	private String cron_expression; //cron 表达式
	private String updateuserid;//当前用户
	
	
	
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getUpdateuserid() {
		return updateuserid;
	}
	public void setUpdateuserid(String updateuserid) {
		this.updateuserid = updateuserid;
	}
	public String getCron_expression() {
		return cron_expression;
	}
	public void setCron_expression(String cron_expression) {
		this.cron_expression = cron_expression;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getStart_flag() {
		return start_flag;
	}
	public void setStart_flag(String start_flag) {
		this.start_flag = start_flag;
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
	public String getDay_plan() {
		return day_plan;
	}
	public void setDay_plan(String day_plan) {
		this.day_plan = day_plan;
	}
	public String getDay_plan_month() {
		return day_plan_month;
	}
	public void setDay_plan_month(String day_plan_month) {
		this.day_plan_month = day_plan_month;
	}



 
     
}
