package cn.com.liandisys.infa.web.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.liandisys.infa.entity.account.Group;
import cn.com.liandisys.infa.entity.account.Permission;

@Controller
@RequestMapping(value = "/account/group")
public class GroupController {

//	private AccountService accountService;

//	@RequestMapping(value = { "list", "" })
//	public String list(Model model) {
//		List<Group> groups = accountService.getAllGroup();
//		model.addAttribute("groups", groups);
//		return "account/groupList";
//	}

	@RequestMapping(value = "create")
	public String createForm(Model model) {
		model.addAttribute("group", new Group());
		model.addAttribute("allPermissions", Permission.values());
		return "account/groupForm";
	}

//	@RequestMapping(value = "save")
//	public String save(Group group, RedirectAttributes redirectAttributes) {
//		accountService.saveGroup(group);
//		redirectAttributes.addFlashAttribute("message",
//				"创建权限组" + group.getName() + "成功");
//		return "redirect:/account/group/";
//	}

//	@RequestMapping(value = "delete/{id}")
//	public String delete(@PathVariable("id") Long id,
//			RedirectAttributes redirectAttributes) {
//		accountService.deleteGroup(id);
//		redirectAttributes.addFlashAttribute("message", "删除权限组成功");
//		return "redirect:/account/group/";
//	}

//	@Autowired
//	public void setAccountManager(AccountService accountService) {
//		this.accountService = accountService;
//	}

}
