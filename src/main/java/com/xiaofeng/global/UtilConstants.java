package com.xiaofeng.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public interface UtilConstants {
	/**
	 * netty常量
	 */
	@Component
	class NettyConfigConstants{
		public static String HOST_NAME = "0.0.0.0";
		public static int HOST_PORT;
		public static final String HOST_PATH = "/ws";

		@Autowired
		private NettyConfigConstants(@Value("${webSocket.netty.port}") int port) {
			NettyConfigConstants.HOST_PORT = port;
		}
	}

	/**
	 * session常量
	 */
	public static final int SESSION_TIMEOUT = 3600 * 24 * 7;//session过期时间
	public static final String SESSION_GROUP_ID = "groupId";//当前组id
	public static final String SESSION_GROUP_PASSWORD = "password";//当前组密码
	public static final String SESSION_GROUP_INDEX_URL = "chat_index.html?code=";//重定向页面
	public static final String SESSION_GROUP_AES_KEY = "aesKey";//当前登录的aes加密key
	public static final String SESSION_USER_NAME = "userName";//当前登录用户的用户名
	public static final int REDIS_TIMEOUT = 3600 * 24;//超时时间

	/**
	 * 消息
	 */
	public interface MSG{
		/**
		 * 消息类型常量
		 */
		public static final String MSG_TEXT = "T";
		public static final String MSG_PICTURE = "P";
		public static final String MSG_SYSTEM = "S";
		public static final String MSG_SYSTEM_REMOVE = "SR";
		public static final String MSG_SYSTEM_ADD = "SA";
	}

}
