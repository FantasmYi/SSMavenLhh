package com.Lynn.core.service.product;

import cn.itcast.common.page.Pagination;
import com.Lynn.core.bean.product.Brand;
import com.Lynn.core.bean.product.BrandQuery;
import com.Lynn.core.bean.product.ProductQuery;
import com.Lynn.core.dao.product.BrandDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by FantasmYii on 2018/3/21.
 */
@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;


    @Override
    public Pagination selectPaginationByQuery(String name, Integer isDisplay, Integer pageNo) {
        BrandQuery brandQuery=new BrandQuery();
        brandQuery.setPageNo(Pagination.cpn(pageNo));
        StringBuilder pagem=new StringBuilder();
        //每页数
        brandQuery.setPageSize(3);
        if(name!=null){
            brandQuery.setName(name);
            pagem.append("name=").append(name);
        }
        if (isDisplay!=null){
            brandQuery.setIsDisplay(isDisplay);
            pagem.append("&isDisplay=").append(isDisplay);
        }else {
            brandQuery.setIsDisplay(1);
            pagem.append("&isDisplay=").append(1);
        }
        Pagination pagination=new Pagination(
            brandQuery.getPageNo(),
                brandQuery.getPageSize(),
                brandDao.selectCount(brandQuery)
        );
        //设置结果集
        pagination.setList(brandDao.selectBrandListByQuery(brandQuery));
        String url="/brand/list.do";
        pagination.pageView(url,pagem.toString());
        return pagination;
    }

    @Override
    public Brand selectBrandById(Long id) {
        Brand brand=brandDao.selectBrandById(id);
        return brand;
    }

    @Autowired
    private Jedis jedis;

    //将商品保存到数据库和redis中,注意这两条语句的先后顺序，如果第一句发生问题，则数据库中也不会放入值
    @Override
    public void updateBrandById(Brand brand) {
        jedis.hset("brand", String.valueOf(brand.getId()),brand.getName());
        brandDao.updateBrandById(brand);
    }

    @Override
    public void deletes(Long[] ids) {
        brandDao.deletes(ids);
    }

    @Override
    public List<Brand> selectBrandListByQuery(Integer isDisplay) {
        BrandQuery brandQuery=new BrandQuery();
        brandQuery.setIsDisplay(isDisplay);
        return brandDao.selectBrandListByQuery(brandQuery);
    }

    //查询redis中的商品
    @Override
    public List<Brand> selectBrandListByRedis(){
        List<Brand> brands=new ArrayList<Brand>();
        Map<String,String> hgetAll=jedis.hgetAll("brand");
        Set<Map.Entry<String,String>> entrySet=hgetAll.entrySet();
        for (Map.Entry<String,String> entry:entrySet){
            Brand brand1=new Brand();
            brand1.setId(Long.valueOf(entry.getKey()));
            brand1.setName(entry.getValue());
            brands.add(brand1);
        }
        return brands;
    }
}
