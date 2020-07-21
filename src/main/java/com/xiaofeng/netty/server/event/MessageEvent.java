package com.xiaofeng.netty.server.event;

/**
 * 定义事件（状态）对象（该事件对象包装了事件源对象、作为参数传递给监听器、很薄的一层包装类）
 * 
 * @author xiaofeng
 *
 */
public class MessageEvent extends java.util.EventObject {
	
	
	public MessageEvent(Object source) {
		super(source);// source—事件源对象—如在界面上发生的点击按钮事件中的按钮
		// 所有 Event 在构造时都引用了对象 "source"，在逻辑上认为该对象是最初发生有关 Event 的对象
	}

	/**
	 * 事件回调方法
	 */
	public void run(String groupId,String txt) {
		System.out.println(String.format("(%s)广播消息:%s", groupId,txt));
	}
}