package com.miufeng.dao.cargo;

import com.miufeng.domain.cargo.Contract;
import com.miufeng.domain.cargo.ContractExample;

import java.util.List;

public interface ContractDao {

	//删除
    int deleteByPrimaryKey(String id);

	//保存
    int insertSelective(Contract record);

	//条件查询
    List<Contract> selectByExample(ContractExample example);

	//id查询
    Contract selectByPrimaryKey(String id);

	//更新
    int updateByPrimaryKeySelective(Contract record);

    //根据部门id查询购销合同
    List<Contract> findByDeptId(String deptId);
}