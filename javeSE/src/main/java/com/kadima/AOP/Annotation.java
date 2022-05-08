package com.kadima.AOP;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/6
 * @title：
 */

public class Annotation {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        ProceedingJoinPoint proceedingJoinPoint = ProceedingJoinPoint.class.newInstance();

    }
}
