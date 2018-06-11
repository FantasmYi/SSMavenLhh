package com.Lynn.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 *
 * @author lx
 */
public class BuyerCart implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //1：商品结果集 List<BuyerItem>
    private List<BuyerItem> items = new ArrayList<>();

    //添加购物项到购物车中
    public void addItem(BuyerItem item){
        //判断同款
        if(items.contains(item)){
            for (BuyerItem it : items) {
                if(it.equals(item)){
                    it.setAmount(item.getAmount() + it.getAmount());
                    System.out.println("it1:"+it.getAmount());
                }
            }
        }else{
            System.out.println("it2:"+item.getAmount());
            items.add(item);
        }
    }

    public List<BuyerItem> getItems() {
        return items;
    }

    public void setItems(List<BuyerItem> items) {
        this.items = items;
    }

    //2:小计     （商品数据  、商品金额  、运费  、 总计）
    //如果不是纯javabean（声明+get,set），那么序列化转json时会报错，所以要加上下面这个注释
    @JsonIgnore
    public Integer getAmount() {
        Integer result = 0;
        for (BuyerItem buyerItem : items) {
            result += buyerItem.getAmount();
        }
        return result;
    }

    @JsonIgnore
    public Float getMoney() {
        Float money = 0f;
        for (BuyerItem buyerItem : items) {
            money += buyerItem.getAmount() * buyerItem.getSku().getPrice();
        }
        return money;
    }

    @JsonIgnore
    public Float getFee() {
        Float result = 0f;
        if (getMoney() < 79) {
            result = 5f;
        }
        return result;
    }
    @JsonIgnore
    public Float getTotalPrice(){
          return getFee()+getMoney();
    }
}
