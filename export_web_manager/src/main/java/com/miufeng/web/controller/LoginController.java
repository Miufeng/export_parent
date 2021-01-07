package com.miufeng.web.controller;

import com.miufeng.domain.system.Module;
import com.miufeng.domain.system.User;
import com.miufeng.service.system.ModuleService;
import com.miufeng.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Copyright (C), 2018-2020
 * FileName: loginController
 * Author:   Administrator
 * Date:     2020/12/20 0020 15:27
 */
@Controller
public class LoginController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    /*
    * 进入后台首页
    * */
    @RequestMapping("/login")
    public String login(String email, String password) {

        //1.判断用户名和密码是否为空
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            //为空，返回登录页面
            request.setAttribute("error", "用户名或密码不能为空");
            return "forward:/login.jsp";

        }

        try {
            //2.得到subjec对象
            Subject subject = SecurityUtils.getSubject();

            //3.把邮箱和密码封装到一个Token
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);

            //4.subjec发出认证请求
            subject.login(token);

            //5.登录成功会返回一个登陆成功对象，shiro在你登录成功之后会往session中做登录成功标记
            User loginUser = (User) subject.getPrincipal();

            //这里的session的数据只是为了让我们方便使用登录者的信息，并不是登录成功标记
            session.setAttribute("loginUser", loginUser);

            //一旦该用户登录成功，就应该马上查找该用户对应的权限
            List<Module> moduleList = moduleService.findModuleByUserId(loginUser);
            session.setAttribute("modules", moduleList);
            //跳转到后台首页
            return "home/main";

        } catch (UnknownAccountException e) {  //下面两个异常可以直接写它们的父类AuthenticationException e
            //如果用户名不存在则抛出该异常
            request.setAttribute("error", "用户名或密码错误");
            return "forward:/login.jsp";
        } catch (IncorrectCredentialsException e) {
            //如果密码不正确则抛出该异常
            request.setAttribute("error", "用户名或密码错误");
            return "forward:/login.jsp";
        }

    }

    /*
     * 加载home/home.jsp页面进内容区域
     * */
    @RequestMapping("/home")
    public String home() {
        return "home/home";
    }

    /*
     * 注销登录
     * 注销的方案：
            1. 把session的登陆成功标记删除 session.removeAttribute("loginUser")
            2. 摧毁整个session（推荐）
     * */
    @RequestMapping("/logout")
    public String logout() {

        //1.销毁shiro登录成功标记
        Subject subject = SecurityUtils.getSubject();
        subject.logout();//这个方法的作用是销毁shiro登录成功标记

        //2.销毁整个session
        session.invalidate();
        return "redirect:/login.jsp";
    }

}
