package com.choice.netty.serialization.messagepack;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.util.ArrayList;
import java.util.List;

/**
 * MessagePack是一种轻量级的二进制数据序列化格式，旨在提供比JSON等其他文本格式更快、更小、更简单的数据交换方案。
 * 它的设计初衷是为了在不同编程语言之间更快地传输数据，并且更好地利用网络带宽和存储空间。
 * 与JSON、XML等其他文本格式不同，MessagePack使用二进制格式来表示数据，这意味着它不需要将数据序列化为字符串，从而避免了字符串解析和编码的开销。
 * 此外，MessagePack采用了紧凑的编码方式，可以更有效地利用带宽和存储空间。因此，它比JSON和XML等其他文本格式更快、更小、更节省资源。
 * MessagePack具有以下特点：
 * - 体积小：MessagePack的编码方式非常紧凑，可以将数据序列化为更小的二进制格式，从而减少传输和存储开销。
 * - 速度快：由于MessagePack不需要将数据序列化为字符串，从而避免了字符串解析和编码的开销，因此它的解析速度比JSON等其他文本格式更快。
 * - 多语言支持：MessagePack支持多种编程语言，包括Java、C++、Python、Ruby等。
 * - 可扩展性：MessagePack可以支持自定义类型，并且可以通过使用扩展标记来支持各种类型。
 * 总之，MessagePack是一种非常有用的数据序列化格式，特别是在需要高效的数据传输和存储时。
 */
public class QuickStartDemo {
    public static void main(String[] args) throws Exception {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        MessagePack messagePack = new MessagePack();
        byte[] raw = messagePack.write(list);

        List<String> dst = messagePack.read(raw, Templates.tList(Templates.TString));
        System.out.println(dst);
    }
}
