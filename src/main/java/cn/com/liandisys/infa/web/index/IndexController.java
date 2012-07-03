package cn.com.liandisys.infa.web.index;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.index.MonthlyRunTime;
import cn.com.liandisys.infa.entity.index.TaskLogTime;
import cn.com.liandisys.infa.entity.mng.LogList;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.PagingUtil;
import cn.com.liandisys.infa.util.workflow.WorkFlowException;

/**
 * 任务管理模块ACTION，
 * 
 * 真正任务的POST请求由Filter完成
 * 
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController {

	private static Logger logger = LoggerFactory
			.getLogger(IndexController.class);

	@Autowired
	private InfaCommonService infaCommonService;

	/**
	 * 总件数
	 */
	private long total = 0;

	@RequestMapping(value = "/taskloglist", method = RequestMethod.GET)
	public String taskloglist(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("验证成功，跳转到mng页面-日志管理！");
		// 返回的视图名称,/WEB-INF/view/mng/loglist.jsp
		return "/mng/taskloglist";
	}

	@RequestMapping(value = "/taskloglisttotal")
	@ResponseBody
	public String taskloglisttotal(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, ParseException {

		logger.info("index: list！");
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		List<LogList> list = infaCommonService.findObjects(
				"log.findtaskloglisttotal", null);

		total = list.size();
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		logger.debug(JasonUtil.JasonTOString(pu.getList()));
		return JasonUtil.JasonTOString(pu.getList());

	}

	@RequestMapping(value = "/monthlyrun")
	@ResponseBody
	public String monthlyrun(HttpServletRequest request, Model model)
			throws BusinessException {
		String time = new SimpleDateFormat("yyyy/MM/dd").format(Calendar
				.getInstance().getTime()); // 获取系统时间
		int end = Integer.parseInt(time.substring(8));
		List<MonthlyRunTime> list = new ArrayList<MonthlyRunTime>();
		String date = new SimpleDateFormat("yyyy/MM").format(Calendar
				.getInstance().getTime()); // 获取系统时间
		for (int i = 1; i <= end; i++) {

			// StringBuffer str = new StringBuffer();
			// str.append(datetime.substring(0, 8));
			// str.append(AutoCompletion(i));
			// List<TaskLogTime> listfirst = infaCommonService.findObjects(
			// "index.findchartsdatafirst", datetime);
			// long x = listfirst.get(0).getSTART_TIME().getTime();
			String datetime = "";
			if (i < 10) {
				datetime = date + "/0" + i;
			} else {
				datetime = date + "/" + i;
			}
			logger.debug("查询的日期：：" + datetime);
			List<TaskLogTime> listend = infaCommonService.findObjects(
					"index.findalltime", datetime);
			logger.info("workflow的个数：" + listend.size());
			Long alltime = (long) 0;
			if (listend.size() == 0) {
				MonthlyRunTime m = new MonthlyRunTime();
				m.setDay(i);
				m.setTime(0);
				list.add(m);
			} else {
				for (int x = 0; x < listend.size(); x++) {
					logger.debug("workflow的结束时间：" + listend.get(x).getEND_TIME()
							+ "    开始时间："
							+ listend.get(x).getSTART_TIME());
					if (null != listend.get(x).getEND_TIME()) {
						Long tmpLong = (listend.get(x).getEND_TIME().getTime() - listend
								.get(x).getSTART_TIME().getTime()) / 1000;
						logger.debug("workflow运行时间小时：" + tmpLong);
						alltime += tmpLong;
					}
				}
				logger.debug("workflow当前总运行小时：" + (double) alltime / 360.0);
				MonthlyRunTime m = new MonthlyRunTime();
				m.setDay(i);
				BigDecimal bd = new BigDecimal((double) alltime / 360.0);
				bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
				m.setTime(bd.doubleValue());
				list.add(m);
			}
		}
		logger.info("total:" + JasonUtil.JasonTOString(list));
		return JasonUtil.JasonTOString(list);
	}

	// 补齐
	private static String AutoCompletion(int date) {
		String str = String.valueOf(date);
		if (date < 10) {
			str = "0" + String.valueOf(date);
		}
		return str;
	}

	@RequestMapping(value = "/findtaskstate")
	@ResponseBody
	public String findTaskState(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, Exception {
		String datetime = new SimpleDateFormat("yyyy/MM/dd").format(Calendar
				.getInstance().getTime()); // 获取系统时间
		logger.info("index: findtaskstate！==" + datetime);
		//ps:更改sql语句
//		Long tasklog_workflow = (Long) infaCommonService.findObject("index.findAllTaskLog_wflow", datetime);
//		Long subtasklog_workflow = (Long) infaCommonService.findObject("index.findAllSbuTaskLog_wflow", datetime);
//		Long all = tasklog_workflow + subtasklog_workflow;
//		Long running = (Long) infaCommonService.findObject("index.findwflowRuning", datetime);
//		Long success = (Long) infaCommonService.findObject("index.findwflowSucceeded", datetime);
//		Long fail = (Long) infaCommonService.findObject("index.findwflowFailed", datetime);
		Long tasklog_workflow = (Long) infaCommonService.findObject("index.findAllTaskLog_wflow2", datetime);
//		Long subtasklog_workflow = (Long) infaCommonService.findObject("index.findAllSbuTaskLog_wflow", datetime);
//		Long all = tasklog_workflow + subtasklog_workflow;
		Long running = (Long) infaCommonService.findObject("index.findwflowRuning2", datetime);
		Long success = (Long) infaCommonService.findObject("index.findwflowSucceeded2", datetime);
		Long fail = (Long) infaCommonService.findObject("index.findwflowFailed2", datetime);
		Long other = tasklog_workflow - running - success - fail;

		logger.info("总记录条数为==" + tasklog_workflow);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("total", "" + tasklog_workflow);
//		map.put("total", "" + all);
		map.put("running", "" + running);
		map.put("success", "" + success);
		map.put("fail", "" + fail);
		map.put("other", "" + other);
		logger.debug("index:map==" + JasonUtil.JasonTOString(map));
		return JasonUtil.JasonTOString(map);

	}

}
