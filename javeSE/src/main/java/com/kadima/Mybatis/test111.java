package com.kadima.Mybatis;

import com.kadima.Mybatis.entity.student;
import com.kadima.Mybatis.service.myService;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/6/11
 * @title：
 */
public class test111 {

    public static void main(String[] args) throws IOException {


       /* student student = new student(1,"你好");
        //读取mybatis-config.xml配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //构建sqlSessionFactory(框架初始化)
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //打开sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper接口对象（底层是动态代理）
        AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class);
        //调用mapper接口对象的方法操作数据库
        student students = accountMapper.selectById(student);
        //业务处理
        System.out.println(students);
        //session提交并关闭
        sqlSession.commit();
        sqlSession.close();*/

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        myService accountService = context.getBean(myService.class);
        student student = accountService.getStudent(1);

        System.out.println(student);

    }
}
