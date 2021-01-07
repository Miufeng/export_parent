package com.miufeng.service.system;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.system.Role;

import java.util.List;


public interface RoleService {
    //分页查询所有角色列表, 注意：需要根据公司id才查，因为每个公司有自己对应的角色
    PageInfo<Role> findAll(Integer pageNum, Integer pageSize, String companyId);

    //根据角色id查询角色
    Role findById(String id);

    //查找所有角色
    List<Role> findAllRole(String companyId);

    //添加角色
    void save(Role role);

    //修改角色信息
    void update(Role role);

    //根据角色id删除角色
    void delete(String id);

    //根据角色id查找权限id
    List<String> findModuleIdByRoleId(String roleid);

    //保存修改角色权限
    void updateRoleModule(String roleid, String[] moduleIds);
}
