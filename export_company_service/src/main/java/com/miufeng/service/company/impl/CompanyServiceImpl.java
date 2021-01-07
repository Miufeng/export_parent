package com.miufeng.service.company.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.company.CompanyDao;
import com.miufeng.domain.company.Company;
import com.miufeng.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: CompanyServiceImpl
 * Author:   Administrator
 * Date:     2020/12/19 0019 16:13
 */
@Service      //换成阿里巴巴的
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    //分页查询所有企业
    @Override
    public PageInfo findByPage(Integer pageNum, Integer pageSize) {
        //设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);

        //查询所有数据
        List<Company> list = companyDao.findAll();

        //创建pageInfo对象
        PageInfo<Company> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    //添加企业
    @Override
    public void add(Company company) {
        //用UUID设置唯一id
        company.setId(UUID.randomUUID().toString());

        companyDao.add(company);
    }

    //修改企业信息
    @Override
    public void update(Company company) {
        companyDao.update(company);
    }

    //根据id查询企业信息
    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    //根据id删除企业
    @Override
    public void delete(String id) {
        companyDao.delete(id);
    }
}
