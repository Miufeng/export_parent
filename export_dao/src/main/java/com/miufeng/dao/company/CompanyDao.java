package com.miufeng.dao.company;

import com.miufeng.domain.company.Company;

import java.util.List;

public interface CompanyDao {

    //查询所有企业
    List<Company> findAll();

    //添加企业
    void add(Company company);

    //修改企业信息
    void update(Company company);

    //根据id查询企业信息
    Company findById(String id);

    //根据id删除企业
    void delete(String id);
}
