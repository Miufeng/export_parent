package com.miufeng.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.miufeng.domain.cargo.ExtCproduct;
import com.miufeng.domain.cargo.ExtCproductExample;
import com.miufeng.domain.cargo.Factory;
import com.miufeng.domain.cargo.FactoryExample;
import com.miufeng.service.cargo.ExtCproductService;
import com.miufeng.service.cargo.FactoryService;
import com.miufeng.web.controller.BaseController;
import com.miufeng.web.utils.FileUploadUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Copyright (C), 2018-2020
 * FileName: ExtCproductController
 * Author:   Administrator
 * Date:     2020/12/30 0030 17:22
 */
@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {
    @Reference
    private ExtCproductService extCproductService;

    @Reference
    private FactoryService factoryService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    /*
        作用：进入附件列表页面
        url: /cargo/extCproduct/list.do?contractId=75ab5619-6a3c-458f-ba6f-e68453a62906&contractProductId=1c226111-7bef-40bb-bc7b-59a63243c0ee
        参数：contractId: 购销合同id, contractProductId: 货物id
        返回值 : /cargo/extc/extc-list
     */
    @RequestMapping("/list")
    public String findByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             String contractId,
                             String contractProductId) {
        //1. 查询当前货物的附件数据,得到pageInfo
        //创建查询条件
        ExtCproductExample extCproductExample = new ExtCproductExample();

        //添加条件，只能查看指定货物的附件
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);

        PageInfo pageInfo = extCproductService.findByPage(extCproductExample, pageNum, pageSize);
        //将pageInfo存储到request域中
        request.setAttribute("pageInfo", pageInfo);

        //2. 查询生成附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);

        //3. 把购销合同id和货物id存储到域中
        request.setAttribute("contractId", contractId);
        request.setAttribute("contractProductId", contractProductId);

        return "/cargo/extc/extc-list";
    }

    /*
    作用：添加附件，更新附件
    url:  /cargo/extCproduct/edit.do
    参数：ExtCproduct: 附件对象信息
    返回值 : 附件列表页面
    */
     @RequestMapping("edit")
     public String edit(ExtCproduct extCproduct, MultipartFile productPhoto) throws Exception {//注意： 如果是文件上传字段类型必须是MultipartFile类型
         //附件的创建人所属的企业的id
         extCproduct.setCompanyId(getLoginCompanyId());
         //附件的创建人所属的企业的名称
         extCproduct.setCompanyName(getLoginCompanyName());

         //上传文件
         if(productPhoto.getSize()>0){
             //如果大小大于0则是有上传,把照片保存七牛云上
             String url = fileUploadUtil.upload(productPhoto);
             //把图片的url保存到附件中
             extCproduct.setProductImage("http://"+url);
         }

         if (StringUtils.isEmpty(extCproduct.getId())) {
             //添加操作
             extCproductService.save(extCproduct);
         }else {
             //修改操作
             extCproductService.update(extCproduct);
         }

         return "redirect:/cargo/extCproduct/list.do?contractId=" + extCproduct.getContractId() + "&contractProductId=" + extCproduct.getContractProductId();
     }

    /*
   作用：  进入更新附件页面
   url:  /cargo/extCproduct/toUpdate.do?id=4731682d-d6b6-4e9b-bc4e-f936bd08c8f5&contractId=75ab5619-6a3c-458f-ba6f-e68453a62906&contractProductId=1c226111-7bef-40bb-bc7b-59a63243c0ee
   参数： id：附件id， contractId 购销合同id， contractProductId 货物id
   返回值 : cargo/extc/extc-update
   */
    @RequestMapping("toUpdate")
    public String toUpdate(String id, String contractId, String contractProductId) {
        //1.根据id查找附件
        ExtCproduct extCproduct = extCproductService.findById(id);
        request.setAttribute("extCproduct", extCproduct);

        //2.查询生成附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);

        //3. 把购销合同id和货物id存储到域中
        request.setAttribute("contractId", contractId);
        request.setAttribute("contractProductId", contractProductId);

        return "cargo/extc/extc-update";
    }

    /*
   作用：  删除附件
   url:  /cargo/extCproduct/delete.do?id=4731682d-d6b6-4e9b-bc4e-f936bd08c8f5&contractId=75ab5619-6a3c-458f-ba6f-e68453a62906&contractProductId=1c226111-7bef-40bb-bc7b-59a63243c0ee
   参数： id：附件id， contractId 购销合同id， contractProductId 货物id
   返回值 : 返回附件列表
   */
    @RequestMapping("delete")
    public String delete(String id, String contractId, String contractProductId) {
        //删除
        extCproductService.delete(id);
        //重新进入附件列表页面
        return "redirect:/cargo/extCproduct/list.do?contractId=" + contractId + "&contractProductId=" + contractProductId;
    }

}
