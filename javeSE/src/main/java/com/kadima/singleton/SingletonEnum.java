package com.kadima.singleton;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/8
 * @title：懒汉式、饿汉式、静态内部类、双重检验锁无法解决问题：序列化可能会单例模式：每次反序列化一个序列化的对象实例都会创建一个新的实例
 */

import java.io.Serializable;

/**
 * 对于序列化问题返回当前的instance
 */
class SingletonSer implements Serializable {

    private static SingletonSer instance = new SingletonSer();

    protected SingletonSer() {
    }
    private Object readResolve(){
        return instance;
    }
}

/**
 * 1:枚举序列化是由jvm保证的，每一个枚举类型和定义的枚举变量在JVM中都是唯一的，在枚举类型的序列化和反序列化上,
 * Java做了特殊的规定：在序列化时Java仅仅是将枚举对象的name属性输出到结果中，
 * 反序列化的时候则是通过java.lang.Enum的valueOf方法来根据名字查找枚举对象。
 * 同时，编译器是不允许任何对这种序列化机制的定制的并禁用了writeObject、readObject、readObjectNoData、writeReplace和readResolve等方法
 * 2:创建枚举实例只有编译器能够做到而已
 */
public enum  SingletonEnum {

    Singleton("only one");
    private String name;

    SingletonEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
