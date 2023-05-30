package com.choice.netty.serialization.messagepack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 注意点
 * - 在接收数据的时候，不能直接使用pojo进行进行强转，要使用List<POJO>进行转换
 * - 传输的POJO实体类，要在类加上注解 @Message
 */
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
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            pipeline.addLast(new MsgpackDecoder());
                            pipeline.addLast(new LengthFieldPrepender(2));
                            pipeline.addLast(new MsgpackEncoder());
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
            // 不可以直接使用对象接收！
            @SuppressWarnings("unchecked")
            List<UserInfo> content = (List<UserInfo>) msg;
            System.out.printf("服务端第%s次收到消息:%s\n", ++counter, content);
        }
    }
}