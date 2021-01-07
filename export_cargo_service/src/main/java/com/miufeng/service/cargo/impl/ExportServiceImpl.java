package com.miufeng.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.cargo.*;
import com.miufeng.domain.cargo.*;
import com.miufeng.service.cargo.ExportService;
import com.miufeng.vo.ExportProductResult;
import com.miufeng.vo.ExportResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Copyright (C), 2018-2021
 * FileName: ExportServiceImpl
 * Author:   Administrator
 * Date:     2021/1/3 0003 15:10
 */
@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;

    @Autowired
    private ExportProductDao exportProductDao;

    @Autowired
    private ExtEproductDao extEproductDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private FactoryDao factoryDao;

    //根据id查找报运单
    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    //查找所有报运单
    @Override
    public List<Export> findAll(ExportExample example) {
        return exportDao.selectByExample(example);
    }

    //保存添加的报运单
    @Override
    public void save(Export export) {
        //设置主键
        export.setId(UUID.randomUUID().toString());

        //1.往报运单表插入一条记录
        //给报运单补充数据
        export.setInputDate(new Date());
        //查出该报运单对应的购销合同
        String[] contractIds = export.getContractIds().split(",");//取出该报运单对应的购销合同的id
        ContractExample contractExample = new ContractExample();
        contractExample.createCriteria().andIdIn(Arrays.asList(contractIds));
        List<Contract> contracts = contractDao.selectByExample(contractExample);
        //遍历购销合同
        String contractNos = ""; //定义变量保存所有的合同号
        Integer totalProNum = 0; //货物的种类数量
        Integer totalExtNum = 0; //附件的种类数量
        for (Contract contract : contracts) {
            contractNos += contract.getContractNo() + " ";  //数据库表中的合同号用空格隔开
            totalProNum += contract.getProNum();
            totalExtNum += contract.getExtNum();

            //2.报运单下面的购销合同的状态从1改为2,（购销合同的状态：0（草稿，草稿状态下的合同不能申请报运） 1(允许被报运了) ，  2（已经生成报运单合同））
            contract.setState(2);
            contractDao.updateByPrimaryKeySelective(contract);
        }

        //给报运单设置合同号
        export.setCustomerContract(contractNos);
        //设置状态 报运单的状态：0 (草稿，刚刚生成报运单)， 1（正式报运单，可以报关）,2（已经报关完毕，海关审核完毕了）
        export.setState(0);
        //设置货物和附件的种类数量
        export.setProNum(totalProNum);
        export.setExtNum(totalExtNum);
        //设置创建时间和修改时间
        export.setCreateTime(new Date());
        export.setUpdateTime(new Date());

        //插入
        exportDao.insertSelective(export);


        //3.把报运单下的购销合同下的所有货物数据导入到报运商品表中
        //找到购销合同下的货物数据
        ContractProductExample contractProductExample = new ContractProductExample();
        contractProductExample.createCriteria().andContractIdIn(Arrays.asList(contractIds));
        List<ContractProduct> contractProducts = contractProductDao.selectByExample(contractProductExample);
        //定义一个Map集合存储购销合同货物的id和报运商品的id
        HashMap<String, String> map = new HashMap<>(); //key:购销合同的货物id, value:报运商品的id

        //遍历购销合同货物
        for (ContractProduct contractProduct : contractProducts) {
            //一个购销合同的货物就是一个报运商品
            ExportProduct exportProduct = new ExportProduct();
            //属性拷贝
            BeanUtils.copyProperties(contractProduct, exportProduct); //拷贝条件:两个属性的属性名要一致
            //补充报运商品的id
            exportProduct.setId(UUID.randomUUID().toString());
            //补充该报运商品所属的报运单
            exportProduct.setExportId(export.getId());
            //把购销合同的货物id金额报运商品的id存储起来
            map.put(contractProduct.getId(), exportProduct.getId());

            //插入报运商品数据
            exportProductDao.insertSelective(exportProduct);
        }

        //4.把报运单下的购销合同下的所有货物的附件数据导入到报运商品附件表中
        //找到购销合同的附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractIdIn(Arrays.asList(contractIds));
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);

        //遍历购销合同的附件
        for (ExtCproduct extCproduct : extCproducts) {
            //一个购销合同的附件对应一个报运的附件
            ExtEproduct extEproduct = new ExtEproduct();
            //属性数据拷贝
            BeanUtils.copyProperties(extCproduct, extEproduct);
            //补充数据：主键
            extEproduct.setId(UUID.randomUUID().toString());
            //该报运附件所属的报运单
            extEproduct.setExportId(export.getId());

            //报运附件所属的报运商品的id
            extEproduct.setExportProductId(map.get(extCproduct.getContractProductId()));

            //插入报运附件数据
            extEproductDao.insertSelective(extEproduct);
        }

    }

    //更新报运单
    @Override
    public void update(Export export) {
        //1.更新报运单信息
        export.setUpdateTime(new Date());
        exportDao.updateByPrimaryKeySelective(export);

        //2.更新报运单的商品信息
        List<ExportProduct> exportProducts = export.getExportProducts();
        if (exportProducts != null) {
            for (ExportProduct exportProduct : exportProducts) {
                //更新每一个报运商品信息
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }


        exportDao.updateByPrimaryKeySelective(export);
    }

    //根据id删除报运单
    @Override
    public void delete(String id) {
        exportDao.deleteByPrimaryKey(id);
    }

    //分页查询所有报运单
    @Override
    public PageInfo<Export> findByPage(ExportExample example, int pageNum, int pageSize) {

        //1.设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);

        //2.查询所有报运表
        List<Export> exportList = exportDao.selectByExample(example);

        //3.创建pageInfo
        PageInfo<Export> pageInfo = new PageInfo<>(exportList);

        return pageInfo;
    }

    //根据海关的审核结果更新报运单的信息
    @Override
    public void updateState(ExportResult exportResult) {
        //1.找到对应报运单
        Export export = exportDao.selectByPrimaryKey(exportResult.getExportId());

        //2.更新报运单状态
        export.setState(exportResult.getState());
        exportDao.updateByPrimaryKeySelective(export);

        //3.找到报运单的商品，更新商品的税收
        Set<ExportProductResult> products = exportResult.getProducts();
        if (products != null) {
            for (ExportProductResult product : products) {
                //找到对应的商品
                ExportProduct exportProduct = exportProductDao.selectByPrimaryKey(product.getExportProductId());
                //更新商品的税收
                exportProduct.setTax(product.getTax());

                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }
}
