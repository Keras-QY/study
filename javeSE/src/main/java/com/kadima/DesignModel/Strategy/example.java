package com.kadima.DesignModel.Strategy;

import com.kadima.DesignModel.Strategy.StrategyImp.FirstStrategy;
import com.kadima.DesignModel.Strategy.StrategyImp.FourStrategy;
import com.kadima.DesignModel.Strategy.StrategyImp.SecondStrategy;
import com.kadima.DesignModel.Strategy.StrategyImp.ThirdStrategy;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/29
 * @title：
 */
public class example {

    public static void main(String[] args) {
        deal(2);

        /**
         * 使用自动注入获取策略类
         */
        List<strategy> list = Arrays.asList(new FirstStrategy(), new SecondStrategy(), new ThirdStrategy(), new FourStrategy());
        //策略类映射关系
        Map<Integer, strategy> map = new HashMap<>();
        map = list.stream().collect(Collectors.toMap(strategy::getType, Function.identity()));
        strategy strategy = map.get(1);
        strategy.dealMethod();


    }

    public static void deal(int type){
        if (type == 1){
            System.out.println("执行第一种策略");
        }
        if (type == 2){
            System.out.println("执行第二种策略");
        }
        if (type == 3){
            System.out.println("执行第三种策略");
        }
        if (type == 4){
            System.out.println("执行第四种策略");
        }
        if (type == 5){
            System.out.println("执行第五种策略");
        }
    }
}
