package com.jiuzhe.core.netty.bytebuffer;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Channel和ByteBuffer使用
 *
 * @author: wautumnli
 * @date: 2021-11-16 13:56
 **/
@Slf4j
public class TestByteBuffer {

    private static final String FILE_NAME = "/Users/wautumnli/Desktop/mbp/work/Jiuzhe-tools/Jiuzhe-core/src/main/resources/data.txt";

    public static void main(String[] args) {
        // Java 7以上写法 try with resource，或使用try.. catch.. finally
        try (FileChannel channel = new FileInputStream(FILE_NAME).getChannel()) {
            // 准备一个10字节大小的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                // 从channel读取数据, 向buffer写入
                int len = channel.read(buffer);
                log.info("读取到的字节: {}", len);
                // channel.read()如果全部读取完，会返回-1
                if (len == -1) {
                    break;
                }
                // 切换到buffer到读模式
                buffer.flip();
                // 检查buffer里是否还有数据
                while (buffer.hasRemaining()) {
                    // 一次读一个字节
                    byte b = buffer.get();
                    System.out.println((char) b);
                }
                // 切换到写模式
                buffer.clear();
                // 使用buffer.clear()会直接清除当前buffer的所有数据，使用buffer.compact()会清除已读数据
                // buffer.compact();
            }
        } catch (IOException e) {
        }
    }
}
