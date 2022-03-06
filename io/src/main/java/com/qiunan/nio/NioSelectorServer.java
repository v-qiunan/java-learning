package com.qiunan.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioSelectorServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));
        // 设置 ServerSocketChannel 为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 打开 Selector 处理 channel，即创建 epoll
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务启动成功...");
        while (true) {
            // 阻塞等待需要处理的事件发生
            selector.select();
            // 获取 selector 中注册的全部事件的 SelectionKey 实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            // 遍历所有 selectionKey 对事件进行处理
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 如果 OP_ACCEPT 事件，则进行连接获取和事件注册
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功...");
                } else if (key.isReadable()) {
                    // 如果是 OP_READ 事件，则进行读取和打印
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(6);
                    int len = socketChannel.read(byteBuffer);
                    // 如果由数据，把数据打印出来
                    if (len > 0) {
                        System.out.println("接收到消息：" + new String(byteBuffer.array()));
                    } else if (len == -1) {
                        System.out.println("客户端断开连接...");
                        socketChannel.close();
                    }
                }
                // 从事件集合里删除本次处理的 key。 防止下次 select 重复处理
                iterator.remove();
            }
        }
    }
}
