package com.miufeng.domain.system;

/**
 * Copyright (C), 2018-2020
 * FileName: User
 * Author:   Administrator
 * Date:     2020/12/22 0022 20:42
 */
public class User {
    private String id;
    private String deptId;
    private String deptName;
    private String email;
    private String userName;
    private String password;
    /**
     * 1启用0停用
     */
    private Integer state;
    private String managerId;
    private String joinDate;
    private Double salary;
    private String birthday;
    private String gender;
    private String station;
    private String telephone;
    /**
     * 0作为内部控制，租户企业不能使用
     *      0-saas管理员
     *      1-企业管理员
     *      2-管理所有下属部门和人员
     *      3-管理本部门
     *      4-普通员工
     */
    private Integer degree;
    private String remark;
    private Integer orderNo;
    private String companyId;
    private String companyName;

    public User() {
    }

    public User(String id, String deptId, String deptName, String email, String userName, String password, Integer state, String managerId, String joinDate, Double salary, String birthday, String gender, String station, String telephone, Integer degree, String remark, Integer orderNo, String companyId, String companyName) {
        this.id = id;
        this.deptId = deptId;
        this.deptName = deptName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.state = state;
        this.managerId = managerId;
        this.joinDate = joinDate;
        this.salary = salary;
        this.birthday = birthday;
        this.gender = gender;
        this.station = station;
        this.telephone = telephone;
        this.degree = degree;
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
     * @return deptId
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * 设置
     * @param deptId
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取
     * @return deptName
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * 设置
     * @param deptName
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * 获取
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return state
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置
     * @param state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取
     * @return managerId
     */
    public String getManagerId() {
        return managerId;
    }

    /**
     * 设置
     * @param managerId
     */
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    /**
     * 获取
     * @return joinDate
     */
    public String getJoinDate() {
        return joinDate;
    }

    /**
     * 设置
     * @param joinDate
     */
    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * 获取
     * @return salary
     */
    public Double getSalary() {
        return salary;
    }

    /**
     * 设置
     * @param salary
     */
    public void setSalary(Double salary) {
        this.salary = salary;
    }

    /**
     * 获取
     * @return birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * 设置
     * @param birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 获取
     * @return station
     */
    public String getStation() {
        return station;
    }

    /**
     * 设置
     * @param station
     */
    public void setStation(String station) {
        this.station = station;
    }

    /**
     * 获取
     * @return telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置
     * @param telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 获取
     * @return degree
     */
    public Integer getDegree() {
        return degree;
    }

    /**
     * 设置
     * @param degree
     */
    public void setDegree(Integer degree) {
        this.degree = degree;
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
    public Integer getOrderNo() {
        return orderNo;
    }

    /**
     * 设置
     * @param orderNo
     */
    public void setOrderNo(Integer orderNo) {
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
        return "User{id = " + id + ", deptId = " + deptId + ", deptName = " + deptName + ", email = " + email + ", userName = " + userName + ", password = " + password + ", state = " + state + ", managerId = " + managerId + ", joinDate = " + joinDate + ", salary = " + salary + ", birthday = " + birthday + ", gender = " + gender + ", station = " + station + ", telephone = " + telephone + ", degree = " + degree + ", remark = " + remark + ", orderNo = " + orderNo + ", companyId = " + companyId + ", companyName = " + companyName + "}";
    }
}
