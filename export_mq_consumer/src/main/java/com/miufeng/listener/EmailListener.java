package com.miufeng.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miufeng.domain.system.User;
import com.miufeng.utils.MailUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Copyright (C), 2018-2021
 * FileName: EmailListener
 * Author:   Administrator
 * Date:     2021/1/7 0007 20:56
 */
public class EmailListener implements MessageListener {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        try {
            //1.获取消息
            byte[] body = message.getBody();
            //2.转换user
            User user = objectMapper.readValue(body, User.class);
            //3. 发送邮件
            /*
            sendMsg(String to, String subject, String content)
                    to: 接收人
                    subject：主题
                    content:邮件正文
             */
            String subject = "saas-export系统的账号已开通";
            MailUtil.sendMsg(user.getEmail(), subject, "以后您的工作只有007，没有996");
            System.out.println("邮件发送完毕...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
