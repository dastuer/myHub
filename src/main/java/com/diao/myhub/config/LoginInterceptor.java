package com.diao.myhub.config;

import com.diao.myhub.model.User;
import com.diao.myhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class LoginInterceptor implements HandlerInterceptor {

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    // 配置登录拦截器
    public boolean preHandle(HttpServletRequest req, HttpServletResponse response, Object handler) throws Exception {
        User user = (User)req.getSession().getAttribute("user");
        if (user!=null){
            return true;
        }
        // 既没有session也没有cookie,拦截请求
        response.sendRedirect("/error/loginError");
        return false;
    }

}


