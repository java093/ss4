package cn.com.liandisys.infa.entity.job;

import cn.com.liandisys.infa.entity.IdEntity;

public class QuartzTaskPlan extends IdEntity {

	private String taskid;
	private Long planid;
	private String triggersname;
	
	

	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Long getPlanid() {
		return planid;
	}
	public void setPlanid(Long planid) {
		this.planid = planid;
	}
	public String getTriggersname() {
		return triggersname;
	}
	public void setTriggersname(String triggersname) {
		this.triggersname = triggersname;
	}
}
