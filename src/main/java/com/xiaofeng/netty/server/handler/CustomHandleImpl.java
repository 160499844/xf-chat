package com.xiaofeng.netty.server.handler;

import org.apache.commons.lang3.StringUtils;

import com.xiaofeng.global.GroupContext;
import com.xiaofeng.global.UserInfoContext;
import com.xiaofeng.utils.DateUtils;
import com.xiaofeng.utils.MessageVo;
import com.xiaofeng.utils.user.UserToken;
import com.xiaofeng.web.service.PushService;

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
	public Object messageHandle(MessageVo messageVo,UserToken user, ChannelHandlerContext ctx) {
		
		String content = messageVo.getMsg();
		messageVo.setName(user.getUserName());
		content = String.format("%s(%s):%s", user.getUserName(), DateUtils.getNowDateToString(), messageVo.getMsg());
		log.info(content);
		
		//保存用户session映射关系key netty sesion value springboot session
//		if(StringUtils.isEmpty(user.getSessionId()) && StringUtils.isNotEmpty(messageVo.getSessionId())) {
//			//第一次加入群聊
//			//user.setSessionId(messageVo.getSessionId());
//			UserInfoContext.updateSessionId(user.getUserId(),messageVo.getSessionId());
//			//加入小组
//			GroupContext.groupAddUser(user.getGroupId(),user.getUserId(),ctx);
//			UserInfoContext.sessionMap.put(user.getUserId(),messageVo.getSessionId());
//			//小组人数+1
//			GroupContext.groupAddCount(user.getGroupId());
//			
//			//发送系统消息
//			PushService pushService = new PushService();
//			pushService.pushMessage(user.getGroupId(), String.format("欢迎%s加入群组", user.getUserName()));
//		}
		
		firstJoinGroup(user.getSessionId(), messageVo.getSessionId(), user, ctx);
		/**
		 * writeAndFlush接收的参数类型是Object类型，但是一般我们都是要传入管道中传输数据的类型，比如我们当前的demo
		 * 传输的就是TextWebSocketFrame类型的数据
		 */
		// 组装返回对象
		messageVo.setContent(content);
		Integer groupCount = GroupContext.getGroupCount(user.getGroupId());
		messageVo.put("group_count", groupCount);// 当前在线人数
		return messageVo;
	}
	/**
	 * 第一次加入群聊
	 */
	private void firstJoinGroup(String sessionId,String mSessionId,UserToken user, ChannelHandlerContext ctx) {
		if(StringUtils.isEmpty(sessionId) && StringUtils.isNotEmpty(mSessionId)) {
			//第一次加入群聊
			//user.setSessionId(messageVo.getSessionId());
			UserInfoContext.updateSessionId(user.getUserId(),mSessionId);
			//加入小组
			GroupContext.groupAddUser(user.getGroupId(),user.getUserId(),ctx);
			UserInfoContext.sessionMap.put(user.getUserId(),mSessionId);
			//小组人数+1
			GroupContext.groupAddCount(user.getGroupId());
			
			//发送系统消息
			PushService pushService = new PushService();
			pushService.pushMessage(user.getGroupId(), String.format("欢迎%s加入群组", user.getUserName()));
		}
	}

}
