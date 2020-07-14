package com;

import java.net.InetSocketAddress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.xiaofeng.netty.server.NettyServer;

@SpringBootApplication
public class XfChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(XfChatApplication.class, args);
		 //启动服务端
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(new InetSocketAddress("0.0.0.0", 8000));
	}
	
	
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
	   return new ServerEndpointExporter();
	}
}
