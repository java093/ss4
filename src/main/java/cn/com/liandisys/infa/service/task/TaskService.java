package cn.com.liandisys.infa.service.task;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.idev.ssi.service.impl.BaseServiceIbatisImpl;
import cn.com.liandisys.infa.entity.job.Task;
import cn.com.liandisys.infa.entity.job.WorkFlow;
import cn.com.liandisys.infa.entity.mng.LogList;
import cn.com.liandisys.infa.entity.mng.LogRelation;
import cn.com.liandisys.infa.entity.sys.DataBase;
import cn.com.liandisys.infa.entity.sys.Informatica;
import cn.com.liandisys.infa.entity.taskDetail.TaskDetail;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.workflow.WorkFlowException;
import cn.com.liandisys.infa.util.workflow.WorkFlowOperateUtil;

import com.informatica.powercenter.sdk.lm.IJLMWorkflow;

/**
 * 
 * @param <T>
 * 
 * 
 */
// Spring Bean的标识.
@Service("taskService")
public class TaskService<T> extends BaseServiceIbatisImpl<T> {

	@Autowired
	private InfaCommonService infaCommonService;
	private static Logger logger = LoggerFactory.getLogger(TaskService.class);
	private long parentID;
	private long Task_id;
	private int Tasktype;
	private final int WORKFLOW = 0;
	private final int TASK = 1;
	private final int SUBTASK = 2;
	private long Task_log_id = 0; // 主任务LOG的自增长ID
	private long SubTask_id = 0; // 子任务LOG的自增长ID
	private Boolean flag = false; // 是否串联
	private Boolean errer_flag = false;

	// 线程启动workflow用
//	private Informatica informatica;
//	private DataBase db;
	private WorkFlow wf;

	/**
	 * 单独执行任务的workflow
	 * 
	 * @param id
	 *            workflow的ID
	 * @param taskLogID
	 *            主任务ID
	 * @param taskLogID
	 *            子任务ID
	 */
	public void startTask(long id, long taskID, long SubTaskID) {
		this.Task_id = id;
		Tasktype = WORKFLOW;
		Timestamp start_time = new Timestamp(System.currentTimeMillis());
		if (taskID != 0) {
			Task_log_id = getTaskID();
			addLog(Task_log_id, taskID, start_time);
		}
		if (SubTaskID != 0) {
			SubTask_id = getSubTaskID();
			addSublog(SubTask_id, SubTaskID, start_time, Task_log_id);
		}
		Start();
	}

	/**
	 * 根据主任务id查找并执行所有子任务和workflow
	 * 
	 * @param id
	 *            Task或workflow的ID
	 * @param Tasktype
	 *            数据类型-1:代表单独运行一个子任务，1：代表传递的是Task的ID，2：代表传递的是子任务ID
	 * @param parentID
	 *            子任务的父taskID，如果是主任务0
	 */
	public void startTask(long id, int Tasktype, long parentID) {
		this.parentID = parentID;
		this.Task_id = id;
		this.Tasktype = Tasktype;
		Start();
	}

