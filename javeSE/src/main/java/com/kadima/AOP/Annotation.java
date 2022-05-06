package com.kadima.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/6
 * @title：
 */

public class Annotation {




    public static void main(String[] args) {
        ProceedingJoinPoint proceedingJoinPoint = ProceedingJoinPoint.class.newInstance();
        proceedingJoinPoint.proceed()

    }
}
