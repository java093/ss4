package cn.com.liandisys.infa.account;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.acct.User;
import cn.com.liandisys.infa.service.InfaCommonService;

public class ShiroDbRealm extends AuthorizingRealm {
	
	private static Logger logger = LoggerFactory
			.getLogger(ShiroDbRealm.class);

	@Autowired
	private InfaCommonService infaCommonService;

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		logger.info("取得的登录名 token.getUsername()="+token.getUsername());
//		User user = accountService.findUserByLoginName(token.getUsername());
		User user = new User();
		try {
			
			List<User> list=infaCommonService.findObjects("acct.findByLogin_name", token.getUsername());
			for(Iterator it=list.iterator(); it.hasNext();) {  
				user=(User)it.next();
	        }  
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (user != null) {
			return new SimpleAuthenticationInfo(new ShiroUser(user.getLogin_name(), user.getName()), user.getPassword(),
					getName());
			
		} else {
			return null;
		}
	}
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.info("授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用");
		System.out.println(principals.fromRealm(getName()));
		ShiroUser shiroUser = (ShiroUser) principals.fromRealm(getName()).iterator().next();
		logger.info("shiroUser.getLoginName()="+shiroUser.getLoginName());
		User user = new User();//accountService.findUserByLoginName(shiroUser.getLoginName());
		if (true) {
//		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//			for (Group group : user.getGroupList()) {
//				info.addStringPermissions(group.getPermissionList());
//			}
			logger.info("info。getStringPermissions()="+getName());
//			info.addStringPermission("/index");
			if("admin".equals(shiroUser.getLoginName())){
				info.addStringPermission("group:view");
			}
			logger.info("info。getStringPermissions()="+info.getStringPermissions());
			return info;
		} else {
			return null;
		}
	}

	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {

		private static final long serialVersionUID = -1748602382963711884L;
		private String loginName;
		private String name;

		public ShiroUser(String loginName, String name) {
			this.loginName = loginName;
			this.name = name;
		}

		public String getLoginName() {
			return loginName;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}

		public String getName() {
			return name;
		}
	}
}
