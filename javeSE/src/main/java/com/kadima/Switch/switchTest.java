package com.kadima.Switch;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/6/1
 * @title：
 */
public class switchTest {

    public static void main(String[] args) {
        char s = 'C';

        /**
         * switch表达式支持6种类型
         * byte、short、int、char、string、enum
         */
        switch (s){
            case 'A':
                System.out.println("优秀");
                break;
            case 'B':
                http://www.baiedu.com
                System.out.println("良好");
                break;

            //只有不执行前面所有分支才会执行该分支
            default:
                System.out.println("成绩不合格");
        }

        switch (s){
            case 'A':
                System.out.println("优秀");
            case 'B':
                System.out.println("良好");

            //只有不执行前面所有分支才会执行该分支
            default:
                System.out.println("成绩不合格");
        }
    }
}
