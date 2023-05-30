package com.choice.netty.tcp.solve.customizebody;

public class MessageCarrier {
    private final int length;
    private final byte[] content;

    public MessageCarrier(int length, byte[] content){
        this.length = length;
        this.content = content;
    }

    public int getLength() {
        return length;
    }

    public byte[] getContent() {
        return content;
    }
}
