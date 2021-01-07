package com.miufeng.dao.system;

import com.miufeng.domain.system.Dept;

import java.util.List;

public interface DeptDao {
    //查询所有部门列表, 注意：需要根据公司id才查，因为每个公司有自己对应的部门
    List<Dept> findAll(String companyId);

    //根据部门id查询部门
    Dept findById(String id);

    //添加部门
    void save(Dept dept);

    //修改部门信息
    void update(Dept dept);

    //查找指定部门列表
    List<Dept> findAllDeptPro(String companyId, String id);

    //根据父部门的id查找部门
    int findByParentId(String id);

    //根据部门id查找用户
    int findByPeptId(String id);

    //根据部门id删除部门
    void delete(String id);
}
