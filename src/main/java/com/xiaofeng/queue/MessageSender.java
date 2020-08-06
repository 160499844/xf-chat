package com.xiaofeng.queue;

import com.xiaofeng.global.UtilConstants;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/*
发送者
 */
@Component
public class MessageSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * 消息队列,存放待发送的消息
     * @param msg
     */
    public void sendMsg(Object msg) {
        this.rabbitTemplate.convertAndSend(UtilConstants.QUEUE.QUEUE_MSG, msg);
    }
//    /**
//     * 消息发送队列,处理呆发送的消息
//     * @param msg
//     */
//    public void sendMsgHandle(Object msg) {
//
//        this.rabbitTemplate.convertAndSend("msg_send_queue", msg);
//    }
    /**
     * 触发事件队列
     * @param msg
     */
    public void sendEnvent(Object msg) {
        this.rabbitTemplate.convertAndSend(UtilConstants.QUEUE.QUEUE_EVENT, msg);
    }
}