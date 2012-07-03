package cn.com.liandisys.infa.util.workflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.informatica.powercenter.sdk.lm.DriverFactory;
import com.informatica.powercenter.sdk.lm.EJLMPingState;
import com.informatica.powercenter.sdk.lm.EJLMRequestMode;
import com.informatica.powercenter.sdk.lm.EJLMTaskStatus;
import com.informatica.powercenter.sdk.lm.IJLMConnection;
import com.informatica.powercenter.sdk.lm.IJLMDate;
import com.informatica.powercenter.sdk.lm.IJLMDriver;
import com.informatica.powercenter.sdk.lm.IJLMLogSegment;
import com.informatica.powercenter.sdk.lm.IJLMLogSegmentCallback;
import com.informatica.powercenter.sdk.lm.IJLMSession;
import com.informatica.powercenter.sdk.lm.IJLMStartWorkflowReplyDetail;
import com.informatica.powercenter.sdk.lm.IJLMTask;
import com.informatica.powercenter.sdk.lm.IJLMTaskTopLevelParent;
import com.informatica.powercenter.sdk.lm.IJLMWorkflow;
import com.informatica.powercenter.sdk.lm.IJLMWorkflowTasks;
import com.informatica.powercenter.sdk.lm.JLMException;

/**
 * Workflow操作类 调用informatica API
 * 
 * @author
 * 
 */

public class WorkFlowOperateUtil {

	// private static Logger logger = LoggerFactory
	// .getLogger(WorkFlowOperateUtil.class);

	// 驱动
	private static IJLMDriver ld = null;

	// 连接组
	private static Map<String, IJLMConnection> connections;

	/**
	 * 连接服务器
	 * 
	 * @param host
	 *            服务器Host
	 * @param port
	 *            服务器port
	 * @param server
	 *            存储库名
	 * @param username
	 *            服务器用户名
	 * @param userpwd
	 *            服务器用户密码
	 * @return 连接
	 * @throws WorkFlowException
	 * @throws JLMException
	 */
	public static IJLMConnection connect(String host, String port,
			String server, String username, String userpwd) throws JLMException {
		// 如果驱动存在则不再新建驱动
		try {
			if (ld == null) {
				ld = DriverFactory.getDriver();
				String path = System.getProperty("localepath");
				ld.initialize(path);
			}
		} catch (JLMException e) {
			// logger.info("initialize errer");
			ld = null;
			throw e;
		}
		// 连接 新建连接将存入Map 如果存在则通过Key取出
		IJLMConnection connection = null;
		String key = host + port + server + username + userpwd;
		if (connections == null) {
			connections = new HashMap<String, IJLMConnection>();
		} else {
			connection = connections.get(key);
		}
		if (connection == null) {
			java.lang.Object context = new java.lang.Object();
			connection = ld.getConnection(host, Integer.parseInt(port), 30,// "Domain_SD5-INFA-C2"
					context);

			connection.login("", server, username, userpwd, null);
			connections.put(key, connection);
		}
		// if (logger.isInfoEnabled()) {
		// logger.info("IJLMConnection OK");
		// }
		return connection;
	}

	/**
	 * 获得连接状态
	 * 
	 * @param host
	 *            服务器Host
	 * @param port
	 *            服务器port
	 * @param server
	 *            存储库名
	 * @param username
	 *            服务器用户名
	 * @param userpwd
	 *            服务器用户密码
	 * @return 连接状态
	 * @throws WorkFlowException
	 */
	public static String getConnectStatus(String host, String port,
			String server, String username, String userpwd)
			throws WorkFlowException {
		// 取得连接
		// if (logger.isInfoEnabled()) {
		// logger.info("why===++");
		// }
		IJLMConnection connection = null;
		String stat = null;
		try {
			connection = connect(host, port, server, username, userpwd);
			// if (logger.isInfoEnabled()) {
			// logger.info("stat = " + stat);
			// }
			stat = connection.getConnectionState().toString();
			// if (logger.isInfoEnabled()) {
			// logger.info("Connect Status is " + stat);
			// }
		} catch (JLMException e) {
			// if (logger.isInfoEnabled()) {
			// logger.info("JLMException ==" + e.getErrorCode());
			// }
			throw new WorkFlowException(e.getErrorCode());
		}
		return stat;
	}

