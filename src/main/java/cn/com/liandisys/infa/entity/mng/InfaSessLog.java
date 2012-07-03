package cn.com.liandisys.infa.entity.mng;

import java.sql.Timestamp;

public class InfaSessLog {
	private long id;
	private String subjectArea;
	private long subjectId;
	private String sessionName;
	private long sessionId;
	private String sessionInstanceName;
	private long successfulRows;
	private long failedRows;
	private long successfulSourceRows;
	private long failedSourceRows;
	private long firstCode;
	private String firstMsg;
	private long lastCode;
	private String lastError;
	private long runCode;
	private Timestamp actualStart;
	private Timestamp sessionTimestamp;
	private String sessionFile;
	private String badLocation;
	private long taskNumber;
	private long workflowNumber;
	private String workflowName;
	private String mappingName;
	private long totalErr;
	private long workflowId;
	private long workflowRunId;
	private long workletId;
	private long instanceId;
	private long infomaticId;
	private String logDetail;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSubjectArea() {
		return subjectArea;
	}
	public void setSubjectArea(String subjectArea) {
		this.subjectArea = subjectArea;
	}
	public long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public String getSessionInstanceName() {
		return sessionInstanceName;
	}
	public void setSessionInstanceName(String sessionInstanceName) {
		this.sessionInstanceName = sessionInstanceName;
	}
	public long getSuccessfulRows() {
		return successfulRows;
	}
	public void setSuccessfulRows(long successfulRows) {
		this.successfulRows = successfulRows;
	}
	public long getFailedRows() {
		return failedRows;
	}
	public void setFailedRows(long failedRows) {
		this.failedRows = failedRows;
	}
	public long getSuccessfulSourceRows() {
		return successfulSourceRows;
	}
	public void setSuccessfulSourceRows(long successfulSourceRows) {
		this.successfulSourceRows = successfulSourceRows;
	}
	public long getFailedSourceRows() {
		return failedSourceRows;
	}
	public void setFailedSourceRows(long failedSourceRows) {
		this.failedSourceRows = failedSourceRows;
	}
	public long getFirstCode() {
		return firstCode;
	}
	public void setFirstCode(long firstCode) {
		this.firstCode = firstCode;
	}
	public String getFirstMsg() {
		return firstMsg;
	}
	public void setFirstMsg(String firstMsg) {
		this.firstMsg = firstMsg;
	}
	public long getLastCode() {
		return lastCode;
	}
	public void setLastCode(long lastCode) {
		this.lastCode = lastCode;
	}
	public String getLastError() {
		return lastError;
	}
	public void setLastError(String lastError) {
		this.lastError = lastError;
	}
	public long getRunCode() {
		return runCode;
	}
	public void setRunCode(long runCode) {
		this.runCode = runCode;
	}
	public Timestamp getActualStart() {
		return actualStart;
	}
	public void setActualStart(Timestamp actualStart) {
		this.actualStart = actualStart;
	}
	public Timestamp getSessionTimestamp() {
		return sessionTimestamp;
	}
	public void setSessionTimestamp(Timestamp sessionTimestamp) {
		this.sessionTimestamp = sessionTimestamp;
	}
	public String getSessionFile() {
		return sessionFile;
	}
	public void setSessionFile(String sessionFile) {
		this.sessionFile = sessionFile;
	}
	public String getBadLocation() {
		return badLocation;
	}
	public void setBadLocation(String badLocation) {
		this.badLocation = badLocation;
	}
	public long getTaskNumber() {
		return taskNumber;
	}
	public void setTaskNumber(long taskNumber) {
		this.taskNumber = taskNumber;
	}
	public long getWorkflowNumber() {
		return workflowNumber;
	}
	public void setWorkflowNumber(long workflowNumber) {
		this.workflowNumber = workflowNumber;
	}
	public String getWorkflowName() {
		return workflowName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	public String getMappingName() {
		return mappingName;
	}
	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}
	public long getTotalErr() {
		return totalErr;
	}
	public void setTotalErr(long totalErr) {
		this.totalErr = totalErr;
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
	public long getWorkletId() {
		return workletId;
	}
	public void setWorkletId(long workletId) {
		this.workletId = workletId;
	}
	public long getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
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
		return "InfaSessLog [id=" + id + ", subjectArea=" + subjectArea
				+ ", subjectId=" + subjectId + ", sessionName=" + sessionName
				+ ", sessionId=" + sessionId + ", sessionInstanceName="
				+ sessionInstanceName + ", successfulRows=" + successfulRows
				+ ", failedRows=" + failedRows + ", successfulSourceRows="
				+ successfulSourceRows + ", failedSourceRows="
				+ failedSourceRows + ", firstCode=" + firstCode + ", firstMsg="
				+ firstMsg + ", lastCode=" + lastCode + ", lastError="
				+ lastError + ", runCode=" + runCode + ", actualStart="
				+ actualStart + ", sessionTimestamp=" + sessionTimestamp
				+ ", sessionFile=" + sessionFile + ", badLocation="
				+ badLocation + ", taskNumber=" + taskNumber
				+ ", workflowNumber=" + workflowNumber + ", workflowName="
				+ workflowName + ", mappingName=" + mappingName + ", totalErr="
				+ totalErr + ", workflowId=" + workflowId + ", workflowRunId="
				+ workflowRunId + ", workletId=" + workletId + ", instanceId="
				+ instanceId + ", infomaticId=" + infomaticId + ", logDetail="
				+ logDetail + "]";
	}
}