	private void Start() {
		if (Tasktype == WORKFLOW) {
			try {
				int runID = runWorkFlow(getWorkFlow(Task_id));
				if (runID != 0) {
					addLogRelation(Task_log_id, SubTask_id, Task_id, runID);
				}else{					
					addLogRelation(Task_log_id, SubTask_id, Task_id, -1);
				}
			} catch (BusinessException e) {
				Timestamp end_time = new Timestamp(System.currentTimeMillis());
				updateSubTaskLog(
						SubTask_id,
						end_time,
						"workflow异常，workflow_id： " + Task_id + " 异常状态为："
								+ e.getMessage());
				updateLog(Task_log_id, end_time, "workflow异常，workflow_id： "
						+ Task_id + " 异常状态为：" + e.getMessage());
				addLogRelation(Task_log_id, SubTask_id, Task_id, -1);
				return;
			} catch (WorkFlowException e) {
				Timestamp end_time = new Timestamp(System.currentTimeMillis());
				updateSubTaskLog(
						SubTask_id,
						end_time,
						"workflow连接异常，workflow_id： " + Task_id + " 异常状态为："
								+ e.getMessage());
				updateLog(Task_log_id, end_time, "workflow连接异常，workflow_id： "
						+ Task_id + " 异常状态为：" + e.getMessage());
				addLogRelation(Task_log_id, SubTask_id, Task_id, -1);
				return;
			}
		} else if (Tasktype == TASK || Tasktype == SUBTASK || Tasktype == -1) {

			try {
				// 获取主任务的信息
				Task task = getTask(Task_id);
				if (null == task) {
					updateAllLog("任务不存在taskid：" + Task_id);
					return;
				}
				Timestamp start_time = new Timestamp(System.currentTimeMillis());
				logger.info("Task的状态：" + Tasktype);
				if (task.getTYPE().equals("0")) {
					Tasktype = TASK;
					Task_log_id = getTaskID();
					addLog(Task_log_id, Task_id, start_time);
				} else if (task.getTYPE().equals("1")) {
					if(Tasktype == -1 ){
						Task_log_id = getTaskID();
						addLog(Task_log_id, parentID, start_time);
					}else{						
						Task_log_id = parentID;
					}
					SubTask_id = getSubTaskID();
					addSublog(SubTask_id, Task_id, start_time, Task_log_id);
					Tasktype = SUBTASK;
				}
				// 获取当前主任务下所有的任务关系表
				List<TaskDetail> alltd = getAllDetail(Task_id);
				if (null == alltd || alltd.isEmpty()) {
					//当任务为空的时候直接添加结束时间和状态 2012.06.28 李明璞
					Timestamp end_time = new Timestamp(System.currentTimeMillis());
					LogList logList = new LogList();
					logList.setEnd_time(end_time);
					logList.setDetail("任务下没有查询到workflow，taskid： " + Task_id);
					if(Tasktype == TASK){
						logList.setId(Task_log_id);
						infaCommonService.modifyObject("log.updateLog", logList);
					}else if(Tasktype == SUBTASK){
						logList.setId(SubTask_id);
						infaCommonService.modifyObject("log.updateSubLog", logList);
					}
					//当任务为空的时候log关系表添加数据 workflowid=0，rinid=-1 2012.06.29 李明璞
					addLogRelation(Task_log_id, SubTask_id, 0, -1);
					return;
				}
				if (task.getRUNTYPE().equals("1")) {
					// 主任务并行
					for (int i = 0; i < alltd.size(); i++) {
						if (alltd.get(i).getSubtaskID() != null
								&& !alltd.get(i).getSubtaskID().equals("")) {
							// 主任务下的子任务
							logger.info("主任务下的子任务,主任务并行");
							startTask(
									Long.valueOf(alltd.get(i).getSubtaskID()
											.toString()), SUBTASK, Task_log_id);
						} else if (alltd.get(i).getWorkflowID() != null
								&& !alltd.get(i).getWorkflowID().equals("")) {
							// 任务下的workflow
							logger.info("主任务下的workflow,主任务并行");
							try {
								if (Tasktype == TASK) {
									SubTask_id = 0;
								}
								wf = getWorkFlow(alltd.get(i).getWorkflowID()
										.longValue());
								if (null == wf) {
									if (logger.isWarnEnabled()) {
										logger.warn("workflow不存在，workflow_id： "
												+ alltd.get(i).getWorkflowID()
														.longValue());
									}
								} else {

									// 线程启动并联workflow，但是在线程中不进行数据库操作，不好存runID
									// ThreadPoolUtil tup = new
									// ThreadPoolUtil(0);
									// informatica =
									// getInformatica(wf.getINFA_ID());
									// if (null == informatica) {
									// if (logger.isWarnEnabled()) {
									// logger.warn("没有查询到informatica信息，informatica： "
									// + wf.getINFA_ID());
									// }
									// }
									// db =
									// getDataBase(Long.valueOf(informatica.getKnowledge_base()));
									// if (null == db) {
									// if (logger.isWarnEnabled()) {
									// logger.warn("没有查询到DataBase信息，DataBase： "
									// + informatica.getKnowledge_base());
									// }
									// }
									// tup.setThread(new Thread(){
									// @Override
									// public void run() {
									// try {
									// runThreadWorkFlow(wf, informatica, db);
									// } catch (BusinessException e) {
									// e.printStackTrace();
									// } catch (WorkFlowException e) {
									// e.printStackTrace();
									// }
									// }
									// });

									int runID = runWorkFlow(wf);
									if (runID != 0) {
										addLogRelation(Task_log_id, SubTask_id,
												alltd.get(i).getWorkflowID()
														.longValue(), runID);
									}else{					
										addLogRelation(Task_log_id, SubTask_id, alltd.get(i).getWorkflowID()
												.longValue(), -1);
									}
								}
							} catch (WorkFlowException e) {
								//并行状态下workflow连接异常没差-1状态 2012.06.28 李明璞
								addLogRelation(Task_log_id, SubTask_id, alltd.get(i).getWorkflowID()
										.longValue(), -1);
								if (task.getRUNTYPE().equals("0")) {
									alltd.clear();
								}
								logger.info("workflow连接失败：" + e.getMessage());
							}
						}
					}
					Tasktype = TASK;
				} else {
					// 主任务串行
					if (Tasktype == TASK && task.getRUNTYPE().equals("0")) {
						logger.info("主任务是串联的状态");
						flag = true;
					}
					for (int i = 0; i < alltd.size(); i++) {
						if (errer_flag && flag) {
							alltd.clear();
							return;
						}
						if (Tasktype == TASK) {
							SubTask_id = 0;
						}
						if (alltd.get(i).getSubtaskID() != null
								&& !alltd.get(i).getSubtaskID().equals("")) {
							// 主任务下的子任务
							startTask(alltd.get(i).getSubtaskID().longValue(),
									2, Task_log_id);
							Long subcode = (Long) infaCommonService.findObject("log.SubCode", SubTask_id);
							if(null!= subcode && subcode == Long.valueOf(3)){
								try {
									alltd.clear();
								} catch (Exception e1) {
									return;
								}
								return;
							}
						} else if (alltd.get(i).getWorkflowID() != null
								&& !alltd.get(i).getWorkflowID().equals("")) {
							// 主任务下的workflow
							try {
								WorkFlow wf = getWorkFlow(alltd.get(i)
										.getWorkflowID().longValue());
								if (null == wf) {
									errer_flag = true;
									updateAllLog("workflow不存在，workflow_id： "
											+ alltd.get(i).getWorkflowID());
									if (flag) {
										Timestamp end_time = new Timestamp(
												System.currentTimeMillis());
										updateLog(
												Task_log_id,
												end_time,
												"workflow不存在，workflow_id： "
														+ alltd.get(i)
																.getWorkflowID());
										try {
											alltd.clear();
										} catch (Exception e1) {
											Tasktype = TASK;
											return;
										}
									}
									return;
								} else {
									int runID = runWorkFlow(wf);
									//关系表插入时对当前Workflow所属关系判断
									if (Tasktype == TASK) {
										SubTask_id = 0;
									}
									if (runID != 0) {
										addLogRelation(Task_log_id, SubTask_id,
												alltd.get(i).getWorkflowID()
														.longValue(), runID);
									}else{				
										//关系表workflowID插入修改，原来插成了task的ID
										addLogRelation(Task_log_id, SubTask_id, alltd.get(i).getWorkflowID()
												.longValue(), -1);
										try {
											alltd.clear();
										} catch (Exception e1) {
											Tasktype = TASK;
											return;
										}
									}
								}
							} catch (WorkFlowException e) {
								addLogRelation(Task_log_id, SubTask_id, alltd.get(i).getWorkflowID()
										.longValue(), -1);
								errer_flag = true;
								updateAllLog("workflow连接异常，workflow_id： "
										+ alltd.get(i).getWorkflowID()
										+ " 异常状态为：" + e.getMessage());
								if (flag) {
									Timestamp end_time = new Timestamp(
											System.currentTimeMillis());
									updateLog(
											Task_log_id,
											end_time,
											"workflow连接异常，workflow_id： "
													+ alltd.get(i)
															.getWorkflowID()
													+ " 异常状态为："
													+ e.getMessage());
								}
								try {
									alltd.clear();
								} catch (Exception e1) {
									Tasktype = TASK;
									return;
								}
								return;
							}
						}

					}
				}
			} catch (BusinessException e) {
				if (logger.isWarnEnabled()) {
					logger.warn(e.getMessage());
					return;
				}
			}

		}
		Tasktype = TASK;
	}

