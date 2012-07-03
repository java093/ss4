package cn.com.liandisys.infa.web.mng;

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

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.mng.CodeMaster;
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
@RequestMapping(value = "/codemaster")
public class CodeMasterController {

	private static Logger logger = LoggerFactory
			.getLogger(MailController.class);

	@Autowired
	private InfaCommonService infaCommonService;
	/**
	 * 任务List
	 */
	private List<CodeMaster> rows = null;
	private long total;

	private int currentpage;
    private int limit;
    private Map<String, Object> pageCondition;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("验证成功，跳转到CODEMASTER页面！");
		}
		// 返回的视图名称
		return "/mng/codemaster";
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
		List<CodeMaster> list = infaCommonService.findObjects(
				"code.findAllCode", null);
		long total = infaCommonService.countObject("code.codeCount", null);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		if (logger.isDebugEnabled()) {
			logger.debug("CODEMASTER: list！");
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
			logger.debug("CODEMASTER: add！");
		}
		CodeMaster code = new CodeMaster();
		code.setCodetype(request.getParameter("codetype"));
		code.setDescription(request.getParameter("description"));
		code.setFlag(request.getParameter("flag"));

		infaCommonService.addObject("code.insertCode", code);
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
			logger.debug("CODEMASTER: edit！");
		}
		CodeMaster code = new CodeMaster();
		code.setId(Long.valueOf(request.getParameter("id")));
		code.setCodetype(request.getParameter("codetype"));
		code.setDescription(request.getParameter("description"));
		code.setFlag(request.getParameter("flag"));
		infaCommonService.modifyObject("code.updateCode", code);
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
			logger.debug("CODEMASTER: delete！");
		}
		rows = new ArrayList<CodeMaster>();
		String selectIdStr = request.getParameter("ids");
		String[] selectedIdArr = selectIdStr.split(",");
		for (int i = 0; i < selectedIdArr.length; i++) {
			CodeMaster code = new CodeMaster();
			logger.debug("CODEMASTERid:" + selectedIdArr[i]);
			code.setId(Long.valueOf(selectedIdArr[i]));
			rows.add(code);
		}
		infaCommonService.batchRemoveObject("code.deleteCode", rows);
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
			logger.debug("CODEMASTER: findbyid！");
		}
		CodeMaster code = new CodeMaster();
		code.setId(Long.valueOf(request.getParameter("id")));
		CodeMaster rcode = (CodeMaster) infaCommonService.findObject(
				"code.findByid", code);
		return JasonUtil.JasonTOString(rcode);
	}
	
	/**
	 * 根据type查找记录
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/findbytype" )
	@ResponseBody
	public String findbytype(HttpServletRequest request, Model model) 
			throws BusinessException {
		
		logger.debug("CODEMASTER: findbytype！");
		CodeMaster code = new CodeMaster();
		String codetype = request.getParameter("codetype");
		String id = request.getParameter("id");
		logger.debug("codetype:"+codetype);	
		logger.debug("id:"+id);	
		if(!id.equals("0")){
			code.setId(Long.valueOf(id));
			CodeMaster rcode = (CodeMaster)infaCommonService.findObject("code.findByid",code);
			if(rcode.getCodetype().equals(codetype)){
				return "true";
			}
		}
		code.setCodetype(codetype);
		long  i = (Long)infaCommonService.findObject("code.findByType",code);
		logger.debug("i:~~~"+i);
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
		total = infaCommonService.countObject("code.codesearchCount", pageCondition);	
		logger.debug("total:"+total);
		logger.debug("==="+pageCondition);
		List<CodeMaster> list = infaCommonService.findObjects("code.findByName", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}
}
