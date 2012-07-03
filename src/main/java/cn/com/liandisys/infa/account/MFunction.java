package cn.com.liandisys.infa.account;

public class MFunction {

	private String name;
	private String code;
	private String FUNC_URL;
	private Long PARENT_ID;
	
	public Long getPARENT_ID() {
		return PARENT_ID;
	}
	public void setPARENT_ID(Long pARENT_ID) {
		PARENT_ID = pARENT_ID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFUNC_URL() {
		return FUNC_URL;
	}
	public void setFUNC_URL(String fUNC_URL) {
		FUNC_URL = fUNC_URL;
	}



}
