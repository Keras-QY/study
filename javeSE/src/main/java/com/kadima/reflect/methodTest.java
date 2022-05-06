package com.kadima.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/4
 * @title：
 */
public class methodTest {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        /**
         * 获取class文件
         */
        Class<studentDomain> studentDomainClass = studentDomain.class;
        /**
         * 获取反射
         * param：方法名
         * param：参数类型
         */
        Method method = studentDomainClass.getMethod("setName", String.class);
        /**
         * param:实列对象
         * param:参数值
         */
        Object invoke = method.invoke(studentDomainClass.newInstance(), "钱勇");
        System.out.println("setName方法的返回值：" + invoke);

        /**
         * 反射获取所有方法
         */
        Method[] methods = studentDomainClass.getMethods();
        for (Method method1 : methods) {
            System.out.println(method1);
        }
        System.out.println("=============================================");
        /**
         * 获取所有的私有方法
         */
        Method[] declaredMethods = studentDomainClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.setAccessible(true);
            System.out.println(declaredMethod);

        }
    }
}
