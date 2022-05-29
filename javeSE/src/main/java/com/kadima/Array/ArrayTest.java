package com.kadima.Array;


/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/21
 * @title：
 */
public class ArrayTest {

    public static void main(java.lang.String[] args) {

        //静态初始化方式初始化第一个数组
        java.lang.String[] strings = {
                "疯狂java讲义",
                "老人与海",
                "疯狂Python讲义",
                "疯狂XML讲义"
        };

        //动态初始化
        String[] strings1 = new String[5];
        System.out.println(strings1.length);

        Student[] students = new Student[2];
        System.out.println(students[0].getAge());
        System.out.println(students[1]);

    }
}


