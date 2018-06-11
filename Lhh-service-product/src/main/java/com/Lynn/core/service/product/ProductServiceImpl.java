package com.Lynn.core.service.product;

import cn.itcast.common.page.Pagination;
import com.Lynn.core.bean.product.*;
import com.Lynn.core.dao.product.ColorDao;
import com.Lynn.core.dao.product.ProductDao;
import com.Lynn.core.dao.product.SkuDao;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by FantasmYii on 2018/3/27.
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private Jedis jedis;

    @Override
    public Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow) {
        ProductQuery productQuery = new ProductQuery();
        productQuery.setPageNo(Pagination.cpn(pageNo));
        //排序
        productQuery.setOrderByClause("id desc");
        ProductQuery.Criteria criteria = productQuery.createCriteria();
        StringBuilder params = new StringBuilder();
        if (null != name) {
            criteria.andNameLike(name);
            params.append("name=").append(name);
        }
        if (null != brandId) {
            criteria.andIdEqualTo(brandId);
            params.append("&brandid=").append(brandId);
        }
        if (null != isShow) {
            criteria.andIsShowEqualTo(isShow);
            params.append("&isShow=").append(isShow);

        } else {
            params.append("$isShow=").append(false);
        }
        Pagination pagination = new Pagination(
                productQuery.getPageNo(),
                productQuery.getPageSize(),
                productDao.countByExample(productQuery),
                productDao.selectByExample(productQuery)
        );

        String url = "/product/list.do";
        pagination.pageView(url, params.toString());
        return pagination;
    }

    @Override
    public List<Color> selectColorList() {
        ColorQuery colorQuery = new ColorQuery();
        colorQuery.createCriteria().andParentIdNotEqualTo(0L);
        return colorDao.selectByExample(colorQuery);
    }
    //保存商品

    @Override
    public void insertProduct(Product product) {
        Long id=jedis.incr("pno");
        product.setId(id);
        //下架状态
        product.setIsNew(false);
        System.out.println("isnew"+product.getIsNew());
        //删除
        product.setIsDel(true);
        System.out.println("isdel"+product.getIsDel());
        productDao.insertSelective(product);
        //返回Id（在dao.xml中写）和保存sku(先取出color和size,双重for循环)
        String[] colors = product.getColors().split(",");
        String[] sizes = product.getSizes().split(",");
        System.out.println("colors+sizes:"+colors.length+sizes.length);
        for (String color : colors) {
            for (String size : sizes) {
                Sku sku = new Sku();
                //商品ＩＤ
                sku.setProductId(product.getId());
                //颜色
                sku.setColorId(Long.parseLong(color));
                //尺码
                sku.setSize(size);
                //市场价
                sku.setMarketPrice(999f);
                //售价
                sku.setPrice(666f);
                //运费
                sku.setDeliveFee(8f);
                //库存
                sku.setStock(0);
                //限制
                sku.setUpperLimit(200);
                //时间
                sku.setCreateTime(new Date());

                skuDao.insertSelective(sku);
            }
        }

    }

 /*   @Autowired
    private SolrServer solrServer;*/
    @Autowired
    private JmsTemplate jmsTemplate;

    //保存商品到solr服务器
    @Override
    public void isShow(Long[] ids) {
        Product product=new Product();
        product.setIsShow(true);
        for(final Long id:ids){
            product.setId(id);
            //updateByPrimaryKeySelective对字段进行判断再更新，为null就忽略，更新某一个字段可以用
            //updateByPrimaryKey对所有都更新，商品信息的变更
            productDao.updateByPrimaryKeySelective(product);
            //发送消息到ActiveMq中
            //如果第一个参数不写的话就代表默认名称，默认名称的配置在mq中
            //jmsTemplate.send("brandId",messageCreator);
            jmsTemplate.send(new MessageCreator() {
                //session用于携带消息
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(String.valueOf(id));
                }
            });
        }
        //TODO 静态
    }


}