	/**
	 * 根据id查找WorkFlow
	 * 
	 * @param id
	 *            WorkFlow的ID
	 * @return 返回WorkFlow信息
	 * @throws SQLException
	 * @throws BusinessException
	 */
	private WorkFlow getWorkFlow(long id) throws BusinessException {
		logger.info("运行的workflowID：" + id);
		WorkFlow wf = new WorkFlow();
		wf.setId(Long.valueOf(id));
		WorkFlow rwf = null;
		rwf = (WorkFlow) findObject("workflow.findByid", wf);
		return rwf;
	}

	/**
	 * 根据id查找主任务下所有的任务
	 * 
	 * @param id
	 *            主任务的ID
	 * @return 返回WorkFlow信息
	 * @throws SQLException
	 * @throws BusinessException
	 */
	private List<TaskDetail> getAllDetail(long id) throws BusinessException {
		logger.info("运行的主任务的ID：" + id);
		TaskDetail td = new TaskDetail();
		td.setTask_ID(BigDecimal.valueOf(id));
		List<TaskDetail> rtd = (List<TaskDetail>) findObjects("td.findByid", td);
		logger.info(rtd.size() + "  主任务下的任务个数");
		return rtd;
	}

	/**
	 * 获取主任务信息
	 * 
	 * @param id
	 *            主任务的ID
	 * @return 返回WorkFlow信息
	 * @throws SQLException
	 * @throws BusinessException
	 */
	private Task getTask(long id) throws BusinessException {
		Task td = new Task();
		td.setId(id);
		Task rtd = (Task) findObject("task.findById", td);
		return rtd;
	}

