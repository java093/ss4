package cn.com.liandisys.infa.util.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBResultSetHalnder {
	Object rshanlder(ResultSet rs) throws SQLException ;
}
