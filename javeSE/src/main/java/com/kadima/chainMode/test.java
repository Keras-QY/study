package com.kadima.chainMode;

import java.util.Arrays;
import java.util.List;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/11
 * @title：
 */
public class test {

    public static void main(String[] args) {
        com.kadima.chainMode.student student = com.kadima.chainMode.student.builder().age(28).name("钱勇").school("兰州大学").build();
        System.out.println(student.toString());

        //stream流式计算
        student student1 = new student("小明", 20, "清华大学");
        student student2 = new student("小明", 22, "北京大学");
        student student3 = new student("小明", 25, "兰州大学");
        List<com.kadima.chainMode.student> studentList = Arrays.asList(student1, student2, student3);
        studentList.stream().filter((u) -> {
            return u.getAge() == 25;
        }).forEach(System.out::println);
    }
}
