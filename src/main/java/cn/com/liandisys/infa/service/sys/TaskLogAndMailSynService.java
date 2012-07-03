package cn.com.liandisys.infa.service.sys;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.index.TaskLogTime;
import cn.com.liandisys.infa.entity.syn.TaskLogSyn;
import cn.com.liandisys.infa.entity.syn.TaskSynBaseLog;
import cn.com.liandisys.infa.entity.syn.TaskSynDetail;
import cn.com.liandisys.infa.entity.sys.MailConfig;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.mail.MailInfoBean;
import cn.com.liandisys.infa.util.mail.SendMailUtil;

/**
 * Log状态设置及提醒邮件发送用任务执行类
 * 
 * @author limp
 * 
 * @param <T>
 */
@Service("tasklogSynService")
@Transactional(readOnly = true)
public class TaskLogAndMailSynService {
	/** Logger */
	private static Logger logger = LoggerFactory
			.getLogger(TaskLogAndMailSynService.class);
	@Autowired
	private InfaCommonService infaCommonService;
	
	private boolean mailerrer = false;

	/**
	 * 执行逻辑
	 * 
	 * @throws BusinessException
	 */
	public void excuteWFSyn() throws BusinessException {
		// 获取需要同步的原始数据
		List<TaskSynDetail> detail = getAllDetail();
		if (null == detail || detail.isEmpty()) {
			logger.info("没有需要同步的日志数据！");
			return;
		}
		// 对数据进行分组
		List<TaskSynBaseLog> dataGrouping = DataGrouping(detail);
		if (null == dataGrouping || dataGrouping.isEmpty()) {
			logger.info("数据分组异常！");
			return;
		}
		// 获取邮件配置信息
		MailConfig getMail = getMail();
		if(null == getMail){
			logger.info("发件人数据获取异常");
			mailerrer = true;
		}

		// 对任务（子任务）日志表进行更新，并发送提醒邮件
		synInfaData(dataGrouping, getMail);
	}

	/**
	 * 数据同步处理
	 * 
	 * @param infaSynDtlList
	 * @throws BusinessException
	 */
	@Transactional(readOnly = false)
	private void synInfaData(List<TaskSynBaseLog> dataGrouping,
			MailConfig getMail) throws BusinessException {
		for (int i = 0; i < dataGrouping.size(); i++) {
			// logger.info("主任务Log的ID：" + dataGrouping.get(i).getTaskLogID());
			// logger.info("主任务下子子任务的个数："
			// + dataGrouping.get(i).getSubtasklogID().size());
			// for (int x = 0; x < dataGrouping.get(i).getSubtasklogID().size();
			// x++) {
			// logger.info("主任务子任务的LOGID："
			// + dataGrouping.get(i).getSubtasklogID().get(x));
			// if (dataGrouping.get(i).getSubtasklogID().get(x).equals("0")) {
			// logger.info("主任务下子workfl的个数aaaaaa："
			// + dataGrouping.get(i).getRunID()
			// .get(dataGrouping.get(i).getTaskLogID())
			// .size());
			// } else {
			// logger.info("主任务的子任务下workfl的个数bbbbbb："
			// + dataGrouping
			// .get(i)
			// .getRunID()
			// .get(dataGrouping.get(i).getSubtasklogID().get(x)).size());
			// }
			// }

			matchedData(dataGrouping.get(i), getMail);
		}
	}

	/**
	 * 根据主任务是否有结束时间查询关系表
	 * 
	 * @throws BusinessException
	 */
	private List<TaskSynDetail> getAllDetail() throws BusinessException {
		return (List<TaskSynDetail>) infaCommonService.findObjects(
				"TaskSyn.selectAllDetail", null);
	}

	/**
	 * 查询邮件发送人信息
	 * 
	 * @throws BusinessException
	 */
	private MailConfig getMail() throws BusinessException {
		return (MailConfig) infaCommonService.findObject("mail.findAllMail",
				null);
	}

