package com.choice.netty.http;

public class HttpServerDemo {
    public static void main(String[] args) throws Exception {
        new HttpServer().run(6666);
    }
}
