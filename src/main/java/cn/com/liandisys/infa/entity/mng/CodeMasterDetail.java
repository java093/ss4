package cn.com.liandisys.infa.entity.mng;

import cn.com.liandisys.infa.entity.IdEntity;

public class CodeMasterDetail extends IdEntity {
	private String codetype;// 类型
	private String code;// 代码
	private String codevalue;// 代码值
	private String description;// 描述
	private String delflag;// 有效标记(0有效)
	private String mark;
	
	public String getCodetype() {
		return codetype;
	}
	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodevalue() {
		return codevalue;
	}
	public void setCodevalue(String codevalue) {
		this.codevalue = codevalue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDelflag() {
		return delflag;
	}
	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}

	public String getMark() {
		if("0".equals(delflag)){
			return "有效";
		}else{
			return "无效";
		}
	}
}