	/**
	 * 根据主log进行分组数据
	 */
	private List<TaskSynBaseLog> DataGrouping(List<TaskSynDetail> detail) {
		List<TaskSynBaseLog> taskBaseLog = new ArrayList<TaskSynBaseLog>();
		TaskSynBaseLog baseLog = null;
		List<String> subtasklogID = null;
		List<String> subtaskID = null;
		List<Timestamp> subend_time = null;
		List<String> subtaskRunType = null;
		List<String> subtaskmail_id = null;
		List<String> run = null;
		Map<String, List<String>> runID = null;
		String tasklogID = null;
		boolean flag = true;
		if (null == detail || detail.size() == 0) {
			if (logger.isDebugEnabled()) {
				logger.info("无需要更新的log");
			}
			return null;
		}
		for (int i = 0; i < detail.size(); i++) {
			if (i == 0
					|| !detail.get(i - 1).getTasklog()
							.equals(detail.get(i).getTasklog())) {
				if (i != 0) {
					if (detail.get(i - 1).getSubtasklog().equals("0")) {
						runID.put(tasklogID, run);
					} else {
						runID.put(detail.get(i - 1).getSubtasklog(), run);
					}

					Iterator<Entry<String, List<String>>> tIt = runID
							.entrySet().iterator();
					while (tIt.hasNext()) {
						Map.Entry<String, List<String>> ent = tIt.next();

						logger.info("dddddddddddddddddd KEY"
								+ Long.parseLong(ent.getKey()));
						for (String l : ent.getValue()) {
							logger.info("dddddddddddddddddd  VALUE" + l);
						}
					}
					baseLog.setSubtasklogID(subtasklogID);
					baseLog.setSubtaskID(subtaskID);
					baseLog.setSubend_time(subend_time);
					baseLog.setSubtaskRunType(subtaskRunType);
					baseLog.setSubtaskmail_id(subtaskmail_id);
					baseLog.setRunID(runID);

					taskBaseLog.add(baseLog);
					flag = false;
				}
				baseLog = new TaskSynBaseLog();
				subtasklogID = new ArrayList<String>();
				subtaskID = new ArrayList<String>();
				subend_time = new ArrayList<Timestamp>();
				subtaskRunType = new ArrayList<String>();
				subtaskmail_id = new ArrayList<String>();
				runID = new HashMap<String, List<String>>();

				tasklogID = detail.get(i).getTasklog();
				baseLog.setTaskLogID(detail.get(i).getTasklog());
				baseLog.setTaskID(detail.get(i).getTask_id());
				baseLog.setTaskRunType(detail.get(i).getTask_runtype());
				baseLog.setTaskmail_id(detail.get(i).getTaskmail_id());
			}
			if (i == 0
					|| !detail.get(i - 1).getSubtasklog()
							.equals(detail.get(i).getSubtasklog()) || !flag) {
				if (i != 0) {
					if (flag) {
						if (detail.get(i - 1).getSubtasklog().equals("0")) {
							runID.put(tasklogID, run);
						} else {
							runID.put(detail.get(i - 1).getSubtasklog(), run);
						}

						Iterator<Entry<String, List<String>>> tIt = runID
								.entrySet().iterator();
						while (tIt.hasNext()) {
							Map.Entry<String, List<String>> ent = tIt.next();

							logger.info("aaaaaaaaaaaaaaaaaaaaaaa KEY"
									+ Long.parseLong(ent.getKey()));
							logger.info("aaaaaaaaaaaaaaaaaaaaaaa VALUE"
									+ ent.getValue().get(0));
						}
					}
				}
				subtasklogID.add(detail.get(i).getSubtasklog());
				subtaskID.add(detail.get(i).getSubtask_id());
				subend_time.add(detail.get(i).getSubend_time());
				subtaskRunType.add(detail.get(i).getSubtask_runtype());
				subtaskmail_id.add(detail.get(i).getSubtaskmaile_id());
				run = new ArrayList<String>();
				flag = true;
			}
			run.add(detail.get(i).getRun_id());

			if (i == detail.size() - 1) {
				if (detail.get(i - 1).getSubtasklog().equals("0")) {
					runID.put(tasklogID, run);
				} else {
					runID.put(detail.get(i - 1).getSubtasklog(), run);
				}

				Iterator<Entry<String, List<String>>> tIt = runID.entrySet()
						.iterator();
				while (tIt.hasNext()) {
					Map.Entry<String, List<String>> ent = tIt.next();

					logger.info("dddddddddddddddddd KEY"
							+ Long.parseLong(ent.getKey()));
					for (String l : ent.getValue()) {
						logger.info("dddddddddddddddddd  VALUE" + l);
					}
				}
				baseLog.setSubtasklogID(subtasklogID);
				baseLog.setSubtaskID(subtaskID);
				baseLog.setSubend_time(subend_time);
				baseLog.setSubtaskRunType(subtaskRunType);
				baseLog.setSubtaskmail_id(subtaskmail_id);
				baseLog.setRunID(runID);

				taskBaseLog.add(baseLog);
			}
		}
		return taskBaseLog;
	}

