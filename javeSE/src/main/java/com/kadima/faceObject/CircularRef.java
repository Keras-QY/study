package com.kadima.faceObject;

/**
 * 一个类的实例持有当前类的其他实例时很有可能会形成递归调用--StackOverflow
 * @author qianyong
 * @company finedo.cn
 * @date 2022/6/5
 * @title：
 */
public class CircularRef {

    private CircularRef instance;
    private String name;

    //无参构造器
    public CircularRef() {
    }

    //有参构造器
    public CircularRef(String name) {
        instance = new CircularRef();
        instance.name = name;
    }

    //重写toString()方法
    @Override
    public String toString() {
        return "Instance = "+instance;
    }

    public static void main(String[] args) {
        CircularRef in = new CircularRef();
        CircularRef n2 = new CircularRef("测试");
        //互相引用
        in.instance = n2;
        n2.instance = in;
        System.out.println(in);
        System.out.println(n2);
    }
}
