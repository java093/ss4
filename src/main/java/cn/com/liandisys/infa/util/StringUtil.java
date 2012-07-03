/* ============================================================ 
 * $Id: StringUtil.java 9140 2011-02-22 08:51:48Z jipl $ 
 * Created: [2010-12-21 10:18:53] by gaowei 
 * ============================================================ 
 * 
 * ProjectName 
 * 
 * Description 
 * 
 *  isEmpty(String src)
 * ============================================================ 
 * 
 * Copyright Information. 
 * 
 * ==========================================================*/
package cn.com.liandisys.infa.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 文字列处理类。
 * </p>
 * 进行文字列相关的处理。
 * 
 * @author $Author: l_walker$
 * @version $Revision: 9140 $ $Date: 2011-02-22 16:51:48 +0800 (星期二, 22 二月 2011) $
 */
public class StringUtil {

	/**
	 * 字符串 4
	 */
	private static final int INT_4 = 4;

	/**
	 * 字符串 6
	 */
	private static final int INT_6 = 6;

	/**
	 * 字符串 8
	 */
	private static final int INT_8 = 8;

	/**
	 * 字符串 -
	 */
	private static final String STR_MINUS = "-";


    /**
     * 判断给定字符串是否空
     * 
     * @param src
     *            字符串
     * @return boolean 空时返回true，非空返回false
     */
	public static boolean isTrimEmpty(String str){
		if(str == null || str.trim().length()==0){
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(String str){
		if(str == null || str.length()==0){
			return true;
		}
		return false;
	}
	
	public static boolean isNotTrimEmpty(String str){
		if(str == null || str.trim().length()==0){
			return false;
		}
		return true;
	}
	
	public static boolean isNotEmpty(String str){
		if(str == null || str.length()==0){
			return false;
		}
		return true;
	}

    /**
     * 返回指定字符串，但当空的时候返回""。
     * 
     * @param src
     *            字符串
     * @return String 空时返回""，否则返回参数本身
     */
    public static String nvl(String src) {
        if (src == null) {
            return "";
        }
        else if (src.equals("null")) {
            return "";
        }
        return src;
    }

    /**
     * 返回指定字符串，但当空的时候返回""。
     * 
     * @param string
     *            字符串
     * @return String 空时返回""，否则返回参数本身
     */
    public static String notNull(String string) {
        return nvl(string);
    }

    /**
     * 返回指定字符串对象，但当空的时候返回""。
     * 
     * @param o BigDecimal对象
     * @return String 空时返回""，否则返回参数本身
     */
    public static String bigDecimal2String(BigDecimal o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    /**
     * 将时间字符串转化为 yyyy-mm-dd的字符串格式
     * @param date 字符串
     * @return 如果传入不为空并且length为8 则转化
     */
    public static String formatString2Date(String date){
        if(date==null||date.equals("")){
            return null;
        }else{
            if(date.length()==INT_8){
                String time="";
                time=time+date.substring(0, INT_4)
                	+STR_MINUS+date.substring(INT_4, INT_6)+STR_MINUS+date.substring(INT_6, INT_8);
                return time;
            }
            return date;
        }
    }
    
    /**
     * 返回指定字符串对象，但当空的时候返回""。
     * 
     * @param o Date对象
     * @return String 空时返回""，否则返回参数本身
     */
    public static String date2String(Date o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    /**
     * 判断给定字符串是否为数字表达式
     * 
     * @param number
     *            字符串
     * @return boolean
     */
    public static boolean isNumber(String number) {
        if (number == null || number.trim().length() <= 0) {
            return false;
        } 

        boolean isNumber = true;
        char h = number.charAt(0);
        if (h != '0' && h != '1' && h != '2' && h != '3' 
                && h != '4' && h != '5' && h != '6' && h != '7'
                && h != '8' && h != '9' && h != '+' && h != '-' && h != '.') {
            isNumber = false;
        } else if (number.length() == 1 && (h == '+' || h == '-' || h == '.')) {
            isNumber = false;
        } else if (number.length() == 2) {
        	char h2 = number.charAt(1);
    	    if ((h == '+' && h2 == '.') || (h == '-' && h2 == '.')) {
                isNumber = false;
    	    }
        }

        if (isNumber) {
            Pattern timePattern = null;
            Matcher timeMatcher = null;
            // 正则表达式，匹配如下字符串：
            // 1).正负号可有可无，若无表示是正数
            // 2).小数点可有可无，若无表示是整数
            // 3).以0开始的数字字符串
            // 4).除了1),2)以外的所有字符必须是数字
            String regexp = "[+|-]?[0-9]*.?[0-9]*";

            timePattern = Pattern.compile(regexp);
            timeMatcher = timePattern.matcher(number.trim());
            isNumber = timeMatcher.matches();
        }

        return isNumber;
    }

    /**
     * 把一个数值按指定格式输出
     * 
     * @param value
     *            数值
     * @param format
     *            指定格式
     * @return String
     */
    public static String getFieldDecimalFormatted(BigDecimal value, String format) {
        if (value == null) {
            return "";
        }

        DecimalFormat localDecimalFormat = null;
        if (format == null) {
            localDecimalFormat = new DecimalFormat();
        } else {
            localDecimalFormat = new DecimalFormat(format);
        }
        return localDecimalFormat.format(value);
    }
    

    /**
     * 转换
     * 
     * @param str
     *            SQL文
     * @param escapeCh
     *            字符
     * @return String
     */
    public static String transferSql(String str, char escapeCh) {
        if(str != null && !"".equals(str)) {
            StringBuffer sbf = new StringBuffer(str.length());
            for(int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                if(ch == '%' || ch == '_' || ch == escapeCh) {
                    sbf.append(escapeCh).append(ch);
                } else {
                    sbf.append(ch);
                }
            }

            return sbf.toString();
        } else {
            return str;
        }
    }
    
	public static String replace(String src, String oldStr, String newStr) {
		int index;
		StringBuffer buffer;

		if (isEmpty(src) || isEmpty(oldStr) || (newStr == null)) {
			return src;
		}

		buffer = new StringBuffer(src);
		index = src.length();

		while ((index = src.lastIndexOf(oldStr, index)) >= 0) {
			buffer.replace(index, index + oldStr.length(), newStr);

			index = index - oldStr.length();
		}

		return buffer.toString();
	}

}
