package com.choice.netty.tcp.solve.customizebody;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerCodeHandler extends SimpleChannelInboundHandler<MessageCarrier> {
    int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageCarrier msg) throws Exception {
        System.out.printf("服务端第%s次接收到的消息：%s\n", ++count, new String(msg.getContent()));
    }
}
