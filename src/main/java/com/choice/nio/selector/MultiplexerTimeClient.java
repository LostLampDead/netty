package com.choice.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class MultiplexerTimeClient implements Runnable {

    private SocketChannel socketChannel;

    public MultiplexerTimeClient(int port) {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            // 在非阻塞模式下，使用SocketChannel.connect 会立即返回结果，但是连接并不会立即成功，
            // 可以SocketChannel.finishChannel阻塞式等待连接成功
            socketChannel.connect(new InetSocketAddress(port));
            socketChannel.finishConnect();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            // SocketChannel#write返回值说明
            // 返回具体的int说明数据从缓存到通道之中了，但是使用write可能不能一次性将所有的数据都写入，一般需要多执行一次用于判断。
            // 返回为0表示当前缓存中已经没有数据写入了。
            // 返回-1表示当前连接已经关闭
            ByteBuffer byteBuffer = ByteBuffer.wrap("服务端你好，我是客户端".getBytes(StandardCharsets.UTF_8));
            while (byteBuffer.hasRemaining()) {
                int write = socketChannel.write(byteBuffer);
                if (write == -1) {
                    this.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.close();
        }
    }

    private void close(){
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
