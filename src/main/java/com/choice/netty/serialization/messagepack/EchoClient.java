package com.choice.netty.serialization.messagepack;

import com.choice.netty.serialization.messagepack.UserInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

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
                            // 解决粘包和拆包->在消息头新增消息长度
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0,0, 0,2));
                            pipeline.addLast(new MsgpackDecoder());
                            pipeline.addLast(new LengthFieldPrepender(2));
                            pipeline.addLast(new MsgpackEncoder());
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
            for (int i = 0; i < 500; i++) {
                UserInfo userInfo = new UserInfo().buildUsername(String.valueOf(i)).buildUserId(i);
                ctx.writeAndFlush(userInfo);
            }
        }
    }
}
