package com.miufeng.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Copyright (C), 2018-2021
 * FileName: EmailConsuer
 * Author:   Administrator
 * Date:     2021/1/7 0007 21:03
 */
public class EmailConsumer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-rabbitmq-consumer.xml");
        context.start();
    }
}
