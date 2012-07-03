package cn.com.liandisys.infa.web.job;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import cn.com.liandisys.infa.entity.job.WorkFlow;

public class SelectRecords {
	public static void main(String[] args) {
		System.out
				.println("Select Records Example by using the Prepared Statement!");
		Connection con = null;
		Connection conDest = null;

		int count = 0;
		ArrayList srcList = new ArrayList();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@172.16.50.199:1521:orcl", "inf04",
					"inf04");

			conDest = DriverManager.getConnection(
					"jdbc:oracle:thin:@172.16.52.129:1521:orcl", "inf04",
					"inf04");

			// 源数据库操作
			try {
				String sql = "SELECT * FROM REP_WORKFLOWS";
				PreparedStatement prest = con.prepareStatement(sql);

				ResultSet rs = prest.executeQuery();

				while (rs.next()) {
					WorkFlow workFlow = new WorkFlow();
					workFlow.setSUBJECT_AREA(rs.getString("SUBJECT_AREA"));
					workFlow.setWORKFLOW_NAME(rs.getString("WORKFLOW_NAME"));
					workFlow.setSCHEDULER_NAME(rs.getString("SCHEDULER_NAME"));
					workFlow.setSTART_TIME(rs.getString("START_TIME"));
					workFlow.setEND_TIME(rs.getString("END_TIME"));
					// int val =
					// stDest.executeUpdate("INSERT TASK_WORKFLOW('SUBJECT_AREA') VALUES("+rs.getString(1)+")");
					srcList.add(workFlow);
				}
				prest.close();
				con.close();
			} catch (SQLException s) {
				System.out.println("SQL statement is not executed!");
			}

			// 目标数据库操作
			try {
				Statement st = conDest.createStatement();
				Iterator it = srcList.iterator();
				while (it.hasNext()) {
					WorkFlow workFlow = (WorkFlow) it.next();
					System.out.println("1 row affected "
							+ workFlow.getSUBJECT_AREA());
					String sql = "INSERT INTO INFA_WORKFLOW (SUBJECT_AREA,WORKFLOW_NAME,SCHEDULER_NAME,START_TIME,END_TIME) VALUES('"
							+ workFlow.getSUBJECT_AREA()
							+ "','"
							+ workFlow.getWORKFLOW_NAME()
							+ "','"
							+ workFlow.getSCHEDULER_NAME()
							+ "','"
							+ workFlow.getSTART_TIME()
							+ "','"
							+ workFlow.getEND_TIME() + "')";
					System.out.println("sql=" + sql);
					int val = st.executeUpdate(sql);
					System.out.println("1 row affected");
				}
				st.close();
				conDest.close();
			} catch (SQLException s) {
				System.out.println("SQL statement is not executed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}