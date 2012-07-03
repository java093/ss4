package cn.com.liandisys.infa.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import cn.com.liandisys.infa.service.task.TaskService;

public class getTaskSecvice {
	@Autowired
	private TaskService taskService;
	
	private static Logger logger = LoggerFactory.getLogger(TaskExcuteJob.class);
	getTaskSecvice(String taskid){
		// 任务ID为空时，返回
		if (!StringUtils.hasLength(StringUtils.trimAllWhitespace(taskid))) {
			if (logger.isWarnEnabled()) {
				logger.warn("任务调度执行无法触发，taskid： " + taskid);
			}
			return;
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("任务调度执行开始，taskid： " + taskid);
		}
		TaskService taskService = new TaskService();
		logger.info("taskService： " + (null == taskService));
		// 开始执行
		taskService.startTask(Integer.valueOf(taskid).longValue(), 1, 0);
		if (logger.isInfoEnabled()) {
			logger.debug("任务调度执行开始，taskid： " + taskid);
		}
	}
}
