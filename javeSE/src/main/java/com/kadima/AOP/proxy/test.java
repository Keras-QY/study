package com.kadima.AOP.proxy;

import java.lang.reflect.Proxy;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/14
 * @title：
 */
public class test {
    public static void main(String[] args) {
        Teacher teacher = new Teacher();
        myInvocationHandle myInvocationHandle = new myInvocationHandle(teacher);
        Worker instance = (Worker)Proxy.newProxyInstance(teacher.getClass().getClassLoader(), teacher.getClass().getInterfaces(), myInvocationHandle);
        instance.sex();
        //System.out.println());
        //instance.work();
    }
}
