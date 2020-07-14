package com.xiaofeng.global;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;

/**
 * 组成员信息保存
 * 
 * @author xiaofeng
 *
 */
public class GroupContext {

	// 存储组成员
	public static Map<String, Set<Map<String, ChannelHandlerContext>>> USER_GROUP = new ConcurrentHashMap<>();
}