	/**
	 * 匹配数据
	 * 
	 * @throws BusinessException
	 */
	private boolean matchedData(TaskSynBaseLog taskSynBaseLog, MailConfig Mail)
			throws BusinessException {
		// int all_count = (Integer) findObject("td.TaskCount",
		// Long.valueOf(taskSynBaseLog.getTaskID()));

		// 存储需要更新的task信息
		List<TaskLogTime> updateTask = new ArrayList<TaskLogTime>();
		// 进行子任务的更新
		for (int i = 0; i < taskSynBaseLog.getSubtasklogID().size(); i++) {
			// 如果子任务有结束时间不进行更新
			if (((null == taskSynBaseLog.getSubend_time().get(i) || taskSynBaseLog
					.getSubend_time().get(i).equals("")) && !taskSynBaseLog
					.getSubtasklogID().get(i).equals("0"))
					&& null != taskSynBaseLog.getSubtaskID().get(i)) {
				// 查询子任务中的workflow的运行状态
				// 如果子任务ID为0代表此子任务work属于主任务下的
				if (!taskSynBaseLog.getSubtasklogID().equals("0")) {
					// 从关系表中查询当前子任务下的workflow的数量
					logger.info("当前子任务的ID："
							+ taskSynBaseLog.getSubtaskID().get(i));
					String subID = taskSynBaseLog.getSubtaskID().get(i);
					Long sub_count = (Long) infaCommonService.findObject(
							"td.TaskCount", Long.valueOf(subID));
					if (null == sub_count) {
						logger.info("为查询到当前子任务的任务执行关系");
						continue;
					}

					logger.info("当前子任务的logID："
							+ taskSynBaseLog.getSubtasklogID().get(i));
					List<TaskLogTime> logTime = (List<TaskLogTime>) infaCommonService
							.findObjects("TaskSyn.selectWorkflowRun", Long
									.valueOf(taskSynBaseLog.getSubtasklogID()
											.get(i)));
					if (null == logTime) {
						logger.info("当前子任务下无完成的workflow");
					}
					if (sub_count != logTime.size()) {
						// 子任务workflow数量与run表不同
						if (taskSynBaseLog.getSubtaskRunType().equals("1")) {
							logger.info("子任务为串联");
							// 当前子任务串行，查询是否有workflow失败的状态
							List<String> runid = taskSynBaseLog.getRunID().get(
									taskSynBaseLog.getSubtasklogID().get(i));
							if (null == runid) {
								logger.info("runid为空");
								continue;
							}
							for (int z = 0; z < runid.size(); z++) {
								if (runid.get(z).equals("-1")) {
									logger.info("串联有连接失败的workflow");
									TaskLogTime tt = new TaskLogTime();
									//添加子任务的结束时间判断，原先的方法取值会去到空 2012.06.28 李明璞
									for (int x = 0; x < logTime.size(); x++) {
										if (null == logTime.get(x).getEND_TIME()) {
											tt.setEND_TIME(logTime.get(x).getSTART_TIME());
										} else {
											if (null == tt.getEND_TIME()
													|| logTime.get(x).getEND_TIME()
															.getTime() < tt.getEND_TIME()
															.getTime()) {
												tt.setEND_TIME(logTime.get(x).getEND_TIME());
											}
											continue;
										}
									}
									tt.setId(Long.valueOf(taskSynBaseLog
											.getSubtasklogID().get(i)));
									tt.setRUN_STATUS_CODE("3");
									updateTask.add(tt);
									if (null != taskSynBaseLog
											.getSubtaskmail_id().get(i)
											&& !taskSynBaseLog
													.getSubtaskmail_id().get(i)
													.equals("")) {
										mail(Mail,
												"Task Journal",
												taskSynBaseLog
														.getSubtaskmail_id()
														.get(i),
												taskSynBaseLog
														.getSubtaskmail_id()
														+ "任务失败");
									}
									continue;
								}
							}
						} else {
							logger.info("子任务为并联，workflow匹配不等: "
									+ taskSynBaseLog.getSubtasklogID().get(i));
							continue;
						}
					} else {
						logger.info("子任务workflow数量与run表相同，更新数据");
						TaskLogTime tt = new TaskLogTime();
						for (int z = 0; z < logTime.size(); z++) {
							if (null == logTime.get(z).getEND_TIME()) {
								tt.setEND_TIME(logTime.get(z).getSTART_TIME());
							} else {
								if (null == tt.getEND_TIME()
										|| logTime.get(z).getEND_TIME()
												.getTime() < tt.getEND_TIME()
												.getTime()) {
									tt.setEND_TIME(logTime.get(z).getEND_TIME());
								}
								continue;
							}
						}
						tt.setId(Long.valueOf(taskSynBaseLog.getSubtasklogID()
								.get(i)));
						tt.setRUN_STATUS_CODE("1");
						if (null != taskSynBaseLog.getSubtaskmail_id().get(i)
								&& !taskSynBaseLog.getSubtaskmail_id().get(i)
										.equals("")) {
							mail(Mail, "Task Journal", taskSynBaseLog
									.getSubtaskmail_id().get(i),
									taskSynBaseLog.getSubtaskmail_id() + "任务成功");
						}
						updateTask.add(tt);
					}
				} else {
					if (taskSynBaseLog.getSubtaskID().size() == 1) {
						return false;
					}
				}
			}
		}
		// 更新子任务LOG
		if (null != updateTask && updateTask.size() > 0) {
			updateSubTaskLog(updateTask);
		}
		// 进行主任务的更新
		TaskLogTime tasktime = new TaskLogTime();
		String taskcode = "1";
		Long tasklogid = Long.valueOf(taskSynBaseLog.getTaskLogID());
		List<TaskLogSyn> task = (List<TaskLogSyn>) infaCommonService
				.findObjects("TaskSyn.selectTask", tasklogid);
		Timestamp endtime = null;
		for (int i = 0; i < task.size(); i++) {
			if (task.get(i).getSub_tasklogid().equals("0")) {
				if (null == task.get(i).getWf_endtime()) {
					logger.info("workflow没有执行结束");
					return false;
				}
				if (null == endtime
						|| endtime.getTime() < task.get(i).getWf_endtime()
								.getTime()) {
					endtime = task.get(i).getWf_endtime();
				}
			} else {
				if (null == task.get(i).getSub_end_time()) {
					logger.info("子任务没有执行结束");
					return false;
				}
				if (null == endtime
						|| endtime.getTime() < task.get(i).getSub_end_time()
								.getTime()) {
					endtime = task.get(i).getSub_end_time();
				}
			}

			if (null != task.get(i).getSub_code()
					&& !task.get(i).getSub_code().equals("1")) {
				taskcode = "3";
			}
			if (null != task.get(i).getWf_code()
					&& !task.get(i).getWf_code().equals("1")) {
				taskcode = "3";
			}
			if(null == endtime){
				endtime = task.get(i).getStart_time();
			}
		}
		tasktime.setRUN_STATUS_CODE(taskcode);
		tasktime.setEND_TIME(endtime);
		tasktime.setId(Long.valueOf(taskSynBaseLog.getTaskLogID()));
		// logger.info("ID               "
		// + Long.valueOf(taskSynBaseLog.getTaskLogID()));
		// logger.info("endtime               " + endtime);
		// logger.info("getSub_code               "
		// + tasktime.getRUN_STATUS_CODE());
		if (null != taskSynBaseLog.getTaskmail_id()
				&& !taskSynBaseLog.getTaskmail_id().equals("")) {
			String code = "成功";
			if (tasktime.getRUN_STATUS_CODE().equals("3")) {
				code = "失败";
			}
			mail(Mail, "Task Journal", taskSynBaseLog.getTaskmail_id(),
					taskSynBaseLog.getTaskID() + "任务执行" + code);
		}
		updateTaskLog(tasktime);
		return true;
	}

