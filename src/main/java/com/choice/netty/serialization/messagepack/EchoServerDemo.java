package com.choice.netty.serialization.messagepack;

public class EchoServerDemo {
    public static void main(String[] args) throws Exception {
        new EchoServer().bind(6666);
    }
}
