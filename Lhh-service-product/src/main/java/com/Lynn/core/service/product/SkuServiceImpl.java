package com.Lynn.core.service.product;


import com.Lynn.core.bean.BuyerCart;
import com.Lynn.core.bean.BuyerItem;
import com.Lynn.core.bean.product.*;
import com.Lynn.core.dao.product.ColorDao;
import com.Lynn.core.dao.product.ProductDao;
import com.Lynn.core.dao.product.SkuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

/**
 * Created by FantasmYii on 2018/3/31.
 */
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;

    //通过商品id查询库存，返回sku
    //通过商品id查询colors,通过colors查询库存
    public List<Sku> selectSkuListByProductId(Long productId) {
        SkuQuery skuQuery = new SkuQuery();
        //skuQuery这个对象中存放商品id的条件
        skuQuery.createCriteria().andProductIdEqualTo(productId);
        List<Sku> skus = skuDao.selectByExample(skuQuery);
        for (Sku sku : skus) {
            sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        }
        return skus;
    }

    @Override
    public void updateSkuById(Sku skuId) {
        skuDao.updateByPrimaryKeySelective(skuId);
    }

    @Autowired
    private ProductDao productDao;

    @Override
    public Sku selectSkuById(Long id) {
        //sku对象
        Sku sku = skuDao.selectByPrimaryKey(id);
        //商品对象
        sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
        //颜色对象
        sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        return sku;
    }

    @Autowired
    private Jedis jedis;

    @Override
    //保存购物车到redis中，一个用户名一个购物车
    public void insertBuyerCartToRedis(BuyerCart buyerCart, String username) {
        List<BuyerItem> buyerItems = buyerCart.getItems();
        if (buyerItems.size() > 0) {
            for (BuyerItem buyerItem : buyerItems) {
                //判断购物车中是否有同款商品，有则合并
                if (jedis.hexists("buyerCart:" + username, String.valueOf(buyerItem.getSku().getId()))) {
                    //加数量
                    jedis.hincrBy("buyerCart:" + username, String.valueOf(buyerItem.getSku().getId()),
                            buyerItem.getAmount());
                }else {
                    jedis.hset("buyerCart:" + username,
                            String.valueOf(buyerItem.getSku().getId()),
                            String.valueOf(buyerItem.getAmount()));
                }
            }
        }
    }

    //从redis中取出购物车,不但要取出，还要将新商品追加到购物车中
    //取出购物车从Redis
    public BuyerCart selectBuyerCartFromRedis(String username) {
        BuyerCart buyerCart = new BuyerCart();
        Map<String, String> hgetAll = jedis.hgetAll("buyerCart:" + username);
        if (null != hgetAll) {
            Set<Entry<String, String>> entrySet = hgetAll.entrySet();
            for (Entry<String, String> entry : entrySet) {
//				5：追加当前商品到购物车
                Sku sku = new Sku();
                //ID
                if (entry.getKey() != null) {
                    sku.setId(Long.parseLong(entry.getKey()));
                }
                BuyerItem buyerItem = new BuyerItem();
                buyerItem.setSku(sku);
                //Amount
                buyerItem.setAmount(Integer.parseInt(entry.getValue()));
                //追加商品到购物车
                buyerCart.addItem(buyerItem);

            }
        }
        return buyerCart;
    }


}
