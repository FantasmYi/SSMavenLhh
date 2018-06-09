package com.Lynn.core.service;

import com.Lynn.core.bean.product.Product;
import com.Lynn.core.bean.product.Sku;

import java.util.List;



public interface CmsService {
	
	
	//查询商品
	public Product selectProductById(Long productId);
	
	//查询Sku结果集 (包括颜色）  有货  
	public List<Sku> selectSkuListByProductId(Long productId);

}
