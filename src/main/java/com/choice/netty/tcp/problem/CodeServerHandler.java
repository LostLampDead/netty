package com.choice.netty.tcp.problem;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class CodeServerHandler extends ChannelInboundHandlerAdapter {
    int count = 0;

    // 客户端发送了5次写bytebuf请求，但是服务端可能一次就全部读取完了
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.printf("服务端第%s次接收到的消息：%s\n", ++count, byteBuf.toString(StandardCharsets.UTF_8));
    }
}
