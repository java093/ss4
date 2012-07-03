package cn.com.liandisys.infa.quartz;

import java.sql.SQLException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.service.sys.WorkFlowSynService;
import cn.com.liandisys.infa.util.SpringBeanUtil;

/**
 * WorkFlow及session运行状态同步用任务执行类
 * @author gaoyh
 *
 */
public class WorkFlowSynJob extends QuartzJobBean {
	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(WorkFlowSynJob.class);
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		if (logger.isInfoEnabled()) {
			logger.info("WorkFlowSynJob Start！");
		}
		// 获取任务用Service
		WorkFlowSynService workflowSynService = 
				(WorkFlowSynService)SpringBeanUtil.getBean("workflowSynService");
		if (null == workflowSynService) {
			logger.error("WorkFlowSynService can not be Initialize!");
			throw new JobExecutionException("WorkFlowSynService can not be Initialize!");
		}
		// 开始
		try {
			workflowSynService.excuteWFSyn();
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("WorkFlow及Session数据同步运行错误：" , e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("WorkFlow及Session数据同步数据库错误：", e);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("WorkFlowSynJob End！");
		}
	}

}
