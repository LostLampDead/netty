package com.choice.netty.tcp.solve.customizebody;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ServerDecodeHandler extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ServerDecodeHandler#decode is running");
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        MessageCarrier messageCarrier = new MessageCarrier(length, bytes);
        out.add(messageCarrier);
    }
}
