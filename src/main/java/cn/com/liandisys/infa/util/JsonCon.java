package cn.com.liandisys.infa.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;

public class JsonCon {

	public static String object2json(Object obj)
	  {
	    StringBuilder json = new StringBuilder();
	    if (obj == null)
	      json.append("\"\"");
	    else if (((obj instanceof String)) || 
	      ((obj instanceof Integer)) || 
	      ((obj instanceof Float)) || 
	      ((obj instanceof Short)) || 
	      ((obj instanceof Double)) || 
	      ((obj instanceof Long)) || 
	      ((obj instanceof BigDecimal)) || 
	      ((obj instanceof BigInteger)) || 
	      ((obj instanceof Byte)))
	      json.append("\"").append(string2json(obj.toString())).append("\"");
	    else if ((obj instanceof Boolean))
	      json.append(string2json(obj.toString()));
	    else if ((obj instanceof Object[]))
	      json.append(array2json((Object[])obj));
	    else if ((obj instanceof List))
	      json.append(list2json((List)obj));
	    else if ((obj instanceof Map))
	      json.append(map2json((Map)obj));
	    else if ((obj instanceof Set))
	      json.append(set2json((Set)obj));
	    else {
	      json.append(bean2json(obj));
	    }
	    return json.toString();
	  }

	  public static String bean2json(Object bean)
	  {
	    StringBuilder json = new StringBuilder();
	    json.append("{");
	    PropertyDescriptor[] props = (PropertyDescriptor[])null;
	    try {
	      props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors(); } catch (IntrospectionException localIntrospectionException) {
	    }
	    if (props != null) {
	      for (int i = 0; i < props.length; i++)
	        try {
	          String name = object2json(props[i].getName());
	          String value = object2json(props[i].getReadMethod().invoke(bean, new Object[0]));
	          name = name.replace("\"", "");

	          json.append("\"");
	          json.append(name);
	          json.append("\"");
	          json.append(":");
	          json.append(value);
	          json.append(",");
	        } catch (Exception localException) {
	        }
	      json.setCharAt(json.length() - 1, '}');
	    } else {
	      json.append("}");
	    }
	    return json.toString();
	  }

