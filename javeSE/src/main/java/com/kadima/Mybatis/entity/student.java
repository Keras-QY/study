package com.kadima.Mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/6/11
 * @title：
 */
@Data
@AllArgsConstructor
public class student {

    private Integer age;
    private String name;

    public Integer getAge() {
        System.out.println("get方法被调用");
        return 0;
    }
}