	/**
	 * 连接运行WorkFlow
	 * 
	 * @param wf
	 *            workflow
	 * @return 返回任务list，其中包括了子任务和workflow
	 * @throws WorkFlowException
	 * @throws BusinessException
	 */
	private int runWorkFlow(WorkFlow wf) throws WorkFlowException,
			BusinessException {
		if (null == wf.getINFA_ID()) {
			if (logger.isWarnEnabled()) {
				logger.warn("workflow没有关联的informatica，WorkFlowid： "
						+ wf.getId());
			}
			return 0;
		}
		Informatica informatica = getInformatica(wf.getINFA_ID());
		if (null == informatica) {
			if (logger.isWarnEnabled()) {
				logger.warn("没有查询到informatica信息，informatica： "
						+ wf.getINFA_ID());
			}
			return 0;
		}
		DataBase db = getDataBase(Long.valueOf(informatica.getKnowledge_base()));
		if (null == db) {
			if (logger.isWarnEnabled()) {
				logger.warn("没有查询到DataBase信息，DataBase： "
						+ informatica.getKnowledge_base());
			}
			return 0;
		}
		IJLMWorkflow wk = WorkFlowOperateUtil.getWorkFlow(informatica.getIp(),
				informatica.getPort(), db.getDbname(),
				informatica.getUser_name(), informatica.getPassword(),
				wf.getSUBJECT_AREA(), wf.getWORKFLOW_NAME());
		int runID = WorkFlowOperateUtil.startWorkFlow(wk);
		return runID;
	}

