package com.jiuzhe.core.netty.bytebuffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.jiuzhe.core.netty.bytebuffer.util.ByteBufferUtil.debugAll;

/**
 * 分散读
 * @author: wautumnli
 * @date: 2021-11-16 15:09
 **/
public class TestScatteringReads {

    private static final String FILE_NAME = "/Users/wautumnli/Desktop/mbp/work/Jiuzhe-tools/Jiuzhe-core/src/main/resources/word.txt";

    public static void main(String[] args) {
        try (FileChannel channel = new RandomAccessFile(FILE_NAME, "r").getChannel()) {
            ByteBuffer b1 = ByteBuffer.allocate(3);
            ByteBuffer b2 = ByteBuffer.allocate(3);
            ByteBuffer b3 = ByteBuffer.allocate(3);
            // 分散读
            channel.read(new ByteBuffer[]{b1, b2, b3});
            b1.flip();
            b2.flip();
            b3.flip();
            debugAll(b1);
            debugAll(b2);
            debugAll(b3);
        } catch (IOException e) {
        }
    }
}
