package cn.com.liandisys.infa.web.acct;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import cn.com.liandisys.idev.modules.security.entity.SpecialOrg;
import cn.com.liandisys.idev.modules.security.entity.impl.UserImpl;
import cn.com.liandisys.idev.modules.security.service.SecurityService;
import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.account.MenuFunction;
import cn.com.liandisys.infa.entity.acct.TSysUser;
import cn.com.liandisys.infa.entity.acct.User;
import cn.com.liandisys.infa.entity.acct.UserFunc;
import cn.com.liandisys.infa.entity.mng.SysDictionary;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.PagingUtil;

/**
 * 人员管理模块ACTION，
 * 
 * 
 */
@Controller
@RequestMapping(value = "/acctuser")
public class AcctUserController {

	private static Logger logger = LoggerFactory
			.getLogger(AcctUserController.class);

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
			logger.info("跳转到人员列表页面！");
		// 返回的视图名称
		return "/acct/acctuser";
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
		long total = infaCommonService
				.countObject("acct.acctUserCount", null);
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
	 * @throws IOException 
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public String add(HttpServletRequest request, Model model)
			throws BusinessException, IOException {
			logger.info("acctUser: add！");

		UserImpl userImpl=new UserImpl();
		userImpl.setId(new Long("11"));
		userImpl.setAdmin(false);
		userImpl.setAvaliable(true);
		userImpl.setLoginName(request.getParameter("login_name"));
		userImpl.setName(request.getParameter("name"));
		String input =request.getParameter("password");
		String passwordforDB = cn.com.liandisys.idev.modules.security.utils.Digests.md5Hex(new ByteArrayInputStream(input.getBytes()));
		userImpl.setPassword(passwordforDB);
		SpecialOrg specialOrg = securityService.getSpecialOrg(1L);
		userImpl.setSpecialOrg(specialOrg);
		securityService.saveUser(userImpl);
		return JasonUtil.getJsonResult(true);
	}
	
	/**
	 * 保存用户菜单
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/saveUserMenu",method=RequestMethod.POST)
	@ResponseBody
	public String saveUserMenu(HttpServletRequest request, Model model)
			throws BusinessException {
			logger.info("saveUserMenu！");
			logger.info("ids="+request.getParameter("ids"));
		String ids=request.getParameter("ids");
		String userid=request.getParameter("userid");
		
		String[] arrIds=ids.split(",");
		UserFunc userFunc=new UserFunc();
		userFunc.setUSER_ID(new Long(userid));
		infaCommonService.removeObject("acct.deleteUserFunc", userFunc);
		logger.info("用户菜单授权 userid="+userid+";授权菜单ids="+ids);
		for(int i=0;i<arrIds.length;i++){
//			userFunc.setFUNC_ID(new Long(arrIds[i]).longValue());
			securityService.authorizationToUser(true, new Long(userid), new Long(arrIds[i]).longValue(), null);
		}
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
		User user=new User();
		user.setId(new Long(request.getParameter("id")));
		user.setName(request.getParameter("name"));
		infaCommonService.modifyObject("acct.updateActtUser", user);
		return JasonUtil.getJsonResult(true);
	}
	
	/**
	 * 编辑
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 * @throws IOException 
	 */
	@RequestMapping(value = "/updatePwd")
	@ResponseBody
	public String updatePwd(HttpServletRequest request, Model model)
			throws BusinessException, IOException {
		logger.info("AcctUser: 更新密码！");
		User user=new User();
		user.setId(new Long(request.getParameter("id")));
		String passwordforDB = 
				cn.com.liandisys.idev.modules.security.utils.Digests.md5Hex(
						new ByteArrayInputStream(request.getParameter("password").getBytes()));
		user.setPassword(passwordforDB);
		infaCommonService.modifyObject("acct.updateActtUserPwd", user);
		return JasonUtil.getJsonResult(true);
	}
	
