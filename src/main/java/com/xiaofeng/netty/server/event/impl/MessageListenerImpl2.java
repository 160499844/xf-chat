package com.xiaofeng.netty.server.event.impl;

import com.xiaofeng.netty.server.event.MessageEvent;
import com.xiaofeng.netty.server.event.MessageListener;

import lombok.Data;

/**
 * 事件监听器的实现方法
 * 
 * @author xiaofeng
 *
 */
@Data
public class MessageListenerImpl2 implements MessageListener {
	
	private String groupId;//群组id
	private String txt;//广播消息
	
	
	public MessageListenerImpl2() {}

	public MessageListenerImpl2(String groupId, String txt) {
		super();
		this.groupId = groupId;
		this.txt = txt;
		
	}
	
	public void handleEvent(MessageEvent de) {
		de.run(groupId,txt);// 回调
		System.out.println("新成员离开了群聊");
	}

}