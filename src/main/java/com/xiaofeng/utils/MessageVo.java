package com.xiaofeng.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**
 * 消息工具类
 * @author xiaofeng
 *
 */
@Data
public class MessageVo implements Serializable {
	/**
	 * var obj = {
				"msg" : message,
				"name" : userName,
				"type" : "T",
				"groupId" : groupId
			}
	 */
	private String sessionId;//创建消息的用户唯一标识
	private String msg;//消息实体
	private String content ;//返回内容
	private String groupId;//群组id
	private String name;//发送用户昵称
	private String type;//消息类型 T-文本 P-图片
	private String dateTime = DateUtils.getNowDateToString();//消息发送时间
	private String time = DateUtils.getNowDateToString("HH:mm:ss");;//消息发送时间
	private Map<String,Object> info = new HashMap<String, Object>();//额外信息
	
	/**
	 * @Title: put   
	 * @Description: 放入额外信息
	 * @param: @param key
	 * @param: @param obj      
	 * @return: void      
	 * @throws
	 */
	public void put(String key,Object obj) {
		info.put(key, obj);
	}
	
	
}
