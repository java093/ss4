package cn.com.liandisys.infa.entity.sys;


/**
 * Informatica详细信息
 * @author gaoyh
 *
 */
public class InfaDetail {
	/** Informatica  ID */
	private long infaId;
	/** Informatica  服务器IP */
	private String infaIp;
	/** Informatica  服务器端口号 */
	private String infaPort;
	/** Informatica  服务器端Domain */
	private String infaDomain;
	/** Informatica  服务器用户名 */
	private String infaUserName;
	/** Informatica  服务器密码 */
	private String infaPass;
	/** Informatica  知识库（数据库）ID */
	private long dbId;
	/** Informatica  知识库（数据库）IP */
	private String dbIp;
	/** Informatica  知识库（数据库）端口号 */
	private String dbPort;
	/** Informatica  知识库（数据库）名 */
	private String dbName;
	/** Informatica  知识库（数据库）用户名 */
	private String dbUser;
	/** Informatica  知识库（数据库）密码 */
	private String dbPass;
	/**	数据库类型  1：oracle，2：greenplum */
	private int dbType;
	/**
	 * @return the infaId
	 */
	public long getInfaId() {
		return infaId;
	}
	/**
	 * @param infaId the infaId to set
	 */
	public void setInfaId(long infaId) {
		this.infaId = infaId;
	}
	/**
	 * @return the infaIp
	 */
	public String getInfaIp() {
		return infaIp;
	}
	/**
	 * @param infaIp the infaIp to set
	 */
	public void setInfaIp(String infaIp) {
		this.infaIp = infaIp;
	}
	/**
	 * @return the infaPort
	 */
	public String getInfaPort() {
		return infaPort;
	}
	/**
	 * @param infaPort the infaPort to set
	 */
	public void setInfaPort(String infaPort) {
		this.infaPort = infaPort;
	}
	/**
	 * @return the infaDomain
	 */
	public String getInfaDomain() {
		return infaDomain;
	}
	/**
	 * @param infaDomain the infaDomain to set
	 */
	public void setInfaDomain(String infaDomain) {
		this.infaDomain = infaDomain;
	}
	/**
	 * @return the infaUserName
	 */
	public String getInfaUserName() {
		return infaUserName;
	}
	/**
	 * @param infaUserName the infaUserName to set
	 */
	public void setInfaUserName(String infaUserName) {
		this.infaUserName = infaUserName;
	}
	/**
	 * @return the infaPass
	 */
	public String getInfaPass() {
		return infaPass;
	}
	/**
	 * @param infaPass the infaPass to set
	 */
	public void setInfaPass(String infaPass) {
		this.infaPass = infaPass;
	}
	/**
	 * @return the dbId
	 */
	public long getDbId() {
		return dbId;
	}
	/**
	 * @param dbId the dbId to set
	 */
	public void setDbId(long dbId) {
		this.dbId = dbId;
	}
	/**
	 * @return the dbIp
	 */
	public String getDbIp() {
		return dbIp;
	}
	/**
	 * @param dbIp the dbIp to set
	 */
	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}
	/**
	 * @return the dbPort
	 */
	public String getDbPort() {
		return dbPort;
	}
	/**
	 * @param dbPort the dbPort to set
	 */
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}
	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	/**
	 * @return the dbUser
	 */
	public String getDbUser() {
		return dbUser;
	}
	/**
	 * @param dbUser the dbUser to set
	 */
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	/**
	 * @return the dbPass
	 */
	public String getDbPass() {
		return dbPass;
	}
	/**
	 * @param dbPass the dbPass to set
	 */
	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}
	/**
	 * @return the dbType
	 */
	public int getDbType() {
		return dbType;
	}
	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(int dbType) {
		this.dbType = dbType;
	}
}
