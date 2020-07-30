package com.xiaofeng.queue;

import com.alibaba.fastjson.JSONObject;
import com.xiaofeng.netty.server.DynMessage;
import com.xiaofeng.utils.MessageHandleVo;
import com.xiaofeng.utils.MessageVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 处理消息队列
 */
//@Slf4j
//@Component
//@RabbitListener(queues = "msg_send_queue")
//public class MessageHandleReceiver {
//
//    /**
//     * 接收
//     * @param messageHandleVo 收到的消息
//     */
//    @RabbitHandler
//    public void process(Integer i) {
//        log.info("[处理队列]收到指示,触发队列" );
//
//    }
//
//}