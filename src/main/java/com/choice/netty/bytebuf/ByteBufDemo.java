package com.choice.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * ByteBuf通过两个位置指针来协助缓冲区的读写操作，读操作使用readerIndex，写操作使用writerIndex。
 * readerIndex和writerIndex的取值一开始都是0，随着数据的写入writerIndex会增加，读取数据会使readerIndex增加，但是它不会超过writerIndex。
 * 在读取之后，0～readerIndex就被视为discard的，调用discardReadBytes方法，可以释放这部分空间，它的作用类似ByteBuffer的compact方法。
 * ReaderIndex和writerIndex之间的数据是可读取的，等价于ByteBuffer position和limit之间的数据。
 * WriterIndex和capacity之间的空间是可写的，等价于ByteBuffer limit和capacity之间的可用空间。
 * 由于写操作不修改readerIndex指针，读操作不修改writerIndex指针，因此读写之间不再需要调整位置指针，
 * 这极大地简化了缓冲区的读写操作，避免了由于遗漏或者不熟悉flip（）操作导致的功能异常。
 * ps 使用write|read索引位置会发生改变，但是使用set|get不会
 * ps 在进行读操作的时候，对writerIndex需要格外注意，因为在执行readByte时候会判断 readerIndex > writerIndex - (读取的长度) ，满足条件会抛出异常
 */
public class ByteBufDemo {
    public static void main(String[] args) {
        // exploreChangeIndex();
        // exploreNoChangeIndex();
        // exploreDiscard();
        // exploreClear();
        // exploreMarkAndReset();
        // exploreDuplicate();
        // exploreCopy();
        // exploreSlice();
        // exploreToByteBuffer();
        exploreArrayOutIndex();
    }

    public static void exploreArrayOutIndex(){
        // 注意：使用set|get 不会导致索引变化，但是不会让数据动态扩展，注意数组越界异常。
        // 使用 write | read 会导致索引变化，但是数据支持动态扩展。
        ByteBuf byteBuf = Unpooled.buffer(1);
        byteBuf.capacity(5);
        // 不会发生数组越界异常
        int capacity = byteBuf.capacity() + 1;
        // 注意千万不要这么写
        /*
            for (int i = 0; i < byteBuf.capacity() + 1; i++) {
                byteBuf.writeByte(i);
            }
            由于支持动态扩展，byteBuf.capacity() 是一直在扩容的！
         */
        for (int i = 0; i < capacity; i++) {
            byteBuf.writeByte(i);
        }
        capacity = byteBuf.capacity() + 1;
        byteBuf.clear();
        for (int i = 0; i < capacity; i++) {
            // Exception in thread "main" java.lang.IndexOutOfBoundsException: index: 64, length: 1 (expected: range(0, 64))
            byteBuf.setByte(i, i);
        }
    }

    private static void exploreToByteBuffer(){
        ByteBuf byteBuf = Unpooled.buffer(5);
        for (int i = 0; i < 5; i++) {
            byteBuf.writeByte(i);
        }
        // 将ByteBuf转换成java.nio.ByteBuffer的方法有两个
        // （1）ByteBuffer nioBuffer（）：将当前ByteBuf可读的缓冲区转换成ByteBuffer，两者共享同一个缓冲区内容引用，对ByteBuffer的读写操作并不会修改原ByteBuf的读写索引。
        // 需要指出的是，返回后的ByteBuffer无法感知原ByteBuf的动态扩展操作。
        // （2）ByteBuffer nioBuffer（int index，int length）：将当前ByteBuf从index开始长度为length的缓冲区转换成ByteBuffer，
        // 两者共享同一个缓冲区内容引用，对ByteBuffer的读写操作并不会修改原ByteBuf的读写索引。需要指出的是，返回后的ByteBuffer无法感知原ByteBuf的动态扩展操作。”
        ByteBuffer buffer = byteBuf.nioBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.printf("复制后的ByteBuffer:%s\n", Arrays.toString(bytes));
        byteBuf.setByte(0, 8);
        System.out.printf("源数据：readerIndex=%s writerIndex=%s array=%s\n", byteBuf.readerIndex(), byteBuf.writerIndex(), Arrays.toString(byteBuf.array()));
        System.out.printf("复制后的ByteBuffer:%s\n", Arrays.toString(bytes));
    }