	/**
	 * 编辑
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 * @throws IOException 
	 */
	@RequestMapping(value = "/updateCurrentUserPwd")
	@ResponseBody
	public String updateCurrentUserPwd(HttpServletRequest request, Model model)
			throws BusinessException, IOException {
		logger.info("AcctUser: 更新当前用户密码！");
		User user=new User();
		user.setId(securityService.getCurrentUser().getId());
		String passwordforDB = 
				cn.com.liandisys.idev.modules.security.utils.Digests.md5Hex(
						new ByteArrayInputStream(request.getParameter("password").getBytes()));
		user.setPassword(passwordforDB);
		infaCommonService.modifyObject("acct.updateActtUserPwd", user);
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
		if (logger.isInfoEnabled()) {
			logger.info("AcctUser: delete！");
		}
		rows = new ArrayList<User>();
		String ids = request.getParameter("ids");
		String[] arrayId =  ids.split(",");
		for (int i = 0; i < arrayId.length; i++) {
			User user = new User();
			user.setId(Long.valueOf(arrayId[i]));
			rows.add(user);
		}
		infaCommonService.batchRemoveObject("acct.deleteAcctUser", rows);
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
		TSysUser tSysUser = (TSysUser) infaCommonService.findObject(
				"acct.findById", request.getParameter("id"));
		return JasonUtil.JasonTOString(tSysUser);
	}
	
	/**
	 * 根据登陆名查找
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/findByLoginName")
	@ResponseBody
	public String findByLoginName(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("AcctUser: 校验新加登陆名是否重复!");
		String login_name=request.getParameter("login_name");
		
		long total = infaCommonService.countObject(
				"acct.findByLoginName", request.getParameter("login_name"));
		logger.info("AcctUser: 已有登陆名个数=!"+total);
		if(total>0){
			return "false";
		}else{
			return "true";
		}
	}
	
	@RequestMapping(value = "/treeList")
	@ResponseBody
	public String treeList(HttpServletRequest request, Model model)
			throws BusinessException {
		List<Function> listFunction=new ArrayList<Function>(10);
		String id=(String)request.getParameter("id");
		if("0".equals(id)){
			for(int i=0;i<10;i++){
				Function function=new MenuFunction();
				function.setId(new Long(i).longValue());
				function.setName("菜单"+i);
				listFunction.add(function);
			}
		}
		if("1".equals(id)){
			Function function=new MenuFunction();
			function.setId(new Long(11).longValue());
			function.setName("菜单"+11);
			listFunction.add(function);
		}
		String ss="";
        ss+="[";
        for( Function function : listFunction )
        {
			ss += "{";
			List<Function> cs = new ArrayList<Function>(0); // 判断当前节点是否还有子节点
			
			//暂时将第一个节点设置为有子节点
			if(function.getId()==1){
				Function functionSub=new MenuFunction();
				functionSub.setId(new Long(11).longValue());
				functionSub.setName("菜单1-1");
				cs.add(functionSub);
			}
			if (cs.size() == 0) { // 没有子节点 设置 state 为空
				logger.info("function.getId()="+function.getId()+"function.getName()="+function.getName());
				ss += String
						.format("\"id\": \"%s\", \"text\": \"%s\", \"iconCls\": \"\", \"state\": \"\"",
								function.getId(), function.getName());
			} else { // 还有子节点 设置 state为closed
				logger.info("function.getId()="+function.getId()+"function.getName()="+function.getName());
				ss += String
						.format("\"id\": \"%s\", \"text\": \"%s\", \"iconCls\": \"\", \"state\": \"closed\"",
								function.getId(), function.getName());
			}
			ss += "},";
		}
             ss=ss.substring(0, ss.length() - 1); 
             ss+="]";
        return ss;            
//		return "[{\"text\":\"Languages\",\"state\":\"closed\",\"children\":[{\"id\":\"j1\",\"text\":\"Java\"},{\"id\":\"j2\",\"text\":\"C#\"}]}]";
	}
	
}
