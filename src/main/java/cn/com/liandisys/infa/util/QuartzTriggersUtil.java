package cn.com.liandisys.infa.util;

import static org.quartz.CronScheduleBuilder.cronSchedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.liandisys.infa.entity.job.QuartzPlan;
import cn.com.liandisys.infa.quartz.TaskExcuteJob;
import cn.com.liandisys.infa.servlet.InfaInitServlet;
import cn.com.liandisys.infa.web.job.WorkFlowController;

/**
 * 根据planID，tastID 创建触发器
 */
public class QuartzTriggersUtil {
	private Logger logger = LoggerFactory.getLogger(WorkFlowController.class);
	public static final String INFA_GROUP = "INFA";
	public static final String INFA_TRIGGER = "Trigger_";// trigger名称
	public static final String JOB_NAME = "TestJob_";// trigger名称
	public static final String PLAN_TYPE = "0";// 执行次数
	public static final String INFA_TASKID = "INFA_TASKID"; // JobDetail参数用TASKID
	public static final String OPERATETYPE_EDIT = "0";//编辑操作类型
	public static final String OPERATETYPE_DEL = "1";//删除操作类型
	public static final String OPERATETYPE_IN = "2";//新增操作类型

	// DBConnectBean bean = new DBConnectBean("oracle.jdbc.driver.OracleDriver",
	// "jdbc:oracle:thin:@172.16.52.129:1521:orcl", "inf04", "inf04");
	// private QuartzPlan qp = null;
	private String type = null; // 计划类型
	private String time; // 执行时间
	private Date startedtime = null;// 开始时间
	private Date enddate = null;// 结束时间
	private String cron_expression = null;// CRON表达式
	private String plan_id = null;// CRON表达式
	private JobDetail jobDetail;
	private List<Trigger> triggerlist = new ArrayList<Trigger>();


	/**
	 * 无参数构造器
	 */
	public QuartzTriggersUtil() {
	};

	/**
	 * 创建有参构造器，调用触发器创建方法
	 * 
	 * @param HttpServletRequest
	 *            tastID，planID，List<QuartzPlan> list,String type
	 */
	public QuartzTriggersUtil(HttpServletRequest request, String tastID,
			String planID, List<QuartzPlan> list, String operateType) {
		this.getScheduler(request, tastID, planID, list ,operateType);
	}

	/**
	 * 根据tastID、planID 创建 触发器
	 * 
	 * @param HttpServletRequest
	 *            tastID，planID，List<QuartzPlan> list,String type
	 */
	public void getScheduler(HttpServletRequest request, String tastID,
			String planID, List<QuartzPlan> list ,String operateType) {

		for (int i = 0; i < list.size(); i++) {
			QuartzPlan quartzPlan = list.get(i);
			this.type = quartzPlan.getType();
			this.time = quartzPlan.getTime();
			this.cron_expression = quartzPlan.getCron_expression();
			this.plan_id = quartzPlan.getPlanid();
			
			// 添加trigger
			Trigger trigger = null;
			
			//仅执行一次
			if (type.equals(PLAN_TYPE)) {
				this.startedtime = strTOdate(quartzPlan.getStartedtime() + " "+ time);
				trigger = TriggerBuilder.newTrigger()
						.withIdentity(INFA_TRIGGER +tastID +"_"+plan_id , INFA_GROUP).startAt(this.startedtime).build();
			} else {
				this.startedtime = strTOdate(quartzPlan.getStartedtime() + " 00:00:00");
				if (null != enddate) {
					this.enddate = strTOdate(quartzPlan.getEnddate() + " 00:00:00");
				}
				
				// 通过cron表达式判断触发时机
				trigger = TriggerBuilder.newTrigger()
						.withIdentity(INFA_TRIGGER +tastID +"_"+plan_id , INFA_GROUP)
						//.startAt(this.startedtime)// 启动时间
						//ps:启动时间改为当前时间，以防止擦枪扫描
						.startAt(new Date())// 启动时间
						.endAt(this.enddate)// 结束时间
						.withSchedule(cronSchedule(this.cron_expression))// cron表达式
						// cron表达式
						.build();
			}

			JobDataMap map = new JobDataMap();
			map.put(INFA_TASKID, tastID);
			jobDetail = JobBuilder.newJob(TaskExcuteJob.class)
					.withIdentity(JOB_NAME + tastID, INFA_GROUP)
					.usingJobData(map).build();

			// 通过过JobDetail封装MyQuartzJobBean，同时指定Job在Scheduler中所属组及名称，组名为INFA，而名称为TestJob。
			logger.debug("JobDetail创建成功 : " + jobDetail);

			triggerlist.add(trigger);
			logger.debug("Trigger创建成功 : " + trigger.toString() + "触发时间"
					+ startedtime + "cron表达式 " + cron_expression);
		}
		Map<JobDetail, List<Trigger>> map = new HashMap<JobDetail, List<Trigger>>();
		map.put(jobDetail, triggerlist);
		//创建调度器，完成任务调度
		
		//新增
		if(operateType.equals(OPERATETYPE_IN)){
			logger.debug("---新增任务ing...");
			createScheduler(request, map, false);
		}else if(operateType.equals(OPERATETYPE_EDIT)){
			//编辑
			logger.debug("---移除任务ing...");
			removeJob(request, JOB_NAME + tastID);
			logger.debug("---新增任务ing...");
			createScheduler(request, map, false);
		}else if(operateType.equals(OPERATETYPE_DEL)){
			logger.debug("---移除任务ing...");
			removeJob(request, JOB_NAME + tastID);
		}	
	}

