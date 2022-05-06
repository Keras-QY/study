package com.kadima.reflect;

import javax.annotation.Resource;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/4
 * @title：
 */
@Deprecated
@Resource(name = "hello world")
public class studentDomain {

    public String name;
    private String shcool;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("反射执行setName方法");
        this.name = name;
    }

    private String getShcool() {
        return shcool;
    }

    private void setShcool(String shcool) {
        this.shcool = shcool;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
