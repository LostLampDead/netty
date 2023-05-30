package com.choice.netty.task.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 在自定义的出站入站处理器时，最好不要做耗时长的操作，不然可能会阻塞整个IO，性能下降。
 * 如果需要处理耗时操作，一般推荐使用任务处理。
 * (1) 用户程序自定义任务
 * (2) 用户自定义定时任务
 * (3) 非当前Reactor线程调用Channel的方法
 */
public class TaskServer {
    public static void main(String[] args) throws Exception {
        // 可以指定含有的子线程数量，默认为当前操作系统的核*2
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        // 创建server启动引导器
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(6666))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("hello");
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new TaskServerHandler());
                    }
                });
        // 阻塞式等到绑定成功
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        // 阻塞时关闭通道。只有发生了关闭通道的事件才会执行，并不会立即关闭通道。
        channelFuture.channel().closeFuture().sync();
    }
}
