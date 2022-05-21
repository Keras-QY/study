package com.kadima.Array;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/21
 * @title：
 */
public class Student {

    private String name;
    private String age;

    public Student(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
