package com.Lynn.core.controller;

import cn.itcast.common.page.Pagination;
import com.Lynn.core.bean.product.Brand;
import com.Lynn.core.bean.product.Color;
import com.Lynn.core.bean.product.Product;
import com.Lynn.core.service.product.BrandService;
import com.Lynn.core.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 商品
 * Created by FantasmYii on 2018/3/27.
 */
@Controller
public class ProductController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;

    @RequestMapping(value="/product/list.do")
    public String list(Integer pageNo,String name,Long brandId,Boolean isShow,Model model){
        //结果集

        List<Brand> brands= brandService.selectBrandListByQuery(1);
        model.addAttribute("brands",brands);
        //分页
        Pagination pagination=productService.selectPaginationByQuery(pageNo,name,brandId,isShow);
        model.addAttribute("pagination",pagination);
        System.out.println("pagination:"+pagination.getList());
        model.addAttribute("name",name);
        model.addAttribute("brandId",brandId);
        if(isShow!=null){
            model.addAttribute("isShow",isShow);
        }else {
            model.addAttribute("isShow",false);
        }

        return "product/list";
    }
    @RequestMapping(value = "product/toAdd.do")
    public String toAdd(Model model){
        //结果集
        List<Brand> brands= brandService.selectBrandListByQuery(1);
        model.addAttribute("brands",brands);
        //颜色
        List<Color> color= productService.selectColorList();
        model.addAttribute("color",color);
        return "product/add";
    }
    @RequestMapping(value = "product/add.do")
    public String add(Product product){
        System.out.println("进入到add中");
        productService.insertProduct(product);
        return "redirect:/product/list.do";
    }
    @RequestMapping(value = "/product/isShow.do")
    public String isShow(Long[] ids){
        productService.isShow(ids);
        return "forward:/product/list.do";
    }
}
