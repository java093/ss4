package cn.com.liandisys.infa.entity.sys;

import cn.com.liandisys.infa.entity.IdEntity;

public class MailConfig extends IdEntity {
	private String HOST;
	private String PORT;
	private String USERNAME;
	private String PASSWORD;
	private String LOGICALNAME;

	

	public String getLOGICALNAME() {
		return LOGICALNAME;
	}

	public void setLOGICALNAME(String lOGICALNAME) {
		LOGICALNAME = lOGICALNAME;
	}

	public String getHOST() {
		return HOST;
	}

	public void setHOST(String hOST) {
		HOST = hOST;
	}

	public String getPORT() {
		return PORT;
	}

	public void setPORT(String pORT) {
		PORT = pORT;
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

}
