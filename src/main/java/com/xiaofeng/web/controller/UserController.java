package com.xiaofeng.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaofeng.global.UtilConstants;
import com.xiaofeng.utils.Result;

/**
 * @ClassName:  UserController   
 * @Description: 用户相关
 * @author: 小峰
 * @date:   2020年7月20日 下午3:56:29
 */
@RestController
@RequestMapping("user")
public class UserController {

	/**
	 * 
	 * @Title: getSession   
	 * @Description: 初始化用户   
	 * @param: @param request
	 * @param: @return      
	 * @return: Result      
	 * @throws
	 */
	@RequestMapping(value = "/getSession", method = RequestMethod.POST)
	public Result<String> getSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = session.getId();
		return new Result<String>(id);
	}
	
	/**
	 * 
	 * @Title: updateInfo   
	 * @Description: 更新当前用户名
	 * @param: @param request
	 * @param: @param userName
	 * @param: @return      
	 * @return: Result<String>      
	 * @throws
	 */
	@RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
	public Result<String> updateInfo(HttpServletRequest request,String userName) {
		HttpSession session = request.getSession();
		session.setAttribute(UtilConstants.SESSION_USER_NAME, userName);
		return new Result<String>();
	}
	
}
