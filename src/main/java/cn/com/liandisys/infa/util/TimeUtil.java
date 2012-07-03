package cn.com.liandisys.infa.util;
/**
 * 时间工具类
 * @author jinxl
 *
 */
public class TimeUtil {
	/**
	 * 秒数转化成时分秒形式 hh:mm:ss
	 * @param time
	 * @return
	 */
	public static String changetime(long time){
		    String s =null;		 
			int hour, minute,second,millsecond;
			hour = (int)time/3600;
			minute = (int)(time - hour*3600)/60;
			second = (int)(time - hour*3600 - minute*60);
			s = hour+":"+minute+":"+second;
		    String[] temp = String.valueOf(s).split(":");
		    String lasttime="";
		    for (int i = 0; i < temp.length; i++) {
				if(temp[i].length()==1){
					temp[i]="0"+temp[i];
				}
				lasttime += temp[i]+":";
			}
		    return lasttime.substring(0, lasttime.length()-1);	        
	}
}