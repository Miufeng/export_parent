package com.miufeng.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.miufeng.domain.cargo.ContractProduct;
import com.miufeng.domain.cargo.ContractProductExample;
import com.miufeng.domain.cargo.Factory;
import com.miufeng.domain.cargo.FactoryExample;
import com.miufeng.service.cargo.ContractProductService;
import com.miufeng.service.cargo.FactoryService;
import com.miufeng.web.controller.BaseController;
import com.miufeng.web.utils.FileUploadUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Copyright (C), 2018-2020
 * FileName: ContractProductController
 * Author:   Administrator
 * Date:     2020/12/30 0030 17:22
 */
@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {
    @Reference
    private ContractProductService contractProductService;

    @Reference
    private FactoryService factoryService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    /*
        作用：进入货物列表页面
        url: /cargo/contractProduct/list.do?contractId=dd63eb3c-6d4e-4a85-9c37-fcfda1998c1d
        参数：购销合同id
        返回值 : 货物列表页面
     */
    @RequestMapping("/list")
    public String findByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             String contractId) {
        //1. 查询当前购销合同下的货物数据,得到pageInfo
        //创建查询条件
        ContractProductExample contractProductExample = new ContractProductExample();

        //添加条件，只能查看指定购销合同的货物
        contractProductExample.createCriteria().andContractIdEqualTo(contractId);

        PageInfo pageInfo = contractProductService.findByPage(contractProductExample, pageNum, pageSize);
        //将pageInfo存储到request域中
        request.setAttribute("pageInfo", pageInfo);

        //2. 查询生成货物的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);

        //3. 把购销合同id存储到域中
        request.setAttribute("contractId", contractId);

        return "cargo/product/product-list";
    }

    /*
    作用：添加货物，更新货物
    url:  /contractProduct/edit.do
    参数：ContractProduct 货物对象信息
    返回值 : 货物列表页面
    */
     @RequestMapping("edit")
     public String edit(ContractProduct contractProduct, MultipartFile productPhoto) throws Exception {//注意： 如果是文件上传字段类型必须是MultipartFile类型
         //货物的创建人
         contractProduct.setCreateBy(getLoginUser().getId());
         //货物的创建人所属的部门
         contractProduct.setCreateDept(getLoginUser().getDeptId());
         //货物的创建人所属的企业的id
         contractProduct.setCompanyId(getLoginCompanyId());
         //货物的创建人所属的企业的名称
         contractProduct.setCompanyName(getLoginCompanyName());

         //上传文件
         if(productPhoto.getSize()>0){
             //如果大小大于0则是有上传,把照片保存七牛云上
             String url = fileUploadUtil.upload(productPhoto);
             //把图片的url保存到货物中
             contractProduct.setProductImage("http://"+url);
         }


         if (StringUtils.isEmpty(contractProduct.getId())) {
             //添加操作
             contractProductService.save(contractProduct);
         }else {
             //修改操作
             contractProductService.update(contractProduct);
         }

         return "redirect:/cargo/contractProduct/list.do?contractId=" + contractProduct.getContractId();
     }

    /*
   作用：  进入更新货物页面
   url:  /cargo/contractProduct/toUpdate.do?id=d3da0636-0f05-49f2-aa98-809729c9127d
   参数： 货物id
   返回值 : /cargo/product/product-update
   */
    @RequestMapping("toUpdate")
    public String toUpdate(String id) {
        //1.根据id查找货物
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct", contractProduct);

        //2.查询生成货物的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);

        return "cargo/product/product-update";
    }

    /*
   作用：  删除货物
   url:  /cargo/contractProduct/delete.do?id=d3da0636-0f05-49f2-aa98-809729c9127d
   参数： 货物id
   返回值 : 返回货物列表
   */
    @RequestMapping("delete")
    public String delete(String id, String contractId) {
        //删除
        contractProductService.delete(id);
        //重新进入货物列表页面
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }

    /*
       作用 ：进入上传货物页面
       url : /cargo/contractProduct/toImport.do?contractId=75ab5619-6a3c-458f-ba6f-e68453a62906
      参数 : 购销合同id
      返回值 :cargo/product/product-import
    */
    @RequestMapping("/toImport")
    public String toImport(String contractId){
        //1.将contractId存到request域
        request.setAttribute("contractId", contractId);

        //2. 返回上传货物页面
        return "cargo/product/product-import";
    }

    /*
     * 作用：保存excel上传货物数据
     * url：/cargo/contractProduct/import.do
     * 参数：contractId: 购销合同的id, file: 上传的excel文件
     * 返回值：货物列表
     * */
    @RequestMapping("/import")
    public String importExcel(String contractId, MultipartFile file) throws IOException {
        //1.创建工作簿，并且传入上传文件的输入流
        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        //2.得到工作单
        Sheet sheet = workbook.getSheetAt(0);

        //3.得到行数
        int rows = sheet.getPhysicalNumberOfRows();

        //4.遍历所有的行，每一行的数据就是ContractProduct的数据，封装数据
        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);
            //每一行对应一个货物对象
            ContractProduct contractProduct = new ContractProduct();

            //生产厂家
            String factoryName = row.getCell(1).getStringCellValue();
            if (factoryName != null) {
                contractProduct.setFactoryName(factoryName);
                //补充厂家的id
                //根据厂家名称查询厂家
                Factory factory = factoryService.findByFactoryName(factoryName);
                contractProduct.setFactoryId(factory.getId());
            }

            //货号
            String productNo = row.getCell(2).getStringCellValue();
            if (productNo != null) {
                contractProduct.setProductNo(productNo);
            }

            //数量
            Double cnumber = row.getCell(3).getNumericCellValue();
            if (cnumber != null) {
                contractProduct.setCnumber(cnumber.intValue());
            }

            //包装单位
            String packingUnit = row.getCell(4).getStringCellValue();
            if (packingUnit != null) {
                contractProduct.setPackingUnit(packingUnit);
            }

            //装率
            Double loadingRate = row.getCell(5).getNumericCellValue();
            if (loadingRate != null) {
                contractProduct.setLoadingRate(loadingRate + "");
            }

            //箱数
            Double boxNum = row.getCell(6).getNumericCellValue();
            if (boxNum != null) {
                contractProduct.setBoxNum(boxNum.intValue());
            }

            //单价
            Double price = row.getCell(7).getNumericCellValue();
            if (price != null) {
                contractProduct.setPrice(price);
            }

            //因为描述和要求在excel中可为空，为避免getStringCellValue()出现空指针异常
            try {
                //货物描述
                String productDesc = row.getCell(8).getStringCellValue();
                contractProduct.setProductDesc(productDesc);

                //要求
                String productRequest = row.getCell(9).getStringCellValue();
                contractProduct.setProductRequest(productRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //补充数据，货物所属的购销合同
            contractProduct.setContractId(contractId);

            //添加货物数据
            contractProductService.save(contractProduct);

        }

        //返回货物列表
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }

}
