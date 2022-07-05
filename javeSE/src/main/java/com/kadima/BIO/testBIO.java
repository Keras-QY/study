package com.kadima.BIO;

import org.aspectj.weaver.ast.Var;
import org.springframework.core.NamedThreadLocal;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class testBIO {

    public static void main(String[] args) throws IOException {

        //创建线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                10,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(20),
                new ThreadPoolExecutorFactoryBean(),
                new ThreadPoolExecutor.AbortPolicy());
        //建立socket链接88
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("服务启动了");
        while (true){

            final Socket accept = serverSocket.accept();
            System.out.println("连接到一个客户端");
            threadPoolExecutor.execute(()-> {
                try {
                    handler(accept);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void handler(Socket socket) throws IOException {
        System.out.println(Thread.currentThread().getId()+"====="+Thread.currentThread().getName());
        byte[] bytes = new byte[1024];
        InputStream inputStream = socket.getInputStream();
        while (true) {
            int i = inputStream.read(bytes);
            if (i != -1) {
                System.out.println(new String(bytes,0,i));

            }else {
                break;
            }
        }

    }
}
