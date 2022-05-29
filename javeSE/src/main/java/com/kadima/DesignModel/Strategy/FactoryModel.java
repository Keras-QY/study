package com.kadima.DesignModel.Strategy;

import java.util.HashMap;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/29
 * @title：
 */
public class FactoryModel {

    private static HashMap<Integer,strategy> hashMap = new HashMap<>();

    public strategy getstrategybyType(int type){
        return hashMap.get(type);
    }

    public static void register(Integer integer,strategy strategys){

        hashMap.put(integer,strategys);

    }
}
