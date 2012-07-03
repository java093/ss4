package cn.com.liandisys.infa.web.account;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.account.User;

/**
 * Urls:
 * List   page        : GET  /account/user/
 * Create page        : GET  /account/user/create
 * Create action      : POST /account/user/save
 * Update page        : GET  /account/user/update/{id}
 * Update action      : POST /account/user/save/{id}
 * Delete action      : POST /account/user/delete/{id}
 * CheckLoginName ajax: GET  /account/user/checkLoginName?oldLoginName=a&loginName=b
 * 
 */
@Controller
@RequestMapping(value = "/account/user")
public class UserController {

//	private AccountService accountService;
	@Autowired
	private GroupListEditor groupListEditor;

	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(List.class, "groupList", groupListEditor);
	}

	@RequiresPermissions("user:view")
	@RequestMapping(value = { "list", "" })
	public String list(Model model) throws BusinessException {
		List<User> users = null;
//		List<User> users = accountService.getAllUser();
//		users = accountService.findObjects("account.getUsers", null);
//		model.addAttribute("users", users);
		return "account/userList";
	}
	
//	@RequiresPermissions("user:edit")
//	@RequestMapping(value = "create")
//	public String createForm(Model model) {
//		model.addAttribute("user", new User());
//		model.addAttribute("allGroups", accountService.getAllGroup());
//		return "account/userForm";
//	}
	
//	@RequiresPermissions("user:edit")
//	@RequestMapping(value = "save")
//	public String save(User user, RedirectAttributes redirectAttributes) {
//		accountService.saveUser(user);
//		redirectAttributes.addFlashAttribute("message", "创建用户" + user.getLoginName() + "成功");
//		return "redirect:/account/user/";
//	}
//	
//	@RequiresPermissions("user:edit")
//	@RequestMapping(value = "delete/{id}")
//	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
//		accountService.deleteUser(id);
//		redirectAttributes.addFlashAttribute("message", "删除用户成功");
//		return "redirect:/account/user/";
//	}
//	
//	@RequiresPermissions("user:edit")
//	@RequestMapping(value = "checkLoginName")
//	@ResponseBody
//	public String checkLoginName(@RequestParam("oldLoginName") String oldLoginName,
//			@RequestParam("loginName") String loginName) {
//		if (loginName.equals(oldLoginName)) {
//			return "true";
//		} else if (accountService.findUserByLoginName(loginName) == null) {
//			return "true";
//		}
//
//		return "false";
//	}

//	@Autowired
//	public void setAccountService(AccountService accountService) {
//		this.accountService = accountService;
//	}

//	@Autowired
//	public void setGroupListEditor(GroupListEditor groupListEditor) {
//		this.groupListEditor = groupListEditor;
//	}

}
