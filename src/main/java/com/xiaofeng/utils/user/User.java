package com.xiaofeng.utils.user;

import java.util.HashSet;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * 
 * @ClassName:  User   
 * @Description: 成员对象  
 * @author: 小峰
 * @date:   2020年7月16日 下午4:48:00    
 * @param <T>
 */
@Data
public class User<T> extends HashSet<T>{
	

	public void addUser(String userId,ChannelHandlerContext ctx) {
		
	}
}
