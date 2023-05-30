package com.choice.netty.tcp.solve.delimiterbasedframedecoder;

public class EchoClientDemo {
    public static void main(String[] args) throws Exception {
        new EchoClient().bind(6666);
    }
}
