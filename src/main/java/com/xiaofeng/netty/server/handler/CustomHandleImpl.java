package com.xiaofeng.netty.server.handler;

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
public class CustomHandleImpl implements CustomHandle {

	/**
	 * 处理消息
	 */
	@Override
	public Object messageHandle(MessageVo messageVo,UserEntity user, ChannelHandlerContext ctx) {
		
		String content = messageVo.getMsg();
		messageVo.setName(user.getUserName());

		
		firstJoinGroup(user.getSessionId(), messageVo, user, ctx);
		/**
		 * writeAndFlush接收的参数类型是Object类型，但是一般我们都是要传入管道中传输数据的类型，比如我们当前的demo
		 * 传输的就是TextWebSocketFrame类型的数据
		 */
		// 组装返回对象
		//messageVo.setContent(content);
	//	Integer groupCount = GroupContext.getGroupCount(user.getGroupId());
		//GroupService groupService = SpringBeanUtil.getBean(GroupService.class);
		//Integer groupCount = groupService.getGroupCount(user.getGroupId());
		//messageVo.put("group_count", 0);// 当前在线人数
		return messageVo;
	}
	/**
	 * 第一次加入群聊
	 */
	private void firstJoinGroup(String sessionId,MessageVo messageVo,UserEntity user, ChannelHandlerContext ctx) {
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
			pushService.pushMessage(user.getGroupId(), String.format("欢迎%s加入群组", user.getUserName()),MessageVo.MSG_SYSTEM_ADD);
		}
	}

}
