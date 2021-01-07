package com.miufeng.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.cargo.ContractDao;
import com.miufeng.domain.cargo.Contract;
import com.miufeng.domain.cargo.ContractExample;
import com.miufeng.service.cargo.ContractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: ContractServiceImpl
 * Author:   Administrator
 * Date:     2020/12/29 0029 20:49
 */
@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;

    /**
     * 分页查询
     *
     * @param contractExample 分页查询的参数
     * @param pageNum         当前页
     * @param pageSize        页大小
     * @return
     */
    @Override
    public PageInfo<Contract> findByPage(ContractExample contractExample, int pageNum, int pageSize) {
        //设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);

        //查询所有购销合同
        List<Contract> contracts = contractDao.selectByExample(contractExample);

        //构建应该pageInfo
        PageInfo<Contract> pageInfo = new PageInfo<>(contracts);

        return pageInfo;
    }

    /**
     * 查询所有
     *
     * @param contractExample
     */
    @Override
    public List<Contract> findAll(ContractExample contractExample) {
        return contractDao.selectByExample(contractExample);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    /**
     * 新增
     *
     * @param contract
     */
    @Override
    public void save(Contract contract) {
        contract.setId(UUID.randomUUID().toString());
        contract.setCreateTime(new Date());
        contract.setUpdateTime(new Date());
        contractDao.insertSelective(contract);
    }

    /**
     * 修改
     *
     * @param contract
     */
    @Override
    public void update(Contract contract) {
        contract.setUpdateTime(new Date());
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }


    //根据部门id分页查询购销合同
    @Override
    public PageInfo<Contract> findByPageDeptId(String deptId, int pageNum, int pageSize) {
        //设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);

        //根据部门id查询购销合同
        List<Contract> contracts = contractDao.findByDeptId(deptId);

        //构建应该pageInfo
        PageInfo<Contract> pageInfo = new PageInfo<>(contracts);

        return pageInfo;
    }
}
