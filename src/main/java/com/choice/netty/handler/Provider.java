package com.choice.netty.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class Provider {
    public static void main(String[] args)  throws Exception {
        final EventLoopGroup parent = new NioEventLoopGroup(1);
        final EventLoopGroup child = new NioEventLoopGroup();
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parent, child)
                .channel(NioServerSocketChannel.class)
                .handler(new ParentHandler())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ChildHandler());
                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(6666)).sync();
        channelFuture.channel().closeFuture().sync();

        parent.shutdownGracefully();
        child.shutdownGracefully();
    }

    private static class ParentHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("parent handler channelActive running...");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("parent handler channelRead running...");
            super.channelRead(ctx, msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("parent handler exceptionCaught running...");
        }
    }

    private static class ChildHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("child handler channelActive running...");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("child handler channelRead running...");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("child handler exceptionCaught running...");
        }
    }
}
