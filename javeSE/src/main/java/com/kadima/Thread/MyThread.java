package com.kadima.Thread;

public class MyThread implements Runnable {

    private String name = "hello world";
    int i=5;
    @Override
    public void run() {
        while (i>0){
            name.substring(0, i);
            System.out.println(Thread.currentThread().getName()+name);
            i--;
        }
    }
}
