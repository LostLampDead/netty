package com.choice.netty.tcp.solve.customizebody;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 解决TCP粘包->拆包
 * 有3中方案，一种是客户端只发送固定长度的数据
 * 另一种方案是每次传输数据的时候，将字节的长度一并传过去，这样服务端就可以根据字节长度来读取数据。
 * 最后一种方案是传一个分隔符，作为一次消息的结束。
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
                        pipeline.addLast(new ServerDecodeHandler());
                        pipeline.addLast(new ServerCodeHandler());
                    }
                });
        // 阻塞式等到绑定成功
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        // 阻塞时关闭通道。只有发生了关闭通道的事件才会执行，并不会立即关闭通道。
        channelFuture.channel().closeFuture().sync();
    }
}
