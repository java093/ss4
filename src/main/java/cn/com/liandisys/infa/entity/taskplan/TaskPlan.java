package cn.com.liandisys.infa.entity.taskplan;

import cn.com.liandisys.infa.entity.IdEntity;

public class TaskPlan extends IdEntity{
	private String taskID;
	private String planID;
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getPlanID() {
		return planID;
	}
	public void setPlanID(String planID) {
		this.planID = planID;
	}
}
