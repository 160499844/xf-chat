package com.xiaofeng.utils;

import java.io.Serializable;

import com.xiaofeng.netty.server.DynMessage;

import lombok.Data;

/**
 * 保存用户信息
 * @author xiaofeng
 *
 */
@Data
public class UserToken implements Serializable{
	//用户id
	private String userId;
	//组id
	private String groupId = DynMessage.DEFAULT_GROUP;
	//用户名称
	private String userName;
	
	private String ip;
	
	
}
