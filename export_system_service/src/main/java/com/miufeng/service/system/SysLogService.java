package com.miufeng.service.system;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.system.SysLog;

import java.util.List;


public interface SysLogService {
    //分页查询所有日志列表, 注意：需要根据公司id才查，因为每个公司有自己对应的部门日志
    PageInfo<SysLog> findAll(Integer pageNum, Integer pageSize, String companyId);

    //添加日志
    void save(SysLog sysLog);
   
}
