package com.kadima.aop.compont;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/6
 * @title：@Aspect 把一个类定义为切面供容器读取
 */
@Component
@Aspect
public class ControllerAspect {


    @Around("@annotation(com.kadima.aop.annnotation.aopTest)")
    public Object befMethod(ProceedingJoinPoint point) throws Throwable {
        System.out.println("目标方法执行前");
        Object o = point.proceed();
        System.out.println("目标方法执行后");
        return null;
    }
}