    private static void exploreSlice(){
        ByteBuf source = Unpooled.buffer(5);
        for (int i = 0; i < 5; i++) {
            source.writeByte(i);
        }
        System.out.printf("源数据：readerIndex=%s writerIndex=%s array=%s\n", source.readerIndex(), source.writerIndex(), Arrays.toString(source.array()));
        // 返回当前ByteBuf的可读子缓冲区，起始位置从readerIndex到writerIndex，返回后的ByteBuf与原ByteBuf共享内容，但是读写索引独立维护。
        // 该操作并不修改原ByteBuf的readerIndex和writerIndex。
        ByteBuf slice = source.slice();
        System.out.printf("目标：readerIndex=%s writerIndex=%s array=%s\n", slice.readerIndex(), slice.writerIndex(), Arrays.toString(slice.array()));

        // 校验共享
        source.setByte(0, 8);
        source.clear();
        System.out.printf("源数据：readerIndex=%s writerIndex=%s array=%s\n", source.readerIndex(), source.writerIndex(), Arrays.toString(source.array()));
        System.out.printf("目标：readerIndex=%s writerIndex=%s array=%s\n", slice.readerIndex(), slice.writerIndex(), Arrays.toString(slice.array()));

    }

    public static void exploreCopy(){
        ByteBuf source = Unpooled.buffer(5);
        for (int i = 0; i < 5; i++) {
            source.writeByte(i);
        }
        System.out.printf("源数据：readerIndex=%s writerIndex=%s array=%s\n", source.readerIndex(), source.writerIndex(), Arrays.toString(source.array()));
        // 复制一个新的ByteBuf对象，它的内容和索引都是独立的，复制操作本身并不修改原ByteBuf的读写索引。
        ByteBuf copy = source.copy();
        System.out.printf("目标：readerIndex=%s writerIndex=%s array=%s\n", copy.readerIndex(), copy.writerIndex(), Arrays.toString(copy.array()));

        // 校验共享
        source.setByte(0, 8);
        System.out.printf("源数据：readerIndex=%s writerIndex=%s array=%s\n", source.readerIndex(), source.writerIndex(), Arrays.toString(source.array()));
        System.out.printf("目标：readerIndex=%s writerIndex=%s array=%s\n", copy.readerIndex(), copy.writerIndex(), Arrays.toString(copy.array()));

    }

    private static void exploreDuplicate(){
        ByteBuf source = Unpooled.buffer(5);
        for (int i = 0; i < 5; i++) {
            source.writeByte(i);
        }
        System.out.printf("源数据：readerIndex=%s writerIndex=%s array=%s\n", source.readerIndex(), source.writerIndex(), Arrays.toString(source.array()));
        // 返回当前ByteBuf的复制对象，复制后返回的ByteBuf与操作的ByteBuf共享缓冲区内容，但是维护自己独立的读写索引。当修改复制后的ByteBuf内容后，
        // 之前原ByteBuf的内容也随之改变，双方持有的是同一个内容指针引用。
        ByteBuf duplicate = source.duplicate();
        System.out.printf("目标：readerIndex=%s writerIndex=%s array=%s\n", duplicate.readerIndex(), duplicate.writerIndex(), Arrays.toString(duplicate.array()));

        // 校验共享
        source.setByte(0, 8);
        System.out.printf("源数据：readerIndex=%s writerIndex=%s array=%s\n", source.readerIndex(), source.writerIndex(), Arrays.toString(source.array()));
        System.out.printf("目标：readerIndex=%s writerIndex=%s array=%s\n", duplicate.readerIndex(), duplicate.writerIndex(), Arrays.toString(duplicate.array()));
    }

    private static void exploreMarkAndReset(){
        ByteBuf byteBuf = Unpooled.buffer(5);
        System.out.printf("初始状态：readerIndex=%s writerIndex=%s\n", byteBuf.readerIndex(), byteBuf.writerIndex());
        for (int i = 0; i < 5; i++) {
            byteBuf.writeByte(i);
            if (i == 2) {
                byteBuf.markWriterIndex();
                System.out.printf("第%d次新增数据：对writerIndex=%s 进行标记\n", (i + 1), byteBuf.writerIndex());
            }
        }
        System.out.printf("writerIndex=%s\n", byteBuf.writerIndex());
        byteBuf.resetWriterIndex();
        System.out.printf("writerIndex=%s\n", byteBuf.writerIndex());
    }

