package cn.com.liandisys.infa.service.sys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.mng.InfaSessLog;
import cn.com.liandisys.infa.entity.mng.InfaWflowRun;
import cn.com.liandisys.infa.entity.mng.TaskLog;
import cn.com.liandisys.infa.entity.syn.InfaSynBaseInfo;
import cn.com.liandisys.infa.entity.syn.InfaSynDetail;
import cn.com.liandisys.infa.entity.sys.InfaDetail;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.database.DatabaseOperateUtil;

/**
 * WorkFlow及session运行状态同步用任务执行类
 * @author gaoyh
 *
 * @param <T>
 */
@Service("workflowSynService")
@Transactional(readOnly = true)
public class WorkFlowSynService {
	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(WorkFlowSynService.class);
	@Autowired
	private InfaCommonService infaCommonService;
	
	/**
	 * 执行逻辑
	 * @throws BusinessException
	 * @throws SQLException 
	 */
	public void excuteWFSyn() throws BusinessException, SQLException {
		// 获取运行未结束的workflow日志记录
		List<InfaSynBaseInfo> wflogList = getUnLogedWFList();
		if (null == wflogList || wflogList.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("没有需要同步的日志数据！");
			}
			return;
		}
		// 获取同步用Informatica详细列表
		List<InfaSynDetail> infaSynDtlList = getInfaSynDtlList(wflogList);
		if (null == infaSynDtlList || infaSynDtlList.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("无法获取同步用Informatica详细列表！");
			}
			return;
		}
		
		// 发行SQL获取数据
		synInfaData(infaSynDtlList);
		
	}

	/**
	 * 数据同步处理
	 * @param infaSynDtlList
	 * @throws BusinessException 
	 * @throws SQLException 
	 */
	@Transactional(readOnly=false)
	public void synInfaData(List<InfaSynDetail> infaSynDtlList) throws BusinessException, SQLException {
		if (logger.isInfoEnabled()) {
			logger.info("同步数据开始！");
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<InfaWflowRun> wfRunList = null;
	 	List<InfaSessLog> sLogList = null;
	 	InfaWflowRun wfRun = null; // workflow日志
	 	InfaSessLog sLog = null; // session日志
	 	TaskLog taskLog = null; // 任务日志
	 	// 20120628 gaoyh 更新状态的处理放到TaskLogAndMailSyn start
//	 	List<TaskLog> taskLogList = null;
//	 	Map<String, String> taskStMap = null; // task运行状态Key：taskRunId，Value：状态
//	 	Map<String, String> subTaskStMap = null; // Sub task运行状态Key：taskRunId，Value：状态
	 	// 20120628 gaoyh 更新状态的处理放到TaskLogAndMailSyn end
	 	long wfStatus; // WorkFlow运行状态
	 	long taskRunId;
	 	String subTaskRunId;
	 	String tRunidStr = null;
	 	long wfRunId; // WorkFlow运行ID
		for (InfaSynDetail synDtl : infaSynDtlList) {
			if (logger.isInfoEnabled()) {
				logger.info("同步数据============ infaID:　【" + synDtl.getInfaId() + "】");
			}
//			// task运行状态
//			taskStMap = new HashMap<String, String>();
//			subTaskStMap = new HashMap<String, String>();

			try {
				// 获取DB连接
				conn = DatabaseOperateUtil.getConnection(
						synDtl.getInfaDetail().getDbType(), 
						synDtl.getInfaDetail().getDbName(), 
						synDtl.getInfaDetail().getDbIp(), 
						synDtl.getInfaDetail().getDbPort(), 
						synDtl.getInfaDetail().getDbUser(),
						synDtl.getInfaDetail().getDbPass());
				// 获取Informatica 知识库查询用SQL
				String repSql = infaCommonService.getIbatisSql(
						"InfaWflowRun.selectRepWflowRunByCond", null);
				if (logger.isDebugEnabled()) {
					logger.debug("获取查询SQL: " + repSql);
				}
				// 对所获取的SQL的参数进行设置
				repSql = MessageFormat.format(repSql, 
						StringUtils.collectionToCommaDelimitedString(synDtl.getWfIdList()), 
						StringUtils.collectionToCommaDelimitedString(synDtl.getWfRunIdList()));
				if (logger.isDebugEnabled()) {
					logger.debug("WorkFlow日志查询用SQL： " + repSql);
				}
				// 执行查询（REP_WFLOW_RUN）
				pstmt = conn.prepareStatement(repSql);
			 	rs = pstmt.executeQuery();
			 	// 对结果集进行转换
			 	wfRunList = new ArrayList<InfaWflowRun>();
			 	taskLog = new TaskLog();
			 	while (rs.next()) {
			 		wfRun = new InfaWflowRun();
			 		wfRun.setSubjectId(rs.getLong("SUBJECT_ID"));
			 		wfRun.setWorkflowId(rs.getLong("WORKFLOW_ID"));
			 		wfRun.setWorkflowRunId(rs.getLong("WORKFLOW_RUN_ID"));
			 		wfRun.setWorkflowName(rs.getString("WORKFLOW_NAME"));
			 		wfRun.setServerId(rs.getLong("SERVER_ID"));
			 		wfRun.setServerName(rs.getString("SERVER_NAME"));
			 		wfRun.setStartTime(rs.getTimestamp("START_TIME"));
			 		wfRun.setEndTime(rs.getTimestamp("END_TIME"));
			 		wfRun.setLogFile(rs.getString("LOG_FILE"));
			 		wfRun.setRunErrCode(rs.getLong("RUN_ERR_CODE"));
			 		wfRun.setRunErrMsg(rs.getString("RUN_ERR_MSG"));
			 		wfStatus = rs.getLong("RUN_STATUS_CODE");
			 		wfRun.setRunStatusCode(wfStatus);
			 		wfRun.setUserName(rs.getString("USER_NAME"));
			 		wfRun.setRunType(rs.getLong("RUN_TYPE"));
			 		wfRun.setVersionNumber(rs.getLong("VERSION_NUMBER"));
			 		wfRun.setSubjectArea(rs.getString("SUBJECT_AREA"));
			 		wfRun.setInfomaticId(synDtl.getInfaId());
			 		wfRunId = rs.getLong("WORKFLOW_RUN_ID");
			 		taskRunId = synDtl.getRel(wfRunId);
			 		wfRun.setTaskRunId(taskRunId);
			 		
			 		if (logger.isDebugEnabled()) {
			 			logger.debug("Infa_Wflow_Run插入数据：【" + wfRun.toString() + "】");
			 			logger.debug("===============状态： 【taskRunId：" + taskRunId + ", wfStatus: " + wfStatus + "】");
			 		}
			 		// 添加到结果集
			 		wfRunList.add(wfRun);
			 		// 20120628 gaoyh 更新状态的处理放到TaskLogAndMailSyn start
//			 		// 对task运行状态进行维护
//			 		tRunidStr = String.valueOf(taskRunId);
//			 		if (0 != taskRunId) {
//			 			if (logger.isDebugEnabled()) {
//			 				logger.debug("对task运行状态进行维护: 【taskRunId：" + taskRunId + ", wfStatus: " + wfStatus + "】");
//			 			}
//				 		if (!taskStMap.containsKey(tRunidStr)) {
//				 			// 默认状态为Succeeded
//				 			taskStMap.put(tRunidStr, "1");
//				 		}
//				 		// 当Task中存在一条workflow运行状态为非“Succeeded”，则置为失败“Failed”
//				 		if (1 != wfStatus) {
//				 			taskStMap.put(tRunidStr, "3");
//				 		}
//			 		}
//			 		// 对子任务的运行状态进行维护
//			 		if (0 != synDtl.getSubRel(wfRunId)) {
//			 			subTaskRunId = String.valueOf(synDtl.getSubRel(wfRunId));
//				 		if (!subTaskStMap.containsKey(subTaskRunId)) {
//				 			// 默认状态为Succeeded
//				 			subTaskStMap.put(subTaskRunId, "1");
//				 		}
//				 		// 当Task中存在一条workflow运行状态为非“Succeeded”，则置为失败“Failed”
//				 		if (1 != wfStatus) {
//				 			subTaskStMap.put(subTaskRunId, "3");
//				 		}
//			 		}
			 		// 20120628 gaoyh 更新状态的处理放到TaskLogAndMailSyn end
			 	}
			 	// 将从知识库获取的workflow运行日志插入平台数据库
			 	infaCommonService.batchAddObject("InfaWflowRun.insertToWFlowRun", wfRunList);
			 	if (logger.isInfoEnabled()) {
			 		logger.info("workflow运行日志插入平台数据库完成！ Size【" + wfRunList.size() + "】");
			 	}
			 	// 20120628 gaoyh 更新状态的处理放到TaskLogAndMailSyn start
			 	// 更新TaskLog状态
//			 	if (!taskStMap.isEmpty()) {
//			 		taskLogList = new ArrayList<TaskLog>();
//			 		if (logger.isDebugEnabled()) {
//			 			logger.debug("任务日志更新用数据Size：【" + taskStMap.size() + "】");
//			 		}
//				 	Iterator<Entry<String, String>> tIt =  taskStMap.entrySet().iterator();
//				 	while (tIt.hasNext()) { 	
//				 		Map.Entry<String, String> ent = tIt.next();
//				 		taskLog = new TaskLog();
//				 		taskLog.setId(Long.parseLong(ent.getKey()));
//				 		taskLog.setRun_status_code(ent.getValue());
//				 		
//				 		taskLogList.add(taskLog);
//				 		logger.info("任务日志更新用数据：【" + taskLog.toString() + "】");
//				 	}
//				 	if (logger.isDebugEnabled()) {
//				 		logger.debug("任务日志更新用SQL：【" + infaCommonService.getIbatisSql("TaskLog.updateStatusById", taskLogList.get(0)) + "】");
//				 	}
//				 	// 更新开始
//				 	infaCommonService.batchModifyObject("TaskLog.updateStatusById", taskLogList);
//				 	if (logger.isInfoEnabled()) {
//				 		logger.info("更新TaskLog状态完成！ Size【" + taskLogList.size() + "】");
//				 	}
//			 	}
//			 	// 更新Sub Task Log状态
//			 	if (!subTaskStMap.isEmpty()) {
//			 		taskLogList = new ArrayList<TaskLog>();
//			 		if (logger.isDebugEnabled()) {
//			 			logger.debug("子任务日志更新用数据Size：【" + taskStMap.size() + "】");
//			 		}
//				 	Iterator<Entry<String, String>> tIt =  subTaskStMap.entrySet().iterator();
//				 	while (tIt.hasNext()) { 	
//				 		Map.Entry<String, String> ent = tIt.next();
//				 		taskLog = new TaskLog();
//				 		taskLog.setId(Long.parseLong(ent.getKey()));
//				 		taskLog.setRun_status_code(ent.getValue());
//				 		
//				 		taskLogList.add(taskLog);
//				 		logger.info("子任务日志更新用数据：【" + taskLog.toString() + "】");
//				 	}
//				 	if (logger.isDebugEnabled()) {
//				 		logger.debug("子任务日志更新用SQL：【" + infaCommonService.getIbatisSql("TaskLog.updateSubStatusById", taskLogList.get(0)) + "】");
//				 	}
//				 	// 更新开始
//				 	infaCommonService.batchModifyObject("TaskLog.updateSubStatusById", taskLogList);
//				 	if (logger.isInfoEnabled()) {
//				 		logger.info("更新TaskLog状态完成！ Size【" + taskLogList.size() + "】");
//				 	}
//			 	
//			 		
//			 	}
			 	// 20120628 gaoyh 更新状态的处理放到TaskLogAndMailSyn end
			 	
			 	// SESSION LOG START
			 	repSql = infaCommonService.getIbatisSql("InfaSessLog.selectRepSessLog", null);
				// 对所获取的SQL的参数进行设置
				repSql = MessageFormat.format(repSql, 
						StringUtils.collectionToCommaDelimitedString(synDtl.getWfRunIdList()), 
						StringUtils.collectionToCommaDelimitedString(synDtl.getWfIdList()));
				logger.info("SESSION日志查询用SQL： " + repSql);
				// 执行查询（REP_SESS_LOG）
				pstmt = conn.prepareStatement(repSql);
			 	rs = pstmt.executeQuery();
			 	// 对结果集进行转换
			 	sLogList = new ArrayList<InfaSessLog>();
			 	while (rs.next()) {
			 		sLog = new InfaSessLog();
			 		sLog.setSubjectArea(rs.getString("SUBJECT_AREA"));
			 		sLog.setSubjectId(rs.getLong("SUBJECT_ID"));
			 		sLog.setSessionName(rs.getString("SESSION_NAME"));
			 		sLog.setSessionId(rs.getLong("SESSION_ID"));
			 		sLog.setSessionInstanceName(rs.getString("SESSION_INSTANCE_NAME"));
			 		sLog.setSuccessfulRows(rs.getLong("SUCCESSFUL_ROWS"));
			 		sLog.setFailedRows(rs.getLong("FAILED_ROWS"));
			 		sLog.setSuccessfulSourceRows(rs.getLong("SUCCESSFUL_SOURCE_ROWS"));
			 		sLog.setFailedSourceRows(rs.getLong("FAILED_SOURCE_ROWS"));
			 		sLog.setFirstCode(rs.getLong("FIRST_ERROR_CODE"));
			 		sLog.setFirstMsg(rs.getString("FIRST_ERROR_MSG"));
			 		sLog.setLastCode(rs.getLong("LAST_ERROR_CODE"));
			 		sLog.setLastError(rs.getString("LAST_ERROR"));
			 		sLog.setRunCode(rs.getLong("RUN_STATUS_CODE"));
			 		sLog.setActualStart(rs.getTimestamp("ACTUAL_START"));
			 		sLog.setSessionTimestamp(rs.getTimestamp("SESSION_TIMESTAMP"));
			 		sLog.setSessionFile(rs.getString("SESSION_LOG_FILE"));
			 		sLog.setBadLocation(rs.getString("BAD_FILE_LOCATION"));
			 		sLog.setTaskNumber(rs.getLong("TASK_VERSION_NUMBER"));
			 		sLog.setWorkflowNumber(rs.getLong("WORKFLOW_VERSION_NUMBER"));
			 		sLog.setWorkflowName(rs.getString("WORKFLOW_NAME"));
			 		sLog.setMappingName(rs.getString("MAPPING_NAME"));
			 		sLog.setTotalErr(rs.getLong("TOTAL_ERR"));
			 		sLog.setWorkflowId(rs.getLong("WORKFLOW_ID"));
			 		sLog.setWorkflowRunId(rs.getLong("WORKFLOW_RUN_ID"));
			 		sLog.setWorkletId(rs.getLong("WORKLET_RUN_ID"));
			 		sLog.setInstanceId(rs.getLong("INSTANCE_ID"));
			 		sLog.setInfomaticId(synDtl.getInfaId());
			 		
			 		if (logger.isDebugEnabled()) {
			 			logger.debug("Infa_session_log插入数据：【" + sLog.toString() + "】");
			 		}
			 		// 添加到结果集
			 		sLogList.add(sLog);
			 	}

			 	// session_log插入开始
			 	infaCommonService.batchAddObject("InfaSessLog.insertInfaSessLog", sLogList);
			 	if (logger.isInfoEnabled()) {
			 		logger.info("session_log插入开始完成！ Size【" + sLogList.size() + "】");
			 	}
			 	
			} finally {
				// 2012/06/27 gaoyh 异常处理统一由入口处处理
//				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
					if (logger.isInfoEnabled()) {
						logger.info("sql connection is closed!");
					}
//				} catch (SQLException e) {
//					if (logger.isInfoEnabled()) {
//						logger.info("close connection is exception: ", e);
//					}
//				}
			
			}
		}
	}

	/**
	 * 获取同步用Informatica详细列表
	 * @param wflogList
	 * @return
	 * @throws BusinessException
	 */
	private List<InfaSynDetail> getInfaSynDtlList(
			List<InfaSynBaseInfo> wflogList) throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("获取同步用Informatic详细信息列表 Start! size【" + wflogList.size() + "】");
		}
		
		long preInfaId = 0;
		long infaId = 0;
		InfaSynDetail infaSynDetail = null;
		InfaDetail infaDtl = null;
		List<InfaSynDetail> infaSynDtlList = new ArrayList<InfaSynDetail>();
		for (InfaSynBaseInfo record : wflogList) {
			// 当前记录是否有效
			if (!record.isValid()) {
				if (logger.isDebugEnabled()) {
					logger.debug("无效的记录 ：" + record.getInfaId() + ", " + record.getDbId());
				}
				
				continue;
			}
			// 调用失败的WorkFlow不需要同步
			if (-1 == record.getRunId()) {
				if (logger.isDebugEnabled()) {
					logger.debug("调用失败的WorkFlow不需要同步! InfaId【" + record.getInfaId() 
							+ "】, TaskLogId【" + record.getTasklogid() 
							+ "】， SubTaskLogId【" + record.getSubTasklogid() + "】");
				}
				continue;
			}
			infaId = record.getInfaId();
			if (logger.isDebugEnabled()) {
				logger.debug("记录信息 ：preInfaId【" + preInfaId + "】, InfaId【" + record.getInfaId() + "】");
			}
			// 当前记录与前条记录是否为同一Informatica
			if (0 == preInfaId || preInfaId != infaId) {
				preInfaId = infaId;
				// 获取Informatica详细信息
				infaDtl = getInfaSynDetail(infaId, record.getDbId());
				if (null  == infaDtl) {
					continue;
				}
				infaSynDetail = new InfaSynDetail();
				infaSynDetail.setInfaId(infaId);
				infaSynDtlList.add(infaSynDetail);
				infaSynDetail.setInfaDetail(infaDtl);
			}
			// infaId与DbId不匹配，则跳过并记录Warn信息
			if (null == infaSynDetail) {
				logger.warn("无法获取Informatica服务器信息！ infaID【" + infaId + "】");
				continue;
			}
			
			// 添加对应关系
			infaSynDetail.addToRelMap(record.getRunId(), record.getTasklogid());
			if (0 != record.getSubTasklogid()) {
				infaSynDetail.addToSubRelMap(record.getRunId(), record.getSubTasklogid());
			}
			logger.info("子任务对应关系： 【WorkFlowRunId:" + record.getRunId() 
					+ ", TaskLogId:" + infaSynDetail.getRel(record.getRunId())
					+ ", SubTaskLogId:" + infaSynDetail.getSubRel(record.getRunId()) + "】");
			// 添加参数
			infaSynDetail.addWfIdList(record.getWorkflowId());
			infaSynDetail.addWrRunIdList(record.getRunId());
			if (logger.isDebugEnabled()) {
				logger.debug("同步用Informatic详细信息参数列表  size【" + infaSynDetail.getWfIdList().size() + "】");
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("获取同步用Informatic详细信息列表 End! size【" + infaSynDtlList.size() + "】");
		}
		return infaSynDtlList;
	}

	/**
	 * 获取Informatic详细信息
	 * @param infaId
	 * @param dbId
	 * @return
	 * @throws BusinessException 
	 */
	private InfaDetail getInfaSynDetail(long infaId, long dbId) 
			throws BusinessException {
		logger.info("获取同步用Informatic详细信息 Start ： infaId[" + infaId + "] dbId[" + dbId + "]");
		InfaDetail para = new InfaDetail();
		para.setInfaId(infaId);
		para.setDbId(dbId);
		
		return (InfaDetail) infaCommonService.findObject("informatica.selectInfaDetail", para);
	}

	/**
	 * 从平台数据库中查找没有运行结束的记录
	 * @return
	 * @throws BusinessException 
	 */
	private List<InfaSynBaseInfo> getUnLogedWFList() throws BusinessException {
		return (List<InfaSynBaseInfo>)infaCommonService.findObjects(
				"InfaSyn.selectUnSynInfaInfo", null);
	}
}
