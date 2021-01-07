package com.miufeng.web.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * Copyright (C), 2018-2020
 * FileName: CustomCredentialsMatcher
 * Author:   Administrator
 * Date:     2020/12/26 0026 16:07
 */
//该类是一个自定义的密码匹配器
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    //把密码对比规则定义在该方法中即可
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1.把token转换为UsernamePasswordToken
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        //2.取出邮箱和密码
        String email = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());

        //3.对用户输入的密码进行加盐加密
        Md5Hash md5Hash = new Md5Hash(password, email);
        String md5Password = md5Hash.toString();

        //4.获取用户在数据库中的密码
        String dbPassword = (String) info.getCredentials();

        //5.对比用户输入的密码与该用户在数据库中的密码
        return dbPassword.equals(md5Password);
    }
}
