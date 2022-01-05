package com.jiuzhe.core.netty.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.jiuzhe.core.netty.bytebuffer.util.ByteBufferUtil.debugAll;

/**
 * String转byte
 * @author: wautumnli
 * @date: 2021-11-16 15:00
 **/
public class TestByteBufferString {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 字符串直接getBytes() 不会自动切换到读模式，继续是写模式
        buffer.put("hello".getBytes());
        debugAll(buffer);
        // 使用Charsets会自动切换到读模式
        ByteBuffer hello = StandardCharsets.UTF_8.encode("hello");
        debugAll(hello);
        // wrap会自动切换到读模式
        ByteBuffer wrap = ByteBuffer.wrap("hello".getBytes());
        debugAll(hello);
    }
}
