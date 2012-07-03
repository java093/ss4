package cn.com.liandisys.infa.util.mail;

/**
 * 邮件BEAN
 * 
 * @author 
 * 
 */

public class MailInfoBean {
	// 邮件主题
	private String mailSubject;
	// 发件人
	private String from;
	// 收件人
	private String to;
	// 抄送
	private String copyto;
	// 内容
	private String mailBody;
	// 附件名
	private String filename;
	
	public MailInfoBean() {
		super();
	}

	public MailInfoBean(String mailSubject, String from, String to,
			String copyto, String mailBody, String filename) {
		super();
		this.mailSubject = mailSubject;
		this.from = from;
		this.to = to;
		this.copyto = copyto;
		this.mailBody = mailBody;
		this.filename = filename;
	}

	public String getCopyto() {
		return copyto;
	}

	public void setCopyto(String copyto) {
		this.copyto = copyto;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
