package com.kadima.Enum;

import java.util.Arrays;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/8
 * @title：
 */
public class EnumDemo {

    public static void main(String[] args) {

        /**
         * values():获取枚举类中的所有变量，并作为数组返回
         */
        Day[] values = Day.values();
        System.out.println(Arrays.toString(values));
        /**
         * 根据名称获取枚举变量
         */
        System.out.println("========="+Day.valueOf("Monday"));
        /**
         * 向上转型Enum，无法调用values，没有此方法，可以通过反射获取
         */
        Enum e = Day.Monday;
        //获取class对象
        Class aClass = e.getDeclaringClass();
        if (aClass.isEnum()) {
            Object[] enumConstants = aClass.getEnumConstants();
            System.out.println(Arrays.toString(enumConstants));
        }

    }

}
