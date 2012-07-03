package cn.com.liandisys.infa.web.mng;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

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
import cn.com.liandisys.infa.entity.mng.Dictentry;
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
@RequestMapping(value = "/dictentry")
public class DictentryController {

	private static Logger logger = LoggerFactory
			.getLogger(MailController.class);

	@Autowired
	private InfaCommonService infaCommonService;
	@Autowired
	private SecurityService securityService;
	/**
	 * 任务List
	 */
	private List<Dictentry> rows = null;
	private long total;

	private int currentpage;
    private int limit;
    private Map<String, Object> pageCondition;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("验证成功，跳转到dictentry页面！");
		}
		// 返回的视图名称
		return "/mng/dictentry";
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
		List<Dictentry> list = infaCommonService.findObjects(
				"dict.findAllDict", null);
		long total = infaCommonService.countObject("dict.dictCount", null);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		if (logger.isDebugEnabled()) {
			logger.debug("Dictentry: list！");
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
			logger.debug("Dictentry: add！");
		}
		Timestamp time = new Timestamp(System.currentTimeMillis()); 
		Dictentry dict = new Dictentry();

		dict.setDict_entry(request.getParameter("dict_entry"));
		dict.setAdd_enable(request.getParameter("add_enable"));
		dict.setEntry_name(request.getParameter("entry_name"));
		dict.setAccess_level(request.getParameter("access_level"));
		dict.setDict_type(request.getParameter("dict_type"));
		dict.setDefunct_ind(request.getParameter("defunct_ind"));
		dict.setRemarks(request.getParameter("remarks"));
		dict.setCreated_by(securityService.getCurrentUser().getId());
		dict.setCreated_datetime(time);
		infaCommonService.addObject("dict.insertDict", dict);
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
			logger.debug("Dictentry: edit！");
			logger.debug("old_dict_entry====="+request.getParameter("old_dict_entry"));
			logger.debug("dict_entry====="+request.getParameter("dict_entry"));
		}
		Dictentry dict = new Dictentry();
		dict.setOld_dict_entry(request.getParameter("old_dict_entry"));
		dict.setDict_entry(request.getParameter("dict_entry"));
		dict.setAdd_enable(request.getParameter("add_enable"));
		dict.setEntry_name(request.getParameter("entry_name"));
		dict.setAccess_level(request.getParameter("access_level"));
		dict.setDict_type(request.getParameter("dict_type"));
		dict.setDefunct_ind(request.getParameter("defunct_ind"));
		dict.setRemarks(request.getParameter("remarks"));
		infaCommonService.modifyObject("dict.updateDict", dict);
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
			logger.debug("Dictentry: delete！");
		}
		rows = new ArrayList<Dictentry>();
		String selectIdStr = request.getParameter("ids");
		String[] selectedIdArr = selectIdStr.split(",");
		for (int i = 0; i < selectedIdArr.length; i++) {
			Dictentry dict = new Dictentry();
			logger.info("mailid:" + selectedIdArr[i]);
			// dict.setId(Long.valueOf(selectedIdArr[i]));
			dict.setDict_entry(selectedIdArr[i]);
			rows.add(dict);
		}
		infaCommonService.batchRemoveObject("dict.deleteDict", rows);
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
			logger.debug("Dictentry: findbyid！");
		}
		Dictentry dict = new Dictentry();
		String dict_entry = request.getParameter("dict_entry");
		dict.setDict_entry(dict_entry);
		Dictentry rdict = (Dictentry) infaCommonService.findObject(
				"dict.findByid", dict);
		return JasonUtil.JasonTOString(rdict);
	}
	
	/**
	 * 根据entry查找记录
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/findbyentry" )
	@ResponseBody
	public String findbyentry(HttpServletRequest request, Model model) 
			throws BusinessException {
		
		logger.debug("Dictentry: findbyentry！");
		Dictentry dict = new Dictentry();
		String dict_entry = request.getParameter("dict_entry");
		String old_dict_entry = request.getParameter("old_dict_entry");
		logger.debug("dict_entry:"+dict_entry);	
		logger.debug("old_dict_entry:"+old_dict_entry);
		if(dict_entry.equals(old_dict_entry)){
			return "true";
		}
		dict.setDict_entry(dict_entry);
		long  i = (Long)infaCommonService.findObject("dict.findByEntry",dict);
	    if(i==0){
	    	return "true";
	    }else{
	    	return "false";
	    }
		
		
	}

	/**
	 * 获取dictentry下拉框数据
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/dictentrySelect")
	@ResponseBody
	public String workflowSelect(HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {

		List<Dictentry> datalist = infaCommonService.findObjects(
				"dict.findAllDict", null);
		int rows = datalist.size();
		logger.debug("dictentry  Select  查到" + rows + "记录。");
		String json = JSONArray.fromObject(datalist).toString();
		response.getWriter().write(json);
		return null;

	}

	/**
	 * 查询当前的Subentry
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 * @throws IOException
	 */
	@RequestMapping(value = "/findSubentry")
	@ResponseBody
	public String find_subentry(HttpServletRequest request, Model model,
			HttpServletResponse response) throws BusinessException, IOException {
		logger.debug("Dictentry: findSubentry");

		SysDictionary sysdict = new SysDictionary();
		String dict_entry = request.getParameter("dict_entry");
		logger.debug("findSubentry：dict_entry=" + dict_entry);
		if (dict_entry == null || dict_entry.equals("")) {
			return JasonUtil.JasonTOString(false);
		}
		sysdict.setDict_entry(request.getParameter("dict_entry"));
		List<SysDictionary> datalist = infaCommonService.findObjects(
				"sysdict.findById", sysdict);
		int rows = datalist.size();
		logger.debug("dictentry  Select  查到" + rows + "记录。");
		String json = JSONArray.fromObject(datalist).toString();
		logger.debug("json==" + json);
		response.getWriter().write(json);
		return null;
	}
	
	@RequestMapping(value = "/listByName")
	@ResponseBody
	public String listByName(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("Dictentry: listByName！");
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
		total = infaCommonService.countObject("dict.dictsearchCount", pageCondition);	
		logger.debug("total:"+total);
		logger.debug("==="+pageCondition);
		List<Dictentry> list = infaCommonService.findObjects("dict.findByName", pageCondition);
		logger.info(JasonUtil.JasonTOString(list));
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}
}
