package com.miufeng.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miufeng.dao.system.SysLogDao;
import com.miufeng.domain.system.SysLog;
import com.miufeng.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: SysLogServiceImpl
 * Author:   Administrator
 * Date:     2020/12/22 0022 15:06
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    //分页查询所有日志列表
    @Override
    public PageInfo<SysLog> findAll(Integer pageNum, Integer pageSize, String companyId) {
        //1.设置当前页和每页大小
        PageHelper.startPage(pageNum, pageSize);

        //2.查询所有日志list
        List<SysLog> list = sysLogDao.findAll(companyId);

        //3.创建pageInfo，并将list加入
        PageInfo<SysLog> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    //添加日志
    @Override
    public void save(SysLog sysLog) {
        //设置唯一日志id
        sysLog.setId(UUID.randomUUID().toString());

        sysLogDao.save(sysLog);
    }

}
