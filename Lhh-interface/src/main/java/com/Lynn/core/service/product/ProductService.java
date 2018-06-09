package com.Lynn.core.service.product;

import cn.itcast.common.page.Pagination;
import com.Lynn.core.bean.product.Color;
import com.Lynn.core.bean.product.Product;

import java.util.List;

/**
 * Created by FantasmYii on 2018/3/27.
 */
public interface ProductService {
    public Pagination selectPaginationByQuery(Integer pageNo, String name
            , Long brandId, Boolean isShow);
    public List<Color> selectColorList();
    //保存商品
    public void insertProduct(Product product);
    //更改商品状态
    public void isShow(Long[] ids);
}
