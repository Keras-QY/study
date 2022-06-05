package com.kadima.Thread;

import lombok.var;
import org.aspectj.weaver.ast.Var;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/31
 * @title：
 */
public class threeCreateThread {

    public static void main(String[] args) {
        new myThread().start();
        new Thread(new myThread2()).start();
    }
}

/**
 * 第一种方式：继承thread类来创建线程类，重写run方法作为线程执行体
 * 此种效果最差：无法再继承其他类
 * 因为每条线程都是thread的子类，因此可以将多条线程的执行流代码与业务数据分离
 */
class myThread extends Thread{
    @Override
    public void run() {
        System.out.println("线程的run方法执行");
    }
}

/**
 * 第二种方式：实现runable接口来创建线程类，重写run方法作为线程执行体
 * 将实现类放入Thread构造器中，start执行
 *
 */
class myThread2 implements Runnable{
    @Override
    public void run() {
        System.out.println("通过实现runable接口重写run方法");
    }
}
