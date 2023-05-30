package com.choice.netty.tcp.solve.customizebody;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class CodeClient {
    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ClientEncodeHandler());
                        pipeline.addLast(new CodeClientHandler());
                    }
                });

        // 阻塞式等待连接成功
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(6666)).sync();
        // 阻塞式等待连接关闭
        channelFuture.channel().closeFuture().sync();
    }
}
