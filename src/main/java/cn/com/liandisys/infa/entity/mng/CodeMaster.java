package cn.com.liandisys.infa.entity.mng;

import cn.com.liandisys.infa.entity.IdEntity;

public class CodeMaster extends IdEntity {
	private String codetype;// 类型
	private String description;// 描述
	private String flag;// 有效标记(0有效)
	private String mark;

	public String getCodetype() {
		return codetype;
	}

	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMark() {
		if("0".equals(flag)){
			return "有效";
		}else{
			return "无效";
		}
	}
}
