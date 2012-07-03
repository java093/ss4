package cn.com.liandisys.infa.util.mail;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件操作类
 * 
 * @author 
 * 
 */

public class SendMailUtil {

	private static Logger logger = LoggerFactory.getLogger(SendMailUtil.class);
	/** 邮箱地址验证用正则表达式 */
	private static String EMAIL_CHECK_REG = 
			"^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 定义一个基本的邮件会话
	 * 
	 * @param hostName 发件Host
	 */
	private static Session setSmtpHost(String hostName) {
		if (logger.isInfoEnabled()) {
			logger.debug("mail.stmp.host= " + hostName);
		}
		Properties props = null;
		if (props == null) {
			props = System.getProperties();
		}
		props.put("mail.smtp.host", hostName);
		return Session.getInstance(props, null);
	}

	/**
	 * 创建邮件信息
	 * 
	 * @param session
	 * @param mailInfo
	 *            邮件信息
	 * @return
	 * @throws MessagingException
	 */
	private static MimeMessage setSubject(Session session, MailInfoBean mailInfo)
			throws MessagingException {
		if (logger.isInfoEnabled()) {
			logger.debug("set mailinfo begin ... ");
		}
		Multipart mp = new MimeMultipart();

		BodyPart bp = new MimeBodyPart();
		bp.setContent(
				"<meta http-equiv=Context-Type context=text/html;charset=gb2312>"
						+ mailInfo.getMailBody(), "text/html;charset=GB2312");
		mp.addBodyPart(bp);

//		String file[];
//		file = mailInfo.getFilename().split(";");
//
//		for (int i = 0; i < file.length; i++) {
//			BodyPart bp1 = new MimeBodyPart();
//			FileDataSource fileds = new FileDataSource(file[i]);
//			bp1.setDataHandler(new DataHandler(fileds));
//			bp1.setFileName(fileds.getName());
//			mp.addBodyPart(bp1);
//		}

		MimeMessage mimeMsg = new MimeMessage(session);
		mimeMsg.setSubject(mailInfo.getMailSubject());// 设置主题
		mimeMsg.setFrom(new InternetAddress(mailInfo.getFrom()));// 设置发件人
		mimeMsg.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(mailInfo.getTo()));// 设置收件人
		// Message.RecipientType.TO发送 Message.RecipientType.CC抄送
//		mimeMsg.setRecipients(Message.RecipientType.CC,
//				InternetAddress.parse(mailInfo.getCopyto()));// 设置抄送人
		mimeMsg.setContent(mp);// 设置邮件主题内容
		mimeMsg.saveChanges();

		return mimeMsg;
	}

	/**
	 *  发送
	 *  
	 * @param hostName
	 * @param mailInfo
	 * @param username
	 * @param password
	 * @return
	 * @throws MessagingException
	 */
	public static boolean setOut(String hostName, MailInfoBean mailInfo,
			String username, String password) throws MessagingException {
		
		Session session = setSmtpHost(hostName);
		
		MimeMessage mimeMsg = setSubject(session, mailInfo);
		
		if (logger.isInfoEnabled()) {
			logger.debug(("正在SendMail..."));
		}
		
		Transport tp = session.getTransport("smtp");
		tp.connect(session.getProperty("mail.smtp.host"), username, password);
		tp.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
		tp.close();
		return true;
	}
	
	/**
	 * 验证邮件地址是否合法
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		 Pattern regex = Pattern.compile(EMAIL_CHECK_REG);  
		 Matcher matcher = regex.matcher(email);  
		 boolean isMatched = matcher.matches();  
		 return isMatched;
	}
}
