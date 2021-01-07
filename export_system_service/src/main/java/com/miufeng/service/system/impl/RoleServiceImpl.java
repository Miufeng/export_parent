package com.miufeng.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.system.RoleDao;
import com.miufeng.domain.system.Role;
import com.miufeng.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: RoleServiceImpl
 * Author:   Administrator
 * Date:     2020/12/22 0022 15:06
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    //分页查询所有角色列表
    @Override
    public PageInfo<Role> findAll(Integer pageNum, Integer pageSize, String companyId) {
        //1.设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);

        //2.查询所有角色list
        List<Role> list = roleDao.findAll(companyId);

        //3.创建pageInfo，并将list加入
        PageInfo<Role> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    //根据角色id查询角色
    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    //查找所有角色
    @Override
    public List<Role> findAllRole(String companyId) {
        return roleDao.findAll(companyId);
    }

    //添加角色
    @Override
    public void save(Role role) {
        //设置唯一角色id
        role.setId(UUID.randomUUID().toString());

        roleDao.save(role);
    }

    //修改角色信息
    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    //根据角色id删除角色
    @Override
    public void delete(String id) {
        roleDao.delete(id);
    }

    //根据角色id查找权限id
    @Override
    public List<String> findModuleIdByRoleId(String roleid) {
        return roleDao.findModuleIdByRoleId(roleid);
    }

    //保存修改角色权限
    @Override
    public void updateRoleModule(String roleid, String[] moduleIds) {
        //1.先删除该角色的所有权限
        roleDao.deleteModuleByRoleId(roleid);

        //2.重新为当前的角色添加权限
        if (moduleIds.length > 0){
            roleDao.addRoleModules(roleid, moduleIds);
        }
    }

}
