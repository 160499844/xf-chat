package com.xiaofeng.web.service;

import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xiaofeng.global.GroupContext;
import com.xiaofeng.utils.EncryptMessage;
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
		Result of = Result.of(txt);
		Map<String, Object> ofInfo = new HashMap<>();
		List<UserToken> currentUsers = GroupContext.getGroupUsers(groupId);
		ofInfo.put("group_count", currentUsers.size());// 当前在线人数
		ofInfo.put("gourp_users", currentUsers);// 当前在线成员
		of.setInfo(ofInfo);

		// 广播给组成员
		String jsonString = JSONObject.toJSONString(of);
		try {
			jsonString = EncryptMessage.encrypt(jsonString);
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		Map<String, Set<Map<String, ChannelHandlerContext>>> uSER_GROUP = GroupContext.USER_GROUP;
		Set<Map<String, ChannelHandlerContext>> set = uSER_GROUP.get(groupId);
		for (Map<String, ChannelHandlerContext> map : set) {
			for (String userId : map.keySet()) {
				ChannelHandlerContext channelHandlerContext = map.get(userId);
				channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(jsonString));
			}
		}
	}

}
