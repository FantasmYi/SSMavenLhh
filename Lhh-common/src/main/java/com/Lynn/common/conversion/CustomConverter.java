package com.Lynn.common.conversion;

import org.springframework.core.convert.converter.Converter;

/**
 * 自定义转换器类
 * 去掉空格
 * Converter<S,T> S:页面上的类型 T:转换后的类型
 * 如果source不为空，把空格变为空串，看是否与全是空串
 * Created by FantasmYii on 2018/4/16.
 */
public class CustomConverter implements Converter<String, String> {


    @Override
    public String convert(String source) {
        try {
            if (null != source) {
                //去掉空格方法
                source = source.trim();
                if (!"".equals(source)) {
                    return source;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
