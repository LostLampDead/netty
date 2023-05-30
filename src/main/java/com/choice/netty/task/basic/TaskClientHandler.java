package com.choice.netty.task.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class TaskClientHandler extends ChannelInboundHandlerAdapter {
    // 连接成功会触发回调
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接成功！");
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端，我来了", StandardCharsets.UTF_8));
    }

    // 当接收到消息会回调
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        // 注意：打印消息需要指定编码类型 StandardCharsets.UTF_8
        System.out.printf("客户端接收到消息：%s\n", byteBuf.toString(StandardCharsets.UTF_8));
    }

    // 当出现异常时会回调
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
