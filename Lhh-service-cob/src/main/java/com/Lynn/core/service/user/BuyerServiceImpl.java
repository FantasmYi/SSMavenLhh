package com.Lynn.core.service.user;

import com.Lynn.core.bean.BuyerCart;
import com.Lynn.core.bean.BuyerItem;
import com.Lynn.core.bean.order.Detail;
import com.Lynn.core.bean.order.Order;
import com.Lynn.core.bean.product.Sku;
import com.Lynn.core.bean.user.Buyer;
import com.Lynn.core.bean.user.BuyerQuery;
import com.Lynn.core.dao.order.DetailDao;
import com.Lynn.core.dao.order.OrderDao;
import com.Lynn.core.dao.product.ColorDao;
import com.Lynn.core.dao.product.ProductDao;
import com.Lynn.core.dao.product.SkuDao;
import com.Lynn.core.dao.user.BuyerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * 通过用户名查询用户对象
 * 添加订单(order)
 * Created by FantasmYii on 2018/4/16.
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private BuyerDao buyerDao;

    @Override
    public Buyer selectBuyerByUsername(String username) {
        BuyerQuery buyerQuery = new BuyerQuery();
        buyerQuery.createCriteria().andUsernameEqualTo(username);
        List<Buyer> buyers = buyerDao.selectByExample(buyerQuery);
        if (buyers != null && buyers.size() > 0) {
            return buyers.get(0);
        }
        return null;
    }

    @Autowired
    private Jedis jedis;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private DetailDao detailDao;

    @Override
    public void insertOrder(Order order, String username) {
        //redis生成唯一的订单编号
        Long id = jedis.incr("oid");
        order.setId(id);
        //订单中有运费，总价和订单金额,所以需要获取购物车
        BuyerCart buyerCart = selectBuyerCartFromRedis(username);
        List<BuyerItem> buyerItems = buyerCart.getItems();
        for (BuyerItem buyerItem : buyerItems) {
            buyerItem.setSku(selectSkuById(buyerItem.getSku().getId()));
        }
        //运费
        order.setDeliverFee(buyerCart.getFee());
        //总价
        order.setTotalPrice(buyerCart.getTotalPrice());
        //订单金额,这里有点问题
        order.setOrderPrice(buyerCart.getMoney());
        //一下是支付状态
        if (order.getPaymentWay() == 1) {
            //0是到付
            order.setIsPaiy(0);
        } else {
            //1是待付款
            order.setIsPaiy(1);
        }
        //订单状态,0是提交订单
        order.setOrderState(0);
        //时间
        order.setCreateDate(new Date());
        //用户id
        System.out.println("buyerService,username:"+username);
        String uid = jedis.get(username);
        order.setBuyerId(Long.parseLong(uid));
        //保存订单
        orderDao.insertSelective(order);
        //保存订单详情
        for (BuyerItem buyerItem : buyerItems) {
            Detail detail = new Detail();
            //		ID
            //		订单ID
            detail.setOrderId(id);
            //		商品编号
            detail.setProductId(buyerItem.getSku().getProductId());
            //		商品名称
            detail.setProductName(buyerItem.getSku().getProduct().getName());
            //		颜色
            detail.setColor(buyerItem.getSku().getColor().getName());
            //		尺码
            detail.setSize(buyerItem.getSku().getSize());
            //		价格
            detail.setPrice(buyerItem.getSku().getPrice());
            //		数量
            detail.setAmount(buyerItem.getAmount());
            //		购物车提供
            detailDao.insertSelective(detail);
        }
        //清空购物车
        jedis.del("buyerCart:fbb2016");


    }

    public BuyerCart selectBuyerCartFromRedis(String username) {
        BuyerCart buyerCart = new BuyerCart();
        Map<String, String> hgetAll = jedis.hgetAll("buyerCart:" + username);
        if (null != hgetAll) {
            Set<Map.Entry<String, String>> entrySet = hgetAll.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
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

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private ProductDao productDao;

    //通过skuid查询sku对象
    public Sku selectSkuById(Long id) {
        //上面3个的对象
        Sku sku = skuDao.selectByPrimaryKey(id);
        sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
        return sku;
    }

}
