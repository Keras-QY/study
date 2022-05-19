package com.kadima.StringTest;

public class StringTest {
    public static void main(java.lang.String[] args) throws Exception {
        System.out.println("11111111");

   /*    StringTest s1 = new StringTest("hello world");
        StringTest s2 = "hello world";

        System.out.println(s1 == s2);
        //intern返回常量池的引用
        StringTest s3 = s1.intern();
        System.out.println(s2 == s3);

        StringTest s4 = "hello world";
        System.out.println(s2 == s4);
        StringBuffer stringBuffer = new StringBuffer("ni hao");
        StringTest substring1 = stringBuffer.substring(5);
        stringBuffer.append("21212");
        StringTest s = "21212";
        StringTest substring = s.substring(0);
        StringTest s5 = s.concat("ni hao ");
        System.out.println(stringBuffer);

        StringBuilder stringBuilder = new StringBuilder("12121");
        StringBuilder stringBuilder1 = stringBuilder.append("212");*/

        //List<java.lang.String> list = Arrays.asList("A", "B", "C", "D");

       /* //StringBuilder
        StringBuilder stringBuilder = new StringBuilder();
        for (java.lang.String s : list) {
            stringBuilder.append(s).append("&");
        }
        stringBuilder.substring(0,stringBuilder.length() - 1);

        //StringJoin操作
        StringJoiner stringJoiner = new StringJoiner("&");
        for (java.lang.String s : list) {
            stringJoiner.add(s);
        }
        System.out.println(stringJoiner);      //A&B&C&D

        StringJoiner stringJoiner1 = new StringJoiner("&", "[", "]");
        for (java.lang.String s : list) {
            stringJoiner1.add(s);
        }
        System.out.println(stringJoiner1.toString()); // [A&B&C&D]*/

        MyClassLoad myClassLoad = new MyClassLoad("D:\\study\\study\\javeSE\\target\\classes\\com\\kadima\\StringTest\\");
        Class<?> aClass = myClassLoad.loadClass("String");
        /*String o = (String) aClass.newInstance();
        o.totring();*/
        System.out.println(aClass);

        /*ClassLoader classLoader = MyClassLoad.class.getClassLoader();
        Class<?> loadClass = classLoader.loadClass("java.lang.String");
        System.out.println(loadClass);*/

        //
        //instance.toString();

    }
}

