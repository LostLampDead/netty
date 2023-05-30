package com.choice.netty.serialization.messagepack;

public class EchoClientDemo {
    public static void main(String[] args) throws Exception {
        new EchoClient().bind(6666);
    }
}
