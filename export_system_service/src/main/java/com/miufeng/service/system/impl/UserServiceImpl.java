package com.miufeng.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.system.UserDao;
import com.miufeng.domain.system.Module;
import com.miufeng.domain.system.User;
import com.miufeng.service.system.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: UserServiceImpl
 * Author:   Administrator
 * Date:     2020/12/22 0022 15:06
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    //分页查询所有用户列表
    @Override
    public PageInfo<User> findAll(Integer pageNum, Integer pageSize, String companyId) {
        //1.设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);

        //2.查询所有用户list
        List<User> list = userDao.findAll(companyId);

        //3.创建pageInfo，并将list加入
        PageInfo<User> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    //根据用户id查询用户
    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    //查找所有用户
    @Override
    public List<User> findAllUser(String companyId) {
        return userDao.findAll(companyId);
    }

    //添加用户
    @Override
    public void save(User user) {
        //设置唯一用户id
        user.setId(UUID.randomUUID().toString());

        //对密码，进行加盐加密
        String md5Password = new Md5Hash(user.getPassword(), user.getEmail()).toString();
        user.setPassword(md5Password);

        userDao.save(user);
    }

    //修改用户信息
    @Override
    public void update(User user) {
        userDao.update(user);
    }

    //根据用户id删除用户
    @Override
    public Boolean delete(String id) {
        //1.查询该用户是否被分配角色
        long count = userDao.findUserRoleByUserId(id);

        //2. 如果用户已经被分配了角色,则不能删除,直接return false
        if (count > 0) {
            return false;
        }

        //3.否则，直接删除
        userDao.delete(id);
        return true;
    }

    //获取指定用户所拥有的角色
    @Override
    public List<String> findRoleIdByUserId(String id) {
        return userDao.findRoleIdByUserId(id);
    }

    //保存用户角色
    @Override
    public void changeRole(String userid, String[] roleIds) {
        //先删除该用户的所有角色
        userDao.deleteRoleByUserId(userid);

        //再重新为该用户添加角色
        if (roleIds != null) {
            userDao.addUserRoles(userid, roleIds);
        }

    }

    //根据用户邮箱查找用户
    @Override
    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

}
