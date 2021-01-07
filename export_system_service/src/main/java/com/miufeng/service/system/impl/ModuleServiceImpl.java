package com.miufeng.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.system.ModuleDao;
import com.miufeng.domain.system.Module;
import com.miufeng.domain.system.User;
import com.miufeng.service.system.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: ModuleServiceImpl
 * Author:   Administrator
 * Date:     2020/12/22 0022 15:06
 */
@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    //分页查询所有模块列表
    @Override
    public PageInfo<Module> findAll(Integer pageNum, Integer pageSize) {
        //1.设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);

        //2.查询所有模块list
        List<Module> list = moduleDao.findAll();

        //3.创建pageInfo，并将list加入
        PageInfo<Module> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    //根据模块id查询模块
    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    //查找所有模块
    @Override
    public List<Module> findAllModule() {
        return moduleDao.findAll();
    }

    //添加模块
    @Override
    public void save(Module module) {
        //设置唯一模块id
        module.setId(UUID.randomUUID().toString());

        moduleDao.save(module);
    }

    //修改模块信息
    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    //查询除指定模块外的所有模块
    @Override
    public List<Module> findAllModulePro(String id) {
        return moduleDao.findAllModulePro(id);
    }

    //根据模块id删除模块
    @Override
    public void delete(String id) {
        moduleDao.delete(id);
    }

    //根据用户id查找用户对应的权限模块
    @Override
    public List<Module> findModuleByUserId(User user) {

        //根据用户等级判断是依据哪种模块查询，分三种查找类型
        /*
        * -- 用户的degree=0(saas管理员) ,能够查看菜单
             SELECT * FROM ss_module WHERE belong=0;

          -- 用户的degree=1(企业管理员)
             SELECT * FROM ss_module WHERE belong=1;

          -- 企业普通员工，需要根据当前用户的角色去查看
        * */

        //1.获取用户等级
        Integer degree = user.getDegree();
        List<Module> modules = null;

        if (degree == 0 || degree == 1) {
            //2.如果是管理员级别是degree=belong这种查询
            modules = moduleDao.findModuleByDegree(degree);

        }else {
            //3. 如果是普通的用户需要根据用户的角色去查询
            modules = moduleDao.findModuleByUserId(user.getId());
        }

        return modules;
    }

}
