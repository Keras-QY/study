package com.kadima.Static;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/6/5
 * @title：
 */
public class staticTest {

    public static void main(String[] args) throws InterruptedException {
        Animal animal = new Animal();
        animal.info();
        Animal cat = new cat();
        cat.info();

        /**
         * Thread.sleep()方法暂停时间的长短取决于具体的操作平台
         */
        long start = System.currentTimeMillis();
        Thread.sleep(1000);
        System.out.println(System.currentTimeMillis() - start);
    }
}

class Animal{
    public static void info(){
        System.out.println("animal的info方法");
    }
}

class cat extends Animal{
    public static void info(){
        System.out.println("cat的info方法");
    }
}
