package cn.com.liandisys.infa.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.com.liandisys.infa.service.task.TaskService;
import cn.com.liandisys.infa.util.QuartzTriggersUtil;
import cn.com.liandisys.infa.util.SpringBeanUtil;

/**
 * 任务调度执行类，触发任务
 * @author gaoyh
 *
 */
@Component
public class TaskExcuteJob extends QuartzJobBean {
	private static Logger logger = LoggerFactory.getLogger(TaskExcuteJob.class);
	
//	@Autowired
//	private TaskService taskService2;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("定时任务开始！！！！！！！！！！！！！！！！！！！！");
		String taskid = (String)context.getJobDetail().getJobDataMap()
				.get(QuartzTriggersUtil.INFA_TASKID);
		//new getTaskSecvice(taskid);
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
//		TaskService taskService = new TaskService();
		TaskService taskService = (TaskService)SpringBeanUtil.getBean("taskService");
		logger.info("taskService： " + (null == taskService));
		// 开始执行
		taskService.startTask(Integer.valueOf(taskid).longValue(), 1, 0);
		if (logger.isInfoEnabled()) {
			logger.info("任务调度执行开始，taskid： " + taskid);
		}
	}
	


}
