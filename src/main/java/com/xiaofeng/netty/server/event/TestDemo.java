package com.xiaofeng.netty.server.event;

import com.xiaofeng.netty.server.event.impl.MessageListenerImpl;

/**
 * 演示监听器模式,暂时没法用上
 * @author xiaofeng
 *
 */
public class TestDemo {
	MessageSource ds;

	public TestDemo() {
		try {
			ds = new MessageSource();
			// 将监听器在事件源对象中登记：
			ds.addDemoListener(new MessageListenerImpl("test","我来了"));
			ds.addDemoListener(new MessageListenerImpl("test","我下线了"));
			ds.notifyDemoEvent();// 触发事件、通知监听器
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new TestDemo();
	}
}