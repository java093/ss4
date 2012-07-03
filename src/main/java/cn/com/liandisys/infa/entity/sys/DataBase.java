package cn.com.liandisys.infa.entity.sys;

import cn.com.liandisys.infa.entity.IdEntity;

public class DataBase extends IdEntity{

	private String title;//标题
	private String dbname;//数据库名称
	private String dbtype;//数据库类型
	private String username;//数据库用户名
	private String password;//数据库密码
	private String port;//端口号
	private String ip;//主机名
	private String infomatic_id; //INFOMATIC_ID
	private String dbuse; //INFOMATIC_ID
	private String server_name;//服务名称
	public String getServer_name() {
		return server_name;
	}
	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}
	public String getDbuse() {
		return dbuse;
	}
	public void setDbuse(String dbuse) {
		this.dbuse = dbuse;
	}
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	public String getDbtype() {
		return dbtype;
	}
	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getInfomatic_id() {
		return infomatic_id;
	}
	public void setInfomatic_id(String infomatic_id) {
		this.infomatic_id = infomatic_id;
	}
	
}
