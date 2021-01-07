package com.miufeng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.miufeng.domain.company.Company;
import com.miufeng.service.company.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright (C), 2018-2020
 * FileName: CompanyController
 * Author:   Administrator
 * Date:     2020/12/29 0029 9:20
 */
@Controller
public class CompanyController {

    @Reference
    private CompanyService companyService;

    @RequestMapping("/apply")
    @ResponseBody   //返回一个普通字符串或者json数据都需要添加 @ResponseBody
    public String apply(Company company) {

        try {
            company.setState(0);    //前台入驻企业需要审核，所以先把状态修改为0
            companyService.add(company);
            return "1"; //代表成功
        } catch (Exception e) {
            e.printStackTrace();
            return "0"; //代表失败
        }
    }
}
