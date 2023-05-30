package com.choice.netty.soucecode;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class EchoClient {
    public static void main(String[] args) throws Exception {

        final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final EchoClientHandler echoClientHandler = new EchoClientHandler();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(echoClientHandler);
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(6666)).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    private static class EchoClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("echoclient channelActive is running...");
            ByteBuf message = Unpooled.copiedBuffer("Hello Server".getBytes(StandardCharsets.UTF_8));
            System.out.println("client send message: " + message.toString(StandardCharsets.UTF_8));
            ctx.writeAndFlush(message);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("echoclient channelRead is running...");
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println("client received message : " + byteBuf.toString(StandardCharsets.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("echoclient exceptionCaught is running...");
        }
    }
}
