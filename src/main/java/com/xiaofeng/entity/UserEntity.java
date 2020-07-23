package com.xiaofeng.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 
 * @ClassName:  Group   
 * @Description:群成员组对象
 * @author: 小峰
 * @date:   2020年7月21日 下午3:48:07
 */
@Data
@Document(collection = "chat_user")
public class UserEntity implements Serializable{
	@Id()
	private String id;
	private String sessionId;//
	private String userId;//成员标识
	private String groupId;//组标识
	private String userName;//名称
	private String ip;//创建ip
	private Date createDt = new Date();//创建时间
	
}
