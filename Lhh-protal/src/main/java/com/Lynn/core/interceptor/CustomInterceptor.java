package com.Lynn.core.interceptor;

import com.Lynn.common.utils.RequestUtils;
import com.Lynn.core.service.user.SessionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截的是controller之前的 springmvc handle处理器
 * Created by FantasmYii on 2018/4/23.
 */
public class CustomInterceptor implements HandlerInterceptor {
    @Autowired
    private SessionProvider sessionProvider;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username=sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(response, request));
        if(username==null){
            //未登录
            //重定向回登录页面
            response.sendRedirect("http://localhost:8085/login.aspx?returnUrl=http://localhost:8082/toCart");
            return false;
        }
        //放行true,不放行false
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
