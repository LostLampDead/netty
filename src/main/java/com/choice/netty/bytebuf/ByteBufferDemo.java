package com.choice.netty.bytebuf;

import java.nio.ByteBuffer;

/**
 * 注意：读写会共用一个索引，每次执行put|get会改变position位置。
 * 比如向ByteBuffer中使用put新增了5个数据，直接执行get获取数据是获取不到的，因为当前position=4+1=5
 * 应该使用flip进行一个状态恢复
 */
public class ByteBufferDemo {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String message = "it so long time";
        byteBuffer.put(message.getBytes());
        // ByteBuffer.flip() limit置为position, 再将position置为0，
        byteBuffer.flip();
        // ByteBuffer.remaining() 获取position到limit之间元素的个数
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes));
    }
}