	/**
	 * 更新主任务task状态和结束时间，结束时间为task下workflow时间最迟的一条
	 * 
	 * @throws BusinessException
	 */
	private void updateTaskLog(TaskLogTime logTime) throws BusinessException {
		logger.info("更新主任务LOG");
		infaCommonService.modifyObject("TaskLog.updateTaskLog", logTime);
	}

	/**
	 * 更新主任务task状态和结束时间，结束时间为task下workflow时间最迟的一条
	 * 
	 * @throws BusinessException
	 */
	private void updateSubTaskLog(List<TaskLogTime> updateTask)
			throws BusinessException {
		logger.info("批量更新子任务LOG");
		infaCommonService.batchModifyObject("TaskLog.updateSubTaskLog",
				updateTask);
	}

	private void mail(MailConfig mail, String MailSubject, String To,
			String setMailBody) {
		if(mailerrer){
			logger.info("发送人邮件配置有误");
			return;
		}
		// 发邮件
		logger.info("收件人地址:  " + To);
		boolean b = false;
		try {
			String[] k = To.split("\\;");
			StringBuffer sb = new StringBuffer();
			if (k.length == 1) {
				if (!SendMailUtil.isEmail(k[0])) {
					logger.error("邮箱地址有错误： " + k[0]);
					return;
				}
				sb.append(k[0]);
			} else {
				for (int i = 0; i < k.length; i++) {
					if (!SendMailUtil.isEmail(k[i])) {
						logger.error("邮箱地址有错误： " + k[i]);
						continue;
					}
					if (i == k.length - 1) {
						sb.append(k[i]);
					} else {
						sb.append(k[i] + ",");
					}
				}
			}
			MailInfoBean mailInfo = new MailInfoBean();
			mailInfo.setMailSubject(MailSubject);
			mailInfo.setFrom(mail.getLOGICALNAME());
			mailInfo.setTo(sb.toString());// 用逗号隔开
			// mailInfo.setCopyto("chub@liandisys.com.cn");//用逗号隔开
			mailInfo.setMailBody(setMailBody);

			b = SendMailUtil.setOut(mail.getHOST(), mailInfo, mail.getUSERNAME(),
					mail.getPASSWORD());
			// b = SendMailUtil.setOut("mailhost", mailInfo,
			// mail.getLOGICALNAME(),
			// mail.getPASSWORD());

		} catch (MessagingException e) {
			// System.out.println(e.getMessage());
			logger.error("邮件发送失败：" + mail.getUSERNAME() + mail.getPASSWORD(), e);
			mailerrer = true;
		} finally {
			if (b) {
				logger.info("\n邮件发送成功!!!!!");
			} else {
				logger.info("邮件发送失败!!!!");
			}
		}
	}
}
