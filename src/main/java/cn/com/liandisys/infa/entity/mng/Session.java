package cn.com.liandisys.infa.entity.mng;

import cn.com.liandisys.infa.entity.IdEntity;

public class Session extends IdEntity {
	private String subj_name; // 文件夹名称
	private String subj_id; // 文件夹ID
	private String session_name; // 会话名称
	private String session_id; // 会话ID
	private String workflow_name; // 工作流名称
	private String workflow_id; // 工作流ID
	private String mapping_name; // 映射名称
	private String mapping_id; // 映射ID
	private String check_level; // 检查层次
	private String check_level_desc;// 检查层次说明
	private String check_type; // 检查类型
	private String check_type_desc; // 检查类型说明
	private String default_ind; // 标识符

	public String getSubj_name() {
		return subj_name;
	}

	public void setSubj_name(String subj_name) {
		this.subj_name = subj_name;
	}

	public String getSubj_id() {
		return subj_id;
	}

	public void setSubj_id(String subj_id) {
		this.subj_id = subj_id;
	}

	public String getSession_name() {
		return session_name;
	}

	public void setSession_name(String session_name) {
		this.session_name = session_name;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getWorkflow_name() {
		return workflow_name;
	}

	public void setWorkflow_name(String workflow_name) {
		this.workflow_name = workflow_name;
	}

	public String getWorkflow_id() {
		return workflow_id;
	}

	public void setWorkflow_id(String workflow_id) {
		this.workflow_id = workflow_id;
	}

	public String getMapping_name() {
		return mapping_name;
	}

	public void setMapping_name(String mapping_name) {
		this.mapping_name = mapping_name;
	}

	public String getMapping_id() {
		return mapping_id;
	}

	public void setMapping_id(String mapping_id) {
		this.mapping_id = mapping_id;
	}

	public String getCheck_level() {
		return check_level;
	}

	public void setCheck_level(String check_level) {
		this.check_level = check_level;
	}

	public String getCheck_level_desc() {
		return check_level_desc;
	}

	public void setCheck_level_desc(String check_level_desc) {
		this.check_level_desc = check_level_desc;
	}

	public String getCheck_type() {
		return check_type;
	}

	public void setCheck_type(String check_type) {
		this.check_type = check_type;
	}

	public String getCheck_type_desc() {
		return check_type_desc;
	}

	public void setCheck_type_desc(String check_type_desc) {
		this.check_type_desc = check_type_desc;
	}

	public String getDefault_ind() {
		return default_ind;
	}

	public void setDefault_ind(String default_ind) {
		this.default_ind = default_ind;
	}

}
