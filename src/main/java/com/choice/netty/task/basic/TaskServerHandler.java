package com.choice.netty.task.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * ChildGroup的自定义处理器
 * ChannelHandlerContext 上下文对象，主要用户pipeline中多个handler之间的交互
 */
public class TaskServerHandler extends ChannelInboundHandlerAdapter {

    // 连接准备完毕会触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端准备完成！");
    }

    // 当接收到消息会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 方式一：用户自定义线程任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                System.out.println("服务端开始执行一个非常耗时的任务～");
                Thread.sleep(10000);
                System.out.println("服务端执行任务结束～");
                ctx.writeAndFlush(Unpooled.copiedBuffer("服务器端刚刚执行了一个非常耗时的任务", StandardCharsets.UTF_8));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // 方式二：定时任务执行
        // ps schedule 只会执行一次
        ctx.channel().eventLoop().schedule(() -> {
            System.out.println("服务端执行一个定时任务(执行一次)～");
            ctx.writeAndFlush(Unpooled.copiedBuffer("服务器端刚刚执行了一个定时任务(执行一次)", StandardCharsets.UTF_8));
        }, 5, TimeUnit.SECONDS);

        // 方式二：定时循环任务执行
        ctx.channel().eventLoop().scheduleWithFixedDelay(() -> {
            System.out.println("服务端执行一个定时任务(循环)～");
            ctx.writeAndFlush(Unpooled.copiedBuffer("服务器端刚刚执行了一个定时任务(循环)", StandardCharsets.UTF_8));
        }, 0, 15, TimeUnit.SECONDS);

        // 先回复客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("我知道你很急，但是你先不要着急", StandardCharsets.UTF_8));
    }

    // 当发生异常会触发，一般用于异常处理以及通道关闭
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
