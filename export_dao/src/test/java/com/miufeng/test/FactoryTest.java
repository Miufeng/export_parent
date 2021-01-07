package com.miufeng.test;

import com.miufeng.dao.cargo.FactoryDao;
import com.miufeng.domain.cargo.Factory;
import com.miufeng.domain.cargo.FactoryExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: FactoryTest
 * Author:   Administrator
 * Date:     2020/12/29 0029 16:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class FactoryTest {

    @Autowired
    private FactoryDao factoryDao;

    /*
        测试： 增加或者更新或者删除, 在这里同学们一定要搞清楚有selective的方法与没有selective的区别?
     */
    @Test
    public void test01() {
        Factory factory = new Factory();
        factory.setId(UUID.randomUUID().toString());
        factory.setFactoryName("传智IT工厂");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());

        //factoryDao.insert(factory); //没有selective的方法是不管实体类的字段是否为空，统一都更新或者插入
        //factoryDao.insertSelective(factory);    带有selective方法是只插入或者更新非空字段的值

        //factoryDao.deleteByPrimaryKey("59ccfa84-2213-4fef-a0c5-9fdf11811927");

        //查询:根据主键查询
        Factory f = factoryDao.selectByPrimaryKey("4028817a389cc7b701389d1efd940001");
        System.out.println("f = " + f);
    }

    //目标： 逆向工程下如何使用多条件查询
    @Test
    public void test02() {
        //添加查询对象，你要查询的条件都应该封装到Example对象中
        FactoryExample factoryExample = new FactoryExample();

        //真正的查询条件应该封装在Example里面的Criteria中
        FactoryExample.Criteria criteria = factoryExample.createCriteria();

        //封装查询条件
        criteria.andCtypeEqualTo("货物");
        criteria.andFactoryNameLike("%花%");

        List<Factory> factories = factoryDao.selectByExample(factoryExample);
        System.out.println("factories = " + factories);
    }
}
