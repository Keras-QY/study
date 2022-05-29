package com.kadima.StringTest;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class StringTest {
    public static void main(java.lang.String[] args) throws Exception {

        /*String s1 = new String("hello world");
        String s2 = "hello world";
        String s4 = "hello world";

        System.out.println(s1 == s2);
        //intern返回常量池的引用
        String s3 = s1.intern();
        System.out.println(s2 == s3);

        System.out.println(s2 == s4);
        StringBuffer stringBuffer = new StringBuffer("ni hao");
        stringBuffer.append("1212");
        StringBuilder builder = new StringBuilder();
        builder.append("2121");

        stringBuffer.append("21212");
        System.out.println(stringBuffer);

        StringBuilder stringBuilder = new StringBuilder("12121");
        StringBuilder stringBuilder1 = stringBuilder.append("212");*/

        List<String> list = Arrays.asList("A", "B", "C", "D");

        //StringBuilder
        StringBuilder stringBuilder = new StringBuilder();
        for (java.lang.String s : list) {
            stringBuilder.append(s).append("&");
        }
        stringBuilder.substring(0,stringBuilder.length() - 1);

        /*//StringJoin操作
        StringJoiner stringJoiner = new StringJoiner("&");
        for (java.lang.String s : list) {
            stringJoiner.add(s);
        }*/


       /* MyClassLoad myClassLoad = new MyClassLoad("D:\\study\\study\\javeSE\\target\\classes\\com\\kadima\\StringTest\\");
        Class<?> aClass = myClassLoad.loadClass("String");
        *//*String o = (String) aClass.newInstance();
        o.totring();*//*
        System.out.println(aClass);*/

        /*ClassLoader classLoader = MyClassLoad.class.getClassLoader();
        Class<?> loadClass = classLoader.loadClass("java.lang.String");
        System.out.println(loadClass);*/

        //
        //instance.toString();

    }
}

