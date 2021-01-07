package com.miufeng.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.cargo.FactoryDao;
import com.miufeng.domain.cargo.Factory;
import com.miufeng.domain.cargo.FactoryExample;
import com.miufeng.service.cargo.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: FactoryServiceImpl
 * Author:   Administrator
 * Date:     2020/12/30 0030 17:28
 */
@Service
public class FactoryServiceImpl implements FactoryService {
    @Autowired
    private FactoryDao factoryDao;

    /**
     * 工厂分页查询
     *
     * @param factoryExample
     * @param pageNum
     * @param pageSize
     */
    @Override
    public PageInfo<Factory> findByPage(FactoryExample factoryExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //查询数据
        List<Factory> factoryList = factoryDao.selectByExample(factoryExample);
        //得到工厂页面
        PageInfo<Factory> pageInfo = new PageInfo<>(factoryList);
        return pageInfo;
    }

    /**
     * 查询所有工厂
     *
     * @param factoryExample
     */
    @Override
    public List<Factory> findAll(FactoryExample factoryExample) {
        List<Factory> factoryList = factoryDao.selectByExample(factoryExample);
        return factoryList;
    }

    /**
     * 根据id查询工厂
     *
     * @param id
     * @return
     */
    @Override
    public Factory findById(String id) {
        return factoryDao.selectByPrimaryKey(id);
    }

    /**
     * 新增工厂
     *
     * @param factory
     */
    @Override
    public void save(Factory factory) {
        factory.setId(UUID.randomUUID().toString());
        //设置两个不为空字段
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());
        factoryDao.insertSelective(factory);
    }

    /**
     * 修改工厂信息
     *
     * @param factory
     */
    @Override
    public void update(Factory factory) {
        //修改更新的时间
        factory.setUpdateTime(new Date());
        factoryDao.updateByPrimaryKeySelective(factory);
    }

    /**
     * 删除工厂
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        factoryDao.deleteByPrimaryKey(id);
    }

    //根据厂家名称查询厂家
    @Override
    public Factory findByFactoryName(String factoryName) {
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andFactoryNameEqualTo(factoryName);
        List<Factory> factoryList = factoryDao.selectByExample(factoryExample);
        if (factoryList.size() > 0) {
            return factoryList.get(0);
        }
        return null;
    }
}
