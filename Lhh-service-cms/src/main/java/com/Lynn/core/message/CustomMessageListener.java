package com.Lynn.core.message;

import com.Lynn.core.bean.product.Color;
import com.Lynn.core.bean.product.Product;
import com.Lynn.core.bean.product.Sku;
import com.Lynn.core.service.CmsService;
import com.Lynn.core.service.SearchService;
import com.Lynn.core.service.staticpage.StaticPageService;
import freemarker.template.TemplateException;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.*;

/**
 * 监听处理类 ActiveMQ
 * Created by FantasmYii on 2018/4/11.
 */
public class CustomMessageListener implements MessageListener{

   @Autowired
   private StaticPageService staticPageService;
   @Autowired
   private CmsService cmsService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage am= (ActiveMQTextMessage) message;
        try {
            System.out.println("ActiveMq中的消息是cms："+am.getText());
            String id=am.getText();
            Map<String,Object> root=new HashMap<String,Object>();
            //写root的数据，和productController里东西一样
            Product product = cmsService.selectProductById(Long.valueOf(id));
            //sku
            List<Sku> skus = cmsService.selectSkuListByProductId(Long.valueOf(id));
            Set<Color> colors = new HashSet<Color>();
            //遍历一次，相同颜色的不要，
            for (Sku sku:skus) {
                colors.add(sku.getColor());
            }
            root.put("product", product);
            root.put("skus", skus);
            root.put("colors",colors);

            try {
                //消息队列和静态页相连，消息队列从service层接收数据发送到静态页中
                staticPageService.productstaticpage(root,id);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