	/**
	 * 获得Ping
	 * 
	 * @param host
	 *            服务器Host
	 * @param port
	 *            服务器port
	 * @return ping状态
	 * @throws JLMException
	 */
	public static String pingConnect(String host, String port)
			throws JLMException {
		if (ld == null) {
			ld = DriverFactory.getDriver();
			String path = System.getProperty("localepath");
			ld.initialize(path);
		}
		java.lang.Object context = new java.lang.Object();
		EJLMPingState pingState = ld.ping(host, Integer.parseInt(port), 10,
				context);
		// if (logger.isInfoEnabled()) {
		// logger.info("Connect Ping is " + pingState);
		// }
		return pingState.toString();
	}

	/**
	 * 获取Workflow
	 * 
	 * @param host
	 *            服务器Host
	 * @param port
	 *            服务器port
	 * @param server
	 *            存储库名
	 * @param username
	 *            服务器用户名
	 * @param userpwd
	 *            服务器密码
	 * @param folderName
	 *            Workflow所在文件夹名
	 * @param workflowName
	 *            Workflow名
	 * @return
	 * @throws WorkFlowException
	 */
	public static IJLMWorkflow getWorkFlow(String host, String port,
			String server, String username, String userpwd, String folderName,
			String workflowName) throws WorkFlowException {

		// 取得连接
		IJLMConnection connection;
		IJLMWorkflow wk;
		try {
			connection = connect(host, port, server, username, userpwd);
			wk = connection.getWorkflow(folderName, workflowName, null);
			// if (logger.isInfoEnabled()) {
			// logger.info(workflowName + " is exit!");
			// }
		} catch (JLMException e) {
			// if (logger.isInfoEnabled()) {
			// logger.info("e = " + e.getErrorCode());
			// }
			throw new WorkFlowException(e.getErrorCode());
		}
		return wk;
	}

	/**
	 * 获取WorkFlow运行状态
	 * 
	 * @return 状态1.running 2.succeeded 3.aborted 4.stoped 5.failed
	 * @throws WorkFlowException
	 */
	public static String getWorkFlowStatus(IJLMWorkflow wk)
			throws WorkFlowException {
		String runStatus = null;
		try {
			runStatus = wk.getWorkflowDetails(null).getWorkflowRunStatus()
					.toString();
			// if (logger.isInfoEnabled()) {
			// logger.info("current runStatus is " + runStatus);
			// }
		} catch (JLMException e) {
			throw new WorkFlowException(e.getErrorCode());
		}
		return runStatus;
	}

	/**
	 * 获取WorkFlow运行 开始时间
	 * 
	 * @return 年/月/日 时：分：秒
	 * @throws WorkFlowException
	 * @throws JLMException
	 */
	public static String getWorkFlowStartTime(IJLMWorkflow wk)
			throws WorkFlowException {
		IJLMDate jd;
		try {
			jd = wk.getWorkflowDetails(null).getStartTime();
		} catch (JLMException e) {
			throw new WorkFlowException(e.getErrorCode());
		}
		String startTime = jd.getYear() + "/" + jd.getMonth() + "/"
				+ jd.getDate() + " " + jd.getHours() + ":" + jd.getMinutes()
				+ ":" + jd.getSeconds();
		// if (logger.isInfoEnabled()) {
		// logger.info("workflow start from  " + startTime);
		// }
		return startTime;
	}

	/**
	 * 获取WorkFlow运行 结束时间
	 * 
	 * @return 年/月/日 时：分：秒
	 * @throws WorkFlowException
	 * @throws JLMException
	 */
	public static String getWorkFlowEndTime(IJLMWorkflow wk)
			throws WorkFlowException {
		IJLMDate jd;
		try {
			jd = wk.getWorkflowDetails(null).getEndTime();
		} catch (JLMException e) {
			throw new WorkFlowException(e.getErrorCode());
		}
		String endTime = jd.getYear() + "/" + jd.getMonth() + "/"
				+ jd.getDate() + " " + jd.getHours() + ":" + jd.getMinutes()
				+ ":" + jd.getSeconds();
		endTime = endTime.equals("1970/1/1 8:0:0") ? "" : endTime;
		// if (logger.isInfoEnabled()) {
		// logger.info("workflow end with  " + endTime);
		// }
		return endTime;
	}

	/**
	 * 启动WorkFlow
	 * 
	 * @throws WorkFlowException
	 * 
	 * @throws JLMException
	 */
	public static int startWorkFlow(IJLMWorkflow wk) throws WorkFlowException {
		// boolean flag =
		// WorkFlowOperateUtil.getWorkFlowStatus(wk).equals("running");
		// while (flag) {
		// flag = WorkFlowOperateUtil.getWorkFlowStatus(wk).equals("running");
		// }
		wk.setWorkflowReason("Starting");
		try {
			wk.start(EJLMRequestMode.NORMAL, null);
			// if (logger.isInfoEnabled()) {
			// logger.info("workflow is starting============!");
			// }
			return wk.getWorkflowDetails(null).getWorkflowRunId();
		} catch (JLMException e) {
			// if (logger.isInfoEnabled()) {
			// logger.info("e = " + e.getErrorCode());
			// }
			throw new WorkFlowException(e.getErrorCode());
		}
	}

