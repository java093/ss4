package cn.com.liandisys.infa.util;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log解析工具类
 * 
 * @author
 * 
 */

public class FileUtil {

	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/** 操作系统 */
	private static String osName;

	static {
		osName = System.getProperty("os.name");
		if (osName == null) {
			try {
				throw new IOException("os.name not found");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		osName = osName.toLowerCase();
		logger.info("========== os name ===========" + osName);
	}

	/**
	 * Informatic服务器上LOG日志 调用命令解析
	 * 
	 * @param fileName
	 * @return
	 */
	public static String parseLog(String fileName) {
		File f = new File(fileName);
		// 判断源Log是否存在 ， 如不存在不进行转码
		if (f.exists()) {
			logger.debug(fileName + " is exit ");
			// Process process;
			int index = fileName.lastIndexOf(".");
			String newFileName = fileName.substring(0, index) + ".log";
			// 判断目标Log文件是否存在，如存在不进行转码
			File newf = new File(newFileName);
			if (!newf.exists()) {
				// infomatic服务器解析bat所在位置
				String infsSvrPath = System
						.getProperty("informatica.server.bin");
				// Log转换命令
				String command = "ConvertLogFile -in " + fileName
						+ " -fm Text -lo " + newFileName;

				String cmd = "";
				try {
					if (osName.indexOf("windows") != -1) {
						// Win拼命令行 .bin转.log
						cmd = infsSvrPath + File.separator + "infacmd.bat "
								+ command;
						logger.debug("!window!cmd!!--!!" + cmd);
					} else if (osName.indexOf("linux") != -1) {
						// Linux 拼命令行 .bin转.log
						String command1 = "chmod 777 " + infsSvrPath
								+ File.separator + "infacmd.sh ";
						// Linux下需添加权限 否则报错Permission denied
						Runtime.getRuntime().exec(command1).waitFor();

						cmd = "/bin/sh " + infsSvrPath + File.separator
								+ "infacmd.sh " + command;
						logger.debug("!linux!cmd!!--!!" + cmd);
					}
					Runtime.getRuntime().exec(cmd).waitFor();
				} catch (IOException e) {
					logger.error("命令行执行错误IOException： ", e);
					// e.printStackTrace();
				} catch (InterruptedException e) {
					logger.error("命令行执行错误InterruptedException： ", e);
					// e.printStackTrace();
				}
			}
			// 输出
			String detail = null;
			try {
				detail = FileUtils.readFileToString(newf, null);
			} catch (IOException e) {
				logger.error("编码转换错误IOException" + e.getMessage());
				// e.printStackTrace();
			}
			return detail;
		} else {
			logger.debug(fileName + " is not exit ");
			return "";
		}
	}

	/**
	 * 文件读取输出
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readFileToString(String fileName) {
		File f = new File(fileName);
		String rt = null;
		try {
			if (f.exists()) {
				rt = FileUtils.readFileToString(f, null);
			}
		} catch (IOException e) {
			logger.error("文件转码错误IOException： " + e.getMessage());
			// e.printStackTrace();
		}
		return rt;
	}

}
