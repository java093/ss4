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
import cn.com.liandisys.infa.entity.mng.ConTranDict;
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
@RequestMapping(value = "/con")
public class ConTranDictController {

	private static Logger logger = LoggerFactory
			.getLogger(MailController.class);

	@Autowired
	private InfaCommonService infaCommonService;
	@Autowired
	private SecurityService securityService;
	/**
	 * 任务List
	 */
	private List<ConTranDict> rows = null;
	private long total;

	private int currentpage;
    private int limit;
    private Map<String, Object> pageCondition;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("验证成功，跳转到ConTranDict页面！");
		}
		// 返回的视图名称
		return "/mng/con_tran_dict";
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
		List<ConTranDict> list = infaCommonService.findObjects(
				"con.findAllCon", null);
		long total = infaCommonService.countObject("con.conCount", null);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		if (logger.isDebugEnabled()) {
			logger.debug("CON: list！");
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
			logger.debug("CON: add！");
			logger.debug("tran_dict_code=="
					+ request.getParameter("tran_dict_code"));
		}
		Timestamp time = new Timestamp(System.currentTimeMillis()); 
		ConTranDict con = new ConTranDict();
		con.setSou_code(request.getParameter("sou_code"));
		con.setSou_desc(request.getParameter("sou_desc"));
		con.setSou_dict_code(request.getParameter("sou_dict_code"));
		con.setSou_dict_desc(request.getParameter("sou_dict_desc"));
		con.setSou_sub_dict_code(request.getParameter("sou_sub_dict_code"));
		con.setSou_sub_dict_desc(request.getParameter("sou_sub_dict_desc"));
		con.setTran_dict_code(request.getParameter("tran_dict_code"));
		con.setTran_dict_desc(request.getParameter("tran_dict_desc"));
		con.setTran_code(request.getParameter("tran_code"));
		con.setTran_desc(request.getParameter("tran_desc"));
		con.setDefunct_ind(request.getParameter("defunct_ind"));
		con.setRemarks(request.getParameter("remarks"));
		con.setCreated_by(securityService.getCurrentUser().getId());
		con.setCreated_datetime(time);
		infaCommonService.addObject("con.insertCon", con);
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
			logger.debug("CON: edit！");
		}
		ConTranDict con = new ConTranDict();
		con.setSou_code(request.getParameter("sou_code"));
		con.setSou_desc(request.getParameter("sou_desc"));
		con.setSou_dict_code(request.getParameter("sou_dict_code"));
		con.setSou_dict_desc(request.getParameter("sou_dict_desc"));
		con.setSou_sub_dict_code(request.getParameter("sou_sub_dict_code"));
		con.setSou_sub_dict_desc(request.getParameter("sou_sub_dict_desc"));
		con.setTran_dict_code(request.getParameter("tran_dict_code"));
		con.setTran_dict_desc(request.getParameter("tran_dict_desc"));
		con.setTran_code(request.getParameter("tran_code"));
		con.setTran_desc(request.getParameter("tran_desc"));
		con.setDefunct_ind(request.getParameter("defunct_ind"));
		con.setRemarks(request.getParameter("remarks"));
		infaCommonService.modifyObject("con.updateCon", con);
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
			logger.debug("CON: delete！");
		}
		rows = new ArrayList<ConTranDict>();
		String sou_codes = request.getParameter("sou_codes");
		String[] sou_code = sou_codes.split(",");
		String sou_dict_codes = request.getParameter("sou_dict_codes");
		String[] sou_dict_code = sou_dict_codes.split(",");
		String sou_sub_dict_codes = request.getParameter("sou_sub_dict_codes");
		String[] sou_sub_dict_code = sou_sub_dict_codes.split(",");
		for (int i = 0; i < sou_code.length; i++) {
			ConTranDict con = new ConTranDict();
			con.setSou_code(sou_code[i]);
			con.setSou_dict_code(sou_dict_code[i]);
			con.setSou_sub_dict_code(sou_sub_dict_code[i]);
			rows.add(con);
		}
		infaCommonService.batchRemoveObject("con.deleteCon", rows);
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
			logger.debug("CON: findbyid！");
		}
		ConTranDict con = new ConTranDict();
		con.setSou_code(request.getParameter("sou_code"));
		con.setSou_dict_code(request.getParameter("sou_dict_code"));
		con.setSou_sub_dict_code(request.getParameter("sou_sub_dict_code"));
		ConTranDict rcon = (ConTranDict) infaCommonService.findObject(
				"con.findByid", con);
		return JasonUtil.JasonTOString(rcon);
	}
	
	/**
	 * 根据前三项代码查找记录
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
		ConTranDict con = new ConTranDict();
		String sou_code = request.getParameter("sou_code");
		String sou_dict_code = request.getParameter("sou_dict_code");
		String sou_sub_dict_code = request.getParameter("sou_sub_dict_code");
		logger.debug("sou_code===="+sou_code);
		logger.debug("sou_dict_code===="+sou_dict_code);
		logger.debug("sou_sub_dict_code===="+sou_sub_dict_code);
		con.setSou_code(sou_code);
		con.setSou_dict_code(sou_dict_code);
		con.setSou_sub_dict_code(sou_sub_dict_code);
		long  i = (Long)infaCommonService.findObject("con.findByCode",con);
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
		logger.debug("CON: listByName！");
		String alias = request.getParameter("name");
		if(alias!=null){
		if(alias.equals("")){
			alias = null;
		}
		}
		logger.debug("CON参数：" + alias);
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.debug("行数："+request.getParameter("rows"));
		logger.debug("当前页数："+request.getParameter("page"));		
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		pageCondition.put("name", alias);
		total = infaCommonService.countObject("con.consearchCount", pageCondition);	
		logger.debug("total:"+total);
		logger.debug("==="+pageCondition);
		List<ConTranDict> list = infaCommonService.findObjects("con.findByName", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}
}
