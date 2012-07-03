package cn.com.liandisys.infa.entity.mng;

import cn.com.liandisys.infa.entity.IdEntity;

public class LogRelation extends IdEntity {
	private String task_logid;
	private String sub_tasklogid;
	private String workflow_id;
	private String run_id;

	public String getTASK_LOGID() {
		return task_logid;
	}

	public void setTASK_LOGID(String task_logid) {
		this.task_logid = task_logid;
	}

	public String getSUB_TASKLOGID() {
		return sub_tasklogid;
	}

	public void setSUB_TASKLOGID(String sub_tasklogid) {
		this.sub_tasklogid = sub_tasklogid;
	}

	public String getWORKFLOW_ID() {
		return workflow_id;
	}

	public void setWORKFLOW_ID(String workflow_id) {
		this.workflow_id = workflow_id;
	}

	public String getRUN_ID() {
		return run_id;
	}

	public void setRUN_ID(String run_id) {
		this.run_id = run_id;
	}

}
