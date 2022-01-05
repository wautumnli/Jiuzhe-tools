package com.jiuzhe.core.netty.noselector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.jiuzhe.core.netty.bytebuffer.util.ByteBufferUtil.debugRead;

/**
 * @author: wautumnli
 * @date: 2021-11-16 16:23
 **/
@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {
        // 服务端
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置为非阻塞模式，默认是阻塞模式
        ssc.configureBlocking(false);
        // 绑定端口
        ssc.bind(new InetSocketAddress(8080));
        // byteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 所有建立连接的通道加入进来
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // accept()建立连接，阻塞方法
            log.info("start-up..");
            SocketChannel sc = ssc.accept();
            log.info("connect..");
            // 设置非阻塞模式时如果没有连接会返回0
            if (sc != null) {
                // 设置非阻塞
                sc.configureBlocking(false);
                channels.add(sc);
            }
            for (SocketChannel channel : channels) {
                log.info("wait read..");
                // 没有读取到数据会返回0
                if (channel.read(buffer) != 0) {
                    buffer.flip();
                    debugRead(buffer);
                    buffer.clear();
                }
            }
        }
    }
}
