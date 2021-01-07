package com.miufeng.domain.system;

/**
 * Copyright (C), 2018-2020
 * FileName: Role
 * Author:   Administrator
 * Date:     2020/12/23 0023 14:36
 */
public class Role {
    private String id;
    private String name;
    private String remark;
    private Long orderNo;
    private String companyId;
    private String companyName;

    public Role() {
    }

    public Role(String id, String name, String remark, Long orderNo, String companyId, String companyName) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.orderNo = orderNo;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    /**
     * 获取
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取
     * @return orderNo
     */
    public Long getOrderNo() {
        return orderNo;
    }

    /**
     * 设置
     * @param orderNo
     */
    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取
     * @return companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * 设置
     * @param companyId
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String toString() {
        return "Role{id = " + id + ", name = " + name + ", remark = " + remark + ", orderNo = " + orderNo + ", companyId = " + companyId + ", companyName = " + companyName + "}";
    }
}
