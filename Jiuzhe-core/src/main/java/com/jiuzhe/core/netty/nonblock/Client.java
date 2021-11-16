package com.jiuzhe.core.netty.nonblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author: wautumnli
 * @date: 2021-11-16 16:29
 **/
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1", 8080));
        System.out.println("wait...");
    }
}
