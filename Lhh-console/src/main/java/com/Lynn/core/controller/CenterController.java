package com.Lynn.core.controller;


import com.Lynn.core.bean.TestTb;
import com.Lynn.core.service.TestTbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * 主页面
 * Created by FantasmYii on 2018/3/27.
 */
@Controller
@RequestMapping(value="/controller")
public class CenterController {

	/*@Autowired
	private TestTbService testTbService;*/


	@RequestMapping(value = "/index.do")
	public String index(Model model){

		return "index";
	}
	@RequestMapping(value = "/top.do")
	public String top(Model model){

		return "top";
	}
	@RequestMapping(value = "/main.do")
	public String main(Model model){

		return "main";
	}
	@RequestMapping(value = "/right.do")
	public String right(Model model){

		return "right";
	}
	@RequestMapping(value = "/left.do")
	public String left(Model model){

		return "left";
	}
	//商品身体
	@RequestMapping(value = "/frame/product_main.do")
	public String product_main(Model model){

		return "frame/product_main";
	}
	//商品左部
	@RequestMapping(value = "/frame/product_left.do")
	public String product_left(){
		return "frame/product_left";
	}
	
	
}
