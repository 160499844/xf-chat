package com.xiaofeng.netty.server;

import java.util.Map;
import java.util.Set;

import com.xiaofeng.global.GroupContext;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 
 * @ClassName:  DynMessage   
 * @Description: 群发推送消息
 * @author: 小峰
 * @date:   2020年7月16日 下午3:28:12
 */
public class DynMessage {
	//默认组
	public static final String DEFAULT_GROUP = "default";
	/**
	 * 
	 * @Title: broadcast   
	 * @Description:  广播消息  
	 * @param: @param groupId 组id
	 * @param: @param msg      消息
	 * @return: void      
	 * @throws
	 */
	public static void broadcast(String groupId,String msg) {
		Set<Map<String, ChannelHandlerContext>> groups = GroupContext.USER_GROUP.get(groupId);
		for (Map<String, ChannelHandlerContext> map : groups) {
			for(String key:map.keySet()) {
				ChannelHandlerContext channelHandlerContext = map.get(key);
				channelHandlerContext.channel().writeAndFlush(
						new TextWebSocketFrame(msg));
			}
		}
	}
	/**
	 * 
	 * @param groupId
	 * @param userId
	 * @param msg
	 */
	public static void broadcast(String groupId,String excludeUserId,String msg) {
		Set<Map<String, ChannelHandlerContext>> groups = GroupContext.USER_GROUP.get(groupId);
		for (Map<String, ChannelHandlerContext> map : groups) {
			for(String userId:map.keySet()) {
				if(userId.equals(excludeUserId)) {
					continue;
				}
				ChannelHandlerContext channelHandlerContext = map.get(userId);
				channelHandlerContext.channel().writeAndFlush(
						new TextWebSocketFrame(msg));
			}
		}
	}
	/**
	 * 
	 * @Title: broadcast   
	 * @Description: 广播消息  
	 * @param: @param msg      消息
	 * @return: void      
	 * @throws
	 */
	public static void broadcast(String msg) {
		broadcast(DEFAULT_GROUP,msg);
	}
	
}
