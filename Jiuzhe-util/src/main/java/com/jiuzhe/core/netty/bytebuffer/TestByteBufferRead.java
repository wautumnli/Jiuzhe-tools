package com.jiuzhe.core.netty.bytebuffer;

import java.nio.ByteBuffer;

import static com.jiuzhe.core.netty.bytebuffer.util.ByteBufferUtil.debugAll;

/**
 * bytebuffer一些用法
 * @author: wautumnli
 * @date: 2021-11-16 14:54
 **/
public class TestByteBufferRead {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();
        buffer.get(new byte[4]);
        debugAll(buffer);
        // 重设position为0
        buffer.rewind();
        debugAll(buffer);
        // mark & reset
        // mark记录一个position位置，reset是重置到mark到位置
        buffer.mark();
        buffer.reset();
    }
}
