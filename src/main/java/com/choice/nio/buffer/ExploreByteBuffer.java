package com.choice.nio.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @see ByteBuffer 底层是数组
 * @see Buffer
 * 一、重要属性
 * capacity 指定容量的大小
 * limit 表示缓冲区的当前终点，即可进行读写操作的极限位置
 * position 表示下一个需要读写操作的位置
 * mark 标记。使用可以reset将索引变成被标记的位置。
 * 二、缓存分类
 * (1) 直接缓冲区
 * 直接缓冲区是通过调用 ByteBuffer 类的 allocateDirect() 方法创建的，它将缓冲区建立在 JVM 以外的物理内存中。
 * 因此，直接缓冲区在进行 I/O 操作时可以避免在 JVM 堆和操作系统内核之间来回拷贝数据，从而提高了 I/O 操作的效率。
 * (2) 非直接缓冲区
 * 非直接缓冲区是通过调用 ByteBuffer 类的 allocate() 方法创建的，默认情况下分配在 JVM 堆中。
 * 因此，非直接缓冲区的 I/O 操作需要进行一次内存拷贝，即将数据从 JVM 堆中的缓冲区复制到内核中的缓冲区，
 * 然后进行 I/O 操作，再将数据从内核中的缓冲区复制回 JVM 堆中的缓冲区。
 * 总的来说，直接缓冲区的优点是可以提高 I/O 操作的效率，因为数据可以直接在内存和外部设备之间进行传输，而非直接缓冲区的优点是更容易创建和使用。
 *
 * 备注：读写共用索引，注意position位置。
 */
public class ExploreByteBuffer {
    public static void main(String[] args) {
        // quickStart();
        // markDemo();
        cacheKindDemo();
    }

    public static void cacheKindDemo(){
        // 非直接缓冲区
        ByteBuffer heapByteBuffer = ByteBuffer.allocate(5);
        // 直接缓冲区
        ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(5);
        // 需要注意的是，部分方法需要注意当前缓存的类型
        // hasArray 当前缓存是非直接内存才返回 true
        boolean heapFlag = heapByteBuffer.hasArray();
        boolean directFlag = directByteBuffer.hasArray();
        System.out.printf("HeapByteBuffer->%s DirectByteBuffer->%s\n", heapFlag, directFlag);

        for (int i = 0; i < 5; i++) {
            heapByteBuffer.put((byte)(i * 2));
            if (i == 3) {
                heapByteBuffer.mark();
            }
        }

        heapByteBuffer.reset();

        // 判断当前位置position到limit 之间是否还有元素
        boolean hasRemaining = heapByteBuffer.hasRemaining();
        if (hasRemaining) {
            // 获取position到limit之间的元素个数
            int remaining = heapByteBuffer.remaining();
            System.out.printf("position -> limit 存在的元素个数：%s\n", remaining);
        }

        // 获取缓存中的所有元素
        byte[] array = heapByteBuffer.array();
        System.out.printf(Arrays.toString(array));
    }

    public static void markDemo(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        for (int i = 0; i < 5; i++) {
            byteBuffer.put((byte)(i * 2));
            if (i == 3) {
                int position = byteBuffer.position();
                System.out.println("标记的position=" + position);
                byteBuffer.mark();
            }
        }

        int position = byteBuffer.position();
        System.out.println("数据填充完成之后的position=" + position);
        // 将position->被标记的位置
        byteBuffer.reset();

        position = byteBuffer.position();
        System.out.println("标记重制之后的position=" + position);
    }

    public static void quickStart(){
        // 当执行put|get->position位置会发生改变，自动加一
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        for (int i = 0; i < 5; i++) {
            byteBuffer.put((byte)(i * 2));
            int limit = byteBuffer.limit();
            int position = byteBuffer.position();
            int capacity = byteBuffer.capacity();
            System.out.printf("position=%s limit=%s capacity=%s\n", position, limit, capacity);
        }
        // 由于读写公用一个索引，需要进行读写索引反转。如果不执行该方法，会导致读写的时候会使用position=6进行读取数据，抛出异常。
        // flip -> 将position赋值给limit，表示当前需要读写的位置不能超过这个值。
        byteBuffer.flip();

        for (int i = 0; i < 5; i++) {
            byte data = byteBuffer.get();
            int limit = byteBuffer.limit();
            int position = byteBuffer.position();
            int capacity = byteBuffer.capacity();
            System.out.printf("position=%s limit=%s capacity=%s data=%s\n", position, limit, capacity, data);
        }
    }
}
