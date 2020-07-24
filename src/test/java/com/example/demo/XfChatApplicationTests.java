package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

	public static void testMap() {
		// 初始化
		Map<String, Map<String, String>> maps = new ConcurrentHashMap();
		for (int j = 0; j < 1000; j++) {
			Map<String, String> m = new HashMap<String, String>();
			for (int i = 0; i < 10000; i++) {
				m.put(i + "", i + "");
			}
			maps.put("k" + j, m);
		}
		// 搜索指定key,比如查找组=888,key=8888的用户
		long startTime=System.currentTimeMillis();   //获取开始时间
		Map<String, String> map = maps.get("k888");
		for(String v:map.values()) {
			if("8888".equals(v)) {
				System.out.println("找到了");
				break;
			}
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
	}
	public static void testList() {
		// 初始化
		Map<String,String> map = new ConcurrentHashMap();
		for(int i=0;i<10000000;i++) {
			map.put("k"+i, i+"");
		}
		long startTime=System.currentTimeMillis();   //获取开始时间
		System.out.println("找到了"+map.get("k888"));
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
	}
	// 测试速度
	public static void main(String[] args) {
		testList();
	}
}
