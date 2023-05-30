package com.choice.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * ChildGroup的自定义处理器
 * ChannelHandlerContext 上下文对象，主要用户pipeline中多个handler之间的交互
 */
public class BasicServerHandler extends ChannelInboundHandlerAdapter {

    // 连接准备完毕会触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端准备完成！");
    }

    // 当接收到消息会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        // 注意：打印消息需要指定编码类型 StandardCharsets.UTF_8
        System.out.printf("服务器端接收到消息：" + byteBuf.toString(StandardCharsets.UTF_8));
        // 回显客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端接收到消息～".getBytes(StandardCharsets.UTF_8)));
    }

    // 当发生异常会触发，一般用于异常处理以及通道关闭
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
