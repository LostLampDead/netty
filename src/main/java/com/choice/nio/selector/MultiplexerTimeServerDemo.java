package com.choice.nio.selector;

public class MultiplexerTimeServerDemo {
    public static void main(String[] args) {
        new MultiplexerTimeServer(6666).run();
    }
}
