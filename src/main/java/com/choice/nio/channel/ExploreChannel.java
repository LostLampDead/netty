package com.choice.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @see Channel 双向通道
 * 一、常见实现类
 * (1) FileChannel 主要用于对本地文件的IO操作
 * - read(ButeBuffer dst) 从通道读取数据并放到缓冲区中。
 * - write(ByteBuffer src) 将缓冲区的数据读取到通道中。
 * - transferFrom(ReadableByteBuffer src,long position, long count) 从目标通道复制到当前通道
 * - transferTo(long position, long count,WritableByteChannel target) 将当前通道复制到目标通道
 * (2) ServerSocketChannel -> ServerSocket
 * (3) SocketChannel -> Socket
 */
public class ExploreChannel {
    public static void main(String[] args) throws Exception {
        // fileChannelDemo();
        transferDemo();
    }

    public static void transferDemo() throws Exception {
        File sourceFile = new File("/Users/huangweihan/Project/me/netty/src/main/java/com/choice/nio/channel/source.txt");
        File targetFile = new File("/Users/huangweihan/Project/me/netty/src/main/java/com/choice/nio/channel/target.txt");
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
        FileChannel inFileChannel = fileInputStream.getChannel();
        FileChannel outFileChannel = fileOutputStream.getChannel();

        // 将当前通道复制到目标通道
        inFileChannel.transferTo(0, sourceFile.length(), outFileChannel);

        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void fileChannelDemo() throws Exception {
        File sourceFile = new File("/Users/huangweihan/Project/me/netty/src/main/java/com/choice/nio/channel/source.txt");
        File targetFile = new File("/Users/huangweihan/Project/me/netty/src/main/java/com/choice/nio/channel/target.txt");
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
        FileChannel inFileChannel = fileInputStream.getChannel();
        FileChannel outFileChannel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate((int) sourceFile.length());
        // 将通道的数据读取到缓存之中
        inFileChannel.read(buffer);
        System.out.printf("从通道中读取到的数据：%s\n", new String(buffer.array()));

        // 将缓冲中的数据写入到通道中
        ByteBuffer byteBuffer = ByteBuffer.wrap("这是要写入到通道的数据".getBytes(StandardCharsets.UTF_8));
        outFileChannel.write(byteBuffer);

        fileInputStream.close();
        fileOutputStream.close();
    }
}
