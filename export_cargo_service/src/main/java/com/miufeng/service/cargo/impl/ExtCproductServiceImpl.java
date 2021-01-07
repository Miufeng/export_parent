package com.miufeng.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.cargo.ContractDao;
import com.miufeng.dao.cargo.ExtCproductDao;
import com.miufeng.domain.cargo.Contract;
import com.miufeng.domain.cargo.ExtCproduct;
import com.miufeng.domain.cargo.ExtCproductExample;
import com.miufeng.service.cargo.ExtCproductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: ExtCproductServiceImpl
 * Author:   Administrator
 * Date:     2021/01/02 0030 17:28
 */
@Service
public class ExtCproductServiceImpl implements ExtCproductService {
    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ContractDao contractDao;

    /**
     * 附件分页查询
     *
     * @param extCproductExample
     * @param pageNum
     * @param pageSize
     */
    @Override
    public PageInfo<ExtCproduct> findByPage(ExtCproductExample extCproductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //查询数据
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(extCproductExample);
        //得到附件页面
        PageInfo<ExtCproduct> pageInfo = new PageInfo<>(extCproductList);
        return pageInfo;
    }

    /**
     * 查询所有附件
     *
     * @param extCproductExample
     */
    @Override
    public List<ExtCproduct> findAll(ExtCproductExample extCproductExample) {
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(extCproductExample);
        return extCproductList;
    }

    /**
     * 根据id查询附件
     *
     * @param id
     * @return
     */
    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    /**
     * 新增附件
     *
     * @param extCproduct
     */
    @Override
    public void save(ExtCproduct extCproduct) {
        extCproduct.setId(UUID.randomUUID().toString());

        //1.计算附件总价
        double amount = 0;
        if (extCproduct.getCnumber() != null && extCproduct.getPrice() != null) {
            amount = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        extCproduct.setAmount(amount);

        //2.插入附件
        extCproductDao.insertSelective(extCproduct);

        //3.更新购销合同的总价
        //获取购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //更新总价
        contract.setTotalAmount(contract.getTotalAmount() + amount);

        //4.更新购销合同的附件种类数量
        if (contract.getExtNum() != null) {
            contract.setExtNum(contract.getExtNum() + 1);
        }else {
            contract.setExtNum(1);
        }

        //5.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 修改附件信息
     *
     * @param extCproduct
     */
    @Override
    public void update(ExtCproduct extCproduct) {

        //1.获取附件旧总价
        //获取未更新前附件
        ExtCproduct oldExtCproduct = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        Double oldAmount = 0.0;
        if (oldExtCproduct.getAmount() != null) {
            oldAmount = oldExtCproduct.getAmount();
        }

        //1.计算附件总价
        double amount = 0;
        if (extCproduct.getCnumber() != null && extCproduct.getPrice() != null) {
            amount = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        extCproduct.setAmount(amount);

        //2.更新附件
        extCproductDao.updateByPrimaryKeySelective(extCproduct);

        //3.更新购销合同的总价
        //获取购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //更新总价
        contract.setTotalAmount(contract.getTotalAmount() - oldAmount + amount);

        //4.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除附件
     *
     * @param id
     */
    @Override
    public void delete(String id) {

        //1.获取被删除的附件
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);

        //2.删除附件
        extCproductDao.deleteByPrimaryKey(id);

        //3.更新购销合同总价
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() - extCproduct.getAmount());

        //4.更新购销合同附件种类数量
        contract.setExtNum(contract.getExtNum() - 1);

        //5.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }
}
