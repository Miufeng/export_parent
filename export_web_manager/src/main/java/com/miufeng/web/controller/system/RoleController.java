package com.miufeng.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.system.Dept;
import com.miufeng.domain.system.Module;
import com.miufeng.domain.system.Role;
import com.miufeng.service.system.DeptService;
import com.miufeng.service.system.ModuleService;
import com.miufeng.service.system.RoleService;
import com.miufeng.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2020
 * FileName: RoleController
 * Author:   Administrator
 * Date:     2020/12/22 0022 14:32
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModuleService moduleService;

    /**
     * 作用：进入角色的列表页面
     * 路径：/system/role/list.do
     * 返回： /system/role/role-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        //1.获取登录者的公司id
        String companyId = getLoginCompanyId();

        //2.分页查找所有角色信息
        PageInfo<Role> pageInfo = roleService.findAll(pageNum, pageSize, companyId);

        //3.将角色信息存储到request域中
        request.setAttribute("pageInfo", pageInfo);

        //4.返回角色列表页面
        return "system/role/role-list";
    }

    /**
     * 作用：进入角色的添加页面
     * 路径：/system/role/toAdd.do
     * 返回：/system/role/role-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        //跳转到角色添加页面
        return "system/role/role-add";
    }

    /**
     * 作用：保存添加和保存修改的角色信息
     * 路径：/system/role/edit.do
     * 返回：查找角色列表
     */
    @RequestMapping("/edit")
    public String edit(Role role) {
        //1.给角色补充公司id和公司名称
        role.setCompanyId(getLoginCompanyId());
        role.setCompanyName(getLoginCompanyName());

        //2.根据角色id有无判断是添加还是修改
        if (StringUtils.isEmpty(role.getId())) {
            //为空是添加
            //保存角色信息
            roleService.save(role);
        }else {
            //不为空是修改
            //保存修改信息
            roleService.update(role);
        }

        //3.返回角色列表页面
        return "redirect:/system/role/list.do";
    }

    /**
     * 作用：进入角色的修改页面
     * 路径：/system/role/toUpdate.do
     * 返回：/system/role/role-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1.获取登录者的公司id
        String companyId = getLoginCompanyId();

        //2.根据角色id查找角色信息
        Role role = roleService.findById(id);

        //3.将信息保存到request域
        request.setAttribute("role", role);

        //4.跳转到角色修改页面
        return "system/role/role-update";
    }

    /**
     * 作用：删除指定角色
     * 路径：/system/role/delete.do
     * 返回：查找角色列表
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        //删除角色
        roleService.delete(id);

        return "redirect:/system/role/list.do";
    }

    /**
     * 作用：进入角色权限管理
     * 路径：/system/role/roleModule.do
     * 返回：/system/role/role-module.jsp
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleid) {
        //1.获取当前角色
        Role role = roleService.findById(roleid);

        //2.将上面的数据存到request域
        request.setAttribute("role", role);

        //3.跳转到角色权限管理页面
        return "system/role/role-module";
    }

    /**
     * 作用：异步提供权限ztree所需数据
     * 路径：/system/role/roleModule.do
     * 返回：/system/role/role-module.jsp
     */
    @RequestMapping("/getModuleTree")
    @ResponseBody
    public List<Map<String, Object>> getModuleTree(String roleid) {
        //1.获取权限ztree所需数据
        /*
        * 格式：[
            { id:1, pId:0, name:"Saas管理", open:true},
            { id:11, pId:1, name:"企业管理"},
            { id:111, pId:1, name:"模块管理"},
        ]
        * */
        //1.1获取所有权限
        List<Module> list = moduleService.findAllModule();

        //1.2 获取当前角色拥有的权限
        List<String> moduleIds = roleService.findModuleIdByRoleId(roleid);

        //1.3拼接传输的数据
        List<Map<String, Object>> moduleList = new ArrayList<>();
        for (Module module : list) {
            //封装map
            Map<String, Object> map = new HashMap<>();
            map.put("id", module.getId());
            map.put("pId", module.getParentId());
            map.put("name", module.getName());
            map.put("open", true);

            //判断moduleId是否相同，从而判断是否打勾
            for (String moduleId : moduleIds) {
                if (moduleId.equals(module.getId())) {
                    map.put("checked", true);
                }
            }
            /*判断当前遍历的module的id是否包含在moduleIds中
            if (moduleIds.contains(module.getId())) {
                map.put("checked", true);
            }
            */

            //添加到moduleList
            moduleList.add(map);
        }

        //2.返回数据
        return moduleList;
    }

    /**
     * 作用：保存角色权限
     * 路径：/system/role/updateRoleModule.do
     * 返回：进入角色列表页面
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleid, String[] moduleIds) {//springmvc可以将字符串自动转换成字符串数组

        roleService.updateRoleModule(roleid, moduleIds);

        //3.返回角色列表页面
        return "redirect:/system/role/list.do";
    }
}
