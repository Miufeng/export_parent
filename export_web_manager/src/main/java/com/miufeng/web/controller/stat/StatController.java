package com.miufeng.web.controller.stat;

import com.alibaba.dubbo.config.annotation.Reference;
import com.miufeng.domain.cargo.Factory;
import com.miufeng.domain.cargo.FactoryExample;
import com.miufeng.service.StatService;
import com.miufeng.service.cargo.FactoryService;
import com.miufeng.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2021
 * FileName: StatController
 * Author:   Administrator
 * Date:     2021/1/4 0004 20:02
 */
@Controller
@RequestMapping("/stat")
public class StatController extends BaseController {

    @Reference
    private StatService statService;

    /*
    * 作用：进入对应的统计分析图页面
    * url：/stat/toCharts.do?chartsType=factory
    * 参数：chartsType：统计分析类型
    * 返回值：stat/stat-factory
    *
    * */
    @RequestMapping("/toCharts")
    public String toCharts(String chartsType) {
        return "stat/stat-" + chartsType;
    }

    /*
     * 作用：获取工厂销售额的统计分析图数据
     * url：/stat/getFactoryData.do
     * 参数：无
     * 返回值：工厂销售额的json数据
     *
     * */
    @RequestMapping("/getFactoryData")
    @ResponseBody
    public List<Map<String, Object>> getFactoryData() {

        //1.获取工厂销售额数据
        List<Map<String, Object>> factoryData = statService.getFactoryData(getLoginCompanyId());

        //返回
        return factoryData;
    }

    /*
     * 作用：获取产品销售排行的统计分析图数据
     * url：/stat/getProductData.do
     * 参数：无
     * 返回值：产品销售排行的json数据
     *
     * */
    @RequestMapping("/getProductData")
    @ResponseBody
    public List<Map<String, Object>> getProductData() {

        //1.获取产品销售排行数据
        List<Map<String, Object>> productData = statService.getProductData(getLoginCompanyId());

        //返回
        return productData;
    }

    /*
     * 作用：获取系统访问压力图数据
     * url：/stat/getOnlineData.do
     * 参数：无
     * 返回值：系统访问压力图的json数据
     *
     * */
    @RequestMapping("/getOnlineData")
    @ResponseBody
    public List<Map<String, Object>> getOnlineData() {

        //1.获取产品销售排行数据
        List<Map<String, Object>> onlineData = statService.getOnlineData(getLoginCompanyId());

        //返回
        return onlineData;
    }
}
