package com.xiaofeng.websocket.utils;

import java.io.IOException;
import java.util.List;

import javax.websocket.Session;

import com.xiaofeng.websocket.WebSocket;

/**
 * 
 * @ClassName:  MsgUtils   
 * @Description:发送消息工具类
 * @author: 严伟峰(ywf)
 * @date:   2020年7月14日 下午5:39:27
 */
public class MsgUtils {

	/**
	 * @throws IOException 
	 * @param session 
	 * 
	 * @Title: sendGroupMsg   
	 * @Description: 群发消息 
	 * @param: @param groupId 组id
	 * @param: @param msg      消息
	 * @return: void      
	 * @throws
	 */
	public static void sendGroupMsg(String groupId,Session session, String msg) throws IOException {
		
		if(WebSocket.electricSocketMap.containsKey(groupId)) {
    		List<Session> allUserSessions =WebSocket.electricSocketMap.get(groupId);
    		for (Session session2 : allUserSessions) {
    			session2.getBasicRemote().sendText(String.format("</br>[组内消息][(%s)]：%s",session.getId(),msg));
			}
    	}
	}
}
