package com.kadima.String;

public class StringTest {

    public static void main(String[] args) {
        String s1 = new String("hello world");
        String s2 = "hello world";

        System.out.println(s1 == s2);
        //intern返回常量池的引用
        String s3 = s1.intern();
        System.out.println(s2 == s3);

        String s4 = "hello world";
        System.out.println(s2 == s4);
        StringBuffer stringBuffer = new StringBuffer("ni hao");
        String substring1 = stringBuffer.substring(5);
        stringBuffer.append("21212");
        String s = "21212";
        String substring = s.substring(0);
        String s5 = s.concat("ni hao ");
        System.out.println(stringBuffer);

        StringBuilder stringBuilder = new StringBuilder("12121");
        StringBuilder stringBuilder1 = stringBuilder.append("212");


    }
}
