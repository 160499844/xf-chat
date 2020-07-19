package com.xiaofeng.netty.server.handler;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.xiaofeng.global.GroupContext;
import com.xiaofeng.global.UserInfoContext;
import com.xiaofeng.netty.server.DynMessage;
import com.xiaofeng.utils.DateUtils;
import com.xiaofeng.utils.EncryptMessage;
import com.xiaofeng.utils.MessageVo;
import com.xiaofeng.utils.Result;
import com.xiaofeng.utils.User;
import com.xiaofeng.utils.UserToken;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty服务端处理器 处理websocket连接
 * https://fangjian0423.github.io/2016/08/29/netty-in-action-note2/
 * 
 * @author xiaofeng
 *
 */
@Slf4j
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	// 读到客户端的内容并且向客户端去写内容
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		// 获取前端传递信息
		String userId = ctx.channel().id().asLongText();
		String message = msg.text();
		// 更新用户信息
		UserToken user = UserInfoContext.getUser(userId);
		MessageVo messageVo = com.xiaofeng.utils.StringUtils.toJsonDecode(message);
		String content = messageVo.getMsg();
		user.setGroupId(StringUtils.isEmpty(messageVo.getGroupId()) ? user.getGroupId() : messageVo.getGroupId());
		user.setUserName(StringUtils.isEmpty(messageVo.getName()) ? user.getUserName() : messageVo.getName().trim());
		
		messageVo.setName(user.getUserName());
		//content = String.format("%s(%s):%s", user.getUserName(), DateUtils.getNowDateToString(), messageVo.getMsg());
		log.info(content);
		/**
		 * writeAndFlush接收的参数类型是Object类型，但是一般我们都是要传入管道中传输数据的类型，比如我们当前的demo
		 * 传输的就是TextWebSocketFrame类型的数据
		 */
		// 遍历组内全部用户，发送消息
		 //User<Map<String, ChannelHandlerContext>> groupList = GroupContext.getGroup(user.getGroupId());
		 GroupContext.groupAddUser(user.getGroupId(),user.getUserId(),ctx);
		
		// 获取小组成员
		 //User<Map<String, ChannelHandlerContext>> groupList = GroupContext.USER_GROUP.get(user.getGroupId());
		// 组装返回对象
		messageVo.setContent(content);
		List<UserToken> currentUsers = GroupContext.getGroupUsers(user.getGroupId());
		messageVo.put("group_count", currentUsers.size());// 当前在线人数
		messageVo.put("gourp_users", currentUsers);// 当前在线成员

		// 广播给组成员
		String jsonString = com.xiaofeng.utils.StringUtils.toJsonEncrypt(messageVo);
		DynMessage.broadcast(user.getGroupId(), jsonString);
		
	}

	// 用户加入
	// 每个channel都有一个唯一的id值
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		SocketAddress localAddress = ctx.channel().localAddress();
		String ip = localAddress.toString().replace("/", "");
		String userId = ctx.channel().id().asLongText();
		// 打印出channel唯一值，asLongText方法是channel的id的全名
		log.info(String.format("连接用户:%s,ip:%s", userId, ip));
		// UserInfoContext.USER_SESSION.put(userId, ctx);

		UserToken user = UserInfoContext.getUser(userId);
		user.setIp(ip);
		user.setUserId(userId);
		Random random = new Random();
		user.setUserName("访客" + random.nextInt(1000));
		UserInfoContext.addUser(userId, user);
		// 获取组id
		String groupId = user.getGroupId();
		User<Map<String, ChannelHandlerContext>> groupList = GroupContext.USER_GROUP.get(groupId);
		if (groupList == null) {
			groupList = new User<Map<String, ChannelHandlerContext>>();

		}
		Map<String, ChannelHandlerContext> map = new ConcurrentHashMap<String, ChannelHandlerContext>();
		map.put(userId, ctx);
		// 加入组中
		groupList.add(map);
		GroupContext.USER_GROUP.put(groupId, groupList);
	}

	/**
	 * 用户退出
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		String userId = ctx.channel().id().asLongText();
		UserToken user = UserInfoContext.getUser(userId);
		UserInfoContext.remove(userId);//删除用户信息
		//删除登录信息
		Set<Map<String, ChannelHandlerContext>> list = GroupContext.USER_GROUP.get(user.getGroupId());
		Iterator<Map<String, ChannelHandlerContext>> iterator = list.iterator();
		while (iterator.hasNext()) {
			Map<String, ChannelHandlerContext> next = iterator.next();
			if (next.containsKey(userId)) {
				iterator.remove();
			}
		}
		log.info("离开用户：" + userId);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		log.info("异常发生");
		ctx.close();
	}
}