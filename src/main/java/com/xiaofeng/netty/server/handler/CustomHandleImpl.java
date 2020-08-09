package com.xiaofeng.netty.server.handler;

import com.xiaofeng.global.UtilConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.xiaofeng.entity.UserEntity;
import com.xiaofeng.global.GroupContext;
import com.xiaofeng.global.UserInfoContext;
import com.xiaofeng.utils.DateUtils;
import com.xiaofeng.utils.MessageVo;
import com.xiaofeng.utils.SpringBeanUtil;
import com.xiaofeng.utils.user.UserToken;
import com.xiaofeng.web.service.GroupService;
import com.xiaofeng.web.service.PushService;
import com.xiaofeng.web.service.UserService;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理
 * @author xiaofeng
 *
 */
@Slf4j
public class CustomHandleImpl {

	/**
	 * 处理消息
	 */
	public static Object messageHandle(MessageVo messageVo,UserEntity user, ChannelHandlerContext ctx) {

		firstJoinGroup(user.getSessionId(), messageVo, user, ctx);
		return messageVo;
	}
	/**
	 * 第一次加入群聊
	 */
	private static void firstJoinGroup(String sessionId,MessageVo messageVo,UserEntity user, ChannelHandlerContext ctx) {
		String mSessionId = messageVo.getSessionId();
		if(StringUtils.isEmpty(sessionId) && StringUtils.isNotEmpty(mSessionId)) {
			//第一次加入群聊
			//user.setSessionId(messageVo.getSessionId());
			user.setSessionId(mSessionId);
			user.setUserName(messageVo.getName());
			user.setGroupId(messageVo.getGroupId());
			UserService userService = SpringBeanUtil.getBean(UserService.class);
			userService.updateUser(user);
			//加入小组
			GroupContext.groupAddUser(user.getGroupId(),user.getUserId(),ctx);
			UserInfoContext.sessionMap.put(user.getUserId(),mSessionId);
			//小组人数+1
			//GroupContext.groupAddCount(user.getGroupId());
			GroupService groupService = SpringBeanUtil.getBean(GroupService.class);
			groupService.groupAddCount(user.getGroupId());
			
			//发送系统消息
			PushService pushService = SpringBeanUtil.getBean(PushService.class);
			String name = user.getUserName().length()>5?user.getUserName().substring(0,5) + "...":user.getUserName();
			pushService.pushMessage(user.getGroupId(), String.format("欢迎%s加入群组", user.getUserName()), UtilConstants.MSG.MSG_SYSTEM_ADD);
		}
	}

}
