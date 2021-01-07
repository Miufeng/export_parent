package com.miufeng.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
自定义类型转换器步骤：
    1. 实现Converter接口，并且实现接口的方法
    2. 创建自定义类型转换器的对象
    3. 把类型转换器交给类型转换器的工厂
    4. 把类型转换器的工厂交给注解驱动去启动
 */
@Component
public class StringToDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        try {
            if (!StringUtils.isEmpty(source)) {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(source);
                return date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }
}
