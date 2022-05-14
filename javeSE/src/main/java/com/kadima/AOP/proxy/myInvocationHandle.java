package com.kadima.AOP.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/14
 * @title：
 */
public class myInvocationHandle implements InvocationHandler {
    private Object target;

    public myInvocationHandle(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理对象执行操作前");
        Object o = method.invoke(target,args);
        System.out.println("代理对象执行操作后");
        return o;
    }
}
