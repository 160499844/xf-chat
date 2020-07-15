package com.xiaofeng.global;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.xiaofeng.utils.UserToken;

import io.netty.channel.ChannelHandlerContext;

/**
 * 组成员信息保存
 * 
 * @author xiaofeng
 *
 */
public class GroupContext {

	// 存储组成员
	public static Map<String, Set<Map<String, ChannelHandlerContext>>> USER_GROUP = new ConcurrentHashMap<>();
	
	public static List<UserToken> getGroupUsers(String groupId) {
		List<UserToken> list = new ArrayList<UserToken>();
		Set<Map<String, ChannelHandlerContext>> set = USER_GROUP.get(groupId);
		for (Map<String, ChannelHandlerContext> map : set) {
			for(String userId:map.keySet()) {
				//获取小组全部成员
				UserToken user = UserInfoContext.getUser(userId);
				list.add(user);
				break;
			}
		}
		return list;
	}
}
