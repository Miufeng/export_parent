package com.miufeng.web.controller;

import com.miufeng.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Copyright (C), 2018-2020
 * FileName: BaseController
 * Author:   Administrator
 * Date:     2020/12/22 0022 20:25
 */
//作用是抽取Controller公共的部分
public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;

    //获取当前登录者
    public User getLoginUser() {
        User loginUser = (User) session.getAttribute("loginUser");
        return loginUser;
    }

    //获取登录者的企业id
    public String getLoginCompanyId() {
        return getLoginUser().getCompanyId();
    }

    //获取登录者的企业名称
    public String getLoginCompanyName() {
        return getLoginUser().getCompanyName();
    }

}


