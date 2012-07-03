package cn.com.liandisys.infa.service.log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.idev.ssi.service.impl.BaseServiceIbatisImpl;
import cn.com.liandisys.infa.entity.mng.InfaSessLog;
import cn.com.liandisys.infa.entity.mng.InfaWflowRun;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.FileUtil;

/**
 * WorkFlow及session日志同步
 * 
 * @author
 * 
 * @param <T>
 */

@Service("logService")
public class LogService<T> extends BaseServiceIbatisImpl<T> {
	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(LogService.class);
	@Autowired
	private InfaCommonService infaCommenService;

	// 存放informatica表路径的Map，减少数据库操作
	private static Map<String, String> catalogues;

	/**
	 * 执行逻辑
	 * 
	 * @throws BusinessException
	 */
	public void excuteLogRead() {
		// 获取已运行结束 但没有日志详情的WorkFlow和session
		List<InfaWflowRun> wflogList = getUnLogedWFList();
		List<InfaSessLog> seslogList = getUnLogedSESList();
		if (wflogList != null && wflogList.size() > 0) {
			logger.info("li : wflogList size == " + wflogList.size());
			long[] infoidlist = getWKInformaticIdList(wflogList);
			String[] logpathlist = getWkLogPathList(wflogList);

			String[] loglist = new String[wflogList.size()];

			for (int i = 0; i < logpathlist.length; i++) {
				long id = infoidlist[i];
				String catalogue = null;
				try {
					// 如果Map中存在 直接取不进行表操作
					if (catalogues == null) {
						catalogues = new HashMap<String, String>();
					} else {
						catalogue = catalogues.get(String.valueOf(id));
					}
					if (catalogue == null) {
						// 获取Informatic表中对应的workflowpath
						catalogue = (String) findObject(
								"InfaWflowRun.findinfoById", id);
						catalogues.put(String.valueOf(id), catalogue);
					}
					if (catalogue != null) {
						// logger.info("li : wk catalogue ====== " + catalogue);
						// 拼Log存放地址
						logpathlist[i] = catalogue + logpathlist[i] + ".bin";
						// logger.info("li : wk logpathlist ====== "
						// + logpathlist[i]);
						// 到指定位置读取Log文件
						loglist[i] = FileUtil.parseLog(logpathlist[i]);
					}
				} catch (BusinessException e) {
					e.printStackTrace();
					logger.error("workflow日志同步获取错误BusinessException： ", e);
				}
			}
			if (loglist != null && loglist.length > 0) {
				logger.info("li : wk saveWKLog ====== ");
				// 截取合适字符串长度 存入数据库
				for (int i = 0; i < wflogList.size(); i++) {
					if (loglist[i] != null) {
						int x = loglist[i].length();
						wflogList.get(i).setLogDetail(
								loglist[i].substring(0, x < 1000 ? x : 1000));
					}
				}
				saveWKLog(wflogList);
			}
		}
		if (seslogList != null && seslogList.size() != 0) {
			logger.info("li : seslogList size == " + seslogList.size());
			long[] infoidlist = getSESInformaticIdList(seslogList);
			String[] logpathlist = getSESLogPathList(seslogList);

			String[] loglist = new String[seslogList.size()];

			for (int i = 0; i < logpathlist.length; i++) {
				long id = infoidlist[i];
				String catalogue = null;
				try {
					// 如果Map中存在 直接取不进行表操作
					if (catalogues == null) {
						catalogues = new HashMap<String, String>();
					} else {
						catalogue = catalogues.get(String.valueOf(id));
					}
					if (catalogue == null) {
						// 获取Informatic表中对应的sessionpath
						catalogue = (String) findObject(
								"InfaSessLog.findinfoById", id);
						catalogues.put(String.valueOf(id), catalogue);
					}

					if (catalogue != null) {
						// logger.info("li : ses catalogue ====== " +
						// catalogue);
						// 拼Log存放地址
						logpathlist[i] = catalogue + logpathlist[i] + ".bin";
						// logger.info("li : ses logpathlist ====== "
						// + logpathlist[i]);
						// 到指定位置读取Log文件
						loglist[i] = FileUtil.parseLog(logpathlist[i]);
					}
				} catch (BusinessException e) {
					logger.error("session日志同步获取错误BusinessException： ", e);
					e.printStackTrace();
				}
			}
			if (loglist != null && loglist.length > 0) {
				logger.info("li : sess savesessLog ====== ");
				// 截取合适字符串长度 存入数据库
				for (int i = 0; i < seslogList.size(); i++) {
					if (loglist[i] != null) {
						int x = loglist[i].length();
						seslogList.get(i).setLogDetail(
								loglist[i].substring(0, x < 1000 ? x : 1000));
					}
				}
				saveSESLog(seslogList);
			}
		}
	}

