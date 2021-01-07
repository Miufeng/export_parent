package com.miufeng.web.shiro;

import com.miufeng.domain.system.Module;
import com.miufeng.domain.system.User;
import com.miufeng.service.system.ModuleService;
import com.miufeng.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Copyright (C), 2018-2020
 * FileName: AuthRealm
 * Author:   Administrator
 * Date:     2020/12/25 0025 19:53
 */
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    /*
    * 登录认证
    * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.先把token强制转换为UserNamePasswordToken，强制转换的目的是为了得到用户的用户名和密码
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String email = usernamePasswordToken.getUsername();
        //String password = new String(usernamePasswordToken.getPassword()); //用户本次输入的密码

        //2.根据邮箱去找应该用户，如果查找用户为空，直接返回null，如果这里返回null，那么登录方法会收到UnknowAccountException
        User dbUser = userService.findUserByEmail(email);
        if (dbUser == null) {
            return null;
        }

        //3.如果根据邮箱查找的用户不为空，则对比密码，只不过密码对比的过程由shiro自己去完成
        /*
            SimpleAuthenticationInfo(Object principal, Object credentials, String realmName)
                principal: 设置登录成功后返回的对象
                credentials: 该用户在数据库中密码
                realmName: 不需要管理
         */
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(dbUser, dbUser.getPassword(), "");

        return simpleAuthenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1.获取登录用户
        User loginUser = (User) principals.getPrimaryPrincipal();

        //2.查询当前用户具备的权限
        List<Module> moduleList = moduleService.findModuleByUserId(loginUser);

        //3.给当前用户分配权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //遍历分配权限
        for (Module module : moduleList) {
            //添加一个全新标记，但是要求该权限的标记必须是唯一(这里用模块名)
            authorizationInfo.addStringPermission(module.getName());
        }

        return authorizationInfo;
    }


}