	  public static String list2json(List<?> list)
	  {
	    StringBuilder json = new StringBuilder();
	    json.append("[");
	    if ((list != null) && (list.size() > 0)) {
	      for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
	        json.append(object2json(obj));
	        json.append(",");
	      }
	      json.setCharAt(json.length() - 1, ']');
	    } else {
	      json.append("]");
	    }
	    return json.toString();
	  }

	  public static String array2json(Object[] array)
	  {
	    StringBuilder json = new StringBuilder();
	    json.append("[");
	    if ((array != null) && (array.length > 0)) {
	      Object[] arrayOfObject = array; int j = array.length; for (int i = 0; i < j; i++) { Object obj = arrayOfObject[i];
	        json.append(object2json(obj));
	        json.append(",");
	      }
	      json.setCharAt(json.length() - 1, ']');
	    } else {
	      json.append("]");
	    }
	    return json.toString();
	  }

	  public static String map2json(Map<?, ?> map)
	  {
	    StringBuilder json = new StringBuilder();
	    json.append("{");
	    if ((map != null) && (map.size() > 0)) {
	      for (Iterator localIterator = map.keySet().iterator(); localIterator.hasNext(); ) { Object key = localIterator.next();
	        json.append(object2json(key));
	        json.append(":");
	        json.append(object2json(map.get(key)));
	        json.append(",");
	      }
	      json.setCharAt(json.length() - 1, '}');
	    } else {
	      json.append("}");
	    }
	    return json.toString();
	  }

	  public static String set2json(Set<?> set)
	  {
	    StringBuilder json = new StringBuilder();
	    json.append("[");
	    if ((set != null) && (set.size() > 0)) {
	      for (Iterator localIterator = set.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
	        json.append(object2json(obj));
	        json.append(",");
	      }
	      json.setCharAt(json.length() - 1, ']');
	    } else {
	      json.append("]");
	    }
	    return json.toString();
	  }

	  public static String string2json(String s)
	  {
	    if (s == null)
	      return "";
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < s.length(); i++) {
	      char ch = s.charAt(i);
	      switch (ch) {
	      case '"':
	        sb.append("\\\"");
	        break;
	      case '\\':
	        sb.append("\\\\");
	        break;
	      case '\b':
	        sb.append("\\b");
	        break;
	      case '\f':
	        sb.append("\\f");
	        break;
	      case '\n':
	        sb.append("\\n");
	        break;
	      case '\r':
	        sb.append("\\r");
	        break;
	      case '\t':
	        sb.append("\\t");
	        break;
	      case '/':
	        sb.append("\\/");
	        break;
	      default:
	        if ((ch >= 0) && (ch <= '\037')) {
	          String ss = Integer.toHexString(ch);
	          sb.append("\\u");
	          for (int k = 0; k < 4 - ss.length(); k++) {
	            sb.append('0');
	          }
	          sb.append(ss.toUpperCase());
	        } else {
	          sb.append(ch);
	        }
	      }
	    }
	    return sb.toString();
	  }

	  public static Object jsonStr2Object(String jsonString, Class<?> pojoCalss)
	  {
	    JSONObject jsonObject = JSONObject.fromObject(jsonString);
	    Object pojo = JSONObject.toBean(jsonObject, pojoCalss);
	    return pojo;
	  }

	  public static Map jsonStr2Map(String jsonString)
	  {
	    JSONObject jsonObject = JSONObject.fromObject(jsonString);
	    Iterator keyIter = jsonObject.keys();

	    Map valueMap = new HashMap();
	    while (keyIter.hasNext()) {
	      String key = (String)keyIter.next();
	      Object value = jsonObject.get(key);
	      valueMap.put(key, value);
	    }
	    return valueMap;
	  }

	  public static Object[] jsonStr2ObjectArray(String jsonString)
	  {
	    net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(jsonString);
	    return jsonArray.toArray();
	  }

	  public static List<?> jsonStr2List(String jsonString, Class<?> pojoClass)
	  {
	    net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(jsonString);

	    List list = new ArrayList();
	    for (int i = 0; i < jsonArray.size(); i++)
	    {
	      JSONObject jsonObject = jsonArray.getJSONObject(i);
	      Object pojoValue = JSONObject.toBean(jsonObject, pojoClass);
	      list.add(pojoValue);
	    }
	    return list;
	  }

	  public static String[] getStringArray4Json(String jsonString)
	  {
	    net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(jsonString);
	    String[] stringArray = new String[jsonArray.size()];
	    for (int i = 0; i < jsonArray.size(); i++) {
	      stringArray[i] = jsonArray.getString(i);
	    }
	    return stringArray;
	  }

	  public static Long[] getLongArray4Json(String jsonString)
	  {
	    net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(jsonString);
	    Long[] longArray = new Long[jsonArray.size()];
	    for (int i = 0; i < jsonArray.size(); i++) {
	      longArray[i] = Long.valueOf(jsonArray.getLong(i));
	    }
	    return longArray;
	  }

	  public static Integer[] getIntegerArray4Json(String jsonString)
	  {
	    net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(jsonString);
	    Integer[] integerArray = new Integer[jsonArray.size()];
	    for (int i = 0; i < jsonArray.size(); i++) {
	      integerArray[i] = Integer.valueOf(jsonArray.getInt(i));
	    }
	    return integerArray;
	  }

	  public static String getComCodeJSONArray(int[] codes, String[] names, String[][] addCodes)
	  {
	    if ((codes.length == 0) || (codes.length != names.length)) {
	      throw new IllegalArgumentException("数组个数必须相等且大于1个");
	    }
	    StringBuffer sbf = new StringBuffer(codes.length * 20 + 10);
	    sbf.append("[");
	    if ((addCodes != null) && (addCodes.length > 0)) {
	      for (int i = 0; i < addCodes.length; i++) {
	        if (i > 0) sbf.append(",");
	        sbf.append("['").append(addCodes[i][0]).append("','").append(addCodes[i][1]).append("']");
	      }
	    }
	    for (int i = 0; i < codes.length; i++) {
	      if ((i > 0) || ((addCodes != null) && (addCodes.length > 0))) sbf.append(",");
	      sbf.append("['").append(codes[i]).append("','").append(names[i]).append("']");
	    }
	    sbf.append("]");
	    return sbf.toString();
	  }

	  public static String getComCodeJSONArray(long[] codes, String[] names, String[][] addCodes)
	  {
	    if ((codes.length == 0) || (codes.length != names.length)) {
	      throw new IllegalArgumentException("数组个数必须相等且大于1个");
	    }
	    StringBuffer sbf = new StringBuffer(codes.length * 20 + 10);
	    sbf.append("[");
	    if ((addCodes != null) && (addCodes.length > 0)) {
	      for (int i = 0; i < addCodes.length; i++) {
	        if (i > 0) sbf.append(",");
	        sbf.append("['").append(addCodes[i][0]).append("','").append(addCodes[i][1]).append("']");
	      }
	    }
	    for (int i = 0; i < codes.length; i++) {
	      if ((i > 0) || ((addCodes != null) && (addCodes.length > 0))) sbf.append(",");
	      sbf.append("['").append(codes[i]).append("','").append(names[i]).append("']");
	    }
	    sbf.append("]");
	    return sbf.toString();
	  }

	  public static String dealString4JSON(String ors)
	  {
	    ors = ors == null ? "" : ors;
	    StringBuffer buffer = new StringBuffer(ors);

	    int i = 0;
	    while (i < buffer.length()) {
	      if ((buffer.charAt(i) == '\'') || (buffer.charAt(i) == '\\')) {
	        buffer.insert(i, '\\');
	        i += 2;
	      } else {
	        i++;
	      }
	    }
	    return buffer.toString().replaceAll("\r", "").replaceAll("\n", "");
	  }
}
