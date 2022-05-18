package com.kadima.chainMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/11
 * @title：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class student implements Comparable{

    private String name;
    private int age;
    private String school;



    @Override
    public int compareTo(Object o) {
        if (age > 0)
        return 0;
        return -1;
    }
}
