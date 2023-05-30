package com.choice.netty.tcp.solve.customizebody;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class CodeClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端开始发送数据～");
        for (int i = 0; i < 5; i++) {
            String message = MessageFormat.format("已经{0}年了", i + 1);
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            ctx.writeAndFlush(new MessageCarrier(bytes.length, bytes));
        }
        System.out.println("客户端发送数据完毕～");
    }
}
