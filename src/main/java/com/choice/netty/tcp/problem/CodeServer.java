package com.choice.netty.tcp.problem;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 演示TCP出现粘包和拆包的解决方案
 * 对于服务端，先使用入站处理器进行编码转换，一般是将字节转成JAVA对象。
 * 对于客户端，会使用出战处理器进行编码转换，一般是将JAVA对象转成字节。
 */
public class CodeServer {
    public static void main(String[] args) throws Exception {
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
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CodeServerHandler());
                    }
                });
        // 阻塞式等到绑定成功
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        // 阻塞时关闭通道。只有发生了关闭通道的事件才会执行，并不会立即关闭通道。
        channelFuture.channel().closeFuture().sync();
    }
}