	/**
	 * 启动WorkFlow
	 * 
	 * @param wk
	 * @return Runid
	 * @throws WorkFlowException
	 */
	public static int startExWorkFlow(IJLMWorkflow wk) throws WorkFlowException {
		IJLMStartWorkflowReplyDetail detail = null;
		if (!getWorkFlowStatus(wk).equals("running")) {
			wk.setWorkflowReason("Starting");
			try {
				detail = wk.startEx(EJLMRequestMode.NORMAL, null);
			} catch (JLMException e) {
				// if (logger.isInfoEnabled()) {
				// logger.info("e = " + e.getErrorCode());
				// }
				throw new WorkFlowException(e.getErrorCode());
			}
			// if (logger.isInfoEnabled()) {
			// logger.info("workflow is startex:");
			// }
		}
		return detail.getRunId();
	}

	/**
	 * 启动task下的WorkFlow
	 * 
	 * @throws WorkFlowException
	 * 
	 * @throws JLMException
	 */
	public static int startWorkFlow(IJLMWorkflow wk, String taskName)
			throws WorkFlowException {
		if (!getWorkFlowStatus(wk).equals("running")) {
			wk.setWorkflowReason("starting");
			try {
				wk.start(EJLMRequestMode.NORMAL, taskName, null);
				// if (logger.isInfoEnabled()) {
				// logger.info("workflow is starting in the " + taskName);
				// }
				return wk.getWorkflowDetails(null).getWorkflowRunId();
			} catch (JLMException e) {
				// if (logger.isInfoEnabled()) {
				// logger.info("e = " + e.getErrorCode());
				// }
				throw new WorkFlowException(e.getErrorCode());
			}
		}
		return 0;
	}

	/**
	 * 中止WorkFlow
	 * 
	 * @throws JLMException
	 * @throws WorkFlowException
	 */
	public static void abortWorkFlow(IJLMWorkflow wk) throws WorkFlowException {
		try {
			if (getWorkFlowStatus(wk).equals("running")) {
				wk.setWorkflowReason("suspending");
				wk.stop(true, null);// 是否要中止或停止
				// if (logger.isInfoEnabled()) {
				// logger.info("workflow is aborting!");
				// }
			}
		} catch (WorkFlowException e) {
			throw new WorkFlowException(e.getErrCode());
		} catch (JLMException e) {
			throw new WorkFlowException(e.getErrorCode());
		}
	}

	/**
	 * 停止WorkFLow
	 * 
	 * @throws JLMException
	 */
	public static void stopWorkFlow(IJLMWorkflow wk) throws WorkFlowException {
		try {
			if (getWorkFlowStatus(wk).equals("running")) {
				wk.setWorkflowReason("stopping");
				wk.stop(false, null);// 是否要中止或停止ֹ
				// if (logger.isInfoEnabled()) {
				// logger.info("workflow is stopping!");
				// }
			}
		} catch (WorkFlowException e) {
			throw new WorkFlowException(e.getErrCode());
		} catch (JLMException e) {
			throw new WorkFlowException(e.getErrorCode());
		}
	}

	/**
	 * 重新运行suspended的WorkFlow
	 * 
	 * @throws JLMException
	 */
	public static void resumeWorkFlow(IJLMWorkflow wk) throws WorkFlowException {
		wk.setWorkflowReason("starting");
		try {
			wk.resume(EJLMRequestMode.NORMAL, null);
		} catch (JLMException e) {
			throw new WorkFlowException(e.getErrorCode());
		}
		// if (logger.isInfoEnabled()) {
		// logger.info("workflow is resume!");
		// }
	}

	/**
	 * 恢复已失败的WorkFlow
	 * 
	 * @throws JLMException
	 */
	public static void recoverWorkFlow(IJLMWorkflow wk)
			throws WorkFlowException {
		wk.setWorkflowReason("starting");
		try {
			wk.recover(null);
		} catch (JLMException e) {
			throw new WorkFlowException(e.getErrorCode());
		}
		// if (logger.isInfoEnabled()) {
		// logger.info("workflow is recover!");
		// }
	}

