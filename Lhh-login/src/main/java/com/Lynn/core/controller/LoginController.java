package com.Lynn.core.controller;

import com.Lynn.common.utils.RequestUtils;
import com.Lynn.core.bean.user.Buyer;
import com.Lynn.core.service.user.BuyerService;
import com.Lynn.core.service.user.SessionProvider;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 由于各种登录的类，还包括判断用户是否登录
 * Created by FantasmYii on 2018/4/16.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login.aspx", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    //第一个参数是用于给jsonp回传数据的,@ResponseBody ：一般在异步获取数据时使用,使返回的结果不会被解析为跳转路径
    @RequestMapping(value = "/isLogin.aspx")
    public @ResponseBody
    MappingJacksonValue login(String callback,HttpServletResponse response,HttpServletRequest request){
       //默认未登录，所以是0
        Integer result=0;
        String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(response, request));
        System.out.println("username:"+username);
        if(username!=null){
            result=1;
        }
        System.out.println("result:"+result);
        //下面是设置跨域请求的代码，主要返回jsonp的，如果不用跨域请求，原代码中写jsonObject.put
        MappingJacksonValue mjv=new MappingJacksonValue(result);
        mjv.setJsonpFunction(callback);
        return mjv;
    }


    @Autowired
    private BuyerService buyerService;
    @Autowired
    private SessionProvider sessionProvider;

    @RequestMapping(value = "/login.aspx", method = RequestMethod.POST)
    public String login(String username, String password, String returnUrl, HttpServletRequest request, HttpServletResponse response, Model model) {

        //用户名不能为空
        if (username != null) {
            //密码不能为空
            if (password != null) {
                Buyer buyer = buyerService.selectBuyerByUsername(username);
                if (buyer != null) {
                    //页面参数加密，然后和数据库调取的比对
                    if (buyer.getPassword().equals(encodePassword(password))) {
                        //保存用户名到session中
                        sessionProvider.setAttribuerForUsername(RequestUtils.getCSESSIONID(response, request),buyer.getUsername());
                        //跳转回之前访问的页面（重定向）
                        return "redirect:"+returnUrl;
                    } else {

                        model.addAttribute("error", "密码错误");
                    }
                } else {
                    model.addAttribute("error", "用户名不存在");
                }
            } else {
                model.addAttribute("error", "密码不能为空");
            }

        } else {
            model.addAttribute("error", "用户名不能为空");
        }

        return "login";
    }

    public String encodePassword(String password) {
        //加盐
        // password="akjshdaku"+password+"alksjdoia";

        //使用md5将页面传来的参数加密
        String algorithm = "MD5";
        char[] encodeHex = null;

        try {
            //MD5加密
            MessageDigest instance = MessageDigest.getInstance(algorithm);
            //加密后
            byte[] digest = instance.digest(password.getBytes());
            //十六进制
            encodeHex = Hex.encodeHex(digest);
            return new String(encodeHex);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String(encodeHex);
    }

   /* public static void main(String[] args) {
        LoginController l = new LoginController();
        String w = l.encodePassword("123456");
        System.out.println(w);
    }*/
}
