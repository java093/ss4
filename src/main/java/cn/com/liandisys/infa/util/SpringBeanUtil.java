package cn.com.liandisys.infa.util;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Spring容器工具类
 */
public class SpringBeanUtil {
	
	public static ApplicationContext applicationContext;  
	
	private static ServletContext servletContext;
	
	
	/**
	 * 初期化applicationContext
	 * @param sc
	 */
    public static void init(ServletContext _servletContext){  
    	servletContext = _servletContext;
        if(applicationContext == null){
        	// 取得Spring ApplicationContext
        	applicationContext = WebApplicationContextUtils
    				.getWebApplicationContext(servletContext,
    						WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        }  
    }  
	
    /**
     * 取得Bean对象
     * @param beanName bean名
     * @return Object
     */
	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
	
	public static ServletContext getServletContext(){
		return servletContext;
	}
}
