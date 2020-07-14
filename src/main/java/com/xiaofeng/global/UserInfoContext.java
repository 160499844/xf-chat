package com.xiaofeng.global;
/**
 * 用户信息
 * @author xiaofeng
 *
 */

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.xiaofeng.utils.UserToken;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class UserInfoContext {
	// 存用户信息
	private static Map<String, UserToken> userMap = new ConcurrentHashMap<String, UserToken>();

	// 存储访问用户session
	//public static Map<String, ChannelHandlerContext> USER_SESSION = new ConcurrentHashMap<String, ChannelHandlerContext>();
	

	/**
	 * 添加用户信息
	 * 
	 * @param userId 用户唯一标识
	 * @param o      存储内容
	 */
	public static void addUser(String userId, UserToken o) {
		userMap.put(userId, o);
	}

	/**
	 * 移除用户信息
	 * 
	 * @param userId 用户唯一标识
	 */
	public static void remove(String userId) {
		userMap.remove(userId);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param userId 用户唯一标识
	 * @return
	 */
	public static UserToken getUser(String userId) {
		return userMap.get(userId);
	}
	
	public static UserToken delUser(String userId) {
		return userMap.remove(userId);
	}
}
