package com.xiaofeng.global;

public interface UtilConstants {


	public static final int SESSION_TIMEOUT = 3600 * 24 * 7;//session过期时间
	public static final String SESSION_GROUP_ID = "groupId";//当前组id
	public static final String SESSION_GROUP_PASSWORD = "password";//当前组密码
	public static final String SESSION_GROUP_INDEX_URL = "chat_index.html?code=";//重定向页面
	public static final String SESSION_GROUP_AES_KEY = "aesKey";//当前登录的aes加密key
	public static final String SESSION_USER_NAME = "userName";//当前登录用户的用户名
	public static final int REDIS_TIMEOUT = 3600 * 24;//超时时间

	public interface MSG{
		/**
		 * 消息类型常量
		 */
		public static final String MSG_TEXT = "T";
		public static final String MSG_PICTURE = "P";
		public static final String MSG_SYSTEM = "S";
	}

}
