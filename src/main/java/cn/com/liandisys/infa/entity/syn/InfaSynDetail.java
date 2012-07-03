package cn.com.liandisys.infa.entity.syn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.liandisys.infa.entity.sys.InfaDetail;

public class InfaSynDetail extends InfaDetail {

	/** Infa详细信息 */
	private InfaDetail infaDetail;
	
	/** 同步用参数信息列表（workFlowID） */
	private List wfIdList = null;

	/** 同步用参数信息列表（workFlowRunID） */
	private List wfRunIdList = null;
	/** wf运行ID和主任务运行ID的对应关系 */
	private Map<String, String> relMap = new HashMap<String, String>();
	/** wf运行ID和子任务运行ID的对应关系 */
	private Map<String, String> subRelMap = new HashMap<String, String>();
	/**
	 * @return the infaDetail
	 */
	public InfaDetail getInfaDetail() {
		return infaDetail;
	}
	/**
	 * @param infaDetail the infaDetail to set
	 */
	public void setInfaDetail(InfaDetail infaDetail) {
		this.infaDetail = infaDetail;
	}
	/**
	 * @return the wfIdList
	 */
	public List getWfIdList() {
		return wfIdList;
	}
	/**
	 * @param wfIdList the wfIdList to set
	 */
	public void setWfIdList(List wfIdList) {
		this.wfIdList = wfIdList;
	}
	/**
	 * @return the wfRunIdList
	 */
	public List getWfRunIdList() {
		return wfRunIdList;
	}
	/**
	 * @param wfRunIdList the wfRunIdList to set
	 */
	public void setWfRunIdList(List wfRunIdList) {
		this.wfRunIdList = wfRunIdList;
	}
	
	public void addWfIdList(long wfId) {
		if (null == this.wfIdList) {
			this.setWfIdList(new ArrayList());
		}
		this.wfIdList.add(wfId);
	}
	
	public void addWrRunIdList(long wfRunId) {
		if (null == this.wfRunIdList) {
			this.setWfRunIdList(new ArrayList());
		}
		this.wfRunIdList.add(wfRunId);
	}
	
	public void addToRelMap(long runId, long taskRunid) {
		if (null == relMap) {
			this.relMap = new HashMap<String, String>();
		}
		relMap.put(String.valueOf(runId), String.valueOf(taskRunid));
	}
	
	public long getRel(long runId) {
		if (null == this.relMap) {
			return 0;
		}
		return Long.parseLong(relMap.get(String.valueOf(runId)));
	}
	
	public void addToSubRelMap(long runId, long taskRunid) {
		if (null == subRelMap) {
			this.subRelMap = new HashMap<String, String>();
		}
		subRelMap.put(String.valueOf(runId), String.valueOf(taskRunid));
	}
	
	public long getSubRel(long runId) {
		if (null == this.subRelMap) {
			return 0;
		}
		if (!subRelMap.containsKey(String.valueOf(runId))) {
			return 0;
		}
		return Long.parseLong(subRelMap.get(String.valueOf(runId)));
	}
}
