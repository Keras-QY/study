package com.kadima.aop.compont;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/6
 * @title：@Aspect 把一个类定义为切面供容器读取
 * 多个切面类，执行的先后顺序根据@order(int value) 值越小越先执行
 */
@Order(1)
@Component
@Aspect
public class ControllerAspect {


    @Before("@annotation(com.kadima.aop.annnotation.aopTest)")
    public void befmethod(){
        System.out.println("before==========目标方法执行前执行");
    }

    /**
     * 方法别拦截后，首先会执行@around bef ->@before  ->  method -> @around after  -> @after -> @afterReturning
     * @param point
     * @throws Throwable
     */
    @Around("@annotation(com.kadima.aop.annnotation.aopTest)")
    public void befMethod(ProceedingJoinPoint point) throws Throwable {
        System.out.println("around====目标方法执行前");
        Object o = point.proceed();
        System.out.println("around====目标方法执行后");//有异常就不执行了
    }

    @AfterReturning("@annotation(com.kadima.aop.annnotation.aopTest)")
    public void afterRet(){
        System.out.println("afterRet==========");//方法执行返回结果之前执行
    }

    @AfterThrowing("@annotation(com.kadima.aop.annnotation.aopTest)")
    public void afterThrow(){
        System.out.println("有异常执行===========");//有异常才会执行
    }

    @After("@annotation(com.kadima.aop.annnotation.aopTest)")
    public void aftmethod(){
        System.out.println("after==========目标方法执行后执行");//不管有没有异常方法都会执行
    }

}
