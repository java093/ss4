package cn.com.liandisys.infa.servlet;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang.time.DateUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.liandisys.infa.quartz.TaskLogAndMailSynJob;
import cn.com.liandisys.infa.quartz.WorkFlowLogReadJob;
import cn.com.liandisys.infa.quartz.WorkFlowSynJob;
import cn.com.liandisys.infa.util.QuartzTriggersUtil;
import cn.com.liandisys.infa.util.SpringBeanUtil;

/**
 * 系统初始化 Servlet，在Spring MVC 初始化后执行
 * 
 * 1。载入系统参数
 * 2。开始自动任务
 * @author gaoyh
 *
 */
public class InfaInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	public static final String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";

	@Override
    public void init(ServletConfig arg0) throws ServletException
    {
        super.init(arg0);
        try {
            Properties prop = new Properties();
            ClassLoader loader = InfaInitServlet.class.getClassLoader();
            prop.load(loader.getResourceAsStream("application.properties"));
            Enumeration keys = prop.keys();
            while(keys.hasMoreElements()){
            	String key = (String)keys.nextElement();
            	System.setProperty( key , prop.getProperty(key) );
            }
            log.info("app.filepath="+System.getProperty("app.filepath"));
            SpringBeanUtil.init(arg0.getServletContext());
            //开始任务
            startSchedule(arg0);
            
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
        log.info("InitServlet successfully initialized!");
    }
	

	/**
	 * 启动预定义任务
	 * @param cfg
	 * @throws Exception
	 */
    public void startSchedule(ServletConfig cfg) throws Exception{
		// 通过SchedulerFactory获取一个调度器实例  
        SchedulerFactory sf = new StdSchedulerFactory("application.properties");
        Scheduler sched = sf.getScheduler();
        sched.start();
        cfg.getServletContext().setAttribute(QUARTZ_FACTORY_KEY, sf);
        
        
        // 系统固化两个任务
        // 任务1：workflow及session日志状态同步任务
        Calendar statTime = Calendar.getInstance();
        if (sched.checkExists(new JobKey("WorkFlowSynJob", QuartzTriggersUtil.INFA_GROUP))) {
        	log.info("删除任务1：workflow及session日志状态同步任务");
        	sched.deleteJob(new JobKey("WorkFlowSynJob", QuartzTriggersUtil.INFA_GROUP));
        }
        if (!sched.checkExists(new JobKey("WorkFlowSynJob", QuartzTriggersUtil.INFA_GROUP))) {
        	log.info("加载任务1：workflow及session日志状态同步任务");
	        JobDetail job = JobBuilder.newJob(WorkFlowSynJob.class)
	        		.withIdentity("WorkFlowSynJob", QuartzTriggersUtil.INFA_GROUP)
	        		.build();
	        Trigger trigger = TriggerBuilder.newTrigger()
		            .withIdentity("WorkFlowSynJobTrigger", QuartzTriggersUtil.INFA_GROUP)
		            .startAt(statTime.getTime())
		            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
		                    .withIntervalInSeconds(new Integer(
		                    		System.getProperty("wfStatusSynIntervalSecond")))
		                    		.repeatForever())
		            .build();
	        sched.scheduleJob(job, trigger);
        }

//        // 任务2：workflow及session日志文件内容获取任务
        if (sched.checkExists(new JobKey("WorkFlowLogReadJob", QuartzTriggersUtil.INFA_GROUP))) {
        	log.info("删除任务2：workflow及session日志文件内容获取任务");
        	sched.deleteJob(new JobKey("WorkFlowLogReadJob", QuartzTriggersUtil.INFA_GROUP));
        }
        if (!sched.checkExists(new JobKey("WorkFlowLogReadJob", QuartzTriggersUtil.INFA_GROUP))) {
        	log.info("加载任务2：workflow及session日志文件内容获取任务");
	        JobDetail job = JobBuilder.newJob(WorkFlowLogReadJob.class)
	        		.withIdentity("WorkFlowLogReadJob", QuartzTriggersUtil.INFA_GROUP)
	        		.build();
	        Trigger trigger = TriggerBuilder.newTrigger()
		            .withIdentity("WorkFlowLogReadJobTrigger", QuartzTriggersUtil.INFA_GROUP)
		            .startAt(DateUtils.addSeconds(statTime.getTime(), 5))
		            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
		                    .withIntervalInSeconds(new Integer(
		                    		System.getProperty("wfLogSynIntervalSecond")))
		                    		.repeatForever())
		            .build();
	        sched.scheduleJob(job, trigger);
        }
        
     // 任务3：TaskLog更新及Mail发送
        if (sched.checkExists(new JobKey("TaskLogAndMailSynJob", QuartzTriggersUtil.INFA_GROUP))) {
        	log.info("删除任务3：TaskLog更新及Mail发送");
        	sched.deleteJob(new JobKey("TaskLogAndMailSynJob", QuartzTriggersUtil.INFA_GROUP));
        }
        if (!sched.checkExists(new JobKey("TaskLogAndMailSynJob", QuartzTriggersUtil.INFA_GROUP))) {
        	log.info("加载任务3：TaskLog更新及Mail发送");
	        JobDetail job = JobBuilder.newJob(TaskLogAndMailSynJob.class)
	        		.withIdentity("TaskLogAndMailSynJob", QuartzTriggersUtil.INFA_GROUP)
	        		.build();
	        Trigger trigger = TriggerBuilder.newTrigger()
		            .withIdentity("TaskLogAndMailSynJob", QuartzTriggersUtil.INFA_GROUP)
		            .startAt(DateUtils.addSeconds(statTime.getTime(), 5))
		            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
		                    .withIntervalInSeconds(new Integer(
		                    		System.getProperty("wfStatusSynIntervalSecond")))
		                    		.repeatForever())
		            .build();
	        sched.scheduleJob(job, trigger);
        }
	}
}
