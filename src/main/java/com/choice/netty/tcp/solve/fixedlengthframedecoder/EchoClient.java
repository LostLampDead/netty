package com.choice.netty.tcp.solve.fixedlengthframedecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class EchoClient {

    public void bind(int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new FixedLengthFrameDecoder(16));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new EchoClientHandler());
                        }
                    });
            // 阻塞式等待连接成功
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(port)).sync();
            // 等待客户端关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class EchoClientHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelActive (ChannelHandlerContext ctx) throws Exception {
            for (int i = 0; i < 5; i++) {
                String message = MessageFormat.format("这是第{0}年了", i + 1);
                ctx.writeAndFlush(Unpooled.copiedBuffer(message, StandardCharsets.UTF_8));
            }
        }
    }
}
