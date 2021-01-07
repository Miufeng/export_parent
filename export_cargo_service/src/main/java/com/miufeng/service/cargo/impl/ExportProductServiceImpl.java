package com.miufeng.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.cargo.ExportProductDao;
import com.miufeng.domain.cargo.ExportProduct;
import com.miufeng.domain.cargo.ExportProductExample;
import com.miufeng.service.cargo.ExportProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Copyright (C), 2018-2021
 * FileName: ExportProductProductServiceImpl
 * Author:   Administrator
 * Date:     2021/1/3 0003 15:11
 */
@Service
public class ExportProductServiceImpl implements ExportProductService {
    @Autowired
    private ExportProductDao exportProductDao;

    //根据id查找报运货物
    @Override
    public ExportProduct findById(String id) {
        return exportProductDao.selectByPrimaryKey(id);
    }

    //查找所有报运货物
    @Override
    public List<ExportProduct> findAll(ExportProductExample example) {
        return exportProductDao.selectByExample(example);
    }

    //保存添加的报运货物
    @Override
    public void save(ExportProduct exportProduct) {
        exportProductDao.insertSelective(exportProduct);
    }

    //更新报运货物
    @Override
    public void update(ExportProduct exportProduct) {
        exportProductDao.updateByPrimaryKeySelective(exportProduct);
    }

    //根据id删除报运货物
    @Override
    public void delete(String id) {
        exportProductDao.deleteByPrimaryKey(id);
    }

    //分页查询所有报运货物
    @Override
    public PageInfo<ExportProduct> findByPage(ExportProductExample example, int pageNum, int pageSize) {

        //1.设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);

        //2.查询所有报运货物
        List<ExportProduct> exportProductList = exportProductDao.selectByExample(example);

        //3.创建pageInfo
        PageInfo<ExportProduct> pageInfo = new PageInfo<>(exportProductList);

        return pageInfo;
    }
}
