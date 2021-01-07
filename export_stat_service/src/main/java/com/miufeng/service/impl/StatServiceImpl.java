package com.miufeng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.miufeng.dao.stat.StatDao;
import com.miufeng.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2021
 * FileName: StatServiceImpl
 * Author:   Administrator
 * Date:     2021/1/4 0004 19:53
 */
@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private StatDao statDao;

    //查找生产货物的工厂的销售额
    @Override
    public List<Map<String, Object>> getFactoryData(String companyId) {
        return statDao.getFactoryData(companyId);
    }

    //查找货物销售额排行前5
    @Override
    public List<Map<String, Object>> getProductData(String companyId) {
        return statDao.getProductData(companyId);
    }

    //分时段查找系统访问次数
    @Override
    public List<Map<String, Object>> getOnlineData(String companyId) {
        return statDao.getOnlineData(companyId);
    }

}
