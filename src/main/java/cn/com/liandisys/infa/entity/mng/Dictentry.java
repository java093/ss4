package cn.com.liandisys.infa.entity.mng;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.liandisys.infa.entity.IdEntity;

public class Dictentry extends IdEntity {
	private String dict_entry;// 字典条目
	private String old_dict_entry;// 原字典条目
	private String add_enable;// 可否增加子项
	private String entry_name;// 条目名称
	private String access_level;// 存取级别
	private String dict_type;// 字典类型
	private String defunct_ind;// 有效标志
	private String remarks;// 备注
	private Long created_by;// 创建人
	private String createdBy;
	private Date created_datetime;// 创建时间
	private String crteat_time;
	private String mark;
	private String addable;

	public String getDict_entry() {
		return dict_entry;
	}

	public void setDict_entry(String dict_entry) {
		this.dict_entry = dict_entry;
	}

	public String getOld_dict_entry() {
		return old_dict_entry;
	}

	public void setOld_dict_entry(String old_dict_entry) {
		this.old_dict_entry = old_dict_entry;
	}

	public String getAdd_enable() {
		return add_enable;
	}

	public void setAdd_enable(String add_enable) {
		this.add_enable = add_enable;
	}

	public String getEntry_name() {
		return entry_name;
	}

	public void setEntry_name(String entry_name) {
		this.entry_name = entry_name;
	}

	public String getAccess_level() {
		return access_level;
	}

	public void setAccess_level(String access_level) {
		this.access_level = access_level;
	}

	public String getDict_type() {
		return dict_type;
	}

	public void setDict_type(String dict_type) {
		this.dict_type = dict_type;
	}

	public String getDefunct_ind() {
		return defunct_ind;
	}

	public void setDefunct_ind(String defunct_ind) {
		this.defunct_ind = defunct_ind;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Long created_by) {
		this.created_by = created_by;
	}
	
	public String getCreatedBy() {
		if (!"".equals(createdBy) && null != createdBy) {
			return createdBy;
		} else {
			return created_by.toString();
		}
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCrteat_time() {
		String datetime = "";
		if (null != getCreated_datetime()) {
			datetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
					.format(getCreated_datetime());
		}
		return datetime;
	}

	public Date getCreated_datetime() {
		return created_datetime;
	}

	public void setCreated_datetime(Date created_datetime) {
		this.created_datetime = created_datetime;
	}

	public String getMark() {
		if("Y".equals(defunct_ind)){
			return "有效";
		}else{
			return "无效";
		}
	}

	public String getAddable() {
		if("1".equals(add_enable)){
			return "是";
		}else{
			return "否";
		}
	}
	
	
}
