package com.miufeng.web.task;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C), 2018-2021
 * FileName: MyTask
 * Author:   Administrator
 * Date:     2021/1/7 0007 14:37
 */
//对应一个任务类，任务类的任务是输出当前的系统时间
public class MyTask {

    public void showTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("当前的时间：" + dateFormat.format(new Date()));

    }
}
