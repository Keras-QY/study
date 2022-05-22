package com.kadima.CodeOrder;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/22
 * @title：
 */

class animal{
    //非静态代码初始化
    {
        System.out.println("animal的非静态初始化快");
    }
    //无参构造器
    public animal(){
        System.out.println("animal无参的构造器方法执行");
    }

    //有参的构造器
    public animal(String name){
        //调用另一个的无参构造器
        this();
        System.out.println("animal有参的构造器方法执行：name="+name);
    }
}

class dog extends animal{
    //非静态代码初始化
    {
        System.out.println("dog的非静态初始化快");
    }
    //无参构造
    public dog(){

        System.out.println("dog的无参构造器方法执行");
    }

    //有参构造
    public dog(String name){
        //调用父类有参构造,没有显式指名会调用父类的无参构造方法
        super(name);
        System.out.println("dog的有参构造方法执行：name"+name);
    }
}
public class CodeOrderTest {
    public static void main(String[] args) {
        new dog("小强");
    }
}
