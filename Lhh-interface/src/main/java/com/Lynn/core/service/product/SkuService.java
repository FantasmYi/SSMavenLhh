package com.Lynn.core.service.product;

import com.Lynn.core.bean.BuyerCart;
import com.Lynn.core.bean.product.Sku;

import java.util.List;

/**
 * Created by FantasmYii on 2018/3/31.
 */
public interface SkuService {
    public List<Sku> selectSkuListByProductId(Long productId);
    public void updateSkuById(Sku skuId);
    public Sku selectSkuById(Long id);
    public void insertBuyerCartToRedis(BuyerCart buyerCart,String username);
      //从redis中搜索购物车
    public BuyerCart selectBuyerCartFromRedis(String username);
}