	/**
	 * 创建调度器实例
	 * 
	 * @param HttpServletRequest
	 *            request Map<JobDetail, List<Trigger>> map boolean replace
	 * **/
	public void createScheduler(HttpServletRequest request,
		Map<JobDetail, List<Trigger>> map, boolean replace) {
		// 通过SchedulerFactory获取一个调度器实例
		ServletContext ctx = request.getSession().getServletContext();
		StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(InfaInitServlet.QUARTZ_FACTORY_KEY);
		Scheduler scheduler;
		try {
			scheduler = factory.getScheduler();	
			//scheduler.start();
			logger.debug("Scheduler : " + (null == scheduler) + scheduler.isStarted());
			scheduler.scheduleJobs(map, replace);
			
			logger.debug("scheduler添加成功 : =========="+ scheduler.checkExists(JobKey.jobKey("TestJob", INFA_GROUP)));
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error("--部署出错,或为创建子任务--" + e.toString());
		} finally {
			logger.debug("========== : 部署结束 : ==========");
		}
	}
	
	
	
	/**
	 * 移除调度器实例
	 * 
	 * @param HttpServletRequest
	 *            request 
	 *        String 
	 *        	  jobName
	 * **/
	   public void removeJob(HttpServletRequest request,String jobName) {
		   ServletContext ctx = request.getSession().getServletContext();
		   StdSchedulerFactory sf = (StdSchedulerFactory) ctx.getAttribute(InfaInitServlet.QUARTZ_FACTORY_KEY);
		   Scheduler sched = null;
			try {
				sched = sf.getScheduler();
				logger.debug("----------JobKey--" +JobKey.jobKey(jobName, INFA_GROUP));
				logger.debug("----------JobKey是否存在--" + sched.checkExists(JobKey.jobKey(jobName, INFA_GROUP)));
				if(sched.checkExists(JobKey.jobKey(jobName, INFA_GROUP))){
//				sched.pauseJob(JobKey.jobKey(jobName, INFA_GROUP));//停止job
//				sched.unscheduleJob(tk);//移除
				sched.deleteJob(JobKey.jobKey(jobName, INFA_GROUP));//删除ob
//				sched.clear();//移除全部
					logger.debug("------------------移除成功----------------");
				}else{					
					logger.debug("---------------JobKey不存在----------------");
				}
			} catch (SchedulerException e) {
				e.printStackTrace();
				logger.error("------------------移除出错---------------- " + e);
			}
	   }

	/**
	 * 日期转换
	 * 
	 * @param String
	 *            date
	 * @return Date date
	 * **/
	public Date strTOdate(String date) {
		if (null != date) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date da = null;
			try {
				da = format.parse(date);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
				logger.debug("日期转换失败");
			}
			logger.debug("date is : " + da);
			return da;
		} else {
			return null;
		}
	}

}