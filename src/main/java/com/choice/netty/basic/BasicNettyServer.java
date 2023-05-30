package com.choice.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * (1) Netty抽象出两组线程池。ParentGroup负责接收客户端的连接，ChildGroup负责网络的读写。
 * (2) ParentGroup和ChildGroup的类型都是NioEventLoopGroup。
 * (3) NioEventLoopGroup 相当于一个事件循环组，每一个事件循环都是EventLoop。
 * (4) NioEventLoop 表示一个不断循环的执行处理任务的过程。每一个NioEventLoop都有一个Selector，用于监听绑定在其上的socket网络通讯。
 * (5) NioEventLoopGroup 可以有多个线程，即可以含有多个NioEventGroup。
 * (6) ParentGroup 主要负责
 * - 轮询accept事件
 * - 处理accept事件，与client建立连接，生成NioSocketChannel，并将其注册到child某个的NioEventLoop上的selector。
 * - 处理任务队列的任务，即runAllTasks。
 * (7) ChildGroup 主要负责
 * - 轮询read，write事件
 * - 处理i/o事件，即read/write事件，在对应的NioSocketChannel处理。
 * - 处理任务队列的任务，即runAllTasks。
 * (8) 每一个ChildGroup 处理业务时，会使用pipeline(管道)，pipeline包含了channel，即通过pipeline可以获取到对应的channel。pipeline中包含许多处理器。
 */
public class BasicNettyServer {
    public static void main(String[] args) throws Exception {
        // 可以指定含有的子线程数量，默认为当前操作系统的核*2
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            // 创建server启动引导器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(6666))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new BasicServerHandler());
                        }
                    });
            // 阻塞式等到绑定成功
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            // 阻塞时关闭通道。只有发生了关闭通道的事件才会执行，并不会立即关闭通道。
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
