package cn.com.liandisys.infa.util.database;

/**
 * 数据库连接BEAN
 * 
 * @author 
 * 
 */

public class DBConnectBean {
	
	// 驱动名
	private String driverName;
	// 数据库URL
	private String dbURL;
	// 数据库用户名
	private String userName;
	// 数据库用户密码
	private String userPwd;

	public DBConnectBean() {
		super();
	}

	/**
	 * 
	 * @param driverName驱动名
	 * @param dbURL数据库URL
	 * @param userName数据库用户名
	 * @param userPwd数据库用户密码
	 */
	public DBConnectBean(String driverName, String dbURL, String userName,
			String userPwd) {
		super();
		this.driverName = driverName;
		this.dbURL = dbURL;
		this.userName = userName;
		this.userPwd = userPwd;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDbURL() {
		return dbURL;
	}

	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
}
