package com.choice.netty.tcp.solve.customizebody;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ClientEncodeHandler extends MessageToByteEncoder<MessageCarrier> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageCarrier msg, ByteBuf out) throws Exception {
        System.out.println("ClientEncodeHandler#encode is running");
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent());
    }
}
