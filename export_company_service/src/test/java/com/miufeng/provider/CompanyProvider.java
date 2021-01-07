package com.miufeng.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Copyright (C), 2018-2020
 * FileName: CompanyProvider
 * Author:   Administrator
 * Date:     2020/12/29 0029 9:04
 */
public class CompanyProvider {
    public static void main(String[] args) throws IOException {
        //易错点：一定要把dao和service的配置文件一起加载
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");

        //启动
        context.start();
        System.in.read();
    }
}
