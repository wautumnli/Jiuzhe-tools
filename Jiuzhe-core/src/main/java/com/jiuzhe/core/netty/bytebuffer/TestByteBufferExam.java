package com.jiuzhe.core.netty.bytebuffer;

import java.nio.ByteBuffer;

import static com.jiuzhe.core.netty.bytebuffer.util.ByteBufferUtil.debugAll;

/**
 * 解决黏包，半包问题
 *
 * @author: wautumnli
 * @date: 2021-11-16 15:21
 **/
public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello world\nI.m wanqiuli\nHo".getBytes());
        split(source);
        source.put("w are you\n".getBytes());
        split(source);
    }

    public static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // 根据每次消息的结束符去处理每条消息
            if (source.get(i) == '\n') {
                int len = i + 1 - source.position();
                ByteBuffer buffer = ByteBuffer.allocate(len);
                for (int j = 0; j < len; j++) {
                    buffer.put(source.get());
                }
                debugAll(buffer);
            }
        }
        source.compact();
    }
}
