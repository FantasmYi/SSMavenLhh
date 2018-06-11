package com.Lynn.core.service;

import cn.itcast.common.page.Pagination;
import com.Lynn.core.bean.product.Product;
import com.Lynn.core.bean.product.ProductQuery;
import com.Lynn.core.bean.product.Sku;
import com.Lynn.core.bean.product.SkuQuery;
import com.Lynn.core.dao.product.ProductDao;
import com.Lynn.core.dao.product.SkuDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全文检索，用solr
 * 此类用于操作solr服务器，从服务器中查询商品和增加商品
 * Created by FantasmYii on 2018/4/3.
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrServer solrServer;

    //从solr服务器中查询商品
    @Override
    public Pagination selectPaginationByQuery(Integer pageNo, String keyword, Long brandId, String price) throws Exception {
        List<Product> products = new ArrayList<Product>();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q", "name_ik:" + keyword);
        //创建包装类，目的使用分页
        ProductQuery productQuery = new ProductQuery();
        productQuery.setPageNo(Pagination.cpn(pageNo));
        productQuery.setPageSize(12);
        //接收分页参数
        solrQuery.set("q", "name_ik:" + keyword);
        StringBuilder params = new StringBuilder();
        params.append("keyword=").append(keyword);
        //过滤条件
        if (null != brandId) {
            solrQuery.addFacetQuery("brandId:" + brandId);
        }
        if (null != price) {
            String[] p = price.split("-");
            if (p.length == 2) {
                solrQuery.addFacetQuery("price:[" + p[0] + " TO " + p[1] + "]");
                System.out.println("p[0]:"+p[0]+"p[1]:"+p[1]);
                System.out.println("price:[" + p[0] + " To " + p[1] + "]");
            }else {
                solrQuery.addFacetQuery("price:["+p[0]+" TO *]");
            }
        }
        //设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("name_ik");
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");
        //排序
        solrQuery.addSort("price", SolrQuery.ORDER.asc);
        //分页
        solrQuery.setStart(productQuery.getStartRow());
        solrQuery.setRows(productQuery.getPageSize());
        //执行查询
        QueryResponse response = solrServer.query(solrQuery);
        //取高亮
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        //结果集
        SolrDocumentList docs = response.getResults();
        //发现的条数
        long numFound = docs.getNumFound();
        for (SolrDocument doc : docs) {
            Product product = new Product();
            String id = (String) doc.get("id");
            product.setId(Long.parseLong(id));
            //名称，图片
            /*String name= (String) doc.get("name_ik");
            product.setName(name);*/
            Map<String, List<String>> map = highlighting.get(id);
            List<String> list = map.get("name_ik");
            product.setName(list.get(0));


            String url = (String) doc.get("url");
            product.setImgUrl(url);//img,img2,img3
            //价格 售价   select price from bbs_sku where product_id =442 order by price asc limit 0,1
            product.setPrice((Float) doc.get("price"));
            //品牌ID Long
            product.setBrandId(Long.parseLong(String.valueOf((Integer) doc.get("brandId"))));

            products.add(product);
        }
        //构建分页对象
        Pagination pagination = new Pagination(
                productQuery.getPageNo(),
                productQuery.getPageSize(),
                (int) numFound,
                products
        );
        System.out.println("products:" + products);
        //页面展示
        String url = "/search";
        pagination.pageView(url, params.toString());
        return pagination;
    }
    @Autowired
    private ProductDao productDao;
    @Autowired
    private SkuDao skuDao;

    //保存商品信息到solr服务器
    public void insertProductToSolr(Long id){
        SolrInputDocument solrInputFields=new SolrInputDocument();
        solrInputFields.setField("id",id);
        Product p=productDao.selectByPrimaryKey(id);
        solrInputFields.setField("name_ik",p.getName());
        //图片,注意这可能有问题,原版为p.getImages()[0]
        solrInputFields.setField("url",p.getImgUrl());
        //价格 售价 select price from bbs_sku where product_id=454 order by price desc limit 1
        SkuQuery skuQuery=new SkuQuery();
        //创建一个skuquery对象，这个对象是id=？的对象
        skuQuery.createCriteria().andProductIdEqualTo(id);
        skuQuery.setOrderByClause("price desc");
        //下面两句是拼接Limit0,1的(1-1)*1
        skuQuery.setPageNo(1);
        skuQuery.setPageSize(1);
        skuQuery.setFields("price");
        //要把这个sku出来
        List<Sku> skus= skuDao.selectByExample(skuQuery);
        //这个集合中只有一个元素
        solrInputFields.setField("price",skus.get(0).getPrice());
        //品牌ID  这个字段solr中没有，所以要添加
        solrInputFields.setField("brandId",p.getBrandId());
        try {
            solrServer.add(solrInputFields);
            solrServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
