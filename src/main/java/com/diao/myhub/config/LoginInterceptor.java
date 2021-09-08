package com.diao.myhub.config;

import com.diao.myhub.exception.CustomizeError;
import com.diao.myhub.exception.CustomizeException;
import com.diao.myhub.model.User;
import com.diao.myhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Morty
 */
@Configuration
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    public void setUserService(UserService userService) {
    }
    // 配置登录拦截器
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse response, Object handler) throws Exception {
        User user = (User)req.getSession().getAttribute("user");
        if (user!=null){
            return true;
        }
        response.sendRedirect("/loginError");
        return false;
    }

}


