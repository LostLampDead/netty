package com.choice.netty.bytebuf;

import io.netty.buffer.AbstractByteBuf;

/**
 * 从内存角度分析：ByteBuf可以分为堆内存(HeapByteBuf)和直接内存(DirectByteBuf
 * （1）堆内存（HeapByteBuf）字节缓冲区：特点是内存的分配和回收速度快，可以被JVM自动回收；
 * 缺点就是如果进行Socket的I/O读写，需要额外做一次内存复制，将堆内存对应的缓冲区复制到内核Channel中，性能会有一定程度的下降。
 * （2）直接内存（DirectByteBuf）字节缓冲区：非堆内存，它在堆外进行内存分配，相比于堆内存，它的分配和回收速度会慢一些，
 * 但是将它写入或者从Socket Channel中读取时，由于少了一次内存复制，速度比堆内存快。
 * ByteBuf的最佳实践是在I/O通信线程的读写缓冲区使用DirectByteBuf，
 * 后端业务消息的编解码模块使用HeapByteBuf，这样组合可以达到性能最优。
 *
 *
 * @see AbstractByteBuf extends ByteBuf
 */
public class ExploreSourceCode {

    /*
     (1) 主要属性
         // leakDetector -> static，意味着所有的ByteBuf实例都共享一个ResourceLeakDetector
         // ResourceLeakDetector 用于检测对象是否泄露
         static final ResourceLeakDetector<ByteBuf> leakDetector =
                ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ByteBuf .class);
        int readerIndex; // 读索引
        int writerIndex; // 写索引
        private int markedReaderIndex;   // 标记读索引
        private int markedWriterIndex;   // 标记写索引
        private int maxCapacity; // 最大容量


      (2) readBytes 将ByteBuf 中的数据读取到
        public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
            // 校验 readerIndex + length > writerIndex
            checkReadableBytes(length);
            // 遍历 ByteBuf 数据复制到 byte[]
            getBytes(readerIndex, dst, dstIndex, length);
            // readerIndex 变化
            readerIndex += length;
            return this;
        }


       (3) writeBytes 将 byte[] 写到 ByteBuf
        public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
            // 首先对 length 长度合法性校验
            // 如果写入的字节数组长度大于可以动态扩展的最大可写字节数，说明缓冲区无法写入超过其最大容量的字节数组，抛出IndexOutOfBoundsException异常。
            // 如果当前写入的字节数组长度虽然大于目前ByteBuf的可写字节数，但是通过自身的动态扩展可以满足新的写入请求，则进行动态扩展。
            ensureWritable(length);
            // 遍历 bytes 复制到 ByteBuf
            setBytes(writerIndex, src, srcIndex, length);
            writerIndex += length;
            return this;
        }
      */

}
