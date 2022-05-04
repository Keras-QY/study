package com.kadima.reflect;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/4
 * @title：
 */
public class annotationTest {

    public static void main(String[] args) {
        Class<studentDomain> studentDomainClass = studentDomain.class;
        /**
         * 获取类上所有的注解
         */
        Annotation[] annotations = studentDomainClass.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        /**
         * 获取类上指定的注解
         */
        Resource annotation = studentDomainClass.getAnnotation(Resource.class);
        String name = annotation.name();
        System.out.println(name);
    }
}
