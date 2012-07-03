package cn.com.liandisys.infa.web.mng;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import cn.com.liandisys.idev.modules.security.service.SecurityService;
import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.mng.SysDictionary;
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
@RequestMapping(value = "/sysdict")
public class SysDictionaryController {

	private static Logger logger = LoggerFactory
			.getLogger(MailController.class);

	@Autowired
	private InfaCommonService infaCommonService;
	@Autowired
	private SecurityService securityService;
	/**
	 * 任务List
	 */
	private List<SysDictionary> rows = null;
	private long total;

	private int currentpage;
    private int limit;
    private Map<String, Object> pageCondition;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("验证成功，跳转到SysDictionary页面！");
		}
		// 返回的视图名称
		return "/mng/sysdictionary";
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
		List<SysDictionary> list = infaCommonService.findObjects(
				"sysdict.findAllSysDict", null);
		long total = infaCommonService
				.countObject("sysdict.sysdictCount", null);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		if (logger.isDebugEnabled()) {
			logger.debug("SysDictionary: list！");
			logger.debug(JasonUtil.JasonTOString(list));
		}
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
			logger.debug("SysDictionary: add！");
		}
		Timestamp time = new Timestamp(System.currentTimeMillis());
		SysDictionary sysdict = new SysDictionary();
		// sysdict.setBranch_no(request.getParameter("branch_no"));
		sysdict.setDict_entry(request.getParameter("dict_entry"));
		sysdict.setDict_type(request.getParameter("dict_type"));
		sysdict.setSubentry(request.getParameter("subentry"));
		sysdict.setAccess_level(request.getParameter("access_level"));
		sysdict.setDict_prompt(request.getParameter("dict_prompt"));
		sysdict.setDefunct_ind(request.getParameter("defunct_ind"));
		sysdict.setRemarks(request.getParameter("remarks"));
		sysdict.setCreated_by(securityService.getCurrentUser().getId());
		sysdict.setCreated_datetime(time);
		infaCommonService.addObject("sysdict.insertSysDict", sysdict);
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
			logger.debug("SysDictionary: edit！");
		}
		SysDictionary sysdict = new SysDictionary();
		// sysdict.setBranch_no(request.getParameter("branch_no"));
		sysdict.setOld_dict_entry(request.getParameter("old_dict_entry"));
		sysdict.setOld_subentry(request.getParameter("old_subentry"));
		sysdict.setDict_entry(request.getParameter("dict_entry"));
		sysdict.setDict_type(request.getParameter("dict_type"));
		sysdict.setSubentry(request.getParameter("subentry"));
		sysdict.setAccess_level(request.getParameter("access_level"));
		sysdict.setDict_prompt(request.getParameter("dict_prompt"));
		sysdict.setDefunct_ind(request.getParameter("defunct_ind"));
		sysdict.setRemarks(request.getParameter("remarks"));
		infaCommonService.modifyObject("sysdict.updateSysDict", sysdict);
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
			logger.debug("SysDictionary: delete！");
		}
		rows = new ArrayList<SysDictionary>();
		String dict_entrys = request.getParameter("dict_entrys");
		String subentrys = request.getParameter("subentrys");
		String[] selectedDict_entrys = dict_entrys.split(",");
		String[] selectedSubentrys = subentrys.split(",");
		for (int i = 0; i < selectedDict_entrys.length; i++) {
			SysDictionary sysdict = new SysDictionary();
			sysdict.setDict_entry(selectedDict_entrys[i]);
			sysdict.setSubentry(selectedSubentrys[i]);
			rows.add(sysdict);
		}
		infaCommonService.batchRemoveObject("sysdict.deleteSysDict", rows);
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
			logger.debug("SysDictionary: findbyid！");
		}
		SysDictionary sysdict = new SysDictionary();
		sysdict.setDict_entry(request.getParameter("dict_entry"));
		sysdict.setSubentry(request.getParameter("subentry"));
		SysDictionary rsysdict = (SysDictionary) infaCommonService.findObject(
				"sysdict.findByid", sysdict);
		return JasonUtil.JasonTOString(rsysdict);
	}
	
	/**
	 * 根据子项查找记录
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/findbysub" )
	@ResponseBody
	public String findbysub(HttpServletRequest request, Model model) 
			throws BusinessException {
		
		logger.debug("SysDictionary: findbysub！");
		SysDictionary sysdict = new SysDictionary();
		String dict_entry = request.getParameter("dict_entry");
		String subentry = request.getParameter("subentry");
		String old_dict_entry = request.getParameter("old_dict_entry");
		String old_subentry = request.getParameter("old_subentry");
		logger.debug("dict_entry===="+dict_entry);
		logger.debug("subentry===="+subentry);
		logger.debug("old_dict_entry===="+old_dict_entry);
		logger.debug("old_subentry===="+old_subentry);
		if(dict_entry.equals(old_dict_entry) && subentry.equals(old_subentry)){
			return "true";
		}
		sysdict.setDict_entry(dict_entry);
		sysdict.setSubentry(subentry);
		long  i = (Long)infaCommonService.findObject("sysdict.findBySub",sysdict);
	    if(i==0){
	    	return "true";
	    }else{
	    	return "false";
	    }
	}
	

	@RequestMapping(value = "/listByName")
	@ResponseBody
	public String listByName(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("WORKFLOW: listByName！");
		String alias = request.getParameter("name");
		if(alias!=null){
		if(alias.equals("")){
			alias = null;
		}
		}
		logger.debug("workflow参数：" + alias);
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.debug("行数："+request.getParameter("rows"));
		logger.debug("当前页数："+request.getParameter("page"));		
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		pageCondition.put("name", alias);
		total = infaCommonService.countObject("sysdict.sysdictsearchCount", pageCondition);	
		logger.debug("total:"+total);
		logger.debug("==="+pageCondition);
		List<SysDictionary> list = infaCommonService.findObjects("sysdict.findByName", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}
}
