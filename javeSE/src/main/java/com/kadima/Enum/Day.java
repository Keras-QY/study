package com.kadima.Enum;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/8
 * @title：
 */
public enum Day {

    Monday(0,"星期一"){
        @Override
        public String getDesc() {
            return "今天是周一呀";
        }
    },
    Tuesday(1,"星期二"){
        @Override
        public String getDesc() {
            return "今天是周二呀";
        }
    },
    Wednesday(2,"星期三"){
        @Override
        public String getDesc() {
            return "今天是周三";
        }
    },
    Thursday(3,"星期四"){
        @Override
        public String getDesc() {
            return "今天是周四呀";
        }
    },
    Firday(4,"星期五"){
        @Override
        public String getDesc() {
            return "今天是周五呀";
        }
    },
    Saturday(5,"星期六"){
        @Override
        public String getDesc() {
            return "今天是周六";
        }
    },
    Sunday(6,"星期日"){
        @Override
        public String getDesc() {
            return "今天是周日";
        }
    };

    private int number;
    private String day;

    /**
     * 定义抽象方法
     */

    public abstract String getDesc();
    Day() {
    }

    Day(int number, String day) {
        this.number = number;
        this.day = day;
    }
}

