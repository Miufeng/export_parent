package com.miufeng.service.system;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.system.Dept;

import java.util.List;


public interface DeptService {
    //分页查询所有部门列表, 注意：需要根据公司id才查，因为每个公司有自己对应的部门
    PageInfo<Dept> findAll(Integer pageNum, Integer pageSize, String companyId);

    //根据部门id查询部门
    Dept findById(String id);

    //查找所有部门
    List<Dept> findAllDept(String companyId);

    //查找指定部门列表
    List<Dept> findAllDeptPro(String companyId, String id);

    //添加部门
    void save(Dept dept);

    //修改部门信息
    void update(Dept dept);

    //根据部门id删除部门
    Boolean delete(String id);
}
