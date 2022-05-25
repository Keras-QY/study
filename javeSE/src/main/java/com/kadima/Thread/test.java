package com.kadima.Thread;

public class test {

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        new Thread(myThread,"小明").start();
        new Thread(myThread,"小红").start();


    }
}
