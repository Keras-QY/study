package com.kadima.Mybatis.mapper;

import com.kadima.Mybatis.entity.student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/6/11
 * @title：
 */

public interface AccountMapper {
    student selectById(int plugin);
}
