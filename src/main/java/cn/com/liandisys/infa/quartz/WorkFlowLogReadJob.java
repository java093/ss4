package cn.com.liandisys.infa.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.com.liandisys.infa.service.log.LogService;
import cn.com.liandisys.infa.util.SpringBeanUtil;

/**
 * WorkFlow及session Log获取用任务执行类
 * 
 * @author
 * 
 */

public class WorkFlowLogReadJob extends QuartzJobBean {
	
	private static final Logger logger = LoggerFactory
			.getLogger(WorkFlowLogReadJob.class);

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		if (logger.isInfoEnabled()) {
			logger.info("logServiceJob Start！!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		// 获取任务用Service
		LogService logService = 
				(LogService)SpringBeanUtil.getBean("logService");
		if (null == logService) {
			logger.error("WorkFlowSynService can not be Initialize!");
			throw new JobExecutionException("WorkFlowSynService can not be Initialize!");
		}
		// 开始
		logService.excuteLogRead();
		
		if (logger.isInfoEnabled()) {
			logger.info("logServiceJob End！!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
			
	}
}
