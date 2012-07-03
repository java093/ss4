package cn.com.liandisys.infa.web.sys;

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
import cn.com.liandisys.infa.entity.sys.MailConfig;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.PagingUtil;

/**
 * 任务管理模块ACTION，
 * 
 * 真正任务的POST请求由Filter完成
 * 
 */
@Controller
@RequestMapping(value = "/mail")
public class MailController {

	private static Logger logger = LoggerFactory
			.getLogger(MailController.class);

	@Autowired
	private InfaCommonService infaCommonService;
	private long total;

	private int currentpage;
    private int limit;
    private Map<String, Object> pageCondition;
	/**
     * 任务List
     */
    private List <MailConfig> rows= null;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("验证成功，跳转到邮件配置页面！");
		}
		// 返回的视图名称
		return "/sys/mailconfig";
	}
	
	/**
	 * 初始化一览数据
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public String list(HttpServletRequest request, Model model)
			throws BusinessException {
		MailConfig mail = (MailConfig) infaCommonService.findObject(
				"mail.findAllMail", null);
//		long total = infaCommonService.countObject("mail.mailCount", null);
//		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
//				.getParameter("rows")), Integer.parseInt(request
//				.getParameter("page")), total);
		if (logger.isDebugEnabled()) {
			logger.debug("Mail: list！");
			logger.debug(JasonUtil.JasonTOString(mail));
		}

		return JasonUtil.JasonTOString(mail);

	}
	/**
	 * 增加
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
			logger.debug("MAIL: add！");
		}
		/**20120328修改，通过解析LOGICALNAME获取username并存入数据库*/
		String [] s = request.getParameter("LOGICALNAME").split("@");
		String username = s[0];
		MailConfig mailConfig = new MailConfig();
		mailConfig.setHOST(request.getParameter("HOST"));
		mailConfig.setPORT(request.getParameter("PORT"));
		mailConfig.setLOGICALNAME(request.getParameter("LOGICALNAME"));
		mailConfig.setUSERNAME(username);
		mailConfig.setPASSWORD(request.getParameter("PASSWORD"));
		infaCommonService.addObject("mail.insertMail", mailConfig);
		return JasonUtil.getJsonResult(true);
	}
	
	/**
	 * 编辑
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	public String update(HttpServletRequest request, Model model) 
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("MAIL: edit！");
		}
		/**20120328修改，通过解析LOGICALNAME获取username并存入数据库*/
		String [] s = request.getParameter("LOGICALNAME").split("@");
		String username = s[0];
		MailConfig mailConfig = new MailConfig();
		
		String id = request.getParameter("id");
		mailConfig.setId(Long.valueOf(id));	
		mailConfig.setHOST(request.getParameter("HOST"));
		mailConfig.setPORT(request.getParameter("PORT"));
		mailConfig.setLOGICALNAME(request.getParameter("LOGICALNAME"));
		mailConfig.setUSERNAME(username);
		mailConfig.setPASSWORD(request.getParameter("PASSWORD"));
    	infaCommonService.modifyObject("mail.updateMail",mailConfig);
		return JasonUtil.getJsonResult(true);
	}
	
	/**
	 * 删除（多行）
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public String delete(HttpServletRequest request, Model model) 
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("MAIL: delete！");
		}
		rows = new ArrayList<MailConfig>();
        String selectIdStr = request.getParameter("ids");
        String[] selectedIdArr=selectIdStr.split(",");
        for (int i = 0; i < selectedIdArr.length; i++) {
        	MailConfig mailConfig = new MailConfig();
        	logger.debug("mailid:"+selectedIdArr[i]);
        	mailConfig.setId(Long.valueOf(selectedIdArr[i])); 	
            rows.add(mailConfig);
        }
        infaCommonService.batchRemoveObject("mail.deleteMail", rows);      
        return JasonUtil.getJsonResult(true);
	}
	
	/**
	 * 根据id查找记录
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/findbyid")
	@ResponseBody
	public String findbyid(HttpServletRequest request, Model model) 
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("Mail: findbyid！");
		}
		MailConfig mailConfig = new MailConfig();
		String id = request.getParameter("id");
		mailConfig.setId(Long.valueOf(id));	
		MailConfig rmailConfig = (MailConfig)infaCommonService.findObject("mail.findByid",mailConfig);
		return JasonUtil.JasonTOString(rmailConfig);
	}
	

	/**
	 * 根据邮件名查找记录
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/listBySearch")
	@ResponseBody
	public String listBySearch(HttpServletRequest request, Model model) 
			throws BusinessException {
		if (logger.isDebugEnabled()) {
			logger.debug("Mail: listBySearch！");
		}
		String name = request.getParameter("name");
		logger.debug("mailname:"+name);
		if(name!=null){
		if(name.equals("")){
			  name = null;
		}
		}
		logger.debug("==========name:"+name);
		// 查询邮件表的数据
		MailConfig mail =new MailConfig();
		mail.setLOGICALNAME(name);
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.debug("行数："+request.getParameter("rows"));
		logger.debug("当前页数："+request.getParameter("page"));		
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		
		pageCondition.put("name", name);
		List<MailConfig> list = infaCommonService.findObjects(
				"mail.findByName", pageCondition);
		
		total = infaCommonService.countObject("mail.mailsearchCount", pageCondition);
		logger.debug("total:"+total);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));

	}
	
	@RequestMapping(value = "/listByName")
	@ResponseBody
	public String listByName(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("MAIL: listByName！");
		String name = request.getParameter("name");
		if(name!=null){
		if(name.equals("")){
			name = null;
		 }
		}
		logger.debug("mail参数：" + name);
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.debug("行数："+request.getParameter("rows"));
		logger.debug("当前页数："+request.getParameter("page"));		
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		pageCondition.put("name", name);
		total = infaCommonService.countObject("mail.mailCount", pageCondition);	
		logger.debug("total:"+total);
		logger.debug("==="+pageCondition);
		List<MailConfig> list = infaCommonService.findObjects("mail.findALL", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}
}
