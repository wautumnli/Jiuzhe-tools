package com.jiuzhe.core.netty.bytebuffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author: wautumnli
 * @date: 2021-11-16 15:42
 **/
public class TestFileChannelTransferTo {

    private static final String FROM_FILE = "/Users/wautumnli/Desktop/mbp/work/Jiuzhe-tools/Jiuzhe-core/src/main/resources/data.txt";

    private static final String TO_FILE = "/Users/wautumnli/Desktop/mbp/work/Jiuzhe-tools/Jiuzhe-core/src/main/resources/to.txt";

    public static void main(String[] args) {
        try (FileChannel from = new FileInputStream(FROM_FILE).getChannel();
             FileChannel to = new FileOutputStream(TO_FILE).getChannel()) {
            // 多次传输可以用这样方式进行
            long size = from.size();
            long left = size;
            while (left > 0) {
                // 速度快，底层使用零拷贝, 一次最多传输2g
                left -= from.transferTo(size - left, left, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
