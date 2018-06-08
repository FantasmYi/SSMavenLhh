package com.Lynn.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 获取CSESSIONID,可以用这个类取用户名
 * Created by FantasmYii on 2018/4/17.
 */
public class RequestUtils {
    public static String getCSESSIONID(HttpServletResponse response, HttpServletRequest request) {
        //取出cookie
        Cookie[] cookies = request.getCookies();
        //有cookie直接取出
        if (cookies != null && cookies.length > 0) {
            //判断cookie中是否有CSESSIONID
            for (Cookie cookie : cookies) {
                if ("CSESSIONID".equals(cookie.getName())) {
                    System.out.println("cookir.getValue:" + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        //没有创建一个CSESSIONID 并保存到cookie中，同时，把cookie写回浏览器，用这个cookie的CSESSIONID
        String CSESSIONID = UUID.randomUUID().toString().replaceAll("-", "");
        Cookie cookie = new Cookie("CSESSIONID", CSESSIONID);
        //设置存活时间  -1 0 1 -1代表关闭浏览器则cookie消失
        cookie.setMaxAge(-1);
        //设置路径
        cookie.setPath("/");
        //设置跨域
        response.addCookie(cookie);
        return CSESSIONID;
    }

}
