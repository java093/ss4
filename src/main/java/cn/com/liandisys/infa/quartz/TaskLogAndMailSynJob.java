package cn.com.liandisys.infa.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.service.sys.TaskLogAndMailSynService;
import cn.com.liandisys.infa.util.SpringBeanUtil;

/**
 * Log状态设置及提醒邮件发送用任务执行类
 * @author limp
 *
 */
public class TaskLogAndMailSynJob extends QuartzJobBean {
	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(TaskLogAndMailSynJob.class);
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		if (logger.isInfoEnabled()) {
			logger.info("TaskLogAndMailSynJob Start！");
		}
		// 获取任务用Service
		TaskLogAndMailSynService tasklogSynService = 
				(TaskLogAndMailSynService)SpringBeanUtil.getBean("tasklogSynService");
		if (null == tasklogSynService) {
			logger.error("tasklogSynService can not be Initialize!");
			throw new JobExecutionException("TaskLogAndMailSynService can not be Initialize!");
		}
		// 开始
		try {
			tasklogSynService.excuteWFSyn();
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("Log状态设置及提醒邮件发送运行错误： ", e);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("TaskLogAndMailSynJob End！");
		}
	}

}
