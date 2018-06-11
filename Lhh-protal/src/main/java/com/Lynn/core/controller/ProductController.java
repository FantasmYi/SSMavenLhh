package com.Lynn.core.controller;

import com.Lynn.core.bean.product.Brand;
import com.Lynn.core.bean.product.Color;
import com.Lynn.core.bean.product.Product;
import com.Lynn.core.bean.product.Sku;
import com.Lynn.core.service.CmsService;
import com.Lynn.core.service.SearchService;
import cn.itcast.common.page.Pagination;
import com.Lynn.core.service.product.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * 前台商品
 *protal模块负责前台页面
 * @author lx
 */
@Controller
public class ProductController {

    //去首页  入口
    @RequestMapping(value = "/")
    public String index() {

        return "index";
    }

    @Autowired
    private SearchService searchService;
    @Autowired
    private BrandService brandService;

    //查询keyword
    @RequestMapping(value = "/search")
    public String search(Integer pageNo, String keyword, Long brandId, String price, Model model) throws Exception {
        List<Brand> brands = brandService.selectBrandListByRedis();
        Pagination pagination = searchService.selectPaginationByQuery(pageNo, keyword, brandId, price);
        model.addAttribute("pagination", pagination);
        System.out.println("看一下pagination："+pagination.getList());
        model.addAttribute("brands", brands);
        model.addAttribute("brandId", brandId);
        model.addAttribute("price", price);
        //已选条件map
        Map<String, String> maps = new HashMap<String, String>();
        //品牌
        if (brandId != null) {
            for (Brand brand : brands) {
                if (brandId == brand.getId()) {
                    maps.put("品牌", brand.getName());
                    break;
                }
            }
        }
        //价格
        if (price != null) {
            if (price.contains("-")) {
                maps.put("价格:", price);
            } else {
                maps.put("价格：", price + "以上");
            }
        }
        model.addAttribute("map", maps);
        return "search";
    }

    @Autowired
    private CmsService cmsService;

    //到商品详情页面
    @RequestMapping(value = "/product/detail")
    public String detail(Long id, Model model) {
        //商品
        Product product = cmsService.selectProductById(id);
        //sku
        List<Sku> skus = cmsService.selectSkuListByProductId(id);
        Set<Color> colors = new HashSet<Color>();
        //遍历一次，相同颜色的不要，
        for (Sku sku:skus) {
          colors.add(sku.getColor());
        }
        model.addAttribute("product", product);
        model.addAttribute("skus", skus);
        model.addAttribute("colors",colors);
        return "product";
    }
}
