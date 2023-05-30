package com.choice.nio.selector;

public class MultiplexerTimeClientDemo {
    public static void main(String[] args) {
        new MultiplexerTimeClient(6666).run();
    }
}
