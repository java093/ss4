package cn.com.liandisys.infa.web.mng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
import cn.com.liandisys.infa.entity.mng.Session;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.PagingUtil;
import cn.com.liandisys.infa.web.sys.MailController;

/**
 * 任务管理模块ACTION，
 * 
 * 真正任务的POST请求由Filter完成
 * 
 */
@Controller
@RequestMapping(value = "/session")
public class SessionController {

	private static Logger logger = LoggerFactory
			.getLogger(MailController.class);

	@Autowired
	private InfaCommonService infaCommonService;
	/**
	 * 任务List
	 */
	private List<Session> rows = null;
	private long total;
	private int currentpage;
    private int limit;
    private Map<String, Object> pageCondition;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("验证成功，跳转到Session页面！");
		}
		// 返回的视图名称
		return "/mng/session";
	}

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
		List<Session> list = infaCommonService.findObjects(
				"session.findAllSession", null);
		long total = infaCommonService
				.countObject("session.sessionCount", null);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		if (logger.isDebugEnabled()) {
			logger.debug("Session: list！");
			logger.debug(JasonUtil.JasonTOString(list));
		}
		return JasonUtil.JasonTOString(pu.getList());
	}

	/**
	 * 根据check_level查找记录
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/findbylevel")
	@ResponseBody
	public String findbylevel(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("Session: findbylevel！");
		}
		Session session = new Session();
		session.setCheck_level(request.getParameter("check_level"));
		List<Session> list = infaCommonService.findObjects(
				"session.findBylevel", session);
		logger.debug(JasonUtil.JasonTOString(list));
		long total = infaCommonService.countObject(
				"session.sessionCountByLevel", session);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		return JasonUtil.JasonTOString(pu.getList());
	}

	/**
	 * 增加
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public String add(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("Session: add！");
		}
		String datetime = new SimpleDateFormat("yyyy/MM/dd HH:MM:ss")
				.format(Calendar.getInstance().getTime()); // 获取系统时间
		Session session = new Session();
		session.setSubj_name(request.getParameter("subj_name"));
		session.setSubj_id(request.getParameter("subj_id"));
		session.setSession_name(request.getParameter("session_name"));
		session.setSession_id(request.getParameter("session_id"));
		session.setWorkflow_name(request.getParameter("workflow_name"));
		session.setWorkflow_id(request.getParameter("workflow_id"));
		session.setMapping_name(request.getParameter("mapping_name"));
		session.setMapping_id(request.getParameter("mapping_id"));
		session.setCheck_level(request.getParameter("check_level"));
		session.setCheck_level_desc(request.getParameter("check_level_desc"));
		session.setCheck_type(request.getParameter("check_type"));
		session.setCheck_type_desc(request.getParameter("check_type_desc"));
		session.setDefault_ind(request.getParameter("default_ind"));
		// con.setCreated_datetime(datetime);
		infaCommonService.addObject("session.insertSession", session);
		return JasonUtil.getJsonResult(true);
	}

	/**
	 * 编辑
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public String update(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("Session: edit！");
		}
		Session session = new Session();
		session.setSubj_name(request.getParameter("subj_name"));
		session.setSubj_id(request.getParameter("subj_id"));
		session.setSession_name(request.getParameter("session_name"));
		session.setSession_id(request.getParameter("session_id"));
		session.setWorkflow_name(request.getParameter("workflow_name"));
		session.setWorkflow_id(request.getParameter("workflow_id"));
		session.setMapping_name(request.getParameter("mapping_name"));
		session.setMapping_id(request.getParameter("mapping_id"));
		session.setCheck_level(request.getParameter("check_level"));
		session.setCheck_level_desc(request.getParameter("check_level_desc"));
		session.setCheck_type(request.getParameter("check_type"));
		session.setCheck_type_desc(request.getParameter("check_type_desc"));
		session.setDefault_ind(request.getParameter("default_ind"));

		infaCommonService.modifyObject("session.updateSession", session);
		return JasonUtil.getJsonResult(true);
	}

	/**
	 * 删除（多行）
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String delete(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("Session: delete！");
		}
		rows = new ArrayList<Session>();
		String selectIdStr = request.getParameter("ids");
		String[] selectedIdArr = selectIdStr.split(",");
		for (int i = 0; i < selectedIdArr.length; i++) {
			Session session = new Session();
			logger.debug("sessionid:" + selectedIdArr[i]);
			session.setSession_id(selectedIdArr[i]);
			rows.add(session);
		}
		infaCommonService.batchRemoveObject("session.deleteSession", rows);
		return JasonUtil.getJsonResult(true);
	}

	/**
	 * 根据id查找记录
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/findbyid")
	@ResponseBody
	public String findbyid(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("Session: findbyid！");
		}
		Session session = new Session();
		session.setSession_id(request.getParameter("session_id"));
		Session rsession = (Session) infaCommonService.findObject(
				"session.findByid", session);
		return JasonUtil.JasonTOString(rsession);
	}
	
	@RequestMapping(value = "/listByName")
	@ResponseBody
	public String listByName(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("SESSION: listByName！");
		String check_level = request.getParameter("check_level");
		if(check_level!=null){
		if(check_level.equals("")){
			check_level = null;
		}
		}
		logger.debug("SESSION参数：" + check_level);
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.debug("行数："+request.getParameter("rows"));
		logger.debug("当前页数："+request.getParameter("page"));		
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		pageCondition.put("name", check_level);
		total = infaCommonService.countObject("session.sessionsearchCount", pageCondition);	
		logger.debug("total:"+total);
		logger.debug("==="+pageCondition);
		List<Session> list = infaCommonService.findObjects("session.findByName", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}
}
