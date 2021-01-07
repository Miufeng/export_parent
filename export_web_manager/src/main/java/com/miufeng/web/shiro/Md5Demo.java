package com.miufeng.web.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * Copyright (C), 2018-2020
 * FileName: Md5Demo
 * Author:   Administrator
 * Date:     2020/12/26 0026 16:04
 */
public class Md5Demo {
    public static void main(String[] args) {
        String slat = "zhangsan@export.com";
        String password = "111";
        Md5Hash md5Hash = new Md5Hash(password, slat);
        System.out.println("123加盐加密后的字符串：" + md5Hash.toString());
    }
}
