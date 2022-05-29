package com.kadima.DesignModel.Strategy.StrategyImp;

import com.kadima.DesignModel.Strategy.FactoryModel;
import com.kadima.DesignModel.Strategy.strategy;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/29
 * @title：
 */
public class ThirdStrategy implements strategy {

    private int type = 3;
    @Override
    public void dealMethod() {
        System.out.println("我是第三种策略");
    }

    @Override
    public int getType() {
        return type;
    }

    //初始化bean之后所作的操作
    public void registerToMap(){
        FactoryModel.register(type,this);
    }
}
