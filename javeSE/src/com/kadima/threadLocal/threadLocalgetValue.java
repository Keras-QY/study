package com.kadima.threadLocal;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/3
 * @title：
 */
public class threadLocalgetValue {

    /**
    * @Author: qianyong
    * @Description: 获取threadLocal中的值
    * @DateTime: 2022/5/3 23:36
    * @Params:
    * @Return
    */
    public void getValue(ThreadLocal threadLocal) {
        System.out.println("当前的线程名字："+Thread.currentThread().getName()+"获取的值为："+threadLocal.get());
        threadLocal.remove();

    }
}
