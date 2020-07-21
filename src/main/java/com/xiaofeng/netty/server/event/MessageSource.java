package com.xiaofeng.netty.server.event;


import java.util.Enumeration;
import java.util.Vector;

/**
 * 事件源
 * 
 * @author xiaofeng
 *
 */
public class MessageSource {
	private Vector repository = new Vector();// 监听自己的监听器队列

	public MessageSource() {
	}

	public void addDemoListener(MessageListener dl) {
		repository.addElement(dl);
	}

	/**
	 * 事件源触发事件
	 */
	public void notifyDemoEvent() {// 通知所有的监听器
		Enumeration elements = repository.elements();
		while (elements.hasMoreElements()) {
			MessageListener dl = (MessageListener) elements.nextElement();
			dl.handleEvent(new MessageEvent(this));
		}
	}
}