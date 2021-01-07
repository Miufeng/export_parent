package com.miufeng.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.company.CompanyDao;
import com.miufeng.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Copyright (C), 2018-2020
 * FileName: CompanyTest
 * Author:   Administrator
 * Date:     2020/12/19 0019 16:03
 */
@RunWith(SpringJUnit4ClassRunner.class)
//加载配置文件
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class CompanyTest {

    @Autowired
    private CompanyDao companyDao;

    @Test
    public void test01() {
        List<Company> list = companyDao.findAll();

        list.forEach(System.out::println);
    }

    //测试分页
    @Test
    public void test02() {
        //设置当前页和每页大小
        PageHelper.startPage(1, 3);

        //查询所有数据
        List<Company> list = companyDao.findAll();

        //创建pageInfo对象
        PageInfo<Company> pageInfo = new PageInfo<>(list);

        System.out.println("当前页："+ pageInfo.getPageNum());
        System.out.println("总页数："+ pageInfo.getPages());
        System.out.println("总记录数："+pageInfo.getTotal() );
        System.out.println("页面大小："+pageInfo.getPageSize() );
        System.out.println("页面数据："+ pageInfo.getList() );
    }
}
