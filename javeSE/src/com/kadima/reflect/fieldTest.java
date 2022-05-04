package com.kadima.reflect;

import java.lang.reflect.Field;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/4
 * @title：
 */
public class fieldTest {

    public static void main(String[] args) throws NoSuchFieldException {
        Class<studentDomain> studentDomainClass = studentDomain.class;
        /**
         * 获取私有属性
         */
        Field field = studentDomainClass.getDeclaredField("name");
        /**
         * 获取类中所有的属性
         */
        Field[] fields = studentDomainClass.getDeclaredFields();
        for (Field field1 : fields) {
            field1.setAccessible(true);
            System.out.println("获取到的属性名："+field1);
        }
        /**
         * 取消反射是Java的访问检查
         */
        //System.out.println(field);
    }
}
