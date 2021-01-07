package com.miufeng.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.system.Module;
import com.miufeng.service.system.DeptService;
import com.miufeng.service.system.ModuleService;
import com.miufeng.web.controller.BaseController;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Copyright (C), 2018-2020
 * FileName: ModuleController
 * Author:   Administrator
 * Date:     2020/12/22 0022 14:32
 */
@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;

    /**
     * 作用：进入模块的列表页面
     * 路径：/system/module/list.do
     * 返回： /system/module/module-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {

        //1.分页查找所有模块信息
        PageInfo<Module> pageInfo = moduleService.findAll(pageNum, pageSize);

        //2.将模块信息存储到request域中
        request.setAttribute("pageInfo", pageInfo);

        //3.返回模块列表页面
        return "system/module/module-list";
    }

    /**
     * 作用：进入模块的添加页面
     * 路径：/system/module/toAdd.do
     * 返回：/system/module/module-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        //1.查找所有模块
        List<Module> moduleList = moduleService.findAllModule();

        //2.将查找结果存储到request域
        request.setAttribute("menus", moduleList);

        //跳转到模块添加页面
        return "system/module/module-add";
    }

    /**
     * 作用：保存添加和保存修改的模块信息
     * 路径：/system/module/edit.do
     * 返回：查找模块列表
     */
    @RequestMapping("/edit")
    public String edit(Module module) {
        //1.根据模块id有无判断是添加还是修改
        if (StringUtils.isEmpty(module.getId())) {
            //为空是添加
            //保存模块信息
            moduleService.save(module);
        }else {
            //不为空是修改
            //保存修改信息
            moduleService.update(module);
        }

        //2.返回模块列表页面
        return "redirect:/system/module/list.do";
    }

    /**
     * 作用：进入模块的修改页面
     * 路径：/system/module/toUpdate.do
     * 返回：/system/module/module-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {

        //1.根据模块id查找模块信息
        Module module = moduleService.findById(id);

        //2.查询除本模块外的所有模块
        List<Module> moduleList = moduleService.findAllModulePro(id);

        //3.将信息保存到request域
        request.setAttribute("module", module);
        request.setAttribute("menus", moduleList);

        //4.跳转到模块修改页面
        return "system/module/module-update";
    }

    /**
     * 作用：删除指定模块
     * 路径：/system/module/delete.do
     * 返回：查找模块列表
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        //删除模块
        moduleService.delete(id);

        return "redirect:/system/module/list.do";
    }


}
