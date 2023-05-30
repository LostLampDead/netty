package com.choice.nio.buffer;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @see MappedByteBuffer
 * 可以让文件直接在内存中进行修改，操作系统不需要进行拷贝一次
 */
public class ExploreMappedByteBuffer {
    public static void main(String[] args) throws Exception {
        String fileName = "/Users/huangweihan/Project/me/netty/src/main/java/com/choice/nio/channel/source.txt";
        RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        // 参数一：读写模式 FileChannel.MapMode
        // 参数二：可以直接修改的起始位置
        // 参数三：是映射到内存的大小，非索引位置。即需要将多少个字节映射到内存中。
        // MappedByteBuffer -> DirectByteBuffer
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        // 在idea中没有立即发生变化，可以打开本地文件，会发现文件中的内容发生改变
        mappedByteBuffer.put(0, (byte)'h');
        mappedByteBuffer.put(3, (byte) '2');

        randomAccessFile.close();
    }
}
