package com.miufeng.service.company;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.company.Company;

public interface CompanyService {
    //分页查询所有企业
    PageInfo findByPage(Integer pageNum, Integer pageSize);

    //添加企业
    void add(Company company);

    //修改企业信息
    void update(Company company);

    //根据id查询企业信息
    Company findById(String id);

    //根据id删除企业
    void delete(String id);

}
