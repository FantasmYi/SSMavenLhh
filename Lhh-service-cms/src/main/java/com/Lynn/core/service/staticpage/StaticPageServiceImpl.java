package com.Lynn.core.service.staticpage;

import com.Lynn.core.bean.product.Product;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * 数据模型+模板=输出
 * Configuretion+template=writer
 * Created by FantasmYii on 2018/4/12.
 */
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {

    //声明
    //注入
    private Configuration configuration;

    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.configuration = freeMarkerConfigurer.getConfiguration();
    }

    //静态化 商品
    @Override
    public void productstaticpage(Map<String, Object> map, String id) throws TemplateException {
        //获取输出路径
        String url = "/html/product/" + id + ".html";
        //因为是相对路径，而wirter需要绝对路径,所以要转换
        String path=getPath(url);
        System.out.println("getPath"+path);
        File file=new File(path);
        File parent=file.getParentFile();
        //如果父目录为空
        if (!parent.exists()){
            //创建目录
            parent.mkdirs();
        }
        Writer out = null;
        try {
            Template template = configuration.getTemplate("product.html");
            //输出,读写都要注意乱码，写在这，上面读的过程防乱码在配置文件
            out=new OutputStreamWriter(new FileOutputStream(file),"utf-8");
            //处理
            template.process(map,out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //获取绝对路径
    public String getPath(String name){
        return servletContext.getRealPath(name);
    }
    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
    }
    //静态化订单
}
