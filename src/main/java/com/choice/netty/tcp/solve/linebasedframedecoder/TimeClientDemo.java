package com.choice.netty.tcp.solve.linebasedframedecoder;

public class TimeClientDemo {
    public static void main(String[] args) throws Exception {
        new TimeClient().bind(6666);
    }
}
