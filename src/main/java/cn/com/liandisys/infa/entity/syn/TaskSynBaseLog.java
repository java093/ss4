package cn.com.liandisys.infa.entity.syn;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.com.liandisys.infa.entity.sys.InfaDetail;

public class TaskSynBaseLog extends InfaDetail {

	private String taskLogID;
	private String taskID;
	private String taskRunType;
	private String taskmail_id;
	private List<String> subtasklogID;
	private List<String> subtaskID;
	private List<Timestamp> subend_time;
	private List<String> subtaskRunType;
	private List<String> subtaskmail_id;
	private Map<String, List<String>> runID;
//	private Map<String, String> runID;
	
	public List<Timestamp> getSubend_time() {
		return subend_time;
	}
	public void setSubend_time(List<Timestamp> subend_time) {
		this.subend_time = subend_time;
	}
	public String getTaskLogID() {
		return taskLogID;
	}
	public void setTaskLogID(String taskLogID) {
		this.taskLogID = taskLogID;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getTaskRunType() {
		return taskRunType;
	}
	public void setTaskRunType(String taskRunType) {
		this.taskRunType = taskRunType;
	}
	public String getTaskmail_id() {
		return taskmail_id;
	}
	public void setTaskmail_id(String taskmail_id) {
		this.taskmail_id = taskmail_id;
	}
	public List<String> getSubtasklogID() {
		return subtasklogID;
	}
	public void setSubtasklogID(List<String> subtasklogID) {
		this.subtasklogID = subtasklogID;
	}
	public List<String> getSubtaskID() {
		return subtaskID;
	}
	public void setSubtaskID(List<String> subtaskID) {
		this.subtaskID = subtaskID;
	}
	public List<String> getSubtaskRunType() {
		return subtaskRunType;
	}
	public void setSubtaskRunType(List<String> subtaskRunType) {
		this.subtaskRunType = subtaskRunType;
	}
	public List<String> getSubtaskmail_id() {
		return subtaskmail_id;
	}
	public void setSubtaskmail_id(List<String> subtaskmail_id) {
		this.subtaskmail_id = subtaskmail_id;
	}
//	public Map<String, String> getRunID() {
//		return runID;
//	}
//	public void setRunID(Map<String, String> runID) {
//		this.runID = runID;
//	}
	public Map<String, List<String>> getRunID() {
		return runID;
	}
	public void setRunID(Map<String, List<String>> runID) {
		this.runID = runID;
	}
	
}
