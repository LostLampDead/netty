package com.choice.netty.tcp.solve.delimiterbasedframedecoder;

import com.choice.netty.tcp.solve.linebasedframedecoder.TimeServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class EchoServer {
    public void bind(int port) throws Exception {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            ByteBuf delimit = Unpooled.copiedBuffer("$_$".getBytes(StandardCharsets.UTF_8));
                            pipeline.addLast(new DelimiterBasedFrameDecoder(1024, delimit));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new EchoServerHandler());
                        }
                    });
            // 阻塞式等待端口绑定成功
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            // 等待服务器监听端口故拿笔
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    private static class EchoServerHandler extends ChannelInboundHandlerAdapter {
        private int counter = 0;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String body = (String) msg;
            System.out.printf("服务端第%s次收到消息:%s\n", ++counter, body);
        }
    }
}
