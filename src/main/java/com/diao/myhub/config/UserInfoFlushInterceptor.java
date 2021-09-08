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
public class UserInfoFlushInterceptor implements HandlerInterceptor {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            // 刷新session
            req.getSession().setAttribute("user", userService.getUserById(user.getId()));
        } else {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        User relUser = userService.selectUserByToken(token);
                        // 找到用户允许访问
                        if (relUser != null) {
                            req.getSession().setAttribute("user", relUser);
                        }
                    }
                }
            }

        }
        return true;
    }
}
