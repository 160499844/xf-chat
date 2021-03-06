package com.xiaofeng.queue;

import com.xiaofeng.global.GroupContext;
import com.xiaofeng.utils.MessageHandleVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理消息队列
 */
@Slf4j
public class MessageRunnable01 implements Runnable {

    @SneakyThrows
    @Override
    public void run() {
        log.info("[消息处理队列01]初始化");
        while (true) {
            try {
                MessageHandleVo take = GroupContext.userSession.take();
                String msg = take.getMsg();
                ChannelHandlerContext channelHandlerContext = take.getChannelHandlerContext();
                channelHandlerContext.channel().writeAndFlush(
                        new TextWebSocketFrame(msg));
                log.info("[消息处理队列01]OK");
            }catch (Exception e){
                log.error("[消息处理队列01]异常");
                e.printStackTrace();
            }
            try {
                if(GroupContext.userSession.isEmpty()){
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
