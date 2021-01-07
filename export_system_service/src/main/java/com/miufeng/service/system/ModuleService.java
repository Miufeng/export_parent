package com.miufeng.service.system;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.system.Module;
import com.miufeng.domain.system.User;

import java.util.List;


public interface ModuleService {

    //分页查询所有模块列表
    PageInfo<Module> findAll(Integer pageNum, Integer pageSize);

    //根据模块id查询模块
    Module findById(String id);

    //查找所有模块
    List<Module> findAllModule();

    //添加模块
    void save(Module module);

    //修改模块信息
    void update(Module module);

    //根据模块id删除模块
    void delete(String id);

    //查询除指定模块外的所有模块
    List<Module> findAllModulePro(String id);

    //根据用户id查找用户对应的权限模块
    List<Module> findModuleByUserId(User user);
}
