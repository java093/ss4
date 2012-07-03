package cn.com.liandisys.infa.util.workflow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.liandisys.infa.entity.job.WorkFlow;


public class WorkFlowImportUtil {
	
	private Connection con = null;
	ArrayList<WorkFlow> srcList = new ArrayList<WorkFlow>();
	private static Logger logger = LoggerFactory
			.getLogger(WorkFlowImportUtil.class);
	
	
	/*
	 * 重载getWorkFlowList()方法，根据传入参数查询WorkFlow.
	 */
    public ArrayList<WorkFlow> getWorkFlowList(String dbname, String dbtype,String username,String port,String ip,String password){
    	
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			con = DriverManager.getConnection(
					dbtype+":@"+ip+":"+port+":"+dbname, username,
					password);
//			con = DriverManager.getConnection(
//					dbtype+":@172.16.50.199:"+port+":"+dbname, username,
//					password);
			
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
					workFlow.setWORKFLOW_ID(rs.getDouble("WORKFLOW_ID"));
					workFlow.setALIAS(rs.getString("WORKFLOW_NAME"));
					srcList.add(workFlow);
				}
				prest.close();
				con.close();
			} catch (SQLException s) {
				logger.info("SQL statement is not executed!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return srcList;
		
	}
    
    
    
	/*
	 * 开始比较，去掉重复workflow
	 */
	public int comparison(List<WorkFlow> list,ArrayList<WorkFlow> importlist){
		int flag ;
		String[][] arrayFlag = new String[list.size()][2];
		// String[][] arrayFlagIM=new String[importlist.size()][2];

		for (int i = 0; i < arrayFlag.length; i++) {
			//arrayFlag[i][0] = list.get(i).getSUBJECT_AREA();
			arrayFlag[i][0] = String.valueOf(list.get(i).getWORKFLOW_ID());
			arrayFlag[i][1] = "0";
		}

		for (int i = 0; i < importlist.size(); i++) {
			WorkFlow workFlow = (WorkFlow) importlist.get(i);
			String area = String.valueOf(workFlow.getWORKFLOW_ID());
			for (int j = 0; j < arrayFlag.length; j++) {
				if (arrayFlag[j][0].equals(area)) {
					arrayFlag[j][1] = "1";
				}
			}
		}

		
		for (int i = 0; i < arrayFlag.length; i++) {
			if (arrayFlag[i][1].equals("1")) {
				
				flag = 0;
				return flag;
			} 
		}
		flag = 1;
		return flag;
	}
	
}
