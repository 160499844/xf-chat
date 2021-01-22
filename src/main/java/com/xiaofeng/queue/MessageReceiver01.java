package com.xiaofeng.queue;

import com.xiaofeng.global.GroupContext;
import com.xiaofeng.global.UtilConstants;
import com.xiaofeng.netty.server.DynMessage;
import com.xiaofeng.utils.MessageHandleVo;
import com.xiaofeng.utils.MessageVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 接收消息队列中的内容
 */
@Slf4j
@Component
@RabbitListener(queues = UtilConstants.QUEUE.QUEUE_MSG, concurrency = "4")
public class MessageReceiver01 {
    @Autowired
    private MessageSender messageSender;
    /**
     * 接收消息
     * @param messageVo 收到的消息
     */
    @RabbitHandler
    public void process(MessageVo messageVo) {
        String jsonString = com.xiaofeng.utils.string.StringUtils.toJson(messageVo);
        log.info("[消息队列]收到消息  : " + jsonString);
        //DynMessage.broadcast(messageVo.getGroupId(), jsonString);
        DynMessage.broadcast(messageVo.getGroupId(),jsonString,messageSender);
    }

}