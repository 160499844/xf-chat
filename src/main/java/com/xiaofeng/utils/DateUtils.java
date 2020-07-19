package com.xiaofeng.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @ClassName:  DateUtils   
 * @Description: 日期处理工具类
 * @date:   2020年7月15日 上午10:16:02
 */
public class DateUtils {

	/**
	 * 
	 * @Title: getNowDateToString   
	 * @Description: 当前时间转字符串
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String getNowDateToString() {
		Date nowDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(nowDate);
	}
	public static String getNowDateToString(String format) {
		Date nowDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(nowDate);
	}
	
}
