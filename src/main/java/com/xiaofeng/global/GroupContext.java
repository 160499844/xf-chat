package com.xiaofeng.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.xiaofeng.entity.Group;
import com.xiaofeng.entity.GroupToken;
import com.xiaofeng.utils.aes.AESUtils;
import com.xiaofeng.utils.user.Groups;
import com.xiaofeng.utils.user.Users;
import com.xiaofeng.utils.user.UserToken;

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
	public static Groups<String, Users<Map<String, ChannelHandlerContext>>> USER_GROUP = new Groups<>();
	///public static Groups<String,Integer> GROUP_COUNTS =  new Groups<>();//群成员数量
	////public static Groups<String,GroupToken> GROUP_KEYS = new Groups<>();//小组口令
	//public static Groups<String,Group> GROUPS = new Groups<>();//小组管理

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
	 * @Title: create
	 * @Description: 创建群组 
	 * @param: @param groupId @return:
	 * void @throws
	 */
	public static void create(String groupId) {

	}

	/**
	 * 
	 * @Title: getGroup 
	 * @Description: 获取小组 
	 * @param: @param
	 * groupId @param: 
	 * @return @return:
	 * Set<Map<String,ChannelHandlerContext>> @throws
	 */
	public static Users<Map<String, ChannelHandlerContext>> getGroup(String groupId) {
		Users<Map<String, ChannelHandlerContext>> groupList = USER_GROUP.get(groupId);
		if (groupList == null) {
			// 创建新小组
			groupList = new Users<Map<String, ChannelHandlerContext>>();
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
		Users<Map<String, ChannelHandlerContext>> groupList = GroupContext.getGroup(groupId);
		Set<Map<String, ChannelHandlerContext>> tempList = new HashSet<Map<String, ChannelHandlerContext>>();
		for (Map<String, ChannelHandlerContext> allUser : groupList) {
			if (!allUser.containsKey(userId)) {
				//创建临时小组
				Map<String, ChannelHandlerContext> userGroup = new ConcurrentHashMap<>();
				userGroup.put(userId, ctx);
				tempList.add(userGroup);
			}
		}
		// 更新组成员
		if (tempList.size() > 0) {
			//将临时小组加入正式小组中
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
	 *//*
	public synchronized static Integer groupReduceCount(String groupId) {
		//Integer groupCount = GroupContext.GROUP_COUNTS.get(groupId);
		Integer groupCount = GroupContext.GROUPS.get(groupId).getCurrentCount();
		if(groupCount!=null && groupCount  > 0 ) {
			groupCount = groupCount - 1;
			//GroupContext.GROUP_COUNTS.put(groupId, groupCount);
			GroupContext.GROUPS.get(groupId).setCurrentCount(groupCount);
		}
		return groupCount;
	}
	*//**
	 * 
	 * @Title: groupAddCount   
	 * @Description: 增加组成员数量 
	 * @param: @param groupId
	 * @param: @return      
	 * @return: Integer      
	 * @throws
	 *//*
	public synchronized static Integer groupAddCount(String groupId) {
		//Integer groupCount = GroupContext.GROUP_COUNTS.get(groupId);
		Integer groupCount = GroupContext.GROUPS.get(groupId).getCurrentCount();
		if(groupCount!=null) {
			groupCount = groupCount + 1;
		}else {
			groupCount = 1;
		}
		//GroupContext.GROUP_COUNTS.put(groupId, groupCount);
		GroupContext.GROUPS.get(groupId).setCurrentCount(groupCount);
		return groupCount;
	}*/
/*	*//**
	 * 
	 * @Title: getGroupCount   
	 * @Description: 获取小组成员数量
	 * @param: @param groupId
	 * @param: @return      
	 * @return: Integer      
	 * @throws
	 *//*
	public static Integer getGroupCount(String groupId) {
		Group group = GroupContext.GROUPS.get(groupId);
		if(group==null) {
			return 0;
		}
		Integer currentCount = group.getCurrentCount();
		return currentCount;
	}
*/	/**
	 * 
	 * @Title: getGroupKey   
	 * @Description:  获取小组密钥
	 * @param: @param groupId
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	/*
	 * public static String getGroupKey(String groupId) { String key =
	 * GroupContext.GROUP_KEYS.get(groupId).getKey(); if(StringUtils.isEmpty(key)) {
	 * key = AESUtils.generateDesKey(); GroupContext.GROUP_KEYS.put(groupId, new
	 * GroupToken(key,"","")); } return key; }
	 */
}
