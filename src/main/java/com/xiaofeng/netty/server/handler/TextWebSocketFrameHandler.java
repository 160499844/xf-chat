package com.xiaofeng.netty.server.handler;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.xiaofeng.global.GroupContext;
import com.xiaofeng.global.UserInfoContext;
import com.xiaofeng.utils.UserToken;

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
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	// 读到客户端的内容并且向客户端去写内容
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		//获取前端传递信息
		String userId = ctx.channel().id().asLongText();
		String message = msg.text();
		log.info(userId + "：" + message);
		//更新用户信息
		UserToken user = UserInfoContext.getUser(userId);
		message = message.replace("null", "\"\"");
		JSONObject json = new JSONObject(message);

		String content = json.getString("msg");
		String groupId =StringUtils.isEmpty(json.getString("groupId"))?user.getGroupId():json.getString("groupId");
		String name = StringUtils.isEmpty(json.getString("name"))?user.getUserName():json.getString("name");
		String type = json.getString("type");
		
		
		user.setGroupId(groupId);
		user.setUserName(name);
		
		Date nowDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		content  = String.format("%s(%s):%s", user.getUserName(), sdf.format(nowDate), content);
		/**
		 * writeAndFlush接收的参数类型是Object类型，但是一般我们都是要传入管道中传输数据的类型，比如我们当前的demo
		 * 传输的就是TextWebSocketFrame类型的数据
		 */
		// 遍历组内全部用户，发送消息
		Set<Map<String, ChannelHandlerContext>> groupList = GroupContext.USER_GROUP.get(user.getGroupId());
		if(groupList==null) {
			//没有找到对应的组
			//创建新小组
			groupList = new HashSet<Map<String, ChannelHandlerContext>>();
			Map<String, ChannelHandlerContext> userGroup = new ConcurrentHashMap<>();
			userGroup.put(user.getUserId(), ctx);
			groupList.add(userGroup);
			GroupContext.USER_GROUP.put(groupId, groupList);
		}else {
			//小组已经存在，如果小组中没有当前成员，加入小组
			//临时保存新的组成员
			Set<Map<String, ChannelHandlerContext>> tempList = new HashSet<Map<String,ChannelHandlerContext>>();
			for (Map<String, ChannelHandlerContext> allUser : groupList) {
				if(!allUser.containsKey(userId)) {
					Map<String, ChannelHandlerContext> userGroup = new ConcurrentHashMap<>();
					userGroup.put(userId, ctx);
					tempList.add(userGroup);
				}
			}
			//更新组成员
			if(tempList.size()>0) {
				Set<Map<String, ChannelHandlerContext>> list = GroupContext.USER_GROUP.get(groupId);
				list.addAll(tempList);
			}
		}
		//广播给组成员
		groupList = GroupContext.USER_GROUP.get(user.getGroupId());
		for (Map<String, ChannelHandlerContext> map : groupList) {
			for(String key:map.keySet()) {
				ChannelHandlerContext channelHandlerContext = map.get(key);
				channelHandlerContext.channel().writeAndFlush(
						new TextWebSocketFrame(content));
			}
		}
	}

	// 每个channel都有一个唯一的id值
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		SocketAddress localAddress = ctx.channel().localAddress();
		String userId = ctx.channel().id().asLongText();
		// 打印出channel唯一值，asLongText方法是channel的id的全名
		log.info(String.format("访问用户:%s,ip:%s", userId, localAddress.toString()));
		// UserInfoContext.USER_SESSION.put(userId, ctx);

		UserToken user = UserInfoContext.getUser(userId);
		if (user == null) {
			// 首次访问
			user = new UserToken();
			user.setIp(localAddress.toString());
			user.setUserId(userId);
			Random random = new Random();
			user.setUserName("访客" + random.nextInt(100));
			UserInfoContext.addUser(userId, user);
		}
		// 获取组id
		String groupId = user.getGroupId();
		Set<Map<String, ChannelHandlerContext>> groupList = GroupContext.USER_GROUP.get(groupId);
		if (groupList == null) {
			groupList = new HashSet<Map<String, ChannelHandlerContext>>();

		}
		Map<String, ChannelHandlerContext> map = new ConcurrentHashMap<String, ChannelHandlerContext>();
		map.put(userId, ctx);
		// 加入组中
		groupList.add(map);
		GroupContext.USER_GROUP.put(groupId, groupList);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		String userId = ctx.channel().id().asLongText();
		// UserInfoContext.USER_SESSION.remove(userId);
		UserToken user = UserInfoContext.getUser(userId);
		UserInfoContext.remove(userId);
		Set<Map<String, ChannelHandlerContext>> list = GroupContext.USER_GROUP.get(user.getGroupId());
		for (Map<String, ChannelHandlerContext> map : list) {
			if (map.containsKey(userId)) {
				map.remove(userId);
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