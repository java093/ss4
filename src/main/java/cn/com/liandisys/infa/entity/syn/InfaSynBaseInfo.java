package cn.com.liandisys.infa.entity.syn;

/**
 * 同步用InforMatica基本信息
 * @author gaoyh
 *
 */
public class InfaSynBaseInfo {
	private long infaId;
	private long dbId;
	private long workflowId;
	private long runId;
	private long tasklogid;
	private long subTasklogid;
	/**
	 * @return the infaId
	 */
	public long getInfaId() {
		return infaId;
	}
	/**
	 * @param infaId the infId to set
	 */
	public void setInfaId(long infaId) {
		this.infaId = infaId;
	}
	/**
	 * @return the dbId
	 */
	public long getDbId() {
		return dbId;
	}
	/**
	 * @param dbId the dbId to set
	 */
	public void setDbId(long dbId) {
		this.dbId = dbId;
	}
	/**
	 * @return the workflowId
	 */
	public long getWorkflowId() {
		return workflowId;
	}
	/**
	 * @param workflowId the workflowId to set
	 */
	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}
	/**
	 * @return the runId
	 */
	public long getRunId() {
		return runId;
	}
	/**
	 * @param runId the runId to set
	 */
	public void setRunId(long runId) {
		this.runId = runId;
	}
	/**
	 * 有效状态判断
	 * @return true：有效
	 */
	public boolean isValid() {
		return !(0 == infaId || 0 == dbId || 0 == workflowId || 0 == runId);
	}
	/**
	 * @return the tasklogid
	 */
	public long getTasklogid() {
		return tasklogid;
	}
	/**
	 * @param tasklogid the tasklogid to set
	 */
	public void setTasklogid(long tasklogid) {
		this.tasklogid = tasklogid;
	}
	/**
	 * @return the subTasklogid
	 */
	public long getSubTasklogid() {
		return subTasklogid;
	}
	/**
	 * @param subTasklogid the subTasklogid to set
	 */
	public void setSubTasklogid(long subTasklogid) {
		this.subTasklogid = subTasklogid;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InfaSynBaseInfo [infaId=" + infaId + ", dbId=" + dbId
				+ ", workflowId=" + workflowId + ", runId=" + runId
				+ ", tasklogid=" + tasklogid + ", subTasklogid=" + subTasklogid
				+ "]";
	}
}
