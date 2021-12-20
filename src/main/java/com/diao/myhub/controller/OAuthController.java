package com.diao.myhub.controller;

import com.diao.myhub.dto.AccessTokenDTO;
import com.diao.myhub.dto.GiteeUser;
import com.diao.myhub.model.User;
import com.diao.myhub.service.UserService;
import com.diao.myhub.strategy.DTO.LoginUser;
import com.diao.myhub.strategy.UserLoginFactory;
import com.diao.myhub.strategy.UserStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Huah
 */ // 接入gitee控制器
@Controller
@Slf4j
@PropertySource("classpath:private.properties")
public class OAuthController {
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    private UserLoginFactory loginFactory;
    private UserService userService;
    @Value("${gitee.client.getOAuth_url}")
    private String oAuUrl;

    @GetMapping("/logout")
    public String logout(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        req.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        cookie.setPath("/");
        return "redirect:/index";
    }
    @GetMapping("/login/{type}")
    public void login(@PathVariable String type,
                      HttpServletRequest req,
                      HttpServletResponse resp) throws IOException {
        req.getSession().setAttribute("loginType",type);
        UserStrategy loginStrategy = loginFactory.getLoginStrategy(type);
        String redirectUri = loginStrategy.getRedirectUri();
        resp.sendRedirect(redirectUri);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           HttpServletRequest req,
                           HttpServletResponse resp) throws Exception {
        String type = (String) req.getSession().getAttribute("loginType");
        if (Objects.isNull(type)){
            type = "gitee";
        }
        AccessTokenDTO tokenDTO = new AccessTokenDTO();
        tokenDTO.setCode(code);
        UserStrategy loginStrategy = loginFactory.getLoginStrategy(type);
        LoginUser loginUser = loginStrategy.getLoginUser(tokenDTO);
        if (loginUser!=null){
            // 查询用户是否曾经登陆过
            User user = userService.getUserByAccountId(type + loginUser.getId());
            String token = UUID.randomUUID().toString().replace("-","");
            // 曾经登录过,更新token
            if (user!=null){
                user.setToken(token);
                userService.updateToken(user.getId(),token);
            }else {
                // 未登录则创建新用户
                user = createUserByLoginUser(type,loginUser);
                user.setToken(token);
                userService.addUser(user);
            }
            // 无论是否登录过,都会创建cookie和session
            req.getSession().setAttribute("user",user);
            Cookie clientToken = new Cookie("token", token);
            clientToken.setMaxAge(2_592_000);
            clientToken.setPath("/");
            resp.addCookie(clientToken);
        }else {log.info("获取用户信息失败");}
        return "redirect:/index";
    }

    private User createUserByLoginUser(String type,LoginUser loginUser){
        User user = new User();
        user.setName(loginUser.getName());
        user.setAccountId(type+loginUser.getId());
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModify(user.getGmtCreate());
        user.setBio(loginUser.getBio());
        user.setAvatarUrl(loginUser.getAvatar_url());
        return user;
    }
}
