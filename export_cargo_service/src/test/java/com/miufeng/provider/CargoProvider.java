package com.miufeng.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Copyright (C), 2018-2020
 * FileName: CargoProvider
 * Author:   Administrator
 * Date:     2020/12/29 0029 21:18
 */
public class CargoProvider {
    public static void main(String[] args) throws IOException {
        //要加载dubbo、dao和service的，总共三个配置文件
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");

        context.start();
        System.in.read();
    }



}
