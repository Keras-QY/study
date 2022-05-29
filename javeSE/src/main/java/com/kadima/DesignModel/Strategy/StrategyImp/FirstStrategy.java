package com.kadima.DesignModel.Strategy.StrategyImp;

import com.kadima.DesignModel.Strategy.strategy;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/29
 * @title：
 */
public class FirstStrategy implements strategy {

    private int type=1;
    @Override
    public void dealMethod() {
        System.out.println("我是第一种策略类");
    }

    @Override
    public int getType() {
        return type;
    }
}
