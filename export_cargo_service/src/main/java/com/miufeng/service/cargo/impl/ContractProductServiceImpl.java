package com.miufeng.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.cargo.ContractDao;
import com.miufeng.dao.cargo.ContractProductDao;
import com.miufeng.dao.cargo.ExtCproductDao;
import com.miufeng.domain.cargo.*;
import com.miufeng.service.cargo.ContractProductService;
import com.miufeng.vo.ContractProductVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: ContractProductServiceImpl
 * Author:   Administrator
 * Date:     2020/12/30 0030 17:14
 */
@Service
public class ContractProductServiceImpl implements ContractProductService {
    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    /**
     * 货物分页查询
     *
     * @param contractProductExample 分页查询的参数
     * @param pageNum                当前页
     * @param pageSize               页大小
     * @return
     */
    @Override
    public PageInfo<ContractProduct> findByPage(ContractProductExample contractProductExample, int pageNum, int pageSize) {
        //设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);
        //查询所有
        List<ContractProduct> contractProducts = contractProductDao.selectByExample(contractProductExample);
        //设置pageInfo
        PageInfo<ContractProduct> pageInfo = new PageInfo<>(contractProducts);

        return pageInfo;
    }

    /**
     * 查询所有货物
     *
     * @param contractProductExample
     */
    @Override
    public List<ContractProduct> findAll(ContractProductExample contractProductExample) {
        return contractProductDao.selectByExample(contractProductExample);
    }

    /**
     * 根据id查询货物
     *
     * @param id
     */
    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    /**
     * 新增货物
     *
     * @param contractProduct
     */
    @Override
    public void save(ContractProduct contractProduct) {
        contractProduct.setId(UUID.randomUUID().toString());

        //1.计算货物的总价
        double amount = 0;
        //由于cnumber与price我们都使用包装类，所以可以用null去判断
        if (contractProduct.getCnumber() != null && contractProduct.getPrice() != null) {
            amount = contractProduct.getCnumber() * contractProduct.getPrice();
            contractProduct.setAmount(amount);
        }

        //2.插入货物的数据
        contractProductDao.insertSelective(contractProduct);

        //3.重新计算购销合同的总价 购销合同总价=购销合同原价格+货物总价
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        if (contract.getTotalAmount() != null) {
            contract.setTotalAmount(contract.getTotalAmount() + amount);
        }else {
            //该购销合同为第一次添加货物
            contract.setTotalAmount(amount);
        }

        //4.更新购销合同的种类数量  购销合同的种类数量 = 购销合同的原种类数量+1
        if (contract.getProNum() != null) {
            contract.setProNum(contract.getProNum() + 1);
        }else {
            contract.setProNum(1);
        }

        //5.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);

    }

    /**
     * 修改货物信息
     *
     * @param contractProduct
     */
    @Override
    public void update(ContractProduct contractProduct) {
        //1.更新之前查询该货物的对象
        ContractProduct oldContractProduct = contractProductDao.selectByPrimaryKey(contractProduct.getId());

        //2.计算货物的总价
        double amount = 0;
        //由于cnumber与price我们都使用包装类，所以可以用null去判断
        if (contractProduct.getCnumber() != null && contractProduct.getPrice() != null) {
            amount = contractProduct.getCnumber() * contractProduct.getPrice();
            contractProduct.setAmount(amount);
        }

        //3.更新货物的数据
        contractProductDao.updateByPrimaryKeySelective(contractProduct);

        //4.重新计算购销合同的总价   购销合同总价 = 购销合同原价格- 货物原本价格+ 当前的新总价
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() - oldContractProduct.getAmount() + amount);

        //5.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除货物
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        //1.找到删除的货物
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);

        //2.删除货物
        contractProductDao.deleteByPrimaryKey(id);

        //3.删除该货物对应的附件
        //找到货物对应的附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        //遍历附件，逐个删除
        double totalExtAmount = 0;
        if (extCproducts != null) {
            for (ExtCproduct extCproduct : extCproducts) {
                //统计附件的总价
                totalExtAmount += extCproduct.getAmount();
                //删除附件
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
        }

        //4.重新计算购销合同的总价   购销合同总价 = 购销合同原价格- 被删除货物总价 - 附件总价
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() - contractProduct.getAmount() - totalExtAmount);

        //5.更新购销合同的货物数量
        contract.setProNum(contract.getProNum() - 1);
        //6.更新购销合同的附件数量
        if (extCproducts != null) {
            contract.setExtNum(contract.getExtNum() - extCproducts.size());
        }

        //7.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    //根据船期查询货物
    @Override
    public List<ContractProductVo> findByShipTime(String shipTime, String companyId) {
        return contractProductDao.findByShipTime(shipTime, companyId);
    }
}
