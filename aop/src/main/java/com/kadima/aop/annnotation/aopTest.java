package com.kadima.aop.annnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/6
 * @title：注解只有成员变量，没有方法;其方法名定义了该成员变量的名字，其返回值定义了该成员变量的类型
 * 成员变量必须有默认值，或者再使用的时候使用值
 */

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
/**
 * 注解的使用范围
 *TYPE：用于描述类、接口（包括注解类型）、枚举的定义
 *FIELD：用于描述成员变量、对象、属性（包括枚举常量
 *METHOD：用户描述方法
 *PARAMETER：用于描述参数
 *CONSTRUCTOR：用于描述构造器
 *LOCAL_VARIABLE： 用于描述局部变量
 *ANNOTATION_TYPE：用于描述注解的（元注解）
 *PACKAGE：用于描述包
 *Type parameter declaration：表示该注解能写在类型变量的声明语句中
 *TYPE_PARAMETER：表示该注解能写在类型变量的声明语句中
 *TYPE_USE：表示该注解能写在使用类型的任何语句中（声明语句、泛型和强制转换语句中的类型）
 */
@Target({ElementType.METHOD})
public @interface aopTest {
    String value();
    String arg() default "hello";

}

