package com.kadima.singleton;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/8
 * @title：饿汉式:优点：创建简单  缺点：该单例涉及资源较多，创建比较耗时间
 */
public class SingletonHungry {

    private static SingletonHungry instance = new SingletonHungry();

    private SingletonHungry() {
    }

    public static SingletonHungry getInstance(){
        return instance;
    }
}
