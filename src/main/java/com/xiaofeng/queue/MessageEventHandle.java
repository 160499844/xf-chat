package com.xiaofeng.queue;

import com.alibaba.fastjson.JSONObject;
import com.xiaofeng.netty.server.DynMessage;
import com.xiaofeng.utils.MessageHandleVo;
import com.xiaofeng.utils.MessageVo;
import com.xiaofeng.web.service.GroupService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理消息队列
 */
@Slf4j
@Component
@RabbitListener(queues = "event_queue")
public class MessageEventHandle {
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private GroupService groupService;
    /**
     * 接收
     * @param messageVo 收到的消息
     */
    @RabbitHandler
    public void process(MessageVo messageVo) {
        log.info("[处理队列]收到指示,触发队列" );

        //GroupService groupService = SpringBeanUtil.getBean(GroupService.class);
       // Integer groupCount = groupService.getGroupCount(messageVo.getGroupId());
       // messageVo.put("group_count", groupCount);// 当前在线人数
        String jsonString = com.xiaofeng.utils.string.StringUtils.toJson(messageVo);
        //DynMessage.broadcast(messageVo.getGroupId(), jsonString);
        DynMessage.broadcast(messageVo.getGroupId(),jsonString,messageSender);
    }

}