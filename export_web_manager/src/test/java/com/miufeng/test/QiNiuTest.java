package com.miufeng.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: QiNiuTest
 * Author:   Administrator
 * Date:     2020/12/31 0031 18:13
 */
public class QiNiuTest {
    public static void main(String[] args) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "I_5fJaG09VBNDBtN0eHGrN_wN8mRftp78331vpLV";
        String secretKey = "olgbnEuXFmZio0X6q0HGlK-p4QApaV-Uz2L7c4Yh";
        String bucket = "miufeng";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "H:/图片/戴眼镜.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = UUID.randomUUID().toString();

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            //文件的访问路径： http://域名/文件名
            System.out.println("访问的路径：http://qm77q7ryv.hn-bkt.clouddn.com/"+key);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }
}
