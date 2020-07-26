package com.xiaofeng.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaofeng.entity.Group;
import com.xiaofeng.entity.UserEntity;
import com.xiaofeng.web.repository.GroupRepository;
import com.xiaofeng.web.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName:  UserService   
 * @Description:成员操作 
 * @author: 小峰
 * @date:   2020年7月23日 上午8:59:45
 */
@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * 
	 * @Title: saveUser   
	 * @Description: 创建用户
	 * @param: @param user      
	 * @return: void      
	 * @throws
	 */
	public void saveUser(UserEntity user) {
		userRepository.saveUser(user);
	}
	/**
	 * 
	 * @Title: getUserByUserId   
	 * @Description: 根据sessionid查询用户
	 * @param: @param userId
	 * @param: @return      
	 * @return: UserEntity      
	 * @throws
	 */
	public UserEntity getUserByUserId(String userId) {
		UserEntity findByGroupId = userRepository.findByUserId(userId);
		return findByGroupId;
	}
	/**
	 * 
	 * @Title: getUserByUserIdAndDel   
	 * @Description:  根据sessionid查询并删除用户
	 * @param: @param userId
	 * @param: @return      
	 * @return: UserEntity      
	 * @throws
	 */
	public UserEntity getUserByUserIdAndDel(String userId) {
		List<UserEntity> deleteUser = userRepository.deleteUser(userId);
		UserEntity userEntity = null;
		if(deleteUser.size()>0) {
			userEntity = deleteUser.get(0);
		}
		return userEntity;
	}
	/**
	 * 
	 * @Title: editCol   
	 * @Description: 更新用户信息
	 * @param: @param userId
	 * @param: @param value      
	 * @return: void      
	 * @throws
	 */
	public void updateSessionId(UserEntity user) {
		userRepository.editCol(user.getUserId(),"sessionId",user.getSessionId());
	}

	/**
	 * 更新字段
	 * @param userId
	 * @param key
	 * @param value
	 */
	public void UpdateKeyValues(String userId,String key,String value){
		userRepository.editCol(userId,key,value);
	}

	/**
	 * 更新用户对象
	 * @param user
	 */
	public void updateUser(UserEntity user) {
		userRepository.editCol(user.getUserId(),"sessionId",user.getSessionId());
		Map<String ,Object> map = new HashMap<>();
		map.put("groupId",user.getGroupId());
		map.put("userName",user.getUserName());
		userRepository.editCols(user.getUserId(),map);
	}
}
