package com.xiaofeng.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.xiaofeng.utils.exception.BaseException;
import com.xiaofeng.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private UserRepository userRepository;

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
	 * @Description: 更新当前用户名,后期增加重复校验
	 * @param: @param request
	 * @param: @param userName
	 * @param: @return      
	 * @return: Result<String>      
	 * @throws
	 */
	@RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
	public Result<String> updateInfo(HttpServletRequest request,String userName) {
		HttpSession session = request.getSession();
		//判断是否有重复
		String groupId = (String)session.getAttribute(UtilConstants.SESSION_GROUP_ID);
		int count = userRepository.findByUserName(groupId, userName);
		if(count==0){
			session.setAttribute(UtilConstants.SESSION_USER_NAME, userName);
		}else{
			throw new BaseException("名称已经被占用!");
		}
		return new Result<String>();
	}
	
}
