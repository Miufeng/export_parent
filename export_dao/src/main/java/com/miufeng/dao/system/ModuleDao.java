package com.miufeng.dao.system;

import com.miufeng.domain.system.Module;

import java.util.List;

public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    void delete(String moduleId);

    //添加
    void save(Module module);

    //更新
    void update(Module module);

    //查询全部
    List<Module> findAll();

    //查询除指定模块外的所有模块
    List<Module> findAllModulePro(String id);

    //根据用户等级查找对应权限模块
    List<Module> findModuleByDegree(Integer degree);

    //根据用户id查找用户对应的权限模块
    List<Module> findModuleByUserId(String id);
}
