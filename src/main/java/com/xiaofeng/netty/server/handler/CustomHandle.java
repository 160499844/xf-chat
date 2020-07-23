package com.xiaofeng.netty.server.handler;

import com.xiaofeng.entity.UserEntity;
import com.xiaofeng.utils.MessageVo;
import com.xiaofeng.utils.user.UserToken;

import io.netty.channel.ChannelHandlerContext;

/**
 * 自定义处理器
 * @author xiaofeng
 *
 */
public interface CustomHandle {

	/**
	 * 处理器
	 * @param ctx 
	 * @return
	 */
	Object messageHandle(MessageVo vo,UserEntity user, ChannelHandlerContext ctx);
	
}
