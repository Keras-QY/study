package com.kadima.threadLocal;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/3
 * @title：ThreadLocal则用于线程间的数据隔离。
 * ThreadLocal的应用场合:最适合的是按线程多实例（每个线程对应一个实例）的对象的访问，并且这个对象很多地方都要用到
 * 使用ThreadLocal时会发生内存泄漏的前提条件：
 * ①ThreadLocal引用被设置为null，且后面没有set，get,remove操作。
 * ②线程一直运行，不停止。（线程池）
 * ③触发了垃圾回收。（Minor GC或Full GC）
 * 我们只要破坏其中一个条件就可以避免内存泄漏，单但为了更好的避免这种情况的发生我们使用ThreadLocal时遵守以下两个小原则:
 * ①ThreadLocal申明为private static final。
 *    Private与final 尽可能不让他人修改变更引用，
 *    Static 表示为类属性，只有在程序结束才会被回收。
 * ②ThreadLocal使用后务必调用remove方法。
 */
public class threadLocal {

    /**
     *定义全局变量来存放线程需要的变量,ThreadLocal是线程Thread中属性threadLocals的管理者
     */
    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();


    public static void main(String[] args) throws NoSuchFieldException {


        threadLocal.set(255);
        for (int i = 0; i < 2; i++) {
            new Thread( new Runnable() {

                @Override
                public void run() {
                    Double v = Math.random() * 10;
                    threadLocal.set(v.intValue());
                    threadLocalgetValue threadLocalgetValue = new threadLocalgetValue();
                        threadLocalgetValue.getValue(threadLocal);

                    System.out.println("当前的线程名字："+Thread.currentThread().getName()+"获取的值为："+threadLocal.get());
                }
            }).start();
        }

        System.out.println("当前的线程名字："+Thread.currentThread().getName()+"获取的值为："+threadLocal.get());
        threadLocalgetValue threadLocalgetValue = new threadLocalgetValue();
        threadLocalgetValue.getValue(threadLocal);
    }

}
