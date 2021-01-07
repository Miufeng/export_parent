package com.miufeng.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (C), 2018-2021
 * FileName: ContractProductVo
 * Author:   Administrator
 * Date:     2021/1/2 0002 20:36
 */
public class ContractProductVo implements Serializable {
    private String customName;		//客户名称
    private String contractNo;		//合同号，订单号
    private String productNo;		//货号
    private Integer cnumber;		//数量
    private String factoryName;		//厂家名称，冗余字段
    private Date deliveryPeriod;	//交货期限
    private Date shipTime;			//船期
    private String tradeTerms;		//贸易条款

    public ContractProductVo() {
    }

    public ContractProductVo(String customName, String contractNo, String productNo, Integer cnumber, String factoryName, Date deliveryPeriod, Date shipTime, String tradeTerms) {
        this.customName = customName;
        this.contractNo = contractNo;
        this.productNo = productNo;
        this.cnumber = cnumber;
        this.factoryName = factoryName;
        this.deliveryPeriod = deliveryPeriod;
        this.shipTime = shipTime;
        this.tradeTerms = tradeTerms;
    }

    /**
     * 获取
     * @return customName
     */
    public String getCustomName() {
        return customName;
    }

    /**
     * 设置
     * @param customName
     */
    public void setCustomName(String customName) {
        this.customName = customName;
    }

    /**
     * 获取
     * @return contractNo
     */
    public String getContractNo() {
        return contractNo;
    }

    /**
     * 设置
     * @param contractNo
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * 获取
     * @return productNo
     */
    public String getProductNo() {
        return productNo;
    }

    /**
     * 设置
     * @param productNo
     */
    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    /**
     * 获取
     * @return cnumber
     */
    public Integer getCnumber() {
        return cnumber;
    }

    /**
     * 设置
     * @param cnumber
     */
    public void setCnumber(Integer cnumber) {
        this.cnumber = cnumber;
    }

    /**
     * 获取
     * @return factoryName
     */
    public String getFactoryName() {
        return factoryName;
    }

    /**
     * 设置
     * @param factoryName
     */
    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    /**
     * 获取
     * @return deliveryPeriod
     */
    public Date getDeliveryPeriod() {
        return deliveryPeriod;
    }

    /**
     * 设置
     * @param deliveryPeriod
     */
    public void setDeliveryPeriod(Date deliveryPeriod) {
        this.deliveryPeriod = deliveryPeriod;
    }

    /**
     * 获取
     * @return shipTime
     */
    public Date getShipTime() {
        return shipTime;
    }

    /**
     * 设置
     * @param shipTime
     */
    public void setShipTime(Date shipTime) {
        this.shipTime = shipTime;
    }

    /**
     * 获取
     * @return tradeTerms
     */
    public String getTradeTerms() {
        return tradeTerms;
    }

    /**
     * 设置
     * @param tradeTerms
     */
    public void setTradeTerms(String tradeTerms) {
        this.tradeTerms = tradeTerms;
    }

    public String toString() {
        return "ContractProductVo{customName = " + customName + ", contractNo = " + contractNo + ", productNo = " + productNo + ", cnumber = " + cnumber + ", factoryName = " + factoryName + ", deliveryPeriod = " + deliveryPeriod + ", shipTime = " + shipTime + ", tradeTerms = " + tradeTerms + "}";
    }
}
