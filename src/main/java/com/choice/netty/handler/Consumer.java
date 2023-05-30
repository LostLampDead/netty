package com.choice.netty.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Consumer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("clint is success connect to server");
                                String message = "Hello Server";
                                System.out.printf("client send message: %s\n", message);
                                ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes(StandardCharsets.UTF_8)));
                            }
                        });
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(6666)).sync();
        channelFuture.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
