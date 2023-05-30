package com.choice.netty.review;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JdkServerSocketDemo {

    public static void main(String[] args) throws Exception {
        // make thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try (
                ServerSocket serverSocket = new ServerSocket(6666)
        ) {
            System.out.println("server service started～");
            for (;;) {
                Socket socket = serverSocket.accept();
                System.out.println("one client come in～");
                executorService.execute(() -> {
                    reply(socket);
                });
            }
        } catch (Exception ex) {
            // do nothing
        }
    }

    private static void reply(Socket socket) {
        assert socket != null;
        Thread thread = Thread.currentThread();
        long threadId = thread.getId();
        String threadName = thread.getName();
        System.out.printf("current thread info show id=%d name=%s\n", threadId, threadName);
        byte[] bytes = new byte[1024];
        // 获取输入流
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            // 循环读取客户端信息
            for (;;) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.printf("client send data: [%s]\n", new String(bytes));
                    // 客户端回显
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write("I received".getBytes());
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            // do nothing...
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
