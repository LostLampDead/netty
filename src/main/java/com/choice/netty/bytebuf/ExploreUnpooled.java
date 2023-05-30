package com.choice.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * @see Unpooled 提供了静态的辅助方法来创建未池化的ByteBuf实例
 */
@SuppressWarnings("unused")
public class ExploreUnpooled {
    public static void main(String[] args) {
        // exploreNormalMethod();
        exploreWrappedBuffer();
    }

    private static void exploreWrappedBuffer(){
        // Unpooled.wrappedBuffer 返回的ByteBuf不可扩容的，maxCapacity 已经确定
        ByteBuf copiedBuffer = Unpooled.copiedBuffer("Hello Netty".getBytes(StandardCharsets.UTF_8));
        // 会抛出异常，并不会动态扩容
        copiedBuffer.writeBytes("Bye!".getBytes(StandardCharsets.UTF_8));
    }

    private static void exploreNormalMethod(){
        // 返回一个未池化的基于堆内存的ByteBuf
        ByteBuf heapBuffer = Unpooled.buffer();
        // 返回一个未池化的基于直接内存的ByteBuf
        ByteBuf directedBuffer = Unpooled.directBuffer();
        // 返回一个包装了给定数据的ByteBuf
        ByteBuf wrappededBuffer = Unpooled.wrappedBuffer("Hello Netty".getBytes(StandardCharsets.UTF_8));
        // 返回一个复制了给定数据的ByteBuf -> 不会影响源数据，以及索引
        ByteBuf copiededBuffer = Unpooled.copiedBuffer(wrappededBuffer);
        System.out.printf("源数据=%s writeIndex=%s readerIndex=%s\n", wrappededBuffer.toString(StandardCharsets.UTF_8),
                wrappededBuffer.writerIndex(), wrappededBuffer.readerIndex());
        System.out.printf("复制结果数据=%s writeIndex=%s readerIndex=%s\n", copiededBuffer.toString(StandardCharsets.UTF_8),
                copiededBuffer.writerIndex(), copiededBuffer.readerIndex());

        wrappededBuffer.clear();
        wrappededBuffer.writeBytes("Bye!".getBytes());
        System.out.println("改变源数据的数据以及下标之后～");

        System.out.printf("源数据=%s writeIndex=%s readerIndex=%s\n", wrappededBuffer.toString(StandardCharsets.UTF_8),
                wrappededBuffer.writerIndex(), wrappededBuffer.readerIndex());
        System.out.printf("复制结果数据=%s writeIndex=%s readerIndex=%s\n", copiededBuffer.toString(StandardCharsets.UTF_8),
                copiededBuffer.writerIndex(), copiededBuffer.readerIndex());
    }
}
