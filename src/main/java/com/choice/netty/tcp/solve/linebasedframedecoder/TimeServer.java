package com.choice.netty.tcp.solve.linebasedframedecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

/**
 * @see LineBasedFrameDecoder 解决粘包
 * 可以将基于行的文本数据流分解为单独的帧。它通过扫描接收到的字节流并查找换行符（"\n"）或回车换行符（"\r\n"）来完成此操作。
 * 每当找到这些字符中的一个时，它就会将前面的字节转换为一个新的帧，然后将其传递给下一个处理器进行处理。
 * 使用LineBasedFrameDecoder可以简化处理基于文本的协议的网络通信。例如，在基于Telnet的应用程序中，
 * 每个命令都以换行符结尾，因此使用LineBasedFrameDecoder可以轻松地将每个命令转换为单独的帧。
 */
public class TimeServer {
    public void bind(int port) throws Exception {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            // 阻塞式等待端口绑定成功
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            // 等待服务器监听端口故拿笔
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    private static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            // 1024表示支持单行的最大长度
            // 超出会报错 io.netty.handler.codec.TooLongFrameException: frame length (16) exceeds the allowed maximum (设置的最大长度)
            pipeline.addLast(new LineBasedFrameDecoder(1024));
            pipeline.addLast(new StringDecoder());
            pipeline.addLast(new TimeServerHandler());
        }
    }

    private static class TimeServerHandler extends ChannelInboundHandlerAdapter {
        private int counter = 0;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String body = (String) msg;
            System.out.printf("服务端第%s次收到消息:%s\n", ++counter, body);
        }
    }
}
