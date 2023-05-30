package com.choice.netty.review;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

public class NioServerSocketDemo {
    public static void main(String[] args) throws IOException {
        // create instance Selector
        Selector selector = SelectorProvider.provider().openSelector();
        // create instance ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // config channel is not blocked
        serverSocketChannel.configureBlocking(false);
        // bing local address and listen to port 6666
        serverSocketChannel.bind(new InetSocketAddress(6666));
        // register ServerSocketChannel to Selector and listen to Accept event
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // cycle listen to event
        for (;;) {
            // block ... until have event happened
            selector.select();

            // get all runnable event
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // get collection iterator
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            // cycle
            while (keyIterator.hasNext()) {
                // make sure channel is ok
                SelectionKey selectionKey = keyIterator.next();
                if (selectionKey.isValid()) {
                    if (selectionKey.isAcceptable()) {
                        // happened client connected
                        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = server.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    if (selectionKey.isReadable()) {
                        // happened i/o event
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        // create ByteBuffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int readBytes = client.read(buffer);
                        if (readBytes > 0) {
                            // reset index
                            buffer.flip();
                            byte[] bytes = new byte[buffer.remaining()];
                            // cache data to array
                            buffer.get(bytes);
                            System.out.printf("client send data: %s\n", new String(bytes));
                            // reply to client
                            client.write(ByteBuffer.wrap("I received".getBytes()));
                        } else {
                            System.out.println("channel is closed");
                            // it means the channel is closed
                            selectionKey.cancel();
                            client.close();
                        }
                    }
                }
                // remove from collection
                keyIterator.remove();
            }
        }
    }
}
