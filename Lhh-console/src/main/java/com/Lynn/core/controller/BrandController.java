package com.Lynn.core.controller;


import cn.itcast.common.page.Pagination;
import com.Lynn.core.bean.product.Brand;
import com.Lynn.core.service.product.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 品牌
 * Created by FantasmYii on 2018/3/27.
 */
@Controller

public class BrandController {

	@Autowired
	private BrandService brandService;

	//查询
	@RequestMapping(value = "/brand/list.do")
	public String list(String name,Integer isDisplay,Integer pageNo,Model model){
		Pagination pagination=brandService.selectPaginationByQuery(name,isDisplay,pageNo);
		model.addAttribute("pagination",pagination);
		model.addAttribute("name",name);
		if(isDisplay!=null){
			model.addAttribute("isDislpay",isDisplay);
		}else {
			model.addAttribute("isDisplay",1);
		}

		return "brand/list";
	}

	@RequestMapping(value = "/brand/toEdit.do")
	public String edit(Long id,Model model){
		System.out.println("id:"+id);
		Brand brand=brandService.selectBrandById(id);
		model.addAttribute("brand",brand);
		return "brand/edit";
	}
	@RequestMapping(value = "/brand/edit.do")
	public String edit(Brand brand){
        brandService.updateBrandById(brand);
		return "redirect:/brand/list.do";
	}
	@RequestMapping(value = "/brand/delete.do")
	public String deletes(Long[] ids){
		brandService.deletes(ids);
		return "forward:/brand/list.do";
	}

	
}
