package com.choice.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

/**
 * @see ReferenceCounted 引用计数，对池化至关重要
 */
public class ExploreReferenceCounted implements ReferenceCounted {

    public static void main(String[] args) {
        // ByteBuf extends ReferenceCounted
        ByteBuf byteBuf = Unpooled.buffer();
        int refCnt1 = ReferenceCountUtil.refCnt(byteBuf);
        System.out.printf("source: refCnt -> %s\n", refCnt1);
        byteBuf.writeBytes("it been so long".getBytes());
        // 手动新增计数；猜测在池化中也是类似
        byteBuf.retain();
        refCnt1 = ReferenceCountUtil.refCnt(byteBuf);
        System.out.printf("updated: refCnt -> %s\n", refCnt1);

        // there return false, but why... this fuck...
        // have chance see the source code
        boolean release = ReferenceCountUtil.release(byteBuf);
        System.out.printf("release=%s byteBuf.refCnt()=%s\n", release, byteBuf.refCnt());
    }

    private int refCnt = 1;

    @Override
    public int refCnt() {
        return refCnt;
    }

    @Override
    public ReferenceCounted retain() {
        refCnt++;
        return this;
    }

    @Override
    public ReferenceCounted retain(int increment) {
        refCnt += increment;
        return this;
    }

    @Override
    public ReferenceCounted touch() {
        return this;
    }

    @Override
    public ReferenceCounted touch(Object hint) {
        return this;
    }

    @Override
    public boolean release() {
        --refCnt;
        return refCnt == 0;
    }

    @Override
    public boolean release(int decrement) {
        refCnt -= decrement;
        return refCnt == 0;
    }
}
