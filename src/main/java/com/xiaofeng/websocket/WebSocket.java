package com.xiaofeng.websocket;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.xiaofeng.websocket.utils.MsgUtils;
 
/**
 * 
 * @ClassName:  WebSocket   
 * @Description: 连接  
 * @author: 严伟峰(ywf)
 * @date:   2020年7月14日 下午4:48:15
 */
@ServerEndpoint("/websocket/{groupId}")
@Component
public class WebSocket {
 
    private static final String loggerName=WebSocket.class.getName();
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    //List<Session>对应一组用户
    public static Map<String, List<Session>> electricSocketMap = new ConcurrentHashMap<String, List<Session>>();
    //用户和组的对应关系，用户id-组id
    public static Map<String,String> userGroups = new HashMap<String, String>();
    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("groupId") String groupId, Session session) {
        List<Session> sessions = electricSocketMap.get(groupId);
        userGroups.put(session.getId(), groupId);
        if(null==sessions){
            List<Session> sessionList = new ArrayList<>();
            sessionList.add(session);
            electricSocketMap.put(groupId,sessionList);
        }else{
            sessions.add(session);
        }
    }
 
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("groupId") String groupId,Session session) {
        if (electricSocketMap.containsKey(groupId)){
            electricSocketMap.get(groupId).remove(session);
        }
    }
 
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("websocket received message:"+message);
      //群发消息给所有连接的用户
    	String id = session.getId();
    	System.out.println(String.format("用户(%s):%s", id,message));
    	String groupId = userGroups.get(id);
    	try {
    		//发给组内的用户，群发
			MsgUtils.sendGroupMsg(groupId,session,message);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
 
    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");;
    }
}
