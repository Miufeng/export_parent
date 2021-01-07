package com.miufeng.service;

import java.util.List;
import java.util.Map;

public interface StatService {

    //查找生产货物的工厂的销售额
    public List<Map<String, Object>> getFactoryData(String companyId);

    //查找货物销售额排行前5
    public List<Map<String, Object>> getProductData(String companyId);

    //分时段查找系统访问次数
    public List<Map<String, Object>> getOnlineData(String companyId);

}