	/**
	 * 关闭连接
	 * 
	 * @throws WorkFlowException
	 * 
	 * @throws JLMException
	 */
	public static void closeConnect(IJLMConnection connection)
			throws WorkFlowException {
		try {
			connections.remove(connection);
			connection.close(null);
		} catch (JLMException e) {
			throw new WorkFlowException(e.getErrorCode());
		}
		// ld.deinitialize();
		// if (logger.isInfoEnabled()) {
		// logger.info("this connection is close!");
		// }
	}

	/**
	 * 关闭所有连接
	 * 
	 * @throws WorkFlowException
	 * 
	 * @throws JLMException
	 */
	public static void closeConnectAll() throws WorkFlowException {
		Set<String> set = connections.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			try {
				connections.get(iterator.next()).close(null);
				connections = null;
				ld.deinitialize();
			} catch (JLMException e) {
				throw new WorkFlowException(e.getErrorCode());
			}
		}
		// if (logger.isInfoEnabled()) {
		// logger.info("all connection is close!");
		// }
	}

	/**
	 * 获得WorkFlowLog日志
	 * 
	 * @param wk
	 * @throws WorkFlowException
	 * @throws JLMException
	 */
	public static String getWorkflowLog(IJLMWorkflow wk)
			throws WorkFlowException {
		MyWorkflowLogSegmentCallback logSegmentCallback = new MyWorkflowLogSegmentCallback();
		try {
			wk.startWorkflowLogFetch(logSegmentCallback, null, null);
		} catch (JLMException e) {
			// if (logger.isInfoEnabled()) {
			// logger.info("e = " + e.getErrorCode());
			// }
			throw new WorkFlowException(e.getErrorCode());
		}
		String log = new String(logSegmentCallback.bufferByte);
		// if (logger.isInfoEnabled()) {
		// logger.info("=========log============");
		// logger.info(log);
		// }
		return log;
	}

	/**
	 * 获得SessionLog日志
	 * 
	 * @param wk
	 * @throws WorkFlowException
	 * @throws JLMException
	 */
	public static String getSessionLog(IJLMWorkflow wk)
			throws WorkFlowException {
		StringBuffer log = new StringBuffer();
		try {
			IJLMWorkflowTasks succeedTasks = wk.getWorkflowTasksByStatus(
					EJLMTaskStatus.SUCCEEDED, null);
			if (succeedTasks.getTopLevelParents() != null) {
				log.append(getSesslog(wk, succeedTasks) + "/n");
			}
			IJLMWorkflowTasks runningTasks = wk.getWorkflowTasksByStatus(
					EJLMTaskStatus.RUNNING, null);
			if (runningTasks.getTopLevelParents() != null) {
				log.append(getSesslog(wk, runningTasks) + "/n");
			}
		} catch (JLMException e) {
			throw new WorkFlowException(e.getErrorCode());
		}
		return log.toString();
	}

	private static String getSesslog(IJLMWorkflow wk,
			IJLMWorkflowTasks objstatus) throws JLMException {

		IJLMTaskTopLevelParent[] taskpar = objstatus.getTopLevelParents();
		IJLMTask task;
		StringBuffer log = new StringBuffer();
		for (IJLMTaskTopLevelParent p : taskpar) {
			String taskname = p.getTaskInstanceName();
			if (!taskname.equals("启动")) {
				task = wk.getTask(null, taskname);
				MyWorkflowLogSegmentCallback logSegmentCallback = new MyWorkflowLogSegmentCallback();
				if (task != null) {
					IJLMSession session = task.getSession(null);// 获得session
					if (session != null) {
						session.startSessionLogFetch(logSegmentCallback, null,
								null);// start回调函数
					}
				}
				log.append(new String(logSegmentCallback.bufferByte) + "/n");
				// if (logger.isInfoEnabled()) {
				// logger.info("=========log============");
				// logger.info(log.toString());
				// }
			}
		}
		return log.toString();
	}

}

// Log回调
class MyWorkflowLogSegmentCallback implements IJLMLogSegmentCallback {

	protected IJLMLogSegment logSeg;
	protected int bufferSize;
	protected byte[] bufferByte;

	public void receiveLogSegment(IJLMLogSegment logSegment) {
		logSeg = logSegment;
		displayLogSegment();
		return;
	}

	public void displayLogSegment() {
		bufferSize = logSeg.getBufferSize();// 返回日志区段大小
		bufferByte = logSeg.getMBCSBuffer();// 以字节组形式返回日志buffer
	}
}
