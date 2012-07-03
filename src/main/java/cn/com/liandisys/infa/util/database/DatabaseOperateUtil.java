package cn.com.liandisys.infa.util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DatabaseOperateUtil {

	private static Logger logger = LoggerFactory
			.getLogger(DatabaseOperateUtil.class);
	
	public static String DB_DRIVER_NAME_ORACLE = "oracle.jdbc.driver.OracleDriver";
	public static String JDBC_URL_ORACLE = "jdbc:oracle:thin:@{0}:{1}:{2}";

	// TODO
	public static String DB_DRIVER_NAME_GREENPLUM = "oracle.jdbc.driver.OracleDriver";
	public static String JDBC_URL_GREENPLUM = "jdbc:oracle:thin:@{0}:{1}:{2}";
	
	/**
	 * 获取数据库连接
	 * @param DBType
	 * @param DBName
	 * @param dbIp
	 * @param port
	 * @param userNm
	 * @param pass
	 * @return
	 */
	public static Connection getConnection(int DBType, String DBName
			, String dbIp, String port, String userNm, String pass) {
		Connection conn = null;
		String driver = getDriver(DBType);
		String dbUrl = getDbUrl(DBType, DBName, dbIp, port);
		dbUrl = getDbUrl(1, "orcl", "172.16.53.37", "1521");
		try {
			Class.forName(driver); // 驱动
			conn = DriverManager.getConnection(dbUrl, userNm, pass);
//			conn = DriverManager.getConnection(dbUrl, "inf04", "inf04");
		} catch (ClassNotFoundException e1) {
			if (logger.isErrorEnabled()) {
				logger.error("driver ClassNotFoundException", e1);
			}
		} catch (SQLException e) {
			if (logger.isErrorEnabled()) {
				logger.error("Connect failed!", e);
			}
		}
		
		return conn;
	}
	
	/**
	 * 获取数据连接字符串
	 * @param dBType 数据库类型，1：Oracle，2：Greenplum
	 * @param dBName 数据库名
	 * @param dbIp 数据库服务器IP
	 * @param port 端口号
	 * @return 数据连接字符串
	 */
	public static String getDbUrl(int dBType, String dBName, String dbIp, String port) {
		if (dBType == 0 
				|| !StringUtils.hasLength(dBName)
				|| !StringUtils.hasLength(dbIp)
				|| !StringUtils.hasLength(port)) {
			return null;
		}
		String url = null;
		switch (dBType) {
		case 1:
			url = MessageFormat.format(JDBC_URL_ORACLE, dbIp, port, dBName);
			break;
		case 2:
			// TODO
			break;
		default:
			url = MessageFormat.format(JDBC_URL_ORACLE, dbIp, port, dBName);
		}
		return url;
	}

	private static String getDriver(int DBType) {
		String driver = null;
		switch (DBType) {
		case 1:
			driver = DB_DRIVER_NAME_ORACLE;
		case 2:
			driver = DB_DRIVER_NAME_GREENPLUM;
		default:
			driver = DB_DRIVER_NAME_ORACLE;
		}
		return driver;
	}

	/**
	 * 连接数据库
	 * 
	 * @param bean
	 *            数据库参数
	 * @return 连接
	 * @throws SQLException
	 */
	private static Connection getConnection(DBConnectBean bean)
			throws SQLException {
		try {
			Class.forName(bean.getDriverName());// 驱动
		} catch (ClassNotFoundException e1) {
			if (logger.isInfoEnabled()) {
				logger.info("driver ClassNotFoundException");
			}
		}
		Connection con = null;
		try {
			con = DriverManager.getConnection(bean.getDbURL(),
					bean.getUserName(), bean.getUserPwd());
		} catch (SQLException e) {
			if (logger.isInfoEnabled()) {
				logger.info("Connect failed!");
			}
			throw e;
		}
		if (logger.isInfoEnabled()) {
			logger.info("Connect succed!");
		}
		return con;
	}

	/**
	 * 数据库查询操作
	 * 
	 * @param bean
	 *            连接bean
	 * @param sql
	 *            原始SQL语句
	 * @param conditions
	 *            SQL拓展条件 “=？”
	 * @param rshandler   
	 * @return 
	 * @throws SQLException
	 */
	public static Object executeSelectSQL(DBConnectBean bean, String sql,
			Object[] conditions, DBResultSetHalnder rshandler)
			throws SQLException {

		Connection con = getConnection(bean);

		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = prepareSQL(con, sql, conditions);

			rs = statement.executeQuery();

			if (logger.isInfoEnabled()) {
				logger.info("execute select statement: " + sql);
			}
			return rshandler.rshanlder(rs);
		} catch (SQLException e) {
			con.rollback();
			if (logger.isInfoEnabled()) {
				logger.info("SelectSQLstatement is exception: "
						+ e.getMessage());
			}
			throw e;
		} finally {
			close(con, statement, rs);
		}
	}

	/**
	 * 数据库更改操作
	 * 
	 * @param bean
	 *            数据库参数
	 * @param sql
	 *            操作语句
	 * @param conditions
	 *            SQL拓展条件 “=？”
	 * @return
	 * @throws SQLException
	 */
	public static int executeUpdateSQL(DBConnectBean bean, String sql,
			Object[] conditions) throws SQLException {

		Connection con = getConnection(bean);

		PreparedStatement statement = null;
		try {
			statement = prepareSQL(con, sql, conditions);
			int rs = statement.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info("execute update statement: " + sql);
			}
			return rs;
		} catch (SQLException e) {
			con.rollback();
			if (logger.isInfoEnabled()) {
				logger.info("UpdateSQLstatement is exception: "
						+ e.getMessage());
			}
			throw e;
		} finally {
			close(con, statement, null);
		}
	}

	/**
	 * SQL语句拓展
	 * 
	 * @param con
	 * @param sql
	 * @param conditions
	 *            SQL拓展条件
	 * @return
	 * @throws SQLException
	 */
	private static PreparedStatement prepareSQL(Connection con, String sql,
			Object[] conditions) throws SQLException {
		PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(sql);

			if (logger.isInfoEnabled()) {
				logger.info(sql);
			}

			if (conditions != null) {
				for (int i = 0; i < conditions.length; i++) {
					// 语句完整
					statement.setObject(i + 1, conditions[i]);
				}
			}
		} catch (SQLException e) {
			if (logger.isInfoEnabled()) {
				logger.info("SQLstatement is exception: " + e.getMessage());
			}
			throw e;
		}
		return statement;
	}

	/**
	 * 关闭连接
	 * 
	 * @param con
	 * @param prest
	 * @param rs
	 */
	private static void close(Connection con, Statement statement, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (con != null && !con.isClosed()) {
				con.close();
			}
			if (logger.isInfoEnabled()) {
				logger.info("sql connection is closed");
			}
		} catch (SQLException e) {
			if (logger.isInfoEnabled()) {
				logger.info("close connection is exception: " + e.getMessage());
			}
		}
	}
}
