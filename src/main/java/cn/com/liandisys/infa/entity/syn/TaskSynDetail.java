package cn.com.liandisys.infa.entity.syn;

import java.sql.Timestamp;

import cn.com.liandisys.infa.entity.sys.InfaDetail;

public class TaskSynDetail extends InfaDetail {

	private String tasklog;
	private String task_id;
	private String task_runtype;
	private String taskmail_id;
	private String subtasklog;
	private String subtask_id;
	private Timestamp subend_time;
	private String subtask_runtype;
	private String subtaskmaile_id;
	private String run_id;
	public Timestamp getSubend_time() {
		return subend_time;
	}
	public void setSubend_time(Timestamp subend_time) {
		this.subend_time = subend_time;
	}
	public String getTasklog() {
		return tasklog;
	}
	public void setTasklog(String tasklog) {
		this.tasklog = tasklog;
	}
	
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getTask_runtype() {
		return task_runtype;
	}
	public void setTask_runtype(String task_runtype) {
		this.task_runtype = task_runtype;
	}
	public String getSubtasklog() {
		return subtasklog;
	}
	public void setSubtasklog(String subtasklog) {
		this.subtasklog = subtasklog;
	}
	public String getSubtask_id() {
		return subtask_id;
	}
	public void setSubtask_id(String subtask_id) {
		this.subtask_id = subtask_id;
	}
	public String getSubtask_runtype() {
		return subtask_runtype;
	}
	public void setSubtask_runtype(String subtask_runtype) {
		this.subtask_runtype = subtask_runtype;
	}
	public String getRun_id() {
		return run_id;
	}
	public void setRun_id(String run_id) {
		this.run_id = run_id;
	}
	public String getTaskmail_id() {
		return taskmail_id;
	}
	public void setTaskmail_id(String taskmail_id) {
		this.taskmail_id = taskmail_id;
	}
	public String getSubtaskmaile_id() {
		return subtaskmaile_id;
	}
	public void setSubtaskmaile_id(String subtaskmaile_id) {
		this.subtaskmaile_id = subtaskmaile_id;
	}
	
	
	
}
