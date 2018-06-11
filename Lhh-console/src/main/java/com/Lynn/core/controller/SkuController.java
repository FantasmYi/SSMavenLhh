package com.Lynn.core.controller;

import com.Lynn.core.bean.product.Sku;
import com.Lynn.core.service.product.SkuService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by FantasmYii on 2018/3/31.
 */
@Controller
public class SkuController {
    @Autowired
    private SkuService skuService;
    @RequestMapping(value = "/sku/list.do")
    public String selectSkuByProductId(Long productId, Model model){
        List<Sku> skus= skuService.selectSkuListByProductId(productId);
        model.addAttribute("skus",skus);
        return "sku/list";
    }
    @RequestMapping(value = "/sku/addSku.do")
    public void updateSkuById(Sku skuId,HttpServletResponse response) throws IOException {
       skuService.updateSkuById(skuId);
        JSONObject jo=new JSONObject();
        jo.put("message","保存成功");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jo.toString());
    }
}