	/**
	 * 连接运行WorkFlow
	 * 
	 * @param wf
	 *            workflow
	 * @param informatica
	 *            informatica
	 * @param db
	 *            DataBase
	 * @return 返回任务list，其中包括了子任务和workflow
	 * @throws WorkFlowException
	 * @throws BusinessException
	 */
	// private int runThreadWorkFlow(WorkFlow wf , Informatica informatica ,
	// DataBase db) throws WorkFlowException,
	// BusinessException {
	// IJLMWorkflow wk = WorkFlowOperateUtil.getWorkFlow(informatica.getIp(),
	// informatica.getPort(), db.getDbname(),
	// informatica.getUser_name(), informatica.getPassword(),
	// wf.getSUBJECT_AREA(), wf.getWORKFLOW_NAME());
	// int runID = WorkFlowOperateUtil.startWorkFlow(wk);
	// return runID;
	// }

	/**
	 * 查询Informatica信息
	 * 
	 * @param informaticaID
	 *            informatica的ID
	 * @throws BusinessException
	 * 
	 * @throws SQLException
	 */
	private Informatica getInformatica(long informaticaID)
			throws BusinessException {
		Informatica informatica = new Informatica();
		informatica.setId(informaticaID);
		Informatica rinformatica = (Informatica) findObject(
				"informatica.findByid", informatica);
		return rinformatica;
	}

	/**
	 * 查询DataBase信息
	 * 
	 * @param DataBaseID
	 *            DataBase的ID
	 * @throws BusinessException
	 * 
	 * @throws SQLException
	 */
	private DataBase getDataBase(long DataBaseID) throws BusinessException {
		DataBase db = new DataBase();
		db.setId(DataBaseID);
		DataBase rdb = (DataBase) findObject("db.findByid", db);
		return rdb;
	}