    private static void exploreClear(){
        ByteBuf byteBuf = Unpooled.buffer(5);
        System.out.printf("初始状态：readerIndex=%s writerIndex=%s\n", byteBuf.readerIndex(), byteBuf.writerIndex());
        for (int i = 0; i < 5; i++) {
            byteBuf.writeByte(i);
            System.out.printf("第%d次新增数据：readerIndex=%s writerIndex=%s\n", (i + 1), byteBuf.readerIndex(), byteBuf.writerIndex());
        }
        // 将 readerIndex writerIndex -> 0
        byteBuf.clear();
    }

    // 执行discardReadBytes并不会清空数据，而是将readerIndex恢复成0，writeIndex-=readerIndex，下一次新增数据，原本的数据就会被覆盖
    private static void exploreDiscard(){
        ByteBuf byteBuf = Unpooled.buffer(5);
        System.out.printf("初始状态：readerIndex=%s writerIndex=%s\n", byteBuf.readerIndex(), byteBuf.writerIndex());
        for (int i = 0; i < 5; i++) {
            byteBuf.writeByte(i);
            System.out.printf("第%d次新增数据：readerIndex=%s writerIndex=%s\n", (i + 1), byteBuf.readerIndex(), byteBuf.writerIndex());
        }
        // 读写不共用索引，不需要切换
        for (int i = 0; i < 5; i++) {
            byte aByte = byteBuf.readByte();
            System.out.printf("第%d次获取数据%s：readerIndex=%s writerIndex=%s\n", (i + 1), aByte, byteBuf.readerIndex(), byteBuf.writerIndex());
        }
        // 丢弃读过的数组。0->readerIndex之间的数据
        byteBuf.discardReadBytes();
        byteBuf.readerIndex(0);
        System.out.printf("初始状态：readerIndex=%s writerIndex=%s 数据：%s\n", byteBuf.readerIndex(), byteBuf.writerIndex(), Arrays.toString(byteBuf.array()));
        for (int i = 0; i < 5; i++) {
            byteBuf.writeByte(i * 2);
            System.out.printf("第%d次新增数据：readerIndex=%s writerIndex=%s 数据：%s\n", (i + 1), byteBuf.readerIndex(), byteBuf.writerIndex(), Arrays.toString(byteBuf.array()));
        }
    }

    private static void exploreNoChangeIndex(){
        ByteBuf byteBuf = Unpooled.buffer(5);
        System.out.printf("初始状态：readerIndex=%s writerIndex=%s\n", byteBuf.readerIndex(), byteBuf.writerIndex());
        for (int i = 0; i < 5; i++) {
            byteBuf.setByte(i, i);
            System.out.printf("第%d次新增数据：readerIndex=%s writerIndex=%s\n", (i + 1), byteBuf.readerIndex(), byteBuf.writerIndex());
        }
        // 读写不共用索引，不需要切换
        for (int i = 0; i < 5; i++) {
            byte aByte = byteBuf.getByte(i);
            System.out.printf("第%d次获取数据%s：readerIndex=%s writerIndex=%s\n", (i + 1), aByte, byteBuf.readerIndex(), byteBuf.writerIndex());
        }
    }

    private static void exploreChangeIndex(){
        ByteBuf byteBuf = Unpooled.buffer(5);
        System.out.printf("初始状态：readerIndex=%s writerIndex=%s\n", byteBuf.readerIndex(), byteBuf.writerIndex());
        for (int i = 0; i < 5; i++) {
            byteBuf.writeByte(i);
            System.out.printf("第%d次新增数据：readerIndex=%s writerIndex=%s\n", (i + 1), byteBuf.readerIndex(), byteBuf.writerIndex());
        }
        // 读写不共用索引，不需要切换
        for (int i = 0; i < 5; i++) {
            byte aByte = byteBuf.readByte();
            System.out.printf("第%d次获取数据%s：readerIndex=%s writerIndex=%s\n", (i + 1), aByte, byteBuf.readerIndex(), byteBuf.writerIndex());
        }
    }
}
