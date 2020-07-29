package com.xiaofeng.queue;

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

    public void send(Object msg) {
        this.rabbitTemplate.convertAndSend("hello", msg);
    }

}