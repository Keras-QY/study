package com.kadima.AOP.proxy;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/14
 * @title：
 */
public class Teacher implements Worker {
    @Override
    public void work() {
        System.out.println("我是一名老师");
    }

    @Override
    public void sex() {
        System.out.println("我的性别是男");
    }
}
