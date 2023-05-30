package com.choice.netty.tcp.solve.linebasedframedecoder;

/**
 * LineBasedFrameDecoder + StringDecode 组合是按照行切换的文本编辑器，用来支持TCP的粘包和拆包
 */
public class TimeServerDemo {
    public static void main(String[] args) throws Exception {
        new TimeServer().bind(6666);
    }
}
