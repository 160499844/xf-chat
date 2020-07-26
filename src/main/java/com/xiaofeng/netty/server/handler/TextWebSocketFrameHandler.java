package com.xiaofeng.netty.server.handler;

import java.net.SocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.xiaofeng.entity.GroupToken;
import com.xiaofeng.entity.UserEntity;
import com.xiaofeng.global.GroupContext;
import com.xiaofeng.global.UserInfoContext;
import com.xiaofeng.netty.server.DynMessage;
import com.xiaofeng.utils.DateUtils;
import com.xiaofeng.utils.EncryptMessage;
import com.xiaofeng.utils.MessageVo;
import com.xiaofeng.utils.SpringBeanUtil;
import com.xiaofeng.utils.user.Users;
import com.xiaofeng.utils.user.UserToken;
import com.xiaofeng.web.service.GroupService;
import com.xiaofeng.web.service.PushService;
import com.xiaofeng.web.service.UserService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty服务端处理器 处理websocket连接
 * 
 * @author xiaofeng
 *
 */

@Slf4j
@Configuration
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	//消息交互
	//接收客户端发送的消息
	//读到客户端的内容并且向客户端去写内容
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		long startTime = System.currentTimeMillis();
		// 获取前端传递信息
		String userId = ctx.channel().id().asLongText();
		String message = msg.text();
		boolean isBroadCase = true;
		// 更新用户信息
		//UserToken user = UserInfoContext.getUser(userId);
		UserService userService = SpringBeanUtil.getBean(UserService.class);
		UserEntity user = userService.getUserByUserId(userId);
		MessageVo messageVo ;
		try {
			messageVo = com.xiaofeng.utils.string.StringUtils.jsonToMessageVo(message);
		} catch (Exception e) {
			messageVo = new MessageVo();
			isBroadCase = false;
			log.error("消息解密失败{}",e);
			e.printStackTrace();
		}
		
		//获取小组消息解密key
		//GroupToken groupToken = GroupContext.GROUP_KEYS.get(messageVo.getGroupId());
		//messageVo.setMsg(EncryptMessage.decrypt(messageVo.getMsg(),groupToken.getAesKey()));
		

		// 广播给组成员
		if(isBroadCase) {
			user.setGroupId(StringUtils.isEmpty(messageVo.getGroupId()) ? user.getGroupId() : messageVo.getGroupId());
			user.setUserName(StringUtils.isEmpty(messageVo.getName()) ? user.getUserName() : messageVo.getName().trim());
			
			CustomHandle handle = new CustomHandleImpl();
			handle.messageHandle(messageVo, user,ctx);
			String jsonString = com.xiaofeng.utils.string.StringUtils.toJson(messageVo);
			DynMessage.broadcast(user.getGroupId(), jsonString);
		}
		long endTime = System.currentTimeMillis();    //获取结束时间
		log.info("用户发送消息消耗时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
	}

	// 用户加入
	// 每个channel都有一个唯一的id值
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		SocketAddress localAddress = ctx.channel().localAddress();
		
		String ip = localAddress.toString().replace("/", "");
		String userId = ctx.channel().id().asLongText();
		// 打印出channel唯一值，asLongText方法是channel的id的全名
		//log.info(String.format("连接用户:%s,ip:%s", userId, ip));
		
		UserService userService = SpringBeanUtil.getBean(UserService.class);
		UserEntity user = new UserEntity();
		user.setIp(ip);
		user.setUserId(userId);
		userService.saveUser(user);
		
		// UserInfoContext.USER_SESSION.put(userId, ctx);

	/*	UserToken user = UserInfoContext.getUser(userId);
		user.setIp(ip);
		user.setUserId(userId);
		UserInfoContext.addUser(userId, user);*/
		
	}

	/**
	 * 用户退出
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		String userId = ctx.channel().id().asLongText();
		//UserToken user = UserInfoContext.getUser(userId);
		UserService userService = SpringBeanUtil.getBean(UserService.class);
		GroupService groupService = SpringBeanUtil.getBean(GroupService.class);
		PushService pushService = SpringBeanUtil.getBean(PushService.class);
		UserEntity user = userService.getUserByUserId(userId);
		//小组人数-1
		//GroupContext.groupReduceCount(user.getGroupId());
		groupService.groupReduceCount(user.getGroupId());
		
		UserInfoContext.remove(userId);//删除用户信息
		//删除登录信息
//		Set<Map<String, ChannelHandlerContext>> list = GroupContext.USER_GROUP.get(user.getGroupId());
//		Iterator<Map<String, ChannelHandlerContext>> iterator = list.iterator();
//		while (iterator.hasNext()) {
//			Map<String, ChannelHandlerContext> next = iterator.next();
//			if (next.containsKey(userId)) {
//				iterator.remove();
//			}
//		}
		log.info("离开用户：" + userId);
		pushService.pushMessage(user.getGroupId(), String.format(user.getUserName() + "离开了", user.getUserName()));

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		log.info("异常发生");
		ctx.close();
	}
}