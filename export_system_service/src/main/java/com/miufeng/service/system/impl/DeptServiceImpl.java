package com.miufeng.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.system.DeptDao;
import com.miufeng.domain.system.Dept;
import com.miufeng.service.system.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: DeptServiceImpl
 * Author:   Administrator
 * Date:     2020/12/22 0022 15:06
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    //分页查询所有部门列表
    @Override
    public PageInfo<Dept> findAll(Integer pageNum, Integer pageSize, String companyId) {
        //1.设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);

        //2.查询所有部门list
        List<Dept> list = deptDao.findAll(companyId);

        //3.创建pageInfo，并将list加入
        PageInfo<Dept> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    //根据部门id查询部门
    @Override
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    //查找所有部门
    @Override
    public List<Dept> findAllDept(String companyId) {
        return deptDao.findAll(companyId);
    }

    //查找指定部门列表
    @Override
    public List<Dept> findAllDeptPro(String companyId, String id) {

        return deptDao.findAllDeptPro(companyId, id);
    }

    //添加部门
    @Override
    public void save(Dept dept) {
        //设置唯一部门id
        dept.setId(UUID.randomUUID().toString());

        deptDao.save(dept);
    }

    //修改部门信息
    @Override
    public void update(Dept dept) {
        deptDao.update(dept);
    }

    //根据部门id删除部门
    @Override
    public Boolean delete(String id) {
        //1.查找是否有子部门,和该部门中是否有员工
        int count1 = deptDao.findByParentId(id);
        int count2 = deptDao.findByPeptId(id);

        //2.如果有子部门或部门有员工，直接return false
        if (count1 > 0 || count2 > 0) {
            return false;
        }

        //3.否则，直接删除
        deptDao.delete(id);
        return true;
    }

}
