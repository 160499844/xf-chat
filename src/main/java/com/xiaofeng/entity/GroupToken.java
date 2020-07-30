package com.xiaofeng.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 保存组信息
 * @author xiaofeng
 *
 */
@Data
public class GroupToken {

	private String key;//小组口令
	
	private String aesKey;//aes key
	
	private String publicKey;//消息解密公钥

	private String link;//访问地址
	
	public GroupToken() {
		super();
	}
	public GroupToken(String key,String aesKey,String publickey) {
		this.key = key;
		this.publicKey = publickey;
		this.aesKey = aesKey;
	}
}
