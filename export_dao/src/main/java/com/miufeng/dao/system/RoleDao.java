package com.miufeng.dao.system;

import com.miufeng.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {
    //根据id查询
    Role findById(String id);

    //查询全部
    List<Role> findAll(String companyId);

    //根据id删除
    void delete(String id);

    //添加
    void save(Role role);

    //更新
    void update(Role role);

    //根据角色id查找权限id
    List<String> findModuleIdByRoleId(String roleid);

    //删除指定角色的所有权限
    void deleteModuleByRoleId(String roleid);

    //为指定的角色添加权限
    void addRoleModules(@Param("roleid") String roleid, @Param("moduleIds") String[] moduleIds);
}
