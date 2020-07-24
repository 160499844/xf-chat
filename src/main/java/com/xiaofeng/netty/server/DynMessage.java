package com.xiaofeng.netty.server;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.xiaofeng.global.GroupContext;
import com.xiaofeng.utils.user.Groups;
import com.xiaofeng.utils.user.Users;
import com.xiaofeng.web.controller.GroupController;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName:  DynMessage   
 * @Description: 群发推送消息
 * @author: 小峰
 * @date:   2020年7月16日 下午3:28:12
 */
@Slf4j
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
		log.info("开始广播");
		Set<Map<String, ChannelHandlerContext>> groups = null;
		if(StringUtils.isEmpty(groupId)) {
			//暂不支持
			//TODO
		}else {
			groups = GroupContext.USER_GROUP.get(groupId);
		}
		//这种方式效率较高
		Iterator<Map<String, ChannelHandlerContext>> iterator = groups.iterator();
		while(iterator.hasNext()){
			Map<String, ChannelHandlerContext> group = iterator.next();
			for(ChannelHandlerContext channelHandlerContext: group.values()) {
				//ChannelHandlerContext channelHandlerContext = map.get(key);
				channelHandlerContext.channel().writeAndFlush(
						new TextWebSocketFrame(msg));
			}
		}
		/*for (Map<String, ChannelHandlerContext> map : groups) {
			
			//这种方式效率较高
			for(ChannelHandlerContext channelHandlerContext: map.values()) {
				//ChannelHandlerContext channelHandlerContext = map.get(key);
				channelHandlerContext.channel().writeAndFlush(
						new TextWebSocketFrame(msg));
			}
		}*/
		log.info("广播完毕");
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
		broadcast(null,msg);
	}
	
}
