package com.xiaofeng.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.xiaofeng.utils.Group;
import com.xiaofeng.utils.User;
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
	// public static Map<String, Set<Map<String, ChannelHandlerContext>>> USER_GROUP
	// = new ConcurrentHashMap<>();
	public static Group<String, User<Map<String, ChannelHandlerContext>>> USER_GROUP = new Group<>();
	public static Group<String,Integer> GROUP_COUNTS =  new Group<>();//群成员数量

	public static List<UserToken> getGroupUsers(String groupId) {
		List<UserToken> list = new ArrayList<UserToken>();
		Set<Map<String, ChannelHandlerContext>> set = USER_GROUP.get(groupId);
		if (set == null) {
			return null;
		}
		for (Map<String, ChannelHandlerContext> map : set) {
			for (String userId : map.keySet()) {
				// 获取小组全部成员
				UserToken user = UserInfoContext.getUser(userId);
				list.add(user);
				break;
			}
		}
		return list;
	}

	/**
	 * 
	 * @Title: create @Description: 创建群组 @param: @param groupId @return:
	 * void @throws
	 */
	public static void create(String groupId) {

	}

	/**
	 * 
	 * @Title: getGroup @Description: 获取小组 @param: @param
	 * groupId @param: @return @return:
	 * Set<Map<String,ChannelHandlerContext>> @throws
	 */
	public static User<Map<String, ChannelHandlerContext>> getGroup(String groupId) {
		User<Map<String, ChannelHandlerContext>> groupList = USER_GROUP.get(groupId);
		if (groupList == null) {
			// 创建新小组
			groupList = new User<Map<String, ChannelHandlerContext>>();
			Map<String, ChannelHandlerContext> userGroup = new ConcurrentHashMap<>();
			groupList.add(userGroup);
			USER_GROUP.put(groupId, groupList);
		}
		return groupList;
	}
	/**
	 * 
	 * @Title: groupAddUser   
	 * @Description: 小组添加成员  
	 * @param: @param groupId
	 * @param: @param userId
	 * @param: @param ctx      
	 * @return: void      
	 * @throws
	 */
	public synchronized static void groupAddUser(String groupId, String userId, ChannelHandlerContext ctx) {
		User<Map<String, ChannelHandlerContext>> groupList = GroupContext.getGroup(groupId);
		Set<Map<String, ChannelHandlerContext>> tempList = new HashSet<Map<String, ChannelHandlerContext>>();
		for (Map<String, ChannelHandlerContext> allUser : groupList) {
			if (!allUser.containsKey(userId)) {
				Map<String, ChannelHandlerContext> userGroup = new ConcurrentHashMap<>();
				userGroup.put(userId, ctx);
				tempList.add(userGroup);
			}
		}
		// 更新组成员
		if (tempList.size() > 0) {
			groupList.addAll(tempList);
		}
	}
	/**
	 * 
	 * @Title: groupReduce   
	 * @Description: 减少组成员数量
	 * @param: @param groupId      
	 * @return: void      
	 * @throws
	 */
	public synchronized static Integer groupReduceCount(String groupId) {
		Integer groupCount = GroupContext.GROUP_COUNTS.get(groupId);
		if(groupCount!=null) {
			groupCount = groupCount - 1;
			GroupContext.GROUP_COUNTS.put(groupId, groupCount);
		}
		return groupCount;
	}
	/**
	 * 
	 * @Title: groupAddCount   
	 * @Description: 增加组成员数量 
	 * @param: @param groupId
	 * @param: @return      
	 * @return: Integer      
	 * @throws
	 */
	public synchronized static Integer groupAddCount(String groupId) {
		Integer groupCount = GroupContext.GROUP_COUNTS.get(groupId);
		if(groupCount!=null) {
			groupCount = groupCount + 1;
		}else {
			groupCount = 1;
		}
		GroupContext.GROUP_COUNTS.put(groupId, groupCount);
		return groupCount;
	}
	/**
	 * 
	 * @Title: getGroupCount   
	 * @Description: 获取小组成员数量
	 * @param: @param groupId
	 * @param: @return      
	 * @return: Integer      
	 * @throws
	 */
	public static Integer getGroupCount(String groupId) {
		return GroupContext.GROUP_COUNTS.get(groupId);
	}
}
