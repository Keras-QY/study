package com.kadima.aop.compont;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/6
 * @title：@Aspect 把一个类定义为切面供容器读取
 * 多个切面类，执行的先后顺序根据@order(int value) 值越小越先执行
 */
@Component
@Aspect
public class ControllerAspect {


    /**
     * 定义一个切入点：execution(* 包名.*.*(..))
     * 1、execution(): 表达式主体。
     *
     * 2、第一个*号：方法返回类型， *号表示所有的类型。
     *
     * 3、包名：表示需要拦截的包名。
     *
     * 4、第二个*号：表示类名，*号表示所有的类。
     *
     * 5、*(..):最后这个星号表示方法名，*号表示所有的方法，后面( )里面表示方法的参数，两个句点表示任何参数
     */
    @Pointcut("execution(* com.kadima.aop.controller..*(..))")
    public void a(){}

    @Before("@annotation(com.kadima.aop.annnotation.aopTest)")
    public void befmethod(){
        System.out.println("before==========目标方法执行前执行");
    }

    /**
     * 方法别拦截后，首先会执行@around bef ->@before  ->  method -> @around after  -> @after -> @afterReturning
     * @param point
     * @throws Throwable
     */
  /*  @Around("@annotation(com.kadima.aop.annnotation.aopTest)")
    public void befMethod(ProceedingJoinPoint point) throws Throwable {
        System.out.println("around====目标方法执行前");
        Object o = point.proceed();
        System.out.println("around====目标方法执行后");//有异常就不执行了
    }*/

    @Around(value = "a()")
    public void befMethod(ProceedingJoinPoint point) throws Throwable {
        System.out.println("around====目标方法执行前");
        Object o = point.proceed();
        System.out.println("around====目标方法执行后");
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
