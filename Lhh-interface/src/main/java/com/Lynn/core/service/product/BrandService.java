package com.Lynn.core.service.product;

import cn.itcast.common.page.Pagination;
import com.Lynn.core.bean.product.Brand;

import java.util.List;

/**
 * Created by FantasmYii on 2018/3/21.
 */
public interface BrandService {
    //查询分页对象
    public Pagination selectPaginationByQuery(String name, Integer isDisplay, Integer pageNo);
    //通过ID查询品牌
    public Brand selectBrandById(Long id);
    //通过id修改商品信息
    public void updateBrandById(Brand brand);
    //批量删除
    public void deletes(Long[] ids);
    //查询结果集
    public List<Brand> selectBrandListByQuery(Integer isDisplay);
    //查询redis中的品牌
    public List<Brand> selectBrandListByRedis();
}