	/**
	 * 插入LOG关系
	 * 
	 * @param Tasklog_id
	 *            主任务的LOG ID
	 * @param subTasklog_id
	 *            子任务LOG ID
	 * @param workflow_id
	 *            workflow的ID
	 * @param runID
	 *            workflow的运行ID
	 * @throws SQLException
	 */
	private void addLogRelation(long Tasklog_id, long subTasklog_id,
			long workflow_id, long runID) {
		logger.info("关系表，主任务LOGID：" + Tasklog_id);
		logger.info("关系表，子任务LOGID：" + subTasklog_id);
		logger.info("关系表，workflow_id：" + workflow_id);
		logger.info("关系表，runID：" + runID);
		LogRelation lr = new LogRelation();
		lr.setTASK_LOGID(String.valueOf(Tasklog_id));
		lr.setSUB_TASKLOGID(String.valueOf(subTasklog_id));
		lr.setWORKFLOW_ID(String.valueOf(workflow_id));
		lr.setRUN_ID(String.valueOf(runID));
		try {
			infaCommonService.addObject("log.insertRelation", lr);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取任务Log自增长ID
	 * 
	 * @return 主任务自增长ID
	 * @throws SQLException
	 */
	private long getTaskID() {
		BigDecimal id = null;
		try {
			id = (BigDecimal) findObject("td.gettaskLogID", null);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return id.longValue();
	}

	/**
	 * 获取子任务Log自增长ID
	 * 
	 * @return 主任务自增长ID
	 * @throws SQLException
	 */
	private long getSubTaskID() {
		BigDecimal id = null;
		try {
			id = (BigDecimal) findObject("td.getSubTaskLogID", null);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return id.longValue();
	}

	/**
	 * 添加主任务的LOG
	 * 
	 * @param id
	 *            数据库这行字段的ID
	 * @param task_id
	 *            主任务的ID
	 * @param start_time
	 *            主任务的开始时间
	 * @throws SQLException
	 */
	private void addLog(long id, long task_id, Timestamp start_time) {
		LogList logList = new LogList();
		logList.setId(id);
		logList.setTask_id(BigDecimal.valueOf(task_id));
		logList.setStart_time(start_time);
		logger.info("主任务LOG ID：" + logList.getId());
		logger.info("主任务LOG task_id：" + logList.getTask_id());
		logger.info("主任务LOG start_time：" + logList.getStart_time());
		try {
			infaCommonService.addObject("log.insertLog", logList);
		} catch (BusinessException e) {
			logger.info("主任务LOG 插入失败：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 添加子任务的LOG
	 * 
	 * @param id
	 *            子任务的唯一ID
	 * @param task_id
	 *            子任务的ID
	 * @param start_time
	 *            主任务的开始时间
	 * @param parentID
	 *            父任务ID
	 * @throws SQLException
	 */
	private void addSublog(long id, long task_id, Timestamp start_time,
			long parentID) {
		logger.info("子任务LOG ID：" + id);
		logger.info("子任务LOG task_id：" + task_id);
		logger.info("子任务LOG start_time：" + start_time.toString());
		logger.info("子任务LOG parentID：" + parentID);
		LogList logList = new LogList();
		logList.setId(id);
		logList.setTask_id(BigDecimal.valueOf(task_id));
		logList.setStart_time(start_time);
		logList.setTask_id_id(BigDecimal.valueOf(parentID));
		try {
			infaCommonService.addObject("log.insertSubLog", logList);
		} catch (BusinessException e) {
			logger.info("子任务LOG 插入失败：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 更新主任务LOG
	 * 
	 * @param id
	 *            主任务的ID
	 * @param end_time
	 *            主任务的结束时间
	 * @param detail
	 *            主任务的详细说明
	 */
	private void updateLog(long id, Timestamp end_time, String detail) {
		LogList logList = new LogList();
		logList.setId(id);
//		logList.setEnd_time(end_time);
		logList.setDetail(detail);
		try {
			infaCommonService.modifyObject("log.updateLog", logList);
		} catch (BusinessException e) {
			logger.info("主任务LOG 更新失败：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 更新子任务LOG
	 * 
	 * @param task_id
	 *            主任务的ID
	 * @param subtask_id
	 *            ID
	 * @param end_time
	 *            子任务的结束时间
	 * @param detail
	 *            子任务的详细说明
	 * 
	 */
	private void updateSubTaskLog(long id, Timestamp end_time, String detail) {
		LogList logList = new LogList();
		logList.setId(id);
//		logList.setEnd_time(end_time);
		logList.setDetail(detail);
		try {
			infaCommonService.modifyObject("log.updateSubLog", logList);
		} catch (BusinessException e) {
			logger.info("子任务LOG更新 失败：" + e.getMessage());
			e.printStackTrace();
		}
	}

	// /**
	// * 更新workflow_run表
	// *
	// * @param workflowID
	// * workflow ID
	// * @param runID
	// * workflow runID
	// */
	// private void addworkflow_run(long workflowID,long runID) {
	// Map<String, Long> map = new HashMap<String, Long>();
	// map.put("WORKFLOW_ID", workflowID);
	// map.put("WORKFLOW_RUN_ID", runID);
	// try {
	// infaCommonService.modifyObject("log.insert_wflow_run", map);
	// } catch (BusinessException e) {
	// logger.info("workflow_run插入 失败：" + e.getMessage());
	// e.printStackTrace();
	// }
	// }
	/**
	 * 更新任务LOG
	 * 
	 * @param detail
	 *            任务失败信息
	 * 
	 */
	private void updateAllLog(String detail) {
		Timestamp end_time = new Timestamp(System.currentTimeMillis());
		if (logger.isWarnEnabled()) {
			logger.warn(detail);
			if (Tasktype == TASK) {
				if (Task_log_id == 0) {
					Task_log_id = getTaskID();
					addLog(Task_log_id, Task_id, end_time);
				}
				updateLog(Task_log_id, end_time, detail);
			} else if (Tasktype == SUBTASK) {
				if (SubTask_id == 0) {
					SubTask_id = getSubTaskID();
					addSublog(SubTask_id, Task_id, end_time, parentID);
				}
				updateSubTaskLog(SubTask_id, end_time, detail);
			}
		}
	}
}
