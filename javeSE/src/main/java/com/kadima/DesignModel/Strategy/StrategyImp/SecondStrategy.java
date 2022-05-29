package com.kadima.DesignModel.Strategy.StrategyImp;

import com.kadima.DesignModel.Strategy.strategy;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/29
 * @title：
 */
public class SecondStrategy implements strategy {

    private int type = 2;


    @Override
    public void dealMethod() {
        System.out.println("我是第二种策略");
    }

    @Override
    public int getType() {
        return type;
    }
}
