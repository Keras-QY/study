package com.kadima.ClassLoader;


import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class test {

    public static void main(String[] args) throws Exception {
        /**
         * 热部署
         */
        new Thread(() -> {
            while (true) {
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                compiler.run(null, null, null, "D:\\company\\myself\\study\\String\\testString\\src\\com\\load\\StringTest.java");
                MyClassLoad myClassLoad = new MyClassLoad("D:\\company\\myself\\study\\String\\testString\\src\\");
                Class<?> aClass = null;
                try {
                    aClass = myClassLoad.loadClass("com.load.StringTest");
                    /**
                     * 反射调用方法
                     */
                    Method show = aClass.getDeclaredMethod("toStings");
                    Object o = aClass.newInstance();
                    Object invoke = show.invoke(o);
                    System.out.println(invoke);
                    Thread.sleep(1000);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }  catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }


        }).start();

        /*//加载自定义class文件
        MyClassLoad myClassLoad = new MyClassLoad("D:\\company\\myself\\study\\String\\testString\\src\\");
        Class<?> stringClass = myClassLoad.findClass("java.lang.String");
        System.out.println(stringClass);*/


    }
}