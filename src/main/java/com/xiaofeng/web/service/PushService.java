package com.xiaofeng.web.service;

import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xiaofeng.entity.GroupToken;
import com.xiaofeng.global.GroupContext;
import com.xiaofeng.netty.server.DynMessage;
import com.xiaofeng.netty.server.handler.TextWebSocketFrameHandler;
import com.xiaofeng.utils.EncryptMessage;
import com.xiaofeng.utils.MessageVo;
import com.xiaofeng.utils.Result;
import com.xiaofeng.utils.exception.BaseException;
import com.xiaofeng.utils.user.UserToken;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PushService {

	/**
	 * 推送消息
	 * 
	 * @param txt
	 * @throws InvalidAlgorithmParameterException 
	 */

	public void pushMessage(String groupId, String txt) throws InvalidAlgorithmParameterException {
		// 组装返回对象
		MessageVo messageVo = new MessageVo();
		Integer groupCount = GroupContext.getGroupCount(groupId);
		messageVo.put("group_count", groupCount);// 当前在线人数
		GroupToken groupToken = GroupContext.GROUPS.get(groupId).getToken();
		if(groupToken!=null) {
			String msg = EncryptMessage.encrypt(txt, groupToken.getAesKey());
			messageVo.setContent(msg);
			messageVo.setGroupId(groupId);
			messageVo.setType("S");
			messageVo.setName("系统消息");
			messageVo.setMsg(msg);
			log.info(String.format("广播消息(%s):%s", groupId,txt));
			DynMessage.broadcast(groupId, com.xiaofeng.utils.string.StringUtils.toJson(messageVo));
		}else {
			throw new BaseException("操作失败,组标识无效!");
		}
	}

}
