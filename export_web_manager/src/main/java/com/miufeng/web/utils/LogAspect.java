package com.miufeng.web.utils;

import com.miufeng.domain.system.SysLog;
import com.miufeng.domain.system.User;
import com.miufeng.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * Copyright (C), 2018-2020
 * FileName: LogAspect
 * Author:   Administrator
 * Date:     2020/12/25 0025 17:43
 */
//日志切面类
@Component
@Aspect //该类是一个切面类
public class LogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SysLogService sysLogService;

    /*
    * 环绕通知的要求：
    *   1.方法的返回值类型一定是Object，这里的返回值代表了目标方法的返回值
    *   2.方法一定要有形参ProceedingJoinPoint, proceedingJoinPoint代表的是目标方法
    * */
    //切入点表达式可以使用逻辑运算符(不包括SysLogController执行的操作)
    @Around("execution(* com.miufeng.web.controller.*.*.*(..)) && !execution(* com.miufeng.web.controller.*.SysLogController.*(..))")
    public Object saveLog(ProceedingJoinPoint pj) {

        try {
            Object result = pj.proceed(); //放行目标方法

            SysLog sysLog = new SysLog();

            //设置日志id
            sysLog.setId(UUID.randomUUID().toString());
            //设置当前用户的用户名
            User loginUser = (User) request.getSession().getAttribute("loginUser");
            sysLog.setUserName(loginUser.getUserName());
            //设置ip地址
            sysLog.setIp(request.getRemoteAddr());
            //设置执行时间
            sysLog.setTime(new Date());
            //设置执行的方法的名称
            sysLog.setMethod(pj.getSignature().getName()); //pj.getSignature()得到方法的签名
            //设置执行的方法所属的controller的类全名
            sysLog.setAction(pj.getTarget().getClass().getName());  //pj.getTarget()当前调用的类对象
            //设置公司id
            sysLog.setCompanyId(loginUser.getCompanyId());
            //设置公司名称
            sysLog.setCompanyName(loginUser.getCompanyName());

            //调用业务层执行保存日志的方法
            sysLogService.save(sysLog);

            //目标方法的执行结果一定要返回
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
