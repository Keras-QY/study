package com.kadima.singleton;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/8
 * @title：懒汉式：由于synchronized的存在，效率低下
 */
public class SingletonLazy {

    private static volatile SingletonLazy instance;

    public SingletonLazy() {
    }

    public static synchronized SingletonLazy getInstance(){
        if (instance == null){
            instance = new SingletonLazy();
        }
        return instance;
    }
}
/**
 * 改进版
 * 关键字volatile有两层语义。
 * 第一层语义是可见性，可见性是指在一个线程中对该变量的修改会马上由工作内存（Work Memory）写回主内存（Main Memory），
 * 所以其它线程会马上读取到已修改的值，关于工作内存和主内存可简单理解为高速缓存（直接与CPU打交道）和主存（日常所说的内存条），
 * 注意工作内存是线程独享的，主存是线程共享的。
 * volatile的第二层语义是禁止指令重排序优化，我们写的代码（特别是多线程代码），由于编译器优化，在实际执行的时候可能与我们编写的顺序不同。
 * 编译器只保证程序执行结果与源代码相同，却不保证实际指令的顺序与源代码相同，这在单线程并没什么问题，然而一旦引入多线程环境，
 * 这种乱序就可能导致严重问题。volatile关键字就可以从语义上解决这个问题，值得关注的是volatile的禁止指令重排序优化功能在Java 1.5后才得以实现，
 * 因此1.5前的版本仍然是不安全的，即使使用了volatile关键字
 */
class Singleton{
    private static volatile Singleton instance;

    public Singleton() {
    }

    public static Singleton getInstance(){
        if (instance == null){
            synchronized (Singleton.class){
                if (instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
