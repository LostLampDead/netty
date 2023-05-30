package com.choice.netty.review;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * nio client instance
 */
public class NioClientSocketDemo {
    public static void main(String[] args) throws Exception {
        // create instance SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        // config channel not blocked
        socketChannel.configureBlocking(false);
        // try to connect server
        socketChannel.connect(new InetSocketAddress(6666));
        // wait for connect
        socketChannel.finishConnect();
        System.out.println("client connected ~");

        // client send data to server
        socketChannel.write(ByteBuffer.wrap("Hello Server".getBytes(StandardCharsets.UTF_8)));

        // release resource
        socketChannel.close();
    }
}
