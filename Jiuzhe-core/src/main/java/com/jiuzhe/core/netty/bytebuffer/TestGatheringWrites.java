package com.jiuzhe.core.netty.bytebuffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import static com.jiuzhe.core.netty.bytebuffer.util.ByteBufferUtil.debugAll;

/**
 * 分散写
 * @author: wautumnli
 * @date: 2021-11-16 15:13
 **/
public class TestGatheringWrites {

    private static final String FILE_NAME = "/Users/wautumnli/Desktop/mbp/work/Jiuzhe-tools/Jiuzhe-core/src/main/resources/word.txt";

    public static void main(String[] args) {
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("world");
        try (FileChannel channel = new RandomAccessFile(FILE_NAME, "rw").getChannel()) {
            debugAll(b1);
            debugAll(b2);
            // 分散写
            channel.write(new ByteBuffer[]{b1, b2});
        } catch (IOException e) {
        }
    }
}
