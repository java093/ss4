package cn.com.liandisys.infa.util;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.com.liandisys.idev.ssi.util.PropertiesUtil;

/**
 * Property加载.
 */
public class WebConfigListener implements ServletContextListener {

    /**
     * Context initialized.
     *
     * @param servletContextEvent servletContextEvent.
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        PropertiesUtil.properties = new Properties();

        try {
            String path = (getClass().getClassLoader().getResource("").toURI()).getPath();
            FileInputStream fis = new FileInputStream(path + "exception.properties");
            InfaPropertiesUtil.properties.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Context destroyed.
     *
     * @param servletContextEvent  servletContextEvent.
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        PropertiesUtil.properties = null;
    }
}
