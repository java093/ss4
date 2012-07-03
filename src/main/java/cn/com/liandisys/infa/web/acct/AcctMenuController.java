package cn.com.liandisys.infa.web.acct;

import java.util.ArrayList;
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

import cn.com.liandisys.idev.modules.security.entity.Function;
import cn.com.liandisys.idev.modules.security.entity.SystemModule;
import cn.com.liandisys.idev.modules.security.entity.impl.FunctionImpl;
import cn.com.liandisys.idev.modules.security.service.SecurityService;
import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.acct.User;
import cn.com.liandisys.infa.entity.acct.UserFunc;
import cn.com.liandisys.infa.entity.mng.SysDictionary;
import cn.com.liandisys.infa.model.Attributes;
import cn.com.liandisys.infa.model.TreeNode;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.JsonCon;
import cn.com.liandisys.infa.util.PagingUtil;
import cn.com.liandisys.infa.util.StringUtil;



/**
 * 人员管理模块ACTION，
 * 
 * 
 */
@Controller
@RequestMapping(value = "/acctmenu")
public class AcctMenuController {

	private static Logger logger = LoggerFactory
			.getLogger(AcctMenuController.class);

	@Autowired
	private InfaCommonService infaCommonService;

	@Autowired
	private SecurityService securityService;

	/**
	 * 人员List
	 */
	private List<User> rows = null;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("跳转到菜单列表页面！");

