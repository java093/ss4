package cn.com.liandisys.infa.util;

import cn.com.liandisys.idev.ssi.util.PropertiesUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: xuyx
 * Date: 12-4-10
 * Time: 下午3:37
 * To change this template use File | Settings | File Templates.
 */
public class InfaPropertiesUtil extends PropertiesUtil {

    /**
     * Gets the property.
     *
     * @param fileName String
     * @param keyName .String
     * @return  string
     */
    public static String getProperty(String fileName, String keyName) {
        InputStream in = null;
        Properties p = new Properties();
        try {
            in = InfaPropertiesUtil.class.getResourceAsStream(fileName);
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return p.getProperty(keyName);
    }
}
