package cn.com.liandisys.infa.entity.index;

import java.sql.Timestamp;

import cn.com.liandisys.infa.entity.IdEntity;

public class TaskLogTime extends IdEntity{
	private Timestamp START_TIME;
	private Timestamp END_TIME;
	private String RUN_STATUS_CODE;
	public String getRUN_STATUS_CODE() {
		return RUN_STATUS_CODE;
	}
	public void setRUN_STATUS_CODE(String rUN_STATUS_CODE) {
		RUN_STATUS_CODE = rUN_STATUS_CODE;
	}
	public Timestamp getSTART_TIME() {
		return START_TIME;
	}
	public void setSTART_TIME(Timestamp sTART_TIME) {
		START_TIME = sTART_TIME;
	}
	public Timestamp getEND_TIME() {
		return END_TIME;
	}
	public void setEND_TIME(Timestamp eND_TIME) {
		END_TIME = eND_TIME;
	}

	
	
}
