package com.qiunan.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true) {
            System.out.println("等待连接中...");
            // 阻塞方法
            Socket clientSocket = serverSocket.accept();
            System.out.println("客户端连接上了...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        handler(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    private static void handler(Socket clientSocket) throws IOException{
        byte[] bytes = new byte[1024];
        System.out.println("准备 read...");
        int read = clientSocket.getInputStream().read(bytes);
        System.out.println("read 完毕...");
        if (read != -1) {
            System.out.println("接受到客户端的数据: " + new String(bytes, 0, read));
        }
        System.out.println("end");
    }
}
