package com.Lynn.core.controller;

import com.Lynn.common.utils.RequestUtils;
import com.Lynn.common.web.Constants;
import com.Lynn.core.bean.BuyerCart;
import com.Lynn.core.bean.BuyerItem;
import com.Lynn.core.bean.order.Order;
import com.Lynn.core.bean.product.Sku;
import com.Lynn.core.service.product.SkuService;
import com.Lynn.core.service.user.BuyerService;
import com.Lynn.core.service.user.SessionProvider;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * 去购物车方法和向购物车中添加商品
 * Created by FantasmYii on 2018/4/20.
 */
@Controller
public class CartController {
    @Autowired
    private SkuService skuService;
    @Autowired
    private SessionProvider sessionProvider;

    //加入购物车
    @RequestMapping(value = "/addCart")
    public String addCart(Long skuId, Integer amount, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper om = new ObjectMapper();
        //空不转
        om.setSerializationInclusion(Include.NON_NULL);
        //cookie中存放字符串，所以对象和字符串要通过json互转
        BuyerCart buyerCart = null;
        //1.从request中取cookies
        Cookie[] cookies = request.getCookies();
        //判断cookies中有没有购物车
        if (cookies != null && cookies.length > 0) {
            //遍历cookies,取出之前的购物车
            for (Cookie cookie : cookies) {
                System.out.println("cookie.getValue1():" + cookie.getValue().toString());
                //判断cookies中有没有购物车，如果是第一次加入购物车的话这个if是false
                if (Constants.BUYER_CART.equals(cookie.getName())) {
                    System.out.println("cooie.getName:" + cookie.getName() + "Constants.BUYER_CART" + Constants.BUYER_CART);
                    System.out.println("cookie.getValue2():" + cookie.getValue().toString());
                    //value是字符串，要将他转换成对象
                    buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
                    break;
                }
            }
        }
        if (null == buyerCart) {
            buyerCart = new BuyerCart();
        }
        //追加当前商品到购物车
        Sku sku = new Sku();
        sku.setId(skuId);
        BuyerItem buyerItem = new BuyerItem();
        buyerItem.setAmount(amount);
        buyerItem.setSku(sku);
        System.out.println("buyerItem:" + buyerItem.toString());
        //追加商品到购物车
        buyerCart.addItem(buyerItem);
        //判断是否登录,如果登录的话，就将购物车存放到redis中
        String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(response, request));
        if (username != null) {
            //有，将购车存到reids中
            skuService.insertBuyerCartToRedis(buyerCart, username);
            //将cookie重置
            Cookie cookie = new Cookie(Constants.BUYER_CART, null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            //创建cookie，把新购物车放回去,要将对象转换成json,必须有下面2句代码
            StringWriter stringWriter = new StringWriter();
            //下面的第二个参数卡了好久
            om.writeValue(stringWriter, buyerCart);
            Cookie cookie = new Cookie(Constants.BUYER_CART, stringWriter.toString());
            //设置时间
            cookie.setMaxAge(60 * 60 * 24);
            //该域名下的所有contextPath都可以访问该Cookie
            cookie.setPath("/");
            //保存回浏览器
            response.addCookie(cookie);
        }
        return "redirect:/toCart";
    }


    //去购物车
    @RequestMapping(value = "/toCart")
    public String toCart(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //从request中取cookie,遍历cookie,取出之前的购物车
        ObjectMapper om = new ObjectMapper();
        //空不转
        om.setSerializationInclusion(Include.NON_NULL);
        //cookie中存放字符串，所以对象和字符串要通过json互转
        BuyerCart buyerCart = new BuyerCart();
        //1.从request中取cookies
        Cookie[] cookies = request.getCookies();
        //判断cookies中有没有购物车
        if (cookies != null && cookies.length > 0) {
            //遍历cookies,取出之前的购物车
            for (Cookie cookie : cookies) {
                //判断cookies中有没有购物车
                if (Constants.BUYER_CART.equals(cookie.getName())) {
                    //value是字符串，要将他转换成对象
                    buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
                    break;
                }
            }
        }
        //用户是否登陆
        String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(response, request));
        if (null != username) {
//		登陆
//		2：再把购物车保存到Redis中，清理Cookie
            if (null != buyerCart) {
                skuService.insertBuyerCartToRedis(buyerCart, username);
                //清理之前Cookie4
                Cookie cookie = new Cookie(Constants.BUYER_CART, null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
//		3：从Redis中取出所有的购物车  Service层

            buyerCart = skuService.selectBuyerCartFromRedis(username);
        }
        //如果取出的购物车后果中有东西 购物车中的数量及sku
        if (buyerCart != null) {
            List<BuyerItem> buyerItems = buyerCart.getItems();
            for (BuyerItem buyerItem : buyerItems) {
                //把东西追加到购物车中
                buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
            }
        }

        //没有
        //回显购物车内容
        model.addAttribute("buyerCart", buyerCart);
        return "cart";
    }

    //与springmvc关联，结算之前判断是否登录
    @RequestMapping(value = "/buyer/trueBuy")
    public String trueString(Long[] SKUIds, Model model, HttpServletResponse response, HttpServletRequest request) {
        //购物车中必须有商品
        String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(response, request));
        BuyerCart buyerCart = skuService.selectBuyerCartFromRedis(username);
        List<BuyerItem> buyerItems = buyerCart.getItems();
        //标记
        Boolean flag = false;
        if(buyerItems.size()>0) {
            //把购物车装满后判断库存是否够
            for (BuyerItem buyerItem : buyerItems) {
                buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
                if (buyerItem.getAmount() > buyerItem.getSku().getStock()) {
                    //无货
                    flag = true;
                }
            }
            //至少有一个无货则不能进入下一个订单页面
            if (flag) {
                model.addAttribute("buyerCart", buyerCart);
                return "cart";
            }
        }else {
            return "redirect:/toCart";
        }
        //都有货进入下一个视图
        return "order";
    }
    @Autowired
    private BuyerService buyerService;
    @RequestMapping(value = "/buyer/submitOrder")
    public String submitOrder(Order order, Model model, HttpServletRequest request, HttpServletResponse response){
        String username=sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(response, request));
        buyerService.insertOrder(order,username);
        return "success";
    }

}
