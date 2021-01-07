package com.miufeng.dao.system;

import com.miufeng.domain.system.Module;
import com.miufeng.domain.system.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    //根据企业id查询全部
    List<User> findAll(String companyId);

    //根据id查询
    User findById(String userId);

    //根据id删除
    void delete(String userId);

    //保存
    void save(User user);

    //更新
    void update(User user);

    //根据用户的id查找用户的角色个数
    long findUserRoleByUserId(String id);

    //获取指定用户所拥有的角色
    List<String> findRoleIdByUserId(String id);

    //删除该用户的所有角色
    void deleteRoleByUserId(String userid);

    //为该用户添加角色
    void addUserRoles(@Param("userid") String userid, @Param("roleIds") String[] roleIds);

    //根据用户邮箱查找用户
    User findUserByEmail(String email);

}
