package com.kadima.chainMode;

//import com.kadima.StringTest.String;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/11
 * @title：
 */
public class test {

    public static void main(String[] args) {
        //com.kadima.chainMode.student student = com.kadima.chainMode.student.builder().age(28).name("钱勇").school("兰州大学").build();
        //System.out.println(student.toString());

        //stream流式计算
        student student1 = new student("小明", 20, "清华大学");
        student student2 = new student("小红", 27, "北京大学");
        student student3 = new student("小强", 26, "兰州大学");
        student student4 = new student("小文", 28, "宿州大学");
        List<com.kadima.chainMode.student> studentList = Arrays.asList(student1, student2, student3,student4);

        Predicate<student> predicate1 = student -> student.getAge() == 25;
        Predicate<student> predicate2 = student -> student.getAge() > 25;
        studentList.stream().filter(predicate1.or(predicate2)).collect(Collectors.toList()).forEach(test::getstring);


        //filter过滤器
        //studentList.stream().filter( (u -> u.getAge() == 25)).forEach(System.out::println);
        List<student> studentList1 = studentList.stream().filter((u -> u.getAge() < 25)).collect(Collectors.toList());
        System.out.println(studentList1);



        /*//收集器,生成新的集合
        Set<com.kadima.chainMode.student> collect = studentList.stream().filter((u) -> u.getAge() > 25).collect(Collectors.toSet());
        System.out.println(collect);

        //limit
         studentList.stream().limit(2).forEach(student -> System.out.println("limit"+student));

        List<Integer> integerList = Arrays.asList(1, 3, 6, 8, 4);
        //sorted
        integerList.stream().sorted().forEach(System.out::println);
        //反序
        integerList.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
        //max
        System.out.println(integerList.stream().max(Integer::compareTo).get());
        //count
        System.out.println(integerList.stream().count());
        //map 对取出的数据进行额外的操作
        studentList.stream().map(student -> student.getAge()+1).forEach(System.out::println);*/
        //reduce
        //studentList.stream().reduce((sum,num) -> sum+num).get()
        //StringTest string = new StringTest(10, 20);


    }

    private static void getstring(student ss){
        System.out.println("===="+ss);
    }
}
