package com.Lynn.core.service;

import com.Lynn.core.bean.product.Product;
import com.Lynn.core.bean.product.Sku;
import com.Lynn.core.bean.product.SkuQuery;
import com.Lynn.core.dao.product.ColorDao;
import com.Lynn.core.dao.product.ProductDao;
import com.Lynn.core.dao.product.SkuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by FantasmYii on 2018/4/11.
 */
@Service("cmsService")
@Transactional
public class CmsServiceImpl implements CmsService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;

    @Override
    public Product selectProductById(Long productId) {
        return productDao.selectByPrimaryKey(productId);
    }

    @Override
    public List<Sku> selectSkuListByProductId(Long productId) {
        SkuQuery skuQuery=new SkuQuery();
        //最后一个方法是找出sku表中库存量大于0的
        skuQuery.createCriteria().andProductIdEqualTo(productId).andStockGreaterThan(0);
        List<Sku> skus=skuDao.selectByExample(skuQuery);
        for(Sku sku:skus){
            sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        }
        return skus;
    }
}
