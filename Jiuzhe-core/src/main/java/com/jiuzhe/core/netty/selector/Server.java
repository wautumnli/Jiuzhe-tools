package com.jiuzhe.core.netty.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.jiuzhe.core.netty.bytebuffer.util.ByteBufferUtil.debugRead;

/**
 * @author: wautumnli
 * @date: 2021-11-16 16:56
 **/
@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));

        // 事件发生时，可以通过selection key获取事件类型和事件
        Selector selector = Selector.open();
        // accept接受连接时，connect建立连接时， read读时， write写时
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // ServerSocketChannel只关注接受事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);

        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        List<SocketChannel> channels = new ArrayList<>();

        while (true) {
            // 无事件阻塞，有事件执行
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                // 已处理过的SelectKey删除
                iter.remove();
                // selectKey 接受处理
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    // 设置非阻塞
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);
                    // 对读感兴趣
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.info("sc: {}", sc);
                    // selectKey 可读处理
                } else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        // 正常断开连接会返回-1，此时需要关闭管道
                        int read = channel.read(buffer);
                        if (read == -1) {
                            key.channel();
                        } else {
                            buffer.flip();
                            // debugRead(buffer);
                            System.out.println(Charset.defaultCharset().decode(buffer));
                        }
                    } catch (IOException e) {
                        // 异常断开连接，关闭管道
                        e.printStackTrace();
                        key.channel();
                    }
                }
            }
        }
    }
}
