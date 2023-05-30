package com.choice.netty.tcp.solve.delimiterbasedframedecoder;

public class EchoServerDemo {
    public static void main(String[] args) throws Exception {
        new EchoServer().bind(6666);
    }
}
