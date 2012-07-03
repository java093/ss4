package cn.com.liandisys.infa.entity.mng;

import java.io.Serializable;
import java.sql.Timestamp;

public class InfaWflowRun implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	private long id;
	/**
	 * SUBJECT_ID
	 */
	private long subjectId;
	/**
	 * WORKFLOW_ID
	 */
	private long workflowId;
	/**
	 * WORFLOW_RUN_ID
	 */
	private long workflowRunId;
	/**
	 * WORKFLOW_NAME
	 */
	private String workflowName;
	/**
	 * SERVER_ID
	 */
	private long serverId;
	/**
	 * SERVER_NAME
	 */
	private String serverName;
	/**
	 * START_TIME
	 */
	private Timestamp startTime;
	/**
	 * END_TIME
	 */
	private Timestamp endTime;
	/**
	 * LOG_FILE
	 */
	private String logFile;
	/**
	 * RUN_ERR_CODE
	 */
	private long runErrCode;
	/**
	 * RUN_ERR_MSG
	 */
	private String runErrMsg;
	/**
	 * RUN_STATUS_CODE
	 */
	private long runStatusCode;
	/**
	 * USER_NAME
	 */
	private String userName;
	/**
	 * RUN_TYPE
	 */
	private long runType;
	/**
	 * VERSION_NUMBER
	 */
	private long versionNumber;
	/**
	 * SUBJECT_AREA
	 */
	private String subjectArea;
	/**
	 * INFOMATIC_ID
	 */
	private long infomaticId;
	/**
	 * LOG_DETAIL
	 */
	private String logDetail;
	
	private long taskRunId;
	public long getTaskRunId() {
		return taskRunId;
	}
	public void setTaskRunId(long taskRunId) {
		this.taskRunId = taskRunId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	public long getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}
	public long getWorkflowRunId() {
		return workflowRunId;
	}
	public void setWorkflowRunId(long workflowRunId) {
		this.workflowRunId = workflowRunId;
	}
	public String getWorkflowName() {
		return workflowName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	public long getServerId() {
		return serverId;
	}
	public void setServerId(long serverId) {
		this.serverId = serverId;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public String getLogFile() {
		return logFile;
	}
	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}
	public long getRunErrCode() {
		return runErrCode;
	}
	public void setRunErrCode(long runErrCode) {
		this.runErrCode = runErrCode;
	}
	public String getRunErrMsg() {
		return runErrMsg;
	}
	public void setRunErrMsg(String runErrMsg) {
		this.runErrMsg = runErrMsg;
	}
	public long getRunStatusCode() {
		return runStatusCode;
	}
	public void setRunStatusCode(long runStatusCode) {
		this.runStatusCode = runStatusCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getRunType() {
		return runType;
	}
	public void setRunType(long runType) {
		this.runType = runType;
	}
	public long getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(long versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getSubjectArea() {
		return subjectArea;
	}
	public void setSubjectArea(String subjectArea) {
		this.subjectArea = subjectArea;
	}
	public long getInfomaticId() {
		return infomaticId;
	}
	public void setInfomaticId(long infomaticId) {
		this.infomaticId = infomaticId;
	}
	public String getLogDetail() {
		return logDetail;
	}
	public void setLogDetail(String logDetail) {
		this.logDetail = logDetail;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InfaWflowRun [id=" + id + ", subjectId=" + subjectId
				+ ", workflowId=" + workflowId + ", workflowRunId="
				+ workflowRunId + ", workflowName=" + workflowName
				+ ", serverId=" + serverId + ", serverName=" + serverName
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", logFile=" + logFile + ", runErrCode=" + runErrCode
				+ ", runErrMsg=" + runErrMsg + ", runStatusCode="
				+ runStatusCode + ", userName=" + userName + ", runType="
				+ runType + ", versionNumber=" + versionNumber
				+ ", subjectArea=" + subjectArea + ", infomaticId="
				+ infomaticId + ", logDetail=" + logDetail + ", taskRunId="
				+ taskRunId + "]";
	}
}
