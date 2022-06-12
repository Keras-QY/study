package com.kadima.Mybatis;

import com.kadima.Mybatis.entity.student;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/6/11
 * @title：
 */
public interface AccountMapper {

    student selectById(int id);
}
