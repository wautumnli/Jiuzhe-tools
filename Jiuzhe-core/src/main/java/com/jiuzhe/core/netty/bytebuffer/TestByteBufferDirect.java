package com.jiuzhe.core.netty.bytebuffer;

import java.nio.ByteBuffer;

/**
 * HeapByteBuffer和DirectByteBuffer区别
 *
 * @author: wautumnli
 * @date: 2021-11-16 14:47
 **/
public class TestByteBufferDirect {
    public static void main(String[] args) {
        // class java.nio.HeapByteBuffer
        System.out.println(ByteBuffer.allocate(10).getClass());
        // class java.nio.DirectByteBuffer
        System.out.println(ByteBuffer.allocateDirect(10).getClass());
        // HeapByteBuffer 堆内存，速度较慢且会收到垃圾回收的影响
        // DirectByteBuffer 直接内存，速度快且不会收到垃圾回收的影响 (分配内存效率低，使用不当会造成内存泄漏)
    }
}
