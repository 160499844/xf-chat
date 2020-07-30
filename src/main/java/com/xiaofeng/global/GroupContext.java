package com.xiaofeng.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.xiaofeng.utils.MessageHandleVo;
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

	//保存每个用户的ChannelHandlerContext对象
	public static Groups<String, Users<Map<String, ChannelHandlerContext>>> USER_GROUP = new Groups<>();
	//临时存放用户和session
	public static ArrayBlockingQueue<MessageHandleVo> userSession = new ArrayBlockingQueue<MessageHandleVo>(1000,true);

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
	public static void groupAddUser(String groupId, String userId, ChannelHandlerContext ctx) {
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
			synchronized(groupList){
				groupList.addAll(tempList);
			}
		}
	}
}
