package cn.com.liandisys.infa.web.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 使用@ModelAttribute, 实现Struts2 Preparable二次绑定的效果。 
 * 因为@ModelAttribute被默认执行, 而其他的action url中并没有${id}，所以需要独立出一个Controller.
 * 
 * 
 */
@Controller
@RequestMapping(value = "/account/user/")
public class UserDetailController {

//    private AccountService accountService;
	@Autowired
    private GroupListEditor groupListEditor;

    @InitBinder
    public void initBinder(WebDataBinder b) {
        b.registerCustomEditor(List.class, "groupList", groupListEditor);
    }

//    @RequestMapping(value = "update/{id}")
//    public String updateForm(Model model) {
//        model.addAttribute("allGroups", accountService.getAllGroup());
//        return "account/userForm";
//    }

//    @RequestMapping(value = "save/{id}")
//    public String save(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
//        accountService.saveUser(user);
//        redirectAttributes.addFlashAttribute("message", "修改用户" + user.getLoginName() + "成功");
//        return "redirect:/account/user/";
//    }

//    @ModelAttribute("user")
//    public User getAccount(@PathVariable("id") Long id) {
//        return accountService.getUser(id);
//    }

//    @Autowired
//    public void setAccountManager(AccountService accountService) {
//        this.accountService = accountService;
//    }

//    @Autowired
//    public void setGroupListEditor(GroupListEditor groupListEditor) {
//        this.groupListEditor = groupListEditor;
//    }
}
