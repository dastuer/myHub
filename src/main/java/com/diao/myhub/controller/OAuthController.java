package com.diao.myhub.controller;

import com.diao.myhub.dto.AccessTokenDTO;
import com.diao.myhub.dto.GiteeUser;
import com.diao.myhub.model.User;
import com.diao.myhub.provider.GiteeProvider;
import com.diao.myhub.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

// 接入gitee控制器
@Controller
@Slf4j
@PropertySource("classpath:private.properties")
public class OAuthController {
    @Autowired
    public void setProvider(GiteeProvider provider) {
        this.provider = provider;
    }
    @Autowired
    public void setAccessTokenDTO(AccessTokenDTO accessTokenDTO) {
        this.accessTokenDTO = accessTokenDTO;
    }
    private GiteeProvider provider;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    private AccessTokenDTO accessTokenDTO;
    private UserService userService;
    @Value("${gitee.client.getOAuth_url}")
    private String oAuUrl;
    @GetMapping("/login")
    public void login(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        String url = oAuUrl;
        resp.sendRedirect(url);
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        req.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        cookie.setPath("/");
        return "redirect:/index";
    }
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 接收返回的code
        accessTokenDTO.setCode(code);
        // 根据返回的code获取token
        String accessToken = provider.getAccessToken(accessTokenDTO);
        // 获取用户数据
        GiteeUser giteeUser = provider.getUser(accessToken);
        // gitee登录成功,得到用户数据
        if (giteeUser!=null){
            // 查询用户是否曾经登陆过
            User user = userService.getUserByAccountId("gitee" + giteeUser.getId());
            String token = UUID.randomUUID().toString().replace("-","");
            // 曾经登录过,更新token
            if (user!=null){
                user.setToken(token);
                userService.updateToken(user.getId(),token);
            }else {
                // 未登录则创建新用户
                user = createUserByGiteeUser(giteeUser);
                user.setToken(token);
                userService.addUser(user);
            }
            // 无论是否登录过,都会创建cookie和session
            req.getSession().setAttribute("user",user);
            Cookie clientToken = new Cookie("token", token);
            clientToken.setMaxAge(2_592_000);
            clientToken.setPath("/");
            resp.addCookie(clientToken);
        }else {log.error("gitee错误,获取用户信息失败{}",giteeUser);}
        return "redirect:/index";
    }
    // 通过gitee用户创建本地用户
    private User createUserByGiteeUser(GiteeUser giteeUser){
        User user = new User();
        user.setName(giteeUser.getName());
        user.setAccountId("gitee"+ giteeUser.getId());
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModify(user.getGmtCreate());
        user.setBio(giteeUser.getBio());
        user.setAvatarUrl(giteeUser.getAvatar_url());
        return user;
    }
}
