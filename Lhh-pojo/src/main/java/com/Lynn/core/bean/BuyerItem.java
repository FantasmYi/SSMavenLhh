package com.Lynn.core.bean;

import com.Lynn.core.bean.product.Sku;

import java.io.Serializable;

/**
 * 购物车里的每一项
 * Created by FantasmYii on 2018/4/19.
 */
public class BuyerItem implements Serializable {
    private static final long serialVersionUID = 1L;
    //skuid sku对象中有自己的id
    private Sku sku;
    //数量
    private Integer amount = 1;

    @Override
    public String toString() {
        return "BuyerItem{" +
                "sku=" + sku +
                ", amount=" + amount +
                ", isHave=" + isHave +
                '}';
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean getHave() {
        return isHave;
    }

    public void setHave(Boolean have) {
        isHave = have;
    }

    //是否有货 需要看前台的页面传过来的值和库存对比，小于则库存为无
    private Boolean isHave = true;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)  // 比较地址
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass()) //class  cn.itcast.core.bean.BuyerItem
            return false;
        BuyerItem other = (BuyerItem) obj;
        if (sku == null) {
            if (other.sku != null)
                return false;
        } else if (!sku.getId().equals(other.sku.getId()))
            return false;
        return true;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sku == null) ? 0 : sku.hashCode());
        return result;
    }
}
