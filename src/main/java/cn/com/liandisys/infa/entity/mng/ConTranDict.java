package cn.com.liandisys.infa.entity.mng;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.liandisys.infa.entity.IdEntity;

public class ConTranDict extends IdEntity {
	private String sou_code;// 源系统编号
	private String sou_desc;// 源系统说明
	private String sou_dict_code;// 源系统字典分类
	private String sou_dict_desc;// 源系统字典分类说明
	private String sou_sub_dict_code;// 源系统字典编码
	private String sou_sub_dict_desc;// 源系统字典编码说明
	private String tran_dict_code;// 统一字典分类
	private String tran_dict_desc;// 纺一字典分类说明
	private String tran_code;// 统一编码
	private String tran_desc;// 统一编码说明
	private String defunct_ind;// 有效标志
	private String remarks;// 备注
	private Long created_by;// 创建人
	private String createdBy;
	private Date created_datetime;// 创建时间
	private String crteat_time;
	private String mark;

	public String getSou_code() {
		return sou_code;
	}

	public void setSou_code(String sou_code) {
		this.sou_code = sou_code;
	}

	public String getSou_desc() {
		return sou_desc;
	}

	public void setSou_desc(String sou_desc) {
		this.sou_desc = sou_desc;
	}

	public String getSou_dict_code() {
		return sou_dict_code;
	}

	public void setSou_dict_code(String sou_dict_code) {
		this.sou_dict_code = sou_dict_code;
	}

	public String getSou_dict_desc() {
		return sou_dict_desc;
	}

	public void setSou_dict_desc(String sou_dict_desc) {
		this.sou_dict_desc = sou_dict_desc;
	}

	public String getSou_sub_dict_code() {
		return sou_sub_dict_code;
	}

	public void setSou_sub_dict_code(String sou_sub_dict_code) {
		this.sou_sub_dict_code = sou_sub_dict_code;
	}

	public String getSou_sub_dict_desc() {
		return sou_sub_dict_desc;
	}

	public void setSou_sub_dict_desc(String sou_sub_dict_desc) {
		this.sou_sub_dict_desc = sou_sub_dict_desc;
	}

	public String getTran_dict_code() {
		return tran_dict_code;
	}

	public void setTran_dict_code(String tran_dict_code) {
		this.tran_dict_code = tran_dict_code;
	}

	public String getTran_dict_desc() {
		return tran_dict_desc;
	}

	public void setTran_dict_desc(String tran_dict_desc) {
		this.tran_dict_desc = tran_dict_desc;
	}

	public String getTran_code() {
		return tran_code;
	}

	public void setTran_code(String tran_code) {
		this.tran_code = tran_code;
	}

	public String getTran_desc() {
		return tran_desc;
	}

	public void setTran_desc(String tran_desc) {
		this.tran_desc = tran_desc;
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

	public Date getCreated_datetime() {
		return created_datetime;
	}

	public void setCreated_datetime(Date created_datetime) {
		this.created_datetime = created_datetime;
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

	public String getMark() {
		if ("Y".equals(defunct_ind)) {
			return "有效";
		} else {
			return "无效";
		}
	}

}
