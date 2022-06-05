package com.kadima.Reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 从Java SE2开始，就提供了四种类型的引用：强引用、软引用、弱引用和虚引用。Java中提供这四种引用类型主要有两个目的：
 * 第一是可以让程序员通过代码的方式决定某些对象的生命周期；
 * 第二是有利于JVM进行垃圾回收。
 */
public class refTest {


    public static void main(String[] args) {
        /**
         * 强引用，只要对象可达就不会回收，OOM也不会清除string
         */
        //String string = new String("hello world");
        //Object[] objects = new Object[Integer.MAX_VALUE];

        /**
         * 弱引用：软引用是用来描述一些有用但并不是必需的对象，在Java中用java.lang.ref.SoftReference类来表示。对于软引用关联着的对象，只有在内存不足的时候JVM才会回收该对象。
         * 因此，这一点可以很好地用来解决OOM的问题，并且这个特性很适合用来实现缓存：比如网页缓存、图片缓存等。
         */

        String s = new String("hello world");
        SoftReference<String> softReference = new SoftReference<>(s);
        s=null;
        String s1 = softReference.get();
        System.gc();
        System.runFinalization();
        System.out.println(s1);

        /**
         * 弱引用也是用来描述非必需对象的，当JVM进行垃圾回收时，无论内存是否充足，都会回收被弱引用关联的对象。在java中，用java.lang.ref.WeakReference类来表示。
         */
        String s2 = new String("hello world");
        WeakReference<String> weakReference = new WeakReference<>(s2);
        s2=null;
        System.gc();
        System.runFinalization();
        String s3 = weakReference.get();
        System.out.println(s3);
        /**
         * 虚引用和前面的软引用、弱引用不同，它并不影响对象的生命周期。在java中用java.lang.ref.PhantomReference类表示。如果一个对象与虚引用关联，则跟没有引用与之关联一样，在任何时候都可能被垃圾回收器回收。
         * 虚引用主要用来跟踪对象被垃圾回收的活动.
         * 虚引用必须和引用队列关联使用，当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会把这个虚引用加入到与之 关联的引用队列中。程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。
         * 如果程序发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象的内存被回收之前采取必要的行动
         */
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        String s4 = new String("hello   world 3");
        PhantomReference<String> phantomReference = new PhantomReference<>(s4, queue);
        //s4 = null;
        System.gc();
        System.runFinalization();
        System.out.println(queue.poll());
        long l = 1653900520000L;

        System.out.println(l / 3600000d);
        System.out.println(Math.round(l/3600000d*100)/100d);

        /**
         * 利用软引用和弱引用解决OOM问题：假如有一个应用需要读取大量的本地图片，如果每次读取图片都从硬盘读取，则会严重影响性能，
         * 但是如果全部加载到内存当中，又有可能造成内存溢出，此时使用软引用可以解决这个问题。
         * 设计思路是：用一个HashMap来保存图片的路径和相应图片对象关联的软引用之间的映射关系，在内存不足时，JVM会自动回收这些缓存图片对象所占用的空间，从而有效地避免了OOM的问题。
         */

    }
}
