package com.choice.netty.tcp.problem;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class CodeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++) {
            String message = MessageFormat.format("已经{0}年了", i + 1);
            ctx.writeAndFlush(Unpooled.copiedBuffer(message, StandardCharsets.UTF_8));
        }
    }
}
