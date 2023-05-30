package com.choice.netty.soucecode;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class EchoServer {
    public static void main(String[] args) throws Exception {
        // 配置指定数量的 EventLoop
        // 实例化 ThreadPerTaskExecutor，会为每一个任务来创建新的线程，保证任务只在当前的线程执行，避免任务之间的相互影响。
        final EventLoopGroup parent = new NioEventLoopGroup(1);
        final EventLoopGroup child = new NioEventLoopGroup(2);
        final EchoChildHandler echoChildHandler = new EchoChildHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parent, child)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(echoChildHandler);
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(6666)).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            parent.shutdownGracefully();
            child.shutdownGracefully();
        }
    }

    private static class EchoChildHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("echochild handler channelActive is running...");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println("echochild received.." + byteBuf.toString(StandardCharsets.UTF_8));
            ctx.writeAndFlush(Unpooled.copiedBuffer("I got".getBytes()));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("echochild handler exceptionCaught is running...");
        }
    }
}
