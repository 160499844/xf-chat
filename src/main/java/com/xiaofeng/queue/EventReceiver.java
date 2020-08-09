package com.xiaofeng.queue;

import com.xiaofeng.global.UtilConstants;
import com.xiaofeng.netty.server.DynMessage;
import com.xiaofeng.utils.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接收触发事件队列中的事件
 */
//@Slf4j
//@Component
//@RabbitListener(queues = UtilConstants.QUEUE.QUEUE_EVENT)
//public class EventReceiver {
//
//    /**
//     * 接收
//     * @param messageVo 收到的事件
//     */
//    @RabbitHandler
//    public void process(MessageVo messageVo) {
//        String jsonString = com.xiaofeng.utils.string.StringUtils.toJson(messageVo);
//        log.info("消息队列收到触发事件  : " + jsonString);
//        DynMessage.broadcast(messageVo.getGroupId(), jsonString);
//    }
//
//}