package com.xiaofeng.queue;

import com.xiaofeng.netty.server.DynMessage;
import com.xiaofeng.utils.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RabbitListener(queues = "hello")
public class MessageReceiver {

    /**
     * 接收消息
     * @param messageVo 收到的消息
     */
    @RabbitHandler
    public void process(MessageVo messageVo) {
        String jsonString = com.xiaofeng.utils.string.StringUtils.toJson(messageVo);
        log.info("消息队列收到消息  : " + jsonString);
        DynMessage.broadcast(messageVo.getGroupId(), jsonString);
    }

}