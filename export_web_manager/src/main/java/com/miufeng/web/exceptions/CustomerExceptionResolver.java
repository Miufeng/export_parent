package com.miufeng.web.exceptions;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
自定义异常的处理器
    1. 自定义一个类实现HandlerExceptionResolver接口，
    2. 实现该接口的方法
    3. 创建异常处理器的对象
 */
@Component
public class CustomerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //1.创建ModelAndView对象
        ModelAndView mv = new ModelAndView();

        //2.设置异常信息保存到request域中
        mv.addObject("errorMsg", e.getMessage());

        //3.将异常信息打印到控制台
        e.printStackTrace();

        //4.设置跳转视图名称
        mv.setViewName("error");

        //5.返回
        return mv;
    }
}
