package com.kadima.Trycatchfinal;

import java.io.FileNotFoundException;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/6/5
 * @title：
 */
public class finalTest {

    public static void main(String[] args) {
        int test = test();
        System.out.println(test);
    }

    /**
     * 当程序执行try块、catch块遇到throw语句中，throw语句会导致该方法立即结束，系统执行throw语句不会立即抛出异常，
     * 而是寻找该异常处理流程中是否包含finally。如果没有finally块，程序立即抛出异常、中止方法；如果有finally，执行finally块，
     * 只有当finally块执行完后才会再次调回来抛出异常。如果有finally中有return语句，会直接返回，不会抛出异常。
     * @return
     */
    public static int test(){
        int count = 5;
        try {
            throw new RuntimeException();
            //int a=20/0;
            //return count++;

            /**
             * 捕获异常的顺序与if...else一样，先捕获小异常，在捕获大异常
             */
        } catch (FileNotFoundException e){
            System.out.println();

        }finally {
            System.out.println("final代码块被执行");
            //return count++;
        }
    }
}
