package cn.com.liandisys.infa.entity.sys;

import cn.com.liandisys.infa.entity.IdEntity;

public class Informatica extends IdEntity{

	private String server_name; //服务器名称
	private String ip;//服务器IP
	private String port;//服务器端口
	private String domain;//域
	private String knowledge_base;//知识库
	private String integration_server;//集成服务器
	private String user_name;//用户名
	private String password;//密码
	private long db_id;
	private String wrlogpath;//workflow日志保存路径
	private String sesslogpath;//session日志保存路径
	public long getDb_id() {
		return db_id;
	}
	public void setDb_id(long db_id) {
		this.db_id = db_id;
	}
	public String getServer_name() {
		return server_name;
	}
	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getKnowledge_base() {
		return knowledge_base;
	}
	public void setKnowledge_base(String knowledge_base) {
		this.knowledge_base = knowledge_base;
	}
	public String getIntegration_server() {
		return integration_server;
	}
	public void setIntegration_server(String integration_server) {
		this.integration_server = integration_server;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getWrlogpath() {
		return wrlogpath;
	}
	public void setWrlogpath(String wrlogpath) {
		this.wrlogpath = wrlogpath;
	}
	public String getSesslogpath() {
		return sesslogpath;
	}
	public void setSesslogpath(String sesslogpath) {
		this.sesslogpath = sesslogpath;
	}
	
}
