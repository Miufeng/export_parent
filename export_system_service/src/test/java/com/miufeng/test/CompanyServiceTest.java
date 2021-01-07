package com.miufeng.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Copyright (C), 2018-2020
 * FileName: CompanyServiceTest
 * Author:   Administrator
 * Date:     2020/12/19 0019 16:46
 */
@RunWith(SpringJUnit4ClassRunner.class)
//注意： 由于service是依赖dao的，所以加载配置文件时候，一定需要把dao的配置文件也加载进来。
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class CompanyServiceTest {


    //private CompanyService companyService;

    @Test
    public void test01() {
       /* List<Company> list = companyService.findAll();

        System.out.println("企业信息： = " + list);*/
    }
}
