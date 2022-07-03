package com.kadima.Mybatis.service;

import com.kadima.Mybatis.mapper.AccountMapper;
import com.kadima.Mybatis.entity.student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/7/3
 * @title：
 */
@Service
public class myService {

    @Autowired
    AccountMapper accountMapper;

    public student getStudent(int id){
        student student = accountMapper.selectById(id);
        return student;
    }
}