	// WORKFLOW LOG READ
	/**
	 * 获取还没生成Log_Detail的workflow
	 * 
	 * @return
	 */
	private List<InfaWflowRun> getUnLogedWFList() {
		List<InfaWflowRun> workFlowRunList = null;
		try {
			workFlowRunList = (List<InfaWflowRun>) findObjects(
					"InfaWflowRun.findwflowByLog", null);
		} catch (BusinessException e) {
			logger.error("workflowlist获取错误BusinessException： ", e);
			e.printStackTrace();
		}
		return workFlowRunList;
	}

	/**
	 * 将获得的Log对应存入数据库
	 * 
	 * @param wflogList
	 */
	private void saveWKLog(List<InfaWflowRun> wflogList) {
		// for (int i = 0; i < wflogList.size(); i++) {
		// if (loglist[i] != null) {
		// int x = loglist[i].length();
		// wflogList.get(i).setLogDetail(
		// loglist[i].substring(0, x < 2000 ? x : 2000));
		// logger.info("=++++=" + loglist[i].substring(0, 5));
		// }
		// }
		try {
			infaCommenService.batchModifyObject(
					"InfaWflowRun.refreshWkLogDetail", wflogList);// 批量
		} catch (BusinessException e) {
			logger.error("workflow日志detail存储错误BusinessException： ", e);
			e.printStackTrace();
		}
	}

	/**
	 * 获得表中的LOG_FILE 路径
	 * 
	 * @param wflogList
	 * @return
	 */
	private String[] getWkLogPathList(List<InfaWflowRun> wflogList) {
		String[] log = new String[wflogList.size()];
		for (int i = 0; i < wflogList.size(); i++) {
			String str = wflogList.get(i).getLogFile();
			log[i] = str.substring(str.indexOf("\\") + 1);
		}
		return log;
	}

	/**
	 * 获得对应的informatic id
	 * 
	 * @param wflogList
	 * @return
	 */
	private long[] getWKInformaticIdList(List<InfaWflowRun> wflogList) {
		long[] info = new long[wflogList.size()];
		for (int i = 0; i < wflogList.size(); i++) {
			info[i] = wflogList.get(i).getInfomaticId();
		}
		return info;
	}

	// SESSION LOG READ
	/**
	 * 获取还没生成Log_Detail的session
	 * 
	 * @return
	 */
	private List<InfaSessLog> getUnLogedSESList() {
		List<InfaSessLog> sesLogList = null;
		try {
			sesLogList = (List<InfaSessLog>) findObjects(
					"InfaSessLog.findsessByLog", null);
		} catch (BusinessException e) {
			logger.error("sessionlist获取错误BusinessException： ", e);
			e.printStackTrace();
		}
		return sesLogList;
	}

	/**
	 * 将获得的Log对应存入数据库
	 * 
	 * @param seslogList
	 */
	private void saveSESLog(List<InfaSessLog> seslogList) {
		try {
			infaCommenService.batchModifyObject(
					"InfaSessLog.refreshSesLogDetail", seslogList);// 批量
		} catch (BusinessException e) {
			logger.error("session日志detail存储错误BusinessException： ", e);
			e.printStackTrace();
		}
	}

	/**
	 * 获得表中的LOG_FILE 路径
	 * 
	 * @param seslogList
	 * @return
	 */
	private String[] getSESLogPathList(List<InfaSessLog> seslogList) {
		String[] log = new String[seslogList.size()];
		for (int i = 0; i < seslogList.size(); i++) {
			String str = seslogList.get(i).getSessionFile();
			log[i] = str.substring(str.indexOf("\\") + 1);
		}
		return log;
	}

	/**
	 * 获得对应的informatic id
	 * 
	 * @param seslogList
	 * @return
	 */
	private long[] getSESInformaticIdList(List<InfaSessLog> seslogList) {
		long[] info = new long[seslogList.size()];
		for (int i = 0; i < seslogList.size(); i++) {
			info[i] = seslogList.get(i).getInfomaticId();
		}
		return info;
	}

}
