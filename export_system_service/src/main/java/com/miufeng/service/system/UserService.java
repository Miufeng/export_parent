package com.miufeng.service.system;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.system.Module;
import com.miufeng.domain.system.User;

import java.util.List;


public interface UserService {
    //分页查询所有用户列表, 注意：需要根据公司id才查，因为每个公司有自己对应的用户
    PageInfo<User> findAll(Integer pageNum, Integer pageSize, String companyId);

    //根据用户id查询用户
    User findById(String id);

    //查找所有用户
    List<User> findAllUser(String companyId);

    //添加用户
    void save(User user);

    //修改用户信息
    void update(User user);

    //根据用户id删除用户
    Boolean delete(String id);

    //获取指定用户所拥有的角色
    List<String> findRoleIdByUserId(String id);

    //保存用户角色
    void changeRole(String userid, String[] roleIds);

    //根据用户邮箱查找用户
    User findUserByEmail(String email);

}
