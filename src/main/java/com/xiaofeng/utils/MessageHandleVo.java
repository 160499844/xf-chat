package com.xiaofeng.utils;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息处理vo
 */
@Data
public class MessageHandleVo {
    private ChannelHandlerContext channelHandlerContext;//消息接收对象
    private String msg;//接收消息内容

    public MessageHandleVo(){}
    public MessageHandleVo(ChannelHandlerContext channelHandlerContext, String msg) {
        this.channelHandlerContext = channelHandlerContext;
        this.msg = msg;
    }
}

