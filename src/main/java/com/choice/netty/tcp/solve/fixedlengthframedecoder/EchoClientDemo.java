package com.choice.netty.tcp.solve.fixedlengthframedecoder;

public class EchoClientDemo {
    public static void main(String[] args) throws Exception {
        new EchoClient().bind(6666);
    }
}
