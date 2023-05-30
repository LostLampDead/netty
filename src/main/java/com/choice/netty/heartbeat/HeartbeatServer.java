package com.choice.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Netty心跳检测机制
 *
 */
public class HeartbeatServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            // 1. 绑定两个线程组分别用来处理客户端通道的accept和读写时间
            server.group(parentGroup, childGroup)
                    // 2. 绑定服务端通道NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    // 3. 给读写事件的线程通道绑定handler去真正处理读写
                    // ChannelInitializer初始化通道SocketChannel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // IdleStateHandler -> Netty 提供的处理空闲状态的处理器
                            // 当发现超时了，会产生一个 IdleStateEvent
                            // readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                            // writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包来检测是否连接
                            // allIdleTime：表示多长时间没有发生读写，就会发送一个心跳检测包来检测是否连接
                            pipeline.addLast(new IdleStateHandler(5, 10, 20, TimeUnit.SECONDS));
                            // 自定义空闲处理
                            pipeline.addLast(new IdleStateEventHandler());
                        }
                    });

            // 4. 监听端口（服务器host和port端口），同步返回
            ChannelFuture future = server.bind(6666).sync();
            // 当通道关闭时继续向后执行，这是一个阻塞方法
            future.channel().closeFuture().sync();
        } finally {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }
    }
}
