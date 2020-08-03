package com;

import java.net.InetSocketAddress;

import com.xiaofeng.queue.MessageRunnable01;
import com.xiaofeng.queue.MessageRunnable02;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring4all.mongodb.EnableMongoPlus;
import com.xiaofeng.netty.server.NettyServer;

@EnableMongoPlus
@SpringBootApplication
public class XfChatApplication {
	

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();    //获取开始时间
		SpringApplication.run(XfChatApplication.class, args);
		//init();
		 //启动服务端
        NettyServer nettyServer = new NettyServer();
        new Thread(new MessageRunnable01()).start();
        new Thread(new MessageRunnable02()).start();
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("启动消耗时间:" + ((endTime - startTime)/1000) + "秒");    //输出程序运行时间
        nettyServer.start(new InetSocketAddress("0.0.0.0", 8900));
	}
	
	
//	@Bean
//	public ServerEndpointExporter serverEndpointExporter() {
//	   return new ServerEndpointExporter();
//	}
}
