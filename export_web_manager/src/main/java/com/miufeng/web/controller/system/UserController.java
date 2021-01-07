package com.miufeng.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.system.Dept;
import com.miufeng.domain.system.Role;
import com.miufeng.domain.system.User;
import com.miufeng.service.system.DeptService;
import com.miufeng.service.system.RoleService;
import com.miufeng.service.system.UserService;
import com.miufeng.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
 * FileName: UserController
 * Author:   Administrator
 * Date:     2020/12/22 0022 14:32
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 作用：进入用户的列表页面
     * 路径：/system/user/list.do
     * 返回： /system/user/user-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {

        /*硬编码的方式去实现权限检查
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("用户管理");*/

        //1.获取登录者的公司id
        String companyId = getLoginCompanyId();

        //2.分页查找所有用户信息
        PageInfo<User> pageInfo = userService.findAll(pageNum, pageSize, companyId);

        //3.将用户信息存储到request域中
        request.setAttribute("pageInfo", pageInfo);

        //4.返回用户列表页面
        return "system/user/user-list";
    }

    /**
     * 作用：进入用户的添加页面
     * 路径：/system/user/toAdd.do
     * 返回：/system/user/user-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        //1.获取登录者的公司id
        String companyId = getLoginCompanyId();

        //2.查找所有部门
        List<Dept> list = deptService.findAllDept(companyId);

        //3.将查找到的结果存储到request域
        request.setAttribute("deptList", list);

        //4.跳转到用户添加页面
        return "system/user/user-add";
    }

    /**
     * 作用：保存添加和保存修改的用户信息
     * 路径：/system/user/edit.do
     * 返回：查找用户列表
     */
    @RequestMapping("/edit")
    public String edit(User user) {
        //1.给用户补充公司id和公司名称
        user.setCompanyId(getLoginCompanyId());
        user.setCompanyName(getLoginCompanyName());

        //2.根据用户id有无判断是添加还是修改
        if (StringUtils.isEmpty(user.getId())) {
            //为空是添加
            //保存用户信息
            userService.save(user);
            //发送消息到rabbitmq中
            rabbitTemplate.convertAndSend("email-exchange", "user.add", user);
        }else {
            //不为空是修改
            //保存修改信息
            userService.update(user);
        }

        //3.返回用户列表页面
        return "redirect:/system/user/list.do";
    }

    /**
     * 作用：进入用户的修改页面
     * 路径：/system/user/toUpdate.do
     * 返回：/system/user/user-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1.获取登录者的公司id
        String companyId = getLoginCompanyId();

        //2.根据用户id查找用户信息
        User user = userService.findById(id);

        //3.查找所有部门
        List<Dept> deptList = deptService.findAllDept(companyId);

        //4.将信息保存到request域
        request.setAttribute("user", user);
        request.setAttribute("deptList", deptList);

        //5.跳转到用户修改页面
        return "system/user/user-update";
    }

    /**
     * 作用：删除指定用户
     * 路径：/system/user/delete.do
     * 返回：查找用户列表
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String id) {
        //1.删除用户
        boolean flag = userService.delete(id);

        //2.判断是否删除成功
        HashMap<String, Object> map = new HashMap<>();
        map.put("flag", flag);
        if (flag) {
            map.put("message", "删除成功");
        }else {
            map.put("message", "该用户还没有被解除角色，不能直接删除");
        }

        return map;
    }

    /**
     * 作用：进入用户角色管理
     * 路径：/system/user/roleList.do
     * 返回：/system/user/user-role.jsp
     */
    @RequestMapping("/roleList")
    public String roleList(String id) {
        //1.获取当前用户
        User user = userService.findById(id);

        //2.获取所有角色
        List<Role> roleList = roleService.findAllRole(getLoginCompanyId());

        //3.获取当前用户所拥有的角色
        List<String> roleIds = userService.findRoleIdByUserId(id);

        //2.将上面的数据存到request域
        request.setAttribute("user", user);
        request.setAttribute("roleList", roleList);
        request.setAttribute("userRoleStr", roleIds.toString());

        //3.跳转到用户角色管理页面
        return "system/user/user-role";
    }


    /**
     * 作用：保存用户角色
     * 路径：/system/user/changeRole.do
     * 返回：进入角色列表页面
     */
    @RequestMapping("/changeRole")
    public String changeRole(String userid, String[] roleIds) {

        userService.changeRole(userid, roleIds);

        //返回用户列表页面
        return "redirect:/system/user/list.do";
    }

    /**
     * 作用：根据用户名模糊查询用户
     * 路径：/system/user/search.do
     * 返回：进入用户列表页面
     */
    /*@RequestMapping("/search")
    public String search(String searchName) {
        List<User> userList = userService.searchUserByName(searchName);

        //查询到用户，
        if (userList.size() > 0) {

        }
    }*/

}
