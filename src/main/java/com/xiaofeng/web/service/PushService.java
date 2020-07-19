package com.xiaofeng.web.service;

import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xiaofeng.global.GroupContext;
import com.xiaofeng.netty.server.DynMessage;
import com.xiaofeng.utils.EncryptMessage;
import com.xiaofeng.utils.MessageVo;
import com.xiaofeng.utils.Result;
import com.xiaofeng.utils.UserToken;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Service
public class PushService {

	/**
	 * 推送消息
	 * 
	 * @param txt
	 */

	public void pushMessage(String groupId, String txt) {
		// 组装返回对象
		// 组装返回对象
		MessageVo messageVo = new MessageVo();
		messageVo.setContent(txt);
		List<UserToken> currentUsers = GroupContext.getGroupUsers(groupId);
		messageVo.put("group_count", currentUsers.size());// 当前在线人数
		messageVo.put("gourp_users", currentUsers);// 当前在线成员

		DynMessage.broadcast(groupId, com.xiaofeng.utils.StringUtils.toJsonEncrypt(messageVo,"1538663015386630"));
	}

}
