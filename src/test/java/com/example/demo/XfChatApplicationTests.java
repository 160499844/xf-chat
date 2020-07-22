package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.xiaofeng.utils.RedisUtil;

@SpringBootTest
class XfChatApplicationTests {
	
	@Autowired
	private RedisUtil redisUtil;

	@Test
	void contextLoads() {
		redisUtil.set("test2", "123");
		System.out.println(redisUtil.get("test2"));
		System.out.println(redisUtil.get("test"));
	}

}
