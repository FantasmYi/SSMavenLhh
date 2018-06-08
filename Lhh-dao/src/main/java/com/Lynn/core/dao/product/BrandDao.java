package com.Lynn.core.dao.product;

import com.Lynn.core.bean.product.Brand;
import com.Lynn.core.bean.product.BrandQuery;

import java.util.List;

/**
 * Created by FantasmYii on 2018/3/21.
 */
public interface BrandDao {
    //查询结果集
    public List<Brand> selectBrandListByQuery(BrandQuery brandQuery);
    //查询总条数
    public Integer selectCount(BrandQuery brandQuery);
    //通过ID查询品牌
    public Brand selectBrandById(Long id);
    //根据id提交修改
    public void updateBrandById(Brand brand);
    //批量删除
    public void deletes(Long[] ids);
}
