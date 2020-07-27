package com.xiaofeng.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.xiaofeng.entity.Group;
import com.xiaofeng.global.UtilConstants;
import com.xiaofeng.utils.exception.BaseException;

/**
 * 
 * @ClassName:  SessionUtils   
 * @Description:session工具类
 * @author: 小峰
 * @date:   2020年7月27日 上午9:19:33
 */
public class SessionUtils {

	/**
	 * 
	 * @Title: getGroup   
	 * @Description: 获取session中的值
	 * @param: @param request
	 * @param: @param key
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String getValue(HttpServletRequest request,String key) {
		String value;
		HttpSession session = request.getSession();
		Object valueObj = session.getAttribute(key);
		if(valueObj==null){
			throw new BaseException("当前会话已经过期");
		}else{
			value = valueObj.toString();
		}
		return value;
	}
}
