package com.kadima.singleton;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/8
 * @title：静态内部类
 * 把Singleton实例放到一个静态内部类中，这样可以避免了静态实例在Singleton类的加载阶段（类加载过程的其中一个阶段的，
 * 此时只创建了Class对象，关于Class对象可以看博主另外一篇博文， 深入理解Java类型信息(Class对象)与反射机制）就创建对象，
 * 毕竟静态变量初始化是在SingletonInner类初始化时触发的，并且由于静态内部类只会被加载一次，所以这种写法也是线程安全的。

 */
public class SingletonInner {

    private static class Holder{
        private static SingletonInner singleton = new SingletonInner();
    }

    public SingletonInner() {
    }

    public static SingletonInner getInstance(){
        return Holder.singleton;
    }
}
