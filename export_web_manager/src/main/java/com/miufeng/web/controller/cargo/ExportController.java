package com.miufeng.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.miufeng.domain.cargo.*;
import com.miufeng.domain.system.User;
import com.miufeng.service.cargo.ContractService;
import com.miufeng.service.cargo.ExportProductService;
import com.miufeng.service.cargo.ExportService;
import com.miufeng.service.cargo.FactoryService;
import com.miufeng.vo.ExportProductVo;
import com.miufeng.vo.ExportResult;
import com.miufeng.vo.ExportVo;
import com.miufeng.web.controller.BaseController;
import com.miufeng.web.utils.BeanMapUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Copyright (C), 2018-2021
 * FileName: ExportController
 * Author:   Administrator
 * Date:     2021/1/3 0003 14:58
 */
@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    @Reference
    private ContractService contractService;

    @Reference
    private ExportService exportService;

    @Reference
    private ExportProductService exportProductService;

    @Reference
    private FactoryService factoryService;

    /*
    * 作用：进入合同管理页面
    * url: /cargo/export/contractList.do
    * 参数：pageNum:当前页， pageSize：每页大小
    * 返回值：cargo/export/export-contractList
    * */
    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "5") Integer pageSize) {

        //1.创建购销合同查询条件
        ContractExample contractExample = new ContractExample();
        contractExample.createCriteria().andCompanyIdEqualTo(getLoginCompanyId()).andStateEqualTo(1);

        //2.根据创建时间排序
        contractExample.setOrderByClause("create_time desc");

        //3.分页查询
        PageInfo<Contract> pageInfo = contractService.findByPage(contractExample, pageNum, pageSize);

        //4.将pageInfo存储到request域中
        request.setAttribute("pageInfo", pageInfo);

        //5.跳转到合同管理页面
        return "cargo/export/export-contractList";
    }

    /*
     * 作用：进入出口报运列表页面
     * url: /cargo/export/list.do
     * 参数：pageNum:当前页， pageSize：每页大小
     * 返回值：cargo/export/export-list
     * */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "5") Integer pageSize) {

        //1.创建报运表查询条件
        ExportExample exportExample = new ExportExample();
        exportExample.createCriteria().andCompanyIdEqualTo(getLoginCompanyId());

        //2.根据创建时间排序
        exportExample.setOrderByClause("create_time desc");

        //3.分页查询
        PageInfo<Export> pageInfo = exportService.findByPage(exportExample, pageNum, pageSize);

        //4.将pageInfo存储到request域中
        request.setAttribute("pageInfo", pageInfo);

        //5.跳转到合同管理页面
        return "cargo/export/export-list";
    }

    /*
     * 作用：查看报运单详情
     * url: /cargo/export/toView.do?id=${o.id}
     * 参数：id: 报运单id
     * 返回值：cargo/export/export-view
     * */
    @RequestMapping("/toView")
    public String toView(String id) {
        //1.根据id查询报运单
        Export export = exportService.findById(id);
        //2.将export存储到request域
        request.setAttribute("export", export);
        //3.跳转到报运单详情页面
        return "cargo/export/export-view";
    }

    /*
     * 作用：进入添加报运单页面
     * url: /cargo/export/toExport.do
     * 参数：id: 选中的购销合同的id组成的字符串，例如：1, 2, 3
     * 返回值：cargo/export/export-toExport
     * */
    @RequestMapping("/toExport")
    public String toExport(String id) {
        //1.将购销合同的id字符串存储到request域
        request.setAttribute("id", id);
        //3.跳转到报运单详情页面
        return "cargo/export/export-toExport";
    }

    /*
     * 作用：保存添加和修改报运单
     * url: /cargo/export/edit.do
     * 参数：export: 报运单
     * 返回值：进入报运单列表
     * */
    @RequestMapping("/edit")
    public String edit(Export export) {
        //1.补全信息
        //报运单创建人信息
        export.setCreateBy(getLoginUser().getId());
        //报运单创建人所属部门
        export.setCreateDept(getLoginUser().getDeptId());
        //报运单创建人所属公司id
        export.setCompanyId(getLoginCompanyId());
        //报运单创建人所属公司名称
        export.setCompanyName(getLoginCompanyName());

        //2.判断并执行对应操作
        if (StringUtils.isEmpty(export.getId())) {
            //添加
            exportService.save(export);
        }else {
            //修改
            exportService.update(export);
        }

        //3.进入报运单列表
        return "redirect:/cargo/export/list.do";
    }

    /*
     * 作用：进入修改页面
     * url: /cargo/export/toUpdate.do?id=${o.id}
     * 参数：id： 报运单id
     * 返回值：cargo/export/export-update
     * */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1.根据id查询报运单
        Export export = exportService.findById(id);
        //2.存储到request
        request.setAttribute("export", export);

        //3.查询报运商品
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProductList = exportProductService.findAll(exportProductExample);

        //4，存储到request
        request.setAttribute("eps", exportProductList);

        //3.进入报运单修改页面
        return "cargo/export/export-update";
    }

    /*
     * 作用：提交报运单
     * url: /cargo/export/submit.do?id=bdc4a0de-31f0-4e23-872a-0a3bc5841c1f
     * 参数：id: 报运单id
     * 返回值：进入报运单列表
     * */
    @RequestMapping("/submit")
    public String submit(String id) {
        //1.根据id查询报运单
        Export export = exportService.findById(id);
        //2.修改状态为1
        export.setState(1);
        //3.更新报运单
        exportService.update(export);
        //4.进入报运单列表
        return "redirect:/cargo/export/list.do";
    }

    /*
     * 作用：取消报运单
     * url: /cargo/export/cancel.do?id=bdc4a0de-31f0-4e23-872a-0a3bc5841c1f
     * 参数：id: 报运单id
     * 返回值：进入报运单列表
     * */
    @RequestMapping("/cancel")
    public String cancel(String id) {
        //1.根据id查询报运单
        Export export = exportService.findById(id);
        //2.修改状态为0
        export.setState(0);
        //3.更新报运单
        exportService.update(export);
        //4.进入报运单列表
        return "redirect:/cargo/export/list.do";
    }


    /*
     * 作用：电子报运
     * url: /cargo/export/exportE.do?id=bdc4a0de-31f0-4e23-872a-0a3bc5841c1f
     * 参数：id: 报运单id
     * 返回值：进入报运单列表
     * */
    @RequestMapping("/exportE")
    public String exportE(String id) {
        //1.根据id查询报运单
        Export export = exportService.findById(id);

        //2.创建ExportVo对象，目标是把Export的数据复制到ExportVo对象，因为需要与海关实体类一致
        ExportVo exportVo = new ExportVo();
        //属性拷贝
        BeanUtils.copyProperties(export, exportVo);
        //补充数据
        exportVo.setExportId(id);

        //3.找到报运单对应的商品数据
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> productList = exportProductService.findAll(exportProductExample);

        //4.需要把商品的数据转换为ExportProductVo对象
        for (ExportProduct exportProduct : productList) {
            //每一个报运单商品对应一个ExportProductVo
            ExportProductVo exportProductVo = new ExportProductVo();
            //拷贝属性
            BeanUtils.copyProperties(exportProduct, exportProductVo);
            //补充数据
            exportProductVo.setExportProductId(exportProduct.getId());
            //把exportProductVo对象添加到exportVo对象
            exportVo.getProducts().add(exportProductVo);
        }

        //5.把exportVo对象提供WebService提交给海关
        WebClient.create("http://localhost:9090/ws/export/user").post(exportVo);

        //6.查看报运单审核结果
        ExportResult exportResult = WebClient.create("http://localhost:9090/ws/export/user/" + id).get(ExportResult.class);

        //7.根据海关的审核结果更新报运单的信息
        exportService.updateState(exportResult);

        //8.进入报运单列表
        return "redirect:/cargo/export/list.do";
    }

    /*
     * 作用：下载报运单
     * url: /cargo/export/exportPdf.do?id=bdc4a0de-31f0-4e23-872a-0a3bc5841c1f
     * 参数：id: 报运单id
     * 返回值：下载
     * */
    @RequestMapping("/exportPdf")
    @ResponseBody
    public void exportPdf(String id) throws Exception {

        //通知浏览器以附件的形式下载
        response.setHeader("content-disposition","attachment;filename=export.pdf");

        //1.得到模板的输入流
        InputStream inputStream = session.getServletContext().getResourceAsStream("/jasper/export.jasper");

        //2.把模板和数据填充，拿到JasperPrint对象
        /*
        fillReport(InputStream inputStream, Map<String, Object> parameters, JRDataSource dataSource)
                inputStream: 模板的输入流
                parameters: 需要被填充的参数，不需要被遍历的
                dataSource: 数据源，需要被遍历的数据
         */
        //根据id查找报运单
        Export export = exportService.findById(id);
        //由于export是不需要遍历的数据应该存放在Map中
        Map<String, Object> map = BeanMapUtils.beanToMap(export);   //map的key就是对象的属性名，值：属性值

        //查找该报运单下的所有商品
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> productList = exportProductService.findAll(exportProductExample);
        //补充打印的报运单所需要的factoryName
        for (ExportProduct exportProduct : productList) {
            String factoryId = exportProduct.getFactoryId();
            Factory factory = factoryService.findById(factoryId);
            exportProduct.setFactoryName(factory.getFactoryName());
        }

        //把集合封装到数据源
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productList);

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, dataSource);

        //3.把pdf文件输出
        /*
            exportReportToPdfStream(JasperPrint jasperPrint, OutputStream outputStream)
                    jasperPrint： jasperprint的对象
                    outputStream: 输出的目标地址的输出流对象
         */
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }
}
