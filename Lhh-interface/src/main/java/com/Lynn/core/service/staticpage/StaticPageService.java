package com.Lynn.core.service.staticpage;

import freemarker.template.TemplateException;

import java.util.Map;

/**
 * Created by FantasmYii on 2018/4/13.
 */
public interface StaticPageService {
    public void productstaticpage(Map<String, Object> map, String id) throws TemplateException;
}
