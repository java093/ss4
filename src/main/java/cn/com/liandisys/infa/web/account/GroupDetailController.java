package cn.com.liandisys.infa.web.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.liandisys.infa.entity.account.Group;
import cn.com.liandisys.infa.entity.account.Permission;

@Controller
@RequestMapping(value = "/account/group/")
public class GroupDetailController {

//    private AccountService accountService;

    @RequestMapping(value = "update/{id}")
    public String updateForm(Model model) {
        model.addAttribute("allPermissions", Permission.values());
        return "account/groupForm";
    }

    @RequestMapping(value = "save/{id}")
    public String save(@ModelAttribute("group") Group group,
            RedirectAttributes redirectAttributes) {
//        accountService.saveGroup(group);
//    	try {
//			accountService.addObject("account.insertGroup", group);
//		} catch (BusinessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        redirectAttributes.addFlashAttribute("message",
                "修改权限组" + group.getName() + "成功");
        return "redirect:/account/group/";
    }

    @ModelAttribute("group")
    public Group getGroup(@PathVariable("id") Long id) {
    	Group group = null;
//    	try {
//    		Group grp = new Group();
//    		grp.setId(id);
//			accountService.findObject("accout.selectGroupsByCond", grp);
//		} catch (BusinessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        return group;
    }

//    @Autowired
//    public void setAccountManager(AccountService accountService) {
//        this.accountService = accountService;
//    }
}
