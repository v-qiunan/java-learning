package com.qiunan.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) {
        // 创建两个线程组 bossGroup 和 workGroup ，含有的子线程 NioEventLoop 的个数默认为 cpu 核数的两倍
        // bossGroup 知识处理连接请求，真正的和客户端业务处理，会交给 workGroup 完成
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(10);
        try {
            //创建服务器端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 使用链式编程来配置参数
            bootstrap.group(bossGroup, workGroup)
                    // 使用 NioServerSocketChannel 作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    //初始化服务器连接队列大小，服务器处理客户端连接请求顺序处理的，所以同一时间只能处理一个客户端
                    // 多个客户端同时来的时候，服务器将不能处理的客户端连接请求放在队列中等待处理
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new NettyServerHandler());
            System.out.println("netty server start");
            // 绑定一个端口并且同步，生成了一个 ChannelFuture 异步对象，通过 isDone() 等方法可以判断异步事件的执行情况
            // 启动服务器（并绑定端口），bind 是异步操作，sync 方法是等待异步操作执行完毕
            ChannelFuture cf = bootstrap.bind(9002).sync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
