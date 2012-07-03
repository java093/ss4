package cn.com.liandisys.infa.web.mng;

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

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.mng.CodeMaster;
import cn.com.liandisys.infa.entity.mng.CodeMasterDetail;
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
@RequestMapping(value = "/detail")
public class CodeMasterDetailController {

	private static Logger logger = LoggerFactory
			.getLogger(MailController.class);

	@Autowired
	private InfaCommonService infaCommonService;
	/**
	 * 任务List
	 */
	private List<CodeMasterDetail> rows = null;
	
	private long total;

	private int currentpage;
    private int limit;
    private Map<String, Object> pageCondition;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("验证成功，跳转到CODEMASTERDETAIL页面！");
		}
		// 返回的视图名称
		return "/mng/codemaster_detail";
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
		List<CodeMasterDetail> list = infaCommonService.findObjects(
				"detail.findAllDetail", null);
		long total = infaCommonService.countObject("detail.detailCount", null);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		if (logger.isDebugEnabled()) {
			logger.debug("CODEMASTERDETAIL: list！");
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
			logger.debug("CODEMASTERDETAIL: add！");
		}
		CodeMasterDetail detail = new CodeMasterDetail();
		detail.setCodetype(request.getParameter("codetype"));
		detail.setCode(request.getParameter("code"));
		detail.setCodevalue(request.getParameter("codevalue"));
		detail.setDescription(request.getParameter("description"));
		detail.setDelflag(request.getParameter("delflag"));

		infaCommonService.addObject("detail.insertDetail", detail);
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
			logger.debug("CODEMASTERDETAIL: edit！");
		}
		CodeMasterDetail detail = new CodeMasterDetail();
		detail.setId(Long.valueOf(request.getParameter("id")));
		detail.setCodetype(request.getParameter("codetype"));
		detail.setCode(request.getParameter("code"));
		detail.setCodevalue(request.getParameter("codevalue"));
		detail.setDescription(request.getParameter("description"));
		detail.setDelflag(request.getParameter("delflag"));
		infaCommonService.modifyObject("detail.updateDetail", detail);
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
			logger.debug("CODEMASTERDETAIL: delete！");
		}
		rows = new ArrayList<CodeMasterDetail>();
		String selectIdStr = request.getParameter("ids");
		String[] selectedIdArr = selectIdStr.split(",");
		for (int i = 0; i < selectedIdArr.length; i++) {
			CodeMasterDetail detail = new CodeMasterDetail();
			logger.debug("CODEMASTERid:" + selectedIdArr[i]);
			detail.setId(Long.valueOf(selectedIdArr[i]));
			rows.add(detail);
		}
		infaCommonService.batchRemoveObject("detail.deleteDetail", rows);
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
			logger.debug("CODEMASTERDETAIL: findbyid！");
		}
		CodeMasterDetail detail = new CodeMasterDetail();
		detail.setId(Long.valueOf(request.getParameter("id")));
		CodeMasterDetail rdetail = (CodeMasterDetail) infaCommonService
				.findObject("detail.findByid", detail);
		return JasonUtil.JasonTOString(rdetail);
	}

	/**
	 * 获取CodeMasterDetail下拉框数据
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/findtype")
	@ResponseBody
	public String findtype(HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {

		List<CodeMaster> datalist = infaCommonService.findObjects(
				"code.findAllCode", null);
		int rows = datalist.size();
		logger.debug("dictentry  Select  查到" + rows + "记录。");
		String json = JSONArray.fromObject(datalist).toString();
		logger.debug("json=" + json);
		response.getWriter().write(json);
		return null;
	}
	
	/**
	 * 根据code查找记录
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/findbycode" )
	@ResponseBody
	public String findbycode(HttpServletRequest request, Model model) 
			throws BusinessException {
		
		logger.debug("CON: findbycode！");
		CodeMasterDetail detail = new CodeMasterDetail();
		String codetype = request.getParameter("codetype");
		String code = request.getParameter("code");
		String id = request.getParameter("id");
		detail.setCodetype(codetype);
		detail.setCode(code);
		logger.debug("id:"+id);	
		if(!id.equals("0")){
			detail.setId(Long.valueOf(id));
			CodeMasterDetail rdetail = (CodeMasterDetail)infaCommonService.findObject("detail.findByid",detail);
			if(rdetail.getCodetype().equals(codetype) && rdetail.getCode().equals(code)){
				return "true";
			}
		}
		long  i = (Long)infaCommonService.findObject("detail.findByCode",detail);
	    if(i==0){
	    	return "true";
	    }else{
	    	return "false";
	    }
	}
	

	/**
	 * 获取DictType下拉框数据
	 * 
	 * @param request
	 * @param model
	 * @param response
	 */
	@RequestMapping(value = "/findbycodetype")
	@ResponseBody
	public String findbycodetype(HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		CodeMasterDetail detail = new CodeMasterDetail();
		detail.setCodetype(request.getParameter("codetype"));
		logger.debug("codetype===" + request.getParameter("codetype"));
		List<CodeMasterDetail> datalist = infaCommonService.findObjects(
				"detail.findByCodeType", detail);
		int rows = datalist.size();
		logger.debug("dictentry  Select  查到" + rows + "记录。");
		String json = JSONArray.fromObject(datalist).toString();
		logger.debug("json=" + json);
		response.getWriter().write(json);
		return null;
	}
	
	@RequestMapping(value = "/listByName")
	@ResponseBody
	public String listByName(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("CODEMASTERDETAIL: listByName！");
		String alias = request.getParameter("name");
		if(alias!=null){
		if(alias.equals("")){
			alias = null;
		}
		}
		logger.debug("codeMasterDetail参数：" + alias);
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.debug("行数："+request.getParameter("rows"));
		logger.debug("当前页数："+request.getParameter("page"));		
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		pageCondition.put("name", alias);
		total = infaCommonService.countObject("detail.detailsearchCount", pageCondition);	
		logger.debug("total:"+total);
		logger.debug("==="+pageCondition);
		List<CodeMasterDetail> list = infaCommonService.findObjects("detail.findByName", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}
}
