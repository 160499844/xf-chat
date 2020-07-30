package com.xiaofeng.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig {

    @Bean
    public Queue eventQueue() {
        return new Queue("event_queue");
    }
    @Bean
    public Queue msgQueue() {
        return new Queue("msg_queue");
    }
//    @Bean
//    public Queue msgSendQueue() {
//        return new Queue("msg_send_queue");
//    }
}