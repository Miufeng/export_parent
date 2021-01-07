package com.miufeng.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.miufeng.domain.cargo.Contract;
import com.miufeng.domain.cargo.ContractExample;
import com.miufeng.domain.system.User;
import com.miufeng.service.cargo.ContractService;
import com.miufeng.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Copyright (C), 2018-2020
 * FileName: ContractController
 * Author:   Administrator
 * Date:     2020/12/29 0029 20:57
 */
@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;

    /**
     * 分页查询购销合同列表
     * 注意：当前页和每页大小需要设置默认值
     * @param pageNum
     * @param pageSize
     * @return String
     */
    @RequestMapping("/list")
    public String findByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             String searchCon) {

        //创建查询条件
        ContractExample contractExample = new ContractExample();
        //创建条件对象
        ContractExample.Criteria criteria = contractExample.createCriteria();

        //获取当前登录者
        User loginUser = getLoginUser();
        //获取登录者等级
        Integer degree = loginUser.getDegree();

        //判断等级，根据等级查询指定的数据
        /*-- 普通的员工，只能查看自己的购销合同 degree=4
             SELECT * FROM co_contract c WHERE c.`create_by`='当前登陆者id'

             -- 部门经理 ,能够查看自己部门的购销合同 degree=3
             SELECT * FROM co_contract c WHERE c.`create_dept`='当前登陆者所属部门'

             -- 大区经理 ，能够查看自己部门以及下属部门购销合同 degree=2
             SELECT * FROM co_contract c WHERE c.`create_dept` IN (自己的部门，子部门)

             -- 企业管理员 degree=1
             SELECT * FROM co_contract c WHERE c.`company_id`='当前登陆者所属的企业id'
         */
        if (degree == 4) {
            //普通员工
            criteria.andCreateByEqualTo(loginUser.getId());
        }else if (degree == 3) {
            //部门经理
            criteria.andCreateDeptEqualTo(loginUser.getDeptId());
        }else if (degree == 2) {
            //大区经理
            //因为需要使用特定的sql语句进行查询，所以重新写service和dao方法
            PageInfo<Contract> pageInfo = contractService.findByPageDeptId(loginUser.getDeptId(), pageNum, pageSize);
            request.setAttribute("pageInfo", pageInfo);
            return "cargo/contract/contract-list";

        }else if (degree == 1) {
            //企业管理员
            criteria.andCompanyIdEqualTo(loginUser.getCompanyId());
        }

        //添加搜索查询条件
        if (searchCon != null) {
            criteria.andCustomNameLike("%" + searchCon + "%");
        }


        //如果需要排序，排序不是查询，所以排序的代码不是在Criteria对象，在ContractExample
        contractExample.setOrderByClause("create_time desc");

        PageInfo pageInfo = contractService.findByPage(contractExample, pageNum, pageSize);

        //将pageInfo存储到request域中
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("searchCon", searchCon);

        return "cargo/contract/contract-list";
    }

    /**
     * 进入购销合同的添加
     * @return String
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "cargo/contract/contract-add";
    }

    /**
     * 保存新建和修改的购销合同的信息
     * @param contract
     * @return String
     */
    @RequestMapping("/edit")
    public String edit(Contract contract) {
        //给购销合同补充企业的id与企业名称
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());

        //购销合同创建人
        contract.setCreateBy(getLoginUser().getId());

        //购销合同创建人所属部门
        contract.setCreateDept(getLoginUser().getDeptId());

        //根据contract的id是否为空，判断是处理新建或修改的请求
        if (StringUtils.isEmpty(contract.getId())) {
            //id为空是保存新建
            contractService.save(contract);
        }else {
            //id不为空是保存修改
            contractService.update(contract);
        }

        //重定向到cargo/contract-list.jsp（直接return会经过视图解析器）
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 跳转到修改购销合同信息页面，并回显数据
     * @return String
     */
    @RequestMapping("toUpdate")
    public String toUpdate(String id) {
        //根据id查询购销合同信息
        Contract contract = contractService.findById(id);

        //将查询结果存储到request中
        request.setAttribute("contract", contract);

        return "cargo/contract/contract-update";
    }

    /**
     * 根据id删除购销合同
     * @param id
     * @return String
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        contractService.delete(id);

        //返回购销合同列表
        return "redirect:/cargo/contract/list.do";
    }

    /*
      作用 ： 进入更新购销合同详情页面
      url :  /cargo/contract/toView.do?id=dd63eb3c-6d4e-4a85-9c37-fcfda1998c1d
     参数 : 购销合同id
     返回值 :contract-view.jsp
  */
    @RequestMapping("/toView")
    public String toView(String id){
        //1. 根据购销合同的id查找购销合同
        Contract contract =  contractService.findById(id);
        //2. 存储到request域中
        request.setAttribute("contract",contract);
        //3. 请求转发到contract-update页面
        return "cargo/contract/contract-view";
    }


    /*
      作用 ： 购销合同提交
      url :  /cargo/contract/submit.do?id=dd63eb3c-6d4e-4a85-9c37-fcfda1998c1d
     参数 : 购销合同id
     返回值 :购销合同列表页面
  */
    @RequestMapping("/submit")
    public String submit(String id){
        //1. 根据购销合同的id查找购销合同
        Contract contract =  contractService.findById(id);
        //2. 修改购销合同的状态为1,更新购销合同
        contract.setState(1);
        contractService.update(contract);
        //3. 返回购销合同列表页面
        return "redirect:/cargo/contract/list.do";
    }

    /*
       作用 ： 购销合同取消
       url :  /cargo/contract/submit.do?id=dd63eb3c-6d4e-4a85-9c37-fcfda1998c1d
      参数 : 购销合同id
      返回值 :购销合同列表页面
    */
    @RequestMapping("/cancel")
    public String cancel(String id){
        //1. 根据购销合同的id查找购销合同
        Contract contract =  contractService.findById(id);
        //2. 修改购销合同的状态为0,更新购销合同
        contract.setState(0);
        contractService.update(contract);
        //3. 返回购销合同列表页面
        return "redirect:/cargo/contract/list.do";
    }

}