		// 返回的视图名称
		return "/acct/acctmenu";
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
				"acct.findAllAcctUser", null);
		long total = infaCommonService.countObject("acct.acctUserCount", null);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		if (logger.isInfoEnabled()) {
			logger.info("AcctUser: list！");
			logger.info(JasonUtil.JasonTOString(list));
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
		logger.info("增加菜单: add！parentId="+request.getParameter("parentId"));
		Function function = new FunctionImpl();
		function.setAvaliable(true);
		function.setName(request.getParameter("name"));
		function.setParentId(new Long(request.getParameter("parentId")));

		function.setCode(request.getParameter("code"));
		function.setLeaf(true);
		function.setSystemModule(securityService.getSystemModule(new Long(1)));
		logger.info("name="+request.getParameter("name"));
		securityService.saveFunction(function);
		logger.info("系统模块表据！"+securityService.getPermittedSystemModules());
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

		logger.info("AcctUser: 更新编辑数据！");
		User user = new User();
		user.setId(new Long(request.getParameter("id")));
		user.setEmail(request.getParameter("email"));
		user.setLogin_name(request.getParameter("login_name"));
		user.setName(request.getParameter("name"));
		user.setPassword(request.getParameter("password"));

		infaCommonService.modifyObject("acct.updateActtUser", user);
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
		String id = request.getParameter("id");
		logger.info("AcctUser: 删除系统菜单id="+id);
		securityService.deleteFunction(new Long(id));
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
		logger.info("AcctUser: findbyid 取得编辑数据！");
		User user = (User) infaCommonService.findObject("acct.findById",
				request.getParameter("id"));
		return JasonUtil.JasonTOString(user);
	}

	/**
	 * 菜单树
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/treeList")
	@ResponseBody
	public String treeList(HttpServletRequest request, Model model)
			throws BusinessException {
		List<SystemModule> listSystemModule=null;
		List<Function> listFunction=null;
		
		String id = (String) request.getParameter("id");
		String userid = (String) request.getParameter("userid");
		
		String functionTag =(String) request.getParameter("tag");
		logger.info("id-----------------！" + id+";functionTag="+functionTag);
		if("1".equals(functionTag)){//--如果是功能菜单
			listFunction=securityService.getPermittedFunctionChildren(new Long(id).longValue());
		}else{						//--如果是模块
			if("0".equals(id)){//是根节点 先取模块列表
				listSystemModule=securityService.getPermittedSystemModules();
			}else{//			不是根节点 根据模块名取系统名
				listFunction=securityService.getRootFuncBySystemModuleId(new Long(id).longValue());
			}
		}
		String ss = "";
		ss += "[";
		if("0".equals(id)){//根节点
			logger.info("取得用户所允许的 模块--菜单 模块数listSystemModule.size()="+listSystemModule.size());
			for (SystemModule systemModule : listSystemModule) {
				ss += "{";
				List<Function> cs = securityService.getPermittedFunctions(systemModule.getId()); // 判断当前节点是否还有子节点
				
				if (cs.size() == 0) { // 没有子节点 设置 state 为空
					ss += String
							.format("\"id\": \"%s\", \"text\": \"%s\", \"iconCls\": \"\", \"state\": \"\", \"attributes\": {\"tag\": \"0\"}",
									systemModule.getId(), systemModule.getName());
				} else { // 还有子节点 设置 state为closed
					ss += String
							.format("\"id\": \"%s\", \"text\": \"%s\", \"iconCls\": \"\", \"state\": \"closed\", \"attributes\": {\"tag\": \"0\"}",
									systemModule.getId(), systemModule.getName());
				}
				ss += "},";
			}
		}else{
			logger.info("菜单----");
			for (Function funcion : listFunction) {
				ss += "{";
//				List<Function> cs = securityService.getPermittedFunctionChildren(funcion.getId()); // 判断当前节点是否还有子节点
				if (funcion.isLeaf()) { // 没有子节点 设置 state 为空
					
					Long longUserid=null;
					if (StringUtil.isNotEmpty(userid)) {
						longUserid = Long.valueOf(userid);
					}else{
						longUserid = Long.valueOf(1);
					}
					List<UserFunc> list = infaCommonService.findObjects(
							"acct.findByIdUserFunc", longUserid);
					logger.info("userid="+userid);
					boolean b=false;
					
					String strTemp=String
							.format("\"id\": \"%s\", \"text\": \"%s\", \"iconCls\": \"\", \"state\": \"\", \"attributes\": {\"tag\": \"0\"}",
									funcion.getId(), funcion.getName());
					for (UserFunc userFunc : list) {
						if(userFunc.getFUNC_ID().equals(funcion.getId())){
							logger.info("userFunction b="+b+";userFunc.getFUNC_ID()="+userFunc.getFUNC_ID()+";funcion.getId()="+funcion.getId()+";是否选中:"+(userFunc.getFUNC_ID().equals(funcion.getId())));
							strTemp=String.format("\"id\": \"%s\", \"text\": \"%s\", \"iconCls\": \"\", \"state\": \"\", \"checked\": \"true\", \"attributes\": {\"tag\": \"0\"}",
									funcion.getId(), funcion.getName());
						}
					}
					ss += strTemp;
					
				} else { // 还有子节点 设置 state为closed

					ss += String
							.format("\"id\": \"%s\", \"text\": \"%s\", \"iconCls\": \"\", \"state\": \"closed\", \"attributes\": {\"tag\": \"1\"}",
									funcion.getId(), funcion.getName());
				}
				ss += "},";
			}
		
		}
		
		
		ss = ss.substring(0, ss.length() - 1);
		ss += "]";
		return ss;
	}
	
	/**
	 * 左侧菜单树
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/leftTreeList")
	@ResponseBody
	public String leftTreeList(HttpServletRequest request, Model model)
			throws BusinessException {
		String ss = "";
		String sysModulId = request.getParameter("moduleid");
		Long sysid = 1L;

		String strmenuId = request.getParameter("id");
		Long menuId = null;
		if (StringUtil.isNotEmpty(strmenuId)) {
			menuId = Long.valueOf(strmenuId);
		}

		if (StringUtil.isNotEmpty(sysModulId)) {
			sysid = Long.valueOf(sysModulId);
		}
		
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		List<Function> funcs = null;
		if (menuId == null) {
			funcs = securityService.getPermittedFunctions(sysid);
		} else {
			funcs = securityService.getPermittedFunctionChildren(menuId);
		}
		logger.info("sysModulId="+sysModulId+";strmenuId="+strmenuId);
		TreeNode treeNode = null;
		for (int i = 0; i < funcs.size(); i++) {
			Function tempFun = funcs.get(i);
//			treeNode = new TreeNode(String.valueOf(tempFun.getId()), tempFun
//					.getHelpUrl(), "");
			treeNode = new TreeNode(String.valueOf(tempFun.getId()), tempFun.getHelpUrl(), "");
			if (tempFun.isLeaf()) {
				treeNode.setState("open");
			} else {
				treeNode.setState("closed");
			}
			Attributes attr = new Attributes("closed", false);
			attr.setLeaf(tempFun.isLeaf());
			treeNode.setAttributes(attr);
			nodeList.add(treeNode);
		}
		ss = JsonCon.list2json(nodeList);
		return ss;
	}
}

