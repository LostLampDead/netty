package com.choice.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * ByteBufUtil提供了用于操作ByteBuf的静态的辅助方法。因为这个API是通用的，并且和池化无关，所以这些方法已然在分配类的外部实现。
 */
public class ExploreByteBufUtil {
    public static void main(String[] args) {
        ByteBuf source = Unpooled.wrappedBuffer("Hello".getBytes(StandardCharsets.UTF_8));
        ByteBuf shadow = Unpooled.wrappedBuffer("Hello".getBytes(StandardCharsets.UTF_8));
        ByteBuf target = Unpooled.wrappedBuffer("World".getBytes(StandardCharsets.UTF_8));
        String hexedDump = ByteBufUtil.hexDump(source);
        System.out.printf("以16进制打印ByteBuf:%s\n", hexedDump);
        System.out.printf("source与target比较结果：%s\n", ByteBufUtil.equals(source, target)); // false
        System.out.printf("source与shadow比较结果：%s\n", ByteBufUtil.equals(source, shadow)); // true
    }
}
