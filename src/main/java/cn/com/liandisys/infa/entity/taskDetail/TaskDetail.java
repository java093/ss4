package cn.com.liandisys.infa.entity.taskDetail;

import java.math.BigDecimal;

import cn.com.liandisys.infa.entity.IdEntity;

public class TaskDetail extends IdEntity{
	private BigDecimal task_ID;	//任务ID
	private BigDecimal workflowID;	//worlflowID
	private BigDecimal subtaskID;	//子任务ID
	private BigDecimal orders;	//排序
	public BigDecimal getTask_ID() {
		return task_ID;
	}
	public void setTask_ID(BigDecimal task_ID) {
		this.task_ID = task_ID;
	}
	public BigDecimal getWorkflowID() {
		return workflowID;
	}
	public void setWorkflowID(BigDecimal workflowID) {
		this.workflowID = workflowID;
	}
	public BigDecimal getSubtaskID() {
		return subtaskID;
	}
	public void setSubtaskID(BigDecimal subtaskID) {
		this.subtaskID = subtaskID;
	}
	public BigDecimal getOrders() {
		return orders;
	}
	public void setOrders(BigDecimal orders) {
		this.orders = orders;
	}
	
}
