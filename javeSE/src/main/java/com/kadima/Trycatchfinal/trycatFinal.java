package com.kadima.Trycatchfinal;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/6/5
 * @title：
 */
public class trycatFinal {
    public static void main(String[] args) throws IOException {
        /**
         * 关闭资源的擦操作不需要一定放在final里进行关闭，在java7：try关键字后紧跟一对圆括号，圆括号可以声明、初始化一个或多个资源，此处的资源
         * 是那些在程序结束时显式关闭的资源（比如数据库连接、网络连接等），try语句会在该语句结束时自动关闭这些资源
         * 关闭的资源必须满足：
         * 1.被自动关闭的资源必须实现closeable或AutoCloseable接口
         * 2.被自动关闭资源必须放在try语句后的圆括号中声明、初始化
         */
        try(
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("a.bin"));
        )
        {
            objectOutputStream.writeObject("hello world");
            objectOutputStream.flush();
        }
    }
}
