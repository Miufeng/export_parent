package com.miufeng.web.controller.company;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.miufeng.domain.company.Company;
import com.miufeng.service.company.CompanyService;
import com.miufeng.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (C), 2018-2020
 * FileName: CompanyController
 * Author:   Administrator
 * Date:     2020/12/19 0019 17:11
 */
@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {

    @Reference
    private CompanyService companyService;

    /**
     * 分页查询企业列表
     * 注意：当前页和每页大小需要设置默认值
     * @param pageNum
     * @param pageSize
     * @return String
     */
    @RequestMapping("/list")
    /*权限控制注解
    @RequiresPermissions("企业管理")*/
    public String findByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize) {

        /*硬编码的方式去实现权限检查
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("企业管理");*/

        PageInfo pageInfo = companyService.findByPage(pageNum, pageSize);

        //将pageInfo存储到request域中
        request.setAttribute("pageInfo", pageInfo);

        return "company/company-list";
    }

    /**
     * 跳转到新建用户页面
     * @return String
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "company/company-add";
    }

    /**
     * 保存新建和修改的企业的信息
     * @param company
     * @return String
     */
    @RequestMapping("/edit")
    public String edit(Company company) {
        //根据company的id是否为空，判断是处理新建或修改的请求
        if (StringUtils.isEmpty(company.getId())) {
            //id为空是保存新建
            companyService.add(company);
        }else {
            //id不为空是保存修改
            companyService.update(company);
        }

        //重定向到company-list.jsp（直接return会经过视图解析器）
        return "redirect:/company/list.do";
    }

    /**
     * 跳转到修改企业信息页面，并回显数据
     * @return String
     */
    @RequestMapping("toUpdate")
    public String toUpdate(String id) {
        //根据id查询企业信息
        Company company = companyService.findById(id);

        //将查询结果存储到request中
        request.setAttribute("company", company);

        return "/company/company-update";
    }

    /**
     * 根据id删除企业
     * @param id
     * @return String
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        companyService.delete(id);

        //返回企业列表
        return "redirect:/company/list.do";
    }

}
