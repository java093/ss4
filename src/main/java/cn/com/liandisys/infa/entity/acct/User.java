package cn.com.liandisys.infa.entity.acct;

import cn.com.liandisys.infa.entity.IdEntity;

public class User extends IdEntity{
	private String login_name;
	private String password;//为简化演示使用明文保存的密码
	private String name;
	private String email;
	
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

}
