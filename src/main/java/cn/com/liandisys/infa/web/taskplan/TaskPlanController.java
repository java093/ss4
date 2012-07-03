package cn.com.liandisys.infa.web.taskplan;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.taskplan.TaskPlan;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.JasonUtil;

/**
 * 系统管理模块ACTION，
 * 
 * 真正任务的POST请求由Filter完成
 * 
 */
@Controller
@RequestMapping(value = "/tp")
public class TaskPlanController {

	private static Logger logger = LoggerFactory
			.getLogger(TaskPlanController.class);

	@Autowired
	private InfaCommonService infaCommonService;

//	@RequestMapping(value = "/index", method = RequestMethod.GET)
//	public String index(HttpServletRequest request, Model model)
//			throws BusinessException {
//		logger.info("验证成功，跳转到SYS页面！");
//		// 返回的视图名称,/WEB-INF/view/task/index.jsp
//		return "/sys/DataBaseList";
//	}

	/**
	 * 初始化一览数据
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public String list(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("TaskPlan: list！");
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		List<TaskPlan> list = infaCommonService.findObjects(
				"tp.findAllTaskPlan", null);
		
		logger.info(JasonUtil.JasonTOString(list));
		return JasonUtil.JasonTOString(list);
	}

	/**
	 * 根据id查找记录
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/findByid")
	@ResponseBody
	public String findbyid(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("DataBase: findbyid！");
		TaskPlan tp = new TaskPlan();
		String id = request.getParameter("id");
		logger.info("DataBase:" + id);
		tp.setId(Long.valueOf(id));
		TaskPlan rtp = (TaskPlan) infaCommonService.findObject("tp.findByid",
				tp);
		return JasonUtil.JasonTOString(rtp);
	}
}
