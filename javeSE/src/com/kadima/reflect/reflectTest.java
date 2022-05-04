package com.kadima.reflect;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/4
 * @title：
 */
public class reflectTest {
    /**
     * 获取class文件的三种方式
     */

    public static void main(String[] args) throws ClassNotFoundException {

        /**
         * 根据包名获取
         */
        Class<?> aClass = Class.forName("com.kadima.reflect.studentDomain");
        System.out.println(aClass);

        /**
         * 根据对象获得
         */
        studentDomain studentdomain = new studentDomain();
        Class<? extends com.kadima.reflect.studentDomain> aClass1 = studentdomain.getClass();
        System.out.println(aClass1);

        /**
         * 通过类.calss获得
         */
        Class<studentDomain> aClass2 = studentDomain.class;
        System.out.println(aClass2);
    }

}
