package com.miufeng.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.system.Dept;
import com.miufeng.service.system.DeptService;
import com.miufeng.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2020
 * FileName: DeptController
 * Author:   Administrator
 * Date:     2020/12/22 0022 14:32
 */
@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    /**
     * 作用：进入部门的列表页面
     * 路径：/system/dept/list.do
     * 返回： /system/dept/dept-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        //1.获取登录者的公司id
        String companyId = getLoginCompanyId();

        //2.分页查找所有部门信息
        PageInfo<Dept> pageInfo = deptService.findAll(pageNum, pageSize, companyId);

        //3.将部门信息存储到request域中
        request.setAttribute("pageInfo", pageInfo);

        //4.返回部门列表页面
        return "system/dept/dept-list";
    }

    /**
     * 作用：进入部门的添加页面
     * 路径：/system/dept/toAdd.do
     * 返回：/system/dept/dept-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        //1.获取登录者的公司id
        String companyId = getLoginCompanyId();

        //2.查找所有部门
        List<Dept> list = deptService.findAllDept(companyId);

        //3.将查找到的结果存储到request域
        request.setAttribute("deptList", list);

        //4.跳转到部门添加页面
        return "system/dept/dept-add";
    }

    /**
     * 作用：保存添加和保存修改的部门信息
     * 路径：/system/dept/edit.do
     * 返回：查找部门列表
     */
    @RequestMapping("/edit")
    public String edit(Dept dept) {
        //1.给部门补充公司id和公司名称
        dept.setCompanyId(getLoginCompanyId());
        dept.setCompanyName(getLoginCompanyName());

        //2.根据部门id有无判断是添加还是修改
        if (StringUtils.isEmpty(dept.getId())) {
            //为空是添加
            //保存部门信息
            deptService.save(dept);
        }else {
            //不为空是修改
            //保存修改信息
            deptService.update(dept);
        }

        //3.返回部门列表页面
        return "redirect:/system/dept/list.do";
    }

    /**
     * 作用：进入部门的修改页面
     * 路径：/system/dept/toUpdate.do
     * 返回：/system/dept/dept-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1.获取登录者的公司id
        String companyId = getLoginCompanyId();

        //2.根据部门id查找部门信息
        Dept dept = deptService.findById(id);

        //3.查找所有部门，除了要修改的本部门
        List<Dept> deptList = deptService.findAllDeptPro(companyId, id);

        //4.将信息保存到request域
        request.setAttribute("dept", dept);
        request.setAttribute("deptList", deptList);

        //5.跳转到部门修改页面
        return "system/dept/dept-update";
    }

    /**
     * 作用：删除指定部门
     * 路径：/system/dept/delete.do
     * 返回：查找部门列表
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String id) {
        //1.删除部门
        boolean flag = deptService.delete(id);

        //2.判断是否删除成功
        HashMap<String, Object> map = new HashMap<>();
        map.put("flag", flag);
        if (flag) {
            map.put("message", "删除成功");
        }else {
            map.put("message", "该部门有子部门或存在员工，不能直接删除");
        }

        return map;
    }


}
