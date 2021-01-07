package com.miufeng.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.miufeng.service.cargo.ContractProductService;
import com.miufeng.vo.ContractProductVo;
import com.miufeng.web.controller.BaseController;
import com.miufeng.web.utils.DownloadUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2018-2021
 * FileName: OutProductController
 * Author:   Administrator
 * Date:     2021/1/2 0002 21:01
 */
@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;

    /*
     * 作用：进入出货表页面
     * url：/cargo/contract/print.do
     * 参数：无
     * 返回值：cargo/print/contract-print
     * */
    @RequestMapping("/print")
    public String print() {
        return "cargo/print/contract-print";
    }

    /*
     * 作用：导出出货表excel
     * url：/cargo/contract/printExcel.do?inputDate=2015-01
     * 参数：inputDate：出货年月
     * 返回值：下载
     * */
    @RequestMapping("/printExcel")
    @ResponseBody   //下载也需要用@ResponseBody
    public void printExcel(String inputDate) throws IOException {
        /*//1.创建工作簿

        Workbook workbook = new XSSFWorkbook();

        //2.创建工作单
        Sheet sheet = workbook.createSheet("出货表");

        //3.合并单元格

        CellRangeAddressint firstRow, int lastRow, int firstCol, int lastCol)
            firstRow: 开始行
            lastRow:  结束行
            firstCol: 开始列
            lastCol: 结束列

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));

        //4.设置列宽
        sheet.setColumnWidth(1,21*256);
        sheet.setColumnWidth(2,16*256);
        sheet.setColumnWidth(3,29*256);
        sheet.setColumnWidth(4,11*256);
        sheet.setColumnWidth(5,11*256);
        sheet.setColumnWidth(6,11*256);
        sheet.setColumnWidth(7,11*256);
        sheet.setColumnWidth(8,11*256);

        //5.创建第一行，并且设置标题内容
        Row row = sheet.createRow(0);
        //2015-01   2015-12
        String title = inputDate.replaceAll("-0", "年").replaceAll("-", "年") + "月份出货表";
        //创建单元格(上面合并后的单元格位置)
        Cell cell = row.createCell(1);
        //设置标题样式
        cell.setCellStyle(bigTitle(workbook));
        //设置单元格内容
        cell.setCellValue(title);

        //6.创建第二行，设置列头的内容
        row = sheet.createRow(1);
        String[] titles = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        for (int i = 0; i < titles.length; i++) {
            //创建单元格
            cell = row.createCell(i + 1);
            //设置标题样式
            cell.setCellStyle(title(workbook));
            //设置单元格内容
            cell.setCellValue(titles[i]);
        }*/

        //1.读取模板的输入流，读取webapp目录下的资源记得使用ServletContext
        InputStream inputStream = session.getServletContext().getResourceAsStream("/make/xlsprint/tOUTPRODUCT.xlsx");
        //2.使用模板的输入流创建一个工作簿
        Workbook workbook = new XSSFWorkbook(inputStream);
        //3.得到工作单
        Sheet sheet = workbook.getSheetAt(0);

        //4.设置标题内容
        String title = inputDate.replaceAll("-0", "年").replaceAll("-", "年") + "月份出货表";
        //获取第一行
        Row row = sheet.getRow(0);
        //获取一个单元格
        Cell cell = row.getCell(1);
        //设置单元格内容
        cell.setCellValue(title);

        //5.提取第三行的样式出来存储到数组中，提供给后面的数据去用
        //定义一个数组存储数据的样式
        CellStyle[] cellStyles = new CellStyle[8];
        //获取第三行
        row = sheet.getRow(2);
        //遍历存储数据样式
        for (int i = 0; i < cellStyles.length; i++) {
            cell = row.getCell(i + 1);
            cellStyles[i] = cell.getCellStyle();
        }


        //7.查询出货表的数据，然后遍历设置到excel中
        List<ContractProductVo> contractProductVoList = contractProductService.findByShipTime(inputDate, getLoginCompanyId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //定义一个变量记录当前行数，数据部分是索引为2开始
        int index = 2;
        for (ContractProductVo contractProductVo : contractProductVoList) {
            //创建行
            row = sheet.createRow(index++);

            //客户
            String customName = contractProductVo.getCustomName();
            if (customName != null) {
                //创建单元格
                cell = row.createCell(1);
                //设置样式
                cell.setCellStyle(cellStyles[0]);
                //设置内容
                cell.setCellValue(customName);
            }

            //订单号
            String contractNo = contractProductVo.getContractNo();
            if(contractNo!=null){
                cell = row.createCell(2);
                //设置样式
                cell.setCellStyle(cellStyles[1]);
                //设置内容
                cell.setCellValue(contractNo);
            }

            //货号
            String productNo = contractProductVo.getProductNo();
            if(productNo!=null){
                cell = row.createCell(3);
                //设置样式
                cell.setCellStyle(cellStyles[2]);
                //设置内容
                cell.setCellValue(productNo);
            }


            //数量
            Integer cnumber = contractProductVo.getCnumber();
            if(cnumber!=null){
                cell = row.createCell(4);
                //设置样式
                cell.setCellStyle(cellStyles[3]);
                //设置内容
                cell.setCellValue(cnumber);
            }


            //工厂名字
            String factoryName = contractProductVo.getFactoryName();
            if(factoryName!=null){
                cell = row.createCell(5);
                //设置样式
                cell.setCellStyle(cellStyles[4]);
                //设置内容
                cell.setCellValue(factoryName);
            }

            //工厂交货日期
            Date deliveryPeriod = contractProductVo.getDeliveryPeriod();
            if(deliveryPeriod!=null){
                cell = row.createCell(6);
                //设置样式
                cell.setCellStyle(cellStyles[5]);
                //设置内容
                cell.setCellValue(dateFormat.format(deliveryPeriod));
            }

            //船期
            Date shipTime = contractProductVo.getShipTime();
            if(shipTime!=null){
                cell = row.createCell(7);
                //设置样式
                cell.setCellStyle(cellStyles[6]);
                //设置内容
                cell.setCellValue(dateFormat.format(shipTime));
            }

            //贸易条款
            String tradeTerms = contractProductVo.getTradeTerms();
            if(tradeTerms!=null){
                cell = row.createCell(8);
                //设置样式
                cell.setCellStyle(cellStyles[7]);
                //设置内容
                cell.setCellValue(tradeTerms);
            }
        }


        //8.把工作簿写出
        //设置响应头通知浏览器是以下载的形式处理内容
        //解决中文文件名显示异常
       /* String flieName = "出货表（" + inputDate + "）.xlsx";
        flieName = URLEncoder.encode(flieName, "utf-8");
        response.setHeader("content-disposition", "attachment;filename=" + flieName); //响应头没有经过url编码，才为出现中文显示异常

        workbook.write(response.getOutputStream());*/

       //使用工具类处理下载中文名显示异常
        /**
         * @param byteArrayOutputStream 将文件内容写入ByteArrayOutputStream
         * @param response HttpServletResponse	写入response
         * @param returnName 返回的文件名
         */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //这个类内部维护一个字节数组
        //把workbook的内容写出到字节数组汇总
        workbook.write(byteArrayOutputStream);
        new DownloadUtil().download(byteArrayOutputStream, response, "出货表（" + inputDate + "）.xlsx");
    }

    /*//大标题的样式
    public CellStyle bigTitle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);				//横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线

        return style;
    }*/
}
