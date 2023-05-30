package com.choice.nio.buffer;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 读写操作可以支持多个buffer
 * Scattering 将数据写入到buffer中。可以采用buffer数组，依次写入->分散
 * Gathering 从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ExploreScatteringAndGathering {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        ByteBuffer buffer1 = ByteBuffer.allocate(3);
        ByteBuffer buffer2 = ByteBuffer.allocate(5);
        ByteBuffer[] buffers = new ByteBuffer[]{buffer1, buffer2};

        SocketChannel socketChannel = serverSocketChannel.accept();

        for (;;) {
            // 分散：将客户端发送过来的数据读取到buffer数组中
            long readLength = socketChannel.read(buffers);
            if (readLength == -1) {
                break;
            }
            System.out.println("接收到的消息长度" + readLength);
            Arrays.stream(buffers)
                    .map(buffer -> "position=" + buffer.position() + " limit=" + buffer.limit())
                    .forEach(System.out::println);
            System.out.println("===============");

            // 回显客户端
            Arrays.stream(buffers).forEach(Buffer::flip);
            // 聚合：将buffer数组中数据回显到客户端
            socketChannel.write(buffers);
            Arrays.stream(buffers).forEach(Buffer::clear);
        }
    }
}
