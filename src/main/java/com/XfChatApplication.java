package com;

import java.net.InetSocketAddress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.xiaofeng.netty.server.NettyServer;
import com.xiaofeng.utils.SpringBeanUtil;
import com.xiaofeng.web.repository.GroupRepository;

@SpringBootApplication
public class XfChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(XfChatApplication.class, args);
		init();
		 //启动服务端
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(new InetSocketAddress("0.0.0.0", 8888));
	}
	
	
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
	   return new ServerEndpointExporter();
	}
	/**
	 * 
	 * @Title: init   
	 * @Description: 初始化
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	private static void init() {
		//清空MongoDB群组数据
		GroupRepository bean = SpringBeanUtil.getBean(GroupRepository.class);
		bean.clearGroups();
	}
	
}
