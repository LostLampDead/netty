package com.choice.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocatorMetric;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Netty中的ByteBufAllocator是一种内存分配器，用于分配ByteBuf实例所需的内存。ByteBufAllocator提供了两种分配ByteBuf的方式：基于池化的分配和非池化的分配。
 * - 在基于池化的分配中，ByteBufAllocator使用一个内存池来管理ByteBuf实例所需的内存。当需要创建新的ByteBuf实例时，
 * ByteBufAllocator会从池中获取可用的内存块，而不是每次都分配新的内存。这种方法可以提高性能和内存利用率。
 * - 在非池化的分配中，ByteBufAllocator不使用内存池，而是每次都分配新的内存。这种方法在某些情况下可能更加适合，例如需要使用大量内存的情况。
 * 在Netty中，每个Channel都有一个自己的ByteBufAllocator实例。默认情况下，这个实例是基于池化的分配。
 * 可以通过设置ChannelConfig的allocator方法来修改它。也可以创建自定义的ByteBufAllocator实例，并将其分配给Channel。
 * 总之，ByteBufAllocator是Netty中非常重要的一个组件，它可以有效地管理内存，提高性能和内存利用率。
 * (1) 常用方法
 * heapBuffer() 获取一个基于堆内存的ByteBuf
 * directBuffer() 获取一个基于直接内存的ByteBuf
 * compositeBuffer() 获取一个CompositeByteBuf实例，用于组合多个ByteBuf实例
 * (2) 实现
 * PooledByteBufAllocator 和 Unpooled-ByteBufAllocator。
 * 前者池化了ByteBuf的实例以提高性能并最大限度地减少内存碎片。后者的实现不池化ByteBuf实例，并且在每次它被调用时都会返回一个新的实例。
 * Netty默认使用了 PooledByteBufAllocator”
 */
public class ExploreByteBufAllocator {
    public static void main(String[] args) {
        explorePooledByteBufAllocator();
    }

    private static void explorePooledByteBufAllocator(){
        PooledByteBufAllocator pooledByteBufAllocator = new PooledByteBufAllocator();
        Set<Integer> hashCodes = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            // 复用～ PoolThreadLocalCache
            ByteBuf byteBuf = pooledByteBufAllocator.heapBuffer();
            System.out.printf("ByteBuf data=%s writerIndex=%s readerIndex=%s\n", byteBuf.toString(StandardCharsets.UTF_8),
                    byteBuf.writerIndex(), byteBuf.readerIndex());
            byteBuf.writeBytes("hello".getBytes(StandardCharsets.UTF_8));
            System.out.printf("ByteBuf data=%s writerIndex=%s readerIndex=%s\n", byteBuf.toString(StandardCharsets.UTF_8),
                    byteBuf.writerIndex(), byteBuf.readerIndex());
            int hashCode = byteBuf.hashCode();
            hashCodes.add(hashCode);
            // data writerIndex readerIndex 都会被清空
            byteBuf.release();
            System.out.println("===================");
        }
        System.out.println("hashCodes.size() = " + hashCodes.size());
        // 返回PooledByteBufAllocator的统计数据。可以获取关于内存分配和回收的信息，例如分配次数、释放次数、总共分配的内存大小等等。
        PooledByteBufAllocatorMetric metric = pooledByteBufAllocator.metric();
        System.out.println(metric);
    }
}
