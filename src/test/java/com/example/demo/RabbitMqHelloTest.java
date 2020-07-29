package com.example.demo;

import com.xiaofeng.queue.MessageSender;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqHelloTest {

    @Autowired
    private MessageSender helloSender;

    @Test
    public void hello() throws Exception {
        helloSender.send("");
    }

}