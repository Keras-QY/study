package com.kadima.Mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/6/11
 * @title：
 */
<<<<<<< Updated upstream
@Data
@AllArgsConstructor
=======

>>>>>>> Stashed changes
public class student {

    private Integer age;
<<<<<<< Updated upstream
    private String name;

    public Integer getAge() {
        System.out.println("get方法被调用");
        return 0;
=======

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
>>>>>>> Stashed changes
    }
}
