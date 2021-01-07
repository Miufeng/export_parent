package com.miufeng.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.system.SysLog;
import com.miufeng.service.system.SysLogService;
import com.miufeng.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Copyright (C), 2018-2020
 * FileName: SysLogController
 * Author:   Administrator
 * Date:     2020/12/22 0022 14:32
 */
@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 作用：进入日志的列表页面
     * 路径：/system/log/list.do
     * 返回： /system/log/log-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        //1.获取登录者的公司id
        String companyId = getLoginCompanyId();

        //2.分页查找所有日志信息
        PageInfo<SysLog> pageInfo = sysLogService.findAll(pageNum, pageSize, companyId);

        //3.将日志信息存储到request域中
        request.setAttribute("pageInfo", pageInfo);

        //4.返回日志列表页面
        return "system/log/log-list";
    }

}
