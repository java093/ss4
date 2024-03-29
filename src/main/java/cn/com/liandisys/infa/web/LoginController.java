package cn.com.liandisys.infa.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成
 * 
 */
@Controller
public class LoginController {

	private static Logger logger = LoggerFactory
			.getLogger(LoginController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String fail(
			@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName,
			Model model) {

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,
				userName);
		logger.info("【" + userName + "】验证失败，跳转到登录页面");
		return "login";
	}

	/**
	 * 登陆页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		if (SecurityUtils.getSubject().isAuthenticated()) {
//			model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
			//不注销则直接进入首页
			return "redirect:/pages/index.jsp";
		}
		return "login";
	}

}
